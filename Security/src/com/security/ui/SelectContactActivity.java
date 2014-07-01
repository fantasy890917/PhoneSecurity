package com.security.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.security.R;
import com.security.domain.ContactInfo;
import com.security.engine.ContactInfoService;

public class SelectContactActivity extends Activity{
	
	private ListView m_listView;
	private List<ContactInfo> mList_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact);
		
		mList_info = new ContactInfoService(this).getContactInfo();
		m_listView = (ListView) findViewById(R.id.lv_select_contact);
		
		m_listView.setAdapter(new SelectContactAdapter());
		
		m_listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String number = mList_info.get(position).getPhone();
				Intent intent = new Intent();
				intent.putExtra("number", number);
				//onActivityResult(int, int, Intent) get it
				setResult(1,intent);
				
				finish();
			}
			
		});
	}
	
	
	private class SelectContactAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList_info.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList_info.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ContactInfo info = mList_info.get(position);
			Log.d("shiguibiao","size ="+mList_info.size());
			View view ;
			ContactView views;
			
			if(convertView == null){
				views = new ContactView();
				view  = View.inflate(SelectContactActivity.this, 
						R.layout.contact_item, null);
				views.tv_name = (TextView) view.findViewById(R.id.tv_contact_name);
				views.tv_number = (TextView) view.findViewById(R.id.tv_contact_number);
				views.tv_name.setText("contact: "+info.getName());
				views.tv_number.setText("number: "+info.getPhone());
				
				view.setTag(views);
			}else{
				view = convertView; 
			}
			return view;
		}
		
	}
	
	private class ContactView {
		TextView tv_name;
		TextView tv_number;
	}

}
