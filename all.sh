#!/bin/sh


ttime=`date +"%Y-%m-%d %H:%M:%S"`
echo "编译开始时间 "$ttime

./compile.sh

./sign.sh

./download.sh

./fire.sh