package com.tencent.mm.action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.mm.wifi.WifiHelp;

import static com.tencent.mm.receiver.BootBroadcastReceiver.TAG;

public class ActionMac implements ActionBase {

    public final static String macStr = "8c:3a:e3:3d:44:cc";
    private ActionBaseListener mListener;
    private Handler mHandler;
    private Context mContext;


    private BroadcastReceiver mwifiBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isConnect(intent)) {
                mHandler.removeMessages(4);
                mHandler.sendEmptyMessageDelayed(4,5000);
            }
        }
    };

    private boolean isConnect(final Intent intent) {
        //wifi连接上与否
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                return false;
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                //获取当前wifi名称
                return true;
            }
        }
        return false;
    }

    public ActionMac(Context context){
        mContext = context;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        WifiHelp.openAirPlane(mContext);
                        break;
                    case 1:
                        WifiHelp.modifyMacAddress();
                        break;
                    case 2:
                        IntentFilter filter = new IntentFilter();
                        filter.addAction("android.net.wifi.STATE_CHANGE");
                        mContext.registerReceiver(mwifiBroadcastReceiver, filter);
                        WifiHelp.closeAirPlane(mContext);
                        break;
                    case 3:
                        changeMac();
                        break;
                    case 4:
                        String newMac = WifiHelp.getAdresseMAC(mContext).substring(0, 17);
                        Log.e(TAG, "newMac=" + newMac);
                        if (newMac.equals(macStr)) {
                            //等待
                            removeMessages(3);
                            sendEmptyMessageDelayed(3,3000);
                        } else {
                            try {
                                mContext.unregisterReceiver(mwifiBroadcastReceiver);
                            } catch (Exception e) {

                            }
                            removeMessages(3);
                            removeMessages(5);
                            sendEmptyMessageDelayed(5,3000);
                        }
                        break;
                    case 5:
                        if(mListener!=null){
                            mListener.onFinish();
                            mListener = null;
                        }
                        break;
                }
            }
        };
    }

    private synchronized void changeMac() {
        Log.e(TAG, "changeMac ");
        new Thread() {
            @Override
            public void run() {
                try {
                    mHandler.sendEmptyMessage(0);

                    sleep(1000);
                    mHandler.sendEmptyMessage(1);

                    sleep(4000);
                    mHandler.sendEmptyMessage(2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public void run(ActionBaseListener listener, String... args) {
        mListener = listener;
        changeMac();
    }
}
