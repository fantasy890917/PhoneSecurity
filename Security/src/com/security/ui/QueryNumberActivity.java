package com.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
	private TextView mTv_toast_bg;
	private TextView mTv_toast_location;
	
	//add blacknumber textView
	private TextView mTv_black_number ;
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
		mTv_black_number = (TextView) findViewById(R.id.tv_atool_black_number);
		mTv_black_number.setOnClickListener(this);
		//set style of Toast
		mTv_toast_bg = (TextView) findViewById(R.id.tv_atool_select_bg);
		mTv_toast_location = (TextView) findViewById(R.id.tv_atool_change_location);
		mTv_toast_bg.setOnClickListener(this);
		mTv_toast_location.setOnClickListener(this);
		
		//query
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
			
			case R.id.tv_atool_select_bg :   
                selectStyle();  
                break; 
                
			 case R.id.tv_atool_change_location :   
	              Intent intent = new Intent(this, DragViewActivity.class);  
	              startActivity(intent);  
	              break;
			 case R.id.tv_atool_black_number:
				 Intent blackNumIntent = new Intent(this,BlackNumberActivity.class);
				 startActivity(blackNumIntent);
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
	
	private void selectStyle(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("归属地显示风格");
		String[] items = new String[] {"半透明", "活力橙", "苹果绿", "孔雀蓝", "金属灰"};
		int selectedItem = sp.getInt(SecurityInfoUtil.TOAST_BACKGROUND, 0);
		builder.setSingleChoiceItems(items, selectedItem, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit(); 
				editor.putInt(SecurityInfoUtil.TOAST_BACKGROUND, which);
				editor.commit();
			}
		});
		
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		builder.create().show();
	}

}
