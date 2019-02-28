#!/bin/sh


ttime=`date +"%Y-%m-%d %H:%M:%S"`
echo "1编译开始时间 "$ttime

./upload.sh

./compile.sh

./sign.sh

./download.sh

./fire.sh