package com.security.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.security.R;
import com.security.adapter.BlackNumberDataBaseAdapter;

public class BlackNumberActivity extends Activity {
	
	private ListView mLv_number;
	 private Button mBt_number_add;  
	private BlackNumberDataBaseAdapter mDatabaseAdapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.number_security_item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
