package com.mariapps.qdmswiki.serviceclasses;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mariapps.qdmswiki.R;
import com.mariapps.qdmswiki.home.view.HomeActivity;

import java.util.Map;


/**
 * Created by aruna.ramakrishnan on 08/26/2019.
 */
public class FcmMessageService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;
    final String CHANNEL_ID = "10001";
    // The user-visible name of the channel.
    final String CHANNEL_NAME = "Default";
    PendingIntent contentIntent;

    static void updateMyActivity(Context context, String count) {

        Intent intent = new Intent("notifications");

        //put whatever data you want to send, if any
        intent.putExtra("count", count);

        //send broadcast
        context.sendBroadcast(intent);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData()!=null){
            Map<String, String> notify;
            notify = remoteMessage.getData();

            String notification_Id = notify.get("Notification_Id");
            String msgType = notify.get("msg_type");
            String count = notify.get("Count");
            String sound = notify.get("sound");
            String message = notify.get("body");
            String badge = notify.get("badge");
            String title = notify.get("title");
            String refId = notify.get("refid");

            sendNotification(message,msgType,notification_Id,count,title,refId);
            }
    }

    private void sendNotification(String msg, String msgType, String notificationId, String notificationCount, String title, String refId) {

        updateMyActivity(this, notificationCount);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fromNotification", true);
        intent.putExtra("notificationPage", msgType);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("refId", refId);
        intent.putExtra("notificationDate", System.currentTimeMillis());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        contentIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initChannels(this,msg, title);
        }
        else{
            mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initChannels(Context context, String msg, String title) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        notificationChannel.setShowBadge(true);
        notificationChannel.setName("LiveFleet");
        notificationChannel.setDescription(msg);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg)
                .setSmallIcon(R.drawable.app_icon)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

}
