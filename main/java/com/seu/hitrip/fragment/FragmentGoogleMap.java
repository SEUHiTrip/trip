package com.seu.hitrip.fragment;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.seu.hitrip.R;

/**
 * Created by WhiteT on 4/1/2014.
 */
public class FragmentGoogleMap extends BaseFragment implements LocationListener {

    private GoogleMap gMap;
    private LocationManager locationmanager;

    public int getTitleResourceId() {
        return R.string.fragment_common_map;
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        View v = inflater.inflate(R.layout.fragment_googlemap, container, false);
        GoogleMap gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.googlemap)).getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        gMap.setTrafficEnabled(true);
        gMap.setMyLocationEnabled(true);
        LatLng latlng = new LatLng(31.8831151, 118.8202376);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        locationmanager = (LocationManager) getActivity().getSystemService(getActivity().getApplicationContext().LOCATION_SERVICE);
        return v;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null) {
            Toast.makeText(getActivity(), "暂无定位", 1000);
        } else {
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
}
