package net.glassstones.thediarymagazine.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.avocarrot.androidsdk.AdError;
import com.avocarrot.androidsdk.AvocarrotCustomListener;
import com.avocarrot.androidsdk.CustomModel;
import com.parse.ParseUser;

import net.glassstones.thediarymagazine.BuildConfig;
import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.network.NetworkOperations;
import net.glassstones.thediarymagazine.interfaces.network.TDMAPIClient;
import net.glassstones.thediarymagazine.models.NI;
import net.glassstones.thediarymagazine.models.Post;
import net.glassstones.thediarymagazine.models.PostEvent;
import net.glassstones.thediarymagazine.network.Request;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.services.UpdateLocalPostsImageService;
import net.glassstones.thediarymagazine.services.UpdateLocalPostsService;
import net.glassstones.thediarymagazine.ui.adapters.MyFragmentAdapter;
import net.glassstones.thediarymagazine.ui.fragments.DashboardFragment;
import net.glassstones.thediarymagazine.ui.fragments.NewsFragment;
import net.glassstones.thediarymagazine.ui.fragments.UnderConstructionFragment;
import net.glassstones.thediarymagazine.utils.HelperSharedPreferences;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import io.realm.Realm;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.os.PersistableBundle;
import retrofit.Call;
import retrofit.Response;


public class NewsFeedActivity extends BaseActivity implements RealmUtils.RealmInterface, NetworkOperations {

    private static final int FETCH_JOB_ID = 100;
    private static final int FETCH_IMAGE_JOB_ID = 200;
    @InjectView(R.id.menu_tab)
    TabLayout mTabLayout;
    @InjectView(R.id.pager)
    ViewPager mPager;
    ParseUser mCurrentUser;
    Realm realm;
    RealmUtils realmUtils;
    private JobScheduler jobScheduler;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_dashboard,
            R.drawable.ic_search,
            R.drawable.ic_favorite,
            R.drawable.shopping_basket
    };
    private int[] defaultTabIcons = {
            R.drawable.ic_home_unselected,
            R.drawable.ic_dashboard_unselected,
            R.drawable.ic_search_unselected,
            R.drawable.ic_favorite_unselected,
            R.drawable.shopping_basket_unselected
    };

    private void constructFetchJob() {
        PersistableBundle bundle = new PersistableBundle();
        JobInfo.Builder builder = new JobInfo.Builder(FETCH_JOB_ID, new ComponentName(this, UpdateLocalPostsService.class));
        builder.setPeriodic(3600000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(bundle)
                .setPersisted(true);
        jobScheduler.schedule(builder.build());
    }

    private void constructImageFetchJob(){
        PersistableBundle bundle = new PersistableBundle();
        JobInfo.Builder builder = new JobInfo.Builder(FETCH_IMAGE_JOB_ID, new ComponentName(this, UpdateLocalPostsImageService.class));
        builder.setPeriodic(300000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(bundle)
                .setPersisted(true);
        jobScheduler.schedule(builder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        if (HelperSharedPreferences.getSharedPreferencesBoolean(this, Common.KEY_FIRST_RUN, true)) {
            doSplash();
        } else {
            init();
            jobScheduler = JobScheduler.getInstance(this);
            constructFetchJob();
            constructImageFetchJob();
        }
        mCurrentUser = ParseUser.getCurrentUser();

        final com.avocarrot.androidsdk.AvocarrotCustom avocarrotCustom =
                new com.avocarrot.androidsdk.AvocarrotCustom(
                        this,                     /* reference to your Activity */
                        "87d7278ea00dbfe6a1e8afed92ceadc9df3aba91", /* this is your Avocarrot API Key */
                        "41b30d19c03fa5eb8884d558500598f8a58c69e9" /* this is your Avocarrot Placement Key */
                );

        if (BuildConfig.DEBUG) {
            avocarrotCustom.setSandbox(true);
            avocarrotCustom.setLogger(true, "ALL");
        }

        avocarrotCustom.setListener(new AvocarrotCustomListener() {
            @Override
            public void onAdLoaded(List<CustomModel> ads) {
                super.onAdLoaded(ads);
                super.onAdLoaded(ads);
                if ((ads == null) || (ads.size() < 1)) {
                    return;
                }
            }

            @Override
            public void onAdError(AdError error) {
                super.onAdError(error);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

        ServiceGenerator sg = new ServiceGenerator((Common) getApplication());

        TDMAPIClient client = sg.createService(TDMAPIClient.class);

        Call<ArrayList<NI>> call = client.getPosts(25, 0, null);

        Request request = new Request(call);

        request.setCallback(this);

        request.execute();

        realm = Realm.getDefaultInstance();

        realmUtils = new RealmUtils(realm, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmUtils.closeRealm();
    }

    private void doSplash() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void init() {

        Intent intent = getIntent();
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle parameters = intent.getExtras();
            String idString = parameters.getString("id");
            Intent i = new Intent(this, NewsDetailsActivity.class);
            i.putExtra("post_id", Integer.valueOf(idString));
            startActivity(i);
        }

        setupViewPager(mPager);
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                setTabSelectedIcon(p, tab, mPager);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int p = tab.getPosition();
                setTabUnSelectedIcon(p, tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupTabIcons();
    }

    private void setTabUnSelectedIcon(int p, TabLayout.Tab tab) {
        tab.setIcon(defaultTabIcons[p]);
    }

    private void setTabSelectedIcon(int p, TabLayout.Tab tab, ViewPager mPager) {
        tab.setIcon(tabIcons[p]);
        mPager.setCurrentItem(p, true);
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(defaultTabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(defaultTabIcons[2]);
        mTabLayout.getTabAt(3).setIcon(defaultTabIcons[3]);
        mTabLayout.getTabAt(4).setIcon(defaultTabIcons[4]);
    }

    private void setupViewPager(ViewPager mPager) {
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());

        adapter.addFrag(new NewsFragment());
        adapter.addFrag(new DashboardFragment());
        adapter.addFrag(UnderConstructionFragment.newInstance(defaultTabIcons[2], "Search"));
        adapter.addFrag(UnderConstructionFragment.newInstance(defaultTabIcons[3], "Favorites"));
        adapter.addFrag(UnderConstructionFragment.newInstance(defaultTabIcons[4], "Shopping"));
        mPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Class clazz() {
        return this.getClass();
    }

    @Override
    public int resourceId() {
        return R.layout.activity_news_feed;
    }

    @Override
    public void realmChange(List<Post> posts) {
        EventBus.getDefault().post(new PostEvent()
                .id(PostEvent.LIST_CHANGE)
                .status(PostEvent.SAVED)
                .type(PostEvent.POST_LIST)
        );
    }

    @Override
    public void realmChange(Post post) {
        EventBus.getDefault().post(new PostEvent()
                .id(post.getId())
                .type(PostEvent.SINGLE_POST)
                .status(PostEvent.SAVED)
        );
    }

    @Override
    public void postSaveFailed(Post post, Throwable t) {

    }

    @Override
    public void onPostResponse(Response<ArrayList<NI>> respose) {
        List<NI> posts = respose.body();
        List<Post> rPosts = new ArrayList<>();
        for (NI p : posts) {
            rPosts.add(realmUtils.NI2Post(p, null));
        }
        realmUtils.savePosts(rPosts);
    }

    @Override
    public void onPostRequestFailure(Throwable t) {

    }
}
