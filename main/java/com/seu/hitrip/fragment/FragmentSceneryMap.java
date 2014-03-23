package com.seu.hitrip.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.GPSConfig;
import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnLocationChangedListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.seu.hitrip.cose.R;
import com.seu.hitrip.map.MapObjectContainer;
import com.seu.hitrip.map.MapObjectModel;
import com.seu.hitrip.map.Popup;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by yqf on 3/23/14.
 */
public class FragmentSceneryMap extends BaseFragment
        implements MapEventsListener,OnMapTouchListener,AMapLocationListener, Runnable {

    private View selfView;

    private static final String TAG = "FragmentSceneryMap";

    private static final Integer LAYER1_ID = 0;
    private static final int MAP_ID = 23;

    private int nextObjectId;
    private int pinHeight;

    private MapObjectContainer model;
    private MapWidget map;
    private Popup mapObjectInfoPopup;

    private Location[] points;
    private int currentPoint;

    private MediaPlayer mp;

    private int maxLevel=14;
    private int minLevel=11;

    private LocationManagerProxy mAMapLocManager;
    private Handler handler = new Handler();
    private AMapLocation aMapLocation;// �����ж϶�λ��ʱ
    private Double longitude;
    private Double latitude;

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_scenery_map_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selfView = inflater.inflate(R.layout.fragment_scenery_map, container, false);
        initTestLocationPoints();
        initMap(savedInstanceState);
        initModel();
        initMapObjects();
        initMapListeners();
        map.setShowMyPosition(false);
        map.centerMap();

        return selfView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGps();

        nextObjectId = 0;

        model = new MapObjectContainer();

    }
    public void initGps(){

        mAMapLocManager = LocationManagerProxy.getInstance(getActivity());

        mAMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, this);

        handler.postDelayed(this, 12000);

    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocation();// ֹͣ��λ
    }

    private void stopLocation() {
        if (mAMapLocManager != null) {
            mAMapLocManager.removeUpdates(this);
            mAMapLocManager.destory();
        }
        mAMapLocManager = null;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            this.aMapLocation = location;

            longitude = location.getLongitude();
            latitude = location.getLatitude();

            Toast.makeText(getActivity(), "��λ�ɹ�:(" + longitude + "," + latitude + ")", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void run() {
        if (aMapLocation == null) {
            Toast.makeText(getActivity(), "12���ڻ�û�ж�λ�ɹ���ֹͣ��λ", Toast.LENGTH_SHORT).show();
            stopLocation();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.saveState(outState);
    }

    private void initTestLocationPoints()
    {
        points = new Location[5];
        for (int i=0; i<points.length; ++i) {
            points[i] = new Location("test");
        }

        points[0].setLatitude(3.2127012756213316);
        points[0].setLongitude(73.03406774997711);

        points[1].setLatitude(3.2122245926560167);
        points[1].setLongitude(73.03744733333588);

        points[2].setLatitude(3.2112819380469135);
        points[2].setLongitude(73.03983449935913);

        points[3].setLatitude(3.2130494147249915);
        points[3].setLongitude(73.03946435451508);

        points[4].setLatitude(3.2148276002942713);
        points[4].setLongitude(73.03796768188477);

        currentPoint = 0;
    }


    private Location getNextLocationPoint()
    {
        if (currentPoint < points.length-1) {
            currentPoint += 1;
        } else {
            currentPoint = 0;
        }

        return points[currentPoint];
    }


    private void initMap(Bundle savedInstanceState)
    {
        Class<?> c = null;
        try {
            c = Class.forName("com.ls.widgets.map.utils.Resources");
            Object obj = c.newInstance();
            Field field = c.getDeclaredField("LOGO");
            field.setAccessible(true);
            field.set(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        map = new MapWidget(savedInstanceState, getActivity(), "map", 12);

        map.setId(MAP_ID);

        OfflineMapConfig config = map.getConfig();
        config.setZoomBtnsVisible(true);
        config.setPinchZoomEnabled(true);
        config.setFlingEnabled(true);
        config.setMaxZoomLevelLimit(maxLevel);
        config.setMinZoomLevelLimit(minLevel);
        config.setMapCenteringEnabled(true);

        GPSConfig gpsConfig = config.getGpsConfig();
        gpsConfig.setPassiveMode(false);
        gpsConfig.setGPSUpdateInterval(500, 5);

        configureLocationPointer();

        FrameLayout layout = (FrameLayout) selfView.findViewById(R.id.rootLayout);

        layout.addView(map, 0);
        layout.setBackgroundColor(Color.parseColor("#4CB263"));

        map.createLayer(LAYER1_ID);
    }




    private void configureLocationPointer()
    {
        MapGraphicsConfig graphicsConfig = map.getMapGraphicsConfig();

        graphicsConfig.setAccuracyAreaColor(0x55FF0000);

        graphicsConfig.setAccuracyAreaBorderColor(Color.RED);

        graphicsConfig.setDotPointerDrawableId(R.drawable.round_pointer);

        graphicsConfig.setArrowPointerDrawableId(R.drawable.arrow_pointer);

    }


    private void initModel()
    {
        MapObjectModel objectModel = new MapObjectModel(0, 500, 300, "«������԰",0);
        model.addObject(objectModel);
        objectModel = new MapObjectModel(1, 600, 350, "��Ӿ���",0);
        model.addObject(objectModel);
    }


    private void initMapObjects()
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.map_point_detail, null);

        mapObjectInfoPopup = new Popup(getActivity(),view, (FrameLayout)selfView.findViewById(R.id.rootLayout));


        Layer layer1 = map.getLayerById(LAYER1_ID);


        for (int i=0; i<model.size(); ++i) {
            addNotScalableMapObject(model.getObject(i), layer1);
        }

    }

    private void addNotScalableMapObject(int x, int y,  Layer layer)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.map_object);
        pinHeight = drawable.getIntrinsicHeight();
        MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),drawable,new Point(x, y), PivotFactory.createPivotPoint(drawable, PivotFactory.PivotPosition.PIVOT_CENTER),true, false);
        layer.addMapObject(object1);
        nextObjectId += 1;
    }


    private void addNotScalableMapObject(MapObjectModel objectModel,  Layer layer)
    {
        if (objectModel.getLocation() != null) {
            addNotScalableMapObject(objectModel.getLocation(), layer);
        } else {
            addNotScalableMapObject(objectModel.getX(), objectModel.getY(),  layer);
        }
    }


    private void addNotScalableMapObject(Location location, Layer layer) {
        if (location == null)
            return;
        Drawable drawable = getResources().getDrawable(R.drawable.map_object);
        MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),
                drawable,
                new Point(0, 0),
                PivotFactory.createPivotPoint(drawable, PivotFactory.PivotPosition.PIVOT_CENTER),
                true,
                true);
        layer.addMapObject(object1);

        object1.moveTo(location);
        nextObjectId += 1;
    }


    private void addScalableMapObject(int x, int y, Layer layer)
    {
        Drawable drawable = getResources().getDrawable(R.drawable.map_object);
        MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),
                drawable,
                x,
                y,
                true,
                true);

        layer.addMapObject(object1);
        nextObjectId += 1;
    }


    private void initMapListeners()
    {
        // ������ͼʱ��
        map.setOnMapTouchListener(this);

        //Ϊ���ڷŴ�ǰ��ִ��һЩ����
        map.addMapEventsListener(this);

        //������ͼ�¼�
        map.setOnMapScrolledListener(new OnMapScrollListener()
        {
            public void onScrolledEvent(MapWidget v, MapScrolledEvent event)
            {
                handleOnMapScroll(v, event);
            }
        });



        map.setOnLocationChangedListener(new OnLocationChangedListener() {
            @Override
            public void onLocationChanged(MapWidget v, Location location) {
                // You can handle location change here.
                // For example you can scroll to new location by using v.scrollMapTo(location)
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = new MenuInflater(getActivity());
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.zoomIn:
                map.zoomIn();
                return true;
            case R.id.zoomOut:
                map.zoomOut();
                return true;

            case R.id.scroll_next:
                map.scrollMapTo(getNextLocationPoint());
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void handleOnMapScroll(MapWidget v, MapScrolledEvent event)
    {
        int dx = event.getDX();

        int dy = event.getDY();

        if (mapObjectInfoPopup.isVisible()) {
            mapObjectInfoPopup.moveBy(dx, dy);
        }
    }


    //��ͼ�Ŵ����¼�
    @Override
    public void onPostZoomIn()
    {
        Log.i(TAG, "onPostZoomIn()");

    }
    //��ͼ��С����¼�
    @Override
    public void onPostZoomOut()
    {
        Log.i(TAG, "onPostZoomOut()");

    }
    //��ͼ�ڷŴ�ǰ���õ��¼�
    @Override
    public void onPreZoomIn()
    {
        Log.i(TAG, "onPreZoomIn()");


        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }

        if (map.getZoomLevel()+1==maxLevel) {
            Toast.makeText(getActivity(), "�ѷŴ���󼶱�", 3000).show();
        }

    }
    //��ͼ����СǮ���õ��¼�
    @Override
    public void onPreZoomOut()
    {
        Log.i(TAG, "onPreZoomOut()");

        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }

        if (map.getZoomLevel()-1==minLevel) {
            Toast.makeText(getActivity(), "����С����С����", 3000).show();
        }
    }


    //* On map touch listener implemetnation *//
    @Override
    public void onTouch(MapWidget v, MapTouchedEvent event)
    {
        // �õ������Ķ���
        ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();
        //�ж��Ƿ�ͬʱ��������ͼ����
        if (touchedObjs.size() > 0) {

            int xInMapCoords = event.getMapX();
            int yInMapCoords = event.getMapY();
            int xInScreenCoords = event.getScreenX();
            int yInScreenCoords = event.getScreenY();

            ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds().get(0);
            //�ĵ�ͼ�����
            long layerId = objectTouchEvent.getLayerId();
            //�õ���ͼ����id
            Integer objectId = (Integer)objectTouchEvent.getObjectId();

            String message = "You touched the object with id: " + objectId + " on layer: " + layerId +
                    " mapX: " + xInMapCoords + " mapY: " + yInMapCoords + " screenX: " + xInScreenCoords + " screenY: " +
                    yInScreenCoords;

            Log.d(TAG, message);
            //���id�ҵ���ͼ����
            MapObjectModel objectModel = model.getObjectById(objectId.intValue());

            if (objectModel != null) {
                //�ĵ���Ļ�ܶ�
                float density = getResources().getDisplayMetrics().density;
                int imgHeight = (int) (pinHeight / density / 2);

                int x = xToScreenCoords(objectModel.getX());
                int y = yToScreenCoords(objectModel.getY()) - imgHeight;

                //��ʾ����
                showLocationsPopup(objectModel,x, y, objectModel.getCaption());
            } else {
                // ���ͬʱ�������Ƕ�� �ж��û�������ĸ�
                showLocationsPopup(objectModel,xInScreenCoords, yInScreenCoords, "Shows where user touched");
            }

        } else {
            if (mapObjectInfoPopup != null) {
                mapObjectInfoPopup.hide();
            }
        }
    }


    private void showLocationsPopup(MapObjectModel objectModel,int x, int y, String text)
    {
        FrameLayout mapLayout = (FrameLayout) selfView.findViewById(R.id.rootLayout);

        if (mapObjectInfoPopup != null)
        {
            mapObjectInfoPopup.hide();
        }
        mapObjectInfoPopup.setInfo(objectModel,text);
        ((Popup) mapObjectInfoPopup).show(mapLayout, x, y);
    }

    /***
     * Transforms coordinate in map coordinate system to screen coordinate system
     * @param mapCoord - X in map coordinate in pixels.
     * @return X coordinate in screen coordinates. You can use this value to display any object on the screen.
     */
    private int xToScreenCoords(int mapCoord)
    {
        return (int)(mapCoord *  map.getScale() - map.getScrollX());
    }

    private int yToScreenCoords(int mapCoord)
    {
        return (int)(mapCoord *  map.getScale() - map.getScrollY());
    }


}

