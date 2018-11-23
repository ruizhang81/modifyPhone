#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
resultPath=$basepath/ZBuildResult
utilpath=$basepath/util
export ANDROID_PRODUCT_OUT=$resultPath

sleep 3

adb reboot bootloader
# echo "1、请进入bootload界面"
# read -n 1

fastboot erase system -w

cd ZBuildResult
fastboot flashall -w

fastboot erase userdata

fastboot flash userdata $resultPath/userdata.img

fastboot flash recovery $utilpath/twrp-3.2.3-0-hammerhead.img

echo "2、选择recovery mode 进入twrp后按回车键"
read -n 1

adb push ~/.android/adb_keys /data/misc/adb/adb_keys

adb reboot

# adb push $utilpath/luckincoffee_25.apk sdcard/Download
# adb push $utilpath/pingyin.apk sdcard/Download
# adb push $utilpath/qqlite.apk sdcard/Download
# adb push $utilpath/tantan.apk sdcard/Download

echo "3、安装完成,重启中..."




