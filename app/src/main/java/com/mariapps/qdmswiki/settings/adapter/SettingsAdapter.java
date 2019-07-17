package com.mariapps.qdmswiki.settings.adapter;

import android.content.Context;
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
import com.mariapps.qdmswiki.settings.model.SettingsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by elby.samson on 21,February,2019
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsVH> {



    private Context context;
    private List<SettingsItem> settingsItems;
    private SettingsListener settingsListener;

    public SettingsAdapter(Context context, List<SettingsItem> settingsItems) {
        this.context = context;
        this.settingsItems = settingsItems;
    }

    @NonNull
    @Override
    public SettingsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_settings, viewGroup, false);
        return new SettingsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingsVH holder, int i) {
        SettingsItem settingsItem=settingsItems.get(holder.getAdapterPosition());
        holder.settingNameTV.setText(settingsItem.getName());
        holder.iconIV.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),settingsItem.getImage(),null));
        holder.settingNameTV.setTextColor(ResourcesCompat.getColor(context.getResources(),settingsItem.getTextColor(),null));
        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsListener.onSettingsClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsItems != null ? settingsItems.size() : 0;
    }

    static class SettingsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iconIV)
        AppCompatImageView iconIV;
        @BindView(R.id.settingNameTV)
        CustomTextView settingNameTV;
        @BindView(R.id.mainLL)
        LinearLayout mainLL;

        public SettingsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SettingsListener{
        void onSettingsClicked(int position);
    }

    public void setSettingsListener(SettingsListener settingsListener){
        this.settingsListener=settingsListener;
    }
}
