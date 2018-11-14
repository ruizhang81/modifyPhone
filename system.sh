#!/bin/sh

rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*.img
rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*.txt
rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*.id
rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*

adb reboot bootloader
export ANDROID_PRODUCT_OUT=/Users/zhangrui/Documents/code/modifyPhone/ZBuildResult

/usr/bin/expect <<EOF

set timeout -1
spawn scp -Cr admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/*.img /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/android-info.txt /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult
expect {
*password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/recovery.id /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult
expect {
*password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

EOF

export ANDROID_PRODUCT_OUT=/Users/zhangrui/Documents/code/modifyPhone/ZBuildResult
cd ZBuildResult
fastboot flashall -w

fastboot erase userdata

fastboot flash userdata /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/userdata.img

fastboot flash recovery /Users/zhangrui/Documents/code/modifyPhone/util/twrp-3.2.3-0-hammerhead.img

echo "请进入Recovery mode模式后按回车键..."
read -n 1

adb shell mkdir dir
adb shell chmod 777 dir

adb push /Users/zhangrui/Documents/code/modifyPhone/util/BETA-SuperSU-v2.64-20151220185127.zip dir
adb push /Users/zhangrui/Documents/code/modifyPhone/util/luckincoffee_25.apk dir
adb push /Users/zhangrui/Documents/code/modifyPhone/util/pingyin.apk dir
adb push /Users/zhangrui/Documents/code/modifyPhone/util/qqlite.apk dir
adb push /Users/zhangrui/Documents/code/modifyPhone/util/tantan.apk dir

echo "请按Install键,然后选择Download/BETA-SuperSU-v2安装"