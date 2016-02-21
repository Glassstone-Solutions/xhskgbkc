package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.interfaces.Callback;
import net.glassstones.thediarymagazine.models.NewsCluster;
import net.glassstones.thediarymagazine.models.NewsItem;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

/**
 * Created by Thompson on 17/02/2016.
 * For The Diary Magazine
 */
public class NewsFlipAdapter extends BaseAdapter {

    private static final int TYPE_HEADLINE_MAIN = 3;
    private static final int TYPE_HEADLINE = 0;
    private static final int TYPE_MAIN_1 = 1;
    private static final int TYPE_MAIN_2 = 2;
    private static final int TYPE_ERROR = -1;
    ViewHolder holder;
    private Context context;
    private List<NewsCluster> items;
    private LayoutInflater inflater;
    private Callback callback;

    public NewsFlipAdapter(Context c, List<NewsCluster> i) {
        this.context = c;
        this.items = i;
        inflater = LayoutInflater.from(context);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
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

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        int type = getItemViewType(position);
//        Log.e("TAG", items.get(position).getItems().get(0).getTitle());
        if (layout == null) {
            if (TYPE_HEADLINE == type) {
                layout = inflater.inflate(R.layout.news_item_headline, parent, false);
                holder = new Headline(layout);
                layout.setTag(holder);
            } else if (TYPE_MAIN_1 == type) {
                layout = inflater.inflate(R.layout.news_item_main_2, parent, false);
                holder = new VH1(layout);
                layout.setTag(holder);
            } else if (TYPE_MAIN_2 == type) {
                layout = inflater.inflate(R.layout.news_item_main_3, parent, false);
                holder = new VH2(layout);
                layout.setTag(holder);
            } else if (TYPE_HEADLINE_MAIN == type) {
                layout = inflater.inflate(R.layout.news_item_main_4, parent, false);
                holder = new VH3(layout);
                layout.setTag(holder);
            }
        } else {
            returnView(convertView, type);
        }

        bindView(position, type, holder);

        return layout;
    }

    private void returnView(View convertView, int type) {
        Log.e("TAG", String.valueOf(type));
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

    private void bindView(int position, int type, ViewHolder holder) {
        if (TYPE_HEADLINE == type) {
            bindHeadline(position, holder);
        } else if (TYPE_MAIN_1 == type) {
            bind1(position, holder);
        } else if (TYPE_MAIN_2 == type) {
            bind2(position, holder);
        } else if (TYPE_HEADLINE_MAIN == type) {
            bind3(position, holder);
        }
    }

    private void bindHeadline(int position, ViewHolder v) {
        if (v instanceof Headline) {
            Headline vh = (Headline) v;
            ImageView splash = vh.getmSplash();
            Glide.with(context).load(items.get(position).getItems().get(0).getImageUrl()).into(splash);
            TextView t = vh.getmTitle();
            t.setText(items.get(position).getItems().get(0).getTitle());
        }
    }

    private void bind1(int position, ViewHolder v) {
        if (v instanceof VH1) {
            VH1 vh = (VH1) v;
            TextView t1, t2;
            TopAlignedImageView i1, i2;

            for (int ii = 0; ii < items.get(position).getItems().size(); ii++) {
                if (ii == 0) {
                    NewsItem ni = items.get(position).getItems().get(0);
                    t1 = vh.getTitle1();
                    i1 = vh.getSplash1();

                    t1.setText(ni.getTitle());
                    setImage(ni, i1);

                    setTextSize(t1, 18);
                } else {
                    NewsItem ni = items.get(position).getItems().get(1);
                    t2 = vh.getTitle2();
                    i2 = vh.getSplash2();

                    t2.setText(ni.getTitle());
                    setImage(ni, i2);

                    setTextSize(t2, 18);
                }
            }

        }
    }

    private void bind2(int position, ViewHolder v) {
        if (v instanceof VH2) {
            VH2 vh = (VH2) v;

            TextView t1, t2, t3;
            TopAlignedImageView i1, i2, i3;

            for (int ii = 0; ii < items.get(position).getItems().size(); ii++) {
                if (ii == 0) {
                    NewsItem ni = items.get(position).getItems().get(0);
                    t1 = vh.getTitle1();
                    i1 = vh.getSplash1();

                    t1.setText(ni.getTitle());
                    setImage(ni, i1);

                    setTextSize(t1, 18);
                } else if (ii == 1) {
                    NewsItem ni = items.get(position).getItems().get(1);
                    t2 = vh.getTitle2();
                    i2 = vh.getSplash2();

                    t2.setText(ni.getTitle());
                    setImage(ni, i2);

                    setTextSize(t2, 14);
                } else {
                    NewsItem ni = items.get(position).getItems().get(2);
                    t3 = vh.getTitle3();
                    i3 = vh.getSplash3();

                    t3.setText(ni.getTitle());
                    setImage(ni, i3);

                    setTextSize(t3, 14);
                }
            }

        }
    }

    private void bind3(int position, ViewHolder v) {
        if (v instanceof VH3) {
            VH3 vh = (VH3) v;
            NewsItem ni = items.get(position).getItems().get(0);

            ImageView i = vh.getSplash();

            setImage(ni, i);

            vh.getTitle().setText(ni.getTitle());

            if (ni.getTitle().length() > 55) {
                setTextSize(vh.getTitle(), 18);
            }
        }
    }

    private void setImage(NewsItem ni, ImageView i) {
        Glide.with(context).load(ni.getImageUrl()).into(i);
    }

    private void setTextSize(TextView t, int size) {
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    static class ViewHolder {

    }

    static class Headline extends ViewHolder {
        @InjectView(R.id.splash)
        ImageView mSplash;
        @InjectView(R.id.title)
        TextView mTitle;

        public Headline(View v) {
            ButterKnife.inject(this, v);
        }

        public ImageView getmSplash() {
            return mSplash;
        }

        public TextView getmTitle() {
            return mTitle;
        }
    }

    static class VH1 extends ViewHolder {
        @InjectViews({R.id.splash1, R.id.splash2})
        List<TopAlignedImageView> splashes;
        @InjectViews({R.id.title1, R.id.title2})
        List<TextView> mText;

        public VH1(View v) {
            ButterKnife.inject(this, v);
        }

        public TopAlignedImageView getSplash1() {
            return splashes.get(0);
        }

        public TopAlignedImageView getSplash2() {
            return splashes.get(1);
        }

        public TextView getTitle1() {
            return mText.get(0);
        }

        public TextView getTitle2() {
            return mText.get(1);
        }
    }

    static class VH2 extends ViewHolder {
        @InjectViews({R.id.splash1, R.id.splash2, R.id.splash3})
        List<TopAlignedImageView> splashes;
        @InjectViews({R.id.title1, R.id.title2, R.id.title3})
        List<TextView> mText;

        public VH2(View v) {
            ButterKnife.inject(this, v);
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

    static class VH3 extends ViewHolder {
        @InjectView(R.id.root)
        LinearLayout mRoot;
        @InjectView(R.id.splash)
        TopAlignedImageView mSplash;
        @InjectViews({R.id.title, R.id.txtBody})
        List<CustomTextView> mText;

        public VH3(View v) {
            ButterKnife.inject(this, v);
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
