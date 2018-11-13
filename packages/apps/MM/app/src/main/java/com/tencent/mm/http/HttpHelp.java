package com.tencent.mm.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.mm.receiver.BootBroadcastReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelp {

    public static void http(final String url, final OnCallBackListener onCallBackListener) {
        final Handler hander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                onCallBackListener.onCallBack((String) msg.obj);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                String back = http_sync(url);
                Message message = new Message();
                message.obj = back;
                hander.sendMessage(message);
            }
        }).start();//启动子线程
    }

    private static String http_sync(String p_Url) {
        Log.e(BootBroadcastReceiver.TAG, "p_Url:" + p_Url);
        try {
            //1,找水源--创建URL
            URL url = new URL(p_Url);//放网站
            //2,开水闸--openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //3，建管道--InputStream
            InputStream inputStream = httpURLConnection.getInputStream();
            //4，建蓄水池蓄水-InputStreamReader
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            //5，水桶盛水--BufferedReader
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer buffer = new StringBuffer();
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                //取水--如果不为空就一直取
                buffer.append(temp);
            }
            bufferedReader.close();//记得关闭
            reader.close();
            inputStream.close();

            String back = buffer.toString();
            Log.e(BootBroadcastReceiver.TAG, "bufferStr:" + back);
            return back;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnCallBackListener {
        void onCallBack(String back);
    }
}
