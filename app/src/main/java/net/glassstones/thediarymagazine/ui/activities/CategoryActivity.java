package net.glassstones.thediarymagazine.ui.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsCluster;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
    NewsFlipAdapter mAdapter;
    int skip = 1;
    TDMAPIClient client;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;

    private Subscription postsSubscription;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int category = getIntent().getIntExtra("cat", -1);

//        mAdapter = new CategoryAdapter(this);

        List<NewsCluster> cluster = Common.getNewsCluster(Realm.getDefaultInstance(), category);

        mAdapter = new NewsFlipAdapter(this, cluster);
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);

        ServiceGenerator sg = new ServiceGenerator((Common) getApplication());

        client = sg.createService(TDMAPIClient.class);

        List<NI> rPosts = new ArrayList<>();

        Observable<ArrayList<NI>> postsObserver = client.getPostsObservableByCategory(category,
                25, skip);

        postsSubscription = postsObserver
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(ni -> {
                    RealmUtils utils = new RealmUtils(Realm.getDefaultInstance());
                    utils.NI2Post(ni, null);
                })
                .subscribe();
    }

    @Override
    protected void onStop () {
        super.onStop();
        postsSubscription.unsubscribe();
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

    }
}
