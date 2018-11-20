#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
resultPath=$basepath/ZBuildResult
utilpath=$basepath/util

echo $resultPath
echo $utilpath


rm $resultPath/*

/usr/bin/expect <<EOF

set timeout -1
spawn scp -Cr admin@172.17.10.25:/home/admin/androidSource/out/dist/signed-img.zip $resultPath
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

EOF

unzip $resultPath/signed-img.zip -d $resultPath

echo "下载完成！"