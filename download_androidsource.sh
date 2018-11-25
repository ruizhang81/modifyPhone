#!/bin/sh

rm ~/bin
mkdir ~/bin
PATH=~/bin:$PATH
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo

export REPO_URL='https://mirrors.tuna.tsinghua.edu.cn/git/git-repo/'

repo init -u https://aosp.tuna.tsinghua.edu.cn/platform/manifest -b android-6.0.1_r77
repo sync

# git clone https://aosp.tuna.tsinghua.edu.cn/kernel/msm.git
# git clone https://aosp.tuna.tsinghua.edu.cn/kernel/goldfish.git


export PATH=/home/admin/androidSource/prebuilts/gcc/linux-x86/arm/arm-eabi-4.8/bin:$PATH
export ARCH=arm
export SUBARCH=arm
export CROSS_COMPILE=arm-eabi-
make hammerhead_defconfig
make -j4

#备份
mv /home/admin/androidSource/device/lge/hammerhead-kernel/zImage-dtb /home/admin 
#移动
mv -f /home/admin/androidSourceKernel/msm/arch/arm/boot/zImage-dtb /home/admin/androidSource/device/lge/hammerhead-kernel
# git clone https://aosp.tuna.tsinghua.edu.cn/kernel/msm.git
# /home/admin/androidSourceKernel/msm/arch/arm/boot/zImage/zImage-dtb

# wl_android.c:int wifi_get_mac_addr(unsigned char *buf)

/home/admin/androidSourceKernel/msm/drivers/net/wireless/bcmdhd
remove -DSET_RANDOM_MAC_SOFTAP

# CONFIG_LOCALVERSION_AUTO is not set
CONFIG_LOCALVERSION_AUTO=y // 802


a1="CONFIG_LOCALVERSION_AUTO=y"
a2="# CONFIG_LOCALVERSION_AUTO is not set"
sed -i '' 's/$a1/$a2/'



scripts/mkcompile_h
echo \#define UTS_VERSION \"`echo $UTS_VERSION | $UTS_TRUNCATE`\"

