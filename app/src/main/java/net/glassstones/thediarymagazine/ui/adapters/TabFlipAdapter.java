package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.Callback;
import net.glassstones.thediarymagazine.network.ServiceGenerator;
import net.glassstones.thediarymagazine.network.TDMAPIClient;
import net.glassstones.thediarymagazine.network.models.NI;
import net.glassstones.thediarymagazine.network.models.NewsCluster;
import net.glassstones.thediarymagazine.network.models.NewsItem;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.ui.widgets.TopAlignedImageView;
import net.glassstones.thediarymagazine.utils.UIUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

/**
 * Created by Thompson on 14/07/2016.
 * For The Diary Magazine
 */
public class TabFlipAdapter extends BaseAdapter {
    private static final String TAG = TabFlipAdapter.class.getSimpleName();
    private static final int TYPE_HEADLINE_MAIN = 3;
    private static final int TYPE_HEADLINE = 0;
    private static final int TYPE_MAIN_1 = 1;
    private static final int TYPE_MAIN_2 = 2;
    private static final int TYPE_ERROR = -1;

    private Context mContext;
    private ViewHolder holder;
    private List<NewsCluster> items;
    private LayoutInflater inflater;
    private Callback callback;
    private FirebaseAnalytics mTracker;

    private int screenWidth;

    public TabFlipAdapter (Context context, List<NewsCluster> items) {
        this.mContext = context;
        this.items = items;
        this.screenWidth = UIUtils.getScreenWidth(context);
        init();
    }

    private void init () {
        inflater = LayoutInflater.from(mContext);

        ServiceGenerator sg = new ServiceGenerator((Common) mContext.getApplicationContext());

        TDMAPIClient client = sg.createService(TDMAPIClient.class);

        mTracker = ((Common) mContext.getApplicationContext()).getmFirebaseAnalytics();
    }

