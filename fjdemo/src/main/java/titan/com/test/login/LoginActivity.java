package titan.com.test.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.titan.baselibrary.util.ToastUtil;

import titan.com.test.map.MainActivity;
import titan.com.test.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText loginName;
    EditText loginPsw;
    Button button;
    CheckedTextView checkboxText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        addEvent();
    }

    private void initView(){
        loginName = findViewById(R.id.login_name);
        loginPsw = findViewById(R.id.login_psw);
        button = findViewById(R.id.button);
        checkboxText = findViewById(R.id.checkboxText);
    }

    private void addEvent(){
        button.setOnClickListener(this);
        checkboxText.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                login();
                break;
            case R.id.checkboxText:
                remeberPassword();
                break;
        }
    }

    private void remeberPassword(){
        checkboxText.toggle();
        boolean flag = checkboxText.isChecked();
        if(flag){

        }
    }


    private void login(){
        String name = loginName.getText().toString();
        if(name.equals("")){
            ToastUtil.setToast(LoginActivity.this,"请输入手机号");
        }

        String pasw = loginPsw.getText().toString();
        if(pasw.equals("")){
            ToastUtil.setToast(LoginActivity.this,"密码不能为空");
        }

        toMainActivity();

        /*Injection.dataRepository(LoginActivity.this).checkLogin(name, pasw,"", new RemotDataSource.getCallback() {
            @Override
            public void onFailure(String info) {

            }

            @Override
            public void onSuccess(Object data) {
                Log.e("=====",data.toString());
                Gson gson = new Gson();
                ResultModel resultModel = gson.fromJson(data.toString(),ResultModel.class);
                if(resultModel.getResponseResult().equals("true")){
                    toMainActivity();
                }else{
                    ToastUtil.setToast(LoginActivity.this,"登录失败");
                }
            }
        });*/
    }


    private void toMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

}
