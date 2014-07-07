package com.security.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams; 
import com.security.R;
import com.security.utils.SecurityInfoUtil;

public class DragViewActivity extends Activity implements OnTouchListener{

	private ImageView mIv_drag_location;
	private SharedPreferences sp;
	
	private int startX;
	private int startY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drag_view);
		
		sp = getSharedPreferences(SecurityInfoUtil.SHARED_OREFERENCE_LIB,Context.MODE_PRIVATE);
		mIv_drag_location = (ImageView) findViewById(R.id.iv_drag_location);
		mIv_drag_location.setOnTouchListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume(); 
		//加载上次移动的效果  
        int x = sp.getInt("lastX", 0);  
        int y = sp.getInt("lastY", 0);  
        
        RelativeLayout.LayoutParams params = (LayoutParams) mIv_drag_location.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
        mIv_drag_location.setLayoutParams(params);
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(view.getId()){
			case R.id.iv_drag_location:
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN :
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					
					case MotionEvent.ACTION_MOVE :
						int x = (int) event.getRawX();
						int y = (int) event.getRawY();
						
						int dx = x - startX ;
						int dy = y -startY ;
						//mIv_drag_location.layout(l, t, r, b)
						mIv_drag_location.layout(mIv_drag_location.getLeft()+dx, 
								mIv_drag_location.getTop()+dy, 
								mIv_drag_location.getRight()+dx, 
								mIv_drag_location.getBottom()+dy);
						
						//重新获取位置  
                        startX = (int) event.getRawX();  
                        startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP :
						int lastX = mIv_drag_location.getLeft();
						int lastY = mIv_drag_location.getTop();
						Editor editor = sp.edit();  
                        editor.putInt("lastX", lastX);  
                        editor.putInt("lastY", lastY);  
                        editor.commit(); 
                        break;
				
					 default:
						break;
				}
				break;
			
			default:
				break;
		}
		return true;
	}

}
