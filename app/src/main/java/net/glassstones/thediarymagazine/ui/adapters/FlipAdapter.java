package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;

import java.util.List;

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
    private Tracker mTracker;

    private int itemPosition = -1;

    private int key = 25;

    public FlipAdapter (Context mContext, List<NI> posts) {
        this.mContext = mContext;
        this.items = posts;
        init();
    }

    private void init () {
        inflater = LayoutInflater.from(mContext);

        ServiceGenerator sg = new ServiceGenerator((Common) mContext.getApplicationContext());

        TDMAPIClient client = sg.createService(TDMAPIClient.class);

        mTracker = ((Common) mContext.getApplicationContext()).getDefaultTracker();
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

        if (items.size() - position == 5 && key == 25) {
            callback.onMoreRequest(items.size());
            key = 0;
        }

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

    private void setImage (NI p, ImageView i) {
        Glide.with(mContext).load(p.getMedia().getSourceUrl()).into(i);
    }

    private void clickHandler (View v, NI p) {
        if (p != null && callback != null) {
            Log.e(TAG, String.valueOf(itemPosition));
            NewsItem ni = new NewsItem();
            ni.setPost(p);
            callback.onPageRequested(ni);
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Read_Post")
                    .build());
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

}
