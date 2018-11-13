package com.tencent.mm.action;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import static com.tencent.mm.receiver.BootBroadcastReceiver.TAG;

public class ActionSecureAndroidId implements ActionBase {

    private Context mContext;

    public ActionSecureAndroidId(Context context){
        mContext = context;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        Log.e(TAG, "writeAndroidid 1 " + Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder mcSb = new StringBuilder();
        int size = chars.length - 1;
        for (int i = 0; i < 16; i++) {
            char item1 = chars[(int) (1 + Math.random() * size)];
            mcSb.append(item1);
        }
        Settings.Secure.putString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID, mcSb.toString());
        Log.e(TAG, "writeAndroidid 2 " + Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));

        if(listener!=null){
            listener.onFinish();
        }
    }
}
