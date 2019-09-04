package titan.com.test.util;

import android.content.Context;
import android.util.Log;

import com.esri.arcgisruntime.geometry.Point;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.query.Query;
import titan.com.test.track.Guijipoint;
import titan.com.test.map.IMap;
import titan.com.test.track.Guijipoint_;
import titan.com.test.track.MyObjectBox;

public class ObjectBoxUtil {

    private static BoxStore boxStore;

    public static void init(Context context){

        boxStore = MyObjectBox.builder().androidContext(context).build();

        new AndroidObjectBrowser(boxStore).start(context);

        DataManager.getInstance().init(context);//数据库统一操作管理类初始化
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }

    /*时间查询*/
    public static List<Guijipoint> queryPoint(String start, String end){
        try {
            Date sdate = Constant.dateFormat.parse(start);
            Date edate = Constant.dateFormat.parse(end);
            Query<Guijipoint> list = DataManager.getInstance().pointBox.query().between(Guijipoint_.time,sdate,edate).build();
            return list.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*添加本地估计点*/
    public static void addGuipoint(IMap iMap){
        //true 为相同点不保存
        if(checkPoint(iMap)){
            return;
        }
        Guijipoint guipoint = new Guijipoint();
        guipoint.setLon(iMap.getGpspoint().getX()+"");
        guipoint.setLat(iMap.getGpspoint().getY()+"");
        guipoint.setTime(new Date());

        long id = DataManager.getInstance().pointBox.put(guipoint);
        Log.e("",""+id);
    }

    public static boolean checkPoint(IMap iMap){
        Point gpspoint = iMap.getGpspoint();
        Point lastpoint = iMap.getLastpoint();
        if(gpspoint.getX() == lastpoint.getX() && gpspoint.getY() == lastpoint.getY()){
            return true;
        }
        return false;
    }

}
