package lv.lumii.tda.raapi;



/**
 * The <code>IRepository</code> interface is a union of
 * <code>RAAPI</code> containing operations on model elements
 * and <code>IRepositoryManagement</code> containing
 * technical operations on repositories such as operations
 * for opening, closing, saving, etc.
 ***/
public interface IRepository  extends lv.lumii.tda.raapi.IRepositoryManagement, lv.lumii.tda.raapi.RAAPI
{
} // interface IRepository
