package com.huaqin.lteband.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.SubscriptionManager;
import android.telephony.ServiceState;

import com.huaqin.lteband.util.*;

import android.content.Context;
import android.telephony.gsm.GsmCellLocation;

public class PhoneStateService extends Service{
        public static final String TAG = Utils.TAG;
        
        private PhoneStateListener[] mPhoneServiceStateListener;
        private int mSlotCount = -1;
        private TelephonyManager mTelephonyManager;
        private String mCellId = "";
        private String mLacId = "";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "service onCreate()");
		// set listen for SIM INFO
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mSlotCount = getSlotCount();
                mPhoneServiceStateListener = new PhoneStateListener[mSlotCount];
                registerPhoneStateListener();
	}
	
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
                
                return Service.START_STICKY;
        }   

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static int getSlotCount() {
                //FIXME: the slot count may not always be equal to phone count
                return TelephonyManager.getDefault().getPhoneCount();
        }
        
        private void registerPhoneStateListener() {
                for (int i = 0 ; i < mSlotCount ; i++) {
                    final int subId = getFirstSubInSlot(i);
                    Log.d(TAG, "subId: " + subId);
                    if (subId >= 0) {
                        mPhoneServiceStateListener[i] = getPhoneStateListener(subId, i);
                        mTelephonyManager.listen(mPhoneServiceStateListener[i], PhoneStateListener.LISTEN_SERVICE_STATE);
                    } else {
                        mPhoneServiceStateListener[i] = null;
                    }
                }
        }
        
        public static int getFirstSubInSlot(int slotId) {
                int[] subIds = SubscriptionManager.getSubId(slotId);
                if (subIds != null && subIds.length > 0) {
                    return subIds[0];
                }
                Log.d(TAG, "Cannot get first sub in slot: " + slotId);
                return SubscriptionManager.INVALID_SUBSCRIPTION_ID;
        }
        private String getTargetString(String target) {
		if (target == null || target.equals("")) {
			return "null";
		}
		return target;

	}
        private PhoneStateListener getPhoneStateListener(final int subId, final int slotId) {
                return new PhoneStateListener(subId) {
                    @Override
                    public void onDataConnectionStateChanged(int state) {
                        //updateNetworkType();
                        Log.d(TAG,"onDataConnectionStateChanged");
                    }
                    
                    @Override
                    public void onServiceStateChanged(ServiceState state) {
                        android.util.Log.d(TAG, "PhoneStateListener:onServiceStateChanged, slot " + slotId + " servicestate = " + state);
                        if (slotId == 0){
                            if (state.getState() == ServiceState.STATE_IN_SERVICE){
                                Log.d(TAG,"onServiceStateChanged");
                                GsmCellLocation cellLocation = (GsmCellLocation) mTelephonyManager.getCellLocationBySubId(subId);
		                if (cellLocation != null) {
			                mCellId = Integer.toString(cellLocation.getCid());
			                mLacId = Integer.toString(cellLocation.getLac());
			                Log.d(TAG,"sim1 mLacId = "+mLacId+" mCellId = "+mCellId);
		                }
                            }else{
                                Log.d(TAG,"onServiceStateChanged 0");
                            }
                        }
                        if (slotId == 1){
                            if (state.getState() == ServiceState.STATE_IN_SERVICE){
                                Log.d(TAG,"onServiceStateChanged 1");
                                GsmCellLocation cellLocation = (GsmCellLocation) mTelephonyManager.getCellLocationBySubId(subId);
		                if (cellLocation != null) {
			                mCellId = Integer.toString(cellLocation.getCid());
			                mLacId = Integer.toString(cellLocation.getLac());
			                Log.d(TAG,"sim2 mLacId = "+mLacId+" mCellId = "+mCellId);
		                }
                            }else{
                                Log.d(TAG,"onServiceStateChanged 11");
                            }
                        }
                    }
                };
        }
}
