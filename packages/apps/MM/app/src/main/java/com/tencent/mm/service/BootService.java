package com.tencent.mm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tencent.mm.MainActivity;
import com.tencent.mm.Util;
import com.tencent.mm.action.ActionBaseListener;
import com.tencent.mm.action.ActionBuildProp;
import com.tencent.mm.action.ActionCoffee;
import com.tencent.mm.action.ActionInstallDefaultApp;
import com.tencent.mm.action.ActionMac;
import com.tencent.mm.action.ActionModifyTime;
import com.tencent.mm.action.ActionRemoveSu;
import com.tencent.mm.action.ActionSecureAndroidId;
import com.tencent.mm.action.ActionSimulationFileSystem;
import com.tencent.mm.action.ActionWakeAndUnlock;

public class BootService extends Service {

    private ActionCoffee actionCoffee;

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
        new ActionInstallDefaultApp(BootService.this).run(null);

        boolean autoLu = MainActivity.get(BootService.this,MainActivity.aotuTag);
        MainActivity.set(this,false,MainActivity.aotuTag);
        if(autoLu){
            new ActionMac(this).run(new ActionBaseListener() {
                @Override
                public void onFinish(String... result) {
                    new ActionRemoveSu(BootService.this).run(null);
                    new ActionModifyTime(BootService.this).run(new ActionBaseListener() {
                        @Override
                        public void onFinish(String... result) {
                            if(Util.checkPackage(BootService.this,"com.lucky.luckyclient")){
                                return;
                            }
                            new ActionBuildProp(BootService.this).run(null);
                            actionCoffee = new ActionCoffee(BootService.this);
                            actionCoffee.run(null);
                        }
                    });
                }
            });
        }else{
            if(MainActivity.get(BootService.this,MainActivity.aotuWifiTag)){
                new ActionModifyTime(BootService.this).run(new ActionBaseListener() {
                    @Override
                    public void onFinish(String... result) {
                        new ActionBuildProp(BootService.this).run(null);
                        new ActionMac(BootService.this).run(new ActionBaseListener() {
                            @Override
                            public void onFinish(String... result) {
                                new ActionSimulationFileSystem(BootService.this).run(new ActionBaseListener() {
                                    @Override
                                    public void onFinish(String... result) {
                                        new ActionRemoveSu(BootService.this).run(null);
                                    }
                                });
                            }
                        });
                    }
                });
            }else{
                new ActionModifyTime(BootService.this).run(new ActionBaseListener() {
                    @Override
                    public void onFinish(String... result) {
                        new ActionBuildProp(BootService.this).run(null);
                        new ActionSimulationFileSystem(BootService.this).run(new ActionBaseListener() {
                            @Override
                            public void onFinish(String... result) {
                                new ActionRemoveSu(BootService.this).run(null);
                            }
                        });
                    }
                });
            }
        }
    }


    @Override
    public void onDestroy() {
        if(actionCoffee!=null){
            actionCoffee.onDestroy(false);
        }
        super.onDestroy();
    }
}
