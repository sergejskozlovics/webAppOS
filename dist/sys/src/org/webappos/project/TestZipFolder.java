package org.webappos.project;

import java.io.*;

public class TestZipFolder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//boolean b = ZipFolder.unzip(new File("d:\\kp.zip"), new File("d:\\qwerty").toPath());
		//boolean b = ZipFolder.zip(new File("d:\\qwerty").toPath(), new File("d:\\qwerty.zip"));
		//System.out.println(b);
		
		ZipFolder f = new ZipFolder();
		System.out.println("open="+f.open(new File("d:\\kp.zip")));
		System.out.println(f.getFolder());
		System.out.println(f.getFile());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("saveAs="+f.saveAs(new File("d:\\kp_qwerty.zip")));
		System.out.println(f.getFolder());
		System.out.println(f.getFile());
		try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.close();
		
		try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("closed");
	}

}
