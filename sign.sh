
#!/bin/sh



/usr/bin/expect <<EOF




set timeout -1
spawn ssh admin@172.17.10.25 "cd /home/admin/androidSource; /home/admin/androidSource/build/tools/releasetools/sign_target_files_apks -d /home/admin/androidSource/vendor/Modul/security/product_modul/ /home/admin/androidSource/out/dist/aosp_hammerhead-target_files-eng.admin.zip /home/admin/androidSource/out/dist/signed_target_files.zip"
expect *password*
send "19451945aA@\n"
expect eof ;



set timeout -1
spawn ssh admin@172.17.10.25 "/home/admin/androidSource/build/tools/releasetools/img_from_target_files  /home/admin/androidSource/out/dist/signed_target_files.zip /home/admin/androidSource/out/dist/signed-img.zip"
expect *password*
send "19451945aA@\n"
expect eof ;


EOF

# https://blog.csdn.net/chen_chun_guang/article/details/6325833
# ./make_key releasekey '/C=CN/ST=Beijing/L=Beijing/O=oo-oo/OU=product/CN=oo-oo/emailAddress=ruizhang81@gmail.com'
# /home/admin/androidSource/build/tools/releasetools/img_from_target_files  /home/admin/androidSource/out/dist/signed_target_files.zip /home/admin/androidSource/out/dist/signed-img.zip