package com.tencent.mm.action;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.tencent.mm.http.HttpHelp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionModifyTime implements ActionBase {

    private Context mContext;

    public ActionModifyTime(Context context){
        mContext = context;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        getTime(listener);
    }

    public  void getTime(final ActionBaseListener listener){
        HttpHelp.http("http://cgi.im.qq.com/cgi-bin/cgi_svrtime",new HttpHelp.OnCallBackListener(){

            @Override
            public void onCallBack(String back) {
                if(TextUtils.isEmpty(back)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTime(listener);
                        }
                    },1000);
                }else{
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date date = formatter.parse(back);
                        long when = date.getTime();
                        if(when / 1000 < Integer.MAX_VALUE){
                            ((AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE)).setTime(when);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(listener!=null){
                        listener.onFinish();
                    }
                }
            }
        });
    }
}
