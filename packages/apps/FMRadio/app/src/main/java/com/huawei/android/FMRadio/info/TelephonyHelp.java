package com.huawei.android.FMRadio.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephonyHelp {


    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }


    @SuppressLint("MissingPermission")
    public static String getSubscriberId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSubscriberId();
    }

    @SuppressLint("MissingPermission")
    public static String getSimSerialNumber(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimSerialNumber();
    }


}
