package titan.com.test.layercontrol;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;

import com.esri.arcgisruntime.arcgisservices.ArcGISMapServiceInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.SublayerList;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.LayerList;

import java.util.ArrayList;
import java.util.List;

import titan.com.test.R;

public class LayerPresenter implements LayerManagerAdapter.MyItemClickListener {

    private ILayerControl _iLayerControl;

    private List<ItemLayer> arrayList = new ArrayList<>();
    private LayerManagerAdapter adapter;


    public LayerPresenter(ILayerControl control){
        this._iLayerControl = control;
    }

    public void initLayer(){
        RecyclerView recyclerView = _iLayerControl.getActivity().findViewById(R.id.layer_recycview);
        String[] layers = _iLayerControl.getContext().getResources().getStringArray(R.array.verctor_online_layer);
        if(adapter == null){
            for (String layer : layers){
                ItemLayer itemLayer = new ItemLayer();
                itemLayer.setName(layer.split(",")[0]);
                itemLayer.setUrl(layer.split(",")[1]);
                arrayList.add(itemLayer);
            }
            adapter = new LayerManagerAdapter(_iLayerControl.getContext(),arrayList);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(_iLayerControl.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.item_imglayer:
                CheckedTextView cview = ((CheckedTextView) view);
                cview.toggle();
                boolean flag = cview.isChecked();
                ItemLayer itemLayer = adapter.getFileList().get(position);
                itemLayer.setSelect(flag);
                adapter.notifyDataSetChanged();

                if (flag) {
                    final ArcGISMapImageLayer mapImageLayer = new ArcGISMapImageLayer(itemLayer.getUrl());
                    itemLayer.setLayer(mapImageLayer);
                    // Add a listener that is invoked when layer loading has completed.
                    mapImageLayer.addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {
                            if (mapImageLayer.getLoadStatus() == LoadStatus.LOADED) {
                                ArcGISMapServiceInfo mapServiceInfo = mapImageLayer.getMapServiceInfo();
                                // work with map service info here
                                SublayerList sublayers = mapImageLayer.getSublayers();
                                for (ArcGISSublayer sublayer : sublayers) {
                                    sublayer.setVisible(true);
                                    setArcGISSublayerVisble(sublayer);
                                }
                            }
                        }
                    });

                    mapImageLayer.setName(itemLayer.getName());
                    _iLayerControl.getMapview().getMap().getOperationalLayers().add(mapImageLayer);
                    mapImageLayer.setVisible(true);

                } else {
                    Layer layer = itemLayer.getLayer();
                    if (layer != null) {
                        _iLayerControl.getMapview().getMap().getOperationalLayers().remove(layer);
                    }
                }
                break;

            case R.id.item_img_location:
                setExtent(adapter.getFileList().get(position));
                break;
        }
    }

    private void setArcGISSublayerVisble(ArcGISSublayer sublayer){
        if(sublayer.getSublayers().size() == 0){
            return;
        }

        for(ArcGISSublayer gisSublayer : sublayer.getSublayers()){
            gisSublayer.setVisible(true);
            setArcGISSublayerVisble(gisSublayer);
        }
    }

    private void setExtent(ItemLayer itemLayer) {
        LayerList baseLayers = _iLayerControl.getMapview().getMap().getOperationalLayers();
        for (Layer layer : baseLayers) {
            String name = layer.getName();
            String layerName = itemLayer.getName();
            if (name.equals(layerName)) {
                Envelope fullExtent = layer.getFullExtent();
                if (fullExtent != null && !fullExtent.isEmpty()) {
                    _iLayerControl.getMapview().setViewpointGeometryAsync(fullExtent);
                }
            }
        }
    }

}
