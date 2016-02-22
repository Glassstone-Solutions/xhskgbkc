package net.glassstones.thediarymagazine.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.Callback;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.ui.adapters.NewsFeedAdapter;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements Callback, FlipView.OnFlipListener, FlipView.OnOverFlipListener {


    @InjectView(R.id.list)
    FlipView list;

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

        NewsFlipAdapter mAdapter = new NewsFlipAdapter(getActivity(), Common.getNewsCluster());
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);
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
}
