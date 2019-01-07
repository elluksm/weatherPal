package com.weatherpal.weatherpal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class NotificationService extends Service {
    private final IBinder mBinder = new MyBinder();

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //add functions to be executed on Start

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    public class MyBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    //  createNotification(56, R.drawable.ic_launcher, "New Message",
//      "There is a new message from Bob!");

    public void createNotification() {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.picture);

        // Configure the channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("myChannelId", "My Channel", importance);
        channel.setDescription("Reminders");
        // Register the channel with the notifications manager
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder mBuilder =
                // Builder class for devices targeting API 26+ requires a channel ID
                new NotificationCompat.Builder(this, "myChannelId")
                        .setSmallIcon(R.drawable.menu_message)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Brīdinājums")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Laiks ārā solās būt īpaši ledaini auksts - saģērbies kārtīgi! Un neaizmirsti cimdus un cepuri!"));

        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

        Toast.makeText(this, "Notification Created", Toast.LENGTH_SHORT).show();

        return;
    }

}
