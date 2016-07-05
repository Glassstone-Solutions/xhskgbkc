package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.network.models.Post;
import net.glassstones.thediarymagazine.network.models.WPMedia;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Thompson on 03/07/2016.
 * For The Diary Magazine
 */
public class FlipAdapter extends BaseAdapter {
    private static final String TAG = FlipAdapter.class.getSimpleName();
    private Context mContext;
    private List<Post> items;
    private LayoutInflater inflater;
    private ViewHolder holder;
    private Callback callback;
    private TDMAPIClient client;
    private Tracker mTracker;

    private int itemPosition = -1;

    public FlipAdapter (Context mContext, List<Post> posts) {
        this.mContext = mContext;
        this.items = posts;
        init();
    }

    private void init () {
        inflater = LayoutInflater.from(mContext);

        ServiceGenerator sg = new ServiceGenerator((Common) mContext.getApplicationContext());

        client = sg.createService(TDMAPIClient.class);

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
        Post p = items.get(position);
        Headline vh = (Headline) v;
        ImageView splash = vh.getmSplash();
        setImage(p, splash);
        TextView t = vh.getmTitle();
        t.setText(Html.fromHtml(p.getTitle()));
        CustomTextView ct = vh.getExcerpt();
        ct.setText(Html.fromHtml(p.getExcerpt()));

        itemPosition = position;
        vh.getRoot().setOnClickListener((this::clickHandler));
    }

    private void setImage (Post p, ImageView i) {
        if (p.isMediaSaved()) {
            Glide.with(mContext).load(p.getSource_url()).into(i);
        } else {
            Call<WPMedia> mediaCall = client.getMedia(p.getFeatured_media());
            mediaCall.enqueue(new retrofit2.Callback<WPMedia>() {
                @Override
                public void onResponse (Call<WPMedia> call, Response<WPMedia> response) {
                    String url = response.body().getSourceUrl();
                    try {
                        Glide.with(mContext)
                                .load(url != null ? url : "")
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady (Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        i.setImageBitmap(resource);
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure (Call<WPMedia> call, Throwable t) {

                }
            });
        }
    }

    private void clickHandler (View v) {
        Post post = (Post) v.getTag();
        if (post != null && callback != null) {
            NewsItem ni = new NewsItem();
            ni.setPost(items.get(itemPosition == -1 ? -1 : itemPosition));
            callback.onPageRequested(ni);
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Read_Post")
                    .build());
        }
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
