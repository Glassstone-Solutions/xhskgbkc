package net.glassstones.thediarymagazine.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.glassstones.thediarymagazine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class MainNewsItemHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.splash) ImageView mSplash;
    @InjectView(R.id.title) TextView mTitle;

    public MainNewsItemHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public ImageView getmSplash() {
        return mSplash;
    }

    public TextView getmTitle() {
        return mTitle;
    }
}
