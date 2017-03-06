package net.glassstones.thediarymagazine.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.common.BaseNewsFragment;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsCluster;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.network.models.PostEvent;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.ui.activities.NewsDetailsActivity;
import net.glassstones.thediarymagazine.ui.adapters.FlipAdapter;
import net.glassstones.thediarymagazine.ui.adapters.TabFlipAdapter;
import net.glassstones.thediarymagazine.utils.ObservableUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.kaush.core.util.CoreNullnessUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

import static net.glassstones.thediarymagazine.utils.Error.exponentialBackoffForExceptions;

/**
 * A simple {@link BaseNewsFragment} subclass.
 */
public class NewsFragment extends BaseNewsFragment implements Callback,
        FlipView.OnFlipListener, FlipView.OnOverFlipListener {

    @InjectView(R.id.list)
    FlipView list;

    int state;

    FlipAdapter mAdapter;

    TabFlipAdapter mTabAdapter;

    List<NI> posts;
    List<NI> _posts;
    List<NewsCluster> clusters;
    TDMAPIClient client;

    ObservableUtil observableUtil;

    boolean isRefreshing = false;
    @InjectView(R.id.retryBtn)
    Button retryBtn;
    @InjectView(R.id.retryWrap)
    LinearLayout retryWrap;

    private boolean isTablet;

    CompositeSubscription subscriptions;
    Subscription moreSub, refreshSub;
    @InjectView(R.id.progress)
    ProgressBar progress;

    private NewsFeedFragmentInteraction callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (NewsFeedFragmentInteraction) context;
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = ServiceGenerator.createGithubService();
        posts = new ArrayList<>();
        _posts = new ArrayList<>();
        clusters = new ArrayList<>();
        isTablet = getActivity().getResources().getBoolean(R.bool.isTablet);
        subscriptions = new CompositeSubscription();
        observableUtil = new ObservableUtil(client);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, view);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);

        retryBtn.setOnClickListener(v -> {
            Observable<List<NI>> postsObservable = observableUtil.GetListObservable(CoreNullnessUtils.isNotNullOrEmpty(posts) ? posts.size()
                    : 25, null, 1, null);
            if (subscriptions != null && !subscriptions.isUnsubscribed()) {
                if (subscriptions.hasSubscriptions()){
                    subscriptions.clear();
                }
                subscriptions.add(getSubscription(postsObservable));

                progress.setVisibility(View.VISIBLE);
                retryWrap.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isTablet) {
            mAdapter = new FlipAdapter(getActivity(), posts);
            mAdapter.setCallback(this);
            list.setAdapter(mAdapter);
        } else {
            clusters = Common.getNICluster(posts);
            mTabAdapter = new TabFlipAdapter(getActivity(), clusters);
            mTabAdapter.setCallback(this);
            list.setAdapter(mTabAdapter);
        }
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        assert posts != null;
        progress.setVisibility(posts.size() > 0 ?View.GONE : View.VISIBLE);

        Observable<List<NI>> postsObservable = observableUtil.GetListObservable(CoreNullnessUtils.isNotNullOrEmpty(posts) ? posts.size()
                : 25, null, 1, null);

        Subscription postSub = getSubscription(postsObservable);
        subscriptions.add(postSub);
    }

    private Subscription getSubscription(Observable<List<NI>> postsObservable) {
        return observableUtil.GetSubscription(postsObservable, postObserver());
    }

    private Observer<List<NI>> postObserver() {
        return new Observer<List<NI>>() {
            @Override
            public void onCompleted() {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Darn! App crapped");
                progress.setVisibility(View.GONE);
                retryWrap.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }

            @Override
            public void onNext(List<NI> nis) {
                if (retryWrap.getVisibility() == View.VISIBLE){
                    retryWrap.setVisibility(View.GONE);
                }
                progress.setVisibility(View.GONE);
                posts.clear();
                posts = nis;
                if (!isTablet) {
                    mAdapter.update(posts);
                } else {
                    clusters = Common.getNICluster(posts);
                    mTabAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostEvent(PostEvent event) {
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        subscriptions.unsubscribe();
    }

    @Override
    public void onPageRequested(NewsItem newsItem) {
        Intent i = new Intent(getActivity(), NewsDetailsActivity.class);
        i.putExtra("postBundle", newsItem.getNi());
        getActivity().startActivity(i);
    }

    @Override
    public void onMoreRequest(int offset) {

        int retryCount = 3;
        int initialDelay = 3;

        if (moreSub != null && !moreSub.isUnsubscribed()) {
            moreSub.isUnsubscribed();
        }

        moreSub = client.getObservablePosts(25, offset, 1, null)
                .retryWhen(exponentialBackoffForExceptions(initialDelay, retryCount, TimeUnit.SECONDS, IOException.class))
                .flatMap(Observable::from)
                .flatMap(this::getPairObservable)
                .filter(p -> p.second != null)
                .flatMap(this::getObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NI>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "Done!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Darn! App crapped");
                        e.printStackTrace();
                        mAdapter.setShouldRequestMore(true);
                    }

                    @Override
                    public void onNext(NI ni) {
                        setNi(ni);
                    }
                });
        subscriptions.add(moreSub);
    }

    @Override
    public void onShowAd() {
        callback.showAd();
    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float
            overFlipDistance, float flipDistancePerPage) {
        if (!isRefreshing) {
            doRefresh(overFlippingPrevious);
        }
    }

    private void doRefresh(boolean overFlippingPrevious) {
        if (refreshSub != null && !refreshSub.isUnsubscribed())
            refreshSub.isUnsubscribed();
        if (mAdapter.getCount() == posts.size() && mAdapter.getItemPosition() == mAdapter.getCount()) {
            refreshSub = Observable.just(overFlippingPrevious)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .take(1)
                    .filter(direction -> false)
                    .flatMap(direction -> client.getObservablePosts(25, mAdapter.getCount(), 1, null))
                    .flatMap(Observable::from)
                    .flatMap(this::getPairObservable)
                    .filter(p -> p.second != null)
                    .flatMap(this::getObservable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NI>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mAdapter.setShouldRequestMore(true);
                        }

                        @Override
                        public void onNext(NI ni) {
                            setNi(ni);
                        }
                    });
            subscriptions.add(refreshSub);
        }
    }

    private void setNi(NI ni) {
        posts.add(ni);
        if (!isTablet) {
            mAdapter.add();
            mAdapter.notifyDataSetChanged();
        } else {
            _posts.add(ni);
            Random rand = new Random();
            int currentCount = rand.nextInt(3) + 1;
            if (_posts.size() == currentCount && _posts.size() == 3) {
                for (NewsCluster nc : Common.getNICluster(_posts)) {
                    clusters.add(nc);
                }
                mTabAdapter.notifyDataSetChanged();
                _posts.clear();
            }
        }
    }

    @Override
    public Class clazz() {
        return this.getClass();
    }

    @NonNull
    private Observable<NI> getObservable(Pair<NI, WPMedia> pair) {
        NI post = pair.first;
        if (pair.second != null) {
            post.setMedia(pair.second);
        }
        return Observable.just(post);
    }

    @NonNull
    private Observable<Pair<NI, WPMedia>> getPairObservable(NI ni) {
        Observable<WPMedia> media = client.getMediaObservable(ni.getFeatured_media()).onErrorReturn(e -> {
            e.printStackTrace();
            return null;
        });
        return Observable.zip(Observable.just(ni), media, Pair::new);
    }


    public interface NewsFeedFragmentInteraction {
        void showAd();
    }


}
