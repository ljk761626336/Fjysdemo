package titan.com.test.gdlocation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "12334121";
    public static final String ANDROID_CHANNEL_NAME = "ChanneName";
    private Context context;

    public NotificationUtils(Context base,String CHANNEL_ID) {
        super(base);
        this.context = base;
        createChannels(CHANNEL_ID);
    }

    public void createChannels(String CHANNEL_ID) {

        // create android channel
        NotificationChannel androidChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(CHANNEL_ID,
                    context.getPackageName(), NotificationManager.IMPORTANCE_HIGH);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(false);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(false);
            androidChannel.setVibrationPattern(new long[]{0});
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(androidChannel);
        }

    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification.Builder getAndroidChannelNotification(String title, String body, PendingIntent pendingIntent,String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(true);

        }else{
            return new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        }
    }


}