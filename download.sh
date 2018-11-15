#!/bin/sh

basepath=$(cd `dirname $0`; pwd)
resultPath=$basepath/ZBuildResult
utilpath=$basepath/util

echo $resultPath
echo $utilpath

rm $resultPath/*.img
rm $resultPath/*.txt
rm $resultPath/*.id
rm $resultPath/*

/usr/bin/expect <<EOF

set timeout -1
spawn scp -Cr admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/*.img $resultPath
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/android-info.txt $resultPath
expect {
*password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp admin@172.17.10.25:/home/admin/androidSource/out/target/product/hammerhead/recovery.id $resultPath
expect {
*password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

EOF

echo "下载完成！"