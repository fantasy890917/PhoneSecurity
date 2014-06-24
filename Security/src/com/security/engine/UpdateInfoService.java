package com.security.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.security.domain.UpdateInfo;
import com.security.ui.SplashActivity;

public class UpdateInfoService {
	private Context context;
	
	public UpdateInfoService(Context context){
		this.context = context;
	}
	
	public UpdateInfo getUpdateInfo(int urlId) throws Exception {
		String path =  context.getResources().getString(urlId);
		Log.d(SplashActivity.TAG, "path:"+path);
		/*
		URL url=new URL(path);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(5000);
		httpURLConnection.setRequestMethod("GET");
		InputStream is = httpURLConnection.getInputStream();
		*/
		InputStream in = null ;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(path);
        HttpResponse response = client.execute(request);
        in = response.getEntity().getContent();
		return UpdateInfoParser.getUpdateInfo(in);
	}

}
