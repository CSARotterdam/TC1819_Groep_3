package nl.group3.techlab;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID="ChannelID";
    public static final String channelName = "Techlab";
    private NotificationManager Manager;
    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel = new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getMAnager().createNotificationChannel(channel);
    }
    public NotificationManager getMAnager(){
        if (Manager == null){
            Manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return Manager;
    }
    public NotificationCompat.Builder getCahnnelNotification(String title, String description ){

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.logotechlab);
    }
}
