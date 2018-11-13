package com.tencent.mm.action;

import android.content.Context;

import com.tencent.mm.buildprop.BuildPropModify;

public class ActionBuildProp implements ActionBase {

    private Context mContext;

    public ActionBuildProp(Context context){
        mContext = context;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        BuildPropModify.modify(mContext);
        if(listener!=null){
            listener.onFinish();
        }
    }
}
