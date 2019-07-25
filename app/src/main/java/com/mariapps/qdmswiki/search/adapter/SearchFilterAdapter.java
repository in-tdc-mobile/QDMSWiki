package com.mariapps.qdmswiki.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.search.model.SearchFilterModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFilterAdapter extends CustomRecyclerView.Adapter<SearchFilterAdapter.SearchTypeVH> {

    private Context mContext;
    private ArrayList<SearchFilterModel> searchTypeList;
    private ItemClickListener itemClickListener;

    public SearchFilterAdapter(Context context, ArrayList<SearchFilterModel> list) {
        mContext = context;
        searchTypeList = list;
    }

    @NonNull
    @Override
    public SearchFilterAdapter.SearchTypeVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_type_list_item, viewGroup, false);
        return new SearchFilterAdapter.SearchTypeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchFilterAdapter.SearchTypeVH holder, int i) {
        SearchFilterModel searchFilterModel = searchTypeList.get(holder.getAdapterPosition());
        holder.searchTypeTV.setText(searchTypeList.get(i).getSearchType());

        if(searchFilterModel.isSelected()){
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
        void onItemClicked(SearchFilterModel searchFilterModel);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}

