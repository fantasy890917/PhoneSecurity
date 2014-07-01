package com.security.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.security.R;

public class SetupGuide1Activity extends Activity implements OnClickListener{

	private Button mBnext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_guide1);
		mBnext = (Button) findViewById(R.id.bt_guide_next);	
		mBnext.setOnClickListener(this);
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.bt_guide_next:
				Intent intent = new Intent(this, SetupGuide2Activity.class);
				finish();
				startActivity(intent);
				//这个是定义activity切换时的动画效果的
				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
				break;
			default:
				break;
		}
	}
}
