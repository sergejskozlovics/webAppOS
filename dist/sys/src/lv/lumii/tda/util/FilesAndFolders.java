package lv.lumii.tda.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FilesAndFolders {

		public static void modifyFilesEndingWith(String directory, String endsWith, String oldString, String newString) {
		        File dir = new File(directory);

		        for (File f : dir.listFiles()) { 
		        	if (f.getName().endsWith(endsWith)) {
		        		modifyFile(directory+File.separator+f.getName(), oldString, newString);
		        	}
		        }

		}
	 
	    public static void modifyFile(String filePath, String oldString, String newString)
	    {
	    	System.err.println("modify file "+filePath+" from="+oldString+" to="+newString);
	        File fileToBeModified = new File(filePath);
	         
	        String oldContent = "";
	         
	        BufferedReader reader = null;
	         
	        FileWriter writer = null;
	         
	        try
	        {
	            reader = new BufferedReader(new FileReader(fileToBeModified));
	             
	            String line = reader.readLine();
	             
	            while (line != null) 
	            {
	                oldContent = oldContent + line + System.lineSeparator();
	                 
	                line = reader.readLine();
	            }
                reader.close();
                reader = null;
	             
	        	int i = 0;
	        	String newContent = oldContent;
	        	int j = newContent.indexOf(oldString, i);
	        	while (j >= 0) {
	        		newContent = newContent.substring(0, j) + newString + newContent.substring(j+oldString.length());
	        		i = j+newString.length();
	        		j = newContent.indexOf(oldString, i);
	        	}
	             
	        	
	            if (!newContent.equals(oldContent)) {
	            	writer = new FileWriter(fileToBeModified);	             
	            	writer.write(newContent);
	            }
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace(System.err);
	        }
	        finally
	        {
	            try
	            {
	            	if (reader != null)
	            		reader.close();
	              
	                if (writer != null)
	                	writer.close();
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace(System.err);
	            }
	        }
	    }
	    
	    
}
