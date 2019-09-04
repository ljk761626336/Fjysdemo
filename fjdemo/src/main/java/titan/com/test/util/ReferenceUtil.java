package titan.com.test.util;

import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;

public class ReferenceUtil {

    public static SpatialReference getDefalutReference(){
        //2361  宜昌
        return SpatialReference.create(2343);
    }

    public static SpatialReference getWgs84(){
        return SpatialReferences.getWgs84();
    }

    public static SpatialReference getWebMercator(){
        return SpatialReferences.getWebMercator();
    }

}
