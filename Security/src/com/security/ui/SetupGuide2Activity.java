package com.security.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.security.R;
import com.security.utils.SecurityInfoUtil;

public class SetupGuide2Activity extends Activity implements OnClickListener {

	private Button bt_bind;
	private Button bt_next;
	private Button bt_perviout;
	private CheckBox cb_bind;
	private SharedPreferences sp;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_guide2);
		sp = getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB, Context.MODE_PRIVATE);
		bt_bind = (Button) findViewById(R.id.bt_guide_bind);
		bt_next = (Button) findViewById(R.id.bt_guide_next);
		bt_perviout = (Button) findViewById(R.id.bt_guide_pervious);
		bt_bind.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_perviout.setOnClickListener(this);
		
		cb_bind = (CheckBox)findViewById(R.id.cb_guide_check);
		//init checkBox
		String sim = sp.getString(SecurityInfoUtil.BIND_SIM_CARD_SERIAL, null);
		Log.d("shiguibiao","SM saved sn ="+sim);
		if(sim != null)
		{
			cb_bind.setText(R.string.guide2_bind);
			cb_bind.setChecked(true);
		}
		else
		{
			cb_bind.setText(R.string.guide2_not_bind);
			cb_bind.setChecked(false);
		}
		
		cb_bind.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cb_bind.setText(R.string.guide2_bind);
					setSimInfo(true);
				}else{
					cb_bind.setText(R.string.guide2_not_bind);
					setSimInfo(false);
				}
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.bt_guide_bind:
				setSimInfo(true);
				cb_bind.setChecked(true);
				cb_bind.setText(R.string.guide2_bind);
				break;
			
			case R.id.bt_guide_pervious:
				Intent i = new Intent(this, SetupGuide1Activity.class);
				finish();
				startActivity(i);
				//这个是定义activity切换时的动画效果的
				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
				break;
				
			case R.id.bt_guide_next:
				Intent intent = new Intent(this, SetupGuide3Activity.class);
				finish();
				startActivity(intent);
				//这个是定义activity切换时的动画效果的
				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
				break;
				
			default:
				break;
		}
	}
	
	private void setSimInfo(boolean setFlag)
	{
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String simSerial = telephonyManager.getSimSerialNumber();//拿到sim卡的序列号，是唯一的

		Editor editor = sp.edit();
		if(setFlag){
			editor.putString(SecurityInfoUtil.BIND_SIM_CARD_SERIAL, simSerial);
		}else{
			editor.remove(SecurityInfoUtil.BIND_SIM_CARD_SERIAL);
		}
		
		editor.commit();
	}
}
