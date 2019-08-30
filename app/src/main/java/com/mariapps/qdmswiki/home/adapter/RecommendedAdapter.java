package com.mariapps.qdmswiki.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.model.DocumentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedAdapter extends CustomRecyclerView.Adapter<RecommendedAdapter.RecommendedRecentlyVH> {

    private Context mContext;
    private List<DocumentModel> recommendedRecentlyModelList;
    private RowClickListener rowClickListener;

    public RecommendedAdapter(Context context, List<DocumentModel> list) {
        mContext = context;
        recommendedRecentlyModelList = list;
    }

    @NonNull
    @Override
    public RecommendedAdapter.RecommendedRecentlyVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_recently_list_row, viewGroup, false);
        return new RecommendedAdapter.RecommendedRecentlyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendedAdapter.RecommendedRecentlyVH holder, int i) {
        holder.headingTextTV.setText(recommendedRecentlyModelList.get(i).getDocumentName());
        holder.categoryTV.setText(recommendedRecentlyModelList.get(i).getCategoryName());
        holder.versionTV.setText("v "+recommendedRecentlyModelList.get(i).getVersion());

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(recommendedRecentlyModelList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return recommendedRecentlyModelList != null ? recommendedRecentlyModelList.size() : 0;
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


        public RecommendedRecentlyVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(DocumentModel documentModel) ;
    }

}
