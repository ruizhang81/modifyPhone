#!/bin/sh

appPath=$(cd `dirname $0`; pwd)
resultPath=../../../ZBuildResult


rm $resultPath/FMRadio.apk


/usr/bin/expect <<EOF


set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSource/packages/apps; rm -rf FMRadio"
expect *password*
send "19451945aA@\n"
expect eof ;


set timeout -1
spawn scp -Cr $appPath admin@172.17.10.25:/home/admin/androidSource/packages/apps/
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr $appPath/Android.mk admin@172.17.10.25:/home/admin/androidSource/packages/apps/FMRadio/
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSource; source build/envsetup.sh; lunch; mmm packages/apps/FMRadio"
expect *password*
send "19451945aA@\n"
expect *aosp_arm-eng*
send "19\n"
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/system/app/FMRadio/FMRadio.apk $resultPath
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

EOF



utilpath=../../../util
adb reboot bootloader

sleep 3

fastboot flash recovery $utilpath/twrp-3.2.3-0-hammerhead.img

echo "进入recovery mode  进入Mount勾上System  后按回车键"
read -n 1

adb push ~/.android/adb_keys /data/misc/adb/adb_keys
adb push $resultPath/FMRadio.apk  /system/app/FMRadio
adb reboot
