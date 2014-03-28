package com.seu.hitrip.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.beardedhen.androidbootstrap.BootstrapButton;
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 1) {

                int i = msg.arg1;
                if(i == mFootprints.size()) {
                    footprintLayer.clearAll();
                }else{
                    //map.postInvalidate();
                    MapObjectModel m = mFootprints.get(i);
                    map.scrollMapTo(m.getX(), m.getY());
                    addNotScalableMapObject(m,footprintLayer);

                }
            }
            super.handleMessage(msg);
        }
    };

    private View selfView;

    private static final String TAG = "FragmentSceneryMap";

    private static final Integer SCENERY_LAYER_ID = 0;
    private static final Integer FOOTPRINT_LAYER_ID = 1;
    private static final Integer PEOPLE_LAYER_ID = 2;
    private static final int MAP_ID = 23;
    private int pinHeight;

    private MapObjectContainer mScenery;
    private MapWidget map;
    private Popup mapObjectInfoPopup;

    private Location[] points;
    private int currentPoint;
    private int maxLevel=14;
    private int minLevel=11;

    private LocationManagerProxy mAMapLocManager;
    private Handler handler = new Handler();
    private AMapLocation aMapLocation;
    private Double longitude;
    private Double latitude;
    private Layer sceneryLayer;
    private Layer footprintLayer;
    private Layer peopleLayer;
    private ArrayList<MapObjectModel> mFootprints;
    private ArrayList<MapObjectModel> mPeople;

    public FragmentSceneryMap(){
        super();
    }

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
        initLayer();
        initMapObjects();
        initMapListeners();
        map.setShowMyPosition(false);
        // map.centerMap();

        BootstrapButton footprintButton = (BootstrapButton) selfView.findViewById(R.id.footprint);
        footprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FootprintDisplayThread().start();
            }
        });

        return selfView;
    }

    private void initLayer() {
        sceneryLayer = map.getLayerById(SCENERY_LAYER_ID);
        footprintLayer = map.getLayerById(FOOTPRINT_LAYER_ID);
        peopleLayer = map.getLayerById(PEOPLE_LAYER_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGps();
        mScenery = new MapObjectContainer();
    }
    public void initGps(){

        mAMapLocManager = LocationManagerProxy.getInstance(getActivity());

        mAMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, this);

        handler.postDelayed(this, 12000);

    }


    @Override
    public void onPause() {
        super.onPause();
        stopLocation();
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

            Toast.makeText(getActivity(), "当前地理位置坐标:(" + longitude + "," + latitude + ")", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void run() {
        if (aMapLocation == null) {
            // Toast.makeText(getActivity(), "12���ڻ�û�ж�λ�ɹ���ֹͣ��λ", Toast.LENGTH_SHORT).show();
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

    private void createLayer(){
        map.createLayer(SCENERY_LAYER_ID);
        map.createLayer(FOOTPRINT_LAYER_ID);
        map.createLayer(PEOPLE_LAYER_ID);

    }

    private void configMap(){
        OfflineMapConfig config = map.getConfig();
        config.setZoomBtnsVisible(true);
        config.setPinchZoomEnabled(true);
        config.setFlingEnabled(true);
        config.setMaxZoomLevelLimit(maxLevel);
        config.setMinZoomLevelLimit(minLevel);
        config.setMapCenteringEnabled(true);

        GPSConfig gpsConfig = config.getGpsConfig();
        gpsConfig.setPassiveMode(false);
        gpsConfig.setGPSUpdateInterval(1000, 5);
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
        createLayer();

        configMap();
        configureLocationPointer();

        FrameLayout layout = (FrameLayout) selfView.findViewById(R.id.rootLayout);

        layout.addView(map, 0);
        layout.setBackgroundColor(Color.parseColor("#4CB263"));


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
        MapObjectModel objectModel = new MapObjectModel(500, 300, "location1", getResources().getDrawable(R.drawable.map_object));
        mScenery.addObject(objectModel);
        objectModel = new MapObjectModel(600, 350, "location2", getResources().getDrawable(R.drawable.map_object));
        mScenery.addObject(objectModel);
    }


    private void initMapObjects()
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.map_point_detail, null);

        mapObjectInfoPopup = new Popup(getActivity(),view, (FrameLayout)selfView.findViewById(R.id.rootLayout),map);

        for (int i=0; i< mScenery.size(); ++i) {
            addNotScalableMapObject(mScenery.getObject(i), sceneryLayer);
        }

        int[][] positions = {
                {1157,2284},
                {1144,2124},
                {1138,1840},
                {1138,1590},
                {1406,1342},
                {1798,1406},
                {2052,1492},
                {1756,748},
                {2052,448},
                {2112,148},
                {1684,240},
                {1172,728},
                {248,532},
                {592,920},
                {388,884},
                {80,988},
                {772,1104},
                {1144,996},
                {392,1380},
                {240,1932},
                {276,2188},
                {968,2108}
        };

        mFootprints = new ArrayList<MapObjectModel>(30);
        for(int i = 1; i < positions.length ; i++)
            mFootprints.add(new MapObjectModel(positions[i][0]/2, positions[i][1]/2,"footprint",getResources().getDrawable(R.drawable.g)));

//        mPeople = new ArrayList<MapObjectModel>(30);
//
//        for(int i = 1; i < 5 ; i++)
//            mPeople.add(new MapObjectModel(MathTools.getRandomBetween(1,2000), MathTools.getRandomBetween(1,2400)/2,"footprint",getResources().getDrawable(R.drawable.avatar_small)));

//        for (int i=0; i< mPeople.size(); i++) {
//            addNotScalableMapObject(mPeople.get(i), peopleLayer);
//        }

    }


    public void addNotScalableMapObject(MapObjectModel objectModel,  Layer layer){
        if (objectModel.getLocation() != null) {
            addNotScalableMapObject(objectModel, objectModel.getLocation(), layer, objectModel.getPic());
        } else {
            addNotScalableMapObject(objectModel, objectModel.getX(), objectModel.getY(), layer, objectModel.getPic());
        }
    }
    private void addNotScalableMapObject(MapObjectModel objectModel, int x, int y,  Layer layer, Drawable drawable) {
        pinHeight = drawable.getIntrinsicHeight();
        MapObject object1 = new MapObject(objectModel ,drawable,new Point(x, y), PivotFactory.createPivotPoint(drawable, PivotFactory.PivotPosition.PIVOT_CENTER),true, false);
        layer.addMapObject(object1);
    }
    private void addNotScalableMapObject(MapObjectModel objectModel, Location location, Layer layer, Drawable drawable) {
        if (location == null)
            return;
        MapObject object1 = new MapObject(
                objectModel,
                drawable,
                new Point(0, 0),
                PivotFactory.createPivotPoint(drawable, PivotFactory.PivotPosition.PIVOT_CENTER),
                true,
                true);
        layer.addMapObject(object1);

        object1.moveTo(location);
    }

    private void initMapListeners()
    {
        map.setOnMapTouchListener(this);

        map.addMapEventsListener(this);

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



    @Override
    public void onPostZoomIn()
    {
        Log.i(TAG, "onPostZoomIn()");

    }

    @Override
    public void onPostZoomOut()
    {
        Log.i(TAG, "onPostZoomOut()");

    }

    @Override
    public void onPreZoomIn()
    {
        Log.i(TAG, "onPreZoomIn()");


        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }

        if (map.getZoomLevel()+1==maxLevel) {
            Toast.makeText(getActivity(), "不能继续缩放了", 3000).show();
        }

    }

    @Override
    public void onPreZoomOut()
    {
        Log.i(TAG, "onPreZoomOut()");

        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }

        if (map.getZoomLevel()-1==minLevel) {
            Toast.makeText(getActivity(), "不能继续缩放了", 3000).show();
        }
    }


    //* On map touch listener implemetnation *//
    @Override
    public void onTouch(MapWidget v, MapTouchedEvent event)
    {

        MapObjectModel objectModel = new MapObjectModel(500, 800, "location3", getResources().getDrawable(R.drawable.map_object));
        addNotScalableMapObject(objectModel, sceneryLayer);

        ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();

        if (touchedObjs.size() > 0) {

            int xInMapCoords = event.getMapX();
            int yInMapCoords = event.getMapY();
            int xInScreenCoords = event.getScreenX();
            int yInScreenCoords = event.getScreenY();

            ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds().get(0);
            long layerId = objectTouchEvent.getLayerId();
            MapObjectModel mapObjectModel = (MapObjectModel)objectTouchEvent.getObjectId();

            String message = "You touched the object with id: " + mapObjectModel.getCaption() + " on layer: " + layerId +
                    " mapX: " + xInMapCoords + " mapY: " + yInMapCoords + " screenX: " + xInScreenCoords + " screenY: " +
                    yInScreenCoords;

            Log.d(TAG, message);

            if (mapObjectModel != null) {
                float density = getResources().getDisplayMetrics().density;
                int imgHeight = (int) (pinHeight / density / 2);

                int x = xToScreenCoords(mapObjectModel.getX());
                int y = yToScreenCoords(mapObjectModel.getY()) - imgHeight;

                showLocationsPopup(mapObjectModel,x, y, mapObjectModel.getCaption());
            } else {
                showLocationsPopup(mapObjectModel,xInScreenCoords, yInScreenCoords, "Shows where user touched");
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

    class FootprintDisplayThread extends Thread{

        @Override
        public void run() {
            for(int i = 0; i <= mFootprints.size() ; i++){
                SystemClock.sleep(1000);
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.arg1 = i;
                msg.sendToTarget();

            }
        }
    }

}

