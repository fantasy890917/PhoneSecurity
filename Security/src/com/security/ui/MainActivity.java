package com.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.security.R;
import com.security.adapter.MainUIAdapter;

public class MainActivity extends Activity implements OnItemClickListener
{
	private GridView gridView;
	private SharedPreferences sp;
	private MainUIAdapter adapter;
	
	private static final int POSITION_ANTI_THEFT = 0;
	private static final int POSITION_COMMUNI_FUARD = 1;
	private static final int POSITION_SOFTWARE_MANAGER = 2;
	private static final int POSITION_DATA_USAGE = 3;
	private static final int POSITION_TAST_MANAGER= 4;
	private static final int POSITION_ANTI_VIRUS = 5;
	private static final int POSITION_SYS_OPTIMIZATION = 6;
	private static final int POSITION_SENIOR_TOOLS = 7;
	private static final int POSITION_SETTINGS = 8;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		gridView = (GridView) findViewById(R.id.gv_main);
		adapter = new MainUIAdapter(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(new OnItemLongClickListener()  
        {  
            @Override  
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id)  
            {  
                if(position == POSITION_ANTI_THEFT)   //这个是因为，如果我们的手机被盗了，用户一看到第一个手机防盗，那样肯定会先卸载我们的程序的，所以我们在手机防盗这个item里面，设置了一个重命名的功能  
                {  
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  
                    builder.setTitle(R.string.rename_title);  
                    builder.setMessage(R.string.rename_msg);  
                    final EditText et = new EditText(MainActivity.this);  
                    et.setHint(R.string.rename_hint);  
                    builder.setView(et);  
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()  
                    {  
                        @Override  
                        public void onClick(DialogInterface dialog, int which)  
                        {  
                            String name = et.getText().toString();  
                            if(name.equals(""))  
                            {  
                                Toast.makeText(MainActivity.this, R.string.rename_msg_not_null, Toast.LENGTH_SHORT).show();  
                            }  
                            else  
                            {  
                                Editor editor = sp.edit();  
                                editor.putString("lostName", name);  
                                editor.commit();  
                                  
                                TextView tv = (TextView) view.findViewById(R.id.tv_main_name);  
                                tv.setText(name);  
                                adapter.notifyDataSetChanged();  
                            }  
                        }  
                    });  
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()  
                    {  
                        @Override  
                        public void onClick(DialogInterface dialog, int which)  
                        {  
                            // TODO Auto-generated method stub  
                              
                        }  
                    });  
                    builder.create().show();  
                }  
                return true;  
            }  
        });  
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch(position)  
        {  
            case 0 : //手机防盗 
            	Intent intentProtect = new Intent(this,LostProtectedActivity.class);
                startActivity(intentProtect);  
                  
            case 1 : //通讯卫士  
                break;  
                  
            case 2 : //软件管理  
                break;  
                  
            case 3 : //流量管理  
                break;  
                  
            case 4 : //任务管理  
                break;  
                  
            case 5 : //手机杀毒  
                break;  
                  
            case 6 : //系统优化  
                break;  
                  
            case 7 : //高级工具  
                break;  
                  
            case 8 : //设置中心  
                break;  
                  
            default :   
                break;  
        }  
	}

}
