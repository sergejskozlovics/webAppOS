@echo off

if .%3==. goto usage
set MMD_FILE=%1
set MMD_NAME=%~n1
set MMD_NOEXT=%~dp1%~n1

call %~dp0mmd2ecore %MMD_FILE%

set PSCMD="(gc %MMD_NOEXT%.ecore) -replace 'name=\"root\"', 'name=\"%MMD_NAME%\"' ^| Out-File -encoding UTF8 %MMD_NOEXT%.ecore"
powershell -Command "%PSCMD%"
set PSCMD="(gc %MMD_NOEXT%.ecore) -replace 'nsPrefix=\"\"', 'nsPrefix=\"%3\"' ^| Out-File -encoding UTF8 %MMD_NOEXT%.ecore"
powershell -Command "%PSCMD%"
del %MMD_NOEXT%.xmi
del %MMD_NOEXT%.xmi_refs

:: Converting .ecore to Java classes; the package name will be taken from the nsPrefix XML attribute
:: in the .ecore file. The factory name will be based on the name XML attribute.

call %~dp0ecore2java %MMD_NOEXT%.ecore %2
pause
goto end

:usage
echo Usage: %0 SomeName.mmd target\path java.package.name
goto end

:end
