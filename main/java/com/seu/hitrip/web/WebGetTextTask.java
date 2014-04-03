package com.seu.hitrip.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import android.R.integer;
import android.util.Log;

import org.apache.http.HttpStatus;

public abstract class WebGetTextTask extends WebTask {

	public WebGetTextTask(String ip, String handler, Map<String, Object> query) {
		super(ip, handler, query);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void beforeConnect(HttpURLConnection urlConn, Integer[] args) {
        try {
			urlConn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected Object afterConnect(HttpURLConnection urlConn, Integer args[]) {
		String resultData = "";
		//得到读取的内容(流)  
		try {
            int status = urlConn.getResponseCode();

            InputStream ins = null;
            if(status >= HttpStatus.SC_BAD_REQUEST)
                ins = urlConn.getErrorStream();
            else
                ins = urlConn.getInputStream();
	        InputStreamReader in = new InputStreamReader(ins);
	        // 为输出创建BufferedReader  
	        BufferedReader buffer = new BufferedReader(in);  
	        String inputLine = null;  
	        //使用循环来读取获得的数据  
	        while (((inputLine = buffer.readLine()) != null))  {  
	            resultData += inputLine;  
	        }           
	        //关闭InputStreamReader  
	        in.close();  
	        
	        urlConn.disconnect();  
	        if ( resultData != null )  {
	        	Log.i("web", resultData);
	            return resultData;
	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
            Log.e("WebGetTextTask",e.toString());
			return null;
		}
        Log.e("WebGetTextTask","return null");
		return null;
	}
}
