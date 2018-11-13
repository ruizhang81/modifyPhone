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

    public static void upgradeRootPermission(String[] cmds) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            for(String cmd:cmds){
                os = new DataOutputStream(process.getOutputStream());
                if (!TextUtils.isEmpty(cmd)) {
                    os.writeBytes(cmd + "\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    process.waitFor();
                }
            }
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

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static void upgradeRootPermission(String cmd) {
        Log.e(BootBroadcastReceiver.TAG, "cmd=" + cmd);
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            if (!TextUtils.isEmpty(cmd)) {
                os.writeBytes(cmd + "\n");
            }
//            printMessage(process.getInputStream());
//            printMessage(process.getErrorStream());
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

    public static String excuteCommand(String cmd) {
        Log.e(BootBroadcastReceiver.TAG, "cmd=" + cmd);
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(cmd); //切换到root帐号
            return printMessage(process.getInputStream());
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
        return "";
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
