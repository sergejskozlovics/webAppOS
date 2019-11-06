package  lv.lumii.tda.kernel.mmdparser;

import java.io.*;
import java.net.MalformedURLException;

import lv.lumii.tda.raapi.IRepository;


public class MMD2Ecore {

	/**
	 * @param args args[0] is the name of the source MMD-file (the target file will be with the same name but with the .ecore extension)
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: MMD2ECore <source-mmd-file-name>");
			return;
		}
				
		// we set the name of the model .xmi file (the corresponding .ecore file will be created automatically)  
		String targetFileName = args[0];
		int i = targetFileName.lastIndexOf('.');
		if (i==-1)
			targetFileName += ".ecore";
		else
			targetFileName = targetFileName.substring(0, i)+".xmi"; // the corresponding .ecore will be created automatically 		
		
		/*InputStream is;
		try {
			is = new FileInputStream(args[0]);
		} catch (FileNotFoundException e) {
			System.err.println("Could not open MMD-file "+args[0]+".\n"+e.getMessage());
			return;
		}*/
		
			

		IRepository r = new lv.lumii.tda.adapters.repository.ecore.RepositoryAdapter();
		r.open(targetFileName);
		
		
		try {
			StringBuffer errorMessages = new StringBuffer();
			if (!MetamodelInserter.insertMetamodel(new File(args[0]).toURI().toURL(), r, errorMessages)) {
				System.err.println("Could not insert metamodel from MMD-file "+args[0]+".\n"+errorMessages);
				r.close();
				return;
			}
		} catch (MalformedURLException e) {
			System.err.println("Could not insert metamodel from MMD-file "+args[0]+".\n"+e.toString());
			r.close();
			return;
		}
		if (!r.startSave() || !r.finishSave()) {
			System.err.println("Could not save ECore-file "+targetFileName+".");
			r.close();
			return;			
		}
		r.close();		
		//new File(targetFileName).delete(); // deleting .xmi file, keeping only .ecore
		//new File(targetFileName+"_refs").delete(); // deleting .xmi_refs file
	}

}
