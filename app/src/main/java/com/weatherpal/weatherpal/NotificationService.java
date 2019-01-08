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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;


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

    public void updateAllNotifications() {
        //send GET request for weather forecast

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //TODO add your personal key
        String url ="https://api.darksky.net/forecast/key/56.946285,24.105078";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        showNotifications(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(NotificationService.this, "Mēģini vēlreiz! Kaut kas nogāja greizi..", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        return;
    }

    public void showNotifications(JSONObject data) {

        //logging out the result of GET request for weather forecast in Riga
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Data - Weather forecast: "+ data);

        //TODO check if json data are valid json object

        //TODO add logic which notifications to show
        createNotification(0, "Brīdinājums", "Laiks ārā solās būt īpaši ledaini auksts - saģērbies kārtīgi! Un neaizmirsti cimdus un cepuri!", R.drawable.picture );
        createNotification(1, "Novēlējums", "Lai tev forša diena!", R.drawable.picture );
        Toast.makeText(this, "Notifications Created", Toast.LENGTH_SHORT).show();
    }


    public void createNotification(Integer id, String title, String text, int picture ) {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), picture);

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
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        // mId allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());

        return;
    }

}
