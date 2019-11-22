@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  ecore2java startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and ECORE2JAVA_OPTS to pass JVM options to this script.
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

set CLASSPATH=%APP_HOME%\lib\ecore2java.jar;%APP_HOME%\lib\org.eclipse.core.runtime-3.15.200.jar;%APP_HOME%\lib\org.eclipse.core.contenttype-3.7.300.jar;%APP_HOME%\lib\org.eclipse.osgi-3.13.300.jar;%APP_HOME%\lib\org.eclipse.equinox.app-1.4.300.jar;%APP_HOME%\lib\org.eclipse.equinox.registry-3.8.300.jar;%APP_HOME%\lib\org.eclipse.core.jobs-3.10.500.jar;%APP_HOME%\lib\org.eclipse.equinox.preferences-3.7.500.jar;%APP_HOME%\lib\org.eclipse.equinox.common-3.10.300.jar;%APP_HOME%\lib\org.eclipse.emf.ecore.xmi-2.15.0.jar;%APP_HOME%\lib\org.eclipse.emf.ecore-2.15.0.jar;%APP_HOME%\lib\org.eclipse.emf.common-2.15.0.jar;%APP_HOME%\lib\guava-28.1-jre.jar;%APP_HOME%\lib\java-2.0.17-v201004271640.jar;%APP_HOME%\lib\org.eclipse.ocl.ecore-3.10.100.v20181210-1441.jar;%APP_HOME%\lib\org.eclipse.ocl.common-1.8.100.v20181210-1441.jar;%APP_HOME%\lib\org.eclipse.ocl-3.10.100.v20181210-1441.jar;%APP_HOME%\lib\org.eclipse.acceleo.engine-3.7.7.201812041426.jar;%APP_HOME%\lib\org.eclipse.acceleo.common-3.7.7.201812041426.jar;%APP_HOME%\lib\org.eclipse.acceleo.model-3.7.7.201812041426.jar;%APP_HOME%\lib\org.eclipse.acceleo.profiler-3.7.7.201812041426.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.8.1.jar;%APP_HOME%\lib\error_prone_annotations-2.3.2.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.18.jar

@rem Execute ecore2java
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %ECORE2JAVA_OPTS%  -classpath "%CLASSPATH%" ecore2java.main.Generate %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable ECORE2JAVA_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%ECORE2JAVA_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
