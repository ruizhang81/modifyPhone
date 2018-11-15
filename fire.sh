#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
resultPath=$basepath/ZBuildResult
utilpath=$basepath/util

adb reboot bootloader
export ANDROID_PRODUCT_OUT=$resultPath

sleep 5

cd ZBuildResult
fastboot flashall -w

fastboot erase userdata

fastboot flash userdata $resultPath/userdata.img

fastboot flash recovery $utilpath/twrp-3.2.3-0-hammerhead.img

echo "1、请进入Recovery mode模式后按回车键..."
read -n 1

adb shell mkdir dir
adb shell chmod 777 dir

adb push $utilpath/BETA-SuperSU-v2.64-20151220185127.zip dir

echo "2、请按Install键,然后选择dir/BETA-SuperSU-v2安装，等待安装完重启到桌面"
read -n 1

adb push $utilpath/luckincoffee_25.apk sdcard/Download
adb push $utilpath/pingyin.apk sdcard/Download
adb push $utilpath/qqlite.apk sdcard/Download
adb push $utilpath/tantan.apk sdcard/Download

echo "3、请连接wifi"
read -n 1

adb reboot
echo "安装完成,重启中..."