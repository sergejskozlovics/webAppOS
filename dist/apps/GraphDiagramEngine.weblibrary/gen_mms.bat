rd /s src\org\webappos\weblib\gde\mm
call ..\..\bin\mmd2java.bat GraphDiagramEngineMetamodel.mmd src org.webappos.weblib.gde.mm
rd /s src\org\webappos\weblib\gde\eemm
call ..\..\bin\ecore2java EEMM.ecore src
