//package com.huawei.android.FMRadio.dialog.action;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Vibrator;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.huawei.android.FMRadio.dialog.Util;
//
//import static com.huawei.android.FMRadio.dialog.receiver.BootBroadcastReceiver.TAG;
//
//public class ActionCoffee implements ActionBase {
//
//    private Context mContext;
//    private Handler mainHandler;
//    private boolean first = true;
//    private ActionGetPhone actionGetPhone;
//
//    public ActionCoffee(Context context){
//        mContext = context;
//        actionGetPhone = new ActionGetPhone(mContext);
//    }
//
//    @Override
//    public void run(ActionBaseListener listener, String... args) {
//        if(Util.checkPackage(mContext,"com.lucky.luckyclient")){
//            if(listener!=null){
//                listener.onFinish();
//            }
//            return;
//        }
////        new ActionRemoveSu(mContext).run(null);
//        start();
//        if(listener!=null){
//            listener.onFinish();
//        }
//    }
//
//    private void start() {
//        ShellHelp.excu("pm install sdcard/Download/luckincoffee_25.apk");
//        Log.e(TAG, "install luck");
//        ShellHelp.excu("am start -n com.lucky.luckyclient/.splash.splash.SplashActivity");
//        mainHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 0:
//                        //滑动
//                        ShellHelp.excu("input swipe 600 300 0 300");
//                        sendEmptyMessageDelayed(1, 1000);
//                        break;
//                    case 1:
//                        //滑动
//                        ShellHelp.excu("input swipe 600 300 0 300");
//                        sendEmptyMessageDelayed(2, 1000);
//                        break;
//                    case 2:
//                        //定位允许
//                        ShellHelp.excu("input tap 500 1600");
//                        sendEmptyMessageDelayed(3, 1500);
//                        break;
//                    case 3:
//                        ShellHelp.excu("input tap 850 1100");
//                        sendEmptyMessageDelayed(4, 2000);
//                        break;
//                    case 4:
//                        //点个人页面
//                        ShellHelp.excu("input tap 900 1600");
//                        sendEmptyMessageDelayed(5, 1000);
//                        break;
//                    case 5:
//                        ShellHelp.excu("input tap 500 800");
//                        if(!first){
//                            for(int i = 0;i < 15;i++){
//                                ShellHelp.excu("input keyevent 67");
//                            }
//                        }
//                        if(actionGetPhone!=null){
//                            actionGetPhone.run(new ActionBaseListener() {
//                                @Override
//                                public void onFinish(String... result) {
//                                    String phone = result[0];
//                                    ShellHelp.excu("input text " + phone);
//                                    if (!TextUtils.isEmpty(phone)) {
//                                        new ActionGetPhoneSms(mContext,null).run(new ActionBaseListener() {
//                                            @Override
//                                            public void onFinish(String... result) {
//                                                if(result!=null){
//                                                    String sms = result[0];
//                                                    if(!TextUtils.isEmpty(sms)){
//                                                        ShellHelp.excu("input text " + sms);
//                                                        //点击登录
////                                                        new ActionRemoveSu(mContext).run(null);
////                                                        ShellHelp.removeSuFinial();
//                                                        ShellHelp.excu("input tap 500 1200");
//                                                        mainHandler.sendEmptyMessageDelayed(6, 3000);
//                                                    }else{
//                                                        onDestroy(true);
//                                                    }
//                                                }else{
//                                                    onDestroy(true);
//                                                }
//                                            }
//                                        },phone);
//                                    }
//                                    //点验证码
//                                    ShellHelp.excu("input keyevent 4");
//                                    ShellHelp.excu("input tap 850 900");
//                                }
//                            });
//                        }
//                        break;
//                    case 6:
//                        String[] arr = {
//                                "liang","zhang","gui","wu","li","zhao","tan","song","wang","chen","sun","ding"
//                        };
//                        String name = arr[(int) (Math.random() * arr.length)];
//                        //输入名字
//                        ShellHelp.excu("input text " + name);
//                        //按空格
////                        if(Util.checkPackage(mContext,"com.google.android.inputmethod.pinyin")){
////                            ShellHelp.excuteCommand("adb shell input  keyevent  62");
////                        }
//                        ShellHelp.excu("input keyevent 66");
//                        //点确认
//                        ShellHelp.excu("input tap 500 600");
//
//                        Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
//                        vibrator.vibrate(1000);
//                        break;
//                }
//            }
//        };
//        mainHandler.sendEmptyMessageDelayed(0, 10000);
//    }
//
//
//
//    public void onDestroy(final boolean again) {
//        if(actionGetPhone!=null){
//            actionGetPhone.freePhone(again, new ActionBaseListener() {
//                @Override
//                public void onFinish(String... result) {
//                    if(again){
//                        first = false;
//                        mainHandler.sendEmptyMessageDelayed(5, 1000);
//                    }
//                }
//            });
//        }
//    }
//}