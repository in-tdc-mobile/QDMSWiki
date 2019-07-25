package com.mariapps.qdmswiki.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
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
import com.mariapps.qdmswiki.search.model.FilterBooleanItem;
import com.mariapps.qdmswiki.search.model.SearchModel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultAdapter extends CustomRecyclerView.Adapter<SearchResultAdapter.SearchVH>  implements Filterable {

    private Context mContext;
    private ArrayList<SearchModel> filteredItems;
    private ArrayList<SearchModel> searchList;
    private SearchResultAdapter.ItemClickListener itemClickListener;

    public SearchResultAdapter(Context context, ArrayList<SearchModel> list) {
        mContext = context;
        searchList = list;
        this.filteredItems = list;
    }

    @NonNull
    @Override
    public SearchResultAdapter.SearchVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_item, viewGroup, false);
        return new SearchResultAdapter.SearchVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultAdapter.SearchVH holder, int i) {
        SearchModel searchModel = filteredItems.get(holder.getAdapterPosition());
        holder.nameTV.setText(searchModel.getName());
        holder.descriptionTV.setText(searchModel.getDescription());

        if(searchModel.getType().equals("Folder"))
            holder.typeIV.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_folder_inactive,null));
        else if(searchModel.getType().equals("Document"))
            holder.typeIV.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_document_inactive,null));
        else if(searchModel.getType().equals("Article"))
            holder.typeIV.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_article_inactive,null));
        else if(searchModel.getType().equals("Forms"))
            holder.typeIV.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_document_inactive,null));


        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(searchList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems != null ? filteredItems.size() : 0;
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

    public void setItemClickListener(SearchResultAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Gson gson = new Gson();
                String charString = charSequence.toString();
                FilterBooleanItem filterBooleanItem = gson.fromJson(charString, FilterBooleanItem.class);
                if (filterBooleanItem.isFolderSelected() && filterBooleanItem.isDocumentSelected() && filterBooleanItem.isArticleSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder") ||
                                    searchModel.getType().equals("Document") ||
                                    searchModel.getType().equals("Article") ||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                } else if (filterBooleanItem.isFolderSelected() && filterBooleanItem.isDocumentSelected() && filterBooleanItem.isArticleSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder")||
                                    searchModel.getType().equals("Document") ||
                                    searchModel.getType().equals("Article")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }
                else if (filterBooleanItem.isDocumentSelected() && filterBooleanItem.isArticleSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Document")||
                                    searchModel.getType().equals("Article") ||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }
                else if (filterBooleanItem.isFolderSelected() && filterBooleanItem.isDocumentSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder")||
                                    searchModel.getType().equals("Document")||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }
                else if (filterBooleanItem.isFolderSelected() && filterBooleanItem.isArticleSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder")||
                                    searchModel.getType().equals("Article") ||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }
                else if (filterBooleanItem.isFolderSelected() && filterBooleanItem.isDocumentSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder")||
                                    searchModel.getType().equals("Document")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }


                else if (filterBooleanItem.isDocumentSelected() && filterBooleanItem.isArticleSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Document")||
                                    searchModel.getType().equals("Article")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else if (filterBooleanItem.isArticleSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Article")||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else if (filterBooleanItem.isFolderSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder")||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else if (filterBooleanItem.isDocumentSelected() && filterBooleanItem.isFormSelected()) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Document")||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }
                else if (filterBooleanItem.isFolderSelected() ) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else if (filterBooleanItem.isDocumentSelected() ) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Document")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else if (filterBooleanItem.isArticleSelected() ) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Article")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else if (filterBooleanItem.isFormSelected() ) {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    } else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;
                }

                else {
                    if (charString.isEmpty()) {
                        filteredItems = searchList;
                    }
                    else {
                        ArrayList<SearchModel> filteredList = new ArrayList<>();
                        for (SearchModel searchModel : searchList) {
                            if (searchModel.getType().equals("Folder") ||
                                    searchModel.getType().equals("Document") ||
                                    searchModel.getType().equals("Article") ||
                                    searchModel.getType().equals("Forms")) {
                                filteredList.add(searchModel);
                            }
                        }
                        filteredItems = filteredList;
                        ArrayList<SearchModel> filteredList1 = new ArrayList<>();
                        if (filterBooleanItem.getCharString() != null && !filterBooleanItem.getCharString().equals("")) {
                            for (SearchModel searchModel : filteredItems) {
                                if (searchModel.getName().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase()) ||
                                        searchModel.getDescription().toLowerCase().contains(filterBooleanItem.getCharString().toLowerCase())) {
                                    filteredList1.add(searchModel);
                                }
                            }
                            filteredItems.clear();
                            filteredItems = filteredList1;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredItems;
                    return filterResults;

                }
            }


            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItems = (ArrayList<SearchModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}


