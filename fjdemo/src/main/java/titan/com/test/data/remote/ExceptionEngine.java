package titan.com.test.data.remote;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by whs on 2017/7/10
 */

class ExceptionEngine {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e){
        ApiException ex;
        if (e instanceof HttpException){//HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e,TitanError.HTTP_ERROR);
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                    ex.message = "服务请求超时，请稍后重试。";
                    break;
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    ex.message = "服务端异常，请与管理员联系";
                    break;
                default:
                    ex.message = "网络错误";  //均视为网络错误
                    break;
            }
            return ex;
        }else if(e instanceof SocketTimeoutException){
            ex = new ApiException(e, TitanError.NETWORD_ERROR);
            ex.message = "连接超时，请检查网络连接或稍后重试";  //均视为网络错误
            return ex;
        }else if (e instanceof ServerException){    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new ApiException(e, TitanError.PARSE_ERROR);
            ex.message = "解析错误";            //均视为解析错误
            return ex;
        }else if(e instanceof ConnectException){
            ex = new ApiException(e, TitanError.NETWORD_ERROR);
            ex.message = "连接失败";  //均视为网络错误
            return ex;
        }else {
            ex = new ApiException(e, TitanError.UNKNOWN);
            ex.message = "未知错误";   //未知错误
            return ex;
        }
    }


    public static class ApiException extends Exception {
        public int code;
        public String message;

        public ApiException(Throwable throwable, int code) {
            super(throwable);
            this.code = code;

        }


    }

    public static  class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}
