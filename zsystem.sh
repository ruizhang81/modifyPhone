#!/bin/sh



rm /Users/zhangrui/Downloads/hammerhead/*.img
rm /Users/zhangrui/Downloads/hammerhead/*.txt
rm /Users/zhangrui/Downloads/hammerhead/*.id
adb reboot bootloader
export ANDROID_PRODUCT_OUT=/Users/zhangrui/Downloads/hammerhead

/usr/bin/expect <<EOF 

set timeout -1
spawn scp -Cr admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/*.img /Users/zhangrui/Downloads/hammerhead
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/android-info.txt /Users/zhangrui/Downloads/hammerhead
expect {
*password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/recovery.id /Users/zhangrui/Downloads/hammerhead
expect {
*password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;


EOF

fastboot flashall -w

fastboot erase userdata

fastboot flash userdata userdata.img

echo "请按电源键启动后按回车键..."
read -n 1

echo "进入桌面后请按回车键..."
read -n 1

adb reboot bootloader

sleep 4

fastboot flash recovery /Users/zhangrui/Downloads/ni/twrp-3.2.3-0-hammerhead.img

echo "请进入Recovery mode模式后按回车键..."
read -n 1

adb push /Users/zhangrui/Downloads/ni/BETA-SuperSU-v2.64-20151220185127.zip sdcard/Download
adb push /Users/zhangrui/Downloads/ni/luckincoffee_25.apk sdcard/Download
adb push /Users/zhangrui/Downloads/ni/pingyin.apk sdcard/Download
adb push /Users/zhangrui/Downloads/ni/qqlite.apk sdcard/Download
adb push /Users/zhangrui/Downloads/ni/tantan.apk sdcard/Download

echo "请按Install键,然后选择Download/BETA-SuperSU-v2安装"



