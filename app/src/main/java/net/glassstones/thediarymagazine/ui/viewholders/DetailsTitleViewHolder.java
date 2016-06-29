package net.glassstones.thediarymagazine.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thompson on 28/06/2016.
 * For The Diary Magazine
 */
public class DetailsTitleViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.title)
    CustomTextView title;
    @InjectView(R.id.source)
    CustomTextView source;
    @InjectView(R.id.timestamp)
    CustomTextView timestamp;

    public DetailsTitleViewHolder (View v) {
        super(v);
        ButterKnife.inject(this, v);
    }

    public CustomTextView getTitle () {
        return title;
    }

    public CustomTextView getSource () {
        return source;
    }

    public CustomTextView getTimestamp () {
        return timestamp;
    }
}
