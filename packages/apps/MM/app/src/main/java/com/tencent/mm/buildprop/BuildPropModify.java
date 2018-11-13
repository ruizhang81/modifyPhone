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
        Root.upgradeRootPermission("cat "+systemPath+buildPropFile);

    }


    private static void modifyFile(Context context,String path){
        HashMap<String,String> map = new HashMap<>(64);

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
                            map.put(keyValue[0],keyValue[1]);
                        }
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
        String tags = "product-keys";
        String user_time = type+".admin."+display_time_str;

        map.put("ro.build.version.incremental",user_time);
        map.put("ro.build.date",build_time_str);
        map.put("ro.build.date.utc",build_timestamp+"");
        map.put("ro.build.type",type);
        map.put("ro.build.host","d-b440b6e7");
        map.put("ro.build.tags",tags);
        map.put("ro.build.flavor",product+"-"+type);
        map.put("ro.product.model",company+" "+product);
        map.put("ro.product.name",product);
        map.put("ro.product.device",product);
        map.put("ro.product.board",product);
        map.put("ro.build.product",product);
        map.put("ro.build.display.id",map.get("ro.build.flavor")+" 6.0.1 M4B30Z "+user_time+" "+tags);
        map.put("ro.build.description",map.get("ro.build.display.id"));
        map.put("ro.build.fingerprint","Android/mtk_hammerhead/hammerhead:6.0.1/M4B30Z/admin"+fingerprint_time_str+":"+type+"/"+tags);

        //write
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append("# begin build properties\n");
            sb.append("# autogenerated by buildinfo.sh\n");
            Set<String> set = map.keySet();
            Iterator<String> iter = set.iterator();
            while(iter.hasNext()){
                String key = iter.next();
                String value = map.get(key);
                sb.append(key+"="+value+"\n");
            }
            FileWriter fileWritter = new FileWriter(path);
            fileWritter.write(sb.toString());
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
