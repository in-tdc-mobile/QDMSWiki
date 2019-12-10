package com.mariapps.qdmswiki.notification.adapter;

import android.content.Context;
import android.os.Build;
import android.service.voice.VoiceInteractionSessionService;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.SessionManager;
import com.mariapps.qdmswiki.custom.CustomRecyclerView;
import com.mariapps.qdmswiki.custom.CustomTextView;
import com.mariapps.qdmswiki.documents.adapter.DocumentsAdapter;
import com.mariapps.qdmswiki.home.model.DocumentModel;
import com.mariapps.qdmswiki.notification.model.NotificationModel;
import com.mariapps.qdmswiki.utils.CommonUtils;
import com.mariapps.qdmswiki.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends CustomRecyclerView.Adapter<NotificationAdapter.NotificationsVH> {

    private Context mContext;
    private List<NotificationModel> notificationList;
    private SessionManager sessionManager;
    private RowClickListener rowClickListener;

    public NotificationAdapter(Context context, List<NotificationModel> list) {
        mContext = context;
        notificationList = list;
        sessionManager = new SessionManager(mContext);
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_row, viewGroup, false);
        return new NotificationAdapter.NotificationsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.NotificationsVH holder, int position) {
        NotificationModel notificationModel = notificationList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvHeadingText.setText(Html.fromHtml(notificationModel.getMessage(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvHeadingText.setText(Html.fromHtml(notificationModel.getMessage()));
        }
        holder.statusTV.setText("Last updated by " + " : ");
        for(int i=0;i<notificationModel.getReceviers().size();i++){
            if(notificationModel.getIsUnread()) {
                holder.linNotification.setBackgroundColor(mContext.getResources().getColor(R.color.grey_200));
                holder.imgStatus.setVisibility(View.VISIBLE);

            }
            else if(!notificationModel.getIsUnread()){
                holder.linNotification.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                holder.imgStatus.setVisibility(View.GONE);
            }
        }
        holder.updatedByTV.setText(notificationModel.getSenderName());
        holder.tvTime.setText(DateUtils.getFormattedDate(notificationModel.getSendTime()));

        holder.linNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    rowClickListener.onItemClicked(notificationList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    static class NotificationsVH extends CustomRecyclerView.ViewHolder {
        @BindView(R.id.linNotification)
        LinearLayout linNotification;
        @BindView(R.id.tvHeadingText)
        CustomTextView tvHeadingText;
        @BindView(R.id.statusTV)
        CustomTextView statusTV;
        @BindView(R.id.updatedByTV)
        CustomTextView updatedByTV;
        @BindView(R.id.tvTime)
        CustomTextView tvTime;
        @BindView(R.id.imgStatus)
        AppCompatImageView imgStatus;

        public NotificationsVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRowClickListener(RowClickListener rowClickListener) {
        this.rowClickListener = rowClickListener;
    }

    public interface RowClickListener {
        void onItemClicked(NotificationModel notificationModel) ;
    }

}
