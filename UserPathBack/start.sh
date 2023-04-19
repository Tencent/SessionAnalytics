#!/bin/bash

bashPath=/usr/local/app
trpcPath=/usr/local/application/bin
logFile=$bashPath/startScript.log
server=userpath-0.0.1-SNAPSHOT.jar

source /etc/profile

if [ ! -d $bashPath ];then
    mkdir -p $bashPath
fi

chmod a+x $trpcPath//$server
# config spring profiles active
sumeru_env=`env | grep -i SUMERU_ENV`
if [ $sumeru_env == "SUMERU_ENV=formal" ];then
    springProfiles=prod
elif [ $sumeru_env == "SUMERU_ENV=pre" ];then
    springProfiles=pre
elif [ $sumeru_env == "SUMERU_ENV=test" ];then
    springProfiles=test
fi
nohup java -jar -Dserver.port=$main_port -Dspring.profiles.active=$springProfiles  $trpcPath/$server 2>&1 | tee  $trpcPath/server.log >> $trpcPath/server.log 2>&1 &

sleep 3
num=`ps -ef |grep $trpcPath/$server |grep -v grep|wc -l`
echo "the num of process after start is $num">>$logFile
if [ $num -lt 1 ];then
    echo "the process is not exit after start ">>$logFile
    exit 1
fi

exit 0
