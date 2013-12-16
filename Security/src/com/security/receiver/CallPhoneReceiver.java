package com.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.security.ui.LostProtectedActivity;
import com.security.ui.SplashActivity;

public class CallPhoneReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// TODO Auto-generated method stub
		String outPhoneNumber =getResultData();
		Log.d(SplashActivity.TAG,"receiver:"+outPhoneNumber);
		if("131 4".equals(outPhoneNumber)){
			Intent intent1 = new Intent(context,LostProtectedActivity.class);
			//这个很重要，如果没有这一句，那就会报错，这一句是因为我们是在一个Receiver里面启动一个activity的，但activity的启动，都是放到一个栈里面的，  
            //但Receiver里面没有那个栈，所以我们要在这里启动一个activity，那就必须要指定这行代码啦  
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
            context.startActivity(intent1);  
            setResultData(null);    //这行代码是把广播的数据设置为null，这样就不会把刚刚那个号码拨打出去啦，只会启动我们的activity 
		}
	}

}
