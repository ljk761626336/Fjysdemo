package titan.com.test.util;

import android.os.Environment;

import java.io.File;

public final class Config extends Config_dev {

    public static String APP_SPATIAL_DIR = "spatial";
    public static String APP_CACHE_DIR = "cache";
    public static String APP_CRASH_DIR= "crash";
    public static String APP_MAP_BASE= "otitan.map/base";
    public static String APP_MAP_IMG= "otitan.map/image";
    public static String APP_VECTOR = "vector";



    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String APP_PATH = ROOT_PATH.concat(File.separator).concat(APP_PATH_NAME);
    public static String APP_DB_PATH = APP_PATH.concat(File.separator).concat(APP_DB_NAME);
    public static String APP_BASE_PATH = APP_PATH.concat(File.separator).concat(APP_MAP_BASE);
    public static String APP_IMG_PATH = APP_PATH.concat(File.separator).concat(APP_MAP_IMG);
    public static String APP_PATH_VECTOR = APP_PATH.concat(File.separator).concat(APP_VECTOR);

    public static String APP_SDB_PATH = APP_PATH.concat(File.separator).concat(APP_SPATIAL_DIR);
    public static String APP_PATH_CRASH = APP_PATH.concat(File.separator).concat(APP_CRASH_DIR);
    public static String APP_MAP_CACHE = APP_PATH.concat(File.separator).concat(APP_CACHE_DIR);


    private Config() {
    }
}
