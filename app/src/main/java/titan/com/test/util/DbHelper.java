package titan.com.test.util;

import android.content.Context;

import com.esri.arcgisruntime.geometry.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;

/**
 * 本地数据库操作帮助类
 */
public class DbHelper {
    public static DbHelper dbHelper;

    public static synchronized DbHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DbHelper();
        }
        return dbHelper;
    }

    /**
     * 添加字段到geodatabase数据库或者sqlite数据库
     * dbpath:数据库文件本地存放路径
     * tbname:要添加的数据库表名称
     */
    public static void addColumnToTab(Context context, String dbpath, String tbname, String column) {
        try {
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "ALTER TABLE " + tbname + " ADD " + column + " TEXT";
            db.exec(sql, null);
            db.close();
            addDataToTab(context, dbpath, tbname, column);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 添加字段的同时在表中加入这个子段的信息 GDB_ColumnRegistry
     */
    private static void addDataToTab(Context context, String dbpath, String tbname, String column) {
        try {
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "insert into GDB_ColumnRegistry values('" + tbname + "','" + column + "',5,50,null,null,4,null)";
            db.exec(sql, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 获取小地名类型
     */
    public static List<String> getXdmType(Context context) {
        final List<String> list = new ArrayList<>();
        try {
            String databaseName = ResourcesManager.getInstance(context).getDataBase("db.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
            String sql = "select distinct type from station";
            db.exec(sql, new Callback() {

                @Override
                public void types(String[] arg0) {
                }

                @Override
                public boolean newrow(String[] data) {// 3 5 6
                    list.add(data[0]);
                    return false;
                }

                @Override
                public void columns(String[] arg0) {

                }
            });
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 本地轨迹数据点添加
     */
    public static boolean addPointGuiji(Context context, String sbh, Point point, String time, String state) {
        boolean flag = false;
        try {
            String databaseName = ResourcesManager.getInstance(context).getDataBase("guiji.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "insert into point values(null," + point.getX() + "," + point.getY()
                    + ",'" + sbh + "','" + time + "'," + state
                    + ",geomfromtext('POINT(" + point.getX() + " " + point.getY() + ")',2343))";
            db.exec(sql, null);
            db.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 本地轨迹数据点添加
     */
    public boolean addGuijiData(Context context, String sbh, Point point, String time, String state, String dbpath) {
        boolean flag = false;
        try {
            String databaseName = ResourcesManager.getInstance(context).getDataBase(dbpath);
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "insert into point values(null," + point.getX() + "," + point.getY()
                    + ",'" + sbh + "','" + time + "'," + state
                    + ",st_geomfromtext('POINT(" + point.getX() + " " + point.getY() + ")',2343))";//st_geomfromtext geomfromtext
            db.exec(sql, null);
            db.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 轨迹点数据查询
     */
    public List<Map<String, Object>> selPointGuiji(Context context, String sbh, String startTime, String endTime) {
        final List<Map<String, Object>> list = new ArrayList<>();
        try {
            String databaseName = ResourcesManager.getInstance(context).getDataBase("guiji.sqlite");
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "SELECT * FROM point WHERE SBH ='" + sbh
                    + "' and time between datetime('" + startTime
                    + "') and datetime('" + endTime
                    + "') order by datetime(time) desc";
            db.exec(sql, new Callback() {

                @Override
                public void types(String[] arg0) {

                }

                @Override
                public boolean newrow(String[] data) {// 3 5 6

                    if (data[1] != null && data[2] != null && data[4] != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("lon", data[1]);
                        map.put("lat", data[2]);
                        map.put("time", data[4]);
                        list.add(map);
                    }
                    return false;
                }

                @Override
                public void columns(String[] arg0) {
                }
            });
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 添加小地名到本地数据库
     */
    public static void addPointToSearchData(Context context, String lon,
                                            String lat, String dbname, String name, String type) {
        try {
            String databaseName = ResourcesManager.getInstance(context).getDataBase(dbname);
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "insert into station values(null,null,'" + name
                    + "','" + lon + "','" + lat + "','" + type + "',null)";
            db.exec(sql, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户登录验证
     */
    static String loginResult = "";

    public static String checkLogin(Context context, String dbname, String name) {
        try {
            String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
            Class.forName("jsqlite.JDBCDriver").newInstance();
            final Database db = new Database();
            db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "select * from user where name = '" + name + "'";
            db.exec(sql, new Callback() {

                @Override
                public boolean newrow(String[] data) {
                    if (!(data.length > 0)) {
                        loginResult = "用户名不存在";
                    } else {
                        loginResult = data[0] + ":" + data[1];
                    }
                    return false;
                }

                @Override
                public void columns(String[] arg1) {

                }

                @Override
                public void types(String[] arg2) {

                }
            });
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loginResult;
    }


    /**
     * 添加登录用户到本地用户表
     */
    public static void addUserName(Context context, String dbname, String username, String psw) {
        try {
            String filename = ResourcesManager.getInstance(context).getDataBase(dbname);
            Class.forName("jsqlite.JDBCDriver").newInstance();
            Database db = new Database();
            db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
            String sql = "insert into user values(" + username + "," + psw
                    + ")";
            db.exec(sql, new Callback() {

                @Override
                public boolean newrow(String[] data) {
                    return false;
                }

                @Override
                public void columns(String[] arg1) {

                }

                @Override
                public void types(String[] arg2) {

                }
            });
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
