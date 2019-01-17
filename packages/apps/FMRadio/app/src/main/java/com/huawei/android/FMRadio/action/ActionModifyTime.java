package com.huawei.android.FMRadio.action;

import android.app.AlarmManager;
import android.content.Context;
import android.text.TextUtils;

import com.huawei.android.FMRadio.http.HttpHelp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionModifyTime implements ActionBase {

    private Context mContext;
    private ActionBaseListener mListener;

    public ActionModifyTime(Context context){
        mContext = context;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        mListener = listener;
        getTime();
    }

    public  void getTime(){
        HttpHelp.http("http://quan.suning.com/getSysTime.do", new HttpHelp.OnCallBackListener() {

            @Override
            public void onCallBack(String back) {
                if (TextUtils.isEmpty(back)) {

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(back);
                        String sysTime2 = jsonObject.getString("sysTime2");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = formatter.parse(sysTime2);
                            long when = date.getTime();
                            if (when / 1000 < Integer.MAX_VALUE) {
                                ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).setTime(when);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (mListener != null) {
                            mListener.onFinish();
                            mListener = null;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }
        });
    }

}
