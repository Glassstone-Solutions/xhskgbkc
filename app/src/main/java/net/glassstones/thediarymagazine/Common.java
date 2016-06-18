package net.glassstones.thediarymagazine;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.squareup.leakcanary.LeakCanary;
import com.wang.avi.AVLoadingIndicatorView;

import net.glassstones.thediarymagazine.di.components.DaggerNetComponent;
import net.glassstones.thediarymagazine.di.components.DaggerTdmComponent;
import net.glassstones.thediarymagazine.di.components.NetComponent;
import net.glassstones.thediarymagazine.di.components.TdmComponent;
import net.glassstones.thediarymagazine.di.modules.AppModule;
import net.glassstones.thediarymagazine.di.modules.NetModule;
import net.glassstones.thediarymagazine.di.modules.TdmModule;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.receivers.DeepLinkReceiver;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.Retrofit;

/**
 * Created by Thompson on 04/02/2016.
 * For The Diary Magazine
 */
public class Common extends Application {

    public static final String KEY_FIRST_RUN = "first_run";

    public static volatile Context applicationContext;
    private static TdmComponent mTDMComponent;
    private static Retrofit retrofit;
    private NetComponent mNetComponent;
    private Tracker mTracker;
    private static Realm realm;

    public static void loadingStatus(AVLoadingIndicatorView loadingView, boolean isLoading) {
        loadingView.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static List<NewsItem> getDemoNews(List<Post> posts) {
        List<NewsItem> list = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            NewsItem item = new NewsItem();
            item.setPost(posts.get(i));
            list.add(item);
        }
        return list;
    }

    private static List<NewsCluster> getNewsCluster(List<NewsItem> list) {

        List<NewsCluster> cluster = new ArrayList<>();


        int currentIndex = 0;
        int currentKey = 0;

        int currentNewsSize = list.size();
        int[] keys = {2, 3, 2, 3, 1, 2, 3, 2, 3, 2, 1};

        NewsCluster nc;
        nc = new NewsCluster();
        nc.getItems().add(list.get(0));
        cluster.add(nc);
        currentIndex++;
        do {
            nc = new NewsCluster();
            for (int i = 0; i < keys[currentKey]; i++) {
                try {
                    nc.getItems().add(list.get(currentIndex));
                    if (!cluster.contains(nc)) {
                        cluster.add(nc);
                    }
                } catch (Exception e) {
//                    Log.e(Common.class.getSimpleName(), e.getMessage());
                }
                currentIndex++;
                currentNewsSize--;
            }
            currentKey++;
        } while (currentIndex > 0 && currentIndex < list.size() && currentNewsSize > 0);

        return cluster;
    }

    public static List<NewsCluster> getNewsCluster() {
        List<Post> p = realm.where(Post.class).findAll();
        Log.e("COUNT", String.valueOf(p.size()));
        final List<NewsItem> items = getDemoNews(p);

        return getNewsCluster(items);
    }

    public static TdmComponent getTDMComponent() {
        return mTDMComponent;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableAdvertisingIdCollection(true);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("http://www.thediarymagazine.com"))
                .build();
        mTDMComponent = DaggerTdmComponent.builder()
                .netComponent(mNetComponent)
                .tdmModule(new TdmModule())
                .build();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .server(Keys.KEY_SERVER)
                .build());
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().increment("RunCount");
        ParseUser.getCurrentUser().saveInBackground();

        Thread.UncaughtExceptionHandler myHandler = new ExceptionReporter(
                this.getDefaultTracker(),
                Thread.getDefaultUncaughtExceptionHandler(),
                this);

        // Make myHandler the new default uncaught exception handler.
        Thread.setDefaultUncaughtExceptionHandler(myHandler);

        IntentFilter intentFilter = new IntentFilter(DeepLinkHandler.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);
        LeakCanary.install(this);

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(config);

    }

    public static Realm getRealm(){
        return realm;
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
