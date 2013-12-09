package com.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.security.R;
import com.security.domain.UpdateInfo;
import com.security.engine.UpdateInfoService;

public class SplashActivity extends Activity
{
	private TextView tv_version;
	private LinearLayout ll;
	
	private UpdateInfo info;
	private String version;
	
	public static final String TAG =  "SplashActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
		version = getVersion();
		tv_version.setText("版本号  " + version);
		
		ll = (LinearLayout) findViewById(R.id.ll_splash_main);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(2000);
		ll.startAnimation(alphaAnimation);
		
	//	if(isNeedUpdate(version))  
    //    {  
   //         showUpdateDialog();  
    //    }
		new Thread()  
		{  
		    public void run()   
		    {  
		        try  
		        {  
		            UpdateInfoService updateInfoService = new UpdateInfoService(SplashActivity.this);  
		            info = updateInfoService.getUpdateInfo(R.string.serverUrl);
		            Log.d(TAG, "info:"+info.toString());
		        }  
		        catch (Exception e)  
		        {  
		            e.printStackTrace();  
		        }  
		    };  
		}.start(); 
		
		if(isNeedUpdate(version))  
	        {  
	            showUpdateDialog();  
	        }
	}
	
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("升级提醒");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage(info.getDescription());
		builder.setCancelable(false); 
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()  
        {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                // TODO Auto-generated method stub  
                  
            }  
        });  
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()  
        {  
  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                // TODO Auto-generated method stub  
                  
            }  
              
        });  
        builder.create().show(); 
	}
	
	private boolean isNeedUpdate(String version) {
		 Log.d(TAG, "isNeedUpdate:");
		if(info == null)  
	    {  
	        Toast.makeText(this, "获取更新信息异常，请稍后再试", Toast.LENGTH_SHORT).show();  
	        //loadMainUI();  
	        return false;  
	    }  
	    String v = info.getVersion();  
	    if(v.equals(version))  
	    {  
	        Log.i(TAG, "当前版本：" + version);  
	        Log.i(TAG, "最新版本：" + v);  
	        //loadMainUI();  
	        return false;  
	    }  
	    else  
	    {  
	        Log.i(TAG, "需要更新");  
	        return true;  
	    }  
	}
	
	
	private String getVersion()
	{
		try
		{
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			
			return packageInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return "版本号未知";
		}
	}

}
