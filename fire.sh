#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
resultPath=$basepath/ZBuildResult
utilpath=$basepath/util
export ANDROID_PRODUCT_OUT=$resultPath



adb reboot bootloader
# echo "1、请进入bootload界面"
# read -n 1

sleep 3

fastboot erase system -w

cd ZBuildResult
fastboot flashall -w

fastboot erase userdata

fastboot flash userdata $resultPath/userdata.img

fastboot flash recovery $utilpath/twrp-3.2.3-0-hammerhead.img

# fastboot oem unlock
# fastboot oem lock

echo "1、选择recovery mode 进入twrp后按回车键"
read -n 1

adb push ~/.android/adb_keys /data/misc/adb/adb_keys

adb reboot


echo "2、重启进入桌面,并打开调试模式后..."
read -n 1


adb install $utilpath/luckincoffee_25.apk
adb install $utilpath/pingyin.apk
adb install $utilpath/qqlite.apk

adb push $utilpath/luckincoffee_25.apk /storage/emulated/0/Download

# adb push $utilpath/tantan.apk sdcard/Download


#grep _t * “or exists select 1 from xxx_t e where a.event_type_id = e.event_type_id”

