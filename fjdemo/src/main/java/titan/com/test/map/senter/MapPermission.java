package titan.com.test.map.senter;

import android.support.v4.app.ActivityCompat;

import com.titan.baselibrary.permission.PermissionsChecker;

import titan.com.test.map.IMap;
import titan.com.test.permission.PermissionsData;
import titan.com.test.util.Config;
import titan.com.test.util.FileUtil;

public class MapPermission {

    private IMap iMap;

    public MapPermission(IMap map){
        this.iMap = map;
        init();
    }

    private void init(){
        checkPermission();
    }

    public void checkPermission(){
        PermissionsChecker permissionsChecker = new PermissionsChecker(iMap.getActivity());
        // 缺少权限时, 进入权限配置页面
        if (permissionsChecker.lacksPermissions(PermissionsData.PERMISSIONS)) {
            ActivityCompat.requestPermissions(iMap.getActivity(), PermissionsData.PERMISSIONS, PermissionsData.REQUEST_CODE);
        }else{
            String path = Config.APP_DB_NAME;
            FileUtil.createAllFile(path);
        }
    }

    public boolean checkStorage(){
        PermissionsChecker permissionsChecker = new PermissionsChecker(iMap.getActivity());
        // 缺少权限时, 进入权限配置页面
        if (permissionsChecker.lacksPermissions(PermissionsData.STORAGE)) {
            ActivityCompat.requestPermissions(iMap.getActivity(), PermissionsData.STORAGE, PermissionsData.REQUEST_STORAGE);
        }else{
            return true;
        }
        return false;
    }


}
