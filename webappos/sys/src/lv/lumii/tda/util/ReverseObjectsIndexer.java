package lv.lumii.tda.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class ReverseObjectsIndexer<T> extends ObjectsIndexer<T> {
	
	private Map<T, Long> objectToIndex = new HashMap<T, Long>();
	
	
	public ReverseObjectsIndexer()
	{
		super();
		objectToIndex.put(null, 0L); // special value: no index
	}
	
	public ReverseObjectsIndexer(int numberOfBits)
	{
		super(numberOfBits);
		objectToIndex.put(null, 0L); // special value: no index
	}
	
	
	public void freeIndex(long index)
	{
		if (index == 0L)
			return;
		T obj = get(index);
		super.freeIndex(index);
		objectToIndex.remove(obj);
	}
	
	public T getObject(long index)
	{
		return super.get(index);
	}
	
	public long getIndex(T obj)
	{
		Long l = objectToIndex.get(obj);
		if (l == null) {
			long retVal = super.acquireIndex(obj);
			objectToIndex.put(obj, retVal);
			return retVal;
		}
		else
			return l;
	}

	public long findIndex(T obj)
	{
		Long l = objectToIndex.get(obj);
		if (l == null) {
			return 0;
		}
		else
			return l;
	}
	
	public void set(long index, T object)
	{
		T oldObject = indexToObject.remove(index);
		if (oldObject != null)
			objectToIndex.remove(oldObject);
		super.set(index, object);
		objectToIndex.put(object, index);		
	}
}
