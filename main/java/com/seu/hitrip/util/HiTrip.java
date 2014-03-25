package com.seu.hitrip.util;


import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class HiTrip extends Application {
	
    private static HiTrip mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;

    public static final String strKey = "BA5EED33E98EB753892FF80B63B59E1972364E5C";
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}
	
	public void initEngineManager(Context context) {
//        if (mBMapManager == null) {
//            mBMapManager = new BMapManager(context);
//        }
//
//        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
//            Toast.makeText(HiTrip.getInstance().getApplicationContext(),
//                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
//        }
	}
	
	public static HiTrip getInstance() {
		return mInstance;
	}
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(HiTrip.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(HiTrip.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(HiTrip.getInstance().getApplicationContext(),
                        "请输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                HiTrip.getInstance().m_bKeyRight = false;
            }
            else{
                HiTrip.getInstance().m_bKeyRight = true;
            	Toast.makeText(HiTrip.getInstance().getApplicationContext(),
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }
}