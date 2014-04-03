package com.seu.hitrip.fragment;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.seu.hitrip.R;
import com.seu.hitrip.util.HiTrip;

/**
 * Created by WhiteT on 3/22/14.
 */
public class FragmentCommonMap extends BaseFragment implements LocationListener{

    //private static final String MAP_URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";
    private static final String MAP_URL = "https://www.google.com.hk/maps/@31.8835448,118.8187479,21z";
    private Location mostRecentLocation;
    public WebView webView;

    public int getTitleResourceId() {
        return R.string.fragment_common_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_commonmap, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLocation();
        setupWebView();
    }

    private void setupWebView() {
        final String centerURL = "javascript:centerAt(" +
                mostRecentLocation.getLatitude() + "," +
                mostRecentLocation.getLongitude()+ ")";
        webView = (WebView) getActivity().findViewById(R.id.common_map_web);
        webView.getSettings().setJavaScriptEnabled(true);
        //Wait for the page to load then send the location information
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(MAP_URL);
    }

    private void getLocation() {
        LocationManager locationManager =
                (LocationManager)getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria,true);
        //In order to make sure the device is getting the location, request updates.
        locationManager.requestLocationUpdates(provider, 1, 0, this);
        mostRecentLocation = locationManager.getLastKnownLocation(provider);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
