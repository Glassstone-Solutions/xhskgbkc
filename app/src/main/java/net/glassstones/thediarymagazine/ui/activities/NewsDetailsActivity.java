package net.glassstones.thediarymagazine.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.ui.adapters.NewsDetailAdapter;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;
import net.glassstones.thediarymagazine.utils.RealmUtils;
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
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO Refactor to clean up code
@DeepLink({"tdm://posts/{id}", "http://www.thediarymagazine.com/{slug}"})
public class NewsDetailsActivity extends BaseActivity implements RealmUtils.RealmInterface {

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

    private Window window;
    private Post post;
    private RealmUtils realmUtils;
    @InjectView(R.id.news)
    RecyclerView newsView;

    @Override
    public Class clazz () {
        return this.getClass();
    }

    @Override
    public int resourceId () {
        return R.layout.activity_news_details;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
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

        Realm realm = Realm.getDefaultInstance();

        realmUtils = new RealmUtils(realm, this);

        sg = new ServiceGenerator(app);

        client = sg.createService(TDMAPIClient.class);
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();
            Log.d(TAG, "Deeplink params: " + parameters);

            String idString = parameters.getString("id");
            String name = parameters.getString("slug");

            if (!TextUtils.isEmpty(idString)) {
                // TODO: Replace with RxJava
                Call<NI> getPost = client.getPost(Integer.parseInt(idString));
                getPost.enqueue(new Callback<NI>() {
                    @Override
                    public void onResponse (Call<NI> call, Response<NI> response) {
                        if (response.isSuccessful()) {
                            NI post = response.body();
                            initPost(realmUtils.NI2Post(post, null));
                        } else {
                            NavUtils.navigateUpFromSameTask(NewsDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onFailure (Call<NI> call, Throwable t) {
                        handleFailure(t);
                    }
                });
                showToast("class id== " + idString + " and name==" + name);
            } else if (!TextUtils.isEmpty(name)) {
                Call<NI> slugPost = client.getPostFromSlug(name);
                slugPost.enqueue(new Callback<NI>() {
                    @Override
                    public void onResponse (Call<NI> call, Response<NI> response) {
                        if (response.isSuccessful()) {
                            NI post = response.body();
                            initPost(realmUtils.NI2Post(post, null));
                        } else {
                            NavUtils.navigateUpFromSameTask(NewsDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onFailure (Call<NI> call, Throwable t) {
                        handleFailure(t);
                    }
                });
                showToast("name==" + name);
            }
        } else if (getIntent().getIntExtra("post_id", -1) != -1) {
            post = realmUtils.getPost("id", getIntent().getIntExtra("post_id", -1));
            initPost(post);
        } else if (getIntent().getParcelableExtra("postBundle") != null) {
            NI post = getIntent().getParcelableExtra("postBundle");
            initPost(post);
        } else {
            showToast("no deep link :( ");
        }
    }

    private void initPost (NI post) {
        PostParser postParser = new PostParser().invoke(post.getContent().getContent(), "td-header-wrap", "p");
        Element b = postParser.getB();
        Elements elements = postParser.getElements();

        setNewsFromParcel(b, elements, post);
        String source = post.getMedia().getSourceUrl();
        if (CoreNullnessUtils.isNotNullOrEmpty(source)) {
            Glide.with(getApplicationContext())
                    .load(source)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                        @Override
                        public void onResourceReady (Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            Palette.from(resource).generate(palette -> setupImage(palette, resource));
                        }
                    });
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initPost (Post body) {

        post = body;

        PostParser postParser = new PostParser().invoke(post.getContent(), "td-header-wrap", "p");
        Element b = postParser.getB();
        Elements elements = postParser.getElements();

        setNews(b, elements);

        if (!post.isMediaSaved()) {
            Call<WPMedia> media = client.getMedia(body.getFeatured_media());

            media.enqueue(new Callback<WPMedia>() {
                @Override
                public void onResponse (Call<WPMedia> call, Response<WPMedia> response) {
                    if (response.isSuccessful()) {
                        Glide.with(getApplicationContext())
                                .load(Uri.parse(response.body().getSourceUrl()))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                                    @Override
                                    public void onResourceReady (final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        realmUtils.updatePostMedia(post, byteArray);
                                        Palette.from(resource).generate(palette -> setupImage(palette, resource));
                                    }
                                });
                    }
                }

                @Override
                public void onFailure (Call<WPMedia> call, Throwable t) {

                }
            });
        } else {
            Glide.with(getApplicationContext())
                    .load(post.getSource_url())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady (Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            Palette.from(resource).generate(palette -> setupImage(palette, resource));
                        }
                    });
        }
    }

    private void setNews (Element b, Elements elements) {
        News news = null;

        try {
            news = new News().title(post.getTitle()).date(UIUtils.getTimeAgo(post.getCreated_at
                    ())).firstParagraph(b).otherParagraphs(elements);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        NewsDetailAdapter adapter = new NewsDetailAdapter(this, news);

        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsView.setAdapter(adapter);
    }

    private void setNewsFromParcel (Element b, Elements elements, NI post) {
        News news = null;

        try {
            news = new News().title(post.getTitle().title()).date(UIUtils.getTimeAgo(post.getCreated_at
                    ())).firstParagraph(b).otherParagraphs(elements);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        NewsDetailAdapter adapter = new NewsDetailAdapter(this, news);

        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsView.setAdapter(adapter);
    }

    private void handleFailure (Throwable t) {

    }

    private void showToast (String s) {
        Snackbar.make(mRoot, s, Snackbar.LENGTH_LONG).show();
    }

    private void setupImage (Palette palette, Bitmap image) {
        int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setContentScrimColor(mutedColor);
        changeStatusBar(mutedColor);
        header.setImageBitmap(image);
    }

    private void changeStatusBar (int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(statusBarColor);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        realmUtils.closeRealm();
        realmUtils = null;
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (post != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, post.getTitle() + "\n\n" + post.getLink());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share this via"));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void realmChange (Post post) {

    }

    @Override
    public void realmChange (List<NI> p) {

    }

    @Override
    public void postSaveFailed (Post post, Throwable t) {

    }

    private class PostParser {
        private Element b;
        private Elements elements;

        public Element getB () {
            return b;
        }

        public Elements getElements () {
            return elements;
        }

        public PostParser invoke (String content, String firstElement, String tag) {
            Document t = Jsoup.parse(content);

            b = t.getElementsByClass(firstElement).first();

            elements = t.select(tag);
            return this;
        }
    }
}
