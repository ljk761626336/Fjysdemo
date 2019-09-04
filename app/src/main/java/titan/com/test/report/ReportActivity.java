package titan.com.test.report;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.data.Feature;
import com.titan.baselibrary.util.ConverterUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import titan.com.test.R;

public class ReportActivity extends AppCompatActivity {

    private ImageView closeView;
    private EditText xcqkText,xcrText,remarkText;
    private TextView addressView,lonView,latView;
    private TextView sureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initData();
        initView();
        addEvent();

    }

    private void initData(){
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onDataEvent(Object object) {
        if (null != object) {
            //赋值

        }
    }

    private void initView(){
        closeView = findViewById(R.id.iv_close_report);
        xcqkText = findViewById(R.id.report_xcqk);
        xcrText = findViewById(R.id.report_xcr);
        addressView = findViewById(R.id.report_address);
        lonView = findViewById(R.id.report_lon);
        latView = findViewById(R.id.report_lat);
        remarkText = findViewById(R.id.report_remark);

        sureView = findViewById(R.id.report_sure);
    }

    private void addEvent(){
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });

        sureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureReport();
            }
        });
    }

    private void sureReport(){
        String xcqk = ConverterUtils.toString(xcqkText.getText().toString());
        String xcr = ConverterUtils.toString(xcrText.getText().toString());
        String address = ConverterUtils.toString(addressView.getText().toString());
        String lon = ConverterUtils.toString(lonView.getText().toString());
        String lat = ConverterUtils.toString(latView.getText().toString());
        String remark = ConverterUtils.toString(remarkText.getText().toString());
    }

}
