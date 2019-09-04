package titan.com.test.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import com.esri.arcgisruntime.symbology.Renderer;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.titan.baselibrary.util.ConverterUtils;


public class RenderUtil {

    public static Renderer getMarkRender(Context context){
        /*BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.shu);
        PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);*/

        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol();
        markerSymbol.setColor(Color.RED);
        markerSymbol.setSize(10);
        markerSymbol.setStyle(SimpleMarkerSymbol.Style.CIRCLE);
        return new SimpleRenderer(markerSymbol);
    }

    public static Renderer getLineRender(Context context, String path){
        SharedPreferences spf =  Constant.getSharedPreferences(context);
        String bjsVaule = spf.getString(path +"bjs","");
        String bjkdVaule = spf.getString(path +"bjkd","2");

        int bjs = ConverterUtils.toInt(bjsVaule);
        float bjkd = ConverterUtils.toFloat(bjkdVaule);

        if(bjsVaule.equals("")){
            bjs = Color.RED;
        }
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,bjs,bjkd);
        return new SimpleRenderer(lineSymbol);
    }

    public static Renderer getHisRender(Context context, String path){
        SharedPreferences spf =  Constant.getSharedPreferences(context);

        String bjsVaule = spf.getString(path +"bjs","");
        String tcsVaule = spf.getString(path +"tcs","");
        String bjkdVaule = spf.getString(path +"bjkd","3");

        int bjs = ConverterUtils.toInt(bjsVaule);
        int tcs = ConverterUtils.toInt(tcsVaule);
        float bjkd = ConverterUtils.toFloat(bjkdVaule);

        if(bjsVaule.equals("")){
            bjs = Color.RED;
        }

        if(tcsVaule.equals("")){
            tcs = Color.TRANSPARENT;
        }

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,bjs,bjkd);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,tcs,lineSymbol);
        return new SimpleRenderer(simpleFillSymbol);
    }

}
