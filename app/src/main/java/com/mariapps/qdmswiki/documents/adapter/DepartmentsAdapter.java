package com.mariapps.qdmswiki.documents.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.model.DepartmentModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsVH> {

    private Context mContext;
    private ArrayList<DepartmentModel> departmentList;

    public DepartmentsAdapter(Context context, ArrayList<DepartmentModel> list) {
        mContext = context;
        departmentList = list;
    }

    @NonNull
    @Override
    public DepartmentsAdapter.DepartmentsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.department_list_row, viewGroup, false);
        return new DepartmentsAdapter.DepartmentsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DepartmentsAdapter.DepartmentsVH holder, int i) {
        holder.tvHeadingText.setText(departmentList.get(i).getpDepartmentName());
    }

    @Override
    public int getItemCount() {
        return departmentList != null ? departmentList.size() : 0;
    }

    static class DepartmentsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;

        public DepartmentsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
