package titan.com.test.map.senter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.layers.SublayerList;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.raster.Raster;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.util.ListenableList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.titan.baselibrary.util.ConverterUtils;
import com.titan.baselibrary.util.ToastUtil;
import com.titan.drawtool.DrawTool;
import com.titan.drawtool.DrawType;
import com.titan.drawtool.GeometryCallback;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import titan.com.test.R;
import titan.com.test.map.IMap;
import titan.com.test.map.MainActivity;
import titan.com.test.navication.NavitcationActivity;
import titan.com.test.report.ReportActivity;
import titan.com.test.track.TrackPresenter;
import titan.com.test.util.ActionModel;
import titan.com.test.util.Constant;
import titan.com.test.util.ResourcesManager;
import titan.com.test.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapTools implements GeometryCallback {

    private IMap _iMap;

    public GraphicsOverlay getOverlay() {
        return overlay;
    }

    private GraphicsOverlay overlay = new GraphicsOverlay();

    private DrawTool drawTool;
    private ActionModel actionModel;

    public MapTools(IMap iMap){
        this._iMap = iMap;
        init();
    }

    private void init(){
        initData();
        trakDialog();
        measureArea();
        measureDistance();
        navigat();
        attributeQuery();
        clean();
        zoomToLocation();
        dataUp();
    }

    private void initData(){
        drawTool = new DrawTool(_iMap.getMapview(),_iMap.getDefalutSpatial());
        drawTool.setCallBack(this);
    }

    private void dataUp(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.data_up);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(_iMap.getGpspoint());
                Intent intent = new Intent(_iMap.getActivity(),ReportActivity.class);
                _iMap.getActivity().startActivity(intent);
            }
        });
    }

    private void trakDialog(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.track);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TrackPresenter(_iMap).initTrackDialog();
            }
        });
    }

    /**测量面积*/
    private void measureArea(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.area_cal);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionModel = ActionModel.AREA;
                drawTool.activate(DrawType.FREEHAND_POLYGON);
            }
        });
    }

    /**测量距离*/
    private void measureDistance(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.distance);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionModel = ActionModel.DISTANCE;
                drawTool.activate(DrawType.FREEHAND_POLYLINE);
            }
        });
    }

    private void navigat(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.navigation_test);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionModel = ActionModel.NAVIGATION;
                drawTool.activate(DrawType.POINT);
            }
        });
    }

    /**属性查询*/
    private void attributeQuery(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.iv_info);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionModel = ActionModel.ISEARCH;
                drawTool.activate(DrawType.POINT);
            }
        });
    }

    private void clean(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.iv_trash);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionModel = ActionModel.EMPERTY;
                clearAllGraphic();
            }
        });
    }

    /*定位到当前位置*/
    public void zoomToLocation(){
        ImageView imageView = _iMap.getActivity().findViewById(R.id.iv_location);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _iMap.getMapview().setViewpointGeometryAsync(_iMap.getMappoint());
            }
        });
    }

    /*清除所有标会*/
    public void clearAllGraphic(){

        drawTool.deactivate();
        _iMap.getMapview().getCallout().dismiss();
        _iMap.getMapview().getGraphicsOverlays().clear();
        if(_iMap.getMapview().getSketchEditor() != null){
            _iMap.getMapview().getSketchEditor().stop();
            _iMap.getMapview().getSketchEditor().clearGeometry();
        }
        _iMap.getMapview().invalidate();

        ListenableList<GraphicsOverlay> overlays = _iMap.getMapview().getGraphicsOverlays();
        for(GraphicsOverlay overlay : overlays){
            overlay.getGraphics().clear();
        }

        LayerList layers =  _iMap.getMapview().getMap().getOperationalLayers();
        for(Layer layer : layers){
            if(layer instanceof FeatureLayer){
                ((FeatureLayer) layer).clearSelection();
            }
        }
    }

    /*二维码扫描*/
    public void toCaptureActivity(){
        Intent intent = new Intent(_iMap.getContext(), CaptureActivity.class);
        ((Activity)_iMap.getContext()).startActivityForResult(intent, Constant.REQUEST_CODE);
    }


    public void initGisLocation() {

        final LocationDisplay locationDisplay = _iMap.getMapview().getLocationDisplay();
        //设置定位模式
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        locationDisplay.startAsync();
        //监听位置的变化
        locationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent event) {
                Point mappoint = locationDisplay.getMapLocation();
                Point gpspoint = locationDisplay.getLocation().getPosition();
            }
        });

    }

    @Override
    public void onGeometry(Geometry geometry) {
        if (geometry == null) {
            return;
        }

        switch (actionModel){
            case NAVIGATION:
                toNaviActivity(geometry);
                break;
            case ISEARCH:
                queryFeature(geometry);
                break;
            case AREA:
                Point center = geometry.getExtent().getCenter();
                double area = Math.abs(GeometryEngine.area((Polygon) geometry));
                showValueOnmap(center,area,"平方米");
                break;
            case DISTANCE:
                Point point = geometry.getExtent().getCenter();
                double length = Math.abs(GeometryEngine.length((Polyline) geometry));
                showValueOnmap(point,length,"米");
                break;
        }
    }

    private void queryFeature(Geometry geometry){
        ArrayList<Feature> features = new ArrayList<>();
        LayerList layerList = _iMap.getMapview().getMap().getOperationalLayers();
        for(Layer layer :layerList){
            if(layer instanceof FeatureLayer){
                FeatureLayer featureLayer = (FeatureLayer) layer;
                featureLayer.clearSelection();
                query(featureLayer,geometry,features);
            }
        }
    }

    private void query(FeatureLayer featureLayer,Geometry geometry,ArrayList<Feature> features){
        QueryParameters parameters = new QueryParameters();
        parameters.setGeometry(geometry);
        parameters.setReturnGeometry(true);
        parameters.setSpatialRelationship(QueryParameters.SpatialRelationship.INTERSECTS);
        ListenableFuture<FeatureQueryResult> featureQueryResult = featureLayer.selectFeaturesAsync(parameters,FeatureLayer.SelectionMode.ADD);
        featureQueryResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = featureQueryResult.get();
                    Iterator<Feature> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        Feature feature = iterator.next();
                        featureLayer.selectFeature(feature);
                        features.add(feature);
                    }
                    if(features.size() > 0){
                        showFeatureAttribute(features,(Point) geometry);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showFeatureAttribute(ArrayList<Feature> features,Point point){

        Callout callout = _iMap.getMapview().getCallout();
        ScrollView scrollView = new ScrollView(_iMap.getActivity());

        TextView calloutContent = new TextView(_iMap.getActivity());
        calloutContent.setTextColor(Color.BLACK);
        //设置Callout样式
        Callout.Style style = new Callout.Style(_iMap.getActivity());
        style.setMaxWidth(200); //设置最大宽度
        style.setMaxHeight(300);  //设置最大高度
        style.setMinWidth(200);  //设置最小宽度
        //style.setMinHeight(100);  //设置最小高度
        style.setBorderWidth(2); //设置边框宽度
        style.setBorderColor(Color.BLUE); //设置边框颜色
        style.setBackgroundColor(Color.WHITE); //设置背景颜色
        style.setCornerRadius(8); //设置圆角半径
        //style.setLeaderLength(50); //设置指示性长度
        //style.setLeaderWidth(5); //设置指示性宽度
        style.setLeaderPosition(Callout.Style.LeaderPosition.LOWER_MIDDLE); //设置指示性位置

        callout.setStyle(style);
        callout.setLocation(point);
        for(Feature feature : features){

            Map<String, Object> map = feature.getAttributes();
            String value = "";
            for (String key : map.keySet()) {
                Field field = feature.getFeatureTable().getField(key);
                Field.Type type = field.getFieldType();
                CodedValueDomain domain = (CodedValueDomain) field.getDomain();
                if(domain != null){
                    List<CodedValue> values = domain.getCodedValues();
                    Object obj = map.get(key);
                    for(CodedValue codedValue : values){
                        if(codedValue.getCode().equals(obj)){
                            value = value + field.getAlias() + ":" + codedValue.getName() + "\r\n";
                        }
                    }
                }else{
                    if(type == Field.Type.DATE){
                        Object obj = map.get(key);
                        String time = TimeUtil.ObjToString(obj);
                        value = value + key + ":" + time + "\r\n";
                    }else{
                        value = value + key + ":" + map.get(key) + "\r\n";
                    }
                }
            }
            calloutContent.setText(value);
        }

        callout.setContent(scrollView);
        scrollView.addView(calloutContent);
        callout.show();

    }


    private void showValueOnmap(Point point, double value, String unit){

        TextView calloutContent = new TextView(_iMap.getActivity());
        calloutContent.setTextColor(Color.RED);
        calloutContent.setSingleLine();

        // format coordinates to 4 decimal places
        String area = Constant.disFormat.format(value)+unit;
        calloutContent.setText(area);

        Callout callout = _iMap.getMapview().getCallout();
        callout.setContent(calloutContent);
        callout.setLocation(point);
        callout.show();

    }

    private void toNaviActivity(Geometry geometry){
        Point point = (Point) GeometryEngine.project(geometry,SpatialReferences.getWgs84());

        if(point == null){
            ToastUtil.setToast(_iMap.getActivity(),"请先选定小班");
            return;
        }

        Intent intent = new Intent(_iMap.getActivity(),NavitcationActivity.class);
        intent.putExtra("s_lon",_iMap.getGpspoint().getX()+"");
        intent.putExtra("s_lat",_iMap.getGpspoint().getY()+"");
        intent.putExtra("e_lon",point.getX()+"");
        intent.putExtra("e_lat",point.getY()+"");
        _iMap.getActivity().startActivity(intent);
    }

    /**
     * 打开指定包名的app,这里的包名指的是app的主包名
     *
     * @param packageName 包名
     */
    public void openApp(String packageName) {
        PackageInfo pi = null;
        try {
            pi = _iMap.getActivity().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        PackageManager pm = _iMap.getActivity().getPackageManager();
        List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String getPackageName = ri.activityInfo.packageName;
            String getClassName = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(getPackageName, getClassName);

            intent.setComponent(cn);
            _iMap.getActivity().startActivity(intent);
        }
    }

    public void showStatistDialog(){
        Dialog dialog = new Dialog(_iMap.getActivity(),R.style.Dialog);
        dialog.setContentView(R.layout.dialog_statist);

        BarChart barChart = dialog.findViewById(R.id.bc_ldzz);

        initChart(barChart,"");

        dialog.show();
    }


    /**
     * 柱形图
     */
    private String[] xData;
    private String[] yData;
    private double yMin = 0.0;
    private double yMax;

    private void initChart(BarChart barChart,String type) {
        String[] datas = _iMap.getActivity().getResources().getStringArray(R.array.bchart_data);
        yData = new String[datas.length];
        xData = new String[datas.length];
        for(int i=0;i<datas.length;i++){
            xData[i] = datas[i].trim().split(",")[0];
            yData[i] = datas[i].trim().split(",")[1];
        }

        barChart.setDrawBarShadow(false); // 柱底阴影
        barChart.setDrawValueAboveBar(true); // 柱顶数值位置
        barChart.setDrawGridBackground(false); // 图形底部阴影
        barChart.getDescription().setEnabled(false); // x轴描述信息
        barChart.setScaleXEnabled(true); // X轴缩放
        barChart.setScaleYEnabled(false); // Y轴缩放
        barChart.setDragEnabled(true); // 是否可以拖拽
        barChart.setPinchZoom(false); // 是否只能根据X,Y轴放大缩小
        //设置图例
        barChart.getLegend().setEnabled(true);//隐藏图例
        barChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);//设置图例的位置
        barChart.getLegend().setTextSize(10f);
        barChart.getLegend().setFormSize(10f); // set the size of the legend forms/shapes
        barChart.getLegend().setForm(Legend.LegendForm.SQUARE);//设置图例形状， SQUARE(方格) CIRCLE（圆形） LINE（线性）

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true); // X轴
        xAxis.setDrawGridLines(false); // 表格
        xAxis.setGranularity(1f); // 标签间隔 设置后可使x轴拖拽后不会出现重复标签
        xAxis.setLabelRotationAngle(0); // 标签文字偏转角度
        xAxis.setCenterAxisLabels(true);

        xAxis.setLabelCount(xData.length); // 标签数目
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xData[Math.abs((int) value % xData.length)];
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();

        getMostY(); // 设置Y轴最大值和最小值

        if (yMin < 0) {
            double min = Math.floor(yMin); // 向下取整
            min = -min + (-min / 10);
            double max = Math.ceil(yMax); // 向上取整
            max = max + (max / 10);
            leftAxis.setAxisMaximum(Float.parseFloat(max + ""));
            leftAxis.setAxisMinimum(Float.parseFloat(-min + ""));
        } else {
            double max = Math.ceil(yMax);
            max = max + (max / 10);
            leftAxis.setAxisMaximum(Float.parseFloat(max + ""));
            leftAxis.setAxisMinimum(0f);
        }

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        barChart.getAxisRight().setEnabled(false);
        barChart.animateXY(2500, 2500);

        // 设置数据
        setChartData(barChart,type);
        barChart.getBarData().setBarWidth(0.8f); // 柱宽，要放在setChartData后面
    }

    /**
     * 设置柱形图数据
     */
    private void setChartData(BarChart barChart,String type) {
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for (int i = 0; i < xData.length; i++) {
            float val = ConverterUtils.toFloat(yData[i]);
            yVals.add(new BarEntry(i, val));
        }

        BarDataSet set = new BarDataSet(yVals, type); // 图表底部文字说明

        int[] colors = {Color.parseColor("#f1c40f"), Color.parseColor("#e74c3c"), Color.parseColor("#2ecc71"), Color.parseColor("#757575")};

        set.setColors(colors);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueFormatter(new DefaultValueFormatter(2)); // bar上的数值样式（保留2位小数）
        barChart.setData(data);
    }

    /**
     * 获取数据最大值、最小值
     */
    private void getMostY() {
        yMax = ConverterUtils.toDouble(yData[0]);
        yMin = ConverterUtils.toDouble(yData[0]);
        for (String y : yData) {
            if (ConverterUtils.toDouble(y) > yMax) {
                yMax = ConverterUtils.toDouble(y);
            }
            if (ConverterUtils.toDouble(y) < yMin) {
                yMin = ConverterUtils.toDouble(y);
            }
        }

    }




}
