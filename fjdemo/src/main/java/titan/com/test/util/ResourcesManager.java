package titan.com.test.util;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sp on 2018/10/21.
 * 获取设备内存地址
 */
public class ResourcesManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Context mContext;

    //private String ROOT_MAPS = "/zrbhd_dev";
    private String ROOT_MAPS = "/scyz_dev";
    private String BASE_MAP = "/otitan.map";
    private String BASE_TITLE = "/base";
    private String IMAGE = "/image";
    private String DXT = "/dxt";
    private String IMG = "/img";
    private String DB = "/sqlite";
    private String OTMS = "/vector";
    private String filePath = "文件可用地址";

    private static class LazyHolder {
        private static final ResourcesManager INSTANCE = new ResourcesManager();
    }

    public static ResourcesManager getInstance(Context context) {
        mContext = context;
        return LazyHolder.INSTANCE;
    }

    /**
     * 获取手机内部存储地址和外部SD卡存储地址
     */
    private String[] getStoragePath() {

        StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        String[] paths = null;
        try {
            //paths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class[0]).invoke(sm,new Object[]{});
            paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * 获取文件可用地址
     */
    private File getFilePath(String path) {
        String[] storagePath = getStoragePath();
        for (String storage : storagePath) {
            File file = new File(storage + ROOT_MAPS + path);
            if (file.exists() && file.isFile()) {
                return file;
            }
        }
        return null;
    }

    /**
     * 获取基础地图的本地路径
     */
    public File getTitlePath() {
        String name = BASE_MAP+BASE_TITLE+"/base.tpk";
        return getFilePath(name);
    }

    /**
     * 获取影像文件列表
     */
    public List<File> getImgTitlePath() {
        List<String> fileter = new ArrayList<>();
        fileter.add(".tpk");
        fileter.add(".tif");
        fileter.add(".img");
        return getPahts(BASE_MAP+IMAGE, fileter);
    }

    private List<File> getPahts(String path, List<String> list) {
        List<File> fileList = new ArrayList<>();
        String[] array = getStoragePath();
        for (String a : array) {
            File file = new File(a + ROOT_MAPS + path);
            if (!file.exists()) {
                continue;
            }
            for (File f : file.listFiles()) {
                if (!f.isFile()) {
                    continue;
                }

                for(String keywords : list){
                    if(f.getName().endsWith(keywords)){
                        fileList.add(f);
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * 获取otms文件夹下的文件夹
     */
    public List<File> getOtmsFolder() {
        String path = OTMS;
        File[] files = new File(getFolderPath(path)).listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<File> groups = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }
            groups.add(file);
        }
        return groups;
    }

    /**
     * 获取文件夹可用地址
     */
    private String getFolderPath(String path) {
        String dataPath = filePath;
        String[] storagePath = getStoragePath();
        for (String storage : storagePath) {
            File file = new File(storage + ROOT_MAPS + path);
            if (file.exists()) {
                dataPath = storage + ROOT_MAPS + path;
            } else {
                if (path.equals("")) {
                    file.mkdirs();
                }
            }
        }
        return dataPath;
    }

    /**
     * 获取otms中每个文件夹下的.otms或者.geodatabase数据
     */
    public List<Map<String, List<File>>> getChildData(List<File> groups) {
        List<Map<String, List<File>>> childs = new ArrayList<>();
        for (File file : groups) {
            /*String path = otms + "/" + file.getName();
            File[] files = new File(getFolderPath(path)).listFiles();
            Map<String, List<File>> map = new HashMap<>();
            map.put(file.getName(), getOtmsData(files));
            childs.add(map);*/

            File[] files = file.listFiles();
            Map<String, List<File>> map = new HashMap<>();
            map.put(file.getAbsolutePath(), getOtmsData(files));
            childs.add(map);
        }
        return childs;
    }

    private List<File> getOtmsData(File[] files) {
        List<File> list = new ArrayList<>();
        if (files != null || (files == null && files.length != 0)) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                if (file.getName().endsWith(".otms") || file.getName().endsWith(".shp") || file.getName().endsWith(".geodatabase")) {
                    list.add(file);
                }
            }
        }
        return list;
    }


    public String getDataBase(String dbname) {
        if (dbname.equals(""))
            return "";
        File db = getFilePath(DB+File.separator+dbname);
        assert db != null;
        if (db.exists()) {
            return db.toString();
        }
        try {
            throw new FileNotFoundException("文件不存在");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return db.toString();
    }

    public String getSqlitePath(String filePath) {
        String[] storagePath = getStoragePath();
        if (storagePath == null || storagePath.length > 0)
            return storagePath[0].concat(ROOT_MAPS+DB).concat(File.separator).concat(filePath);
        return null;
    }

}
