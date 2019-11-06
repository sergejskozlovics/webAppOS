package lv.lumii.tda.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

public class ObjectsIndexer<T> {
	
	private static long bitMasks[];

	static {
		bitMasks = new long[65]; // using indices 1..64
		
		long mask = 0;
		bitMasks[0] = 0; // fictive
		for (byte i=1; i<=64; i++) {
			mask = mask << 1;
			mask |= 1;
			bitMasks[i] = mask;
		}
	}
	
	private long bitMask = bitMasks[63];
	protected Map<Long, T> indexToObject = new HashMap<Long, T>();
	
	public Map<Long, T> getMap() {
		return indexToObject;
	}
	
	
	public ObjectsIndexer()
	{
		indexToObject.put(0L, null); // special value: no index
	}
	
	public ObjectsIndexer(int numberOfBits)
	{
		setNumberOfBits(numberOfBits);
		indexToObject.put(0L, null); // special value: no index
	}
	
	public void setNumberOfBits(int numberOfBits)
	{
		if ((numberOfBits >= 1) && (numberOfBits <= 64))
			bitMask = bitMasks[numberOfBits];
	}
	
	/* Generates a value of n bits, where n is the number of ones in PROXY_REFERENCE_MASK.
	 * The generated value will be that, which is not already in use  (but it becomes ``in use''
	 * until freeIndex is called).
	 * In case of error, returns 0 (special value).
	 */
	public long acquireIndex()
	{
		return acquireIndex(null);
	}
	
	/* Works as acquireIndex, but also associates the given object
	 * with the index returned.
	 * In case of error, returns 0 (special value).
	 */
	public long acquireIndex(T object) {
		Random rnd = new Random();
		long result = rnd.nextLong() & this.bitMask;
		if ((result!=0) && !indexToObject.containsKey(result)) {
			indexToObject.put(result, object);
			return result;
		}
		
		// if random returns a new long value right away, we skip the following part...
		
		int tries = 1;
		final int MAX_TRIES = 20;
		for(;;) {
			result = rnd.nextLong() & this.bitMask;
			if ((result!=0) && !indexToObject.containsKey(result)) {
				indexToObject.put(result, object);
				return result;
			}
			tries++;
			if (tries >= MAX_TRIES) {
				// TODO: if indexToObject.size() <= this.bitMask  (total max values: this.bitMask+1(special value))
				// then try to obtain a reference in another way 
				return 0;
			}
		}			
	}
	
	public void freeIndex(long index)
	{
		indexToObject.remove(index);
		
	}
	
	public T get(long index)
	{
		return indexToObject.get(index);
	}
	
	public void set(long index, T object)
	{
		indexToObject.remove(index);
		indexToObject.put(index, object);
	}
}
