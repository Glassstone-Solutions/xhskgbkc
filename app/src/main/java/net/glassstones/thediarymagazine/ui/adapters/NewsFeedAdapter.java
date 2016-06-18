package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.ui.viewholders.MainNewsItemHolder;
import net.glassstones.thediarymagazine.ui.viewholders.VH1;
import net.glassstones.thediarymagazine.ui.viewholders.VH2;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;
import net.glassstones.thediarymagazine.utils.UIUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

/**
 * Created by Thompson on 11/02/2016.
 * For The Diary Magazine
 */
public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ROOT_ID = R.id.root;
    private static final int TYPE_HEADLINE_MAIN = 3;
    private static int TYPE_HEADLINE = 0;
    private static int TYPE_MAIN_1 = 1;
    private static int TYPE_MAIN_2 = 2;
    private static int TYPE_ERROR = -1;
    private final int cellSize;
    private Context context;
    private List<NewsCluster> items;

    public NewsFeedAdapter(Context c) {
        this.context = c;
        this.cellSize = UIUtils.getScreenWidth(context) / 2;
    }

    @Override
    public int getItemViewType(int position) {
        List<NewsItem> i = items.get(position).getItems();
        if (i.size() == 1 && position > 1)
            return TYPE_HEADLINE_MAIN;
        else if (i.size() == 1)
            return TYPE_HEADLINE;
        else if (i.size() == 2)
            return TYPE_MAIN_1;
        else if (i.size() == 3)
            return TYPE_MAIN_2;
        else
            return TYPE_ERROR;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (TYPE_HEADLINE == viewType) {
            v = LayoutInflater.from(context).inflate(R.layout.news_item_headline, parent, false);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
            layoutParams.setFullSpan(true);
            v.setLayoutParams(layoutParams);
            return new MainNewsItemHolder(v);
        }
        if (TYPE_MAIN_1 == viewType) {
            v = LayoutInflater.from(context).inflate(R.layout.news_item_main_2, parent, false);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
            layoutParams.setFullSpan(true);
            v.setLayoutParams(layoutParams);
            return new VH1(v);
        }

        if (TYPE_MAIN_2 == viewType) {
            v = LayoutInflater.from(context).inflate(R.layout.news_item_main_3, parent, false);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
            layoutParams.setFullSpan(true);
            v.setLayoutParams(layoutParams);
            return new VH2(v);
        }

        if (TYPE_HEADLINE_MAIN == viewType) {
            v = LayoutInflater.from(context).inflate(R.layout.news_item_main_4, parent, false);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
            layoutParams.setFullSpan(true);
            v.setLayoutParams(layoutParams);
            return new VH3(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VH3) {

            VH3 vh = (VH3) holder;
            NewsItem ni = items.get(position).getItems().get(0);

            ImageView i = vh.getSplash();

            setImage(ni, i);

            vh.getTitle().setText(ni.getPost().getTitle());

            if (ni.getPost().getTitle().length() > 55) {
                setTextSize(vh.getTitle(), 18);
            }

        }
        if (holder instanceof MainNewsItemHolder) {
            MainNewsItemHolder vh = (MainNewsItemHolder) holder;
            ImageView img = vh.getmSplash();
            TextView t = vh.getmTitle();

            int itemType = getItemType(items.get(position).getItems());


            if (itemType == TYPE_HEADLINE) {
                NewsItem ni = items.get(position).getItems().get(0);
                setImage(ni, img);

                t.setText(ni.getPost().getTitle());
                if (ni.getPost().getTitle().length() > 55) {
                    setTextSize(t, 18);
                }
            }

        }
        if (holder instanceof VH1) {

            VH1 vh = (VH1) holder;

            TextView t1, t2;
            TopAlignedImageView i1, i2;

            for (int ii = 0; ii < items.get(position).getItems().size(); ii++) {
                if (ii == 0) {
                    NewsItem ni = items.get(position).getItems().get(0);
                    t1 = vh.getTitle1();
                    i1 = vh.getSplash1();

                    t1.setText(ni.getPost().getTitle());
                    setImage(ni, i1);

                    setTextSize(t1, 18);
                } else {
                    NewsItem ni = items.get(position).getItems().get(1);
                    t2 = vh.getTitle2();
                    i2 = vh.getSplash2();

                    t2.setText(ni.getPost().getTitle());
                    setImage(ni, i2);

                    setTextSize(t2, 18);
                }
            }
        }
        if (holder instanceof VH2) {
            VH2 vh = (VH2) holder;

            TextView t1, t2, t3;
            TopAlignedImageView i1, i2, i3;

            for (int ii = 0; ii < items.get(position).getItems().size(); ii++) {
                if (ii == 0) {
                    NewsItem ni = items.get(position).getItems().get(0);
                    t1 = vh.getTitle1();
                    i1 = vh.getSplash1();

                    t1.setText(ni.getPost().getTitle());
                    setImage(ni, i1);

                    setTextSize(t1, 18);
                } else if (ii == 1) {
                    NewsItem ni = items.get(position).getItems().get(1);
                    t2 = vh.getTitle2();
                    i2 = vh.getSplash2();

                    t2.setText(ni.getPost().getTitle());
                    setImage(ni, i2);

                    setTextSize(t2, 14);
                } else {
                    NewsItem ni = items.get(position).getItems().get(2);
                    t3 = vh.getTitle3();
                    i3 = vh.getSplash3();

                    t3.setText(ni.getPost().getTitle());
                    setImage(ni, i3);

                    setTextSize(t3, 14);
                }
            }
        }
    }

    private void setImage(NewsItem ni, ImageView i) {
        Glide.with(context).load(ni.getImageUrl()).into(i);
    }

    private void setTextSize(TextView t, int size) {
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    private int getItemType(List<NewsItem> items) {
        int itemType;
        switch (items.size()) {
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

    @Override
    public int getItemCount() {
//        Log.e("TAG",String.valueOf(items.size()));
        return items.size();
    }

    static class VH3 extends RecyclerView.ViewHolder {
        @InjectView(R.id.root)
        LinearLayout mRoot;
        @InjectView(R.id.splash)
        TopAlignedImageView mSplash;
        @InjectViews({R.id.title, R.id.txtBody})
        List<CustomTextView> mText;

        public VH3(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

        public LinearLayout getmRoot() {
            return mRoot;
        }

        public TopAlignedImageView getSplash() {
            return mSplash;
        }

        public CustomTextView getTitle() {
            return mText.get(0);
        }

        public CustomTextView getBody() {
            return mText.get(1);
        }
    }
}
