package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.models.StoreItem;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.utils.UIUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thompson on 17/09/2016.
 * For The Diary Magazine
 */
public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<StoreItem> items;
    private Context mContext;

    private final int cellSize;

    public StoreAdapter (Context context, List<StoreItem> items) {
        this.mContext = context;
        this.items = items;
        this.cellSize = UIUtils.getScreenWidth(context) / 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.store_item, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        layoutParams.setFullSpan(false);
        v.setLayoutParams(layoutParams);
        return new StoreItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int i) {
        StoreItemViewHolder vh = (StoreItemViewHolder) holder;
        StoreItem item = items.get(i);
        vh.getTitle().setText(item.getTitle());
        vh.getPrice().setText(formatNumber(item.getPrice()));
        Glide.with(mContext).load(item.getImageUri()).into(vh.getImage());
    }

    @Override
    public int getItemCount () {
        return items.size();
    }

    public String formatNumber(double n) {
        Locale nigeria = new Locale("en", "NG");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(nigeria);

        return formatter.format(n);
    }

    class StoreItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title)
        CustomTextView title;
        @InjectView(R.id.price)
        CustomTextView price;
        @InjectView(R.id.storeImage)
        ImageView image;

        public StoreItemViewHolder (View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

        public CustomTextView getTitle () {
            return title;
        }

        public CustomTextView getPrice () {
            return price;
        }

        public ImageView getImage () {
            return image;
        }
    }
}
