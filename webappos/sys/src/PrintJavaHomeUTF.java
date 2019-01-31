import java.io.DataOutputStream;
import java.io.IOException;

// This program will print to the output stream the Java home directory
// for the Java used to launch this program.
// We use writeUTF, i.e., the result is in Modified UTF-8, prepended with
// two bytes specifying the length (high-order byte first).

public class PrintJavaHomeUTF {

	public static void main(String[] args) {
		try {
			if ((args.length>0) && (args[0].toLowerCase().equals("--nolengthbytes"))) {
				DataOutputStream dos = new DataOutputStream(System.out); 
				dos.writeBytes(System.getProperty("java.home"));
				//dos.writeUTF(System.getProperty("java.home"));
				dos.close();
			}
			else {
				DataOutputStream dos = new DataOutputStream(System.out); 
				//dos.writeBytes(System.getProperty("java.home"));
				dos.writeUTF(System.getProperty("java.home"));
				dos.close();
			}
		} catch (IOException e) {
		}
	}

}