    public void setCallback (Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount () {
        return items.size();
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
    public boolean hasStableIds(){
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
    public View getView (int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        int type = getItemViewType(position);

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
            if (TYPE_HEADLINE == type) {
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
        bindView(position, type, holder);

        return layout;
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
            final NewsItem item = items.get(position).getItems().get(0);

            Headline vh = (Headline) v;
            ImageView splash = vh.getmSplash();
            setImage(v, item.getNi(), splash, null);
            TextView t = vh.getmTitle();
            t.setText(Html.fromHtml(item.getNi().getTitle().title()));
            CustomTextView ct = vh.getExcerpt();
            ct.setText(Html.fromHtml(item.getNi().getExcerpt().excerpt()));
            vh.getRoot().setOnClickListener(v1 -> handleClick(position, item.getNi()));
        }
    }

    private void bind1(int position, ViewHolder v) {
        if (v instanceof VH1) {
            VH1 vh = (VH1) v;
            TextView t1, t2;
            TopAlignedImageView i1, i2;

            RelativeLayout root;

            for (int ii = 0; ii < items.get(position).getItems().size(); ii++) {
                if (ii == 0) {
                    NewsItem ni = items.get(position).getItems().get(0);
                    t1 = vh.getTitle1();
                    i1 = vh.getSplash1();

                    if (ni.getNi().getTitle().title() != null) {
                        t1.setText(Html.fromHtml(ni.getNi().getTitle().title()));
                    }
                    setImage(v, ni.getNi(), i1, 0);

                    setTextSize(t1, 18);

                    CustomTextView ct = vh.getExcerpts().get(0);

                    ct.setText(Html.fromHtml(ni.getNi().getExcerpt().excerpt()));

                    root = vh.getRoot().get(0);
                    root.setOnClickListener(v1 -> handleClick(position, ni.getNi()));

                } else {
                    NewsItem ni = items.get(position).getItems().get(1);
                    t2 = vh.getTitle2();
                    i2 = vh.getSplash2();

                    if (ni.getNi().getTitle().title() != null) {
                        t2.setText(Html.fromHtml(ni.getNi().getTitle().title()));
                    }
                    setImage(v, ni.getNi(), i2, 0);

                    setTextSize(t2, 18);

                    CustomTextView ct = vh.getExcerpts().get(1);

                    ct.setText(Html.fromHtml(ni.getNi().getExcerpt().excerpt()));

                    root = vh.getRoot().get(1);
                    root.setOnClickListener(v1 -> handleClick(position, ni.getNi()));

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

                    if (ni.getNi().getTitle().title() != null) {
                        t1.setText(Html.fromHtml(ni.getNi().getTitle().title()));
                    }
                    setImage(v, ni.getNi(), i1, 0);

                    setTextSize(t1, 18);

                    CustomTextView ct = vh.getExcerpts().get(0);

                    ct.setText(Html.fromHtml(ni.getNi().getExcerpt().excerpt()));

                    RelativeLayout root = vh.getRoot1();

                    root.setOnClickListener(v1 -> handleClick(position, ni.getNi()));

                } else if (ii == 1) {
                    NewsItem ni = items.get(position).getItems().get(1);
                    t2 = vh.getTitle2();
                    i2 = vh.getSplash2();

                    if (ni.getNi().getTitle().title() != null) {
                        t2.setText(Html.fromHtml(ni.getNi().getTitle().title()));
                    }
                    setImage(v, ni.getNi(), i2, 1);

                    setTextSize(t2, 14);

                    LinearLayout root = vh.getRootBottom().get(0);
                    root.setOnClickListener(v1 -> handleClick(position, ni.getNi()));

                } else {
                    NewsItem ni = items.get(position).getItems().get(2);
                    t3 = vh.getTitle3();
                    i3 = vh.getSplash3();

                    if (ni.getNi().getTitle().title() != null) {
                        t3.setText(Html.fromHtml(ni.getNi().getTitle().title()));
                    }
                    setImage(v, ni.getNi(), i3, 1);

                    setTextSize(t3, 14);

                    LinearLayout root = vh.getRootBottom().get(1);
                    root.setOnClickListener(v1 -> handleClick(position, ni.getNi()));

                }
            }

        }
    }

    private void bind3(int position, ViewHolder v) {
        if (v instanceof VH3) {
            VH3 vh = (VH3) v;
            NewsItem ni = items.get(position).getItems().get(0);

            ImageView i = vh.getSplash();

            setImage(v, ni.getNi(), i, 0);

            TextView t = vh.getTitle();

            if (ni.getNi().getTitle().title() != null) {
                t.setText(Html.fromHtml(ni.getNi().getTitle().title()));
            }

            if (ni.getNi().getTitle().title().length() > 55) {
                setTextSize(vh.getTitle(), 18);
            }

            CustomTextView ct = vh.getBody();

            ct.setText(Html.fromHtml(ni.getNi().getExcerpt().excerpt()));

            LinearLayout root = vh.getmRoot();

            root.setOnClickListener(v1 -> handleClick(position, ni.getNi()));

        }
    }

    private void setImage (ViewHolder v, NI p, ImageView i, @NonNull Integer position) {
        int width = screenWidth;

        if (v instanceof VH2 && position == 1){
            width = screenWidth/2;
        }


        Glide.with(mContext)
                .load(p.getMedia().getSourceUrl())
                .override(width, UIUtils.getScreenHeight(mContext)/3)
                .fitCenter()
                .crossFade()
                .into(i);
    }
    private void setTextSize(TextView t, int size) {
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    private void handleClick (int position, NI p) {
        if (p != null && callback != null) {
            Log.e(TAG, String.valueOf(position));
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

    public void update (List<NewsCluster> niCluster) {
        items.clear();
        items = niCluster;
        notifyDataSetChanged();
    }

    public void append (List<NewsCluster> clusters) {
        for (NewsCluster nc : clusters){
            items.add(nc);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {

    }

    static class Headline extends ViewHolder {
        @InjectView(R.id.splash)
        ImageView mSplash;
        @InjectView(R.id.title)
        TextView mTitle;
        @InjectView(R.id.txtBody1)
        CustomTextView mExcerpt;
        @InjectView(R.id.root)
        RelativeLayout root;

        public Headline(View v) {
            ButterKnife.inject(this, v);
        }

        public ImageView getmSplash() {
            return mSplash;
        }

        public TextView getmTitle() {
            return mTitle;
        }

        public CustomTextView getExcerpt() {
            return mExcerpt;
        }

        public RelativeLayout getRoot() {
            return root;
        }
    }

    static class VH1 extends ViewHolder {
        @InjectViews({R.id.splash1, R.id.splash2})
        List<TopAlignedImageView> splashes;
        @InjectViews({R.id.title1, R.id.title2})
        List<TextView> mText;
        @InjectViews({R.id.txtBody1, R.id.txtBody2})
        List<CustomTextView> excerpts;
        @InjectViews({R.id.root1, R.id.root2})
        List<RelativeLayout> root;

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

        public List<CustomTextView> getExcerpts() {
            return excerpts;
        }

        public List<RelativeLayout> getRoot() {
            return root;
        }
    }

    static class VH2 extends ViewHolder {
        @InjectViews({R.id.splash1, R.id.splash2, R.id.splash3})
        List<TopAlignedImageView> splashes;
        @InjectViews({R.id.title1, R.id.title2, R.id.title3})
        List<TextView> mText;

        @InjectViews({R.id.txtBody1})
        List<CustomTextView> excerpts;

        @InjectView(R.id.root1)
        RelativeLayout root1;
        @InjectViews({R.id.root2, R.id.root3})
        List<LinearLayout> rootBottom;

        public VH2(View v) {
            ButterKnife.inject(this, v);
        }

        public List<CustomTextView> getExcerpts() {
            return excerpts;
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

        public RelativeLayout getRoot1() {
            return root1;
        }

        public List<LinearLayout> getRootBottom() {
            return rootBottom;
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

        public LinearLayout getmRoot() {
            return mRoot;
        }
    }

}
