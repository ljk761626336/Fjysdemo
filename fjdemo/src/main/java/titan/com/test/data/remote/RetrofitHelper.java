package titan.com.test.data.remote;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.titan.baselibrary.util.ToastUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import titan.com.test.R;

/**
 * Created by whs on 2017/2/17
 * Retrofit 初始化
 */


public class RetrofitHelper {
    private Context mContext;

    public NetworkMonitor networkMonitor;
    //OkHttpClient client = new OkHttpClient();
    //private static NetworkMonitor networkMonitor;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    private Retrofit xJRetrofit = null;
    private Retrofit fileRetrofit = null;
    private Retrofit mWeatherRetrofit=null;
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);

        }
        return instance;
    }
    private RetrofitHelper(Context mContext){
        this.mContext = mContext;
        networkMonitor = new LiveNetworkMonitor(mContext);
        init();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addNetworkInterceptor(new MyNetworkInterceptor());
        //5秒超时
        okHttpClientBuilder.connectTimeout(20, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.serverhost))
                .client(okHttpClientBuilder.build())
                //.addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        //文件上传
        fileRetrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.fileUrl))
                .client(okHttpClientBuilder.build())
                //.addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        //巡检
        xJRetrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.xhserverhost))
                .client(okHttpClientBuilder.build())
                //.addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        /*//获取气象数据
        mWeatherRetrofit=new Retrofit.Builder()
                .baseUrl("http://222.85.160.4/")
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();*/
    }

    private class MyNetworkInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            if (networkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                //throw new NoNetworkException();
                ToastUtil.setToast(mContext, "无网络连接，请检查网络");
            }
            return null;
        }
    }

    /**
     * 常规接口
     * @return
     */
    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }

    /**
     * 文件上传常规接口
     * @return
     */
    public RetrofitService getFileServer(){
        return mRetrofit.create(RetrofitService.class);
    }


    /**
     * 气象数据接口
     * @return
     */
    public MeteorologyService getWeatherServer(){
        return mWeatherRetrofit.create(MeteorologyService.class);
    }

    /**
     * 常规接口
     *
     * @return
     */
    public RetrofitService getXjServer() {
        return xJRetrofit.create(RetrofitService.class);
    }

   /* private class MyNetworkInterceptor implements Interceptor
    {
        @Override
        public Response intercept(Chain chain) throws IOException {
            boolean connected = networkMonitor.isConnected();
            if (networkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                //throw new NoNetworkException();
                ToastUtil.setToast((Activity) mCntext,"无网络连接，请检查网络");
            }
            return null;
        }
    }*/
}
