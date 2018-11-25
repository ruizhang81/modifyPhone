package com.tencent.mm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.tencent.mm.MainActivity;
import com.tencent.mm.action.ActionBaseListener;
import com.tencent.mm.action.ActionModifyTime;
import com.tencent.mm.action.ActionSecureAndroidId;
import com.tencent.mm.action.ActionWakeAndUnlock;

public class BootService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        new ActionSecureAndroidId(this).run(null);
        new ActionWakeAndUnlock(this).run(null);
        init();
    }

    private void init(){
        boolean autoLu = MainActivity.get(BootService.this,MainActivity.aotuTag);
        MainActivity.set(this,false,MainActivity.aotuTag);
        if(autoLu){

//                            new ActionRemoveSu(BootService.this).run(null);
//                    actionCoffee = new ActionCoffee(BootService.this);
//                    actionCoffee.run(null);

            showFinish();
        }else{
            showFinish();
        }
    }

    private void showFinish(){
        Toast.makeText(BootService.this,"finish!!!!!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
//        if(actionCoffee!=null){
//            actionCoffee.onDestroy(false);
//        }
        super.onDestroy();
    }
}
