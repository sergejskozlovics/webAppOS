@echo off

call %~dp0..\..\bin\mmd2java %~dp0HelloWorldMetamodel.mmd %~dp0src org.webappos.apps.helloworld.mm
