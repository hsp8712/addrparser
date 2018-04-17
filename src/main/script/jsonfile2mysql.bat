@echo off

set "WORK_DIR=%cd%"
cd /d %~dp0
cd ..
set "PRJ_HOME=%cd%"
cd /d %WORK_DIR%

setlocal EnableDelayedExpansion
set CLASSPATH=%PRJ_HOME%\conf
for /F %%j in ('dir /s/b %PRJ_HOME%\lib\*.jar') do set CLASSPATH=!CLASSPATH!;%%j

java -Dfile.encoding=utf-8 -classpath %CLASSPATH% tech.spiro.addrparser.tool.JSONFile2MySQL %*