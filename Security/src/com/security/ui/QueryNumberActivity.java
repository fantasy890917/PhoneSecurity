package com.security.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.security.R;
import com.security.engine.NumberAddressService;
import com.security.service.PhoneAddressService;
import com.security.utils.SecurityInfoUtil;

public class QueryNumberActivity extends Activity implements OnClickListener{
	
	private Button mBt_query;
	private EditText mEt_query_number;
	private TextView mTv_query_result;
	
	private CheckBox mCb_open_service;
	private TextView mTv_service_state;
	
	private Intent serviceIntent;
	private Thread loadDBThread;
	private boolean threadFlag = false ;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_query);
		sp = getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB, Context.MODE_PRIVATE);  
		
		mBt_query = (Button) findViewById(R.id.bt_query);
		mEt_query_number = (EditText) findViewById(R.id.et_query_number);
		mTv_query_result =(TextView) findViewById(R.id.tv_query_result);
		
		mBt_query.setOnClickListener(this);
		
		loadDBThread = new Thread(){
			public void run(){
				NumberAddressService.getInstance(QueryNumberActivity.this).loadDBFile();
				threadFlag = true;
			}
		};
		
		loadDBThread.start();
		
		//checkbox open service
		mCb_open_service = (CheckBox) findViewById(R.id.cb_atool_state);
		mTv_service_state = (TextView) findViewById(R.id.tv_atool_number_service_state);
		serviceIntent = new Intent(this, PhoneAddressService.class);
		
		boolean isPhoneServiceChecked = sp.getBoolean(SecurityInfoUtil.PHONE_NUMBER_SERVICE_STATE, false); 
		
		if(isPhoneServiceChecked){
			mTv_service_state.setText(R.string.number_service_state_open);
			mCb_open_service.setChecked(true);
		}
		
		mCb_open_service.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					startService(serviceIntent);
					mTv_service_state.setTextColor(Color.BLACK);
					mTv_service_state.setText(R.string.number_service_state_open);
					
					Editor editor = sp.edit();  
                    editor.putBoolean(SecurityInfoUtil.PHONE_NUMBER_SERVICE_STATE, true);  
                    editor.commit();
				}else{
					stopService(serviceIntent);
					mTv_service_state.setTextColor(Color.RED);
					mTv_service_state.setText(R.string.number_service_state_not_open);
					
					Editor editor = sp.edit();  
                    editor.putBoolean(SecurityInfoUtil.PHONE_NUMBER_SERVICE_STATE, false);  
                    editor.commit();
				}
			}
			
		});
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.bt_query:
				query();
				break;
			default:
				break;
		}
	}
	
	private void query(){
		String number = mEt_query_number.getText().toString().trim();  
        //如果查询内容为空，那么就抖动输入框  
		if(!threadFlag){
			mTv_query_result.setText("正在加载");  
			return ;
        }  

        if(TextUtils.isEmpty(number))  
        {  
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);  
            mEt_query_number.startAnimation(shake);  
        }  
        else  
        {  
            String address = NumberAddressService.getInstance(this).getAddress(number);  
            mTv_query_result.setText("归属地信息：" + address);  
        }  
	}

}
