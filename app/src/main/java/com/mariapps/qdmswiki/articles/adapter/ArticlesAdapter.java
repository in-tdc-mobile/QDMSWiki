package com.mariapps.qdmswiki.articles.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.adapter.TagsAdapter;
import com.mariapps.qdmswiki.home.model.ArticleModel;
import com.mariapps.qdmswiki.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends CustomRecyclerView.Adapter<ArticlesAdapter.ArticlesVH> implements Filterable {

    private Context mContext;
    private List<ArticleModel> documentsList;
    private List<ArticleModel> filterdDocumentsList;
    private ArticlesAdapter.RowClickListener rowClickListener;
    private String type;

    public ArticlesAdapter(Context context, List<ArticleModel> list) {
        mContext = context;
        this.documentsList = list;
        this.filterdDocumentsList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public ArticlesAdapter.ArticlesVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_list_row, viewGroup, false);
        return new ArticlesAdapter.ArticlesVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticlesAdapter.ArticlesVH holder, int i) {

        ArticleModel articleModel = filterdDocumentsList.get(i);
        String categoriesString = "";
        holder.tvHeadingText.setText(articleModel.getArticleName());
        List<String> categories = articleModel.getCategoryNames();
        for(int j=0; j<categories.size(); j++){
            if(j == categories.size() - 1)
                categoriesString = categoriesString + categories.get(j);
            else
                categoriesString = categoriesString + categories.get(j) + ",";

        }

        holder.tvCategory.setText(categoriesString);
        holder.tvDate.setText(DateUtils.getFormattedDate(articleModel.getDate()));
        holder.tvVersion.setText("V "+String.valueOf(articleModel.getVersion()));

        holder.rvDepartments.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.rvDepartments.setHasFixedSize(true);

        TagsAdapter tagsAdapter = new TagsAdapter(mContext,articleModel.getTags());
        holder.rvDepartments.setAdapter(tagsAdapter);

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(type.equals("DOCUMENTS"))
                    rowClickListener.onItemClicked(documentsList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterdDocumentsList != null ? filterdDocumentsList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filterdDocumentsList = documentsList;
                } else {

                    ArrayList<ArticleModel> filteredList = new ArrayList<>();

                    for (ArticleModel articleModel : documentsList) {
                        if (articleModel.getArticleName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(articleModel);
                        }
                    }

                    filterdDocumentsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterdDocumentsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterdDocumentsList = (ArrayList<ArticleModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ArticlesVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;
        @BindView(R.id.tvCategory)
        CustomTextView tvCategory;
        @BindView(R.id.tvDate)
        CustomTextView tvDate;
        @BindView(R.id.tvVersion)
        CustomTextView tvVersion;
        @BindView(R.id.rvDepartments)
        CustomRecyclerView rvDepartments;

        public ArticlesVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRowClickListener(ArticlesAdapter.RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(ArticleModel articleModel) ;
    }

}

