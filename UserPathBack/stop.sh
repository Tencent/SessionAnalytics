#!/bin/bash

bashPath=/usr/local/app
trpcPath=/usr/local/application/bin
logFile=$bashPath/startScript.log
server=userpath-0.0.1-SNAPSHOT.jar

pidInfo=$(ps -ef | grep "$trpcPath/$server" | grep -v grep | awk '{print $2}')


for pid in $pidInfo;do
	kill -9 $pid
done

#check process num
num=$(ps -ef | grep "$trpcPath/$server" | grep -v grep |wc -l)

if [ $num -gt 0 ];then
	echo "`date` after stop process in force way the processNum is $num still bigger than 0">>$logFile
	exit 1
fi
exit 0