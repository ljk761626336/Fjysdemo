package titan.com.test.data.local;


public interface LocalDataSource {

    interface Callback {

        void onFailure(String info);

        void onSuccess(Object object);
    }



}
