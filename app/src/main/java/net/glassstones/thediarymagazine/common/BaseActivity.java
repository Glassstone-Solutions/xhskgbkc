package net.glassstones.thediarymagazine.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.leakcanary.RefWatcher;

import net.glassstones.thediarymagazine.Common;

import butterknife.ButterKnife;

/**
 * Created by Thompson on 10/06/2016.
 * For The Diary Magazine
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected String TAG = clazz().getSimpleName();
    protected FirebaseAnalytics mTracker;

    public abstract Class clazz();

    public abstract int resourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common app = (Common) getApplication();
        mTracker = app.getmFirebaseAnalytics();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, TAG);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, TAG);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "SCREEN_HIT");
        mTracker.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        setContentView(resourceId());
        ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        RefWatcher watcher = Common.getRefWatcher();
        watcher.watch(this);
    }
}
