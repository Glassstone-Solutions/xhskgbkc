package net.glassstones.thediarymagazine.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import se.emilsjolander.flipview.FlipView.OnFlipListener;
import se.emilsjolander.flipview.FlipView.OnOverFlipListener;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.Callback;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

public class UnderConstructionFragment extends Fragment implements Callback, OnFlipListener, OnOverFlipListener {

//    private FlipViewController flipView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @InjectView(R.id.flipView)
    FlipView flipView;

    private NewsFlipAdapter mAdapter;

    private int mParam1;
    private String mParam2;


    public UnderConstructionFragment() {
        // Required empty public constructor
    }

    public static UnderConstructionFragment newInstance(int param1, String param2) {
        UnderConstructionFragment fragment = new UnderConstructionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_under_construction, container, false);
        ButterKnife.inject(this, view);

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new NewsFlipAdapter(getActivity(), Common.getNewsCluster());
        mAdapter.setCallback(this);
        flipView.setAdapter(mAdapter);
        flipView.setOnFlipListener(this);
        flipView.setOnOverFlipListener(this);
        flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onFlippedToPage(FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip(FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }

    @Override
    public void onPageRequested(NewsItem newsItem) {
//        flipView.smoothFlipTo(page);
    }
}
