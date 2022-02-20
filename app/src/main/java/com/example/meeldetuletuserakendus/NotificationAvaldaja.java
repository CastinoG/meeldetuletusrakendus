package com.example.meeldetuletuserakendus;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationAvaldaja extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationAbistaja notificationAbistaja = new NotificationAbistaja(context);
        NotificationCompat.Builder nb = notificationAbistaja.getChannelNotification();
        notificationAbistaja.getManager().notify(4, nb.build());
        Notification notification = nb.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
    }
}
