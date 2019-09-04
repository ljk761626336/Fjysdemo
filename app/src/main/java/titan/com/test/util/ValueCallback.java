package titan.com.test.util;

import com.titan.drawtool.GeometryCallback;

public interface ValueCallback  extends GeometryCallback {

    void onSucess(Object object);

    void onFailed(String json);

}
