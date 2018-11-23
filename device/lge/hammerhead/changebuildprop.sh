#! /system/bin/sh


if [  -f "/sdcard/Download/build.prop" ]; then
   echo "changebuildprop"
   mv -f /sdcard/Download/build.prop /system
fi



