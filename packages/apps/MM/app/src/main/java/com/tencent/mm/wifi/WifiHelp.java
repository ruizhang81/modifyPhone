package com.tencent.mm.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class WifiHelp {


    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            return getAddressMacByFile(wifiMan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marshmallowMacAddress;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }


    public static void forget(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        if (list != null) {
            for (WifiConfiguration i : list) {
                wifiManager.removeNetwork(i.networkId);
                wifiManager.saveConfiguration();
            }
        }
    }
//
//    public static void modifyMacAddress() {
//        StringBuilder cmd = new StringBuilder();
//        cmd.append("ifconfig wlan0 hw ether 8c:3a:e3");
//        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        int size = chars.length;
//        for (int i = 0; i < 3; i++) {
//            char char1 = chars[(int) (Math.random() * size)];
//            char char2 = chars[(int) (Math.random() * size)];
//            cmd.append(":");
//            cmd.append(char1);
//            cmd.append(char2);
//        }
//        String cmdStr = cmd.toString();
////        ShellHelp.excu(cmdStr);
//    }

    public static void openAirPlane(Context context) {
        Settings.Global.putInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 1);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", true);
        context.sendBroadcast(intent);
    }

    public static void closeAirPlane(Context context) {
        Settings.Global.putInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", true);
        context.sendBroadcast(intent);
    }
}
//:11:11:11
//ip link set wlan0 down &&ip link set wlan0 address 54:36:9b:22:33:aa
//ip link set wlan0 down &&ip link set wlan0 address 00:90:4c:c5:12:38
//ip link set wlan0 down &&ip link set wlan0 address 8c:3a:e3:3d:44:cc
//cat /sys/class/net/wlan0/address