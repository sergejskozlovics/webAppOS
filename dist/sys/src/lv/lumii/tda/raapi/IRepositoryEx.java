package lv.lumii.tda.raapi;


/**
 * The <code>IRepositoryEx</code> defines methods for more tight cooperation with
 * model repository.
 * 
 ***/
public interface IRepositoryEx  extends lv.lumii.tda.raapi.IRepositoryManagement, lv.lumii.tda.raapi.RAAPI
{
	/**
	 * Calculates the memory usage factor for this repository (regarding to some memory limit).
	 * @return how much memory of some predefined limit (e.g., specified in some setting or environment variable)
	 * is being used by the current repository.
	 * This function can be used to inform users about reaching their memory limits.
	 * The value is between 0.0 and 1.0.
	 */
	public double getMemoryUsageFactor();
	
	/**
	 * Checks whether the repository internal data structures are OK.
	 * @return whether some memory fault occurred; if true, the repository is unusable, but it
	 * can be re-opened from the last startSave/finishSave save point 
	 */
	public boolean memoryFault();
} // interface IRepository
