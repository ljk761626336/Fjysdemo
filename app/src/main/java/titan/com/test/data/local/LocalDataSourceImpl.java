package titan.com.test.data.local;



public class LocalDataSourceImpl implements LocalDataSource {



    private static class Laydz {
        private static LocalDataSourceImpl instance = new LocalDataSourceImpl();
    }

    public static LocalDataSourceImpl instance() {
        return Laydz.instance;
    }


}
