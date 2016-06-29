package net.glassstones.thediarymagazine.ui.viewholders;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.LinearLayout;

import net.glassstones.thediarymagazine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thompson on 28/06/2016.
 * For The Diary Magazine
 */
public class DetailsParagraphViewHolder extends ViewHolder {
    @InjectView(R.id.root)
    LinearLayout root;

    public DetailsParagraphViewHolder (View v) {
        super(v);
        ButterKnife.inject(this, v);
    }

    public LinearLayout getRoot () {
        return root;
    }
}
