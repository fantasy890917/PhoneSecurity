package com.security.service;

import com.security.engine.NumberAddressService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

public class PhoneAddressService extends Service{
	
	private TelephonyManager telephonyManager;
	private MyPhoneListener listener;
	private WindowManager windowManager;
	private TextView mTv_place;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		telephonyManager =(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//stop listener
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
	
	private class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
				case TelephonyManager.CALL_STATE_IDLE ://空闲状态
					if(mTv_place!=null){
						windowManager.removeView(mTv_place);//移除显示归属地的那个view
						mTv_place = null;
					}
					break;
					
				case TelephonyManager.CALL_STATE_OFFHOOK : //接通电话
					if(mTv_place != null)
					{
						windowManager.removeView(mTv_place);//移除显示归属地的那个view
						mTv_place = null;
					}
					break;
					
				case TelephonyManager.CALL_STATE_RINGING://铃响状态
					String addressString =  NumberAddressService.getInstance(PhoneAddressService.this).getAddress(incomingNumber); 
					showLocation(addressString);
			}
		}
		
	}
	
	private  void showLocation(String address){
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;//设置成半透明的
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.setTitle("Toast");
		
		mTv_place = new TextView(PhoneAddressService.this);
		mTv_place.setText("归属地： " + address);
		windowManager.addView(mTv_place, params);
	}

}
