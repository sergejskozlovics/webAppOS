package org.webappos.benchmark;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.UIManager;
/* for nimbus look and feel:
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException; */

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI;

public class TestAR_Mem {
	
	
	private static long last = 0;
	
	private static long getUsedMem() {
		long t = Runtime.getRuntime().totalMemory();
		long f = Runtime.getRuntime().freeMemory();
		return t-f;
	}
	
	private static void printMem(String info) {
		long t = Runtime.getRuntime().totalMemory();
		long f = Runtime.getRuntime().freeMemory();
		
		System.out.println(info+" total:"+t+" free:"+f+" used:"+" "+(t-f)+" this:"+(t-f-last));
		last = t-f;
	}

	public static void main(String[] args) {
		
		
		System.out.println("Running in OS "+System.getProperty("os.name"));
		
		String folder = "D:\\test_ar\\pizza_ar"; // "pizza_ar" "big_ar"
		
		String tmp = "f:\\tmp5";//"Z:\\Darbinieki\\SergejsK\\tmp"; //"D:\\test_ar\\tmp";  
		
		//String pivotLocation = "ar:shmserver:
		
		int maxN = 100;
		System.out.println("Copying "+maxN+" repos...");
		
		try {
		for (int i=0; i<maxN; i++) {
			
			File dir=new File(tmp+File.separator+i); 
			dir.mkdirs();
			
			CopyOption[] options = new CopyOption[]{
				      StandardCopyOption.REPLACE_EXISTING,
				      StandardCopyOption.COPY_ATTRIBUTES
				    }; 
			
			if (!new File(tmp+File.separator+i+File.separator+"ar.actions").exists())
				Files.copy(new File(folder+File.separator+"ar.actions").toPath(), new File(tmp+File.separator+i+File.separator+"ar.actions").toPath(), options);
			if (!new File(tmp+File.separator+i+File.separator+"ar.strings").exists())
				Files.copy(new File(folder+File.separator+"ar.strings").toPath(), new File(tmp+File.separator+i+File.separator+"ar.strings").toPath(), options);
			
		}
		}
		catch(Throwable t) {
			t.printStackTrace();			
			return;
		}
		
		
		TDAKernel[] arr = new TDAKernel[maxN];
		
		
		
		System.out.println("Opening "+maxN+" kernels...");

		
		
		long time1 = System.currentTimeMillis();
		
		//printMem("0");
		long mem1 = getUsedMem();
		
		try {
		for (int i=0; i<maxN; i++) {					
			String pivotLocation = "ar:shmserver:"+tmp+File.separator+i;
			
			TDAKernel kernel = new TDAKernel();
			boolean b = kernel.open(pivotLocation);
			if (!b) {
				System.err.println("Open error.");
				return;
			}			
			arr[i] = kernel;
//			printMem((i+1)+"");
			long time2 = System.currentTimeMillis();
			long mem2=getUsedMem();
			if (i==0) {
				time1 = time2;
				mem1 = mem2;
			}
			else {
				System.out.println("repos="+(i+1)+" TIME="+(time2-time1)+" avg="+((time2-time1)/(i))+"ms");
				System.out.println("   MEM="+((mem2-mem1)*1.0/1024/1024)+" MiB avg="+((mem2-mem1)*1.0/(i)/1024/1024)+" MiB");
			}
		}
		}
		catch(Throwable t) {
			t.printStackTrace();
			
			return;
		}
	}

}
