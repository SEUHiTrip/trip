package com.seu.hitrip.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import android.R.integer;
import android.R.interpolator;
import android.util.Log;

public abstract class WebPostTextTask extends WebTask {

	protected Map<String, String> body = null;
	
	
	public WebPostTextTask(String ip, String handler, 
			Map<String, String> query, Map<String, String> body) {
		super(ip, handler, query);
		this.body = body;
	}

	@Override
	protected void beforeConnect(HttpURLConnection urlConn, Integer[] args) {
        try {
        	urlConn.setDoOutput(true);  
            urlConn.setDoInput(true);  
            // 设置以POST方式  
            urlConn.setRequestMethod("POST"); 
            
			//DataOutputStream流  
	        DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());  
	        //要上传的参数  
	        String content = stringfyMap(body);
	        //将要上传的内容写入流中  
	        out.writeBytes(content);   
	        //刷新、关闭  
	        out.flush();  
	        out.close();   
	        //获取数据  
	        
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
            Log.e("web", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
            Log.e("web", e.toString());
			e.printStackTrace();

		}
	}
	
	@Override
	protected Object afterConnect(HttpURLConnection urlConn, Integer[] args) {
		String resultData = "";
		
		try {
	        //得到读取的内容(流)  
	        InputStreamReader in = new InputStreamReader(urlConn.getInputStream());  
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
            Log.e("web", "WebPostTextTask " + e.toString());
		}
        return null;
    }

}
