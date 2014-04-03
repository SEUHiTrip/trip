package com.seu.hitrip.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Objects;

import android.R.integer;
import android.R.string;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpStatus;

public class WebGetFileTask extends WebTask {

	public WebGetFileTask(String ip, String handler, Map<String, Object> query) {
		super(ip, handler, query);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void beforeConnect(HttpURLConnection urlConn, Integer[] arg0) {
		Log.i("web", "TestPic5");
	}

	@Override
	protected Object afterConnect(HttpURLConnection urlConn, Integer[] arg0) {
		File file = null;
		InputStream inputStream = null;
		try {
            int status = urlConn.getResponseCode();

            InputStream ins = null;
            if(status >= HttpStatus.SC_BAD_REQUEST)
                ins = urlConn.getErrorStream();
            else
                ins = urlConn.getInputStream();
			inputStream = ins;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			switch (arg0[0]) {
			case PIC_BITMAP:
				return BitmapFactory.decodeStream(inputStream);
			case FILE_URI:
			default:
				return writeToExternalStorage(inputStream,"hitrip/pics/",getFilename(urlConn.getURL().getFile()));
			}
			
		} catch (Exception e) {
			Log.e("test", e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}

	private static String getFilename(String fileString) {
		int i = fileString.lastIndexOf('/') + 1;
		return String.format("%1$04d_", System.currentTimeMillis()%10000) + fileString.substring(i);
	}
	
	private File writeToExternalStorage(InputStream inputStream, String dirName, String fileName) {
		
		Log.e("web", "writeToExternalStorage");
		//获取SD卡的路径
		String SDPath = Environment.getExternalStorageDirectory() + "/";
		//创建要使用的文件夹
		File dir = new File(SDPath + dirName);
		if(!dir.exists())
			dir.mkdirs();
		//创建输出文件
		File file = new File(SDPath + dirName + fileName);
		Log.e("web", file.getAbsolutePath());

		try {
			if(!file.exists())
				file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);
			byte[] bs = new byte[1024];
			int len = 0;

			while ((len = inputStream.read(bs)) > 0) {
				output.write(bs, 0, len);
			}
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
}
