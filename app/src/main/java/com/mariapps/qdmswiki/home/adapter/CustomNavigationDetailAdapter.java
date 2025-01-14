package com.mariapps.qdmswiki.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.home.model.NavDrawerObj;
import com.mariapps.qdmswiki.search.model.SearchModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomNavigationDetailAdapter extends RecyclerView.Adapter<CustomNavigationDetailAdapter.CustomNavigationDetailVH1> {

    private Context context;
    private NavAdapterListener navAdapterListener;
    List<SearchModel> navigationItems;

    public CustomNavigationDetailAdapter(Context context, List<SearchModel> list) {
        this.context = context;
        this.navigationItems = list;
    }


    @NonNull
    @Override
    public CustomNavigationDetailVH1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom_drawer, viewGroup, false);
        return new CustomNavigationDetailVH1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomNavigationDetailVH1 customNavigationDetailVH1, int i) {
        if (customNavigationDetailVH1.getAdapterPosition() < navigationItems.size()) {
            final SearchModel searchModel = navigationItems.get(customNavigationDetailVH1.getAdapterPosition());
            if (searchModel != null) {
                customNavigationDetailVH1.drawerItemName.setText(searchModel.getName());
                if(searchModel.getType().equals("Category")) {
                    customNavigationDetailVH1.drawerIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_folder));
                    customNavigationDetailVH1.arrowIV.setVisibility(View.VISIBLE);
                    customNavigationDetailVH1.arrowIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_inactive));
                }
                else if(searchModel.getType().equals("Document")) {
                    customNavigationDetailVH1.drawerIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_document_active));
                    customNavigationDetailVH1.arrowIV.setVisibility(View.GONE);
                }
                else if(searchModel.getType().equals("Article")) {
                    customNavigationDetailVH1.drawerIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_article_active));
                    customNavigationDetailVH1.arrowIV.setVisibility(View.GONE);
                }
            }
            customNavigationDetailVH1.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    navAdapterListener.onItemClick(navigationItems.get(customNavigationDetailVH1.getAdapterPosition()));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return navigationItems != null ? navigationItems.size() : 0;
    }


    static class CustomNavigationDetailVH1 extends RecyclerView.ViewHolder {

        @BindView(R.id.drawer_icon)
        AppCompatImageView drawerIcon;
        @BindView(R.id.drawer_itemName)
        CustomTextView drawerItemName;
        @BindView(R.id.arrowIV)
        AppCompatImageView arrowIV;
        @BindView(R.id.mainRL)
        LinearLayout mainRL;
        @BindView(R.id.seperatorView)
        View seperatorView;
        @BindView(R.id.itemLayout)
        LinearLayout itemLayout;

        public CustomNavigationDetailVH1(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface NavAdapterListener {
        void onItemClick(SearchModel searchModel);
    }

    public void setNavAdapterListener(NavAdapterListener navAdapterListener) {
        this.navAdapterListener = navAdapterListener;
    }


}

