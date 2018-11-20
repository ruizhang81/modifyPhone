package com.tencent.mm;

import android.util.Log;

import com.tencent.mm.receiver.BootBroadcastReceiver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellHelp {


    public final static String prop_changemac = "dev.changemac.enable";
    public final static String prop_changebuildprop = "dev.changebuildprop.enable";
    public final static String prop_myreboot = "dev.myreboot.enable";

    public static void setprop(String prop,String value){
        Log.e(BootBroadcastReceiver.TAG, "setprop " +prop +" "+ value);
//        excu("setprop "+prop+" "+value);

        android.os.SystemProperties.set(prop,value);
    }

    public static void excu(String cmd) {
        Log.e(BootBroadcastReceiver.TAG, "cmd=" + cmd);
        DataOutputStream os = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd+"\n");
            os = new DataOutputStream(process.getOutputStream());
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
