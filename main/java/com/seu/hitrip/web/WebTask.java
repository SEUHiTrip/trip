package com.seu.hitrip.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import android.R.integer;
import android.R.string;
import android.os.AsyncTask;
import android.util.Log;

public abstract class WebTask extends AsyncTask<Integer, Integer, Object>{
    public static String SERVER_ADDRESS = "192.168.1.132";
	public static final int PORT = 3000;
	
	public static final int FILE_URI = 1;
	public static final int PIC_BITMAP = 2;
	
	protected static String cookie = null;
	
	protected String ip = null;
	protected String handler = null;
	protected Map<String, Object> query = null;
	
	public WebTask(String ip, String handler, Map<String, Object> query){
		this.ip = ip;
		this.handler = handler;
		this.query = query;
	}
	
	protected abstract void beforeConnect(HttpURLConnection urlConn, Integer[] arg0);
	protected abstract Object afterConnect(HttpURLConnection urlConn, Integer[] arg0);

	@Override
	protected Object doInBackground(Integer... arg0) {
		if( ip == null || handler == null )
			throw new IllegalArgumentException("should have ip and handler");
		
		URL url = null;
		try {
			url = new URL(buildUrlString(ip, handler, query));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (url != null)  {  
            try  {
            	
            	Log.i("web", url.toString());
            	
                // 使用HttpURLConnection打开连接  
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();  
                
                
                if(getCookie() != null)
                	urlConn.setRequestProperty("Cookie", getCookie());

                beforeConnect(urlConn,arg0);
                ///////////////////
                urlConn.connect();
                ///////////////////
                
                String cookieString = urlConn.getHeaderField("set-cookie");                
                
                if(cookieString != null)
                	setCookie(cookieString);

                ///////////////////
                Object result = afterConnect(urlConn, arg0);
                return result;
            }  
            catch (IOException e)  {  
                Log.e("web", "IOException");
                Log.e("web", e.toString());
            }
        }  
        else  {  
            Log.e("web", "Url NULL");  
        }  
		return null;
	}
	
	public static String stringfyMap(Map<String, Object> query) {
		StringBuilder sBuilder = new StringBuilder();
        if(query == null) return sBuilder.toString();
		Iterator iter = query.entrySet().iterator();
		boolean isFirst = true;
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    Object val = entry.getValue();
		    if(isFirst){
		    	isFirst = false;
		    }else {
		    	sBuilder.append("&");
			}
		    sBuilder.append(key).append("=").append(val);
		}
		return sBuilder.toString();
	}
	
	public static String buildUrlString(String ip, String handler, Map<String, Object> query) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("http://").append(ip).append(":").append(PORT)
				.append(handler);
		if (query != null) {
			sBuilder.append("?");
			sBuilder.append(stringfyMap(query));
		}
		return sBuilder.toString(); 
	}
	
	public static void setCookie(String cookie) {
		WebTask.cookie = cookie;
	}

	public static String getCookie() {
		return cookie;
	}
}
