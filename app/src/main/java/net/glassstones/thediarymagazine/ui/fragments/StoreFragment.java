package net.glassstones.thediarymagazine.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.models.StoreItem;
import net.glassstones.thediarymagazine.ui.adapters.StoreAdapter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {

    private StoreFragmentInteraction mListener;
    private StoreAdapter mAdapter;
    private Context c;
    private SecureRandom random = new SecureRandom();

    @InjectView(R.id.storeList) RecyclerView mRecycler;

    private List<StoreItem> items;

    public StoreFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance () {
        return new StoreFragment();
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        if (context instanceof StoreFragmentInteraction) {
            mListener = (StoreFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = getActivity();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = generateItems();
        mAdapter = new StoreAdapter(c, items);
        mRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDetach () {
        super.onDetach();
        mListener = null;
    }

    public interface StoreFragmentInteraction {
        // TODO: Update argument type and name
        void onItemClick (StoreItem item);
    }

    private List<StoreItem> generateItems () {
        String baseUrl = "http://lorempixel.com/400/200/fashion/";
        List<StoreItem> items = new ArrayList<>();
        for (int i = 0; i<10; i++){
            Random r = new Random();
            double price = 10 + (70 - 10) * r.nextDouble();
            StoreItem item = new StoreItem().description("Description"+i)
                                .extUrl(baseUrl+i)
                                .imageUri(baseUrl+i)
                                .objectId(new BigInteger(130, random).toString(32))
                                .price(price)
                                .title("Item"+i);
            items.add(item);
        }
        return items;
    }
}
