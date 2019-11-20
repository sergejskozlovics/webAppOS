package org.webappos.project;

import lv.lumii.tda.util.ZipFolder;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.IRepository;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.webappos.server.ConfigStatic;

public class LegacyToCloudProjectConverter {
	
	
	private static boolean DEBUG = !false;
	
/*	public static void readln()
	{
		if (!DEBUG)
			return;
		System.out.println("Press Enter...");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}*/
	
	public static String getLegacyToolName(String legacyProjectDir) {
		File fXmlFile = new File(legacyProjectDir + File.separator + "project.grt");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (Throwable t) {
			return null;
		}
	 
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
	 
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		String toolName = null;
		
		NodeList nList = doc.getElementsByTagName("Project");
		if ((nList != null) && (nList.getLength()>0) && (nList.item(0)instanceof Element)) {
			nList = ((Element)nList.item(0)).getElementsByTagName("Tools");
			if ((nList != null) && (nList.getLength()>0) && (nList.item(0)instanceof Element)) {
				nList = ((Element)nList.item(0)).getElementsByTagName("Name");
				if ((nList != null) && (nList.getLength()>0) && (nList.item(0)instanceof Element)) {
					toolName = nList.item(0).getTextContent();
				}
			}
		}
		
		return toolName;		
	}
	
	private static void cleanup(String legacyProjectDir, String targetDir, boolean ok)
	{
		if (ok)	{
			String s = targetDir;
			
			new File(s+File.separator+"metamodel.xml").delete();
			new File(s+File.separator+"data.xml").delete();
			new File(s+File.separator+"metamodel.xml.gz").delete();
			new File(s+File.separator+"data.xml.gz").delete();			
			new File(s+File.separator+"engines.txt").delete();			
			new File(s+File.separator+"project.grt").delete();			
			new File(s+File.separator+"framework.version").delete();			
			new File(s+File.separator+"tmp_version.lua").delete();			
		}
		else {
			if (!legacyProjectDir.equals(targetDir)) {
				try {
					File f = new File(targetDir);				
					FileUtils.deleteDirectory(f);
				} catch (IOException e) {
				}
			}
		}		
	}
	
	private static String getTargetDir(String legacyProjectDirOrFile) {
		if (legacyProjectDirOrFile.endsWith("_legacy")) {
			return legacyProjectDirOrFile.substring(0, legacyProjectDirOrFile.length()-7)+"_ar"; // JR-AR
		}
		else {
			return legacyProjectDirOrFile+"_ar";
		}		
	}
	
