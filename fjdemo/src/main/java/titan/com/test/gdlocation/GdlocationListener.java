package titan.com.test.gdlocation;

import android.app.Notification;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

/**
 * 高德定位
 */
public class GdlocationListener implements AMapLocationListener {


    private AMapLocationClient gdClient;
    private IGdLocationListener listener;

    private static class Holder {
        private static GdlocationListener INSTANCE = new GdlocationListener();
    }

    public static GdlocationListener getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (listener == null) {
            return;
        }

        listener.onLocation(aMapLocation);
    }

    public void reStartClient(){
        if(gdClient != null && gdClient.isStarted()){
            gdClient.startLocation();
        }
    }

    public void init(Context mContext, IGdLocationListener listener,Notification notification) {
        this.listener = listener;
        LocationSource locationSource = new LocationSourceImpl();
        gdClient = locationSource.initGdlocation(mContext, gdClient, this, notification);
    }

    public void stop() {
        if (gdClient != null) {
            gdClient.stopLocation();
        }
    }

    public interface IGdLocationListener {
        void onLocation(AMapLocation location);
    }
}
