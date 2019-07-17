package com.mariapps.qdmswiki.notification.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.notification.model.NotificationModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationsVH> {

    private Context mContext;
    private ArrayList<NotificationModel> notificationList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        mContext = context;
        notificationList = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_row, viewGroup, false);
        return new NotificationAdapter.NotificationsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.NotificationsVH holder, int i) {
        holder.tvHeadingText.setText(notificationList.get(i).getHeading());
        holder.statusTV.setText(notificationList.get(i).getStatus() + " : ");
        holder.updatedByTV.setText(notificationList.get(i).getUpdatedBy());
        holder.tvTime.setText(notificationList.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    static class NotificationsVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;
        @BindView(R.id.statusTV)
        CustomTextView statusTV;
        @BindView(R.id.updatedByTV)
        CustomTextView updatedByTV;
        @BindView(R.id.tvTime)
        CustomTextView tvTime;

        public NotificationsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
