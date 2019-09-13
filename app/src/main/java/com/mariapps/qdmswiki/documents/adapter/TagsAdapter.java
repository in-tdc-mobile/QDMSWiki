package com.mariapps.qdmswiki.documents.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.model.TagModel;
import com.mariapps.qdmswiki.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagsAdapter extends CustomRecyclerView.Adapter<TagsAdapter.DepartmentsVH> {

    private Context mContext;
    private List<TagModel> tagList;

    public TagsAdapter(Context context, List<TagModel> list) {
        mContext = context;
        tagList = list;
    }

    @NonNull
    @Override
    public TagsAdapter.DepartmentsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tag_list_row, viewGroup, false);
        return new TagsAdapter.DepartmentsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsAdapter.DepartmentsVH holder, int i) {
        holder.tvHeadingText.setText(CommonUtils.toTitleCase(tagList.get(i).getName()));
    }

    @Override
    public int getItemCount() {
        return tagList != null ? tagList.size() : 0;
    }

    static class DepartmentsVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;

        public DepartmentsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
