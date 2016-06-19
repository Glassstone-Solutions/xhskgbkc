package net.glassstones.thediarymagazine.ui.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.models.WPMedia;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.ui.widgets.NestedWebView;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;
import net.glassstones.thediarymagazine.utils.RealmUtils;
import net.glassstones.thediarymagazine.utils.UIUtils;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;

import butterknife.InjectView;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    @InjectView(R.id.web_view)
    NestedWebView webView;
    ServiceGenerator sg;
    @InjectView(R.id.title)
    CustomTextView title;
    @InjectView(R.id.title_wrap)
    LinearLayout titleWrap;

    @InjectView(R.id.source)
    CustomTextView source;
    @InjectView(R.id.timestamp)
    CustomTextView timestamp;

    TDMAPIClient client;

    Window window;
    Post post;
    Realm realm;
    RealmUtils realmUtils;

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

        realm = Realm.getDefaultInstance();

        realmUtils = new RealmUtils(realm, this);

        sg = new ServiceGenerator(app);

        client = sg.createService(TDMAPIClient.class);
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = getIntent().getExtras();
            Log.d(TAG, "Deeplink params: " + parameters);

            String idString = parameters.getString("id");
            String name = parameters.getString("slug");

            if (!TextUtils.isEmpty(idString)) {
                Call<NI> getPost = client.getPost(Integer.parseInt(idString));
                getPost.enqueue(new Callback<NI>() {
                    @Override
                    public void onResponse(Response<NI> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            NI post = response.body();
                            initPost(realmUtils.NI2Post(post, null));
                        } else {
                            NavUtils.navigateUpFromSameTask(NewsDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        handleFailure(t);
                    }
                });
                showToast("class id== " + idString + " and name==" + name);
            } else if (!TextUtils.isEmpty(name)) {
                Call<NI> slugPost = client.getPostFromSlug(name);
                slugPost.enqueue(new Callback<NI>() {
                    @Override
                    public void onResponse(Response<NI> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            NI post = response.body();
                            initPost(realmUtils.NI2Post(post, null));
                        } else {
                            NavUtils.navigateUpFromSameTask(NewsDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        handleFailure(t);
                    }
                });
                showToast("name==" + name);
            }
        } else if (getIntent().getIntExtra("post_id", -1) != -1) {
            post = realmUtils.getPost("id", getIntent().getIntExtra("post_id", -1));
            initPost(post);
        } else {
            showToast("no deep link :( ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmUtils.closeRealm();
        realmUtils = null;
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
                intent.putExtra(Intent.EXTRA_TEXT, post.getTitle() + "\n\n" + post.getLink());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share this via"));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initPost(Post body) {

        post = body;

        title.setText(post.getTitle());

        try {
            String date = UIUtils.getTimeAgo(post.getCreated_at());
            timestamp.setText(date);
        } catch (ParseException e) {
            timestamp.setVisibility(View.GONE);
            e.printStackTrace();
        }

        if (!post.isMediaSaved()) {
            Call<WPMedia> media = client.getMedia(body.getFeatured_media());

            media.enqueue(new Callback<WPMedia>() {
                @Override
                public void onResponse(Response<WPMedia> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Glide.with(getApplicationContext())
                                .load(Uri.parse(response.body().getSourceUrl()))
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                                    @Override
                                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        realmUtils.updatePostMedia(post, byteArray);
                                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                setupImage(palette, resource);
                                            }
                                        });
                                    }
                                });
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            final Bitmap image = BitmapFactory.decodeByteArray(post.getImageByte(), 0, post.getImageByte().length);

            Palette.from(image).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    setupImage(palette, image);
                }
            });
        }

        setupWebView(webView, body);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(NestedWebView webView, Post post) {
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.setScrollbarFadingEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        String sb = getPostString(post);
        webView.loadDataWithBaseURL("file:///android_asset/", sb, "text/html", "utf-8", null);
    }

    @NonNull
    private String getPostString(Post post) {
        return String.format("<!DOCTYPE html><HTML lang=\"en\">" +
                "<HEAD>" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\"" +
                "integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">" +
                "<style type=\"text/css\">html{background-color:#fff}.pvc_stats{display:none}p:first-child{margin-top:16px;font-weight:700;font-size:18px}</style>" +
                "</HEAD>" +
                "<body class=\"container\">" +
                "%s" +
                "<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "$(\"img\").removeClass();\n" +
                "$(\"img\").removeAttr(\"sizes\");\n" +
                "$(\"img\").removeAttr(\"srcset\");\n" +
                "$(\"img\").removeAttr(\"width\");\n" +
                "$(\"img\").removeAttr(\"height\");\n" +
                "$(\"img\").addClass(\"img-responsive\");\n" +
                "</script></body></HTML>", post.getContent());
    }

    private void setupImage(Palette palette, Bitmap image) {
        int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setContentScrimColor(mutedColor);
        changeStatusBar(mutedColor);
        header.setImageBitmap(image);
    }

    private void handleFailure(Throwable t) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBar(int statusBarColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(statusBarColor);
    }

    private void showToast(String s) {
        Snackbar.make(mRoot, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void realmChange(List<Post> posts) {

    }

    @Override
    public void realmChange(Post post) {

    }

    @Override
    public void postSaveFailed(Post post, Throwable t) {

    }

    private class WebAppInterface {

        Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

    }
}
