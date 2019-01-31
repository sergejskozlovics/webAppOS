package lv.lumii.tda.util;

public class ParentProcessWaiter {

	public native static void waitForTheParentProcessToTerminate();
	
	static {
		try {
			System.loadLibrary("tdakernel64");
		}
		catch (Throwable t) {
			try {
				System.loadLibrary("tdakernel32");
			}
			catch (Throwable tt) {
				System.loadLibrary("tdakernel");				
			}
		}
	}
	

}
