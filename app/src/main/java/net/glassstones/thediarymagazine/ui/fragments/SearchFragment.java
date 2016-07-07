package net.glassstones.thediarymagazine.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.common.BaseFragment;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.ui.adapters.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTextChanged;
import co.kaush.core.util.CoreNullnessUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {


    private static final String TAG = SearchFragment.class.getSimpleName();
    @InjectView(R.id.search_et)
    EditText searchEt;
    @InjectView(R.id.search_result_rv)
    RecyclerView searchResultRv;
    TDMAPIClient client;
    @InjectView(R.id.progress)
    ProgressBar progress;
    private Subscription _subscription;
    private List<NI> _posts;
    private SearchResultAdapter adapter;
    private String searchQuery;

    public SearchFragment () {
        // Required empty public constructor
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = ServiceGenerator.createGithubService();
        _posts = new ArrayList<>();
        adapter = new SearchResultAdapter(getActivity());
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEt.setOnFocusChangeListener((v, hasFocus) -> hideKeyboard(v));
        searchResultRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultRv.setAdapter(adapter);
        searchResultRv.setOnTouchListener((v, event) -> {
            hideKeyboard(searchEt);
            return false;
        });
    }

    private void hideKeyboard (View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService
                (Context
                        .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            searchQuery = savedInstanceState.getString("SearchQuery");
        else
            searchQuery = "";
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SearchQuery", searchQuery);
    }

    @Override
    public void onPause () {
        super.onPause();
        _posts.clear();
        adapter.update(_posts);
        hideKeyboard(searchEt);
    }

    @Override
    public void onStop () {
        super.onStop();
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnTextChanged({R.id.search_et})
    public void doSearch () {
        if (_subscription != null && !_subscription.isUnsubscribed()) {
            _subscription.unsubscribe();
            _posts.clear();
            adapter.update(_posts);
        }
        String q = searchEt.getText().toString();
        searchQuery = q;
        if (CoreNullnessUtils.isNotNullOrEmpty(q)) {
            progress.setVisibility(View.VISIBLE);
            progress.setIndeterminate(true);
        }
        doSubscribe(q);
    }

    private void doSubscribe (String q) {
        _subscription = Observable.just(q)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> CoreNullnessUtils.isNotNullOrEmpty(q))
                .flatMap(s -> client.searchPostsObservable(s).subscribeOn(Schedulers.newThread()))
                .flatMap(Observable::from)
                .flatMap(ni -> {
                    Observable<WPMedia> _getMedia = ServiceGenerator.createGithubService()
                            .getMediaObservable(ni.getFeatured_media());
                    return Observable.zip(Observable.just(ni), _getMedia, Pair::new);
                })
                .flatMap(pair -> {
                    NI post = pair.first;
                    WPMedia media = pair.second;
                    post.setMedia(media);
                    return Observable.just(post);
                })
                .take(25)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchObserver());
    }

    private Observer<NI> getSearchObserver () {
        return new Observer<NI>() {
            @Override
            public void onCompleted () {
                progress.setVisibility(View.INVISIBLE);
                hideKeyboard(searchEt);
                Log.e(TAG, "Completed");
            }

            @Override
            public void onError (Throwable e) {
                Log.e(TAG, "App crapped");
                e.printStackTrace();
            }

            @Override
            public void onNext (NI news) {
                if (_posts != null) {
                    progress.setVisibility(progress.getVisibility() == View.VISIBLE ? View
                            .INVISIBLE : View.INVISIBLE);
                    adapter.add(news);
                }
            }
        };
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
    }

}
