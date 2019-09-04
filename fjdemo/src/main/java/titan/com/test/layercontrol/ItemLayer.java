package titan.com.test.layercontrol;

import com.esri.arcgisruntime.layers.Layer;

import java.io.Serializable;

public class ItemLayer implements Serializable {

    private static final long serialVersionUID = 4109288410947928966L;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect = false;

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    private Layer layer;

    @Override
    public String toString() {
        return this.name;
    }
}
