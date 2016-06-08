package net.glassstones.thediarymagazine.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

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
public abstract class BaseNewsFragment extends Fragment implements NetworkOperations {
    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    TDMAPIClient client;
    String TAG = clazz().getSimpleName();
    protected List<NI> posts;
    List<NewsCluster> clusters;

    public abstract int limit();

    public abstract int skip();

    public abstract Class clazz();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Common.getTDMComponent().inject(this);


        clusters = new ArrayList<>();

        ServiceGenerator sg = new ServiceGenerator((Common) getActivity().getApplication());

        TDMAPIClient client = sg.createService(TDMAPIClient.class);

        Call<ArrayList<NI>> call = client.getPosts(limit(), skip());

        Request request = new Request(call);

        request.setCallback(this);

        request.execute();
    }

    @Override
    public void onPostResponse(Response<ArrayList<NI>> response) {
        posts = response.body();

        for (NI post : posts) {
            Log.e(TAG, post.getTitle().getTitle());
        }
    }

    @Override
    public void onPostRequestFailure(Throwable t) {

    }
}
