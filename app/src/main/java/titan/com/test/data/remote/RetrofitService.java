package titan.com.test.data.remote;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by whs on 2017/2/17
 * Retrofit 接口
 */

public interface RetrofitService {
    //登陆
    @GET("account/login")
    Observable<ResponseBody> checkLogin(@Query("username") String username, @Query("password") String password, @Query("applicationName") String appname);




}
