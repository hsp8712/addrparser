#!/bin/bash

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd $bin; cd ..; pwd`
PRJ_HOME=$bin

LOG_HOME=$PRJ_HOME/logs

if [ ! -d $LOG_HOME ]; then
    mkdir -p $LOG_HOME
fi

CLASSPATH=$PRJ_HOME/conf

for f in $PRJ_HOME/lib/*.jar; do
  if [ "$CLASSPATH" ]; then
    CLASSPATH=$CLASSPATH:$f
  fi
done

for f in $PRJ_HOME/dist/*.jar; do
  if [ "$CLASSPATH" ]; then
    CLASSPATH=$CLASSPATH:$f
  fi
done

nohup java -classpath $CLASSPATH -Dlogging.home=$LOG_HOME \
    cn.com.tiza.earth4j.crawler.CrawlerServer $@ > $LOG_HOME/app.log 2>&1 &
