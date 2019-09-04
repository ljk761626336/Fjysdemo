package titan.com.test.bean;

import com.ahao.basetreeview.model.NodeId;
import com.esri.arcgisruntime.layers.Layer;

import java.io.File;

public class Vector implements NodeId {

    private String id;

    private String parentId;

    private String name;

    private String path;

    private boolean visible = false;

    private Layer layer;

    private LayerType layerType;

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPId() {
        return parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public LayerType getLayerType() {
        return layerType;
    }

    public void setLayerType(LayerType layerType) {
        this.layerType = layerType;
    }

    public Vector(){

    }

    public Vector(String name, String id, String parentId,String path,LayerType layerType) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.path = path;
        this.layerType = layerType;
    }

    @Override
    public String toString() {
        String value = "";
        String parent = new File(path).getParent();
        String pname = new File(parent).getName().split("\\.")[0];
        return pname+"---"+this.name;
    }

    /*@Override
    public String toString() {
        return "Vector{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }*/


}