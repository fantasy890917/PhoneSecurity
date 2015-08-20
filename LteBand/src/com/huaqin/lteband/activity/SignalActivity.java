package com.huaqin.lteband.activity;

import com.huaqin.lteband.adapter.BandListAdapter;
import com.huaqin.lteband.util.LogUtils;
import com.huaqin.lteband.util.Utils;
import com.huaqin.lteband.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class SignalActivity extends Activity{
	
	private static final String TAG = "SignalActivity";
	
	/** Read all Todo infos from QB */
    private BandListAdapter mBandListAdapter = null;
    /** Show all Todo infos in ListView */
    private ListView mBandListView = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items);
		initViews();
        //configureActionBar();
        LogUtils.d(TAG, "SignalActivity.onCreate() finished.");
	}
	
	private void initViews() {
		LogUtils.d(TAG, "initViews()");
		mBandListAdapter = new BandListAdapter(this);
		mBandListView = (ListView) findViewById(R.id.list_band);
		mBandListView.setAdapter(mBandListAdapter);
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

}
