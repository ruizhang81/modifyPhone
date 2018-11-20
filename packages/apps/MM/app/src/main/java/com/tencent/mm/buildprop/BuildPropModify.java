package com.tencent.mm.buildprop;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.R;
import com.tencent.mm.ShellHelp;
import com.tencent.mm.Test;
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
import java.util.List;
import java.util.Locale;

public class BuildPropModify {

    public static final String path = "/sdcard/Download/build.prop";

    public static void modify(){

        String newBuildPorp = Test.modifyFile();

        try {
            FileWriter fileWritter = new FileWriter(path);
            fileWritter.write(newBuildPorp);
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
