package net.glassstones.thediarymagazine.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.common.BaseActivity;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.News;
import net.glassstones.thediarymagazine.network.models.WPAuthor;
import net.glassstones.thediarymagazine.ui.adapters.NewsDetailAdapter;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;
import net.glassstones.thediarymagazine.utils.HelperSharedPreferences;
import net.glassstones.thediarymagazine.utils.ObservableUtil;
import net.glassstones.thediarymagazine.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;

import butterknife.InjectView;
import co.kaush.core.util.CoreNullnessUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


// TODO Refactor to clean up code
@DeepLink({"tdm://posts/{id}", "http://www.thediarymagazine.com/{slug}"})
public class NewsDetailsActivity extends BaseActivity {

    final int myWidth = UIUtils.getScreenWidth(this);
    final int myHeight = 384;
    @InjectView(R.id.root)
    CoordinatorLayout mRoot;
    @InjectView(R.id.header)
    TopAlignedImageView header;
    @InjectView(R.id.anim_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    //    @InjectView(R.id.web_view)
//    NestedWebView webView;
    ServiceGenerator sg;

    TDMAPIClient client;

    ObservableUtil observableUtil;

    private Window window;
    private NI post;
    @InjectView(R.id.news)
    RecyclerView newsView;

    @Override
    public Class clazz() {
        return this.getClass();
    }

    @Override
    public int resourceId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common app = (Common) getApplication();
        assert getSupportActionBar() != null;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        sg = new ServiceGenerator(app);

        client = sg.createService(TDMAPIClient.class);
        observableUtil = new ObservableUtil(client);
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();
            Log.d(TAG, "Deeplink params: " + parameters);

            String idString = parameters.getString("id");

            Observable<NI> p = observableUtil.GetPost(idString);
            observableUtil.GetPostSubscription(p, new Observer<NI>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    showToast("no deep link :( ");
                }

                @Override
                public void onNext(NI ni) {
                    post = ni;
                    initPost(ni);
                }
            });

        } else if (getIntent().getParcelableExtra("postBundle") != null) {
            post = getIntent().getParcelableExtra("postBundle");
            initPost(post);
        } else {
            showToast("no deep link :( ");
        }
    }

    private void initPost(NI post) {
        Observable<WPAuthor> authorObservable = observableUtil.getAuthor(post);
        authorObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WPAuthor>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WPAuthor author) {
                        getPost(author, post);
                    }
                });


    }

    private void getPost(WPAuthor author, NI post) {
        PostParser postParser = new PostParser().invoke(post.getContent().getContent(), "td-header-wrap", "p");
        Element b = postParser.getB();
        Elements elements = postParser.getElements();

        setNewsFromParcel(b, elements, post, author);
        String source = post.getMedia().getSourceUrl();
        if (CoreNullnessUtils.isNotNullOrEmpty(source)) {
            Glide.with(getApplicationContext())
                    .load(source)
                    .asBitmap()
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            Palette.from(resource).generate(palette -> setupImage(palette, resource));
                        }
                    });
        }
    }

    private void setNewsFromParcel(Element b, Elements elements, NI post, WPAuthor author) {
        News news = null;

        try {
            news = new News().title(post.getTitle().title())
                    .author(author.getName())
                    .date(UIUtils.getTimeAgo(post.getCreated_at()))
                    .firstParagraph(b)
                    .otherParagraphs(elements);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        NewsDetailAdapter adapter = new NewsDetailAdapter(this, news);

        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsView.setAdapter(adapter);
    }

    private void showToast(String s) {
        Snackbar.make(mRoot, s, Snackbar.LENGTH_LONG).show();
    }

    private void setupImage(Palette palette, Bitmap image) {
        int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setContentScrimColor(mutedColor);
        changeStatusBar(mutedColor);
        header.setImageBitmap(image);
    }

    private void changeStatusBar(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(statusBarColor);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (post != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, post.getTitle().title() + "\n\n" + post.getLink());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share this via"));
            }
            return true;
        }
        if (id == R.id.action_fav) {
            List<NI> posts = HelperSharedPreferences.getPostsListFromSP(this, NI.POST_FAV_LIST_PARCEL_KEY);
            posts.add(0, post);
            HelperSharedPreferences.putSharedPreferencesString(this,
                    NI.POST_FAV_LIST_PARCEL_KEY, HelperSharedPreferences.getJsonString(posts));
            Snackbar.make(newsView, "Added to favorite", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class PostParser {
        private Element b;
        private Elements elements;

        Element getB() {
            return b;
        }

        Elements getElements() {
            return elements;
        }

        PostParser invoke(String content, String firstElement, String tag) {
            Document t = Jsoup.parse(content);

            b = t.getElementsByClass(firstElement).first();

            elements = t.select(tag);
            return this;
        }
    }
}
