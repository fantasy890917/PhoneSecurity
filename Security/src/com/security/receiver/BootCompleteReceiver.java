package com.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.security.R;
import com.security.utils.SecurityInfoUtil;

public class BootCompleteReceiver extends BroadcastReceiver {
	
	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		sp = context.getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB,
				Context.MODE_PRIVATE);
		boolean isProtected = sp.getBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, false);
		
		if(isProtected){
			TelephonyManager telephonyManager = (TelephonyManager) context.
					getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = telephonyManager.getSimSerialNumber();
			String protectSim = sp.getString(SecurityInfoUtil.BIND_SIM_CARD_SERIAL, null);
			Log.d("shiguibiao","currentSim = "+currentSim+" protectSim="+protectSim);
			if(protectSim!=null &&
					(!currentSim.equals(protectSim))){
				//send sms to phone security call that sim card changed
				SmsManager smsManager = SmsManager.getDefault();
				String number  = sp.getString(SecurityInfoUtil.PROTECTED_PHONE_NUMBER, null);
				if(number !=null){
					smsManager.sendTextMessage(number, null, 
							context.getResources().getString(R.string.sim_changed_attention), null, null);
				}
			}
		}
	}

}
