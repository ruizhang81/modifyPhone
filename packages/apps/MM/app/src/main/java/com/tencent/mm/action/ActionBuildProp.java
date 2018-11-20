package com.tencent.mm.action;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.buildprop.BuildPropModify;

public class ActionBuildProp implements ActionBase {

    private Context mContext;

    public ActionBuildProp(Context context){
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
                BuildPropModify.modify();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
