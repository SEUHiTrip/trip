package com.seu.hitrip.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.hitrip.R;
import com.seu.hitrip.map.MapObjectModel;
import com.seu.hitrip.util.ActivityLocation;

/**
 * Created by WhiteT on 4/3/2014.
 */
public class FragmentMyfootprint extends BaseFragment {

    private View selfView;
    private ListView listview;
    public static String lati;
    public static String longti;


    @Override
    public int getTitleResourceId() {
        return R.string.fragment_my_footprint;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selfView = inflater.inflate(R.layout.frament_footprint, container, false);
        listview = (ListView) selfView.findViewById(R.id.footprint_listview);

        if(FragmentSceneryMap.time.size() > 0) {
            Toast.makeText(getActivity(), "获取足迹成功", 3000).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_footprint,
                    R.id.ft_item, FragmentSceneryMap.time);
            listview.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "当前无足迹信息", 3000).show();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                View view = listview.getChildAt(arg2);
                TextView textview = (TextView) view.findViewById(R.id.ft_item);
                String ft_item = textview.getText().toString();
                for(int i=0; i<FragmentSceneryMap.time.size(); i++) {
                    if (ft_item.equals(FragmentSceneryMap.time.get(i))) {
                        MapObjectModel m = FragmentSceneryMap.mFootprints.get(i);
                        Location l = m.getLocation();
                        lati = Double.toString(l.getLatitude());
                        longti = Double.toString(l.getLongitude());
                        Intent intent=new Intent();
                        intent.setClass(getActivity(), ActivityLocation.class);
                        getActivity().startActivity(intent);
                    }
                }
            }

        });
        return selfView;
    }

}
