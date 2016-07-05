package net.glassstones.thediarymagazine.ui.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.Categories;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.ui.adapters.FlipAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

public class CategoryActivity extends AppCompatActivity implements
        FlipView.OnFlipListener,
        FlipView.OnOverFlipListener, Callback {
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
    private Subscription postsSubscription;
    Realm realm;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        category = getIntent().getIntExtra("cat", -1);

        List<Post> posts = realm.where(Post.class).equalTo("categories.id", category)
                .findAllSorted(Post.CREATED_AT, Sort.DESCENDING);

        mAdapter = new FlipAdapter(this, posts);
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);

        ServiceGenerator sg = new ServiceGenerator((Common) getApplication());

        client = sg.createService(TDMAPIClient.class);

        Observable<ArrayList<NI>> postsObserver = client.getPostsObservableByCategory(category,
                25, skip + posts.size());

        postsSubscription = postsObserver
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(ni -> {
                    Call<WPMedia> getMedia = client.getMedia(ni.getFeatured_media());
                    getMedia.enqueue(new retrofit2.Callback<WPMedia>() {
                        @Override
                        public void onResponse (Call<WPMedia> call, Response<WPMedia> response) {
                            saveToRealm(response, ni);
                        }

                        @Override
                        public void onFailure (Call<WPMedia> call, Throwable t) {
                            Snackbar.make(list, "The thing failed", Snackbar
                                    .LENGTH_LONG).show();
                        }
                    });
                })
                .subscribe();
    }

    private void saveToRealm (Response<WPMedia> response, NI ni) {
        ni.setMedia(response.body());
        Realm r = Realm.getDefaultInstance();
        Post p = r.where(Post.class).equalTo(Post.ID, ni.getId()).findFirst();

        if (p == null) {
            r.executeTransaction(realm -> {
                Post p1 = realm.createObject(Post.class);
                RealmList<Categories> categories = new RealmList<>();
                for (Integer i : ni.getCategories()) {
                    if (realm.where(Categories.class).equalTo("id", i).count
                            () < 1) {
                        Categories cat = realm.createObject(Categories.class);
                        cat.setId(i);
                        categories.add(cat);
                    } else {
                        Categories cat = realm.where(Categories.class)
                                .equalTo("id", i).findFirst();
                        categories.add(cat);
                    }
                }
                p1.setId(ni.getId());
                p1.setCreated_at(ni.getCreated_at());
                p1.setSlug(ni.getSlug());
                p1.setType(ni.getType());
                p1.setLink(ni.getLink());
                p1.setTitle(ni.getTitle().getTitle());
                p1.setContent(ni.getContent().getContent());
                p1.setExcerpt(ni.getExcerpt().getExcerpt());
                p1.setAuthorId(ni.getAuthorId());
                p1.setFeatured_media(ni.getFeatured_media());
                p1.setMediaId(ni.getMedia().getId());
                p1.setMedia_type(ni.getMedia().getMedia_type());
                p1.setMime_type(ni.getMedia().getMime_type());
                p1.setSource_url(ni.getMedia().getSourceUrl());
                p1.setMediaSaved(true);
                p1.setCategories(categories);
            });
        }
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
