package net.glassstones.thediarymagazine.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.glassstones.thediarymagazine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnderConstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnderConstructionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @InjectView(R.id.pholder)
    ImageView pholder;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.subtitle)
    TextView subtitle;

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
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pholder.setImageDrawable(ContextCompat.getDrawable(getActivity(), mParam1));
        subtitle.setText(mParam2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
