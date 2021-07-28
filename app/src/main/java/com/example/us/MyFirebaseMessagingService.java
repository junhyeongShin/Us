package com.example.us;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.jetbrains.annotations.NotNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService : ";

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        System.out.println(TAG+" onNewToken : "+s);
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {

        if(remoteMessage.getNotification() != null){

            System.out.println(TAG+" FCM 알림 메시지 : "+remoteMessage.getNotification().getBody());
            String message_body = remoteMessage.getNotification().getBody();
            String message_title = remoteMessage.getNotification().getTitle();

            if(message_body.equals("message_test")){
                return;
            }

            Intent intent = new Intent(this, Activity_board_view.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            String channel_id = "channel_id";

            NotificationCompat.Builder notification_builder =
                    new NotificationCompat.Builder(this,channel_id)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(message_title)
                    .setContentText(message_body)
                    .setAutoCancel(true)
                    .setSound(sound_uri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
               String channel_name = "channel_name";
               NotificationChannel channel = new NotificationChannel(channel_id,channel_name,NotificationManager.IMPORTANCE_HIGH);
               notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0,notification_builder.build());


        }
    }
}
