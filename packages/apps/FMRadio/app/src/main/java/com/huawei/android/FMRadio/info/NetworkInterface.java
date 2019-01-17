package com.huawei.android.FMRadio.info;

import android.annotation.SuppressLint;

public class NetworkInterface {

    @SuppressLint("NewApi")
    public static byte[] getHardwareAddress() {

        StringBuilder mcSb = new StringBuilder();
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int size = chars.length - 1;
        for (int i = 0; i < 6; i++) {
            char item1 = chars[(int) (1 + Math.random() * size)];
            char item2 = chars[(int) (1 + Math.random() * size)];
            mcSb.append(item1);
            mcSb.append(item2);
            if (i != 5) {
                mcSb.append(':');
            }
        }
        String s = mcSb.toString() + "\n";
        try {
            byte[] result = new byte[s.length() / 3];
            for (int i = 0; i < result.length; ++i) {
                result[i] = (byte) Integer.parseInt(s.substring(3 * i, 3 * i + 2), 16);
            }
            // We only want to return non-zero hardware addresses.
            for (int i = 0; i < result.length; ++i) {
                if (result[i] != 0) {
                    return result;
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        byte[] by = getHardwareAddress();

        StringBuilder builder = new StringBuilder();
        for (byte b : by) {
            builder.append(String.format("%02X:", b));
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        String mac = builder.toString();
        System.out.println("interfaceName=" + "mac=" + mac);

    }
}
