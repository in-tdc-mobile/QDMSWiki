package com.mariapps.qdmswiki;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mariapps.qdmswiki.home.model.DownloadFilesResponseModel;
import com.mariapps.qdmswiki.home.view.HomeActivity;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DownloadService extends Service {
    int downloadID=0;
    final String CHANNEL_ID = "10001";
    final String CHANNEL_NAME = "Default";
    PendingIntent contentIntent;
  public static   String url="";
    public static String filename="";
    NotificationManager notificationManager;
    ArrayList<DownloadFilesResponseModel.DownloadEntityList> downloadEntityLists = new ArrayList();
    int urlNum=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onHandleIntent","started");
        url = intent.getStringExtra("url");
        filename = intent.getStringExtra("filename");
        downloadEntityLists=intent.getParcelableArrayListExtra("downloadEntityLists") ;
        urlNum=Integer.valueOf(intent.getStringExtra("urlNum"));

       // Intent pintent = new Intent(this, HomeActivity.class);
       // contentIntent = PendingIntent.getActivity(this, 1, pintent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(url!=null)
        //beginDownload(url,filename);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initChannels(this,"Downloading Files", "QDMS");
        }
        else{
            notificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("QDMS")
                   // .setContentIntent(contentIntent)
                    .setContentText("Downloading Files");
            notificationManager.notify(1, mBuilder.build());
            startForeground(1,mBuilder.build());
        }
        beginDownload(url,filename);
        return START_STICKY;
    }


    public void beginDownload(String url, String zipFileName) {
        AppConfig.getDwnldstarted().postValue("started");
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .enableRetryOnNetworkGain(true)
                .build();
       Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);
        final Request request = new Request(url, Environment.getExternalStorageDirectory() + "/QDMSWiki/" + zipFileName);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);
        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG");
        fetch.enqueue(request, updatedRequest -> {
        }, error -> {
            //An error occurred enqueuing the request.
        });
        FetchListener     fetchListener = new FetchListener() {




            @Override
            public void onWaitingNetwork(@NotNull Download download) {
                fetch.resume(downloadID);
            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
                if (request.getId() == download.getId()) {
                    downloadID = download.getId();
                }
            }

            @Override
            public void onProgress(@NotNull Download download, long l, long l1) {
                int progress = download.getProgress();
                AppConfig.getDwnldprgress().postValue(progress);
            }
            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {
               // appendLog("Error while Downloading " + error.getHttpResponse());
                AppConfig.getDwnlderror().postValue("error");

            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onAdded(@NotNull Download download) {

            }

            @Override
            public void onQueued(@NotNull Download download, boolean waitingOnNetwork) {
                if (request.getId() == download.getId()) {
                    //showDownloadInList(download);
                }
            }



            @Override
            public void onCompleted(@NotNull Download download) {
                //Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            //    appendLog("Downloading " + url + " completed");
               // progressLayout.setVisibility(View.GONE);
              //  HomeActivity.Decompress decompress = new HomeActivity.Decompress(Environment.getExternalStorageDirectory() + "/QDMSWiki/" + zipFileName, Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
                //decompress.execute();
                AppConfig.getDwnldcmplted().postValue("completed");
                Intent insertServiceIntent = new Intent(DownloadService.this, InsertionService.class);
                insertServiceIntent.putExtra("destDirectory",Environment.getExternalStorageDirectory() + "/QDMSWiki/ExtractedFiles");
                insertServiceIntent.putExtra("zipFilePath", filename);
                insertServiceIntent.putParcelableArrayListExtra("downloadEntityLists",downloadEntityLists);
                insertServiceIntent.putExtra("urlNum",urlNum);
                if (isMyServiceRunning(InsertionService.class)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(insertServiceIntent);
                    } else {
                        startService(insertServiceIntent);
                    }
                    stopSelf();
                }

            }

            @Override
            public void onPaused(@NotNull Download download) {
                fetch.pause(download.getId());
            }

            @Override
            public void onResumed(@NotNull Download download) {
                fetch.resumeAll();
            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };
        fetch.addListener(fetchListener);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initChannels(Context context, String msg, String title) {
       notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        notificationChannel.setShowBadge(true);
        notificationChannel.setName("QDMS");
        notificationChannel.setDescription(msg);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.app_icon)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(1, notification);
        startForeground(1,notification);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("onTaskRemoved","onTaskRemoved");
    }
}
