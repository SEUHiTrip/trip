package com.seu.hitrip.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.xml.transform.OutputKeys;

import android.R.string;
import android.util.Log;

import org.apache.http.HttpStatus;

public class WebPostFileTask extends WebPostTextTask {
	
	protected Map<String,File> files = null;
	
	protected String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	protected String PREFIX = "--", LINE_END = "\r\n";
	protected String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	
	public WebPostFileTask(String ip, String handler,
			Map<String, Object> query, Map<String, Object> body, Map<String,File> files) {
		super(ip, handler, query, body);
		this.files = files;
	}

	@Override
	protected void beforeConnect(HttpURLConnection urlConn, Integer[] arg0) {
    	urlConn.setDoOutput(true);  
        urlConn.setDoInput(true);  
        // 设置以POST方式  
        try {
			urlConn.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		urlConn.setRequestProperty("Charset", "UTF-8"); // 设置编码
		urlConn.setRequestProperty("connection", "keep-alive");
		urlConn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);
		
		if( files == null ) throw new IllegalArgumentException("file is null");
		OutputStream oStream;
		try {
			oStream = urlConn.getOutputStream();
			
			DataOutputStream dos = new DataOutputStream(oStream);

			if(body != null){
				Iterator iter = body.entrySet().iterator();
				while (iter.hasNext()) { 
				    Map.Entry entry = (Map.Entry) iter.next(); 
				    Object key = entry.getKey(); 
				    Object val = entry.getValue();
					StringBuilder sBuilder = new StringBuilder();
		            sBuilder.append(PREFIX).append(BOUNDARY).append(LINE_END);

		            sBuilder.append("Content-Disposition: form-data; name=\"" + key + "\"; "
		                    + LINE_END);
		            sBuilder.append(LINE_END);
		            dos.write(sBuilder.toString().getBytes());
		            
		            dos.write((val.toString()).getBytes());
		            
		            dos.write(LINE_END.getBytes());
				}
			}
			
			if(files != null){
				byte[] bytes = new byte[1024];
                Iterator iter = files.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    File val = (File) entry.getValue();

                    StringBuffer sb = new StringBuffer();
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);

                    sb.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                            + val.getName() + "\"" + LINE_END);
                    sb.append("Content-Type: application/octet-stream; charset=UTF-8"
                            + LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());

                    InputStream is = new FileInputStream(val);

                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        dos.write(bytes, 0, len);
                    }
                    is.close();

                    dos.write(LINE_END.getBytes());
                }
			}

			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
	                .getBytes();
	        
	        dos.write(end_data);
	        dos.flush();
	        dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    @Override
    protected Object afterConnect(HttpURLConnection urlConn, Integer[] args) {
        String resultData = "";

        try {
            int status = urlConn.getResponseCode();

            InputStream ins = null;
            if(status >= HttpStatus.SC_BAD_REQUEST)
                ins = urlConn.getErrorStream();
            else
                ins = urlConn.getInputStream();

            //得到读取的内容(流)
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
            Log.e("web", "WebPostFileTask " + e.toString());
        }
        return null;
    }

}
