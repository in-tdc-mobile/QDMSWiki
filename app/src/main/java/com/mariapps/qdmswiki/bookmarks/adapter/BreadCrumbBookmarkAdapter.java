package com.mariapps.qdmswiki.bookmarks.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.search.model.BreadCrumbItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by elby.samson on 30,January,2019
 */
public class BreadCrumbBookmarkAdapter extends RecyclerView.Adapter<BreadCrumbBookmarkAdapter.BreadCrumbVH> {
    private List<BreadCrumbItem> breadCrumbList;
    private Context context;
    private BreadCrumbListener breadCrumbListener;

    public BreadCrumbBookmarkAdapter(List<BreadCrumbItem> breadCrumbList, Context context) {
        this.breadCrumbList = breadCrumbList;
        this.context = context;
    }

    @NonNull
    @Override
    public BreadCrumbVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bread_crumb, viewGroup, false);
        return new BreadCrumbVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BreadCrumbVH holder, final int i) {
        BreadCrumbItem breadCrumbItem=breadCrumbList.get(holder.getAdapterPosition());

        holder.titleTV.setText(breadCrumbItem.getHeading());
        if(holder.getAdapterPosition()==breadCrumbList.size()-1){
            holder.titleTV.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.black,null));
        }else {
            holder.titleTV.setTextColor(Color.parseColor("#000000"));
            holder.titleTV.setAlpha(0.54f);
        }

        holder.breadCrumbMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   breadCrumbListener.onClick((breadCrumbList.size()-1)-holder.getAdapterPosition(),breadCrumbItem);
            }
        });


    }

    @Override
    public int getItemCount() {
        return breadCrumbList !=null ? breadCrumbList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class BreadCrumbVH extends RecyclerView.ViewHolder {
        @BindView(R.id.appCompatImageView)
        AppCompatImageView appCompatImageView;
        @BindView(R.id.titleTV)
        CustomTextView titleTV;
        @BindView(R.id.breadCrumbMain)
        LinearLayout breadCrumbMain;

        public BreadCrumbVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface BreadCrumbListener{
        void onClick(int count, BreadCrumbItem breadCrumbItem);
    }
    public void setBreadCrumbListener(BreadCrumbListener breadCrumbListener){
        this.breadCrumbListener=breadCrumbListener;
    }

    public void removeItem(){
        breadCrumbList.remove(breadCrumbList.size()-1);
        notifyDataSetChanged();
    }
}
