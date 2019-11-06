package lv.lumii.tda.adapters.repository.ar;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import lv.lumii.tda.raapi.RAAPI_Synchronizable;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public interface IARMemory {
	
	
	public interface ActionsIterator {
		public int next(); // returns -1 if no more
		public int nextGreaterOrEqual(int value); // returns -1 if no more
	}
	
	public boolean reallocateFromCache();
	public boolean reallocate(int nActions, int nStrings);
	public void free(boolean clearCache);
	
	public boolean setMaxReference(long ref);
	public long getMaxReference();
	public boolean setPredefinedBits(int bitsCount, long bitsValues);
	public int getPredefinedBitsCount();
	public long getPredefinedBitsValues();
	public long newReference();
	
	public boolean hasMultiTypedObjects();

	public int addAction(double... arr);
	public int addStringAction(String s, double... arr);
	public int addRoleStringAction(String s, double... arr);
	public void deleteSimpleAction(int index);

	public double actions_get(int i);
	public String strings_get(int i);
	
	public ActionsIterator s2a_get(String s, boolean isRoleName);
	public int a2s_get(int index);
//	public List<Integer> r2a_get(double r); - excluded in favor of the two next functions
	public ActionsIterator r2a_get_iterator(double r);
	public int r2a_get_first(double r);
	public List<Integer> r2a_get_and_remove(double r);
	
	public long[] getAllClasses();
	
	public void storeActions(DataOutputStream fw1) throws IOException;
	public void storeStrings(BufferedWriter fw2) throws IOException;
	
	public boolean tryLock();
	public void lock();
	public void unlock();
	
	public void syncAllEx(RAAPI_Synchronizer synchronizer, int bitsCount, long bitsValues);
	
	public double getMemoryUsageFactor();
	public boolean memoryFault();
	
	public void rearrange();
	
}
