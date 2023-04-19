#!/bin/bash

bashPath=/usr/local/app
trpcPath=/usr/local/application/bin
logFile=$bashPath/startScript.log
server=userpath-0.0.1-SNAPSHOT.jar

num=`ps -ef |grep $trpcPath/$server |grep -v grep|wc -l`

echo "`date` the num of process is $num">>$logFile
if [ $num -lt 1 ];then
    exit 1
fi

exit 0