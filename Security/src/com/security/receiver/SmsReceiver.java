package com.security.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.security.R;
import com.security.engine.GPSInfoProvider;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu :pdus){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
			//get message
			String content = smsMessage.getMessageBody();
			
			//get send address
			String sender = smsMessage.getOriginatingAddress();
			
			Log.d("shiguibiao","message =" +content);
			Log.d("shiguibiao","sender =" +sender);
			
			if(content.equals("*#location#*")){
				abortBroadcast();
				
				GPSInfoProvider gpsInfoProvider = GPSInfoProvider.getInstance(context);
				String location = gpsInfoProvider.getLocation();  
				
				Log.d("shiguibiao","get location ="+location);
				if(!location.equals(""))  
                {  
                    //发送短信  
                    SmsManager smsManager = SmsManager.getDefault();  
                    smsManager.sendTextMessage(sender, null, location, null, null);  
                }
			}else if(content.equals("*#lockscreen#*")){
				DevicePolicyManager manager = (DevicePolicyManager)context
						.getSystemService(Context.DEVICE_POLICY_SERVICE);
				//set password
				manager.resetPassword("test", 0);
				//lockscreen
				manager.lockNow();
				abortBroadcast();
			}else if(content.equals("*#reset#*")){
				DevicePolicyManager manager = (DevicePolicyManager)context
						.getSystemService(Context.DEVICE_POLICY_SERVICE);
				//reset
				manager.wipeData(0);

				abortBroadcast();
			}else if(content.equals("*#alarm#*")){
				MediaPlayer media = MediaPlayer.create(context, R.raw.alarm);
				
				media.setVolume(1.0f, 1.0f); 
				media.start();
				abortBroadcast();
			}
		}
	}

}
