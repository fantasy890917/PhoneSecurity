/*
* Copyright (C) 2014 MediaTek Inc.
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/**
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.huaqin.lteband.util;

import static android.content.Intent.EXTRA_USER;

import android.annotation.Nullable;
import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.IActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceFrameLayout;
import android.preference.PreferenceGroup;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Profile;
import android.provider.ContactsContract.RawContacts;
import android.service.persistentdata.PersistentDataBlockManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabWidget;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.mediatek.common.MPlugin;
import android.content.res.Resources;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneConstants;
import com.huaqin.lteband.R;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
public final class Utils {
    public static final String TAG = "LteBand";
    

    /**
     * finds a record with slotId.
     * Since the number of SIMs are few, an array is fine.
     */
    public static SubscriptionInfo findRecordBySlotId(Context context, final int slotId) {
        final List<SubscriptionInfo> subInfoList =
                SubscriptionManager.from(context).getActiveSubscriptionInfoList();
        if (subInfoList != null) {
            final int subInfoLength = subInfoList.size();

            for (int i = 0; i < subInfoLength; ++i) {
                final SubscriptionInfo sir = subInfoList.get(i);
                if (sir.getSimSlotIndex() == slotId) {
                    //Right now we take the first subscription on a SIM.
                    return sir;
                }
            }
        }

        return null;
    }
	
    /* M: create settigns plugin object
     * @param context Context
     * @return ISettingsMiscExt
     */
    public static ISettingsMiscExt getMiscPlugin(Context context) {
        ISettingsMiscExt ext;
        ext = (ISettingsMiscExt) MPlugin.createInstance(
                     ISettingsMiscExt.class.getName(), context);
        if (ext == null) {
            ext = new DefaultSettingsMiscExt(context);
        }
        return ext;
    }
    
    public static String getCapaBitilySim(Resources mRes){
        Log.d(TAG,"getCapaBitilySim()");
        String result = mRes.getString(R.string.not_set);

        ITelephony iTelephony =
                ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        if (iTelephony == null) {
            return result;
        }
        
        try {
            if ( (iTelephony.getRadioAccessFamily(PhoneConstants.SIM_ID_1) & (RadioAccessFamily.RAF_UMTS
                    | RadioAccessFamily.RAF_LTE)) > 0 ){
                result = mRes.getString(R.string.radio_sim_1);
            }else if( (iTelephony.getRadioAccessFamily(PhoneConstants.SIM_ID_2) & (RadioAccessFamily.RAF_UMTS
                    | RadioAccessFamily.RAF_LTE)) > 0 ){
                result = mRes.getString(R.string.radio_sim_2);
            }
            Log.d(TAG,"result 11= "+result);
        } catch (RemoteException e) {
           // Log.e(LOG_TAG, e.getMessage());
            return result;
        }
        return result ;
    }
}
