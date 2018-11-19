package com.tencent.mm;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.receiver.BootBroadcastReceiver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Root {

    private static Process process = null;
    private static DataOutputStream os = null;


//    private static void init(){
//        if(process == null){
//            Log.e(BootBroadcastReceiver.TAG, "Root init");
//            try {
//                process = Runtime.getRuntime().exec("su");
//                os = new DataOutputStream(process.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void startSu(){
        upgradeRootPermission("mv system/xbin/su1 system/xbin/su");
        upgradeRootPermission("mv /su/bin/su1 /su/bin/su");
    }

    public static void removeSu(){
        upgradeRootPermission("mv system/xbin/su system/xbin/su1");
        upgradeRootPermission("mv /su/bin/su /su/bin/su1");
    }



    public static void upgradeRootPermission(String cmd) {
        Log.e(BootBroadcastReceiver.TAG, "cmd=" + cmd);
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            if (!TextUtils.isEmpty(cmd)) {
                os.writeBytes(cmd + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }




    private static String printMessage(final InputStream input) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bf.readLine()) != null) {
                sb.append(line+"\n");
//                Log.e(BootBroadcastReceiver.TAG, "output:" + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
