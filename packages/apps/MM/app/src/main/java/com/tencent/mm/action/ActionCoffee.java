package com.tencent.mm.action;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.Root;
import com.tencent.mm.Util;

import static com.tencent.mm.receiver.BootBroadcastReceiver.TAG;

public class ActionCoffee implements ActionBase {

    private Context mContext;
    private Handler mainHandler;
    private boolean first = true;
    private ActionGetPhone actionGetPhone;

    public ActionCoffee(Context context){
        mContext = context;
        actionGetPhone = new ActionGetPhone(mContext);
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {
        Root.upgradeRootPermission("mv system/xbin/su system/xbin/su1");
        start();
        if(listener!=null){
            listener.onFinish();
        }
    }

    private void start() {
        Root.upgradeRootPermission("pm install sdcard/Download/luckincoffee_25.apk");
        Log.e(TAG, "install luck");
        Root.upgradeRootPermission("am start -n com.lucky.luckyclient/.splash.splash.SplashActivity");
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        //滑动
                        Root.upgradeRootPermission("input swipe 600 300 0 300");
                        sendEmptyMessageDelayed(1, 1000);
                        break;
                    case 1:
                        //滑动
                        Root.upgradeRootPermission("input swipe 600 300 0 300");
                        sendEmptyMessageDelayed(2, 1000);
                        break;
                    case 2:
                        //定位允许
                        Root.upgradeRootPermission("input tap 500 1600");
                        sendEmptyMessageDelayed(3, 1500);
                        break;
                    case 3:
                        Root.upgradeRootPermission("input tap 850 1100");
                        sendEmptyMessageDelayed(4, 2000);
                        break;
                    case 4:
                        //点个人页面
                        Root.upgradeRootPermission("input tap 900 1600");
                        sendEmptyMessageDelayed(5, 1000);
                        break;
                    case 5:
                        Root.upgradeRootPermission("input tap 500 800");
                        if(!first){
                            for(int i = 0;i < 15;i++){
                                Root.excuteCommand("input keyevent 67");
                            }
                        }
                        if(actionGetPhone!=null){
                            actionGetPhone.run(new ActionBaseListener() {
                                @Override
                                public void onFinish(String... result) {
                                    String phone = result[0];
                                    Root.upgradeRootPermission("input text " + phone);
                                    if (!TextUtils.isEmpty(phone)) {
                                        new ActionGetPhoneSms(mContext,null).run(new ActionBaseListener() {
                                            @Override
                                            public void onFinish(String... result) {
                                                if(result!=null){
                                                    String sms = result[0];
                                                    if(!TextUtils.isEmpty(sms)){
                                                        Root.upgradeRootPermission("input text " + sms);
                                                        //点击登录
                                                        Root.upgradeRootPermission("rm system/xbin/su");
                                                        Root.excuteCommand("input tap 500 1200");
                                                        mainHandler.sendEmptyMessageDelayed(6, 3000);
                                                    }else{
                                                        onDestroy(true);
                                                    }
                                                }else{
                                                    onDestroy(true);
                                                }
                                            }
                                        },phone);
                                    }
                                    //点验证码
                                    Root.upgradeRootPermission("input keyevent 4");
                                    Root.upgradeRootPermission("input tap 850 900");
                                }
                            });
                        }
                        break;
                    case 6:
                        String[] arr = {
                                "liang","zhang","gui","wu","li","zhao","tan","song","wang","chen","sun","ding"
                        };
                        String name = arr[(int) (Math.random() * arr.length)];
                        //输入名字
                        Root.excuteCommand("input text " + name);
                        //按空格
//                        if(Util.checkPackage(mContext,"com.google.android.inputmethod.pinyin")){
//                            Root.excuteCommand("adb shell input  keyevent  62");
//                        }
                        Root.excuteCommand("input keyevent 66");
                        //点确认
                        Root.excuteCommand("input tap 500 600");

                        Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                        break;
                }
            }
        };
        mainHandler.sendEmptyMessageDelayed(0, 10000);
    }



    public void onDestroy(final boolean again) {
        if(actionGetPhone!=null){
            actionGetPhone.freePhone(again, new ActionBaseListener() {
                @Override
                public void onFinish(String... result) {
                    if(again){
                        first = false;
                        mainHandler.sendEmptyMessageDelayed(5, 1000);
                    }
                }
            });
        }
    }
}