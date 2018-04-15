#!/bin/bash

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd $bin; cd ..; pwd`
PRJ_HOME=$bin

CLASSPATH=$PRJ_HOME/conf

for f in $PRJ_HOME/lib/*.jar; do
  if [ "$CLASSPATH" ]; then
    CLASSPATH=$CLASSPATH:$f
  fi
done

java -Dfile.encoding=utf-8 -classpath $CLASSPATH tech.spiro.addrparser.tool.CrawlerServer $@
