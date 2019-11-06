package org.webappos.project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.webappos.memory.OfflineMRAM;
import org.webappos.properties.WebAppProperties;
import org.webappos.properties.PropertiesManager;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.raapi.RAAPI;

public class ProjectUpgrade {

	private static ZippedProject p = new ZippedProject(true); // offline
	
	public static boolean closeAndExit(RAAPI raapi, long __rObj) {
		
		String dir = p.getFolderName();
		
		try {
			FileUtils.deleteDirectory(new File(dir+File.separator+"lua"));
		} catch (IOException e) {
		}
		try {
			FileUtils.deleteDirectory(new File(dir+File.separator+"Pictures"));
		} catch (IOException e) {
		}
		try {
			FileUtils.deleteDirectory(new File(dir+File.separator+"Plugins"));
		} catch (IOException e) {
		}
		try {
			FileUtils.deleteDirectory(new File(dir+File.separator+"PluginData"));
		} catch (IOException e) {
		}
		try {
			FileUtils.deleteDirectory(new File(dir+File.separator+"PluginUninstalls"));
		} catch (IOException e) {
		}
		try {
			FileUtils.deleteDirectory(new File(dir+File.separator+"Migration"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new File(dir+File.separator+"about.html").delete();
		new File(dir+File.separator+"engines.txt").delete();
		new File(dir+File.separator+"files.txt").delete();
		new File(dir+File.separator+"framework.version").delete();
		new File(dir+File.separator+"main.dll").delete();
		new File(dir+File.separator+"diagram_export_result").delete();
		new File(dir+File.separator+"session.lua").delete();
		new File(dir+File.separator+"tmp_version.lua").delete();
		
		boolean b = p.save();
		if (!b)
			System.err.println("Could not save the project to " + p.getName());
		else
			System.out.println("Project saved.");		
		
		p.close();
		System.exit(0); 
		return true;
	}
	
	public static void main(String[] args) {
		

		try {
			if (args.length < 2) {
				System.err.println(
						"Usage 1: ProjectUpgrade <old-project-file-name[.grt|.app-extension]> <new-project-file-name.app-extension> [auto.webcalls]");
				System.err.println(
						"Usage 2: ProjectUpgrade <old-project-folder.app-extension_jr> <new-project-folder.app-extension_ar> [auto.webcalls]");
				return;
			}
			
			API.initOfflineAPI();			
			assert (API.propertiesManager instanceof PropertiesManager);
			assert (API.dataMemory instanceof OfflineMRAM);
			assert (API.webCaller instanceof WebCaller);

			((OfflineMRAM)API.dataMemory).addProject(p);
			
			
			for (File f : new File(ConfigStatic.APPS_DIR).listFiles()) {
				if (f.isDirectory() && f.getName().endsWith(".webapp")) {						
					((PropertiesManager)API.propertiesManager).loadWebAppPropertiesByFullName(f.getName(),
							f.getAbsolutePath());
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
					tmp = File.createTempFile("tmp_convert", f.getName().substring(0, f.getName().length() - 3));
				} catch (IOException e) {
					System.err.println("Could not create temp file. " + e.getMessage());
					return;
				}

				if (!ZipFolder.zip(f.toPath(), tmp)) {
					System.err.println("Could not zip " + f.getAbsolutePath() + " to " + tmp.getAbsolutePath());
					tmp.delete();
					return;
				}

				

				WebAppProperties appProps = null;
				String s = tmp.getName();
				int i = s.lastIndexOf('.');
				if (i >= 0) {
					List<WebAppProperties> list = API.propertiesManager.getWebAppPropertiesByExtension(s.substring(i + 1));
					if ((list != null) && (!list.isEmpty()))
						appProps = list.iterator().next();
				}
				if (!p.open(appProps, tmp.getAbsolutePath(), "standalone", null, null)) {
					System.err.println(
							"Could not open the project at zipped " + tmp.getAbsolutePath() + " from " + args[0]);
					return;
				}
				// p.setName(tmp.getName());

				boolean b = p.save();
				if (!b)
					System.err.println("Could not save the project to " + tmp.getAbsolutePath());

				if (!ZipFolder.unzip(tmp, new File(args[1]).toPath()))
					System.err.println("Could not unzip " + tmp.getAbsolutePath() + " to " + args[1]);

				tmp.delete();

				p.close();

			} else {

				WebAppProperties appProps = null;
				String s=args[1];
				int i = s.lastIndexOf('.');
				if (i >= 0) {
					List<WebAppProperties> list = API.propertiesManager.getWebAppPropertiesByExtension(s.substring(i + 1));
					if ((list != null) && (!list.isEmpty()))
						appProps = list.iterator().next();
				}
				
				if ((appProps!=null)&&(args.length>=3)) {
					// adding auto.webcalls
					appProps.webcallsFiles.add(args[2]);
					appProps.classpaths.add(new File(args[2]).getParent());
				}
				
				((WebCaller)API.webCaller).loadWebCalls(appProps);
				
				API.config.inline_webcalls = true;

				p.setName(args[1]);
				
				if (!p.open(appProps, args[0], "standalone", null, null)) {
					System.err.println("Could not open the project at " + args[0]);
					return;
				}

			}
		} finally {
			//System.exit(0); 
		}

	}

}
