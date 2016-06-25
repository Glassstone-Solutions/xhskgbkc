package net.glassstones.thediarymagazine.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.okhttp.ResponseBody;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.Callback;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.tasks.GetPostsByCategory;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.Sort;
import retrofit.Call;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

public class CategoryActivity extends AppCompatActivity implements Callback, FlipView.OnFlipListener, FlipView.OnOverFlipListener, GetPostsByCategory.GetPostsByCategoryInterface {

    private static final String TAG = CategoryActivity.class.getSimpleName();
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.list)
    FlipView list;
    NewsFlipAdapter mAdapter;
    int skip = 1;
    TDMAPIClient client;
    private GetPostsByCategory task;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);

        int category = getIntent().getIntExtra("cat", -1);

        List<Post> posts = Realm.getDefaultInstance()
                .where(Post.class)
                .equalTo("categories.id", category)
                .equalTo("categories.isTitleSaved", true)
                .findAllSorted(Post.CREATED_AT, Sort.DESCENDING);
        List<NewsCluster> cluster = Common.getPostsCluster(posts);
        mAdapter = new NewsFlipAdapter(this, cluster);
        mAdapter.setCallback(this);
        list.setAdapter(mAdapter);
        list.setOnFlipListener(this);
        list.setOnOverFlipListener(this);
        list.setOverFlipMode(OverFlipMode.RUBBER_BAND);

        ServiceGenerator sg = new ServiceGenerator((Common) getApplication());

        client = sg.createService(TDMAPIClient.class);

        Call<ArrayList<NI>> call = client.getPostsByCategory(category, 25, skip);

        task = new GetPostsByCategory(call);
        task.setListeners(this);
        task.execute();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (task != null) {
            task.closeRealm();
        }
    }

    @Override
    public void onPageRequested (NewsItem newsItem) {

    }

    @Override
    public void onFlippedToPage (FlipView v, int position, long id) {

    }

    @Override
    public void onOverFlip (FlipView v, OverFlipMode mode, boolean overFlippingPrevious, float overFlipDistance, float flipDistancePerPage) {

    }

    @Override
    public void getPosts (List<Post> posts) {
        Log.e(TAG, String.valueOf(posts.size()));
    }

    @Override
    public void callFailed (Throwable t) {

    }

    @Override
    public void responseFailed (ResponseBody responseBody) {

    }
}
