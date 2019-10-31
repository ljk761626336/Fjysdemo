package titan.com.test.data;

import android.content.Context;


import java.io.File;
import java.util.Date;
import java.util.Map;

import okhttp3.RequestBody;
import titan.com.test.data.local.LocalDataSource;
import titan.com.test.data.remote.RemotDataSource;
import titan.com.test.data.remote.RemotDataSourceImpl;


/**
 * Created by whs on 2017/5/18
 */

public class DataRepository implements LocalDataSource, RemotDataSource {
    private Context mContext;

    private static DataRepository INSTANCE = null;


    private final RemotDataSourceImpl mRemoteDataSource;

    private final LocalDataSource mLocalDataSource;

    public static DataRepository getInstance(RemotDataSourceImpl remoteDataSource, LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    private DataRepository(RemotDataSourceImpl remoteDataSource, LocalDataSource localDataSource) {
        //this.mContext=context;
        this.mRemoteDataSource = remoteDataSource;
        this.mLocalDataSource = localDataSource;
    }


    @Override
    public void checkLogin(String username, String password, String appname, getCallback callback) {
        mRemoteDataSource.checkLogin(username,password,appname,callback);
    }

    @Override
    public void upEvent(String json, getCallback callback) {
        mRemoteDataSource.upEvent(json,callback);
    }

}
