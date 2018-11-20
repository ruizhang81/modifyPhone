package com.tencent.mm.action;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tencent.mm.MainActivity;
import com.tencent.mm.ShellHelp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActionSimulationFileSystem implements ActionBase {

    private final static String PathsTag = "PathsTag";
    private final static String sp = ",";
    private List<String> stringList;
    private Context mContext;

    public ActionSimulationFileSystem(Context context){
        mContext = context;
        stringList = new ArrayList<>();
    }

    @Override
    public void run(final ActionBaseListener listener, String... args) {
//        if(listener!=null){
//            listener.onFinish();
//        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(listener!=null){
                    listener.onFinish();
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                ShellHelp.excu("mount -ro remount,rw /data");
                ShellHelp.excu("mount -ro remount,rw /system");

                removeFile();
//                createFiles();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void removeFile() {
        ShellHelp.excu("rm -rf /storage/emulated/0/Android");
        ShellHelp.excu("rm -rf /storage/emulated/0/Mob");
        ShellHelp.excu("rm -rf /storage/emulated/0/libs");
        ShellHelp.excu("rm -rf /storage/emulated/0/Podcasts");
        ShellHelp.excu("rm -rf /storage/emulated/0/baidu");
        ShellHelp.excu("rm -rf /storage/emulated/0/TWRP");
        ShellHelp.excu("rm -rf /storage/self/primary/Mob");
        ShellHelp.excu("rm -rf /storage/self/primary/libs");
        ShellHelp.excu("rm -rf /storage/self/primary/Podcasts");
        ShellHelp.excu("rm -rf /storage/self/primary/baidu");
        ShellHelp.excu("rm -rf /storage/self/primary/TWRP");
        ShellHelp.excu("rm -rf /data/bugreports");
        ShellHelp.excu("rm -rf /data/ss");
        ShellHelp.excu("rm -rf /data/tombstones/*");


        String str = getPath(mContext,PathsTag);
        String[] pathList = str.split(sp);
        if(pathList.length>0){
            for(String path : pathList){
                if(!TextUtils.isEmpty(path)){
                    ShellHelp.excu("rm "+path);
                }
            }
        }
        stringList.clear();
        savePath(mContext,"",PathsTag);
    }


    private void createFiles(){
        String[] dirs = {
                "/data/system",
                "/system/framework"
        };
        for(String dir:dirs){
            createFile(dir);
        }

        String str = listToString(stringList,sp);
        savePath(mContext,str,PathsTag);
    }

    private void createFile(String dir){
        ShellHelp.excu("mount -ro remount,rw "+dir);
        String fileName = getRandomString(12);
        String path = dir+ File.separator+fileName;
        ShellHelp.excu("echo 'hahaha!'>"+path);
    }

    private String listToString(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if(i != size-1){
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    private static String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        int size = str.length();
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(size);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }



    private  void savePath(Context context,String value,String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.applicationTag, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(tag,value);
        edit.commit();
    }

    private  String getPath(Context context,String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.applicationTag, Context.MODE_PRIVATE);
        return sharedPreferences.getString(tag,"");
    }
}
