package lv.lumii.dialoglayout;

import  lv.lumii.layoutengine.funcmin.CycleReducer;
import java.io.*;

public class Test {

	public static void main(String[] args) {
		CycleReducer cr = new CycleReducer(53, 0.0001); 
		try (BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\Sergejs Kozlovics\\Downloads\\85.254.199.113-1492692175071.log")))) {
			
			
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
	    		String[] arr = line.split("\\s+");
	    		if (arr[0].equals("INEQ")) {
	    			cr.addConstraint(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Double.parseDouble(arr[3]), Double.parseDouble(arr[4]));
	    		}
	    		else {
	    			cr.addConstraint(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Double.parseDouble(arr[3]), Double.parseDouble(arr[4]));
	    			cr.addConstraint(Integer.parseInt(arr[2]), Integer.parseInt(arr[1]),-Double.parseDouble(arr[3]), -Double.parseDouble(arr[4]));	    		
	    		}
		    }
		    
		    System.out.println(  cr.getSolution() );
		    

		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
