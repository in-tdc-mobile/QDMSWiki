package com.mariapps.qdmswiki.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedRecentlyAdapter extends CustomRecyclerView.Adapter<RecommendedRecentlyAdapter.RecommendedRecentlyVH> {

    private Context mContext;
    private ArrayList<RecommendedRecentlyModel> departmentList;
    private RowClickListener rowClickListener;
    private String fromPage;

    public RecommendedRecentlyAdapter(Context context, ArrayList<RecommendedRecentlyModel> list, String fromActivity) {
        mContext = context;
        departmentList = list;
        fromPage = fromActivity;
    }

    @NonNull
    @Override
    public RecommendedRecentlyAdapter.RecommendedRecentlyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_recently_list_row, viewGroup, false);
        return new RecommendedRecentlyAdapter.RecommendedRecentlyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendedRecentlyAdapter.RecommendedRecentlyVH holder, int i) {
        holder.headingTextTV.setText(departmentList.get(i).getName());
        holder.categoryTV.setText(departmentList.get(i).getCategory());
        holder.versionTV.setText(departmentList.get(i).getVersionNo());

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(departmentList.get(holder.getAdapterPosition()));
            }
        });

        if(fromPage.equals("HOME")){
            holder.arrowIV.setVisibility(View.GONE);
            holder.historyIV.setVisibility(View.GONE);
        }
        else{
            holder.versionTV.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return departmentList != null ? departmentList.size() : 0;
    }

    static class RecommendedRecentlyVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.headingTextTV)
        CustomTextView headingTextTV;
        @BindView(R.id.categoryTV)
        CustomTextView categoryTV;
        @BindView(R.id.versionTV)
        CustomTextView versionTV;

        @BindView(R.id.arrowIV)
        AppCompatImageView arrowIV;
        @BindView(R.id.historyIV)
        AppCompatImageView historyIV;

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
