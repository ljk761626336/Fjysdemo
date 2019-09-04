package titan.com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    HashMap<String,String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        initView();
    }

    private void initView(){
        TextView xian = findViewById(R.id.xian);
        xian.setText(hashMap.get("xian"));

        TextView xiang = findViewById(R.id.xiang);
        xiang.setText(hashMap.get("xiang"));

        TextView cun = findViewById(R.id.cun);
        cun.setText(hashMap.get("cun"));

        TextView yzhz = findViewById(R.id.yzhz);
        yzhz.setText(hashMap.get("yzhz"));

        TextView tel = findViewById(R.id.tel);
        tel.setText(hashMap.get("tel"));

        TextView scode = findViewById(R.id.scode);
        scode.setText(hashMap.get("scode"));

        TextView area = findViewById(R.id.area);
        area.setText(hashMap.get("area"));

        TextView type = findViewById(R.id.type);
        type.setText(hashMap.get("type"));

    }
}
