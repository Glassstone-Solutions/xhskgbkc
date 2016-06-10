package net.glassstones.thediarymagazine.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.parse.ParseUser;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.ui.fragments.DashboardFragment;
import net.glassstones.thediarymagazine.ui.fragments.NewsFragment;
import net.glassstones.thediarymagazine.ui.fragments.UnderConstructionFragment;
import net.glassstones.thediarymagazine.utils.HelperSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class NewsFeedActivity extends BaseActivity {

    @InjectView(R.id.menu_tab)
    TabLayout mTabLayout;
    @InjectView(R.id.pager)
    ViewPager mPager;

    ParseUser mCurrentUser;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        if (HelperSharedPreferences.getSharedPreferencesBoolean(this, Common.KEY_FIRST_RUN, true))
            doSplash();
        else
            init();
        mCurrentUser = ParseUser.getCurrentUser();
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

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }

    }
}
