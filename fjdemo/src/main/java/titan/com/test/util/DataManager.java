package titan.com.test.util;

import android.content.Context;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import titan.com.test.track.Guijipoint;

/**
 * 数据库操作统一管理类
 */
public class DataManager {
    private static DataManager dataManager;

    public static synchronized DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public BoxStore boxStore;
    public Box<Guijipoint> pointBox;

    public void init(Context myApplication) {
        boxStore = ObjectBoxUtil.getBoxStore();
        initEntityBox();
    }

    private void initEntityBox() {
        //对应操作对应表的类
        pointBox = boxStore.boxFor(Guijipoint.class);
    }
}
