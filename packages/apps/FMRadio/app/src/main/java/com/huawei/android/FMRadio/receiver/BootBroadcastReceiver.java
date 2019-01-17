package com.huawei.android.FMRadio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huawei.android.FMRadio.service.BootService;

public class BootBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "test_android";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG,"action="+action);
        if (action.equals(ACTION_BOOT)) {
            Intent intent1 = new Intent(context, BootService.class);
            context.startService(intent1);
        }
    }

}