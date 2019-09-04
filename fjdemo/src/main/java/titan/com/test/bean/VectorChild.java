package titan.com.test.bean;

import com.ahao.basetreeview.model.NodeId;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VectorChild implements NodeId {

    private String id;

    private String parentId;

    private Vector vector;

    private FeatureTable table;

    private List<Field> fieldList = new ArrayList<>();


    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPId() {
        return parentId;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public FeatureTable getTable() {
        return table;
    }

    public void setTable(FeatureTable table) {
        this.table = table;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public VectorChild(){

    }

    public VectorChild(String id, Vector vector) {
        this.id = id;
        this.parentId = vector.getId();
        this.vector = vector;
    }

    @Override
    public String toString() {
        String parent = new File(vector.getPath()).getParent();
        String pname = new File(parent).getName().split("\\.")[0];
        return pname+"---"+this.vector.getName()+"---"+table.getTableName();
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