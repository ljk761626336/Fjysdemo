package titan.com.test.track;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.titan.baselibrary.util.ScreenTool;

import java.util.List;

import titan.com.test.R;
import titan.com.test.map.IMap;
import titan.com.test.util.Constant;
import titan.com.test.util.ObjectBoxUtil;
import titan.com.test.util.TimeUtil;


/**
 */
public class TrackDialogFragment extends DialogFragment implements OnDateSetListener,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //时间选择
    private TimePickerDialog mTimePickerDialog = null;
    //日期类型
    private int dateType = 1;

    private TextInputEditText startTxt,endTxt;
    private Button sure,cancle;

    public IMap get_iMap() {
        return _iMap;
    }

    public void set_iMap(IMap _iMap) {
        this._iMap = _iMap;
    }

    private IMap _iMap;
    public TrackDialogFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TrackDialogFragment newInstance(String param1, String param2) {
        TrackDialogFragment fragment = new TrackDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.My_DialogFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track_dialog, container, false);
        startTxt = view.findViewById(R.id.tet_starttime);
        startTxt.setText(TimeUtil.getSystemTime());
        startTxt.setOnClickListener(this);
        endTxt = view.findViewById(R.id.tet_endtime);
        endTxt.setText(TimeUtil.getSystemTime());
        endTxt.setOnClickListener(this);
        sure = view.findViewById(R.id.tv_confirm);
        sure.setOnClickListener(this);
        cancle = view.findViewById(R.id.tv_concel);
        cancle.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String timeValue = Constant.dateFormat.format(millseconds);
        if(dateType == 1) {
            startTxt.setText(timeValue);
        }else if(dateType == 2){
            endTxt.setText(timeValue);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tet_starttime:
                showDateSelect(1);
                break;
            case R.id.tet_endtime:
                showDateSelect(2);
                break;
            case R.id.tv_confirm:
                onConfirm();
                break;
            case R.id.tv_concel:
                onCancle();
                break;
        }
    }

    /**
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        ScreenTool.Screen screen = ScreenTool.getScreenPix(this.getContext());
        //params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = (int) (screen.getWidthPixels()*0.8);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }


    public void showDateSelect(int type){
        //设置日期类型
        dateType = type;
        if (mTimePickerDialog == null) {
            long fiveYears = 5L * 365 * 1000 * 60 * 60 * 24L;
            long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
            mTimePickerDialog =new TimePickerDialog.Builder()
                    .setCallBack(this)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("时间选择")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setThemeColor(getResources().getColor(R.color.colorPrimary))
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis() - fiveYears)
                    .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(getResources().getColor(R.color.colorAccent))
                    .setWheelItemTextSize(12)
                    .build();

        }
        mTimePickerDialog.show(getFragmentManager(), "timepicker");
    }

    /*确定 查询轨迹数据*/
    private void onConfirm(){
        String starttime = startTxt.getText().toString();
        String endtime = endTxt.getText().toString();
        List<Guijipoint> guijipoints = ObjectBoxUtil.queryPoint(starttime,endtime);
        for(Guijipoint guijipoint : guijipoints){

        }
    }

    private void onCancle(){
        this.dismiss();
    }
}
