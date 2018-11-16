package com.tencent.mm.buildprop;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.R;
import com.tencent.mm.Root;
import com.tencent.mm.receiver.BootBroadcastReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class BuildPropModify {


    public static void modify(Context context){
        String buildPropFile = "build.prop";
        String systemPath = "/system/";
        String sourceDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e(BootBroadcastReceiver.TAG,"sourceDir="+sourceDir);
        String sourceAbSolutePath = sourceDir+File.separator+buildPropFile;
        Root.upgradeRootPermission("cp -f "+systemPath+buildPropFile+" "+sourceDir);

        modifyFile(context,sourceAbSolutePath);

        Root.upgradeRootPermission("mount -ro remount,rw /system");
        Root.upgradeRootPermission("chmod 664 "+systemPath+buildPropFile);
        Root.upgradeRootPermission("mv -f "+sourceAbSolutePath+" "+systemPath);
        Root.upgradeRootPermission("chmod 644 "+systemPath+buildPropFile);

//        Root.upgradeRootPermission("cat "+sourceAbSolutePath);
//        Root.upgradeRootPermission("cat "+systemPath+buildPropFile);

    }


    private static void modifyFile(Context context,String path){
        List<BuildPropKV> buildPropKVList = new ArrayList<>();

        //read
        try {
            FileReader m=new FileReader(path);
            BufferedReader reader=new BufferedReader(m);

            while(true) {
                String nextline = reader.readLine();
                if(nextline == null){
                    break;
                }else{
                    if(!TextUtils.isEmpty(nextline)){
                        String[] keyValue = nextline.split("=");
                        if(keyValue.length == 2){
                            buildPropKVList.add(new BuildPropKV(keyValue[0],keyValue[1]));
                        }else{
                            buildPropKVList.add(new BuildPropKV(nextline,null));
                        }
                    }else{
                        buildPropKVList.add(new BuildPropKV("",null));
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //display time
        Calendar display_calendar = getRandomTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss", Locale.CHINA);
        String display_time_str = format.format(display_calendar.getTime());

        //build time
        Calendar build_calendar = getRandomTime();
        long build_timestamp = build_calendar.getTimeInMillis();
        SimpleDateFormat build_format= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        String build_time_str = build_format.format(build_calendar.getTime());

        //fingerprint time
        SimpleDateFormat fingerprint_format = new SimpleDateFormat("MMddHHmm", Locale.CHINA);
        String fingerprint_time_str = fingerprint_format.format(display_calendar.getTime());

        ArrayList<BuildPropClass> list = readAssert(context);
        int randomIndex = (int)(Math.random()*list.size());
        BuildPropClass buildProp = list.get(randomIndex);
        String product = buildProp.product;
        String company = buildProp.company;

        String type = "user";
        String tags = "release-keys";
        String user_time = type+".admin."+display_time_str;
        String flavor = product+"-"+type;
        String display_id = flavor+" 6.0.1 M4B30Z "+user_time+" "+tags;
        String buildId = company+" "+product;

        modifyValue(buildPropKVList,"ro.build.id",buildId);
        modifyValue(buildPropKVList,"ro.build.version.incremental",user_time);
        modifyValue(buildPropKVList,"ro.build.date",build_time_str);
        modifyValue(buildPropKVList,"ro.build.date.utc", (build_timestamp*1000+3228)+"");
        modifyValue(buildPropKVList,"ro.build.type",type);
        modifyValue(buildPropKVList,"ro.build.host","d-b440b6e7");
        modifyValue(buildPropKVList,"ro.build.tags",tags);
        modifyValue(buildPropKVList,"ro.build.flavor",flavor);
        modifyValue(buildPropKVList,"ro.product.model",buildId);
        modifyValue(buildPropKVList,"ro.product.name",product);
        modifyValue(buildPropKVList,"ro.product.device",product);
        modifyValue(buildPropKVList,"ro.product.board",product);
        modifyValue(buildPropKVList,"ro.build.product",product);
        modifyValue(buildPropKVList,"ro.build.display.id",display_id);
        modifyValue(buildPropKVList,"ro.build.description",display_id);
        modifyValue(buildPropKVList,"ro.build.fingerprint","Android/"+product+"/hammerhead:6.0.1/M4B30Z/admin"+fingerprint_time_str+":"+type+"/"+tags);
        modifyValue(buildPropKVList,"ro.expect.recovery_id",create_recovery_id());
        modifyValue(buildPropKVList,"ro.product.manufacturer",company);

        //write
        try {
            StringBuilder sb = new StringBuilder();
            int size = buildPropKVList.size();
            for(int i = 0; i < size; i ++ ){
                BuildPropKV buildPropKV = buildPropKVList.get(i);
                String key = buildPropKV.key;
                String value = buildPropKV.value;
                if(!TextUtils.isEmpty(value)){
                    sb.append(key+"="+value);
                    if(i!=size-1){
                        sb.append("\n");
                    }
                }else{
                    sb.append(key+"\n");
                }
            }
            FileWriter fileWritter = new FileWriter(path);
            fileWritter.write(sb.toString());
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void modifyValue(List<BuildPropKV> buildPropKVList,String key,String value){
        for(BuildPropKV buildPropKV:buildPropKVList){
            if(buildPropKV.key.equals(key)){
                buildPropKV.value = value;
                break;
            }
        }
    }

    private static String create_recovery_id(){
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int size = chars.length;
        for(int i = 0; i < 40; i ++ ){
            char charItem = chars[(int) (Math.random() * size)];
            sb.append(charItem);
        }
        sb.append("000000000000000000000000");
        return sb.toString();
    }

    private static Calendar getRandomTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-1);
        long display_time_start = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH,-4);
        long display_time_end = calendar.getTimeInMillis();
        long display_time = (long)(display_time_start+Math.random()*(display_time_end-display_time_start+1));
        calendar.setTimeInMillis(display_time);
        return calendar;
    }


    private static ArrayList<BuildPropClass> readAssert(Context context){

        InputStream inputStream = context.getResources().openRawResource(R.raw.prop);


        ArrayList<BuildPropClass> list = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line;

            while ((line = bf.readLine()) != null) {
                if(list.size() == 0){
                    BuildPropClass buildProp = new BuildPropClass();
                    list.add(buildProp);
                }
                BuildPropClass buildPropOld = list.get(list.size()-1);

                if(TextUtils.isEmpty(buildPropOld.product)){
                    buildPropOld.product = line;
                }else if(TextUtils.isEmpty(buildPropOld.company)){
                    buildPropOld.company = line;
                }else if(TextUtils.isEmpty(buildPropOld.other)){
                    buildPropOld.other = line;
                }else{
                    BuildPropClass buildPropNew = new BuildPropClass();
                    buildPropNew.product = line;
                    list.add(buildPropNew);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
