package titan.com.test.data.remote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

public class FileUtil {

    /**
     * 打开pdf文件
     *
     * @param Path 地址
     * @return intent
     */
    public static Intent getPdfFileIntent(String Path, Context context) {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 打开图片
     *
     * @param Path 地址
     * @return intent
     */
    public static Intent getImageFileIntent(String Path, Context context) {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 打开word文件
     *
     * @param Path 地址
     * @return intent
     */
    public static Intent getWordFileIntent(String Path, Context context) {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 新建文件夹
     *
     * @param filePath 地址
     */
    public static void makeRootDirectory(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}
