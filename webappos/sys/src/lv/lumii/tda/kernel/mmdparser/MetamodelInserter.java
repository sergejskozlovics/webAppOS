package lv.lumii.tda.kernel.mmdparser;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import lv.lumii.tda.kernel.TDACopier;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.RAAPI;

public class MetamodelInserter {
	
	public static boolean insertMetamodel(URL url, RAAPI repository)
	{
		return insertMetamodel(url, repository, null);
	}
	
	public static boolean insertMetamodel(URL url, RAAPI repository, StringBuffer errorMessages)
	{
		if (url == null) {
			if (errorMessages != null)
				errorMessages.append("Metamodel insert error: incorrect resource URL.\n");
			return false;
		}
		
		try {						
			if (url.toString().toLowerCase().endsWith(".mmd")) {					
				InputStream is = url.openStream();
				boolean result = false;
				try {
					 result = insertMMD(is, repository, errorMessages);
				}
				finally {
					is.close();
				}
				return result;
			}
			else
			if (url.toString().toLowerCase().endsWith(".ecore")) {
					IRepository ecore =TDAKernel.newRepositoryAdapter("ecore", null);
					if (ecore == null) {
						if (errorMessages != null)
							errorMessages.append("Could not load the ECore adapter.\n");
						return false;
					}
					if (!ecore.open(url.toString())) {
						errorMessages.append("Could not open ECore at ."+url+"\n");
						return false;
					}
					List<String> list = new LinkedList<String>();
					if (!TDACopier.makeCopy(ecore, repository, list, true)) {
						for (String s : list)
							errorMessages.append(s+"\n");
						return false;
					}
					ecore.close();
					return true;
			}
			else {
				if (errorMessages != null)
					errorMessages.append("The metamodel at "+url+" is of unknown format (only .mmd and .ecore are supported).\n");
				return false;
			}
		} catch (FileNotFoundException e) {
			if (errorMessages != null)
				errorMessages.append("Metamodel not found at "+url+".\n");
			return false;
		} catch (IOException e) {
			if (errorMessages != null)
				errorMessages.append("Error reading metamodel at "+url+".\n");
			return false;			
		}
	}
	
	// The input stream will NOT BE closed in insertMetamodel
	public static boolean insertMMD(InputStream is, RAAPI repository, StringBuffer errorMessages)
	{
		if (is == null)
			return false;
		
		
			// Reading input stream to a byte array...
			Writer writer = new StringWriter();
			try {
				char[] buf = new char[64*1024];
				Reader reader = new InputStreamReader(is);
				for(;;) {
					int readCount = reader.read(buf);
					if (readCount == -1)
						break;
					writer.write(buf, 0, readCount);
				}
			} catch (IOException e) {
				return false;
			}
			
			byte[] bytes = writer.toString().getBytes();
			
			boolean result = true;
					
			MMDParser p = new MMDParser(new ByteArrayInputStream(bytes));
			String errors = p.checkSyntax();
			if (errors != null) {
				result = false;
				if (errorMessages != null)
					errorMessages.append("Error parsing the metamodel.\n"+errors);
			}
			else {
				p.ReInit(new ByteArrayInputStream(bytes));
				
				try {
				errors = p.loadMMD(repository);
				}
				catch (Throwable t) {
					result = false;
					if (errorMessages != null) {
					 	StringWriter sw = new StringWriter();
					 	t.printStackTrace(new PrintWriter(sw));
						errorMessages.append(sw.toString());
					}
				}
				if (errors != null) {
					result = false;
					if (errorMessages != null)
						errorMessages.append("Error loading the metamodel.\n"+errors);
				}
			}
		
			return result;
		
	}		
}
