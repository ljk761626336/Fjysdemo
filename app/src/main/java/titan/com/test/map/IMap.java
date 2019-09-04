package titan.com.test.map;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.drawtool.DrawTool;

import titan.com.test.map.senter.MapPermission;
import titan.com.test.util.ActionModel;

public interface IMap {

    MapView getMapview();

    MainActivity getActivity();

    Context getContext();

    Point getGpspoint();

    Point getLastpoint();

    Point getMappoint();

    MapPermission getMapPermission();

    SpatialReference getDefalutSpatial();

}
