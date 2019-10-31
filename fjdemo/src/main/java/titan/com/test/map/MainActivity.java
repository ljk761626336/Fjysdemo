package titan.com.test.map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.baselibrary.permission.PermissionsActivity;
import com.titan.baselibrary.permission.PermissionsChecker;
import com.titan.baselibrary.util.Gps;
import com.titan.baselibrary.util.PositionUtil;
import com.titan.baselibrary.util.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import titan.com.test.R;
import titan.com.test.TestActivity;
import titan.com.test.gdlocation.BackGroundService;
import titan.com.test.gdlocation.GdLocationService;
import titan.com.test.gdlocation.GdlocationListener;
import titan.com.test.map.senter.MapControl;
import titan.com.test.map.senter.MapPermission;
import titan.com.test.map.senter.MapTools;
import titan.com.test.track.TrackDialogFragment;
import titan.com.test.track.TrackPresenter;
import titan.com.test.util.Constant;
import titan.com.test.util.PermissionsUtil;
import titan.com.test.util.ReferenceUtil;

public class MainActivity extends BaseMapActivity implements IMap, GdlocationListener.IGdLocationListener,
        TrackDialogFragment.OnFragmentInteractionListener, View.OnClickListener {


    private int index = 0;

    private GdlocationListener gdlocationListener;

    private Point gpspoint, mappoint, lastpoint, selpoint;

    private GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    private MapControl mapControl;
    private MapPermission mapPermission;
    private MapTools mapTools;
    private TrackPresenter trackPresenter;

    private MapView mapView;
    private ImageView openView, closeView;
    private DrawerLayout drawerLayout;
    private ImageView vidioStart;
    private ImageView scanning,statistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initPresenter();
        initData();
        addEvent();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private void initView() {
        drawerLayout = (DrawerLayout) getView(R.id.drawerLayout);
        mapView = (MapView) getView(R.id.mapview);
        openView = (ImageView) getView(R.id.tv_layerw);
        closeView = (ImageView) getView(R.id.iv_close_layer);
        vidioStart = (ImageView) getView(R.id.vidio_start);

        scanning = (ImageView)getView(R.id.iv_scanning);
        statistView = (ImageView) getView(R.id.vidio_statistics);
    }

    private void initPresenter() {
        mapPermission = new MapPermission(this);
        mapControl = new MapControl(this);
        mapTools = new MapTools(this);

        trackPresenter = new TrackPresenter(this);
    }


    private void initData() {

    }

    private void addEvent() {
        openView.setOnClickListener(this);
        closeView.setOnClickListener(this);
        vidioStart.setOnClickListener(this);
        scanning.setOnClickListener(this);
        statistView.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkFinalLocation();
    }

    public void checkFinalLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean flag = new PermissionsChecker(this).lacksPermissions(PermissionsUtil.LOCATION);
            if (flag) {
                PermissionsActivity.startActivityForResult(this, PermissionsUtil.LOCATION_REQUEST_CODE, PermissionsUtil.LOCATION);
            } else {
                initGdlocation();
                mapTools.initGisLocation();
            }
        } else {
            initGdlocation();
            mapTools.initGisLocation();
        }
    }

    public void checkCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean f1 = new PermissionsChecker(this).lacksPermissions(PermissionsUtil.CAMERA);
            if (f1) {
                PermissionsActivity.startActivityForResult(this, PermissionsUtil.CAMERA_REQUEST_CODE, PermissionsUtil.CAMERA);
            } else {
                mapTools.toCaptureActivity();
            }
        } else {
            mapTools.toCaptureActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == PermissionsUtil.PERMISSIONS_GRANTED &&
                requestCode == PermissionsUtil.LOCATION_REQUEST_CODE) {
            initGdlocation();
            mapTools.initGisLocation();
        }

        if (resultCode == PermissionsUtil.PERMISSIONS_GRANTED &&
                requestCode == PermissionsUtil.STORAGE_REQUEST_CODE) {
            mapControl.init();
        }

        if (resultCode == PermissionsUtil.PERMISSIONS_GRANTED &&
                requestCode == PermissionsUtil.CAMERA_REQUEST_CODE) {
            mapTools.toCaptureActivity();
        }

        if (requestCode == Constant.REQUEST_CODE && null != data) {
            //处理扫描结果（在界面上显示）
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                return;
            }

            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, TestActivity.class);

                startActivity(intent);

            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public MapView getMapview() {
        return mapView;
    }

    @Override
    public MainActivity getActivity() {
        return MainActivity.this;
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    public Point getGpspoint() {
        if (gpspoint == null) {
            return new Point(0, 0);
        }
        return gpspoint;
    }

    @Override
    public Point getLastpoint() {
        if (lastpoint == null) {
            return new Point(0, 0);
        }
        return lastpoint;
    }

    @Override
    public Point getMappoint() {
        if (mappoint == null) {
            return new Point(0, 0);
        }
        return mappoint;
    }

    @Override
    public MapPermission getMapPermission() {
        return mapPermission;
    }

    @Override
    public SpatialReference getDefalutSpatial() {
        return ReferenceUtil.getDefalutReference();
    }

    @Override
    public void onLocation(AMapLocation location) {

        addLocationResult(location);
    }

    private BackGroundService service = null;
    private boolean isBind = false;
    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            isBind = true;
            BackGroundService.BackServiceBinder binder = (BackGroundService.BackServiceBinder) iBinder;
            service = binder.getService();
            service.setCallback(new BackGroundService.Callback() {
                @Override
                public void onDataChange(AMapLocation data) {
                    addLocationResult(data);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    /*初始化定位*/
    private void initGdlocation() {
        Intent gervice = new Intent(MainActivity.this, GdLocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent bservice = new Intent(MainActivity.this, BackGroundService.class);

            MainActivity.this.bindService(bservice, conn, Context.BIND_AUTO_CREATE);
            MainActivity.this.startForegroundService(gervice);
        } else {
            gdlocationListener = GdlocationListener.getInstance();
            gdlocationListener.init(MainActivity.this, this, null);

        }
    }


    /*定位成功后要做的操作*/
    private void addLocationResult(AMapLocation location) {

        Gps gps = PositionUtil.gcj02_To_Gps84(location.getLongitude(), location.getLatitude());
        gpspoint = new Point(gps.getWgLon(), gps.getWgLat(), SpatialReferences.getWgs84());
        mappoint = (Point) GeometryEngine.project(gpspoint, SpatialReferences.getWebMercator());

        trackPresenter.addTravelPoint(gpspoint);
        //ObjectBoxUtil.addGuipoint(this);
        lastpoint = getGpspoint();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scanning:
                checkCamera();
                break;
            case R.id.tv_layerw:
                drawerLayout.openDrawer(GravityCompat.END);
                mapControl.initVector();
                break;
            case R.id.iv_close_layer: // 关闭图层控制
                drawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.vidio_start:
                //ToastUtil.setToast(this,"功能待开发。。。");
                //startActivity(new Intent(MainActivity.this,SplashActivity.class));//SplashActivity

                //String pageName = "com.yuntongxun.eckuailiao";
                String pageName = "com.netease.nim.demo";
                mapTools.openApp(pageName);
                break;
            case R.id.vidio_statistics:
                mapTools.showStatistDialog();
                break;
        }
    }




}
