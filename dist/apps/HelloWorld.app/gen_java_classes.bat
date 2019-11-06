@echo off

:: If you wish to use .mmd syntax for the metamodel, then you have to
:: convert it to .ecore. After the conversion please modify the name and nsPrefix XML attributes.
:: To do that, uncomment the 7 lines below.

::call %~dp0..\..\bin\mmd2ecore %~dp0HelloWorld.mmd
::set PSCMD="(gc %~dp0HelloWorld.ecore) -replace 'name=\"root\"', 'name=\"HelloWorld\"' ^| Out-File -encoding UTF8 %~dp0HelloWorld.ecore"
::powershell -Command "%PSCMD%"
::set PSCMD="(gc %~dp0HelloWorld.ecore) -replace 'nsPrefix=\"\"', 'nsPrefix=\"org.webappos.apps.helloworld.mm\"' ^| Out-File -encoding UTF8 %~dp0HelloWorld.ecore"
::powershell -Command "%PSCMD%"
::del %~dp0HelloWorld.xmi
::del %~dp0HelloWorld.xmi_refs

:: Converting .ecore to Java classes; the package name will be taken from the nsPrefix XML attribute
:: in the .ecore file. The factory name will be based on the name XML attribute.

call %~dp0..\..\bin\ecore2java %~dp0HelloWorld.ecore %~dp0src
pause
