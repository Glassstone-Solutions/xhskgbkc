package net.glassstones.thediarymagazine.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.interfaces.network.NetworkOperations;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.network.Request;
import net.glassstones.thediarymagazine.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Thompson on 08/06/2016.
 * For The Diary Magazine
 */
public abstract class BaseNewsFragment extends Fragment {
    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    TDMAPIClient client;
    String TAG = clazz().getSimpleName();
    List<NewsCluster> clusters;

    public abstract Class clazz();

    Tracker mTracker;

    Common app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Common) getActivity().getApplication();
        mTracker = app.getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Common.getTDMComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
