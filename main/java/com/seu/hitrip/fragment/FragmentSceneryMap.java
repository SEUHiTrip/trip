package com.seu.hitrip.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.seu.hitrip.R;
import com.seu.hitrip.card.NewsCard;
import com.seu.hitrip.map.MapObjectContainer;
import com.seu.hitrip.map.MapObjectModel;
import com.seu.hitrip.map.Popup;
import com.seu.hitrip.person.PersonalInfo;
import com.seu.hitrip.util.StatusActivity;
import com.seu.hitrip.web.WebPostFileTask;
import com.seu.hitrip.web.WebPostTextTask;
import com.seu.hitrip.web.WebTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yqf on 3/23/14.
 */
public class FragmentSceneryMap extends BaseFragment
        implements MapEventsListener,OnMapTouchListener {
    private static final String TAG = "FragmentSceneryMap";
    private static final Integer SCENERY_LAYER_ID = 0;
    private static final Integer FOOTPRINT_LAYER_ID = 1;
    private static final Integer PEOPLE_LAYER_ID = 2;
    final static int FOOTPRINT_MESSAGE = 1;
    final static int CURRENTLOCATION_MESSAGE = 2;

    Handler mFootprintHandler = new Handler() {

        MapObject me;

        private void addMe(MapObjectModel objectModel, int x, int y,  Layer layer, Drawable drawable) {
            pinHeight = drawable.getIntrinsicHeight();
            me = new MapObject(objectModel ,drawable,new Point(x, y),
                    PivotFactory.createPivotPoint(drawable, PivotFactory.PivotPosition.PIVOT_CENTER),true, false);
            layer.addMapObject(me);
        }

        @Override
        public void handleMessage(Message msg) {

            if(msg.what == FOOTPRINT_MESSAGE) {

                int i = msg.arg1;
                if(i == mFootprints.size()) {
                    footprintLayer.clearAll();
                }else if(i == 0){
                    MapObjectModel m = mFootprints.get(i);
                    map.scrollMapTo(m.getX(), m.getY());
                    addMe(m,m.getX(),m.getY(),footprintLayer,m.getPic());

                }else{
                    //map.postInvalidate();
                    MapObjectModel m = mFootprints.get(i);
                    map.scrollMapTo(m.getX(), m.getY());

                    me.moveTo(m.getX(),m.getY());
//                    addNotScalableMapObject(m,footprintLayer);

                }
            }
            super.handleMessage(msg);
        }
    };

    Handler mCurrentLocationHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == CURRENTLOCATION_MESSAGE) {

                for(int i = mCurrentLocationArray.length() - 1; i > -1; i--){
                    try {
                        JSONObject locationRecord = mCurrentLocationArray.getJSONObject(i);

                        displayLocationRecord(locationRecord);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            super.handleMessage(msg);
        }

        private void displayLocationRecord(JSONObject record) {
            try {
                int id = record.getInt("id");
                String name = record.getString("name");
                PersonalInfo.People people = PersonalInfo.peopleMap.get(id);
                if(people == null){
                    people = new PersonalInfo.People(id, name,getResources().getDrawable(R.drawable.avatar_small));
                    PersonalInfo.peopleMap.put(id, people);
                }
                JSONObject locationRecord = record.getJSONObject("current_location");
                if(locationRecord == null) return;
                people.currentLocation.setLatitude(locationRecord.getDouble("latitude"));
                people.currentLocation.setLongitude(locationRecord.getDouble("longitude"));

                if(people.mapObject == null){
                    people.mapObject = addNotScalableMapObject(new MapObjectModel(people.currentLocation,people.name,people.avatar),peopleLayer);
                }

                int[] xy = transformLocationToXY(people.currentLocation);
                people.mapObject.moveTo(xy[0]/2,xy[1]/2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private View selfView;
    private static final int MAP_ID = 23;
    private int pinHeight;
    private MapObjectContainer mScenery;
    private MapWidget map;
    private Popup mapObjectInfoPopup;
    private int currentPoint;
    private int maxLevel=14;
    private int minLevel=13;
    private Layer sceneryLayer;
    private Layer footprintLayer;
    private Layer peopleLayer;
    //足迹
    public static ArrayList<MapObjectModel> mFootprints;
    private LocationManager mLocationManager;
    private MyLocationListner mGPSListener;
    private MyLocationListner mNetworkListner;
    private CurrentLocationThread mCurrentLocationThread;
    private JSONArray mCurrentLocationArray;
    private FootprintDisplayThread mFootprintDisplayThread;

    //Time
    public static ArrayList<String> time = new ArrayList<String>();

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

        initMap(savedInstanceState);
        initModel();
        initLayer();
        initMapObjects();
        initMapListeners();
        map.setShowMyPosition(false);
        // map.centerMap();

        registerButton();

        return selfView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String news_text = data.getExtras().getString("text");
        String news_img_url = data.getExtras().getString("img");

        Bitmap news_img = BitmapFactory.decodeFile(news_img_url);

        Map<String,Object> body = new HashMap<String, Object>();
        body.put("id",PersonalInfo.me.id);
        body.put("post_time",new Date().getTime());
        body.put("action_type", NewsCard.NewsInfo.ACTION_PIC);
        body.put("location","");
        body.put("msg",news_text);

        Map<String,File> files = new HashMap();
        files.put("pic",new File(news_img_url));
        WebTask task = new WebPostFileTask(WebTask.SERVER_ADDRESS,"/i/news/post/pic",null,body,files){
            @Override
            protected void onPostExecute(Object o) {

                String string = (String) o;
                Log.i("web /i/news/post/pic", string);
            }
        };
        task.execute();
        // PersonalInfo.addNewsInfo(news_text, news_img);
    }

    private void registerButton(){
        BootstrapButton footprintButton = (BootstrapButton) selfView.findViewById(R.id.footprint);
        footprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFootprintDisplayThread != null )
                    if(mFootprintDisplayThread.isAlive())
                        return;
                (mFootprintDisplayThread = new FootprintDisplayThread()).start();
            }
        });

        BootstrapButton postPicButton = (BootstrapButton) selfView.findViewById(R.id.post_pic);
        postPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), StatusActivity.class);
                startActivityForResult(in, 0);
                // PersonalInfo.addNewsInfo("hehe", BitmapFactory.decodeResource(getResources(), R.drawable.news_pic_1));
            }
        });

        BootstrapButton selfLocationButton = (BootstrapButton) selfView.findViewById(R.id.map_self_location);
        selfLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] xy = transformLocationToXY(PersonalInfo.me.currentLocation);
                map.scrollMapTo(xy[0]/2,xy[1]/2);
            }
        });

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

