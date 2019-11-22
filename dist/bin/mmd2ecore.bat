@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  mmd2ecore startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and MMD2ECORE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\sys\bin;%APP_HOME%\lib\sys.jar;%APP_HOME%\lib\guava-27.1-jre.jar;%APP_HOME%\lib\log4j-slf4j-impl-2.11.2.jar;%APP_HOME%\lib\acme4j-utils-1.1.jar;%APP_HOME%\lib\acme4j-client-1.1.jar;%APP_HOME%\lib\jose4j-0.6.4.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\log4j-core-2.11.2.jar;%APP_HOME%\lib\log4j-api-2.11.2.jar;%APP_HOME%\lib\fcgi-server-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-webapp-9.4.14.v20181114.jar;%APP_HOME%\lib\websocket-server-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-servlet-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-security-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-server-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-proxy-9.4.14.v20181114.jar;%APP_HOME%\lib\fcgi-client-9.4.14.v20181114.jar;%APP_HOME%\lib\websocket-client-9.4.14.v20181114.jar;%APP_HOME%\lib\websocket-common-9.4.14.v20181114.jar;%APP_HOME%\lib\websocket-servlet-9.4.14.v20181114.jar;%APP_HOME%\lib\websocket-api-9.4.14.v20181114.jar;%APP_HOME%\lib\javafx-fxml-11.0.2-mac.jar;%APP_HOME%\lib\javafx-web-11.0.2-mac.jar;%APP_HOME%\lib\javafx-controls-11.0.2-mac.jar;%APP_HOME%\lib\javafx-controls-11.0.2.jar;%APP_HOME%\lib\javafx-media-11.0.2-mac.jar;%APP_HOME%\lib\javafx-media-11.0.2.jar;%APP_HOME%\lib\javafx-swing-11.0.2-mac.jar;%APP_HOME%\lib\javafx-graphics-11.0.2-mac.jar;%APP_HOME%\lib\javafx-graphics-11.0.2.jar;%APP_HOME%\lib\javafx-base-11.0.2-mac.jar;%APP_HOME%\lib\javafx-base-11.0.2.jar;%APP_HOME%\lib\cloudrail-si-java-2.21.11.jar;%APP_HOME%\lib\javax.mail-1.6.2.jar;%APP_HOME%\lib\commons-fileupload-1.4.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\commons-text-1.8.jar;%APP_HOME%\lib\commons-lang3-3.9.jar;%APP_HOME%\lib\org.apache.commons.codec-1.8.jar;%APP_HOME%\lib\lightcouch-0.2.0.jar;%APP_HOME%\lib\gson-2.8.5.jar;%APP_HOME%\lib\jettison-1.4.0.jar;%APP_HOME%\lib\bcpkix-jdk15on-1.60.jar;%APP_HOME%\lib\bcprov-jdk15on-1.60.jar;%APP_HOME%\lib\org.eclipse.emf.ecore.xmi-2.15.0.jar;%APP_HOME%\lib\org.eclipse.emf.ecore-2.15.0.jar;%APP_HOME%\lib\org.eclipse.emf.common-2.15.0.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.5.2.jar;%APP_HOME%\lib\error_prone_annotations-2.2.0.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.17.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\jetty-client-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-http-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-io-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-xml-9.4.14.v20181114.jar;%APP_HOME%\lib\jetty-util-9.4.14.v20181114.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\httpclient-4.5.3.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\httpcore-4.4.6.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\sys\bin

@rem Execute mmd2ecore
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %MMD2ECORE_OPTS%  -classpath "%CLASSPATH%" lv.lumii.tda.kernel.mmdparser.MMD2Ecore %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable MMD2ECORE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%MMD2ECORE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
