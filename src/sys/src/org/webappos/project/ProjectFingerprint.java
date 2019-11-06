package org.webappos.project;

import lv.lumii.tda.util.ZipFolder;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.IRepository;

import java.io.*;

import org.apache.commons.io.FileUtils;

public class ProjectFingerprint {
	
	
	public static boolean fingerprint(String folderOrZippedFile)
	{
		if (folderOrZippedFile==null)
			return false;
		
		File f = new File(folderOrZippedFile); 
		File folder;

		boolean isZipped = f.isFile();
		
		if (isZipped) {
			File targetFolder = new File(folderOrZippedFile+"_tmp");
		
			if (targetFolder.exists() && targetFolder.isDirectory()) {
				try {
					FileUtils.deleteDirectory(targetFolder);
				} catch (IOException e) {
					return false;
				}
			}
			if (!ZipFolder.unzip(f, targetFolder.toPath()))
				return false;
			folder = targetFolder;
		}
		else
			folder = f;
		
		
		IRepository k1;
		
		k1 = TDAKernel.newRepositoryAdapter("ar", null);
		if (k1 == null) {
			System.err.println("Error: could not initialize a repository adapter for AR.");
			return false;			
		}
		
		if (!k1.exists(folder.getAbsolutePath())) {
			k1 = TDAKernel.newRepositoryAdapter("jr", null);
			if (k1 == null) {
				System.err.println("Error: could not initialize a repository adapter for JR.");
				return false;			
			}
			
			if (!k1.exists(folder.getAbsolutePath())) {
				System.err.println("Neither AR, nor JR repository found.");
				try {
					if (isZipped)
						FileUtils.deleteDirectory(folder.getAbsoluteFile());
				} catch (IOException e) {
				}
				return false;
			}
			
			System.out.println("JR");
		}
		else
			System.out.println("AR");			
		
		boolean b;
			
		b = k1.open(folder.getAbsolutePath());
		if (!b) {
			System.err.println("Could not open the project in "+folderOrZippedFile);
			try {
				if (isZipped)
					FileUtils.deleteDirectory(folder.getAbsoluteFile());
			} catch (IOException e) {
			}
			return false;
		}
		
		

		// metamodel (without generalizations)
		long itC = k1.getIteratorForClasses();
		if (itC!=0) {
			long rC = k1.resolveIteratorFirst(itC);
			while (rC != 0) {
				System.out.print("CLASS "+k1.getClassName(rC));

				long itAt = k1.getIteratorForDirectAttributes(rC);
				if (itAt!=0) {
					long rAt = k1.resolveIteratorFirst(itAt);
					while (rAt != 0) {
						System.out.print(" ATTR "+k1.getAttributeName(rAt)+":"+k1.getPrimitiveDataTypeName(k1.getAttributeType(rAt)) );
						rAt = k1.resolveIteratorNext(itAt);
					}
					k1.freeIterator(itAt);
				}

				System.out.println();
				rC = k1.resolveIteratorNext(itC);
			}
			k1.freeIterator(itC);
		}

		// associations and generalizations...			
		itC = k1.getIteratorForClasses();
		if (itC!=0) {
			long rC = k1.resolveIteratorFirst(itC);
			while (rC != 0) {
				
				long itAO = k1.getIteratorForDirectOutgoingAssociationEnds(rC);
				if (itAO!=0) {
					long rAO = k1.resolveIteratorFirst(itAO);
					while (rAO != 0) {
						long rInv = k1.getInverseAssociationEnd(rAO);
						if (rInv != 0) {
							// sync only once:
							if (((rInv > rAO)&&(!k1.isComposition(rInv))) || k1.isComposition(rAO)) {
								if (k1.isComposition(rAO))
									System.out.print("COMPOS");
								else
									System.out.print("ASSOC");
								System.out.println(" "+k1.getClassName(k1.getSourceClass(rAO))+"."+k1.getRoleName(rInv)+"<->"+k1.getRoleName(rAO)+"."+k1.getClassName(k1.getTargetClass(rAO)));
							}
						}
						else
							System.out.println("DASSOC "+k1.getClassName(k1.getSourceClass(rAO))+"->"+k1.getRoleName(rAO)+"."+k1.getClassName(k1.getTargetClass(rAO)));
						rAO = k1.resolveIteratorNext(itAO);
					}
					k1.freeIterator(itAO);
				}

				long itAI = k1.getIteratorForDirectIngoingAssociationEnds(rC);
				if (itAI!=0) {
					long rAI = k1.resolveIteratorFirst(itAI);
					while (rAI != 0) {
						long rInv = k1.getInverseAssociationEnd(rAI);
						if (rInv != 0) {
							// sync only once:
							if (((rInv > rAI)&&(!k1.isComposition(rInv))) || k1.isComposition(rAI)) {
								System.out.println("ASSOC "+k1.getClassName(k1.getSourceClass(rAI))+"."+k1.getRoleName(rInv)+"<->"+k1.getRoleName(rAI)+"."+k1.getClassName(k1.getTargetClass(rAI)));
							}
						}
						else
							System.out.println("DASSOC "+k1.getClassName(k1.getSourceClass(rAI))+"->"+k1.getRoleName(rAI)+"."+k1.getClassName(k1.getTargetClass(rAI)));
													
						rAI = k1.resolveIteratorNext(itAI);
					}
					k1.freeIterator(itAI);
				}
				
				long itSuper = k1.getIteratorForDirectSuperClasses(rC);
				if (itSuper!=0) {
					long rSuper = k1.resolveIteratorFirst(itSuper);
					while (rSuper != 0) {
						System.out.println("GEN "+k1.getClassName(rC)+"->"+k1.getClassName(rSuper));
						rSuper = k1.resolveIteratorNext(itSuper);
					}
					k1.freeIterator(itSuper);
				}
				
				rC = k1.resolveIteratorNext(itC);
			}
			k1.freeIterator(itC);
		}
		
		// sync objects and attributes...
/*		itC = k1.getIteratorForClasses();
		if (itC!=0) {
			long rC = k1.resolveIteratorFirst(itC);
			while (rC != 0) {					
				long itO = k1.getIteratorForDirectClassObjects(rC);
				if (itO!=0) {
					long rO = k1.resolveIteratorFirst(itO);
					while (rO != 0) {
						synchronizer.syncCreateObject(rC, rO);
				
						long itAt = k1.getIteratorForAllAttributes(rC);
						if (itAt!=0) {
							long rAt = k1.resolveIteratorFirst(itAt);
							while (rAt != 0) {
								String value = k1.getAttributeValue(rO, rAt);
								if (value != null)
									synchronizer.syncSetAttributeValue(rO, rAt, value);
								rAt = k1.resolveIteratorNext(itAt);
							}
							k1.freeIterator(itAt);
						}
	
	
						rO = k1.resolveIteratorNext(itO);
					}
					k1.freeIterator(itO);
				}				
				
				rC = k1.resolveIteratorNext(itC);
			}
			k1.freeIterator(itC);
		}*/
		
		/*
		// sync links...
		itC = k1.getIteratorForClasses();
		if (itC!=0) {
			long rC = k1.resolveIteratorFirst(itC);
			while (rC != 0) {					
				long itO = k1.getIteratorForDirectClassObjects(rC);
				if (itO!=0) {
					long rO = k1.resolveIteratorFirst(itO);
					while (rO != 0) {
	
						long itAO = k1.getIteratorForAllOutgoingAssociationEnds(rC);
						if (itAO!=0) {
							long rAO = k1.resolveIteratorFirst(itAO);
							while (rAO != 0) {
								
								long rInv = k1.getInverseAssociationEnd(rAO);
								if ((rInv ==0) || (rAO < rInv)) {
									// do not create the same link twice;
									// we create just directed links, and links, where current assoc ref < inverse assoc ref
									long itLinked = k1.getIteratorForLinkedObjects(rO, rAO);
									long rLinked = k1.resolveIteratorFirst(itLinked);
									while (rLinked != 0) {
										synchronizer.syncCreateLink(rO, rLinked, rAO);
										rLinked = k1.resolveIteratorNext(itLinked);
									}
									k1.freeIterator(itLinked);
								}
								
								rAO = k1.resolveIteratorNext(itAO);
							}
							k1.freeIterator(itAO);
						}
	
						rO = k1.resolveIteratorNext(itO);
					}
					k1.freeIterator(itO);
				}				
				
				rC = k1.resolveIteratorNext(itC);
			}
			k1.freeIterator(itC);
		}*/
		
		
		k1.close();
		
		if (isZipped) {
		
			try {
				FileUtils.deleteDirectory(folder.getAbsoluteFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: ProjectFingerprint <ar/jr-project-zipped-file-or-folder>");
			return;
		}
		
		
		if (!fingerprint(args[0])) {
			System.err.println("Fingerprint failed.");
		}
	}

}
