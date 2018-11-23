#! /system/bin/sh

macaddress=`cat /sys/class/net/wlan0/address`
if [ $macaddress = "8c:3a:e3:3d:44:cc" ]; then

    ifconfig wlan0 down

	sleep 1

	mac1=`cat /proc/sys/kernel/random/uuid| md5sum | cut -c1-2`
	mac2=`cat /proc/sys/kernel/random/uuid| md5sum | cut -c1-2`
	mac3=`cat /proc/sys/kernel/random/uuid| md5sum | cut -c1-2`
	ifconfig wlan0 hw ether 8c:3a:e3:$mac1:$mac2:$mac3

	sleep 10

	echo "changemac"

	ifconfig wlan0 up
fi




