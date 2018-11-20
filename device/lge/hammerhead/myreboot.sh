#! /system/bin/sh

setprop dev.myreboot.enable 0

echo "myreboot"

rm -rf /storage/emulated/0/Android
rm -rf /storage/emulated/0/Mob
rm -rf /storage/emulated/0/libs
rm -rf /storage/emulated/0/Podcasts
rm -rf /storage/emulated/0/baidu
rm -rf /storage/emulated/0/TWRP
rm -rf /storage/self/primary/Mob
rm -rf /storage/self/primary/libs
rm -rf /storage/self/primary/Podcasts
rm -rf /storage/self/primary/baidu
rm -rf /storage/self/primary/TWRP
rm -rf /data/bugreports
rm -rf /data/ss
rm -rf /data/tombstones/*

pm uninstall com.lucky.luckyclient

reboot

