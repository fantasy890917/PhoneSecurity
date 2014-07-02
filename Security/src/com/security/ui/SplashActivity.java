package com.security.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.security.R;
import com.security.domain.UpdateInfo;
import com.security.engine.DownloadTask;
import com.security.engine.UpdateInfoService;

public class SplashActivity extends Activity
{
	private TextView tv_version;
	private LinearLayout ll;
	
	private UpdateInfo info;
	private String version;
	private ProgressDialog progressDialog;
	
	public static final String TAG =  "Security";
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(isNeedUpdate(version))  
	        {  
	            showUpdateDialog();  
	        }
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
		version = getVersion();
		tv_version.setText("version  " + version);
		
		ll = (LinearLayout) findViewById(R.id.ll_splash_main);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(2000);
		ll.startAnimation(alphaAnimation);
		
		progressDialog = new ProgressDialog(this);  
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
        progressDialog.setMessage(getResources().getText(R.string.upgrade_dialog_downloading));
        
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
		
		//handdler
		new Thread()
		{
			public void run()
			{
				try
				{
					sleep(3000);
					handler.sendEmptyMessage(0);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			};
		}.start();
		
	}
	
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.upgrade_dialog_title);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage(info.getDescription());
		builder.setCancelable(false); 
		
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()  
        {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                // TODO Auto-generated method stub  
            	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))  
                {  
                    File dir = new File(Environment.getExternalStorageDirectory(), "/security/update");  
                    if(!dir.exists())  
                    {  
                        dir.mkdirs();  
                    }  
                    Log.d(TAG,"dirPath: "+dir.getPath());
                    String apkPath = Environment.getExternalStorageDirectory() + "/security/update/new.apk";  
                    UpdateTask task = new UpdateTask(info.getUrl(), apkPath);  
                    progressDialog.show();  
                    new Thread(task).start();  
                }  
                else  
                {  
                    Toast.makeText(SplashActivity.this, R.string.upgrade_no_sdcard, Toast.LENGTH_SHORT).show();  
                    loadMainUI();  
                }    
            }  
        });  
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()  
        {  
  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                // TODO Auto-generated method stub  
            	loadMainUI();  
            }  
              
        });  
        builder.create().show(); 
	}
	
	private boolean isNeedUpdate(String version) {
		if(info == null)  
	    {  
	        Toast.makeText(this, R.string.upgrade_exception, Toast.LENGTH_SHORT).show();  
	        loadMainUI();  
	        return false;  
	    }  
	    String v = info.getVersion();  
	    if(v.equals(version))  
	    {  
	        Log.i(TAG, "current version：" + version);  
	        Log.i(TAG, "last version：" + v);  
	        loadMainUI();  
	        return false;  
	    }  
	    else  
	    {  
	        Log.i(TAG, "need update ");  
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
	
	private void loadMainUI()  
    {  
        Intent intent = new Intent(this, MainActivity.class);  
        startActivity(intent);  
        finish();  
    }
	
	/** 
     * 安装apk 
     * @param file 要安装的apk的目录 
     */  
    private void install(File file)  
    {  
        Intent intent = new Intent();  
        intent.setAction(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
        finish();  
        startActivity(intent);  
    } 
    
	//===========================================================================================  
    
    /** 
     * download thread
     * 
     */ 
	class UpdateTask implements  Runnable{
		
		private String path;
		private String filePath;
		
		public UpdateTask(String path,String filePath){
			this.path=path;
			this.filePath=filePath;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				File file = DownloadTask.getFile(path, filePath, progressDialog);
				progressDialog.dismiss();
				install(file);
			}catch(Exception e){
				e.printStackTrace();  
                progressDialog.dismiss();  
                Toast.makeText(SplashActivity.this, "更新失败", Toast.LENGTH_SHORT).show();  
                loadMainUI(); 
				
			}
		}
		
	}
}
