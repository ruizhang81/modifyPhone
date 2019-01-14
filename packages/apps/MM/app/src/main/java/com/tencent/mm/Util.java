package com.tencent.mm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import static com.tencent.mm.receiver.BootBroadcastReceiver.TAG;

public class Util {

    //com.google.android.inputmethod.pinyin "com.lucky.luckyclient"
    public static boolean checkPackage(Context context, String packageStr){

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfos = packageManager.getInstalledPackages(0);
        for (PackageInfo pinfo:pinfos) {
//            Log.e(TAG,pinfo.packageName);
            if(pinfo.packageName.equals(packageStr)){
                Log.e(TAG,"已经有应用了");
                return true;
            }
        }
        return false;

    }


    public static class ItemText{
        int start;
        int end;
        ForegroundColorSpan span;
        String key;
        String value;
        String default_value;

        public ItemText(String p_key,String p_value,String p_default_value){
            key = p_key;
            value = p_value;
            default_value = p_default_value;
        }

    }
    private static StringBuilder sb;

    public static void setInfo(TextView textView,List<ItemText> itemTexts){
        sb = new StringBuilder();
        for(ItemText itemText:itemTexts){
            addValue(itemText);
        }
        SpannableStringBuilder style = new SpannableStringBuilder(sb.toString());
        for(ItemText itemText:itemTexts){
            style.setSpan(itemText.span, itemText.start, itemText.end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        textView.setText(style);
    }


    private static void addValue(ItemText itemText) {
        sb.append(itemText.key);
        sb.append(":");
        itemText.start = sb.length();
        if(!TextUtils.isEmpty(itemText.value)){
            itemText.value = itemText.value.replaceAll("(\r\n|\r|\n|\n\r)", "");
            sb.append(itemText.value);
        }else{
            itemText.value = "";
        }
        int org = Color.WHITE;
        int modify = Color.GREEN;
        if(itemText.default_value ==null){
            itemText.span = new ForegroundColorSpan(org);
        }else{
            if(itemText.default_value.equals(itemText.value)){
                itemText.span = new ForegroundColorSpan(org);
            }else{
                itemText.span = new ForegroundColorSpan(modify);
            }
        }
        itemText.end = sb.length();
        sb.append("\n");
    }



    public static void main(String[] args){
        String input = "35823912345678";
        System.out.println(input);
        System.out.println(getLast(input));
    }


    private static int getLast(String input){
        char[] ch = input.toCharArray();
        int a = 0, b = 0;
        for (int i = 0; i < ch.length; i++) {
            int tt = Integer.parseInt(ch[i] + "");
            if (i % 2 == 0) {
                a = a + tt;
            } else {
                int temp = tt * 2;
                b = b + temp / 10 + temp % 10;
            }
        }
        int last = (a + b) % 10;
        if (last == 0) {
            last = 0;
        } else {
            last = 10 - last;
        }
        return last;
    }
}
