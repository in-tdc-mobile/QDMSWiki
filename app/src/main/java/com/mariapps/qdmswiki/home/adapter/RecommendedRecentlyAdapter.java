package com.mariapps.qdmswiki.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.model.DepartmentModel;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedRecentlyAdapter extends RecyclerView.Adapter<RecommendedRecentlyAdapter.RecommendedRecentlyVH> {

    private Context mContext;
    private ArrayList<RecommendedRecentlyModel> departmentList;
    private RowClickListener rowClickListener;

    public RecommendedRecentlyAdapter(Context context, ArrayList<RecommendedRecentlyModel> list) {
        mContext = context;
        departmentList = list;
    }

    @NonNull
    @Override
    public RecommendedRecentlyAdapter.RecommendedRecentlyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_recently_list_row, viewGroup, false);
        return new RecommendedRecentlyAdapter.RecommendedRecentlyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendedRecentlyAdapter.RecommendedRecentlyVH holder, int i) {
        holder.tvHeadingText.setText(departmentList.get(i).getName());
        holder.tvCategory.setText(departmentList.get(i).getCategory());
        holder.tvVersion.setText(departmentList.get(i).getVersionNo());

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(departmentList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentList != null ? departmentList.size() : 0;
    }

    static class RecommendedRecentlyVH extends RecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;
        @BindView(R.id.tvCategory)
        CustomTextView tvCategory;
        @BindView(R.id.tvVersion)
        CustomTextView tvVersion;

        public RecommendedRecentlyVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(RecommendedRecentlyModel recommendedRecentlyModel) ;
    }

}
