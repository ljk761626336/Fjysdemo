package titan.com.test.data.remote;

import android.content.Context;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by hanyw on 2017/11/15/015.
 * 下载文件
 */

public class DownLoadManager {
    private static final String PDF = "application/pdf";
    private static final String WORD = "application/msword";
    private static final String JPEG = "image/jpeg";

    private static String fileSuffix = "";

    public static String writeResponseBodyToDisk(Context context, ResponseBody body) {
        try {
            String type = body.contentType().toString();
            Log.e("tag", "type:" + type);
            switch (type) {
                case PDF:
                    fileSuffix = ".pdf";
                    break;
                case WORD:
                    fileSuffix = ".doc";
                    break;
                case JPEG:
                    fileSuffix = ".jpg";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.e("tag", "body error:" + e);
        }
        String folder = context.getExternalFilesDir(null) + File.separator + "files";
        FileUtil.makeRootDirectory(folder);
        String path = folder + File.separator + System.currentTimeMillis() + fileSuffix;
        Log.e("tag", "path:" + path);
        try {
            File file = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return path;
            } catch (Exception e) {
                Log.e("tag", "fileWriteError:" + e);
                return "";
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            Log.e("tag", "fileError:" + e);
            return "";
        }
    }
}
