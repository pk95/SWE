package com.example.tankverhalten.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.tankverhalten.R;

public class NotificationTUV extends Service {

     final int NOTIFICATION_ID = 1;
    public NotificationTUV() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void displayTUVNotification(String title, String text) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.garage_white)
                .setColor(getResources().getColor(R.color.FHGreen))
                .setVibrate(new long[]{0, 300, 200, 300})
                .setLights(Color.WHITE, 1000, 5000);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }
}