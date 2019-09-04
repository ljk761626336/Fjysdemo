package titan.com.test;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import titan.com.test.util.ObjectBoxUtil;

public class MyApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);

        ObjectBoxUtil.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
