package net.glassstones.thediarymagazine.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.ui.activities.LoginActivity;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.ui.widgets.ImageViewTopCrop;
import net.glassstones.thediarymagazine.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private static final int CAT_COUNT = 6;

    @InjectView(R.id.cat_list)
    RecyclerView list;
    String[] labels = {"News", "Business", "Entertainment", "Sports", "Lifestyle", "Well Being"};
    int[] cat_id = {20, 4, 27, 14, 34, 37};
    int[] images = {
            R.drawable.img_news,
            R.drawable.img_business,
            R.drawable.img_entertainment,
            R.drawable.img_sports,
            R.drawable.img_lifestyle,
            R.drawable.img_wellbeing
    };

    CategoryAdapter adapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adapter = new CategoryAdapter();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    protected class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_CAT = 0;
        private static final int TYPE_MORE = 1;
        private final String TAG = CategoryAdapter.class.getSimpleName();
        private final int cellSize;
        int mutedColor = R.attr.colorPrimary;


        public CategoryAdapter() {
            this.cellSize = UIUtils.getScreenWidth(getActivity()) / 2;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (TYPE_CAT == viewType) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.cat_item_layout, parent, false);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
                layoutParams.height = cellSize;
                layoutParams.width = cellSize;
                layoutParams.setFullSpan(false);
                v.setLayoutParams(layoutParams);
                return new CatViewHolder(v);
            } else {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.view_category_footer, parent, false);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) v.getLayoutParams();
                layoutParams.setFullSpan(true);
                v.setLayoutParams(layoutParams);
                return new CatViewFooterHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CatViewHolder) {
                bindCat((CatViewHolder) holder, position);
            } else {
                bindFooter((CatViewFooterHolder) holder);
            }
        }

        private void bindCat(CatViewHolder holder, int position) {
            CustomTextView textView = holder.getLabel();
            ImageView bg = holder.getBg();


            textView.setText(labels[position]);
            Glide.with(getActivity()).load(images[position]).into(bg);
        }

        private void bindFooter(CatViewFooterHolder holder) {

        }

        @Override
        public int getItemCount() {
            return CAT_COUNT + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == CAT_COUNT) {
                return TYPE_MORE;
            } else {
                return TYPE_CAT;
            }
        }

        class CatViewHolder extends RecyclerView.ViewHolder {
            @InjectView(R.id.bg_img)
            ImageView bg;
            @InjectView(R.id.label)
            CustomTextView label;

            public CatViewHolder(View itemView) {
                super(itemView);
                ButterKnife.inject(this, itemView);
            }

            @OnClick({R.id.bg_img, R.id.label})
            void onAnyClick(View v){
                int pos = getAdapterPosition();
                // TODO: Create Category filtered news flip activity
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.putExtra("cat", cat_id[pos]);
                getActivity().startActivity(i);
            }

            public ImageView getBg() {
                return bg;
            }

            public CustomTextView getLabel() {
                return label;
            }
        }

        class CatViewFooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public CatViewFooterHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void onClick(View v) {

            }
        }

    }
}
