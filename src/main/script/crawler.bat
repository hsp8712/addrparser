@echo off

cd /d %~dp0
cd ..
set "PRJ_HOME=%cd%"

setlocal EnableDelayedExpansion
set CLASSPATH=%PRJ_HOME%\conf
for /F %%j in ('dir /s/b lib\*.jar') do set CLASSPATH=!CLASSPATH!;%%j

java -Dfile.encoding=utf-8 -classpath %CLASSPATH% tech.spiro.addrparser.tool.CrawlerServer %*