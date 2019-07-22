package com.mariapps.qdmswiki.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.search.model.SearchModel;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends CustomRecyclerView.Adapter<SearchAdapter.SearchVH> {

    private Context mContext;
    private ArrayList<SearchModel> searchList;
    private SearchAdapter.ItemClickListener itemClickListener;

    public SearchAdapter(Context context, ArrayList<SearchModel> list) {
        mContext = context;
        searchList = list;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_item, viewGroup, false);
        return new SearchAdapter.SearchVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.SearchVH holder, int i) {
        SearchModel searchModel = searchList.get(holder.getAdapterPosition());
        holder.nameTV.setText(searchModel.getName());
        holder.descriptionTV.setText(searchModel.getDescription());

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(searchList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList != null ? searchList.size() : 0;
    }

    static class SearchVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.mainLL)
        LinearLayout mainLL;
        @BindView(R.id.typeIV)
        AppCompatImageView typeIV;
        @BindView(R.id.nameTV)
        CustomTextView nameTV;
        @BindView(R.id.descriptionTV)
        CustomTextView descriptionTV;
        @BindView(R.id.openIV)
        AppCompatImageView openIV;

        public SearchVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void onItemClicked(SearchModel item);
    }

    public void setItemClickListener(SearchAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}


