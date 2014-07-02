package com.security.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.security.R;

public class MainUIAdapter extends BaseAdapter{
	
	private static final int [] NAMES = new int []{
		R.string.grid_view_Phone_theft, 
		R.string.grid_view_communitcate, 
		R.string.grid_view_softManage,
		R.string.grid_view_data_usage, 
		R.string.grid_view_task, 
		R.string.grid_view_antivirus, 
		R.string.grid_view_optimization,
		R.string.grid_view_tools,
		R.string.grid_view_settings};
	
	private static final int [] ICONS = new int [] {R.drawable.widget01, R.drawable.widget02, R.drawable.widget03,
		R.drawable.widget04, R.drawable.widget05, R.drawable.widget06, R.drawable.widget07, R.drawable.widget08, R.drawable.widget09 };
	
	private static ImageView imageView;
	private static TextView textView;
	private Context context;
	private LayoutInflater inflater;
	private SharedPreferences sp;
	
	public MainUIAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return NAMES.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 MainViews views;  
	        View view;  
	        if(convertView == null)  
	        {  
	            views = new MainViews();  
	            view = inflater.inflate(R.layout.main_item, null);  
	            views.imageView = (ImageView) view.findViewById(R.id.iv_main_icon);  
	            views.textView = (TextView) view.findViewById(R.id.tv_main_name);  
	            views.imageView.setImageResource(ICONS[position]);  
	            views.textView.setText(NAMES[position]);  
	              
	            view.setTag(views);  
	        }  
	        else  
	        {  
	            view = convertView;  
	            views = (MainViews) view.getTag();  
	            views.imageView = (ImageView) view.findViewById(R.id.iv_main_icon);  
	            views.textView = (TextView) view.findViewById(R.id.tv_main_name);  
	            views.imageView.setImageResource(ICONS[position]);  
	            views.textView.setText(NAMES[position]);  
	        }  
	              
	        if(position == 0)  
	        {  
	            String name = sp.getString("lostName", "");  
	            if(!name.equals(""))  
	            {  
	                views.textView.setText(name);  
	            }  
	        }    
	          
	        return view;  
	}
	
	 //==================================================================================  
    
    //一个存放所有要绘制的控件的类  
    private class MainViews  
    {  
        ImageView imageView;  
        TextView textView;  
    }  
}
