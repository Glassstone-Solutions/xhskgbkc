package net.glassstones.thediarymagazine.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.Callback;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Response;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * A simple {@link BaseNewsFragment} subclass.
 */
public class NewsFragment extends BaseNewsFragment implements Callback, FlipView.OnFlipListener, FlipView.OnOverFlipListener {


    @InjectView(R.id.list)
    FlipView list;

    NewsFlipAdapter mAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, view);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new NewsFlipAdapter(getActivity(), clusters);
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onPageRequested(NewsItem newsItem) {

    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }

    @Override
    public int limit() {
        return 10;
    }

    @Override
    public int skip() {
        return 1;
    }

    @Override
    public Class clazz() {
        return this.getClass();
    }


    @Override
    public void onPostResponse(Response<ArrayList<NI>> response) {
        super.onPostResponse(response);
        Log.e(TAG, String.valueOf(response.body().size()));

        clusters = Common.getNewsCluster(response.body());

        mAdapter.update(clusters);
    }

    @Override
    public void onPostRequestFailure(Throwable t) {

    }
}
