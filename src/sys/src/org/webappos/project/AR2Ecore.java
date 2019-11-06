package org.webappos.project;

import lv.lumii.tda.util.ZipFolder;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.IRepository;

import java.io.*;

import org.apache.commons.io.FileUtils;

public class AR2Ecore {
	
	
	private static boolean DEBUG = false;
	
	public static String convert(String folderOrZippedFile)
	{
		if (folderOrZippedFile==null)
			return null;
		
		File f = new File(folderOrZippedFile); 
		File folder;

		boolean isZipped = f.isFile();
		
		if (isZipped) {
			File targetFolder = new File(folderOrZippedFile+"_tmp");
		
			if (targetFolder.exists() && targetFolder.isDirectory()) {
				try {
					FileUtils.deleteDirectory(targetFolder);
				} catch (IOException e) {
					return null;
				}
			}
			if (!ZipFolder.unzip(f, targetFolder.toPath()))
				return null;
			folder = targetFolder;
		}
		else
			folder = f;
		
		
		IRepository k1;
		
		k1 = TDAKernel.newRepositoryAdapter("ar");
		
		boolean b;
		//TDAKernel k1 = new TDAKernel();
		if (k1 == null) {
			System.err.println("Error: could not initialize a repository adapter for AR.");
			return null;			
		}
		
		if (!k1.exists(folder.getAbsolutePath())) {
			System.err.println("Error: the source project cannot be found at "+folderOrZippedFile+".");
			try {
				if (isZipped)
					FileUtils.deleteDirectory(folder.getAbsoluteFile());
			} catch (IOException e) {
			}
			return null;
		}
			
		b = k1.open(folder.getAbsolutePath());
		if (!b) {
			System.err.println("Could not open the AR project in "+folderOrZippedFile);
			try {
				if (isZipped)
					FileUtils.deleteDirectory(folder.getAbsoluteFile());
			} catch (IOException e) {
			}
			return null;
		}
		
		
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
		String targetLocation = "ecore:"+folder.getAbsolutePath()+"/data.xmi";
				
		
		if (k2.exists(targetLocation))
			k2.drop(targetLocation);
		
		b = k2.open(targetLocation);
		if (!b) {
			System.err.println("Could not create target repository "+targetLocation);
			k1.close();
			try {
				if (isZipped)
					FileUtils.deleteDirectory(folder.getAbsoluteFile());
			} catch (IOException e) {
			}
			return null;
		}
		
		System.out.println("Target repository location = "+targetLocation);
		try {
			b = lv.lumii.tda.kernel.TDACopier.makeCopy(k1, k2, false);
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
				if (isZipped)
					FileUtils.deleteDirectory(folder.getAbsoluteFile());
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
		
		try {
			new File(folder.getAbsolutePath()+File.separator+"ar.actions").delete();
			new File(folder.getAbsolutePath()+File.separator+"ar.strings").delete();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (isZipped) {
			ZipFolder.zip(folder.toPath(), f);
		
			try {
				FileUtils.deleteDirectory(folder.getAbsoluteFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return folderOrZippedFile;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: AR2Ecore <ar-project-zipped-file-or-folder>");
			return;
		}
		

		if (convert(args[0])==null) {
			System.err.println("Convert failed.");
		}
	}

}
