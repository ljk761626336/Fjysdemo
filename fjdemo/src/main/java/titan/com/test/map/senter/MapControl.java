package titan.com.test.map.senter;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahao.basetreeview.adapter.MultiLayoutTreeAdapter;
import com.ahao.basetreeview.model.TreeNode;
import com.ahao.basetreeview.util.TreeDataUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.esri.arcgisruntime.raster.Raster;
import com.esri.arcgisruntime.symbology.Renderer;
import com.titan.baselibrary.util.ToastUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import titan.com.test.R;
import titan.com.test.adapter.VectorTreeAdapter;
import titan.com.test.bean.LayerType;
import titan.com.test.bean.Vector;
import titan.com.test.map.IMap;
import titan.com.test.util.Config;
import titan.com.test.util.DividerGridItemDecoration;
import titan.com.test.util.FileUtil;
import titan.com.test.util.RenderUtil;
import titan.com.test.util.ResourcesManager;

public class MapControl {

    IMap iMap;
    ArrayList<Layer> layers = new ArrayList<>();

    public GraphicsOverlay overlay = new GraphicsOverlay();

    public GraphicsOverlay getOverlay() {
        return overlay;
    }

    public MapControl(IMap iMap){
        this.iMap = iMap;
        init();
    }

    public void init(){
        addOutlineBaseLayer();
        initBackground();
        //initLocation();
        initVector();
        creatFolder();

    }

    private void initBackground(){
        //去除licensed for developer use。。。。 水印
        //水印去除后无法编辑矢量数据
        //ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        //隐藏网格
        BackgroundGrid mainBackgroundGrid = new BackgroundGrid();
        mainBackgroundGrid.setColor(0xffffffff);
        mainBackgroundGrid.setGridLineColor(0xffffffff);
        mainBackgroundGrid.setGridLineWidth(0);
        iMap.getMapview().setBackgroundGrid(mainBackgroundGrid);
        iMap.getMapview().setAttributionTextVisible(false);
    }

    /**添加在线地图服务*/
    public void addOnlineBaseLayer(){
        Basemap basemap = Basemap.createImagery();
        ArcGISMap gisMap = new ArcGISMap(basemap);
        iMap.getMapview().setMap(gisMap);
    }

