package org.webappos.project;

import lv.lumii.tda.util.ZipFolder;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.IRepository;

import java.io.*;

import org.apache.commons.io.FileUtils;

public class ZippedToCloudProjectConverter {
	
	
	private static boolean DEBUG = false;
	
	public static String convert(String zippedFile, String targetFolder, String targetRepository)
	{
		if (zippedFile==null)
			return null;
		

		if (targetFolder == null)
			targetFolder = zippedFile+"_"+targetRepository;

		if (ZipFolder.zipHasEntry(new File(zippedFile), "metamodel.xml") || ZipFolder.zipHasEntry(new File(zippedFile), "metamodel.xml.gz") || ZipFolder.zipHasEntry(new File(zippedFile), "metamodel.zip")) {
			System.err.println("ZIP HAS legacy project");
			return LegacyToCloudProjectConverter.convert(zippedFile, targetFolder);
		}
		
		
		// check whether the zipped file contains jr_data, or data.xmi
		File farFolder = new File(targetFolder);
		
		if (farFolder.exists() && farFolder.isDirectory()) {
			try {
				FileUtils.deleteDirectory(farFolder);
			} catch (IOException e) {
				return null;
			}
		}
		
		if (!ZipFolder.unzip(new File(zippedFile), farFolder.toPath()))
			return null;
		
		if ("ar".equals(targetRepository)) {
			if (new File(targetFolder+File.separator+"ar.actions").exists() && new File(targetFolder+File.separator+"ar.strings").exists())
				return targetFolder; // all OK; AR was already there
		}
		else
		if ("jr".equals(targetRepository)) {

			File d = new File(targetFolder+File.separator+"jr_data"); 
			if (d.exists() && d.isDirectory())
				return targetFolder; // JR exists
		}
		else
			return null; // unsupported target repository
		
		
		IRepository k1;
		String oldProjectURL;
		
		File jrdataFolder;
		
		jrdataFolder = new File(targetFolder+File.separator+"jr_data");
		
		File ecoreFile = new File(targetFolder+File.separator+"data.xmi");
		File arFile = new File(targetFolder+File.separator+"ar.actions");
		
		if (jrdataFolder.exists()) {
			k1 = TDAKernel.newRepositoryAdapter("jr");
			oldProjectURL = targetFolder;
		}
		else
			if (ecoreFile.exists()) {
				k1 = TDAKernel.newRepositoryAdapter("ecore");
				oldProjectURL = "file:///" + targetFolder.replace('\\', '/') + "/data.xmi";
			}
			else
			if (arFile.exists()){
				k1 = TDAKernel.newRepositoryAdapter("ar");
				oldProjectURL = targetFolder;				
			}
			else
				return null; // neither JR, AR, nor ECore
		
		
		boolean b;
		//TDAKernel k1 = new TDAKernel();
		if (k1 == null) {
			System.err.println("Error: could not initialize a repository adapter for JR, AR, or ECore.");
			return null;			
		}
		
		if (!k1.exists(oldProjectURL)) {
			System.err.println("Error: the source project cannot be found at "+oldProjectURL+".");
			try {
				FileUtils.deleteDirectory(farFolder);
			} catch (IOException e) {
			}
			return null;
		}
			
		b = k1.open(oldProjectURL);
		if (!b) {
			System.err.println("Could not open the JR or ECore project in "+oldProjectURL);
			try {
				FileUtils.deleteDirectory(farFolder);
			} catch (IOException e) {
			}
			return null;
		}
		
		TDAKernel k2 = new TDAKernel();
		String targetLocation = IProject.DEFAULT_REPOSITORY+":"+targetFolder;
				
		
		if (k2.exists(targetLocation))
			k2.drop(targetLocation);
		
		b = k2.open(targetLocation);
		if (!b) {
			System.err.println("Could not create target repository "+targetLocation);
			k1.close();
			try {
				FileUtils.deleteDirectory(farFolder);
			} catch (IOException e) {
			}
			return null;
		}
		
		System.err.println("Copying "+oldProjectURL+" to "+targetLocation);
		try {
			b = lv.lumii.tda.kernel.TDACopier.makeCopy(k1, k2, true);
		}
		catch(Throwable t) {
			b = false;
			t.printStackTrace(System.err);
		}
		if (!b) {
			System.err.println("Copy failed at step 1.");			
			k1.close();
			k2.close();
			try {
				FileUtils.deleteDirectory(farFolder);
			} catch (IOException e) {
			}
			return null;
		}
		
				
		b = k2.startSave();
		if (b) {
			b = k2.finishSave();
			if (!b)
				System.err.println("Could not finish save after copy.");				
		}
		else
			System.err.println("Could not start save after copy.");

				
		k2.close();
		k1.close();
		
		new File(targetFolder+File.separator+"data.xmi").delete();
		new File(targetFolder+File.separator+"data.ecore").delete();
		
		if (!"jr".equals(targetRepository)) { 
			new File(targetFolder+File.separator+"writable_references.map").delete();
			try {
				FileUtils.deleteDirectory(jrdataFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return targetFolder;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: ZippedToCloudProjectConverter <ecore-project-zipped-file> [<target-AR-folder>]");
			return;
		}
		
		String targetDir = null;
		if (args.length>2)
			targetDir = args[1];

		if (convert(args[0], targetDir, IProject.DEFAULT_REPOSITORY)==null) {
			System.err.println("Convert failed.");
		}
	}

}
