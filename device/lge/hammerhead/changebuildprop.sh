#! /system/bin/sh

setprop dev.changebuildprop.enable 0

echo "changebuildprop"

cp -f /sdcard/Download/build.prop /system
