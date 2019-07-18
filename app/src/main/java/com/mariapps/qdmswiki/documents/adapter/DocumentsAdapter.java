package com.mariapps.qdmswiki.documents.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.model.DocumentsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentsAdapter extends CustomRecyclerView.Adapter<DocumentsAdapter.DocumentsVH> {

    private Context mContext;
    private ArrayList<DocumentsModel> documentsList;

    public DocumentsAdapter(Context context, ArrayList<DocumentsModel> list) {
        mContext = context;
        documentsList = list;
    }

    @NonNull
    @Override
    public DocumentsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.documents_list_row, viewGroup, false);
        return new DocumentsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentsVH holder, int i) {
        holder.tvHeadingText.setText(documentsList.get(i).getDocumentName());
        holder.tvCategory.setText(documentsList.get(i).getCategory());
        holder.tvDate.setText(documentsList.get(i).getDate());
        holder.tvTime.setText(documentsList.get(i).getTime());

        holder.rvDepartments.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        holder.rvDepartments.setHasFixedSize(true);

        TagsAdapter tagsAdapter = new TagsAdapter(mContext,documentsList.get(i).getDepartments());
        holder.rvDepartments.setAdapter(tagsAdapter);
    }

    @Override
    public int getItemCount() {
        return documentsList != null ? documentsList.size() : 0;
    }

    static class DocumentsVH extends CustomRecyclerView.ViewHolder {
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

}
