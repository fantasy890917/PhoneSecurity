package com.security.ui;

import com.security.R;
import com.security.engine.NumberAddressService;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryNumberActivity extends Activity implements OnClickListener{
	
	private Button mBt_query;
	private EditText mEt_query_number;
	private TextView mTv_query_result;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_query);
		
		mBt_query = (Button) findViewById(R.id.bt_query);
		mEt_query_number = (EditText) findViewById(R.id.et_query_number);
		mTv_query_result =(TextView) findViewById(R.id.tv_query_result);
		
		mBt_query.setOnClickListener(this);
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
