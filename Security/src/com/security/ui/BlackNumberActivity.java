package com.security.ui;

import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.security.R;
import com.security.adapter.BlackNumberDataBaseAdapter;
public class BlackNumberActivity extends Activity {

	private ListView mLv_number;
	 private Button mBt_number_add;  
	private BlackNumberDataBaseAdapter mDatabaseAdapter ;
	private List<String> mListNumbers;
	private NumberAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.number_security);
		//load database,fitch all phone number
		mDatabaseAdapter = new BlackNumberDataBaseAdapter(this);
		adapter = new NumberAdapter();
		mDatabaseAdapter.open();
		mListNumbers = mDatabaseAdapter.fetchAllData();
		
		//number set list adapter
		mLv_number = (ListView) findViewById(R.id.lv_number);
		mLv_number.setAdapter(adapter);
		
		//regit context for lisview
		registerForContextMenu(mLv_number);
		
		//button add number
		mBt_number_add =(Button) findViewById(R.id.bt_number_add);
		mBt_number_add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(BlackNumberActivity.this);
				builder.setTitle(R.string.add_black_number);
				final EditText et_number = new EditText(BlackNumberActivity.this);
				et_number.setInputType(InputType.TYPE_CLASS_PHONE);
				et_number.setHint(R.string.hint_black_number);
				builder.setView(et_number);
				
				builder.setPositiveButton(R.string.button_add_msg, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String number = et_number.getText().toString().trim();
						if(TextUtils.isEmpty(number) || !Pattern.compile("[0-9]*").matcher(number).matches()){
							Toast.makeText(BlackNumberActivity.this, 
									R.string.number_not_null_msg, Toast.LENGTH_SHORT).show();
						}else{
							mDatabaseAdapter.insertData(number);
							mListNumbers = mDatabaseAdapter.fetchAllData();
							//Log.d("shiguibiao","mListNumbers =" +mListNumbers);
							adapter.notifyDataSetChanged();
						}
					}
				});
				
				builder.setNegativeButton(R.string.button_cancle_msg, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				
				builder.create().show();
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDatabaseAdapter.close();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.black_number, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		//拿到菜单的信息
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
			case R.id.update_number :
				String oldNum = mListNumbers.get((int) info.id);
				updateNumber(oldNum);
				break;
			
			case R.id.delete_number :
				mDatabaseAdapter.deleteData(mListNumbers.get((int)info.id));
				mListNumbers = mDatabaseAdapter.fetchAllData();
				adapter.notifyDataSetChanged();
				break;
		}
		return super.onContextItemSelected(item);
	}
	
	
	private void updateNumber(final String oldNumber){
		AlertDialog.Builder builder = new AlertDialog.Builder(BlackNumberActivity.this);  
        builder.setTitle(R.string.update_black_number_title);  
        final EditText et_number = new EditText(BlackNumberActivity.this);  
        et_number.setInputType(InputType.TYPE_CLASS_PHONE);  
        et_number.setHint(R.string.update_black_number_msg);  
        builder.setView(et_number);  
        builder.setPositiveButton(R.string.button_modify_msg, new DialogInterface.OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                String number = et_number.getText().toString().trim();  

                if(TextUtils.isEmpty(number) || !Pattern.compile("[0-9]*").matcher(number).matches())  
                {  
                	Toast.makeText(BlackNumberActivity.this, 
							R.string.number_not_null_msg, Toast.LENGTH_SHORT).show();  
                }  
                else  
                {  
                	mDatabaseAdapter.updateData(oldNumber, number);
					mListNumbers = mDatabaseAdapter.fetchAllData();
					//Log.d("shiguibiao","mListNumbers =" +mListNumbers);
					adapter.notifyDataSetChanged(); 
                }  
            }  
        });  
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                  
            }  
        });  
        builder.create().show(); 
	}
	private class NumberAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListNumbers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListNumbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				View view = View.inflate(BlackNumberActivity.this, 
						R.layout.number_security_item, null);
				TextView tv_item = (TextView) view.findViewById(R.id.tv_number_item);
				tv_item.setText(mListNumbers.get(position));
				return view ;
			}else{
				TextView tv_item = (TextView) convertView.findViewById(R.id.tv_number_item);
				tv_item.setText(mListNumbers.get(position));
				return convertView ;
			}

		}
		
	}
}
