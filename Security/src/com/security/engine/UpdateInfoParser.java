package com.security.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.security.domain.UpdateInfo;
import com.security.ui.SplashActivity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
public class UpdateInfoParser {
	public static UpdateInfo getUpdateInfo(InputStream is) throws Exception
	{	
		Log.d(SplashActivity.TAG, "UpdateInfo");
		UpdateInfo info = new UpdateInfo();
		XmlPullParser xmlPullParser = Xml.newPullParser();
		xmlPullParser.setInput(is, "utf-8");
		int type = xmlPullParser.getEventType();
		while(type != XmlPullParser.END_DOCUMENT)
		{
			switch(type)
			{
				case XmlPullParser.START_TAG :
					if(xmlPullParser.getName().equals("version"))
					{
						info.setVersion(xmlPullParser.nextText());
					}
					else if(xmlPullParser.getName().equals("description"))
					{
						info.setDescription(xmlPullParser.nextText());
					}
					else if(xmlPullParser.getName().equals("apkurl"))
					{
						info.setUrl(xmlPullParser.nextText());
					}
					break;
					
				default : 
					break;
			}
			type = xmlPullParser.next();
		}
		return info;
	}
}
