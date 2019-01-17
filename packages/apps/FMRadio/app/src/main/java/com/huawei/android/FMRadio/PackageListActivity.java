package com.huawei.android.FMRadio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class PackageListActivity extends Activity {


    private ListView listview;
    private List<String> packageInfoList;

    public static void start(Context context){
        Intent intent = new Intent();
        intent.setClass(context,PackageListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pkglist);

        listview = (ListView)findViewById(R.id.listview);

        PackageManager packageManager = getPackageManager();
        packageInfoList = new ArrayList<>();

        packageManager.getInstalledApplications()
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for(PackageInfo packageInfo:packageInfos){

            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0 ||
                    packageInfo.packageName.equals(this.getPackageName()) ) {
                // 第三方应用
                String str = getString(packageInfo);
                if(!TextUtils.isEmpty(str)){
                    packageInfoList.add(str);
                }
            }
        }
        listview.setAdapter(new PkgAdapter());
    }


    private String getString(PackageInfo packageInfo) {
        Field[] fields = packageInfo.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object value = field.get(packageInfo);
                if( value != null){
                    Class className = value.getClass();
                    if (className.equals(java.lang.Integer.class) ||
                            className.equals(java.lang.String.class) ||
                            className.equals(java.lang.Boolean.class)) {
                        sb.append(name);
                        sb.append(":");
                        sb.append(value);
                        sb.append("\n");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private class PkgAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return packageInfoList.size();
        }

        @Override
        public String getItem(int position) {
            return packageInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(PackageListActivity.this);
            String packageInfo = getItem(position);
            textView.setText(packageInfo);
            return textView;
        }



    }





}
