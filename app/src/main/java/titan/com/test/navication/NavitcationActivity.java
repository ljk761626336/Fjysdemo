package titan.com.test.navication;

import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.titan.baselibrary.util.ConverterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import titan.com.test.R;

public class NavitcationActivity extends NavBaseActivity {

    protected AMapNaviView mAMapNaviView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navitcation);
        ButterKnife.bind(this);

        mAMapNaviView = findViewById(R.id.navi_view);

        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        String s_lon = getIntent().getStringExtra("s_lon");
        String s_lat = getIntent().getStringExtra("s_lat");
        String e_lon = getIntent().getStringExtra("e_lon");
        String e_lat = getIntent().getStringExtra("e_lat");

        mStartLatlng.setLongitude(ConverterUtils.toDouble(s_lon));
        mStartLatlng.setLatitude(ConverterUtils.toDouble(s_lat));

        mEndLatlng.setLongitude(ConverterUtils.toDouble(e_lon));
        mEndLatlng.setLatitude(ConverterUtils.toDouble(e_lat));

        sList.add(mStartLatlng);
        eList.add(mEndLatlng);

        boolean isUseInnerVoice = getIntent().getBooleanExtra("useInnerVoice", false);

        if (isUseInnerVoice) {
            /**
             * 设置使用内部语音播报，
             * 使用内部语音播报，用户注册的AMapNaviListener中的onGetNavigationText 方法将不再回调
             */
            mAMapNavi.setUseInnerVoice(isUseInnerVoice);
        }
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
    }
}
