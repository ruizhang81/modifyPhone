#!/bin/sh


rm -rf /Users/zhangrui/Documents/code/modifyPhone/packages/apps/MM/app/build



/usr/bin/expect <<EOF 

set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSource/packages/apps; rm -rf MM"
expect *password*
send "19451945aA@\n"
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/build admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/external admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/frameworks admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/device admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/packages admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/system admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;

set timeout -1
spawn scp -Cr /Users/zhangrui/Documents/code/modifyPhone/vendor admin@172.17.10.25:/home/admin/androidSource
expect {
    *password* { send "19451945aA@\r" }
};
expect 100%
expect eof ;


set timeout -1
spawn ssh admin@172.17.10.25 "cd androidSource; make clobber; source build/envsetup.sh; make -j4 PRODUCT-aosp_hammerhead-user dist"
expect {
    *password* { send "19451945aA@\r" }
};
expect eof ;

EOF