	/**
	 * Converts the given TDA 1.0 folder (or zipped folder) into a TDA2Web Cloud AR folder.
	 * @param legacyProjectDirOrFile the folder path of a TDA 1.0 project, or the name of a zipped file
	 * @param targetDir the name of the target output folder (if null, the name will be generated automatically)
	 * @return the name of the output _ar folder, or null on error
	 */
	public static String convert(String legacyProjectDirOrFile, String targetDir)
	{
		if (!System.getProperty("os.name").contains("Windows")) {
			System.err.println("Legacy projects can be converted only in Windows.");
			return null;			
		}
		
		if (!"32".equals(System.getProperty("sun.arch.data.model"))) {
			System.out.println("A 32-bit Java Virtual Machine is required to convert legacy projects.");
			return null;
		/*	System.out.println("Not 32-bit JVM. Executing another JVM...");
			// TODO: COMSPEC
			String cmdLine = ConfigStatic.BIN_DIR+File.separator+"legacy2cloud.bat \""+legacyProjectDirOrFile+"\"";
			System.out.println("Command line: "+cmdLine);
			try {
				Process p = Runtime.getRuntime().exec(cmdLine);
				p.waitFor();
			} catch (Throwable e) {
				e.printStackTrace();
				return null;
			}
			System.out.println("JVM done.");
			
			if (targetDir == null)
				targetDir = getTargetDir(legacyProjectDirOrFile);
			
			if (new File(targetDir).exists())
				return targetDir;
			else
				return null;*/
		}
				
		File ff = new File(legacyProjectDirOrFile);
		String legacyProjectDir;
		
		if (!ff.exists())
			return null;
		
		File fLegacyFolder = null;
		
		File f;
	try {
		
		if (ff.isFile()) {
			String legacyFolder = legacyProjectDirOrFile+"_legacy";
			
			fLegacyFolder = new File(legacyFolder);
			
			if (fLegacyFolder.exists() && fLegacyFolder.isDirectory()) {
				try {
					FileUtils.deleteDirectory(fLegacyFolder);
				} catch (IOException e) {
					return null;
				}
			}
			if (!ZipFolder.unzip(new File(legacyProjectDirOrFile), fLegacyFolder.toPath()))
				return null;
			
			String toolName = getLegacyToolName(legacyFolder);
			if (DEBUG) System.out.println("toolName="+toolName);
			if (toolName == null) {
				return null;
			}
			String name = ff.getName();
			int i = name.indexOf('.');
			if (i>=0)
				name = name.substring(0,i);
			legacyProjectDir = ff.getParent()+File.separator+name+"."+/*Dirs.getToolExtension(toolName)*/toolName.toLowerCase()+"_legacy";
			fLegacyFolder.renameTo(new File(legacyProjectDir));
			fLegacyFolder = new File(legacyProjectDir);
			if (DEBUG) System.out.println("legacyProjectDir="+legacyProjectDir);			
		}
		else
			legacyProjectDir = legacyProjectDirOrFile;
		
		System.out.println("DIR="+legacyProjectDir);
		
		if (targetDir == null)
			targetDir = getTargetDir(legacyProjectDirOrFile);
		
		f = new File(targetDir);
		if (!legacyProjectDir.equals(targetDir)) {
			try {
				if (f.exists() && f.isDirectory())
					FileUtils.deleteDirectory(f);
				FileUtils.copyDirectory(new File(legacyProjectDir), f);
			} catch (IOException e) {
				return null;
			}
		}
		
		
		boolean b;
		//TDAKernel k1 = new TDAKernel();
		IRepository k1 = TDAKernel.newRepositoryAdapter("mii_rep");
		if (k1 == null) {
			System.err.println("Error: could not initialize a repository adapter for mii_rep.");
			return null;			
		}
		
		if (!k1.exists(legacyProjectDir)) {
			System.err.println("Error: the source project cannot be found in "+legacyProjectDir+".");
			return null;
		}
			
		b = k1.open(legacyProjectDir);
		if (!b) {
			System.err.println("Could not open the legacy project in "+legacyProjectDir);
			return null;
		}
		
		String retStr = k1.callSpecificOperation("renameRole", "Node\u001EchangeParentEvent\u001EchangeParentEventN");
		if (retStr == null)
			System.err.println("renameRole for duplicate Node.changeParentEvent(N) failed");		
		if (DEBUG) System.out.println("renameRole for duplicate Node.changeParentEvent(N) returned "+retStr);

		retStr = k1.callSpecificOperation("renameRole", "Node\u001EchangeParentEvent\u001EchangeParentEventT");
		if (retStr == null)
			System.err.println("renameRole for duplicate Node.changeParentEvent(T) failed");		
		if (DEBUG) System.out.println("renameRole for duplicate Node.changeParentEvent(T) returned "+retStr);
		
		retStr = k1.callSpecificOperation("renameRole", "ClassDgrM#AssocEnd\u001Eassoc\u001Eassoc1");
		if (retStr == null)
			System.err.println("renameRole for duplicate ClassDgrM#AssocEnd.assoc failed");
		if (DEBUG) System.out.println("renameRole for duplicate ClassDgrM#AssocEnd.assoc returned "+retStr);

		retStr = k1.callSpecificOperation("renameRole", "Element\u001EnewLineEvent\u001EnewLineEventS");
		if (retStr == null)
			System.err.println("renameRole for duplicate Element.newLineEvent(S) failed");
		if (DEBUG) System.out.println("renameRole for duplicate Element.newLineEvent(S) returned "+retStr);

		retStr = k1.callSpecificOperation("renameRole", "Element\u001EnewLineEvent\u001EnewLineEventE");
		if (retStr == null)
			System.err.println("renameRole for duplicate Element.newLineEvent(E) failed");
		if (DEBUG) System.out.println("renameRole for duplicate Element.newLineEvent(E) returned "+retStr);
		
		retStr = k1.callSpecificOperation("renameRole", "Element\u001EmoveLineStartPointEvent\u001EmoveLineStartPointEventT");
		if (retStr == null)
			System.err.println("renameRole for duplicate Element.moveLineStartPointEvent(T) failed");
		if (DEBUG) System.out.println("renameRole for duplicate Element.moveLineStartPointEvent(T) returned "+retStr);

		retStr = k1.callSpecificOperation("renameRole", "Element\u001EmoveLineEndPointEvent\u001EmoveLineEndPointEventT");
		if (retStr == null)
			System.err.println("renameRole for duplicate Element.moveLineEndPointEvent(T) failed");
		if (DEBUG) System.out.println("renameRole for duplicate Element.moveLineEndPointEvent(T) returned "+retStr);
		
		retStr = k1.callSpecificOperation("renameRole", "Edge\u001EmoveLineStartPointEvent\u001EmoveLineStartPointEventE");
		if (retStr == null)
			System.err.println("renameRole for duplicate Edge.moveLineStartPointEvent(E) failed");
		if (DEBUG) System.out.println("renameRole for duplicate Edge.moveLineStartPointEvent(E) returned "+retStr);		
		
		retStr = k1.callSpecificOperation("renameRole", "Edge\u001EmoveLineEndPointEvent\u001EmoveLineEndPointEventE");
		if (retStr == null)
			System.err.println("renameRole for duplicate Edge.moveLineEndPointEvent(E) failed");
		if (DEBUG) System.out.println("renameRole for duplicate Edge.moveLineEndPointEvent(E) returned "+retStr);
		
		retStr = k1.callSpecificOperation("renameClass", "Command\u001ETDAKernel::Command");
		if (retStr == null)
			System.err.println("renameClass for Command failed");
		if (DEBUG) System.out.println("renameClass for Command returned "+retStr);
		
		retStr = k1.callSpecificOperation("renameClass", "Event\u001ETDAKernel::Event");
		if (retStr == null)
			System.err.println("renameClass for Event failed");
		if (DEBUG) System.out.println("renameClass for Event returned "+retStr);
		
		k1.deleteObject(k1.resolveIteratorFirst(k1.getIteratorForDirectClassObjects(k1.findClass("TreeEngine"))));
		k1.deleteObject(k1.resolveIteratorFirst(k1.getIteratorForDirectClassObjects(k1.findClass("GraphDiagramEngine"))));
		
		
		/*retStr = k1.callSpecificOperation("changeAttributeType", "GraphDiagramStyle\u001EbkgColor\u001EString");
		System.out.println("changeAttributeType for GraphDiagramStyle.bkgColor returned "+retStr);
		retStr = k1.callSpecificOperation("changeAttributeType", "GraphDiagram\u001EbkgColor\u001EString");
		System.out.println("changeAttributeType for GraphDiagram.bkgColor returned "+retStr);*/
				
		
//		long r = k1.findClass("FirstCmdPtr");
		//b = k1.deleteAssociation(k1.findAssociationEnd(r, "command"));
		//System.out.println("delete FirstCmdPtr.command = "+b);
		//b = k1.deleteClass(r);
		//System.out.println("delete FirstCmdPtr = "+b);
		
	//	r = k1.findClass("LastCmdPtr");
		//b = k1.deleteAssociation(k1.findAssociationEnd(r, "command"));
		//System.out.println("delete LastCmdPtr.command = "+b);
		
		long it = k1.getIteratorForAllClassObjects(k1.findClass("TDAKernel::Command"));
		long r = k1.resolveIteratorFirst(it);
		while (r!=0) {
			long it2 = k1.getIteratorForDirectObjectClasses(r);
			long rCls = k1.resolveIteratorFirst(it2);
			k1.freeIterator(it2);
			if (DEBUG) System.out.println("deleting object of type "+k1.getClassName(rCls)+" = "+k1.deleteObject(r));			
			r = k1.resolveIteratorNext(it);
		}
		k1.freeIterator(it);


		it = k1.getIteratorForAllClassObjects(k1.findClass("TDAKernel::Event"));
		r = k1.resolveIteratorFirst(it);
		while (r!=0) {
			long it2 = k1.getIteratorForDirectObjectClasses(r);
			long rCls = k1.resolveIteratorFirst(it2);
			k1.freeIterator(it2);
			if (DEBUG) System.out.println("deleting object of type "+k1.getClassName(rCls)+" = "+k1.deleteObject(r));			
			r = k1.resolveIteratorNext(it);
		}
		k1.freeIterator(it);
		
		TDAKernel k2 = new TDAKernel();
		
		String targetLocation = IProject.DEFAULT_REPOSITORY+":"+targetDir;
		
		if (k2.exists(targetLocation))
			k2.drop(targetLocation);
		
		b = k2.open(targetLocation);
		if (!b) {
			System.err.println("Could not create target repository "+targetLocation);
			k1.close();
			cleanup(legacyProjectDir, targetDir, false);
			return null;
		}
		
		b = lv.lumii.tda.kernel.TDACopier.makeCopy(k1, k2, true);
		if (!b) {
			System.err.println("Copy failed at step 1.");			
			k1.close();
			k2.close();
			cleanup(legacyProjectDir, targetDir, false);
			return null;
		}
		
		long rrrK = k2.findClass("TDAKernel::TDAKernel");
		long rrrIt = k2.getIteratorForAllClassObjects(rrrK);
		long rrrO = k2.resolveIteratorFirst(rrrIt);
		k2.freeIterator(rrrIt);
		if (DEBUG) System.out.println("CONVERT KERNEL WAS="+rrrO);
		
		
		TDAKernel kHelper = new TDAKernel(); // deletes undo history
		
		String classDir = ConfigStatic.SYS_DIR+"/src/org/webappos/project"; // TODO?
		
		String helperLocation = "ecore:file:///"+classDir.replace('\\', '/')+"/convert_to_tda2_helper.xmi";
		b = kHelper.exists(helperLocation);
		if (!b) {
			System.err.println("ecore helper does not exist at location "+helperLocation);
			k1.close();
			k2.close();
			cleanup(legacyProjectDir, targetDir, false);
			return null;
		}
		b = kHelper.open(helperLocation);		                                                                                                           
		if (!b) {
			System.err.println("ecore helper could not be opened from the location "+helperLocation);
			k1.close();
			k2.close();
			cleanup(legacyProjectDir, targetDir, false);
			return null;
		}

		it = kHelper.getIteratorForAllClassObjects(kHelper.findClass("TDAKernel::Command"));
		r = kHelper.resolveIteratorFirst(it);
		while (r!=0) {
/*			long it2 = kHelper.getIteratorForDirectObjectClasses(r);
			long rCls = kHelper.resolveIteratorFirst(it2);
			kHelper.freeIterator(it2);*/
			kHelper.deleteObject(r);			
			r = kHelper.resolveIteratorNext(it);
		}
		kHelper.freeIterator(it);
		
		b = lv.lumii.tda.kernel.TDACopier.makeCopy(kHelper, k2, null, true, 
				new String[]{
					"TDAKernel::.*",
					".*Engine",
					".*Event",
					".*Command",
					"GraphDiagramState",
					"Option",
					"Frame"
				}, null, true);
		if (!b) {
			System.err.println("Copy failed at step 2.");			
			k1.close();
			k2.close();
			kHelper.close();
			cleanup(legacyProjectDir, targetDir, false);
			return null;
		}
		
		b = lv.lumii.tda.kernel.TDACopier.makeCopy(kHelper, k2, null, true, 
				new String[]{
					"TDAKernel::HistoryStream",
					"ActivateDgrEvent",
					".*Cmd",
					"GraphDiagram",
					"Element"
				}, null, false);
		
		if (!b) {
			System.err.println("Copy failed at step 3.");			
			k1.close();
			k2.close();
			kHelper.close();
			cleanup(legacyProjectDir, targetDir, false);
			return null;
		}

		rrrK = k2.findClass("TDAKernel::TDAKernel");
		rrrIt = k2.getIteratorForAllClassObjects(rrrK);
		rrrO = k2.resolveIteratorFirst(rrrIt);
		k2.freeIterator(rrrIt);
		if (DEBUG) System.out.println("CONVERT KERNEL BECAME="+rrrO);
		
		//readln();
		
		b = k2.startSave();
		if (b) {
			b = k2.finishSave();
			if (!b)
				System.err.println("Could not finish save after copy.");				
		}
		else
			System.err.println("Could not start save after copy.");

		
//	    CreateLinkType("historyStream", "", "graphDiagram",
//                GetObjectTypeIdByName("GraphDiagram"), Card_01, Role_IndependentPartner, true,
//                GetObjectTypeIdByName("TDAKernel::HistoryStream"), Card_01, Role_IndependentPartner, true);
		
		
		long rKernelCls = k2.findClass("TDAKernel::TDAKernel");
		it = k2.getIteratorForDirectClassObjects(rKernelCls);
		long rKernel = k2.resolveIteratorFirst(it);
		
		// delete other kernels...
		while (rKernel != 0) {
			rKernel = k2.resolveIteratorNext(it);
			if (rKernel!=0)
				k2.deleteObject(rKernel);
		}
		k2.freeIterator(it);
		
		it = k2.getIteratorForDirectClassObjects(rKernelCls);
		rKernel = k2.resolveIteratorFirst(it);
		k2.freeIterator(it);
		
		long rKernelToEngine = k2.findAssociationEnd(rKernelCls, "attachedEngine");
		
		long rEngineCls = k2.findClass("TDAKernel::Engine");
		//it = k2.getIteratorForAllClassObjects(rEngineCls);
		it = k2.getIteratorForLinkedObjects(rKernel, rKernelToEngine);
		long rEngine = k2.resolveIteratorFirst(it);
		while (rEngine != 0) {
			long it2 = k2.getIteratorForDirectObjectClasses(rEngine);
			long rCls = k2.resolveIteratorFirst(it2);
			k2.freeIterator(it2);
			if (DEBUG) System.out.println("found engine "+k2.getClassName(rCls));
			
			long itAttr = k2.getIteratorForDirectAttributes(rCls);
			long rAttr = k2.resolveIteratorFirst(itAttr);
			while (rAttr != 0) {
				String attrName = k2.getAttributeName(rAttr);
				if (DEBUG) System.out.println("found attr "+attrName);
				if (attrName.startsWith("on")) {
					attrName = attrName.substring(2);
					
					long rAttr2 = k2.findAttribute(rKernelCls, "engineFor"+attrName);
					if (rAttr2 == 0) {
						rAttr2 = k2.createAttribute(rKernelCls, "engineFor"+attrName, k2.findPrimitiveDataType("String"));
						b = k2.setAttributeValue(rKernel, rAttr2, k2.getClassName(rCls));
						if (DEBUG) System.out.println("Associating "+attrName+" with "+k2.getClassName(rCls) +" = " +b );
					}
					
				}
				rAttr = k2.resolveIteratorNext(itAttr);
			}
			k2.freeIterator(itAttr);
			
			rEngine = k2.resolveIteratorNext(it);
		}
		k2.freeIterator(it);

		b = k2.startSave();
		if (b) {
			b = k2.finishSave();
			if (!b)
				System.err.println("Could not finish save.");				
		}
		else
			System.err.println("Could not start save.");
		
		kHelper.close();
		k2.close();
		k1.close();

		cleanup(legacyProjectDir, targetDir, b);
		return f.getAbsolutePath();
	}
	finally {
		if (fLegacyFolder != null)
			try {
				FileUtils.deleteDirectory(fLegacyFolder);
			} catch (IOException e1) {
			}
	}
	}