//        mAMapLocManager = LocationManagerProxy.getInstance(getActivity());
//
//        mAMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, this);
//
//        handler.postDelayed(this, 12000);

        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (isGPSEnabled()) {
            mGPSListener=new MyLocationListner();

            //五个参数分别为位置服务的提供者，最短通知时间间隔，最小位置变化，listener，listener所在消息队列的looper  
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mGPSListener);
        }
        else {
            mNetworkListner=new MyLocationListner();

            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, mNetworkListner);
        }

    }
    public boolean isGPSEnabled() {
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i(Thread.currentThread().getName(), "isGPSEnabled");
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentLocationThread = new CurrentLocationThread();
        mCurrentLocationThread.start();
    }

    @Override
    public void onPause() {
        stopLocation();
        mCurrentLocationThread.interrupt();// invalid interrupt
        mCurrentLocationThread = null;

        super.onPause();
    }

    private void stopLocation() {
//        if (mAMapLocManager != null) {
//            mAMapLocManager.removeUpdates(this);
//            mAMapLocManager.destory();
//        }
//        mAMapLocManager = null;
    if(mGPSListener!=null){
        mLocationManager.removeUpdates(mGPSListener);
        mGPSListener=null;
    }
    if(mNetworkListner!=null){
        mLocationManager.removeUpdates(mNetworkListner);
        mNetworkListner=null;
    }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.saveState(outState);
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

        map = new MapWidget(savedInstanceState, getActivity(), "map", 14);

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

        Location location = new Location("locationtest");
        location.setLatitude(31.89256174);
        location.setLongitude(118.8113451);

        int[] xy = transformLocationToXY(location);

        objectModel = new MapObjectModel(xy[0]/2,xy[1]/2, "location3", getResources().getDrawable(R.drawable.map_object));
        mScenery.addObject(objectModel);

        mFootprints = new ArrayList<MapObjectModel>(30);
    }


    private void initMapObjects()
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.map_point_detail, null);

        mapObjectInfoPopup = new Popup(getActivity(),view, (FrameLayout)selfView.findViewById(R.id.rootLayout),map);

        for (int i=0; i< mScenery.size(); ++i) {
            addNotScalableMapObject(mScenery.getObject(i), sceneryLayer);
        }


        Iterator iter = PersonalInfo.peopleMap.entrySet().iterator();
        boolean isFirst = true;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            PersonalInfo.People people = (PersonalInfo.People) entry.getValue();
            people.mapObject = addNotScalableMapObject(new MapObjectModel(people.currentLocation,people.name,people.avatar), peopleLayer);

        }


    }


    public MapObject addNotScalableMapObject(MapObjectModel objectModel,  Layer layer){

        return addNotScalableMapObject(objectModel, objectModel.getX(), objectModel.getY(), layer, objectModel.getPic());

    }
    private MapObject addNotScalableMapObject(MapObjectModel objectModel, int x, int y,  Layer layer, Drawable drawable) {
        pinHeight = drawable.getIntrinsicHeight();
        MapObject object1 = new MapObject(objectModel ,drawable,new Point(x, y),
                PivotFactory.createPivotPoint(drawable, PivotFactory.PivotPosition.PIVOT_CENTER),true, false);
        layer.addMapObject(object1);
        return object1;
    }


    private int[] transformLocationToXY(Location location){
        int[] xy = new int[2];

        int x1 = 174;
        int y1 = 248;
        int x2 = 1763;
        int y2 = 1779;

        double lat1 = 31.89256174;
        double lon1 = 118.8113451;
        double lat2 = 31.88285992;
        double lon2 = 118.8246489;

        xy[0] = (int) ( (x2 - x1) / (lon2 - lon1) * (location.getLongitude()  - lon1) + x1);
        xy[1] = (int) ( (y2 - y1) / (lat2 - lat1) * (location.getLatitude()   - lat1) + y1);

        return xy;
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
     * @return X coordinate in screen coordinates. You can use this value to mFootprintDisplayThread any object on the screen.
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

            for(int i = 0; i <= mFootprints.size() ; i++){ // white
                SystemClock.sleep(1000);
                Message msg = mFootprintHandler.obtainMessage();
                msg.what = FOOTPRINT_MESSAGE;
                msg.arg1 = i;
                msg.sendToTarget();

            }
        }
    }

    private class MyLocationListner implements LocationListener {

        Location lastLocation = new Location("test");

        boolean isLocationEqual(Location l1, Location l2){
            return l1.getLatitude() == l2.getLatitude() && l1.getLongitude() == l2.getLongitude();
        }

        @Override
        public void onLocationChanged(Location location) {
            // 当LocationManager检测到最小位置变化时，就会回调到这里
            Log.i("location", "Got New Location of provider:"+location.getProvider());
            Log.i("location", "New Location:"+location.toString());

            if(isLocationEqual(lastLocation,location)) return;

            lastLocation = location;

            int[] xy = transformLocationToXY(location);
            MapObjectModel objectModel = new MapObjectModel(xy[0] / 2, xy[1] / 2, "footprint", PersonalInfo.me.avatar);
            objectModel.setLocation(lastLocation);
            mFootprints.add(objectModel); // white
            //Time
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            time.add(date);

            if(mFootprintDisplayThread != null )
                if(mFootprintDisplayThread.isAlive())
                    PersonalInfo.me.mapObject.moveTo(xy[0] / 2, xy[1] / 2);

            startUpdateLocationTask(location);

        }

        private void startUpdateLocationTask(Location location){

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",PersonalInfo.me.id);
            map.put("latitude",location.getLatitude());
            map.put("longitude",location.getLongitude());
            map.put("timestamp",(new Date()).getTime());
            // Log.i("web send", map.toString());
            WebTask task = new WebPostTextTask(WebTask.SERVER_ADDRESS, "/i/locations/update",null,map) {
                @Override
                protected void onPostExecute(Object o) {
                    String res = (String) o;
                    Log.i("web", res);
                }
            };
            task.execute();
        }

        //后3个方法此处不做处理
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

    };

    class CurrentLocationThread extends Thread{
        @Override
        public void run() {
            while (true){
                WebTask task = new WebPostTextTask(WebTask.SERVER_ADDRESS,"/i/locations/current",null,null){
                    @Override
                    protected void onPostExecute(Object o) {
                        if(mFootprintDisplayThread != null )
                            if(mFootprintDisplayThread.isAlive())
                                return;

                        if(o == null) {
                            Log.e("web", "result error");
                            return;
                        }

                        String data = (String)o;
                        Log.i("web task",data);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if(jsonObject.getString("type").equals("ok")){
                                mCurrentLocationArray = jsonObject.getJSONArray("current_locations_list");
                                Message msg = mCurrentLocationHandler.obtainMessage();
                                msg.what = CURRENTLOCATION_MESSAGE;
                                msg.sendToTarget();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("json", e.toString());

                        }
                    }
                };
                task.execute();
                SystemClock.sleep(2*1000);
            }
        }
    }
}

