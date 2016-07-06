package net.glassstones.thediarymagazine.common;

import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import net.glassstones.thediarymagazine.Common;

/**
 * Created by Thompson on 05/07/2016.
 * For The Diary Magazine
 */
public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = Common.getRefWatcher();
        refWatcher.watch(this);
    }
}
