package com.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.security.R;
import com.security.receiver.MyAdminReceiver;
import com.security.utils.SecurityInfoUtil;

public class SetupGuide4Activity extends Activity implements OnClickListener {

	private SharedPreferences sp;
	private Button mBt_finish;
	private Button mBt_pervious;
	private CheckBox mCb_protected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_guide4);
		
		sp = getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB, Context.MODE_PRIVATE);  
		
		mBt_finish = (Button) findViewById(R.id.bt_guide_finish);
		mBt_pervious = (Button) findViewById(R.id.bt_guide_pervious);
		mBt_finish.setOnClickListener(this);
		mBt_pervious.setOnClickListener(this);
		
		mCb_protected = (CheckBox) findViewById(R.id.cb_guide_protected);
		boolean isProtecting = sp.getBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, false); 
		
		if(isProtecting){
			mCb_protected.setText(R.string.has_open_protect);
			mCb_protected.setChecked(true);
		}
		
		mCb_protected.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mCb_protected.setText(R.string.has_open_protect);
					Editor editor = sp.edit();  
                    editor.putBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, true);  
                    editor.commit();
				}else{
					mCb_protected.setText(R.string.not_open_protect);  
	                Editor editor = sp.edit();  
	                editor.putBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, false);  
	                editor.commit(); 
				}
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
			case R.id.bt_guide_pervious :
				
				Intent intentPervi = new Intent(this,SetupGuide3Activity.class);
				finish();
				startActivity(intentPervi);
				break;
			
			case R.id.bt_guide_finish :
				
				if(mCb_protected.isChecked()){
					
					finishSetupGuide();
					finish();
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(R.string.protect_dialog_title);
					builder.setMessage(R.string.protect_dialog_msg);
					builder.setCancelable(false);
					builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							 Editor editor = sp.edit();  
	                         editor.putBoolean(SecurityInfoUtil.SETUPWIZARD_CHECKED, true); 
	                         editor.putBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, true);
	                         editor.commit();  
	                         mCb_protected.setText(R.string.has_open_protect);
	             			 mCb_protected.setChecked(true);
	                         finish();
						}
						
					});
					
					builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Editor editor = sp.edit();  
	                         editor.putBoolean(SecurityInfoUtil.SETUPWIZARD_CHECKED, true); 
	                         editor.commit();  
	                         finish();
						}
					});
					builder.create().show();
				}
				
				break;
				
			default:
				break;
		}
	}
	
	private void finishSetupGuide(){
		// setupWizard completed
		Editor editor = sp.edit();  
        editor.putBoolean(SecurityInfoUtil.SETUPWIZARD_CHECKED, true); 
        editor.commit();  
        
        //get device 
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        
        //new component to start ui
        ComponentName componentName = new ComponentName(this,MyAdminReceiver.class);
        if(!devicePolicyManager.isAdminActive(componentName)){
        	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        	startActivity(intent);
        }
	}
}
