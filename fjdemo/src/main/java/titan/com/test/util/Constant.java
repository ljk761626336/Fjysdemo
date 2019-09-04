package titan.com.test.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Constant {

    public static final String TAG = "日志错误";

    public static final int SCALE = 2000;

    public static int REQUEST_CODE = 10086;//二维码扫描请求码

    public static final int PICK_CAMERA= 0x000007;
    public static final int PICK_PHOTO = 0x000003;
    public static final int PICK_AUDIO = 0x000004;
    public static final int PICK_VIDEO = 0x000005;
    public static final int PICK_PEOPLE = 0x000006;

    public static final String PREFS_NAME = "MYSP";

    public static DecimalFormat disFormat = new DecimalFormat("0.00");
    public static DecimalFormat sixFormat = new DecimalFormat("0.000000");

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat nameFormat = new SimpleDateFormat("yyyyMMddHHmmss");


    /*String 字符串保留两位小数*/
    public static String strFormat(String value){
        return Constant.disFormat.format(new BigDecimal(value));
    }

    public static RequestBody requestBody(Object obj) {
        String json = new Gson().toJson(obj);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    public static MultipartBody fileBody(File file) {
        //构建body
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        MultipartBody multipartbody = requestBody.build();
        return multipartbody;
    }


    public static SharedPreferences getSharedPre(Context context){
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static SharedPreferences getSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences("date", Context.MODE_PRIVATE);
        return preferences;
    }


}
