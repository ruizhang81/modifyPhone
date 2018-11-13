package com.tencent.mm.action;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.Root;
import com.tencent.mm.Util;
import com.tencent.mm.http.HttpHelp;
import com.tencent.mm.yima.Yima;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tencent.mm.receiver.BootBroadcastReceiver.TAG;

public class ActionGetPhone implements ActionBase {


    private Context mContext;
    private String mPhone;

    public ActionGetPhone(Context context){
        mContext = context;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        new ActionRemoveSu(mContext).run(null);
        getPhone(listener);

    }

    private void getPhone(final ActionBaseListener listener) {
        Yima.getInstanse().getPhone(new HttpHelp.OnCallBackListener() {
            @Override
            public void onCallBack(String phone) {
                mPhone = phone;
                listener.onFinish(mPhone);
            }
        });
    }


    public void freePhone(final boolean again,final ActionBaseListener listener){
        if(!TextUtils.isEmpty(mPhone)){
            Yima.getInstanse().freePhone(mPhone, new HttpHelp.OnCallBackListener() {
                @Override
                public void onCallBack(String phone) {
                    mPhone = null;
                    if(again){
                        listener.onFinish(null);
                    }
                }
            });
        }
    }


}