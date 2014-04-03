package com.seu.hitrip.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seu.hitrip.R;
import com.seu.hitrip.fragment.FragmentMyfootprint;

/**
 * Created by WhiteT on 4/3/2014.
 */
public class ActivityLocation extends Activity {

    private TextView lati;
    private TextView longti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location);
        initView();
    }

    public void initView() {
        lati = (TextView) findViewById(R.id.location_latitude_text);
        longti = (TextView) findViewById(R.id.location_longitude_text);
        lati.setText(FragmentMyfootprint.lati);
        longti.setText(FragmentMyfootprint.longti);
    }
}
