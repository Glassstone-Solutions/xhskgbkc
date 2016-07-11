package net.glassstones.thediarymagazine.ui.activities;

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

import net.glassstones.thediarymagazine.BuildConfig;
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

import java.util.List;

import butterknife.InjectView;


public class NewsFeedActivity extends BaseActivity {
    @InjectView(R.id.menu_tab)
    TabLayout mTabLayout;
    @InjectView(R.id.pager)
    ViewPager mPager;
    RealmUtils realmUtils;
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

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (realmUtils != null) {
            realmUtils.closeRealm();
        }
    }

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
                public void onAdLoaded (List<CustomModel> ads) {
                    super.onAdLoaded(ads);
                    super.onAdLoaded(ads);
                    if ((ads == null) || (ads.size() < 1)) {
                        return;
                    }
                }

                @Override
                public void onAdError (AdError error) {
                    super.onAdError(error);
                }

                @Override
                public void onAdClicked () {
                    super.onAdClicked();
                }

                @Override
                public void onAdImpression () {
                    super.onAdImpression();
                }
            });
        }
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
}
