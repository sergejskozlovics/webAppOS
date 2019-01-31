package org.webappos.webproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebProcTab {
	
	public static class Row {
		String type = null;
		String location = null;
		int instances = 1;
		String name = null;
		int reconnectMs = 0;
		public String toString() {
			return "WebProcTab.Row ["+type+"] ["+location+"] x"+instances+" `"+name+"`"+", reconnect in "+reconnectMs+" ms";
		}
	}
	
	private ArrayList<Row> wpArr = new ArrayList<Row>();
	
	public WebProcTab(String filename) {
		
		
		Pattern p = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
		
		BufferedReader r = null;
		try {
			try {
				r = new BufferedReader(new FileReader(new File(filename)));
				
				for (;;) {
					String s = r.readLine();
					if (s==null)
						return;
					s = s.trim();
					if (s.isEmpty() || s.startsWith("#"))
						continue;
					Matcher m = p.matcher(s);
					Row wproc = new Row();
					int i=0;
					while (m.find()) {
						String item = m.group(1).replace("\"", "");
						switch(i) {
						case 0: wproc.type = item; break;
						case 1: wproc.location = item; break;
						case 2: try {wproc.instances=Integer.parseInt(item);}catch(Throwable t) {};
								if (wproc.instances<0)
									wproc.instances = 1;
								break;
						case 3: wproc.name = item; break;
						case 4: try {wproc.reconnectMs=Integer.parseInt(item);}catch(Throwable t) {}; break;
						}
						i++;
						if (i>4) {
							wpArr.add(wproc);
							break; // while
						}
					}
					
					
					
				}
			}
			finally {
				if (r!=null)
					r.close();
			}
		}
		catch(Throwable t) {
			
		}
		
		
	}
	
	public List<Row> getRows() {
		return wpArr;
	}
		
/*	public static void main(String[] args) {
		for (WebProcRow r : new WebProcTab(Config.ETC_DIR+File.separator+"webproctab").wpArr)
			System.out.println(r);
	}*/

}
