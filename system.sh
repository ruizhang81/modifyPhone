#!/bin/sh


rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*.img
rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*.txt
rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*.id
rm /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/*


adb reboot bootloader
export ANDROID_PRODUCT_OUT=$basepath/ZBuildResult

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
fastboot flashall -w

fastboot erase userdata

fastboot flash userdata /Users/zhangrui/Documents/code/modifyPhone/ZBuildResult/userdata.img

echo "请按电源键启动后---等待进入桌面后按回车键..."
read -n 1

adb reboot bootloader

sleep 4

fastboot flash recovery /Users/zhangrui/Documents/code/modifyPhone/util/twrp-3.2.3-0-hammerhead.img

echo "请进入Recovery mode模式后按回车键..."
read -n 1

adb push /Users/zhangrui/Documents/code/modifyPhone/util/BETA-SuperSU-v2.64-20151220185127.zip sdcard/Download
adb push /Users/zhangrui/Documents/code/modifyPhone/util/luckincoffee_25.apk sdcard/Download
adb push /Users/zhangrui/Documents/code/modifyPhone/util/pingyin.apk sdcard/Download
adb push /Users/zhangrui/Documents/code/modifyPhone/util/qqlite.apk sdcard/Download
adb push /Users/zhangrui/Documents/code/modifyPhone/util/tantan.apk sdcard/Download

echo "请按Install键,然后选择Download/BETA-SuperSU-v2安装"



