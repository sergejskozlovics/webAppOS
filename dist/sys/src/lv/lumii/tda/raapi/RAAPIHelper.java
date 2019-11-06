package lv.lumii.tda.raapi;

public class RAAPIHelper {
	
	public static long getSingletonObject(RAAPI raapi, String className)
	{
		long rClass = raapi.findClass(className);
		if (rClass == 0)
			return 0;
		
		long it = raapi.getIteratorForAllClassObjects(rClass);
		if (it == 0) {
			raapi.freeReference(rClass);
			return 0;			
		}
		raapi.freeReference(rClass);			
		
		long rObject = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		return rObject;
	}
	
	public static long getObjectClass(RAAPI raapi, long rObject)
	{
		long it = raapi.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return 0;
		long rCls = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		return rCls;		
	}
	
	public static String getObjectClassName(RAAPI raapi, long rObject)
	{
		long it = raapi.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return null;
		long rCls = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rCls == 0)
			return null;
		
		String clsName = raapi.getClassName(rCls);
		raapi.freeReference(rCls);
		
		return clsName;
	}

}
