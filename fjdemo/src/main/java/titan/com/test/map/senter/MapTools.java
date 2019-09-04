package titan.com.test.map.senter;

import android.app.Activity;
import android.content.Intent;
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
import com.titan.baselibrary.util.ToastUtil;
import com.titan.drawtool.DrawTool;
import com.titan.drawtool.DrawType;
import com.titan.drawtool.GeometryCallback;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

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
        ImageView imageView = _iMap.getActivity().findViewById(R.id.navigation);
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
    }

    /*二维码扫描*/
    public void toCaptureActivity(){
        Intent intent = new Intent(_iMap.getContext(), CaptureActivity.class);
        ((Activity)_iMap.getContext()).startActivityForResult(intent, Constant.REQUEST_CODE);
    }


    private void showCallout(Feature queryFeature,Geometry point){

        ScrollView scrollView = new ScrollView(_iMap.getContext());

        TextView calloutContent = new TextView(_iMap.getContext());
        calloutContent.setTextColor(Color.BLACK);

        Map<String, Object> map = queryFeature.getAttributes();

        String value = "";
        for (String key : map.keySet()) {
            value = value + key + ":" + map.get(key) + "\r\n";
        }
        calloutContent.setText(value);
        scrollView.addView(calloutContent);

        //设置Callout样式
        Callout.Style style = new Callout.Style(_iMap.getContext());
        style.setMaxWidth(200); //设置最大宽度
        style.setMaxHeight(300);  //设置最大高度
        style.setMinWidth(200);  //设置最小宽度
        style.setMinHeight(100);  //设置最小高度
        style.setBorderWidth(2); //设置边框宽度
        style.setBorderColor(Color.BLUE); //设置边框颜色
        style.setBackgroundColor(Color.WHITE); //设置背景颜色
        style.setCornerRadius(8); //设置圆角半径
        //style.setLeaderLength(50); //设置指示性长度
        //style.setLeaderWidth(5); //设置指示性宽度
        style.setLeaderPosition(Callout.Style.LeaderPosition.LOWER_MIDDLE); //设置指示性位置

        _iMap.getMapview().getCallout().setStyle(style);
        _iMap.getMapview().getCallout().setLocation((Point) point);
        _iMap.getMapview().getCallout().setContent(scrollView);
        _iMap.getMapview().getCallout().show();
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
                        value = value + field.getAlias() + ":" + time + "\r\n";
                    }else{
                        value = value + field.getAlias() + ":" + map.get(key) + "\r\n";
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

}
