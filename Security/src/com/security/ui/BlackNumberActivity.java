package com.security.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

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
		mDatabaseAdapter.open();
		mListNumbers = mDatabaseAdapter.fetchAllData();
		
		//number set list adapter
		mLv_number = (ListView) findViewById(R.id.lv_number);
		mLv_number.setAdapter(adapter);
		
		//button add number
		mBt_number_add =(Button) findViewById(R.id.bt_number_add);
		mBt_number_add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
		

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	private class NumberAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
