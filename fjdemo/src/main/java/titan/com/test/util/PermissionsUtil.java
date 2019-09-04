package titan.com.test.util;

import android.Manifest;

public class PermissionsUtil {

    public static int STORAGE_REQUEST_CODE = 10081;// 请求码
    public static int LOCATION_REQUEST_CODE = 10081;// 请求码
    public static int CAMERA_REQUEST_CODE = 10082;// 请求码




    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    public static String[] STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static String[] LOCATION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public static String[] CAMERA = new String[]{
            Manifest.permission.CAMERA
    };

}
