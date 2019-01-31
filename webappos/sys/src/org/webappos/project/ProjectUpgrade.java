package org.webappos.project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.webappos.properties.AppProperties;
import org.webappos.server.API;
import org.webappos.server.APIForServerBridge;
import org.webappos.server.ConfigStatic;

public class ProjectUpgrade {

	public static void main(String[] args) {
		API.initAPI();
		if (args.length<2) {
			System.err.println("Usage 1: ProjectUpgrade <old-project-file-name[.grt|.app-extension]> <new-project-file-name.app-extension>");
			System.err.println("Usage 2: ProjectUpgrade <old-project-folder.app-extension_jr> <new-project-folder.app-extension_ar>");
			return;
		}
		
		for (File f : new File(ConfigStatic.APPS_DIR).listFiles()) {
			if (f.isDirectory() && f.getName().endsWith(".app")) {
				APIForServerBridge.propertiesManagerForServerBridge.loadAppProperties(f.getName(), f.getAbsolutePath());
			}
		}
		
		
		File f = new File(args[0]);
		File tmp;
		if (f.isDirectory()) {
			if (!f.getName().endsWith("_jr")) {
				System.err.println("The folder must end by '_jr'");
				return;
			}
			
			try {
				tmp = File.createTempFile("tmp_convert", f.getName().substring(0, f.getName().length()-3));
			} catch (IOException e) {
				System.err.println("Could not create temp file. "+e.getMessage());
				return;
			}
			
			
			if (!ZipFolder.zip(f.toPath(), tmp)) {
				System.err.println("Could not zip "+f.getAbsolutePath()+" to "+tmp.getAbsolutePath());				
				tmp.delete();
				return;
			}
			
			ZippedProject p = new ZippedProject(true); // offline
			
			AppProperties appProps = null; 
			String s = tmp.getName();
			int i = s.lastIndexOf('.');
			if (i>=0) {
				List<AppProperties> list = API.propertiesManager.getAppPropertiesByExtension(s.substring(i+1));
				if ((list!=null) && (!list.isEmpty()))
					appProps = list.iterator().next();
			}
			if (!p.open(appProps, tmp.getAbsolutePath(), "standalone", null, null)) {
				System.err.println("Could not open the project at zipped "+tmp.getAbsolutePath()+" from "+args[0]);
				return;
			}
			//p.setName(tmp.getName());
			
			boolean b = p.save();
			if (!b)
				System.err.println("Could not save the project to "+tmp.getAbsolutePath());
			
			if (!ZipFolder.unzip(tmp, new File(args[1]).toPath()))
				System.err.println("Could not unzip "+tmp.getAbsolutePath()+" to "+args[1]);
					
			tmp.delete();
			
			p.close();
			
		}
		else {
		
			ZippedProject p = new ZippedProject(true); // offline
			
			AppProperties appProps = null; 
			String s = p.getName();
			int i = s.lastIndexOf('.');
			if (i>=0) {
				List<AppProperties> list = API.propertiesManager.getAppPropertiesByExtension(s.substring(i+1));
				if ((list!=null) && (!list.isEmpty()))
					appProps = list.iterator().next();
			}
			
			if (!p.open(appProps, args[0], "standalone", null, null)) {
				System.err.println("Could not open the project at "+args[0]);
				return;
			}
			p.setName(args[1]);
			
			boolean b = p.save();
			if (!b)
				System.err.println("Could not save the project to "+args[1]);
			
			p.close();
		}
		
	}

}
