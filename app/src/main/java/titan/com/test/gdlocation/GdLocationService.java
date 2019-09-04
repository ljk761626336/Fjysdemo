package titan.com.test.gdlocation;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.amap.api.location.APSService;

import java.util.Timer;
import java.util.TimerTask;

import titan.com.test.R;
import titan.com.test.map.MainActivity;

public class GdLocationService extends APSService implements StrongService {

    private MediaPlayer bgmediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int i, int i1) {
        //return super.onStartCommand(intent, START_STICKY, i1);
        //START_REDELIVER_INTENT
        //START_STICKY
        return START_REDELIVER_INTENT;
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            //播放无声音乐
            if (bgmediaPlayer == null) {
                bgmediaPlayer = MediaPlayer.create(GdLocationService.this, R.raw.silent);
                if (!bgmediaPlayer.isPlaying()) {
                    // 允许循环播放
                    bgmediaPlayer.setLooping(true);
                    // 开始播放
                    bgmediaPlayer.start();
                }
            }


            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    String servicename = "titan.com.test.gdlocation.BackGroundService";
                    boolean b = ServiceUtils.isServiceWork(GdLocationService.this, servicename);
                    if (!b) {
                        Intent service = new Intent(GdLocationService.this, BackGroundService.class);
                        startService(service);
                        Log.e("====", "Start ServiceTwo");
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });

    @Override
    public void onCreate() {
        thread.start();
        service();
        super.onCreate();
    }

    NotificationUtils mNotificationUtils = null;
    Notification notification = null;

    private void service() {

        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String ANDROID_CHANNEL_ID = "12334124";
            mNotificationUtils = new NotificationUtils(this, ANDROID_CHANNEL_ID);
            //点击通知 跳转到activity
            Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.setClass(this, MainActivity.class);
            notificationIntent.setAction(Intent.ACTION_MAIN);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 4,
                    notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("后台定位服务", "后台服务", pendingIntent, ANDROID_CHANNEL_ID);
            notification = builder2.build();
            startForeground(1112, notification);//id must not be 0,即禁止是0
        }

    }

    @Override
    public void startService() {
        Intent i = new Intent(getBaseContext(), GdLocationService.class);
        getBaseContext().startService(i);
    }

    @Override
    public void stopService() {
        Intent i = new Intent(getBaseContext(), GdLocationService.class);
        getBaseContext().stopService(i);
    }

    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service1
     */
    @Override
    public void onTrimMemory(int level) {

        String sname = "com.otitan.gdlocation.BackGroundService";
        boolean flag2 = ServiceUtils.isServiceWork(this, sname);
        if (!flag2) {
            Intent i = new Intent(getBaseContext(), BackGroundService.class);
            getBaseContext().startService(i);
        }
    }

    @Override
    public void onDestroy() {

        stopForeground(true);
        if (bgmediaPlayer != null) {
            bgmediaPlayer.release();
        }
        stopSelf();

        if(thread != null){
            thread.interrupt();
        }

        super.onDestroy();
    }
}
