package net.glassstones.thediarymagazine.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.common.BaseActivity;
import net.glassstones.thediarymagazine.ui.adapters.MyFragmentAdapter;
import net.glassstones.thediarymagazine.ui.fragments.DashboardFragment;
import net.glassstones.thediarymagazine.ui.fragments.NewsFragment;
import net.glassstones.thediarymagazine.ui.fragments.SearchFragment;
import net.glassstones.thediarymagazine.ui.fragments.UnderConstructionFragment;
import net.glassstones.thediarymagazine.utils.HelperSharedPreferences;
import net.glassstones.thediarymagazine.utils.RealmUtils;

import butterknife.InjectView;


public class NewsFeedActivity extends BaseActivity implements NewsFragment.NewsFeedFragmentInteraction {
    @InjectView(R.id.menu_tab)
    TabLayout mTabLayout;
    @InjectView(R.id.pager)
    ViewPager mPager;
    RealmUtils realmUtils;
    private InterstitialAd mInterstitialAd;
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

    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler(Looper.getMainLooper());
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run () {
            showInterstitial();
            handler.postDelayed(runnableCode, 480000);
        }
    };

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
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
    public Class clazz () {
        return this.getClass();
    }

    @Override
    public int resourceId () {
        return R.layout.activity_news_feed;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        if (HelperSharedPreferences.getSharedPreferencesBoolean(this, Common.KEY_FIRST_RUN, true)) {
            doSplash();
        } else {
            init();
            initAds();
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (realmUtils != null) {
            realmUtils.closeRealm();
        }
        handler.removeCallbacks(runnableCode);
    }

    private void doSplash () {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void init () {

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
            public void onTabSelected (TabLayout.Tab tab) {
                int p = tab.getPosition();
                setTabSelectedIcon(p, tab, mPager);
            }

            @Override
            public void onTabUnselected (TabLayout.Tab tab) {
                int p = tab.getPosition();
                setTabUnSelectedIcon(p, tab);
            }

            @Override
            public void onTabReselected (TabLayout.Tab tab) {

            }
        });
        setupTabIcons();
    }

    private void initAds () {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-9323445577003464~5537659546");

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed () {
                Log.e(TAG, "Ad closed");
                loadAds();
            }
        });
        handler.post(runnableCode);
    }

    private void setupViewPager (ViewPager mPager) {
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());
        adapter.addFrag(new NewsFragment());
//        adapter.addFrag(UnderConstructionFragment.newInstance(defaultTabIcons[2], "Search"));
        adapter.addFrag(new DashboardFragment());
        adapter.addFrag(new SearchFragment());
        adapter.addFrag(UnderConstructionFragment.newInstance(defaultTabIcons[3], "Favorites"));
        adapter.addFrag(UnderConstructionFragment.newInstance(defaultTabIcons[4], "Shopping"));
        mPager.setAdapter(adapter);
    }

    private void setTabSelectedIcon (int p, TabLayout.Tab tab, ViewPager mPager) {
        tab.setIcon(tabIcons[p]);
        mPager.setCurrentItem(p, true);
    }

    private void setTabUnSelectedIcon (int p, TabLayout.Tab tab) {
        tab.setIcon(defaultTabIcons[p]);
    }

    private void setupTabIcons () {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(defaultTabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(defaultTabIcons[2]);
        mTabLayout.getTabAt(3).setIcon(defaultTabIcons[3]);
        mTabLayout.getTabAt(4).setIcon(defaultTabIcons[4]);
    }

    private void loadAds () {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    public void showAd () {
        showInterstitial();
    }

    private void showInterstitial () {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            loadAds();
        }
    }
}
