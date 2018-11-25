#!/bin/sh



/usr/bin/expect <<EOF 

set timeout -1
spawn ssh admin@172.17.10.25 "rm /home/admin/androidSourceKernel/msm/arch/arm/boot/zImage-dtb"
expect {
    *password* { send "19451945aA@\r" }
};
expect eof ;

set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSourceKernel/msm; export PATH=/home/admin/androidSource/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:$PATH; export ARCH=arm; export SUBARCH=arm; export CROSS_COMPILE=arm-eabi-; make hammerhead_defconfig; sed -i  's/CONFIG_LOCALVERSION_AUTO=y/# CONFIG_LOCALVERSION_AUTO is not set/' /home/admin/androidSourceKernel/msm/.config; make -j4"
expect {
    *password* { send "19451945aA@\r" }
};
expect eof ;


set timeout -1
spawn ssh admin@172.17.10.25 "mv -f /home/admin/androidSourceKernel/msm/arch/arm/boot/zImage-dtb /home/admin/androidSource/device/lge/hammerhead-kernel"
expect {
    *password* { send "19451945aA@\r" }
};
expect eof ;


set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSource; make clobber; source build/envsetup.sh; make -j4 PRODUCT-aosp_hammerhead-user dist"
expect {
    *password* { send "19451945aA@\r" }
};
expect eof ;

EOF
