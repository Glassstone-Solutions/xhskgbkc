package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thompson on 03/07/2016.
 * For The Diary Magazine
 */
public class FlipAdapter extends BaseAdapter {
    private static final String TAG = FlipAdapter.class.getSimpleName();
    private Context mContext;
    private List<NI> items;
    private LayoutInflater inflater;
    private Callback callback;
    private FirebaseAnalytics mTracker;
    private boolean shouldRequestMore = false;

    private int itemPosition = -1;

    private int key = 25;

    private int adskey = 0;

    public FlipAdapter (Context mContext, List<NI> posts) {
        this.mContext = mContext;
        this.items = posts;
        init();
    }

    private void init () {
        inflater = LayoutInflater.from(mContext);

        mTracker = ((Common) mContext.getApplicationContext()).getmFirebaseAnalytics();
    }

    public void setCallback (Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount () {
        return items.size() == 0 ? 0 : items.size();
    }

    @Override
    public Object getItem (int position) {
        return position;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        ViewHolder holder;
        if (layout == null) {
            layout = inflater.inflate(R.layout.news_item_headline, parent, false);
            holder = new Headline(layout);
            layout.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindView(position, holder);
        return layout;
    }

    private void bindView (int position, ViewHolder v) {
        // Load more and show ads
        loadMoreAndShowAds(position);

        NI p = items.get(position);
        Headline vh = (Headline) v;
        ImageView splash = vh.getmSplash();
        setImage(p, splash);
        TextView t = vh.getmTitle();
        t.setText(Html.fromHtml(p.getTitle().title()));
        CustomTextView ct = vh.getExcerpt();
        ct.setText(Html.fromHtml(p.getExcerpt().excerpt()));

        itemPosition = position;
        vh.getRoot().setOnClickListener((v1 -> clickHandler(v1, p)));
    }

    private void loadMoreAndShowAds (int position) {
        if (items.size() - position == 5 && key == 25 || shouldRequestMore) {
            callback.onMoreRequest(items.size());
            if (adskey > 1) {
                int pos;
                Random rand = new Random();
                pos = rand.nextInt(3) + 1;
                if (pos == 2) {
                    callback.onShowAd();
                }
            }
            adskey++;
            key = 0;
            shouldRequestMore = false;
        }
    }

    private void setImage (NI p, ImageView i) {
        Glide.with(mContext).load(p.getMedia().getSourceUrl()).into(i);
    }

    private void clickHandler (View v, NI p) {
        if (p != null && callback != null) {
            Log.e(TAG, String.valueOf(itemPosition));
            NewsItem ni = new NewsItem();
            ni.setNi(p);
            callback.onPageRequested(ni);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(p.getId()));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, p.getTitle().title());
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "POST_CLICK");
            mTracker.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    public void update (List<NI> posts) {
        items.clear();
        items = posts;
        notifyDataSetChanged();
    }

    public void add () {
        key++;
    }

    public static class ViewHolder {

    }

    public static class Headline extends ViewHolder {
        @InjectView(R.id.splash)
        ImageView mSplash;
        @InjectView(R.id.title)
        TextView mTitle;
        @InjectView(R.id.txtBody1)
        CustomTextView mExcerpt;
        @InjectView(R.id.root)
        RelativeLayout root;

        public Headline (View v) {
            ButterKnife.inject(this, v);
        }

        public ImageView getmSplash () {
            return mSplash;
        }

        public TextView getmTitle () {
            return mTitle;
        }

        public CustomTextView getExcerpt () {
            return mExcerpt;
        }

        public RelativeLayout getRoot () {
            return root;
        }
    }

    public void setShouldRequestMore (boolean shouldRequestMore) {
        this.shouldRequestMore = shouldRequestMore;
    }

    public int getItemPosition () {
        return itemPosition;
    }
}
