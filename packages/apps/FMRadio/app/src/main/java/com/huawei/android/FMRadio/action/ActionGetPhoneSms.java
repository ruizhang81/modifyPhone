package com.huawei.android.FMRadio.action;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.huawei.android.FMRadio.http.HttpHelp;
import com.huawei.android.FMRadio.yima.Yima;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionGetPhoneSms implements ActionBase {

    private Context mContext;
    private OnTimesListener mListener;
    private static Pattern p = Pattern.compile("\\d{6,}");
    private long startTime = 0;

    public interface OnTimesListener{
        void onTimes(long times);
    }

    public ActionGetPhoneSms(Context context,OnTimesListener listener){
        mContext = context;
        mListener = listener;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        String phone = args[0];
        if(!TextUtils.isEmpty(phone)){
            getSMS(phone,listener);
        }
    }

    private void getSMS(final String phone,final ActionBaseListener listener){
        final long durr = 5000;
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startTime++;
                if(mListener!=null){
                    mListener.onTimes(startTime);
                }
                if(startTime > 12){
                    listener.onFinish();
                    return;
                }
                Yima.getInstanse().getResult(phone, new HttpHelp.OnCallBackListener() {
                    @Override
                    public void onCallBack(String sms) {
                        if (TextUtils.isEmpty(sms)) {
                            sendEmptyMessageDelayed(0, durr);
                        } else {
                            String SmsNum = getSmsNum(sms);
                            if(!TextUtils.isEmpty(SmsNum)){
                                removeMessages(0);
                                listener.onFinish(SmsNum);
                            }
                        }
                    }
                });
            }

        }.sendEmptyMessageDelayed(0, durr);
    }


    public static String getSmsNum(String sms){
        Matcher m = p.matcher(sms);
        while (m.find()) {
            return m.group();
        }
        return null;
    }
}