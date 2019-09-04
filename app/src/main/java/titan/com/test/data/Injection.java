package titan.com.test.data;

import android.content.Context;

import titan.com.test.data.local.LocalDataSourceImpl;
import titan.com.test.data.remote.RemotDataSourceImpl;


public class Injection {

    public static DataRepository dataRepository(Context context){
        return DataRepository.getInstance(RemotDataSourceImpl.getInstance(context), LocalDataSourceImpl.instance());
    }
}
