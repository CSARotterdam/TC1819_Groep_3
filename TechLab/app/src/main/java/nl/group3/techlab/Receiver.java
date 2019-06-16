package nl.group3.techlab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import nl.group3.techlab.R;

public class Receiver extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, Notification.class);
        long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(this, 01234, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logotechlab)
                .setContentTitle("Take Questionnaire")
                .setContentText("Take questionnaire for Duke Mood Study.")
                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01234, mBuilder.build());
    }
}