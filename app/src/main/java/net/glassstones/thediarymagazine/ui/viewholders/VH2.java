package net.glassstones.thediarymagazine.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectViews;

/**
 * Created by Thompson on 12/02/2016.
 * For The Diary Magazine
 */
public class VH2 extends RecyclerView.ViewHolder {
    @InjectViews({R.id.splash1, R.id.splash2, R.id.splash3})
    List<TopAlignedImageView> splashes;
    @InjectViews({R.id.title1, R.id.title2, R.id.title3})
    List<TextView> mText;

    public VH2(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public TopAlignedImageView getSplash1() {
        return splashes.get(0);
    }

    public TopAlignedImageView getSplash2() {
        return splashes.get(1);
    }

    public TopAlignedImageView getSplash3() {
        return splashes.get(2);
    }

    public TextView getTitle1() {
        return mText.get(0);
    }

    public TextView getTitle2() {
        return mText.get(1);
    }

    public TextView getTitle3() {
        return mText.get(2);
    }
}
