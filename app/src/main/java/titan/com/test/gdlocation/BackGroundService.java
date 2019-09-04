package titan.com.test.gdlocation;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;

import java.util.Timer;
import java.util.TimerTask;

import titan.com.test.R;
import titan.com.test.map.MainActivity;


//后台服务
public class BackGroundService extends Service implements GdlocationListener.IGdLocationListener,StrongService {

    private MediaPlayer bgmediaPlayer;
    private GdlocationListener gdlocationListener;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Callback callback;

    @Override
    public void onLocation(AMapLocation location) {
        if(location == null){
            return;
        }
        callback.onDataChange(location);

    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            //播放无声音乐
            if (bgmediaPlayer == null) {
                bgmediaPlayer = MediaPlayer.create(BackGroundService.this, R.raw.silent);

                if(!bgmediaPlayer.isPlaying()){
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
                    Log.e("===", "ServiceOne Run: "+System.currentTimeMillis());

                    String servicename = "com.amap.api.location.APSService";
                    boolean flag = ServiceUtils.isServiceWork(BackGroundService.this,servicename);
                    if(!flag){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //Intent intent = new Intent(getBaseContext(), APSService.class);
                            //getBaseContext().startService(intent);
                            if(gdlocationListener != null){
                                gdlocationListener.reStartClient();
                            }
                        }
                    }

                    String sname = "titan.com.test.gdlocation.GdLocationService";
                    boolean flag2 = ServiceUtils.isServiceWork(BackGroundService.this,sname);
                    if(!flag2){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Intent i = new Intent(getBaseContext(), GdLocationService.class);
                            getBaseContext().startForegroundService(i);
                        }
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });

    @Override
    public void startService() {
        Intent i = new Intent(getBaseContext(), GdLocationService.class);
        getBaseContext().stopService(i);
    }

    @Override
    public void stopService() {
        Intent i = new Intent(getBaseContext(), GdLocationService.class);
        getBaseContext().startService(i);
    }

    public class BackServiceBinder extends Binder {
        public BackGroundService getService() {
            return BackGroundService.this;
        }
    }

    private BackServiceBinder binder = new BackServiceBinder();

    @Override
    public void onCreate() {
        service();
        thread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //START_REDELIVER_INTENT
        //START_STICKY
        return START_REDELIVER_INTENT;
    }

    NotificationUtils mNotificationUtils = null;
    Notification notification = null;

    private void service() {

        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String ANDROID_CHANNEL_ID = "12334123";
            mNotificationUtils = new NotificationUtils(this,ANDROID_CHANNEL_ID);
            //点击通知 跳转到activity
            Intent notificationIntent = new Intent(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.setComponent(new ComponentName(this, MainActivity.class));
            //用ComponentName得到class对象
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 3,
                    notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("定位服务", "智慧林业",pendingIntent,ANDROID_CHANNEL_ID);
            notification = builder2.build();
            notification.flags=Notification.FLAG_AUTO_CANCEL;//设置自动取消
            //startForeground(1, notification);//id must not be 0,即禁止是0
        }
        if (gdlocationListener == null) {
            gdlocationListener = GdlocationListener.getInstance();
        }
        gdlocationListener.init(this, this,notification);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onTrimMemory(int level) {

        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        if(bgmediaPlayer != null){
            bgmediaPlayer.release();
        }
        stopSelf();
        if(gdlocationListener != null){
            gdlocationListener.stop();
        }
        if(thread != null){
            thread.interrupt();
        }
        super.onDestroy();
    }

    public static interface Callback {
        void onDataChange(AMapLocation data);
    }

}
