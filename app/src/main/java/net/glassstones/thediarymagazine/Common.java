package net.glassstones.thediarymagazine;

import android.content.Context;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.glassstones.thediarymagazine.common.receivers.DeepLinkReceiver;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsCluster;
import net.glassstones.thediarymagazine.network.models.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Thompson on 04/02/2016.
 * For The Diary Magazine
 */
public class Common extends MultiDexApplication {

    public static final String KEY_FIRST_RUN = "first_run";
    private static final String TAG = Common.class.getSimpleName();

    public static volatile Context applicationContext;
    private static Common instance;
    private Tracker mTracker;
    private RefWatcher _refWatcher;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static RefWatcher getRefWatcher () {
        return Common.get()._refWatcher;
    }

    public static Common get () {
        return instance;
    }

    public static List<NewsCluster> getNICluster (List<NI> list) {
        List<NewsItem> items = getNewsList(list);
        return getNewsCluster(items);
    }

    private static List<NewsItem> getNewsList (List<NI> ni) {
        Log.e(TAG, "Tab flip adapter initialized with "+ni.size()+" clusters.");
        List<NewsItem> list = new ArrayList<>();
        for (int i = 0; i < ni.size(); i++) {
            NewsItem item = new NewsItem();
            item.setNi(ni.get(i));
            list.add(item);
        }
        Log.e(TAG, "Tab flip adapter initialized with "+list.size()+" clusters.");
        return list;
    }

    private static List<NewsCluster> getNewsCluster (List<NewsItem> list) {

        List<NewsCluster> cluster = new ArrayList<>();
        if (list.size() > 0) {
            int currentIndex = 0;
            int currentKey = 0;

            int currentNewsSize = list.size();
//            int[] keys = {2, 3, 2, 3, 1, 2, 3, 2, 3, 2, 1};
            List<Integer> keys = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                Random rand = new Random();
                keys.add(rand.nextInt(3) + 1);
            }

            NewsCluster nc;
            nc = new NewsCluster();
            nc.getItems().add(list.get(0));
            cluster.add(nc);
            currentIndex++;
            do {
                nc = new NewsCluster();
                for (int i = 0; i < keys.get(currentKey); i++) {
                    try {
                        nc.getItems().add(list.get(currentIndex));
                        if (!cluster.contains(nc)) {
                            cluster.add(nc);
                        }
                    } catch (Exception e) {
                        Log.e(Common.class.getSimpleName(), e.getMessage());
                    }
                    currentIndex++;
                    currentNewsSize--;
                }
                currentKey++;
            } while (currentIndex > 0 && currentIndex < list.size() && currentNewsSize > 0);
        }

        return cluster;
    }

    public static Common getApp (Context context) {
        return (Common) context.getApplicationContext();
    }

    @Override
    public void onCreate () {
        super.onCreate();

        applicationContext = getApplicationContext();

        instance = (Common) getApplicationContext();

        IntentFilter intentFilter = new IntentFilter(DeepLinkHandler.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);
        _refWatcher = LeakCanary.install(this);

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    synchronized public FirebaseAnalytics getmFirebaseAnalytics(){
        return mFirebaseAnalytics;
    }
}
