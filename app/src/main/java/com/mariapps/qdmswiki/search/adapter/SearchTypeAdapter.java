package com.mariapps.qdmswiki.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.search.model.SearchTypeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTypeAdapter extends RecyclerView.Adapter<SearchTypeAdapter.SearchTypeVH> {

    private Context mContext;
    private ArrayList<SearchTypeModel> searchTypeList;

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
        holder.searchTypeTV.setText(searchTypeList.get(i).getSearchType());
    }

    @Override
    public int getItemCount() {
        return searchTypeList != null ? searchTypeList.size() : 0;
    }

    static class SearchTypeVH extends RecyclerView.ViewHolder {
        @BindView(R.id.searchTypeTV)
        CustomTextView searchTypeTV;

        public SearchTypeVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

