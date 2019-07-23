package com.mariapps.qdmswiki.documents.adapter;

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
import com.mariapps.qdmswiki.documents.model.DocumentsModel;
import com.mariapps.qdmswiki.home.adapter.RecommendedRecentlyAdapter;
import com.mariapps.qdmswiki.home.model.RecommendedRecentlyModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentsAdapter extends CustomRecyclerView.Adapter<DocumentsAdapter.DocumentsVH> implements Filterable {

    private Context mContext;
    private ArrayList<DocumentsModel> documentsList;
    private ArrayList<DocumentsModel> filterdDocumentsList;
    private RowClickListener rowClickListener;
    private String type;

    public DocumentsAdapter(Context context, ArrayList<DocumentsModel> list,String type) {
        mContext = context;
        this.documentsList = list;
        this.filterdDocumentsList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public DocumentsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_list_row, viewGroup, false);
        return new DocumentsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentsVH holder, int i) {

        DocumentsModel documentsModel = filterdDocumentsList.get(i);

        holder.tvHeadingText.setText(documentsModel.getDocumentName());
        holder.tvCategory.setText(documentsModel.getCategory());
        holder.tvDate.setText(documentsModel.getDate());
        holder.tvTime.setText(documentsModel.getTime());

        holder.rvDepartments.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.rvDepartments.setHasFixedSize(true);

        TagsAdapter tagsAdapter = new TagsAdapter(mContext,documentsModel.getDepartments());
        holder.rvDepartments.setAdapter(tagsAdapter);

        holder.rowLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("DOCUMENTS"))
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

                        ArrayList<DocumentsModel> filteredList = new ArrayList<>();

                        for (DocumentsModel documentsModel : documentsList) {
                            if (documentsModel.getDocumentName().toLowerCase().contains(charString.toLowerCase())) {

                                filteredList.add(documentsModel);
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
                    filterdDocumentsList = (ArrayList<DocumentsModel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

    static class DocumentsVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.rowLL)
        LinearLayout rowLL;
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;
        @BindView(R.id.tvCategory)
        CustomTextView tvCategory;
        @BindView(R.id.tvDate)
        CustomTextView tvDate;
        @BindView(R.id.tvTime)
        CustomTextView tvTime;
        @BindView(R.id.rvDepartments)
        CustomRecyclerView rvDepartments;

        public DocumentsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(DocumentsModel documentsModel) ;
    }

}
