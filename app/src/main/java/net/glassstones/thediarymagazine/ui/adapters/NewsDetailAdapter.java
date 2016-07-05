package net.glassstones.thediarymagazine.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.models.News;
import net.glassstones.thediarymagazine.ui.viewholders.DetailsParagraphViewHolder;
import net.glassstones.thediarymagazine.ui.viewholders.DetailsTitleViewHolder;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;

import org.jsoup.nodes.Element;

/**
 * Created by Thompson on 28/06/2016.
 * For The Diary Magazine
 */
public class NewsDetailAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_PARA = 1;
    private Context context;
    private News news;

    public NewsDetailAdapter (Context c, News n) {
        this.context = c;
        this.news = n;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v;

        if (viewType == TYPE_TITLE) {
            v = LayoutInflater.from(context).inflate(R.layout.details_title_layout, parent,
                    false);
            return new DetailsTitleViewHolder(v);
        } else if (viewType == TYPE_PARA) {
            v = LayoutInflater.from(context).inflate(R.layout.details_paragraph_layout, parent,
                    false);
            return new DetailsParagraphViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DetailsTitleViewHolder) {
            DetailsTitleViewHolder vh = (DetailsTitleViewHolder) holder;
            bindTitle(vh, news);
        } else if (holder instanceof DetailsParagraphViewHolder) {
            DetailsParagraphViewHolder vh = (DetailsParagraphViewHolder) holder;
            LinearLayout root = vh.getRoot();

            if (news.getFirstParagraph() != null) {
                TextView tv1 = getTextPara(news.getFirstParagraph());
                if (tv1 != null) {
                    tv1.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    root.addView(tv1);
                }
            }

            for (Element element : news.getOtherParagraphs()) {
                TextView tv = getTextPara(element);
                if (tv != null) {
                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    root.addView(tv);
                }
            }
        }
    }

    private void bindTitle (DetailsTitleViewHolder vh, News news) {
        CustomTextView title = vh.getTitle();
        CustomTextView source = vh.getSource();
        CustomTextView timestamp = vh.getTimestamp();

        title.setText(Html.fromHtml(news.getTitle()));
        source.setText(R.string.source_placeholder);
        timestamp.setText(news.getDate());
    }

    private TextView getTextPara (Element element) {
        TextView valueTV = new TextView(context);
        valueTV.setText(String.format("%s\n", element.text()));
        return element.text().isEmpty() ? null : valueTV;
    }

    @Override
    public int getItemViewType (int position) {
        if (position == 0) {
            return TYPE_TITLE;
        } else if (position == TYPE_PARA) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount () {
        return 2;
    }
}
