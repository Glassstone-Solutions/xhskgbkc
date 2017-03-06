package net.glassstones.thediarymagazine.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.network.models.NI;

import net.glassstones.thediarymagazine.ui.fragments.FavFragment.OnFavListInteraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NI} and makes a call to the
 * specified {@link OnFavListInteraction}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNIRecyclerViewAdapter extends RecyclerView.Adapter<MyNIRecyclerViewAdapter.ViewHolder> {

    private final List<NI> mValues;
    private final OnFavListInteraction mListener;

    public MyNIRecyclerViewAdapter(List<NI> items, OnFavListInteraction listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ni, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle().title());
        Document t = Jsoup.parse(mValues.get(position).getExcerpt().excerpt());
        holder.mContentView.setText(t.body().text());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onNewsClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        NI mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
