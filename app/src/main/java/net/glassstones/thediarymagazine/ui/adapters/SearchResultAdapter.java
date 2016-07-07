package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.ui.activities.NewsDetailsActivity;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Thompson on 06/07/2016.
 * For The Diary Magazine
 */
public class SearchResultAdapter extends RecyclerView.Adapter {

    private static final String TAG = SearchResultAdapter.class.getSimpleName();
    private Context context;
    private List<NI> posts;

    public SearchResultAdapter (Context c) {
        this.context = c;
        posts = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_result_layout, parent, false);
        return new SearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        SearchResultViewHolder vh = (SearchResultViewHolder) holder;
        NI post = posts.get(position);
        LinearLayout root = vh.getRoot();
        RoundedImageView splash = vh.getSplash();
        ImageView sourceImage = vh.getSourceImage();
        CustomTextView title = vh.getTitle();
        CustomTextView sourceTitle = vh.getSourceTitle();
        CustomTextView timestamp = vh.getTimestamp();

        Glide.with(context).load(post.getMedia().getSourceUrl()).into(splash);
        title.setText(Html.fromHtml(post.getTitle().getTitle()));


        Log.e(TAG, post.getTitle().getTitle());

        root.setOnClickListener(v -> {
            Intent i = new Intent(context, NewsDetailsActivity.class);
            i.putExtra("postBundle", post);
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount () {
        return posts != null ? posts.size() : 0;
    }

    public void add (NI news) {
        posts.add(news);
        notifyItemInserted(posts.size());
    }

    public void update (List<NI> p) {
        posts.clear();
        posts = p;
        notifyDataSetChanged();
    }

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.root)
        LinearLayout root;
        @InjectView(R.id.splash)
        RoundedImageView splash;
        @InjectView(R.id.title)
        CustomTextView title;
        @InjectView(R.id.source_logo)
        ImageView sourceImage;
        @InjectView(R.id.source_title)
        CustomTextView sourceTitle;
        @InjectView(R.id.timestamp)
        CustomTextView timestamp;

        public SearchResultViewHolder (View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

        public LinearLayout getRoot () {
            return root;
        }

        public void setRoot (LinearLayout root) {
            this.root = root;
        }

        public RoundedImageView getSplash () {
            return splash;
        }

        public void setSplash (RoundedImageView splash) {
            this.splash = splash;
        }

        public CustomTextView getTitle () {
            return title;
        }

        public void setTitle (CustomTextView title) {
            this.title = title;
        }

        public ImageView getSourceImage () {
            return sourceImage;
        }

        public void setSourceImage (ImageView sourceImage) {
            this.sourceImage = sourceImage;
        }

        public CustomTextView getSourceTitle () {
            return sourceTitle;
        }

        public void setSourceTitle (CustomTextView sourceTitle) {
            this.sourceTitle = sourceTitle;
        }

        public CustomTextView getTimestamp () {
            return timestamp;
        }

        public void setTimestamp (CustomTextView timestamp) {
            this.timestamp = timestamp;
        }
    }

}
