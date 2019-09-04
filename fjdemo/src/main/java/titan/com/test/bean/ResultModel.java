package titan.com.test.bean;

import java.util.List;

public class ResultModel {

    private String ResponseResult;
    private String ResponseMsg;
    private String ResponseCode;
    private Data data;

    public String getResponseResult() {
        return ResponseResult;
    }

    public void setResponseResult(String responseResult) {
        ResponseResult = responseResult;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        ResponseMsg = responseMsg;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    class Data{
        private Token token;
        private List<String> menu;
        private String code;

        public Token getToken() {
            return token;
        }

        public void setToken(Token token) {
            this.token = token;
        }

        public List<String> getMenu() {
            return menu;
        }

        public void setMenu(List<String> menu) {
            this.menu = menu;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    class Token{
        private String access_token;
        private String token_type;
        private String expires_in;
        private String refresh_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
    }
}
