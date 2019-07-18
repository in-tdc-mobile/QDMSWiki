package com.mariapps.qdmswiki.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.search.model.SearchTypeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTypeAdapter extends CustomRecyclerView.Adapter<SearchTypeAdapter.SearchTypeVH> {

    private Context mContext;
    private ArrayList<SearchTypeModel> searchTypeList;
    private ItemClickListener itemClickListener;

    public SearchTypeAdapter(Context context, ArrayList<SearchTypeModel> list) {
        mContext = context;
        searchTypeList = list;
    }

    @NonNull
    @Override
    public SearchTypeAdapter.SearchTypeVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_type_list_item, viewGroup, false);
        return new SearchTypeAdapter.SearchTypeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchTypeAdapter.SearchTypeVH holder, int i) {
        SearchTypeModel searchTypeModel = searchTypeList.get(holder.getAdapterPosition());
        holder.searchTypeTV.setText(searchTypeList.get(i).getSearchType());

        if(searchTypeModel.isSelected()){
            holder.mainLL.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.drawable_search_filter_selected,null));
        }else {
            holder.mainLL.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.drawable_search_filter_unselected,null));
        }

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(searchTypeList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchTypeList != null ? searchTypeList.size() : 0;
    }

    static class SearchTypeVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.mainLL)
        LinearLayout mainLL;
        @BindView(R.id.searchTypeTV)
        CustomTextView searchTypeTV;

        public SearchTypeVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void onItemClicked(SearchTypeModel item);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}