	public static void main(String[] args) {
		
/*
  test ecore open...		
		TDAKernel kHelper = new TDAKernel(); // deletes undo history
		
		String classDir = ConfigStatic.SYS_DIR+"/src/org/webappos/project"; // TODO?
		
		String helperLocation = "ecore:file:///"+classDir.replace('\\', '/')+"/convert_to_tda2_helper.ecore";
		boolean b = kHelper.exists(helperLocation);
		if (!b) {
			System.err.println("ecore helper does not exist at location "+helperLocation);
			return;
		}
		b = kHelper.open(helperLocation);		                                                                                                           
		if (!b) {
			System.err.println("ecore helper could not be opened from the location "+helperLocation);
			return;
		}
*/
				
		if (!System.getProperty("os.name").contains("Windows")) {
			System.err.println("LegacyProjectConverter can be used only in Windows.");
			return;			
		}
		
		if (args.length < 1) {
			System.out.println("Usage: LegacyProjectConverter <TDA-1-project-folder-or-zip-file> [<target-ar-folder>]");
			return;
		}
		
		String legacyProjectDir = args[0];
		String targetDir = null;
		if (args.length>2)
			targetDir = args[1];
	
		if (convert(legacyProjectDir, targetDir)==null) {
			System.err.println("Convert failed.");
		}
	}

}