    /**添加离线地图*/
    public void addOutlineBaseLayer(){

        boolean flag = iMap.getMapPermission().checkStorage();
        if(flag){
            File title = ResourcesManager.getInstance(iMap.getActivity()).getTitlePath();
            if(title == null){
                addOnlineBaseLayer();
                return;
            }
            TileCache tileCache = new TileCache(title.getAbsolutePath());
            ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);
            Basemap basemap = new Basemap(tiledLayer);
            iMap.getMapview().setMap(new ArcGISMap(basemap));
            iMap.getMapview().getGraphicsOverlays().add(overlay);
        }
    }

    public List<TreeNode<Vector>> getDataToBind() {
        return dataToBind;
    }

    /**初始化矢量地图*/
    private List<TreeNode<Vector>> dataToBind = new ArrayList<>();
    public void initVector(){

        RecyclerView vectorRecyc = iMap.getActivity().findViewById(R.id.layer_recycview);
        vectorRecyc.setLayoutManager(new LinearLayoutManager(iMap.getActivity()));
        vectorRecyc.addItemDecoration(new DividerGridItemDecoration(iMap.getActivity()));
        if(dataToBind.size() == 0){
            dataToBind.addAll(TreeDataUtils.convertDataToTreeNode(addList(),1));
        }else{
            dataToBind.addAll(TreeDataUtils.convertDataToTreeNode(updataList(),1));
        }

        VectorTreeAdapter adapter = new VectorTreeAdapter(dataToBind);
        vectorRecyc.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter aadapter, View view, int position) {
                switch (view.getId()){
                    case R.id.leaf_zoomTent:
                        zoomToLayer(dataToBind.get(position).getData());
                        break;
                    case R.id.leaf_checkboxText:
                        CheckedTextView checkedTextView = (CheckedTextView) view;
                        checkedTextView.toggle();

                        Vector vector = dataToBind.get(position).getData();
                        vector.setVisible(checkedTextView.isChecked());
                        if(checkedTextView.isChecked()){
                            addLayer(vector);
                        }else{
                            removeLayer(vector);
                        }
                        break;
                }
            }
        });
        adapter.setOnTreeClickedListener(new MultiLayoutTreeAdapter.OnTreeClickedListener() {
            @Override
            public void onNodeClicked(View view, TreeNode node, int position) {
                Log.e("",position+"");
            }

            @Override
            public void onLeafClicked(View view, TreeNode node, int position) {
                Log.e("",position+"");
            }
        });

    }

    private List<Vector> updataList(){

        List<Vector>  vectors = new ArrayList<>();
        ResourcesManager resourcesManager = ResourcesManager.getInstance(iMap.getActivity());

        List<File> imglist = resourcesManager.getImgTitlePath();

        if(imglist.size() == 0){
            vectors.add(new Vector("影像地图",""+(vectors.size()+1),"","",LayerType.FOLDER));
        }else{
            vectors.add(new Vector("影像地图",""+(vectors.size()+1),"",imglist.get(0).getParent(),LayerType.FOLDER));
        }

        String imgPid = vectors.size()+"";
        for(File file : imglist){
            vectors.add(new Vector(file.getName().split("\\.")[0],""+(vectors.size()+1),imgPid,file.getAbsolutePath(),LayerType.RASTERLAYER));
        }

        List<File> otmsList = resourcesManager.getOtmsFolder();
        List<Map<String, List<File>>> mapList = resourcesManager.getChildData(otmsList);
        for(int i=0;i<mapList.size();i++){
            Map<String, List<File>> map = mapList.get(i);
            List<File> fileList = new ArrayList<>();
            for(String key : map.keySet()){
                fileList.addAll(map.get(key));
                vectors.add(new Vector(new File(key).getName(),""+(vectors.size()+1),"",key,LayerType.FOLDER));
            }

            if(fileList.size() == 0){
                continue;
            }
            String vecPid =""+ vectors.size();
            for(File file : fileList){
                vectors.add(new Vector(file.getName().split("\\.")[0],""+(vectors.size()+1),vecPid,file.getAbsolutePath(),LayerType.FEATURElAYER));
            }
        }

        for(TreeNode<Vector> treeNode : dataToBind){
            Vector vector = treeNode.getData();
            for(Vector vector1 : vectors){
                if(vector.getPath().equals(vector1.getPath())){
                    vector1.setLayer(vector.getLayer());
                    vector1.setVisible(vector.isVisible());
                    break;
                }
            }
        }
        dataToBind.clear();
        return vectors;
    }

    private void updataVector(Vector vector){
        List<TreeNode<Vector>> treeNodes = new ArrayList<>();
        for(TreeNode<Vector> treeNode : dataToBind){
            Vector vec = treeNode.getData();
            if(vec.getPath().equals(vector.getPath())){
                vec.setLayer(vector.getLayer());
                vec.setVisible(vector.isVisible());
                vec.setLayerType(vector.getLayerType());
                treeNode.setData(vec);
            }

            treeNodes.add(treeNode);
        }

        dataToBind.clear();
        dataToBind.addAll(treeNodes);
    }

    /**获取本地数据 构建树形*/
    private List<Vector> addList(){
        List<Vector> vectors = new ArrayList<>();
        ResourcesManager resourcesManager = ResourcesManager.getInstance(iMap.getActivity());

        List<File> imglist = resourcesManager.getImgTitlePath();

        if(imglist.size() == 0){
            vectors.add(new Vector("影像地图",""+(vectors.size()+1),"","",LayerType.FOLDER));
        }else{
            vectors.add(new Vector("影像地图",""+(vectors.size()+1),"",imglist.get(0).getParent(),LayerType.FOLDER));
        }

        String imgPid = vectors.size()+"";
        for(File file : imglist){
            vectors.add(new Vector(file.getName().split("\\.")[0],""+(vectors.size()+1),imgPid,file.getAbsolutePath(),LayerType.RASTERLAYER));
        }

        List<File> otmsList = resourcesManager.getOtmsFolder();
        List<Map<String, List<File>>> mapList = resourcesManager.getChildData(otmsList);
        for(int i=0;i<mapList.size();i++){
            Map<String, List<File>> map = mapList.get(i);
            List<File> fileList = new ArrayList<>();
            for(String key : map.keySet()){
                fileList.addAll(map.get(key));
                vectors.add(new Vector(new File(key).getName(),""+(vectors.size()+1),"",key,LayerType.FOLDER));
            }
            if(fileList.size() == 0){
                continue;
            }
            String vecPid =""+ vectors.size();
            for(File file : fileList){
                vectors.add(new Vector(file.getName().split("\\.")[0],""+(vectors.size()+1),vecPid,file.getAbsolutePath(),LayerType.FEATURElAYER));
            }
        }
        return vectors;
    }

    private void addLayer(Vector vector){
        if(vector.getPath().endsWith(".tpk") || vector.getPath().endsWith(".img") || vector.getPath().endsWith(".tif")){
            addTpk(vector);
        }else if(vector.getPath().endsWith(".geodatabase") || vector.getPath().endsWith(".shp") || vector.getPath().endsWith(".otms")){
            addVector(vector);
        }else{
            ToastUtil.setToast(iMap.getActivity(),"数据类型错误");
        }
    }

    /**添加影像数据*/
    private void addTpk(Vector vector){
        File file = new File(vector.getPath());
        if(file.getName().endsWith(".tpk")){
            TileCache tileCache = new TileCache(file.getAbsolutePath());
            ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);
            chanageLayerIndex(tiledLayer);
            vector.setLayer(tiledLayer);
            vector.setLayerType(LayerType.RASTERLAYER);
        }else if(file.getName().endsWith(".img") || file.getName().endsWith(".tif")){
            Raster raster = new Raster(file.getPath());
            RasterLayer rasterLayer = new RasterLayer(raster);
            chanageLayerIndex(rasterLayer);
            vector.setLayer(rasterLayer);
            vector.setLayerType(LayerType.RASTERLAYER);
        }
        updataVector(vector);
    }

    private void zoomToLayer(Vector vector){

        Layer layer = vector.getLayer();
        if(layer == null){
            ToastUtil.setToast(iMap.getActivity(),"图层未加载");
            return;
        }
        if(layer instanceof ArcGISTiledLayer){
            ArcGISTiledLayer tiledLayer = (ArcGISTiledLayer) layer;
            Envelope envelope = tiledLayer.getFullExtent();
            if(envelope != null){
                iMap.getMapview().setViewpointGeometryAsync(envelope);
            }else{
                ToastUtil.setToast(iMap.getActivity(),"获取不到数据范围");
            }

        }else if(layer instanceof RasterLayer){
            RasterLayer rasterLayer = (RasterLayer) layer;
            Envelope envelope = rasterLayer.getFullExtent();
            if(envelope != null){
                iMap.getMapview().setViewpointGeometryAsync(envelope);
            }else{
                ToastUtil.setToast(iMap.getActivity(),"获取不到数据范围");
            }
        }else if(layer instanceof FeatureLayer){
            FeatureLayer featureLayer = (FeatureLayer) layer;
            Envelope envelope = featureLayer.getFullExtent();
            if(envelope != null ){
                iMap.getMapview().setViewpointGeometryAsync(envelope);
            }else{
                ToastUtil.setToast(iMap.getActivity(),"获取不到数据范围");
            }
        }
    }

    /**添加矢量数据*/
    public void addVector(Vector vector){
        if(vector.getPath().endsWith(".geodatabase") || vector.getPath().endsWith(".otms")){
            addGeodatabase(vector);
        }else if(vector.getPath().endsWith(".shp")){
            addShape(vector);
        }
    }
    /**添加.geodatabase格式矢量数据*/
    private void addGeodatabase(Vector vector){
        Geodatabase geodatabase = new Geodatabase(vector.getPath());
        geodatabase.loadAsync();

        geodatabase.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                List<GeodatabaseFeatureTable> list = geodatabase.getGeodatabaseFeatureTables();
                for(GeodatabaseFeatureTable gdbTable : list){
                    FeatureLayer featureLayer = new FeatureLayer(gdbTable);
                    chanageLayerIndex(featureLayer);
                    setRender(featureLayer,vector);
                    vector.setLayer(featureLayer);
                    vector.setLayerType(LayerType.FEATURElAYER);

                    updataVector(vector);
                }
            }
        });
    }
    /**添加shape格式的矢量数据*/
    private void addShape(Vector vector){
        String path = vector.getPath();
        ShapefileFeatureTable table = new ShapefileFeatureTable(path);
        table.loadAsync();
        table.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                FeatureLayer featureLayer = new FeatureLayer(table);
                chanageLayerIndex(featureLayer);
                setRender(featureLayer,vector);
                vector.setLayer(featureLayer);
                vector.setLayerType(LayerType.FEATURElAYER);
                updataVector(vector);

            }
        });
    }

    private void setRender(FeatureLayer featureLayer,Vector vector){
        GeometryType geometryType = featureLayer.getFeatureTable().getGeometryType();

        Renderer renderer = null;
        if(geometryType == GeometryType.POINT){
            renderer = RenderUtil.getMarkRender(iMap.getActivity());
        }else if(geometryType == GeometryType.POLYLINE){
            renderer = RenderUtil.getLineRender(iMap.getActivity(),vector.getPath());
        }else if(geometryType == GeometryType.POLYGON){
            renderer = RenderUtil.getHisRender(iMap.getActivity(),vector.getPath());
        }else{
            renderer = RenderUtil.getHisRender(iMap.getActivity(),vector.getPath());
        }
        featureLayer.setRenderer(renderer);
    }

    /**移除矢量图层*/
    public void removeLayer(Vector vector){
        Layer layer = vector.getLayer();
        LayerList layerList = iMap.getMapview().getMap().getOperationalLayers();
        if(layer instanceof ArcGISTiledLayer){
            ArcGISTiledLayer tiledLayer = (ArcGISTiledLayer) layer;
            layerList.remove(tiledLayer);
        }else if(layer instanceof RasterLayer ){
            RasterLayer rasterLayer = (RasterLayer) layer;
            layerList.remove(rasterLayer);
        }else if(layer instanceof FeatureLayer){
            FeatureLayer featureLayer = (FeatureLayer) layer;
            layerList.remove(featureLayer);
        }
        vector.setLayer(null);
        updataVector(vector);
        iMap.getMapview().invalidate();
    }

    private void chanageLayerIndex(Layer layer){
        layers = new ArrayList<>();
        LayerList layerList = iMap.getMapview().getMap().getOperationalLayers();
        Iterator<Layer> iterator = layerList.iterator();
        while (iterator.hasNext()){
            Layer bLayer = iterator.next();
            layers.add(bLayer);
            if(bLayer instanceof FeatureLayer){
                iterator.remove();
            }
        }

        if(layer instanceof ArcGISTiledLayer){
            ArcGISTiledLayer tiledLayer = (ArcGISTiledLayer) layer;
            iMap.getMapview().getMap().getOperationalLayers().add(tiledLayer);
        }else if(layer instanceof RasterLayer){
            RasterLayer rasterLayer = (RasterLayer) layer;
            iMap.getMapview().getMap().getOperationalLayers().add(rasterLayer);
            rasterLayer.loadAsync();
            rasterLayer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    Log.e("======",rasterLayer.getLoadStatus().name());
                }
            });
        }

        for(Layer cLayer : layers){
            if(cLayer instanceof FeatureLayer){
                FeatureLayer featureLayer = (FeatureLayer)  cLayer;
                iMap.getMapview().getMap().getOperationalLayers().add(featureLayer);
            }
        }

        if(layer instanceof FeatureLayer){
            FeatureLayer featureLayer = (FeatureLayer) layer;
            iMap.getMapview().getMap().getOperationalLayers().add(featureLayer);
        }
    }

    public ArrayList<FeatureLayer> getFeatureLayers(){
        ArrayList<FeatureLayer> featureLayers = new ArrayList<>();
        LayerList layerList = iMap.getMapview().getMap().getOperationalLayers();
        for(Layer layer : layerList){
            if(layer instanceof FeatureLayer){
                FeatureLayer featureLayer = (FeatureLayer) layer;
                featureLayers.add(featureLayer);
            }
        }
        return featureLayers;
    }

    public void creatFolder(){

        if(iMap.getMapPermission().checkStorage()){
            FileUtil.createAllFile(Config.APP_PATH);
            FileUtil.createAllFile(Config.APP_BASE_PATH);
            FileUtil.createAllFile(Config.APP_IMG_PATH);
            FileUtil.createAllFile(Config.APP_PATH_VECTOR);
        }
    }

    public List<Vector> getFeatureLayer(){
        List<Vector> vectorList = new ArrayList<>();
        for(TreeNode<Vector> treeNode : getDataToBind()){
            List<TreeNode<Vector>> childList = treeNode.getChildren();
            for(TreeNode<Vector> childTree:childList){
                Vector child = childTree.getData();
                if(child.getLayer() != null && child.getLayer() instanceof FeatureLayer){
                    vectorList.add(child);
                }
            }
        }
        return vectorList;
    }


}
