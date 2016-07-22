package net.glassstones.thediarymagazine.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.network.models.NewsCluster;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public abstract class BaseNewsFragment extends BaseFragment {
    protected String TAG = clazz().getSimpleName();
    protected List<NewsCluster> clusters;

    public abstract Class clazz();

    FirebaseAnalytics mTracker;

    Common app;

    protected Context CONTEXT;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CONTEXT = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Common) getActivity().getApplication();
        mTracker = app.getmFirebaseAnalytics();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, TAG);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, TAG);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "SCREEN_HIT");
        mTracker.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void onStart () {
        super.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void bindViews (View view) {
        ButterKnife.inject(this, view);
    }
}
