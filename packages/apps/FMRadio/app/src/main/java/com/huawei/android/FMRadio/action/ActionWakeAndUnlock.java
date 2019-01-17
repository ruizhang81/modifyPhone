package com.huawei.android.FMRadio.action;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class ActionWakeAndUnlock implements ActionBase {

    private Context mContext;

    public ActionWakeAndUnlock(Context context){
        mContext = context;
    }

    @Override
    public void run(ActionBaseListener listener, String... args) {

        // 获取电源管理器对象
        PowerManager pm = (PowerManager) mContext.getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "myApp:bright");
            wl.acquire();
            wl.release();
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) mContext.getApplicationContext()
                .getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
//        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁

        if(listener!=null){
            listener.onFinish();
        }
    }
}
