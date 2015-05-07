package com.example.raspisanie_proj;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.parovoz_small, "Спешите!!!", System.currentTimeMillis());
        Intent intentTL = new Intent(context, MainActivity.class);
        intent.getIntExtra("Edit", 100);
        notification.setLatestEventInfo(context, "Расписание", "паровоз через : " + intent.getIntExtra("Edit", 100) + " минут",
                PendingIntent.getActivity(context, 0, intentTL,
                        PendingIntent.FLAG_CANCEL_CURRENT)
        );
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notification);

    }

}
