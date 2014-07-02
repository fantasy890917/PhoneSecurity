package com.security.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.security.R;
import com.security.utils.MD5Encoder;
import com.security.utils.SecurityInfoUtil;

public class LostProtectedActivity extends Activity implements OnClickListener{

	private SharedPreferences sp;  
    private Dialog dialog;  
    private EditText password;  
    private EditText confirmPassword; 
    
    private TextView mTv_protectNumber;
    private Button mTv_protectGuide;
    private CheckBox mCb_isProtected;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB, Context.MODE_PRIVATE);  
        
        if(isSetPassword())  
        {  
            showLoginDialog();  
        }  
        else  
        {  
            showFirstDialog();  
        }
	}
	
	private void showLoginDialog()  
    {  
        dialog = new Dialog(this, R.style.MyDialog);  
        View view = View.inflate(this, R.layout.login_dialog, null);  
        password = (EditText) view.findViewById(R.id.et_protected_password);  
        Button yes = (Button) view.findViewById(R.id.bt_protected_login_yes);  
        Button cancel = (Button) view.findViewById(R.id.bt_protected_login_no);  
        yes.setOnClickListener(this);  
        cancel.setOnClickListener(this);  
        dialog.setContentView(view);  
        dialog.show();  
    } 
	
	 private void showFirstDialog() {  
		 dialog = new Dialog(this, R.style.MyDialog);  
	     //dialog.setContentView(R.layout.first_dialog);  
	     View view = View.inflate(this, R.layout.first_dialog, null);    //这样来填充一个而已文件，比较方便  
	     password = (EditText) view.findViewById(R.id.et_protected_first_password);  
	     confirmPassword = (EditText) view.findViewById(R.id.et_protected_confirm_password);  
	     Button yes = (Button) view.findViewById(R.id.bt_protected_first_yes);  
	     Button cancel = (Button) view.findViewById(R.id.bt_protected_first_no);  
	     yes.setOnClickListener(this);  
	     cancel.setOnClickListener(this);  
	     dialog.setContentView(view);  
	     dialog.show();
	}
	
	 private boolean isSetPassword(){  
	    String pwd = sp.getString(SecurityInfoUtil.LOGIN_PASSWORD, "");  
	    if(pwd.equals("") || pwd == null)  {  
	       return false;  
	    }  
	    return true;  
	} 
	
	 private boolean isSetupWizard(){
		 return sp.getBoolean(SecurityInfoUtil.SETUPWIZARD_CHECKED, false);
	 }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())  
        {  
            case R.id.bt_protected_first_yes :   
                String fp = password.getText().toString().trim();  
                String cp = confirmPassword.getText().toString().trim();  
                if(fp.equals("") || cp.equals(""))  
                {  
                    Toast.makeText(this, R.string.password_not_null, Toast.LENGTH_SHORT).show();  
                    return;  
                }  
                else   
                {  
                    if(fp.equals(cp))  
                    {  
                        Editor editor = sp.edit();  
                        editor.putString(SecurityInfoUtil.LOGIN_PASSWORD, MD5Encoder.encode(fp));  
                        editor.commit(); 
                        dialog.dismiss(); 
                        //first set password ,go into setup wizard
                        if(!isSetupWizard()){
                        	finish();
                        	Intent setupWizardIntent = new Intent(this,SetupGuide1Activity.class);
                        	startActivity(setupWizardIntent);
                        }
                        
                    }  
                    else  
                    {  
                        Toast.makeText(this, R.string.password_same, Toast.LENGTH_SHORT).show();  
                        return;  
                    }  
                }  
                dialog.dismiss();  
                break;  
                  
            case R.id.bt_protected_first_no :   
                dialog.dismiss();  
                finish();  
                break;  
                  
            case R.id.bt_protected_login_yes :   
                String pwd = password.getText().toString().toString();  
                if(pwd.equals(""))  
                {  
                    Toast.makeText(this, R.string.inputPassword, Toast.LENGTH_SHORT).show();  
                }  
                else  
                {  
                    String str = sp.getString(SecurityInfoUtil.LOGIN_PASSWORD, "");  
                    if(MD5Encoder.encode(pwd).equals(str))  
                    {  
                        dialog.dismiss();  
                    	setContentView(R.layout.lost_protected);
                    	mTv_protectNumber = (TextView) findViewById(R.id.tv_lost_protected_number);
                    	mTv_protectGuide = (Button) findViewById(R.id.tv_lost_protected_guide);
                    	mCb_isProtected = (CheckBox) findViewById(R.id.cb_lost_protected_isProtected);
                    	mTv_protectNumber.setText("Safety call：" + sp.getString("number", ""));
                    	mTv_protectGuide.setOnClickListener(this);
                    	
                    	boolean isProtecting = sp.getBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, false);
                    	if(isProtecting)  
                        {  
                    		mCb_isProtected.setText(R.string.has_open_protect);  
                    		mCb_isProtected.setChecked(true);  
                        }else{
                        	mCb_isProtected.setText(R.string.not_open_protect);  
                    		mCb_isProtected.setChecked(false);  
                        }
                    	
                    	mCb_isProtected.setOnCheckedChangeListener(new OnCheckedChangeListener(){

							@Override
							public void onCheckedChanged(CompoundButton buttonView,
									boolean isChecked) {
								// TODO Auto-generated method stub
								if(isChecked)  
                                {  
									mCb_isProtected.setText(R.string.has_open_protect);  
                                    Editor editor = sp.edit();  
                                    editor.putBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, true);  
                                    editor.commit();  
                                }  
                                else  
                                {  
                                	mCb_isProtected.setText(R.string.not_open_protect);  
                                    Editor editor = sp.edit();  
                                    editor.putBoolean(SecurityInfoUtil.IS_PROTECTED_CHECKED, false);  
                                    editor.commit();  
                                }  
							}
                    		
                    	});
                    }  
                    else  
                    {  
                        Toast.makeText(this, R.string.password_is_wrong, Toast.LENGTH_SHORT).show();  
                    }  
                }  
                break;  
                  
            case R.id.bt_protected_login_no :   
                dialog.dismiss();  
                finish();  
                break;  
            
            case R.id.tv_lost_protected_guide : //reset into setupWizard  
                finish();  
                Intent setupGuideIntent = new Intent(this, SetupGuide1Activity.class);  
                startActivity(setupGuideIntent);  
                break;
                
            default :   
                break;  
        }  
	} 
	 
	

}
