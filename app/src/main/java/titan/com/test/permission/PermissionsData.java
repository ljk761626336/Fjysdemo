package titan.com.test.permission;

import android.Manifest;

public class PermissionsData {

    public static final int REQUEST_CODE = 10086;

    public static final int REQUEST_STORAGE = 10081;

    // 所需的定位权限
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    public static final String[] STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    // 所需的拍照权限
    public static final String[] CAMERA = new String[]{
            Manifest.permission.CAMERA,
    };

    /**
     * 录视频和选择图片
     */
    public static final String[] AUDIO = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

}
