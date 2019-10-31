package titan.com.test.data.remote;


import okhttp3.RequestBody;
import retrofit2.http.Query;

/**
 * Created by whs on 2017/6/7
 */

public interface RemotDataSource {
    interface getCallback {

        void onFailure(String info);

        void onSuccess(Object data);
    }

    /**
     * 登录
     */
    void checkLogin(String username, String password,String appname, getCallback callback);

    /**
     *数据上报
     */
    void upEvent(String json, final getCallback callback);


}
