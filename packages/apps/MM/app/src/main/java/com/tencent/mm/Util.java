package com.tencent.mm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import static com.tencent.mm.receiver.BootBroadcastReceiver.TAG;

public class Util {

    //com.google.android.inputmethod.pinyin "com.lucky.luckyclient"
    public static boolean checkPackage(Context context, String packageStr){

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ ) {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageStr)){
                Log.e(TAG,"已经有应用了");
                return true;
            }
        }
        return false;

    }
}
