package com.seu.hitrip.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.seu.hitrip.cose.R;
import com.seu.hitrip.util.HiTrip;

/**
 * Created by WhiteT on 3/22/14.
 */
public class FragmentCommonMap extends BaseFragment {

/*
    public int getTitleResourceId() {
        return R.string.fragment_common_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_commonmap, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";
        WebView mWebView = (WebView) getActivity().findViewById(R.id.common_map_web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(URL);
    }
*/

    static MapView mMapView = null;
    private MapController mMapController = null;

    private LocationClient mLocClient;
    public MyLocationListener myListener;
    MyLocationOverlay myLocationOverlay = null;
    LocationData locData = null;

    long startTime;
    long costTime;

    public int getTitleResourceId() {
        return R.string.fragment_common_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        View v = inflater.inflate(R.layout.fragment_commonmap, container, false);
        mMapView = (MapView) v.findViewById(R.id.baidu_map_view);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
        mLocClient.requestLocation();
        myLocationOverlay = new MyLocationOverlay(mMapView);
        locData = new LocationData();
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        mMapView.refresh();
        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HiTrip app = (HiTrip) getActivity().getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getActivity());
            app.mBMapManager.init(HiTrip.strKey, new HiTrip.MyGeneralListener());
        }
        mLocClient = new LocationClient(getActivity());
        myListener = new MyLocationListener();
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");
        option.setPoiExtraInfo(true);
        option.setPoiDistance(500);
        option.setOpenGps(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);     //设置发起定位请求的间隔时间，单位毫秒

        mLocClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            costTime = System.currentTimeMillis() - startTime;
            Log.d("MapFragment", "" + costTime);
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            mMapView.refresh();
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
            // if request location success , stop it
            stopRequestLocation();
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }
    //核心方法，避免因Fragment跳转导致地图崩溃
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            // if this view is visible to user, start to request user location
            startRequestLocation();
        } else if (isVisibleToUser == false) {
            // if this view is not visible to user, stop to request user
            // location
            stopRequestLocation();
        }
    }

    private void stopRequestLocation() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
        }
    }

    private void startRequestLocation() {
        // this nullpoint check is necessary
        if (mLocClient != null) {
            mLocClient.registerLocationListener(myListener);
            mLocClient.start();
            mLocClient.requestLocation();
            startTime = System.currentTimeMillis();
            Toast.makeText(HiTrip.getInstance().getApplicationContext(),
                    "开始获取位置", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


}
