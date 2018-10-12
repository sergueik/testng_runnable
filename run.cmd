@echo OFF
REM NOTE: slow
echo standalone run

set DUMMYDIR=c:\temp\shared
rd /s/q %DUMMYDIR%
mkdir %DUMMYDIR%
call mvn -Psingle clean package
copy /y target\runnable_testng-jar-with-dependencies.jar %DUMMYDIR%
mkdir %DUMMYDIR%\target\classes
copy /y target\classes\Test.xlsx %DUMMYDIR%\target\classes
pushd %DUMMYDIR%
java -jar runnable_testng-jar-with-dependencies.jar
popd

CHOICE /T 1 /C ync /CS /D y	
REM pause "Press any Key"
echo clasic run

call mvn clean package
set DUMMYDIR=c:\temp\shared
rd /s/q %DUMMYDIR%
mkdir %DUMMYDIR%
robocopy target\lib %DUMMYDIR% /s
rd /s/q target\lib
java -cp %DUMMYDIR%\*;target\runnable_testng-0.2-SNAPSHOT.jar demo.EntryPoint

echo all done
goto :EOF