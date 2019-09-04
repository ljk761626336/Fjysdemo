package titan.com.test.track;


import android.support.v4.app.FragmentManager;

import com.esri.arcgisruntime.geometry.Point;
import com.titan.baselibrary.util.MobileInfoUtil;

import titan.com.test.map.IMap;
import titan.com.test.util.DbHelper;
import titan.com.test.util.TimeUtil;

public class TrackPresenter {

    private IMap _iMap ;

    public TrackPresenter(IMap iMap){
        this._iMap = iMap;
    }

    public void initTrackDialog(){
        TrackDialogFragment dialogFragment = new TrackDialogFragment();
        dialogFragment.set_iMap(_iMap);
        FragmentManager fragmentManager = _iMap.getActivity().getSupportFragmentManager();
        dialogFragment.show(fragmentManager,"轨迹弹出");


    }

    /**添加轨迹点*/
    public void addTravelPoint(Point point){
        String sbh = MobileInfoUtil.getMAC(_iMap.getActivity());
        String time = TimeUtil.curTime();
        String state = "0";
        DbHelper.getInstance().addGuijiData(_iMap.getActivity(),sbh,point,time,state,"guiji.sqlite");

    }

}
