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
import com.mariapps.qdmswiki.home.model.RecentlyViewedModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentlyViewedAdapter extends CustomRecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedVH> {

    private Context mContext;
    private List<RecentlyViewedModel> recentlyViewedList;
    private RecentlyViewedAdapter.RowClickListener rowClickListener;

    public RecentlyViewedAdapter(Context context, List<RecentlyViewedModel> list) {
        mContext = context;
        recentlyViewedList = list;
    }

    @NonNull
    @Override
    public RecentlyViewedAdapter.RecentlyViewedVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommended_recently_list_row, viewGroup, false);
        return new RecentlyViewedAdapter.RecentlyViewedVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecentlyViewedAdapter.RecentlyViewedVH holder, int i) {
        holder.headingTextTV.setText(recentlyViewedList.get(i).getDocumentName());
        holder.categoryTV.setText(recentlyViewedList.get(i).getCategoryName());
        holder.versionTV.setText("v "+recentlyViewedList.get(i).getVersion());

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClicked(recentlyViewedList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentlyViewedList != null ? recentlyViewedList.size() : 0;
    }

    static class RecentlyViewedVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.headingTextTV)
        CustomTextView headingTextTV;
        @BindView(R.id.categoryTV)
        CustomTextView categoryTV;
        @BindView(R.id.versionTV)
        CustomTextView versionTV;


        public RecentlyViewedVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRowClickListener(RecentlyViewedAdapter.RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(RecentlyViewedModel recentlyViewedModel) ;
    }

}
