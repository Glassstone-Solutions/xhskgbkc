package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.glassstones.thediarymagazine.network.models.NewsCluster;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter.Headline;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter.VH1;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter.VH2;
import net.glassstones.thediarymagazine.ui.adapters.NewsFlipAdapter.VH3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thompson on 03/07/2016.
 * For The Diary Magazine
 */
public class CategoryAdapter extends BaseAdapter {

    private static final int TYPE_HEADLINE_MAIN = 3;
    private static final int TYPE_HEADLINE = 0;
    private static final int TYPE_MAIN_1 = 1;
    private static final int TYPE_MAIN_2 = 2;
    private static final int TYPE_ERROR = -1;
    private static final String TAG = CategoryAdapter.class.getSimpleName();
    NewsFlipAdapter.ViewHolder holder;
    private Context mContext;
    private List<NewsCluster> items;

    public CategoryAdapter (Context mContext) {
        this.mContext = mContext;
        this.items = new ArrayList<>();
    }

    private void returnView (View convertView, int type) {
        if (TYPE_HEADLINE == type) {
            if (convertView.getTag() instanceof Headline)
                holder = (Headline) convertView.getTag();
        } else if (TYPE_MAIN_1 == type) {
            if (convertView.getTag() instanceof VH1)
                holder = (VH1) convertView.getTag();
        } else if (TYPE_MAIN_2 == type) {
            if (convertView.getTag() instanceof VH2)
                holder = (VH2) convertView.getTag();
        } else if (TYPE_HEADLINE_MAIN == type) {
            if (convertView.getTag() instanceof VH3)
                holder = (VH3) convertView.getTag();
        }
    }

    @Override
    public int getCount () {
        return items.size() == 0 ? 0 : items.size();
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType (int position) {
        int itemType;
        switch (items.get(position).getItems().size()) {
            case 1:
                itemType = TYPE_HEADLINE;
                break;
            case 2:
                itemType = TYPE_MAIN_1;
                break;
            case 3:
                itemType = TYPE_MAIN_2;
                break;
            default:
                itemType = TYPE_ERROR;
                break;
        }
        return itemType;
    }

    public void update (List<NewsCluster> clusters) {
        items.clear();
        items = clusters;
        Log.e(TAG, String.valueOf(items.size()));
        notifyDataSetChanged();
    }

    public void add (NewsCluster cluster){
        items.add(cluster);
        notifyDataSetChanged();
    }
}
