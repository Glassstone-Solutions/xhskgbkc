package net.glassstones.thediarymagazine.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.leakcanary.RefWatcher;

import net.glassstones.thediarymagazine.Common;

import butterknife.ButterKnife;

/**
 * Created by Thompson on 10/06/2016.
 * For The Diary Magazine
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected String TAG = clazz().getSimpleName();
    protected Tracker mTracker;

    public abstract Class clazz();

    public abstract int resourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common app = (Common) getApplication();
        mTracker = app.getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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
