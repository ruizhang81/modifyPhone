#!/bin/sh

rm ~/bin
mkdir ~/bin
PATH=~/bin:$PATH
curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo

export REPO_URL='https://mirrors.tuna.tsinghua.edu.cn/git/git-repo/'

repo init -u https://aosp.tuna.tsinghua.edu.cn/platform/manifest -b android-6.0.1_r77
repo sync