package com.security.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.security.ui.SplashActivity;

import android.app.ProgressDialog;
import android.util.Log;

public class DownloadTask {
	
	public static File getFile(String path, String filePath, ProgressDialog progressDialog) 
		throws Exception {
		URL url = new URL(path);  
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();  
        httpURLConnection.setConnectTimeout(2000);  
        httpURLConnection.setRequestMethod("GET");
        Log.d(SplashActivity.TAG, "process:getFile");
        Log.d(SplashActivity.TAG, "httpURLConnection.getResponseCode()"+httpURLConnection.getResponseCode());
        if(httpURLConnection.getResponseCode() == 200)  
        {  
            int total = httpURLConnection.getContentLength();  
            progressDialog.setMax(total);  
              
            InputStream is = httpURLConnection.getInputStream(); 
            Log.d(SplashActivity.TAG, "process is InputStream ");
            File file = new File(filePath);  
            Log.d(SplashActivity.TAG, "process is File ");
            FileOutputStream fos = new FileOutputStream(file);
            Log.d(SplashActivity.TAG, "process is FileOutputStream ");
            byte[] buffer = new byte[1024];  
            int len;  
            int process = 0;  
            Log.d(SplashActivity.TAG, "process is 0 ");
            while((len = is.read(buffer)) != -1)  
            {  
                fos.write(buffer, 0, len);  
                process += len;  
                progressDialog.setProgress(process); 
            }  
            fos.flush();  
            fos.close();  
            is.close();  
            return file;  
        }  
        return null;  
	}
}
