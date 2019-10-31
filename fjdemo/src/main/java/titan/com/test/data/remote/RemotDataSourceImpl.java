package titan.com.test.data.remote;


import android.content.Context;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by whs on 2017/5/18
 */

public class RemotDataSourceImpl implements RemotDataSource {

    private static Context mContext;

    private static class Laydz {
        private static RemotDataSourceImpl instance = new RemotDataSourceImpl();
    }


    public static RemotDataSourceImpl getInstance(Context context) {
        mContext = context;
        return Laydz.instance;
    }


    @Override
    public void checkLogin(String username, String password,String appname, final getCallback callback) {
        Observable<ResponseBody> observable = RetrofitHelper.getInstance(mContext).getServer().checkLogin(username, password,appname);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() { // 完成请求后

            }

            @Override
            public void onError(Throwable e) { // 异常处理
                callback.onFailure(e.getMessage());
            }
            public void onNext(ResponseBody s) { // 请求成功
                try {
                    callback.onSuccess(s.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void upEvent(String json, final getCallback callback) {
        Observable<ResponseBody> observable = RetrofitHelper.getInstance(mContext).getXjServer().addEvent(json);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() { // 完成请求后

                    }

                    @Override
                    public void onError(Throwable e) { // 异常处理
                        callback.onFailure(e.getMessage());
                    }
                    public void onNext(ResponseBody s) { // 请求成功
                        try {
                            callback.onSuccess(s.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
