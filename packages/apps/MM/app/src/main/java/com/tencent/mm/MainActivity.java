package com.tencent.mm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.action.ActionBaseListener;
import com.tencent.mm.action.ActionGetPhone;
import com.tencent.mm.action.ActionGetPhoneSms;
import com.tencent.mm.action.ActionModifyTime;
import com.tencent.mm.action.ActionSecureAndroidId;
import com.tencent.mm.action.ActionWakeAndUnlock;
import com.tencent.mm.dialog.WaitDialog;
import com.tencent.mm.info.TelephonyHelp;
import com.tencent.mm.receiver.BootBroadcastReceiver;
import com.tencent.mm.wifi.WifiHelp;

import java.text.SimpleDateFormat;


public class MainActivity extends Activity {

    private TextView tv;
    private StringBuilder sb;
    private TextView phoneText;
    private TextView smsText;
    private Handler initHandler;
    private ActionGetPhone actionGetPhone;
    private ActionModifyTime actionModifyTime;
    private String timeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        smsText = (TextView) findViewById(R.id.sms);
        phoneText = (TextView) findViewById(R.id.phone);
        phoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(phoneText.getText().toString());
            }
        });
        tv = (TextView) findViewById(R.id.info);


        TextView reboot_normal = (TextView) findViewById(R.id.reboot_normal);
        TextView modifyother = (TextView) findViewById(R.id.modifyother);
        TextView get_phone = (TextView) findViewById(R.id.get_phone);
        TextView free_phone = (TextView) findViewById(R.id.free_phone);
        TextView install = (TextView) findViewById(R.id.install);
        TextView install2 = (TextView) findViewById(R.id.install2);


        reboot_normal.setText("重启");
        modifyother.setText("2、修改唯一号");
        install.setText("3、安装咖啡");
        get_phone.setText("4、获取手机号");
        install2.setText("安装探探");
        free_phone.setText("一直没收到验证码，就释放手机号");
        phoneText.setText("手机号");
        smsText.setText("验证码");

        actionGetPhone = new ActionGetPhone(this);

        reboot_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("重启中...");
                Intent intent=new Intent(Intent.ACTION_REBOOT);
                intent.putExtra("nowait", 1);
                intent.putExtra("interval", 1);
                intent.putExtra("window", 0);
                sendBroadcast(intent);
            }
        });
        modifyother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaitDialog.showDialog(MainActivity.this,"加载中...",true);
                new ActionSecureAndroidId(MainActivity.this).run(null);
                new ActionWakeAndUnlock(MainActivity.this).run(null);
                WaitDialog.dismissDialog(MainActivity.this);
            }
        });

        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("安装咖啡...");

            }
        });
        install2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("安装探探...");

            }
        });
        get_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("请求手机号");
                if(actionGetPhone!=null){
                    actionGetPhone.run(new ActionBaseListener() {
                        @Override
                        public void onFinish(String... result) {
                            String phone = result[0];
                            phoneText.setText(phone);
                            if (!TextUtils.isEmpty(phone)) {
                                new ActionGetPhoneSms(MainActivity.this, new ActionGetPhoneSms.OnTimesListener() {
                                    @Override
                                    public void onTimes(long times) {
                                        smsText.setText("第" + times + "次获取验证码...");
                                    }
                                }).run(new ActionBaseListener() {
                                    @Override
                                    public void onFinish(String... result) {
                                        String sms = result[0];
                                        if (TextUtils.isEmpty(sms)) {
                                            smsText.setText("没有获取到验证码");
                                        } else{
                                            smsText.setText(sms);
                                        }
                                    }
                                },phone);
                            }
                        }
                    });
                }
            }
        });
        findViewById(R.id.free_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("释放手机号");
                if(actionGetPhone!=null) {
                    actionGetPhone.freePhone(false, new ActionBaseListener() {
                        @Override
                        public void onFinish(String... result) {
                            show("释放成功");
                            phoneText.setText("手机号");
                            smsText.setText("验证码");
                        }
                    });
                }
            }
        });

        smsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ActionGetPhoneSms.getSmsNum(smsText.getText().toString());
                copy(str);
            }
        });
        init();
    }


    @SuppressLint("MissingPermission")
    private void init() {
        initHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                actionModifyTime = new ActionModifyTime(MainActivity.this);
                actionModifyTime.run(new ActionBaseListener() {
                    @Override
                    public void onFinish(String... result) {
                        sb = new StringBuilder();
                        timeStr = getString(MainActivity.this,firstOpenTimeTag);
                        if(TextUtils.isEmpty(timeStr)){
                            long time = System.currentTimeMillis();
                            Log.e(BootBroadcastReceiver.TAG,"time="+time);
                            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
                            formatter.applyPattern("yyyy/MM/dd HH:mm:ss");
                            timeStr = formatter.format(new java.util.Date(time));
                            setString(MainActivity.this,timeStr,firstOpenTimeTag);
                        }
                        addValue("time:", timeStr);
                        addValue("DeviceId:", TelephonyHelp.getDeviceId(MainActivity.this));
                        addValue("IMSI:", TelephonyHelp.getSubscriberId(MainActivity.this));
                        addValue("ICCID:", TelephonyHelp.getSimSerialNumber(MainActivity.this));
                        addValue("SERIAL:", Build.SERIAL);
                        addValue("madAddress:", WifiHelp.getAdresseMAC(MainActivity.this));
                        addValue("android_id:", Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        tv.setText(sb.toString());
                        sendEmptyMessageDelayed(0, 3000);
                    }
                });
            }
        };
        initHandler.sendEmptyMessage(0);
    }



    private void addValue(String key, String value) {
        sb.append(key + ":" + value + "\n");
    }


    private void show(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (initHandler != null) {
            initHandler.removeMessages(0);
        }
        if(actionModifyTime!=null){
            actionModifyTime.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (actionGetPhone != null) {
            actionGetPhone.freePhone(false,null);
        }
        if(actionModifyTime!=null){
            actionModifyTime.onStop();
        }
    }


    private void copy(String text){
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
        Toast.makeText(MainActivity.this, "复制成功! "+text, Toast.LENGTH_LONG).show();
    }

    public final static String applicationTag = "applicationTag";
    public final static String aotuTag = "aotuTag";
    public final static String firstOpenTimeTag = "firstOpenTimeTag";

    public static void set(Context context,boolean aotu,String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(applicationTag, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(tag,aotu);
        edit.commit();
    }
    public static boolean get(Context context,String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(applicationTag, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(tag,false);
    }

    public static void setString(Context context,String value,String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(applicationTag, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(tag,value);
        edit.commit();
    }
    public static String getString(Context context,String tag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(applicationTag, Context.MODE_PRIVATE);
        return sharedPreferences.getString(tag,"");
    }
}
