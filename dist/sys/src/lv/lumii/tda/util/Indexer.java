package lv.lumii.tda.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;


/**
 * A bi-directional indexer for assigning long-s to Java objects.
 * The zero index is a special one (not-an-index). It is returned on errors.
 * Objects are indexed starting from 1 and forward. When
 * certain objects are excluded from indexing, their indexes can be assigned
 * to other objects later.
 * <BR/>
 * The indexer preserves the following relation:<BR/>
 *   1 <= each newly assigned index <= MAX_INDEX<BR/>
 * When the number of objects indexed reaches 1/2*MAX_INDEX, the indexer
 * is allowed not to assign the index, and return 0 (not-an-index).
 *  
 * The MAX_INDEX value is determined by the maximal number of
 * bits that can be used for indexes. If this number is n,
 * then MAX_INDEX = 2^n-1.
 * 
 * @param <T> the type of objects to be indexed (java.lang.Object is OK, too)
 */
public class Indexer<T> {
	
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
	
	private long MAX_INDEX = bitMasks[31];	
	private long largestIndex = 0;
	private long indexesUsed = 0;
	
	private Map<Long, T> indexToObject = new HashMap<Long, T>();
	private Map<T, Long> objectToIndex = new HashMap<T, Long>();
	
	
	/** 
	 * Initializes the indexer with MAX_INDEX = 2^31-1 (31 bit per index).
	 * You can use <code>setMaximumNumberOfBits</code> to change that value.
	 * @see Indexer#setMaximumNumberOfBits
	 */
	public Indexer()
	{
		indexToObject.put(0L, null); // special value: no index
		objectToIndex.put(null, 0L); 
	}
		
	/**
	 * Sets the new upper bound for MAX_INDEX depending on the number of bits per index given.
	 * @param maxNumberOfBits the new maximum number of bits per index
	 * @return whether the operation succeeded
	 */
	public boolean setMaximumNumberOfBits(int maxNumberOfBits)
	{
		if ((maxNumberOfBits >= 1) && (maxNumberOfBits <= 64)) {
			MAX_INDEX = bitMasks[maxNumberOfBits];
			return true;
		}
		return false;
	}
		
		
	/**
	 * Generates an index for the given object.
	 * @param object an object for which to obtain an index
	 * @return the index just acquired. If indexes used > MAX_INDEX/2,
	 *   the operation may fail. In this case, zero (not-an-index) is returned.
	 */
	public long acquireIndex(T object) {
		if (largestIndex < MAX_INDEX) {
			largestIndex++;
			indexesUsed++; // acquiring a new index
			indexToObject.put(largestIndex, object);
			objectToIndex.put(object, largestIndex);
			return largestIndex;
		}
		
		assert(largestIndex == MAX_INDEX);
		
		if (indexesUsed > (MAX_INDEX>>1))
			return 0; // indexesUsed > MAX_INDEX/2 => the density is too big 
		
		// using random; at least half of the indexes are free,
		// thus, the probability to fail in n steps is 1/2^n,
		// which decreases exponentially with each next step
		Random rnd = new Random();
		long result;
		for(;;) {
			result = rnd.nextLong() & this.MAX_INDEX;
			if ((result!=0) && !indexToObject.containsKey(result)) {
				indexesUsed++; // acquiring a new index
				indexToObject.put(result, object);
				objectToIndex.put(object, result);
				return result;
			}
		}			
	}
	
	/**
	 * Associates the given index with the given object.
	 * If the index has not been acquired yet, it is acquired.
	 * Otherwise, the object previously associated with the given index
	 * is replaced by the given object.
	 * @param index the index to associate (not 0)
	 * @param object the object to associate (not null)
	 * @return whether the operation succeeded
	 */
	public boolean set(long index, T object)
	{
		if ((index<=0) || (index>MAX_INDEX) || (object == null))
			return false;
		
		if (index > largestIndex)		
			largestIndex = index;
		
		if (object != null) {
			Long oldIndex = objectToIndex.remove(object);
			if (oldIndex != null) {
				indexToObject.remove(oldIndex);
				indexesUsed--;
			}
		}
		
		T oldObject = indexToObject.remove(index);
		if (oldObject != null)
			objectToIndex.remove(oldObject);
		else
			indexesUsed++; // acquiring a new index
		indexToObject.put(index, object);
		objectToIndex.put(object, index);		
		return true;
	}
	
	/**
	 * Frees the given index. This index may be assigned to another object later.
	 * The zero index (not-an-index) cannot be freed.
	 * @param index the index to free
	 */
	public void freeIndex(long index)
	{		
		T oldObject = indexToObject.get(index);
		if (index != 0) {
			indexToObject.remove(index);
			if (oldObject != null)
				objectToIndex.remove(oldObject);
			indexesUsed--;
			
			if (index == largestIndex)
				largestIndex--;
		}
	}

	/**
	 * Returns the object associated with the given index.
	 * @param index the index
	 * @return the object associated with the given index, or null if this index is not occupied
	 */
	public T get(long index)
	{
		return indexToObject.get(index);		
	}
	
	/**
	 * If the index has not been acquired yet, it is acquired.
	 * Otherwise, the previously acquired index for this objects is returned.
	 * @param object the object for which to find/acquire an index
	 * @return the index found/acquired, or 0 on error
	 */
	public long indexOf(T object)
	{
		Long l = objectToIndex.get(object); // for the null object, (l!=null) && (l.equals(0))
		if (l == null)
			return acquireIndex(object); // acquiring a new index for this object
		else
			return l;
		
	}
	
	/**
	 * Checks whether the given index is associated with some object.
	 * @param index the index to check
	 * @return whether the given index is associated with some object;
	 *   if index==0, returns <code>false</code>
	 */
	public boolean indexUsed(long index)
	{
		return (index!=0) && indexToObject.containsKey(index);
	}
	
	/*/*
	 * Test program
	 * @param args
	 *\
	public static void main(String args[])
	{
		Indexer<Object> i = new Indexer<Object>();
		i.setMaximumNumberOfBits(2);
		
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		Object o4 = new Object();
		System.out.println(i.acquireIndex(o1)+" "+o1);
		System.out.println(i.acquireIndex(o2)+" "+o2);
		i.set(1, o2);
		System.out.println(i.get(1));
		System.out.println(i.get(2));
		System.out.println(i.indexOf(o1));
		System.out.println(i.indexOf(o2));
		i.freeIndex(3);
		i.freeIndex(1);
		System.out.println(i.indexOf(o3));
		System.out.println(i.indexOf(o4));
	}*/
}
