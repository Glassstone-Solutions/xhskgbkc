package net.glassstones.thediarymagazine.ui.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.WPMedia;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.ui.widgets.NestedWebView;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;
import net.glassstones.thediarymagazine.utils.UIUtils;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@DeepLink({"tdm://posts/{id}", "http://www.thediarymagazine.com/{slug}"})
public class NewsDetailsActivity extends BaseActivity {

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
    TDMAPIClient client;

    Window window;

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

        window = this.getWindow();

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
                            initPost(response.body());
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
                            initPost(response.body());
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
            Call<NI> getPost = client.getPost(getIntent().getIntExtra("post_id", -1));
            getPost.enqueue(new Callback<NI>() {
                @Override
                public void onResponse(Response<NI> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        initPost(response.body());
                    } else {
                        NavUtils.navigateUpFromSameTask(NewsDetailsActivity.this);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    handleFailure(t);
                }
            });
        } else {
            showToast("no deep link :( ");
        }
    }

    private void handleFailure(Throwable t) {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initPost(NI body) {

        final int myWidth = UIUtils.getScreenWidth(this);
        final int myHeight = 384;

        Call<WPMedia> media = client.getMedia(body.getFeatured_media());

        media.enqueue(new Callback<WPMedia>() {
            @Override
            public void onResponse(Response<WPMedia> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Glide.with(getApplicationContext())
                            .load(Uri.parse(response.body().getSource_url()))
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                                @Override
                                public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                                            int statusBarColor = palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark));
                                            collapsingToolbar.setContentScrimColor(mutedColor);
                                            changeStatusBar(statusBarColor);
                                            header.setImageBitmap(resource);
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

        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        String sb = "<!DOCTYPE html><HTML lang=\"en\"><HEAD><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" " +
                "integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">" +
                "<style type=\"text/css\">html{background-color:#fff}.pvc_stats{display:none}p:first-child{margin-top:16px;font-weight:700;font-size:18px}</style>" +
                "</HEAD>" +
                "<body class=\"container\">" +
                body.getContent().getContent() +
                "</body></HTML>";
        webView.loadDataWithBaseURL("file:///android_asset/", sb, "text/html", "utf-8", null);
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
