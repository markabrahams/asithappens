@echo off

rem Section 1: Location variables - change to suit your environment
set javaExe=C:\Program Files\Java\jre1.5.0_06\bin\java
set asithappensHome=C:\Program Files\asithappens

rem Section 2: System variables - do not change
set asithappensBin=%asithappensHome%\bin
set asithappensLib=%asithappensHome%\lib
set asithappensJar=%asithappensHome%\dist\asithappens.jar
set asithappensNative=%asithappensHome%\lib\native
set classpath=%asithappensJar%
set PATH=%PATH%;%asithappensBin%

rem Section 3: Start-up code
cd "%asithappensHome%"
for %%i in ("%asithappensLib%\*.jar") do call classpathadd.bat %%i
"%javaExe%" -cp "%classpath%" -Djava.library.path="%asithappensNative%" nz.co.abrahams.asithappens.mainui.AsItHappens
