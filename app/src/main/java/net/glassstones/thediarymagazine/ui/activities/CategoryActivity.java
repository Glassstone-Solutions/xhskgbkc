package net.glassstones.thediarymagazine.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.Constants;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.ui.adapters.FlipAdapter;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.utils.HelperSharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.kaush.core.util.CoreNullnessUtils;
import retrofit2.Call;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

public class CategoryActivity extends AppCompatActivity implements
        FlipView.OnFlipListener,
        FlipView.OnOverFlipListener, Callback {
    private static final String TAG = CategoryActivity.class.getSimpleName();
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.list)
    FlipView list;
    FlipAdapter mAdapter;
    int skip = 1;
    TDMAPIClient client;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    int category;
    Call<WPMedia> getMedia;
    List<NI> posts;

    CustomTextView mLabel;

    CompositeSubscription subscriptions;
    Subscription moreSub;

    Context context;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLabel = (CustomTextView) toolbar.findViewById(R.id.title);
        mLabel.setText(CoreNullnessUtils.isNotNullOrEmpty(Constants.CATEGOIRES[category])
                ? Constants.CATEGOIRES[category] : "News");

        subscriptions = new CompositeSubscription();

        context = this;

        category = getIntent().getIntExtra("cat", -1);

        posts = new ArrayList<>();

        mAdapter = new FlipAdapter(this, posts);
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);

        ServiceGenerator sg = new ServiceGenerator((Common) getApplication());

        client = sg.createService(TDMAPIClient.class);

        Observable<List<NI>> postsObserver = client.getPostsObservableByCategory(category,
                25, skip + posts.size(), null).flatMap(Observable::from)
                .flatMap(this::getPairObservable)
                .flatMap(this::getObservable)
                .toList();

        if (CoreNullnessUtils.isNullOrEmpty(getPostsListFromSP(NI.POST_LIST_PARCEL_KEY+"_"+category))) {
            Subscription postsSubscription = getSubscription(postsObserver);
            subscriptions.add(postsSubscription);
        } else {
            if (progress.getVisibility() == View.VISIBLE) {
                progress.setVisibility(View.GONE);
            }
            posts = getPostsListFromSP(NI.POST_LIST_PARCEL_KEY+"_"+category);
            mAdapter.update(posts);
        }

    }

    private Subscription getSubscription (Observable<List<NI>> postsObservable) {
        return postsObservable
                .retry()
                .distinct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NI>>() {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable e) {
                        Log.e(TAG, "Darn! App crapped");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext (List<NI> nis) {
                        posts.clear();
                        posts = nis;
                        if (posts.size() > 0 && progress.getVisibility() == View.VISIBLE) {
                            progress.setVisibility(View.GONE);
                        }
                        HelperSharedPreferences.putSharedPreferencesString(context,
                                NI.POST_LIST_PARCEL_KEY+"_"+category, getJsonString(posts));
                        mAdapter.update(posts);
                    }
                });
    }

    private Observer<NI> observePost(){
        return new Observer<NI>() {
            @Override
            public void onCompleted () {
                Log.e(TAG, "Done!");
            }

            @Override
            public void onError (Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext (NI post) {
                Log.e(TAG, post.getTitle().title());
            }
        };
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (getMedia != null) {
            getMedia.cancel();
        }
        subscriptions.unsubscribe();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ButterKnife.reset(this);
    }


    @Override
    public void onFlippedToPage (FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip (FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }

    @Override
    public void onPageRequested (NewsItem newsItem) {
        Intent i = new Intent(this, NewsDetailsActivity.class);
        i.putExtra("postBundle", newsItem.getNi());
        startActivity(i);
    }

    @Override
    public void onMoreRequest (int offset) {
        if (moreSub != null && !moreSub.isUnsubscribed()){
            moreSub.isUnsubscribed();
        }

        moreSub = client.getPostsObservableByCategory(category,
                25, skip + posts.size(), null)
                .flatMap(Observable::from)
                .flatMap(this::getPairObservable)
                .flatMap(this::getObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
        subscriptions.add(moreSub);
    }

    @Override
    public void onShowAd () {

    }

    @NonNull
    private Observable<? extends NI> getObservable (Pair<NI, WPMedia> pair) {
        NI post = pair.first;
        post.setMedia(pair.second);
        return Observable.just(post);
    }

    @NonNull
    private Observer<NI> getObserver () {
        return new Observer<NI>() {
            @Override
            public void onCompleted () {
                Log.e(TAG, "Done!");
            }

            @Override
            public void onError (Throwable e) {
                Log.e(TAG, "Darn! App crapped");
                e.printStackTrace();
            }

            @Override
            public void onNext (NI ni) {
                posts.add(ni);
                HelperSharedPreferences.putSharedPreferencesString(context,
                        NI.POST_LIST_PARCEL_KEY+"_"+category, getJsonString(posts));
                mAdapter.add();
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    @NonNull
    private Observable<? extends Pair<NI, WPMedia>> getPairObservable (NI ni) {
        Observable<WPMedia> media = client.getMediaObservable(ni.getFeatured_media());
        return Observable.zip(Observable.just(ni), media, Pair::new);
    }

    private String getJsonString(List<? extends NI> posts){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NI>>() {
        }.getType();
        return gson.toJson(posts, listType);
    }

    private List<NI> getPostsListFromSP(String key) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NI>>() {
        }.getType();
        return gson.fromJson(HelperSharedPreferences.getSharedPreferencesString(this, key, "[]"), listType);
    }

}
