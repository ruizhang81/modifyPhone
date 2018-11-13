#!/bin/sh


cp -rf /Users/zhangrui/Documents/code/MM/app/src /Users/zhangrui/Downloads/ni/modify/packages/apps/MM/app

/usr/bin/expect <<EOF 


set timeout -1
spawn scp -Cr /Users/zhangrui/Downloads/ni/modify/packages/apps/MM/app/src admin@172.17.10.25:/home/admin/androidSource/packages/apps/MM/app/
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
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/system/app/MM/MM.apk /Users/zhangrui/Downloads/hammerhead/apk/
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

EOF

adb root && adb remount && adb shell mount -o remount rw /system && adb push /Users/zhangrui/Downloads/hammerhead/apk/MM.apk  /system/app/MM && adb reboot