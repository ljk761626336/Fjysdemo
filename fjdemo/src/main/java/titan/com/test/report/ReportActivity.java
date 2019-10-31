package titan.com.test.report;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Point;
import com.google.gson.Gson;
import com.titan.baselibrary.util.ConverterUtils;
import com.titan.baselibrary.util.MobileInfoUtil;
import com.titan.baselibrary.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import titan.com.test.R;
import titan.com.test.bean.EventData;
import titan.com.test.bean.EventReport;
import titan.com.test.bean.UpFile;
import titan.com.test.data.Injection;
import titan.com.test.data.remote.RemotDataSource;
import titan.com.test.util.Constant;

public class ReportActivity extends AppCompatActivity {

    private ImageView closeView;
    private EditText xcqkText,remarkText,telView;
    private TextView addressView,lonView,latView;
    private TextView sureView;
    private Spinner spinnerType;

    private Point gpspoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initData();
        initView();
        initViewData();
        addEvent();

    }

    private void initData(){
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onDataEvent(Point point) {
        if (null != point) {
            //赋值
            this.gpspoint = point;
        }
    }

    private void initView(){
        closeView = findViewById(R.id.iv_close_report);
        xcqkText = findViewById(R.id.report_xcqk);
        spinnerType = findViewById(R.id.report_xcr);
        addressView = findViewById(R.id.report_address);
        lonView = findViewById(R.id.report_lon);
        latView = findViewById(R.id.report_lat);
        remarkText = findViewById(R.id.report_remark);

        telView = findViewById(R.id.report_tel);

        sureView = findViewById(R.id.report_sure);
    }

    private void initViewData(){
        if(gpspoint != null){
            lonView.setText(Constant.sixFormat.format(gpspoint.getX()));
            latView.setText(Constant.sixFormat.format(gpspoint.getY()));
        }
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

    private ArrayList<EventReport.XJLX> xjlxs = new ArrayList<>();
    private ArrayList<UpFile> audios = new ArrayList<>();
    private ArrayList<EventReport> reports = new ArrayList<>();
    private ArrayList<UpFile> images = new ArrayList<>();
    private ArrayList<UpFile> videos = new ArrayList<>();

    private void sureReport(){
        String xcqk = ConverterUtils.toString(xcqkText.getText().toString());
        int spid = spinnerType.getSelectedItemPosition();
        String[] array = this.getResources().getStringArray(R.array.event_type);
        String type = ConverterUtils.toString(array[spid]);
        String address = ConverterUtils.toString(addressView.getText().toString());
        String lon = ConverterUtils.toString(lonView.getText().toString());
        String lat = ConverterUtils.toString(latView.getText().toString());
        String remark = ConverterUtils.toString(remarkText.getText().toString());
        String tel = ConverterUtils.toString(telView.getText().toString());

        //网络连接后上报成功
        final EventReport report = new EventReport();
        report.setUSERID("");
        report.setXJ_SJMC(Constant.nameFormat.format(new Date()));
        report.setXJ_SJLX(type);
        String macAddress = MobileInfoUtil.getMAC(this);
        report.setXJ_SBBH(macAddress);
        report.setXJ_MSXX(xcqk);
        report.setXJ_JD(lon);
        report.setXJ_WD(lat);
        report.setREMARK("");
        report.setXJ_XXDZ(address);
        report.setXC_ID(UUID.randomUUID().toString());
        report.setXJ_YPDZ(audios);

        EventReport.XJLX xjlx = new EventReport.XJLX();
        xjlx.setSJ_DL("222");
        xjlx.setSJ_XL("333");

        xjlxs.add(xjlx);
        report.setXJ_LX(xjlxs);
        report.setXJ_ZPDZ(images);
        report.setXJ_SPDZ(videos);

        reports.add(report);
        String json = new Gson().toJson(reports);
        Injection.dataRepository(this).upEvent(json,new RemotDataSource.getCallback() {
            @Override
            public void onFailure(String info) {
                Log.e("",info);
            }

            @Override
            public void onSuccess(Object data) {
                Log.e("",data.toString());
                if(data.equals("true")){
                    ToastUtil.setToast(ReportActivity.this,"上传成功");
                }else{
                    ToastUtil.setToast(ReportActivity.this,"上传失败");
                }
            }
        });
    }

}
