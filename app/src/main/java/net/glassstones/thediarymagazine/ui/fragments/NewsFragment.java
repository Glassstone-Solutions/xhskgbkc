package net.glassstones.thediarymagazine.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.common.BaseNewsFragment;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.network.models.PostEvent;
import net.glassstones.thediarymagazine.ui.activities.NewsDetailsActivity;
import net.glassstones.thediarymagazine.ui.adapters.FlipAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.Sort;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * A simple {@link BaseNewsFragment} subclass.
 */
public class NewsFragment extends BaseNewsFragment implements Callback,
        FlipView.OnFlipListener, FlipView.OnOverFlipListener {

    @InjectView(R.id.list)
    FlipView list;

    FlipAdapter mAdapter;

    Realm realm;

    public NewsFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, view);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return view;
    }

    @Override
    public void onStart () {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public void onStop () {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
//        handler.removeCallbacks(runnableCode);
    }

    @Override
    public void onPageRequested (NewsItem newsItem) {
        Intent i = new Intent(getActivity(), NewsDetailsActivity.class);
        i.putExtra("post_id", newsItem.getPost().getId());
        getActivity().startActivity(i);
    }

    @Override
    public void onFlippedToPage (FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip (FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float
            overFlipDistance, float flipDistancePerPage) {

    }

    @Override
    public Class clazz () {
        return this.getClass();
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "On Create");
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Post> posts = realm.where(Post.class).findAllSorted(Post.CREATED_AT, Sort.DESCENDING);
        if (realm.where(Post.class).count() > 0) {
            clusters = Common.getNewsCluster(realm);
        } else {
            clusters = new ArrayList<>();
        }
        mAdapter = new FlipAdapter(getActivity(), posts);
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostEvent (PostEvent event) {
    }
}
