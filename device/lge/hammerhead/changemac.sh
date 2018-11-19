#! /system/bin/sh

sleep 10

ifconfig wlan0 down

mac1=`cat /proc/sys/kernel/random/uuid| md5sum | cut -c1-2`
mac2=`cat /proc/sys/kernel/random/uuid| md5sum | cut -c1-2`
mac3=`cat /proc/sys/kernel/random/uuid| md5sum | cut -c1-2`
ifconfig wlan0 hw ether 8c:3a:e3:$mac1:$mac2:$mac3`

ifconfig wlan0 up