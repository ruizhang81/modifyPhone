#!/bin/sh

appPath=$(cd `dirname $0`; pwd)
resultPath=../../../ZBuildResult


rm $resultPath/MM.apk
adb root
adb shell rm /data/data/com.tencent.mm/shared_prefs/applicationTag.xml

/usr/bin/expect <<EOF 


set timeout -1
spawn scp -Cr $appPath/app/src admin@172.17.10.25:/home/admin/androidSource/packages/apps/MM/app/
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr $appPath/Android.mk admin@172.17.10.25:/home/admin/androidSource/packages/apps/MM/
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSource; source build/envsetup.sh; lunch; mmm packages/apps/MM"
expect *password*
send "19451945aA@\n"
expect *aosp_arm-eng*
send "19\n"
expect eof ;



set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/system/app/MM/MM.apk $resultPath
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

EOF



adb root && adb remount && adb shell mount -o remount rw /system && adb push $resultPath/MM.apk  /system/app/MM && adb reboot