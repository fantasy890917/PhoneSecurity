package com.security.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.security.R;
import com.security.utils.SecurityInfoUtil;

public class SetupGuide3Activity extends Activity implements OnClickListener{

	private SharedPreferences sp;
	private Button mBt_next;
	private Button mBt_pervious;
	private Button mBt_select;
	
	private EditText mEt_phoneNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_guide3);
		
		sp = getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB, Context.MODE_PRIVATE);  
		
		mBt_next = (Button) findViewById(R.id.bt_guide_next);
		mBt_pervious = (Button) findViewById(R.id.bt_guide_pervious);
		mBt_select = (Button) findViewById(R.id.bt_guide_select);
		
		mBt_next.setOnClickListener(this);
		mBt_pervious.setOnClickListener(this);
		mBt_select.setOnClickListener(this);
		
		mEt_phoneNumber = (EditText) findViewById(R.id.et_guide_phoneNumber);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.bt_guide_select:
				
				Intent intent = new Intent(this,SelectContactActivity.class);
				startActivityForResult(intent,1);
				
				break;
			
			case R.id.bt_guide_next:
				
				String number = mEt_phoneNumber.getText().toString().trim();
				Log.d("shiguibiao","number = "+ number);
				if(number.equals("")){
					 Toast.makeText(this, R.string.guide3_number_not_null, Toast.LENGTH_SHORT).show();
				}else{
					Editor editor = sp.edit();
					editor.putString(SecurityInfoUtil.PROTECTED_PHONE_NUMBER, number);
					editor.commit();
					
					Intent intentNext = new Intent(this,SetupGuide4Activity.class);
					finish();
					startActivity(intentNext);
				}
				
				break;
				
			case R.id.bt_guide_pervious:
				
				Intent intentPervi = new Intent(this,SetupGuide2Activity.class);
				finish();
				startActivity(intentPervi);
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data != null){
			mEt_phoneNumber.setText(data.getStringExtra(SecurityInfoUtil.PROTECTED_PHONE_NUMBER));
		}
	}
}
