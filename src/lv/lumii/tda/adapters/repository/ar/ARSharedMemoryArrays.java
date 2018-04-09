package lv.lumii.tda.adapters.repository.ar;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class ARSharedMemoryArrays implements IARMemory {

	private static int stringHash(String s) {
		return Math.abs((s+"webAppOS").hashCode());
	}
	
	private RAAPI_Synchronizer synchronizer = null;
	
	
	private static Charset UTF8 = Charset.forName("UTF-8"); 
	private static boolean DEBUG = false;

	private static final int[] PRIMES_N = { 10007, // >INITIAL_N*1
			20011, // >INITIAL_N*2
			40009, // >INITIAL_N*4
			80021, // >INITIAL_N*8
			160001, // >INITIAL_N*16
			320009, // >INITIAL_N*32
			640007, // ...
			1280023, 2560021, 5120029, 10240033, 20480011, 40960001, 81920021, 163840007, 327680009, 655360001,
			1310720003 // this will be multiplied by sizeof(double)=8; taking
						// into a consideration other maps,
						// 1310720000 actions will lead to memory consumption up
						// to 32GB (for just this slot)
	};
/*
	private static final int[] PRIMES_N2 = { 50021, // >INITIAL_N*1/2
			10007, // >INITIAL_N*2/2
			20011, // >INITIAL_N*4/2
			40009, // >INITIAL_N*8/2
			80021, // >INITIAL_N*16/2
			160001, // >INITIAL_N*32/2
			320009, // >INITIAL_N*64/2
			640007, // ...
			1280023, 2560021, 5120029, 10240033, 20480011, 40960001, 81920021, 163840007, 327680009, 655360001 };
*/
	private static final int[] PRIMES_2S = { 2003, // >INITIAL_S*2*1
			4001, // >INITIAL_S*2*2
			8009, // >INITIAL_S*2*4
			16001, // >INITIAL_S*2*8
			32003, // >INITIAL_S*2*16
			64007, // >INITIAL_S*2*32
			128021, // ...
			256019, 512009, 1024021, 2048003, 4096013, 8192003, 16384001, 32768011, 65536043, 131072003, 262144009 };

	// TODO: allow configure (max ops cnt, max mem size..)
	// TODO: check for max size

	private static final int INITIAL_N = 10000;
	private static final int INITIAL_S = 1000;
	private static final int AVG_S_LEN = 25 + 5;
	
	private static final int MAX_S_LEN = 4096;

	// ar.common.shm indixes...
	private static final int I_LOCK_FLAG = 0;
		// 0=not-initialized, 1=initialized&free, 2=bridge access, 3=processor access, 4=processor-to-bridge sync
	private static final int I_ACTIONS_LEN = 1;
	private static final int I_STRINGS_LEN = 2;
	private static final int I_FACTOR_N_LOG = 3;
	private static final int I_FACTOR_S_LOG = 4;
	private static final int I_CURMAX_N = 5;
	private static final int I_CURMAX_S = 6;
	private static final int I_DELETED_N = 7;
	private static final int I_DELETED_S = 8;
	private static final int I_REMAP_CHARS = 9;
	private static final int I_REMAP_ACTIONS = 10;
	private static final int I_REMAP_STRINGS = 11;
	private static final int I_CHARS_LEN = 12;
	private static final int I_CURMAX_CHARS = 13; // in UTF-8 encoding, counting delimiters
	
	private static final int I_MAXREF_LOW = 14; 
	private static final int I_MAXREF_HI = 15; 
	private static final int I_SYNC_ACTIONS_LEN = 16; 
	private static final int I_SYNC_ACTIONS_MAXLEN = 17; 
	private static final int I_SYNC_STRINGS_LEN = 18; 
	private static final int I_SYNC_STRINGS_MAXLEN = 19;
	
	private static final int I_HAS_MULTI_TYPED_OBJECTS = 20;
	
	// reserved until N_INDICES
	
	private static final int N_INDICES = 32; // > last index

	private String dir;

	private boolean isAllocated = false;
	private boolean isShmServer = false;

	private RandomAccessFile fCommon;
	private RandomAccessFile fChars;
	private RandomAccessFile fActions;
	private RandomAccessFile fStrings;

	private IntBuffer common;	
	private DoubleBuffer maxRef;
	private ByteBuffer syncActions;
	private ByteBuffer syncStrings;
	private ByteBuffer chars;

	private DoubleBuffer actions;
	private DoubleBuffer r2a_r;
	private IntBuffer r2a_a;

	private IntBuffer strings2;
	private IntBuffer s2a_s, s2a_a, a2s_a, a2s_s;

	public ARSharedMemoryArrays(String dir, boolean isShmServer) {
		this.dir = dir;
		this.isShmServer = isShmServer;
	}

	
	private void deleteShmFiles() {
		File f;
		f = new File(dir + File.separator + "ar.common.shm");
		if (f.exists())
			f.delete();
		f = new File(dir + File.separator + "ar.chars.shm");
		if (f.exists())
			f.delete();
		f = new File(dir + File.separator + "ar.actions.shm");
		if (f.exists())
			f.delete();
		f = new File(dir + File.separator + "ar.strings.shm");
		if (f.exists())
			f.delete();		
	}
	
	public boolean reallocateFromCache() {
		boolean b = 
				(new File(dir+File.separator+"ar.common.shm").exists()) &&
				(new File(dir+File.separator+"ar.chars.shm").exists()) &&
				(new File(dir+File.separator+"ar.actions.shm").exists()) &&
				(new File(dir+File.separator+"ar.strings.shm").exists());
		
		if (!b) {
			deleteShmFiles();
			return false;
		}

		try {
			fCommon = new RandomAccessFile(dir + File.separator + "ar.common.shm", "rw");			
			fChars = new RandomAccessFile(dir + File.separator + "ar.chars.shm", "rw");
			fActions = new RandomAccessFile(dir + File.separator + "ar.actions.shm", "rw");
			fStrings = new RandomAccessFile(dir + File.separator + "ar.strings.shm", "rw");
		} catch (Throwable t) {
			return false;
		}
		
		// mapping fCommon...
		try {
			common = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, N_INDICES * 4).asIntBuffer();
			
			maxRef = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 56, 8).asDoubleBuffer();
			
			syncActions = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 80, common.get(I_SYNC_ACTIONS_MAXLEN)*8); // this shm server doesn't need to be aware of doubles here
			syncStrings = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 80+common.get(I_SYNC_ACTIONS_MAXLEN)*8, common.get(I_SYNC_STRINGS_MAXLEN));
			
		} catch (IOException e) {
			return false;
		}
		
		
		if (isShmServer) {
			
			common.put(I_LOCK_FLAG, 1);
			
			lock();
			
			common.put(I_REMAP_CHARS, 1); // marking as if the client requested to re-map
			common.put(I_REMAP_ACTIONS, 1);
			common.put(I_REMAP_STRINGS, 1);
		}
		else {
			lock();
			common.put(I_REMAP_CHARS, 2); // marking as if the server requested to re-map
			common.put(I_REMAP_ACTIONS, 2);
			common.put(I_REMAP_STRINGS, 2);
		}
			
		b = remapChars(false) && remapActions(false) && remapStrings(false);
		unlock();
		isAllocated = b;
		return b;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see lv.lumii.tda.adapters.repository.ar.IARMemory#reallocate(int, int)
	 * 
	 * 
	 * if (!isShmServer) just connects to the shared memory (ignores nActions
	 * and nStrings)
	 */
	public boolean reallocate(int nActions, int nStrings) {
		if (!isShmServer) {			
			return reallocateFromCache();
		}

		// delete existing files...
		deleteShmFiles();

		try {
			fCommon = new RandomAccessFile(dir + File.separator + "ar.common.shm", "rw");
			fChars = new RandomAccessFile(dir + File.separator + "ar.chars.shm", "rw");
			fActions = new RandomAccessFile(dir + File.separator + "ar.actions.shm", "rw");
			fStrings = new RandomAccessFile(dir + File.separator + "ar.strings.shm", "rw");
		} catch (Throwable t) {
			return false;
		}

		int factorNLog = 0;
		int nSize = INITIAL_N;
		while (nActions > nSize) {
			nSize *= 2;
			factorNLog++;
		}

		int factorSLog = 0;
		int sSize = INITIAL_S;
		while (nStrings > sSize) {
			sSize *= 2;
			factorSLog++;
		}

		try {
			fCommon.setLength(N_INDICES * 4); // 4=sizeof(int),																			
			fChars.setLength(sSize * AVG_S_LEN); 
			fActions.setLength(nSize * 8 + PRIMES_N[factorNLog] * 8 + PRIMES_N[factorNLog] * 4); // INITIAL_N
																									// doubles
																									// +
																									// PRIMES_N[factorNLog]
																									// doubles
																									// +
																									// PRIMES_N[factorNLog]
																									// ints
			fStrings.setLength(4 * (sSize * 2 + PRIMES_2S[factorSLog] * 4)); // the
																				// first
																				// 4=sizeof(int)
		} catch (Throwable t) {
			return false;
		}

		// mapping fCommon...
		try {
			common = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, N_INDICES * 4).asIntBuffer();
			
			maxRef = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 56, 8).asDoubleBuffer();
			
			syncActions = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 80, common.get(I_SYNC_ACTIONS_MAXLEN)*8); // this shm server doesn't need to be aware of doubles here
			syncStrings = fCommon.getChannel().map(FileChannel.MapMode.READ_WRITE, 80+common.get(I_SYNC_ACTIONS_MAXLEN)*8, common.get(I_SYNC_STRINGS_MAXLEN));
		} catch (IOException e) {
			return false;
		}
		
		maxRef.put(0, RepositoryAdapter.MAX_PRIMITIVE_REFERENCE);

		common.put(I_LOCK_FLAG, 1);
		
		lock();
		
		common.put(I_ACTIONS_LEN, nSize);
		common.put(I_STRINGS_LEN, sSize);
		common.put(I_CHARS_LEN, sSize * AVG_S_LEN);
		common.put(I_FACTOR_N_LOG, factorNLog);
		common.put(I_FACTOR_S_LOG, factorSLog);
		
		common.put(I_HAS_MULTI_TYPED_OBJECTS, 0);

		// curmaxS = 0
		// deletedN, deletedS = 0

		
		// TODO: check overflows

		common.put(I_REMAP_CHARS, 1); // marking as if the client requested to re-map
		common.put(I_REMAP_ACTIONS, 1);
		common.put(I_REMAP_STRINGS, 1);
		
		boolean errFlag = !remapChars(false) || !remapActions(false) || !remapStrings(false);
		unlock();
		// curmaxN=1:
		common.put(I_CURMAX_N, 1); 
		if (errFlag)
			return false;
		actions.put(0, 0xEE);
		isAllocated = true;
		return true;
	}

	public void free(boolean clearCache) {
		if (isAllocated) {
			if (isShmServer) {
				try {
					fCommon.close();
					fChars.close();
					fActions.close();
					fStrings.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (clearCache)
					deleteShmFiles();
			}
		}
		isAllocated = false;
	}

	private boolean remapChars(boolean force) {
		if (!force) {
			if (common.get(I_REMAP_CHARS) == 0)
				return true; // all was OK
			
			if (isShmServer) {
				if (common.get(I_REMAP_CHARS) == 2) // the server (we) requested to re-map, ignoring...
					return true;
			}
			else {
				if (common.get(I_REMAP_CHARS) == 1) // the client (we) requested to re-map, ignoring...
					return true;				
			}
			
			common.put(I_REMAP_CHARS, 0);
		}

		try {
			chars = fChars.getChannel()
					.map(FileChannel.MapMode.READ_WRITE, 0, common.get(I_CHARS_LEN));
		} catch (IOException e) {
			return false;
		}
		
		if (force) {
			// inform the other side that it has to re-map chars
			if (isShmServer)
				common.put(I_REMAP_CHARS, 2);
			else
				common.put(I_REMAP_CHARS, 1);			
		}
 		
		return true;
	}

	private boolean remapActions(boolean force) {
		if (!force) {
			if (common.get(I_REMAP_ACTIONS) == 0)
				return true; // all was OK
			common.put(I_REMAP_ACTIONS, 0);
		}

		int offset1 = common.get(I_ACTIONS_LEN) * 8;
		int offset2 = offset1 + PRIMES_N[common.get(I_FACTOR_N_LOG)] * 8;

		try {
			actions = fActions.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, common.get(I_ACTIONS_LEN) * 8).asDoubleBuffer();
			r2a_r = fActions.getChannel()
					.map(FileChannel.MapMode.READ_WRITE, offset1, PRIMES_N[common.get(I_FACTOR_N_LOG)] * 8)
					.asDoubleBuffer();
			r2a_a = fActions.getChannel()
					.map(FileChannel.MapMode.READ_WRITE, offset2, PRIMES_N[common.get(I_FACTOR_N_LOG)] * 4)
					.asIntBuffer();
		} catch (Throwable t) {
			return false;
		}
		
		if (force) {
			// inform the other side that it has to re-map actions
			if (isShmServer)
				common.put(I_REMAP_ACTIONS, 2);
			else
				common.put(I_REMAP_ACTIONS, 1);			
		}
		return true;
	}

	private boolean remapStrings(boolean force) {
		if (!force) {
			if (common.get(I_REMAP_STRINGS) == 0)
				return true; // all was OK
			common.put(I_REMAP_STRINGS, 0);
		}

		int oneLen = PRIMES_2S[common.get(I_FACTOR_S_LOG)] * 4;

		int offset1 = common.get(I_STRINGS_LEN)*2 * 4;
		int offset2 = offset1 + oneLen;
		int offset3 = offset2 + oneLen;
		int offset4 = offset3 + oneLen;

		try {
			strings2 = fStrings.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, offset1).asIntBuffer();

			s2a_s = fStrings.getChannel().map(FileChannel.MapMode.READ_WRITE, offset1, oneLen).asIntBuffer();
			s2a_a = fStrings.getChannel().map(FileChannel.MapMode.READ_WRITE, offset2, oneLen).asIntBuffer();
			a2s_a = fStrings.getChannel().map(FileChannel.MapMode.READ_WRITE, offset3, oneLen).asIntBuffer();
			a2s_s = fStrings.getChannel().map(FileChannel.MapMode.READ_WRITE, offset4, oneLen).asIntBuffer();
		} catch (Throwable t) {
			return false;
		}
		if (force) {
			// inform the other side that it has to re-map strings
			if (isShmServer)
				common.put(I_REMAP_STRINGS, 2);
			else
				common.put(I_REMAP_STRINGS, 1);			
		}
		return true;
	}

	public IARMemory.ActionsIterator s2a_get(String s, boolean isRoleName) {
		if (s == null)
			return null;

		int h = stringHash(s);
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int hash1 = (h % s2a_a.limit());
		int hash2 = (h % (s2a_a.limit() - 2)) + 1; // co-prime
																		// with
																		// hash1
																		// (hash
																		// 2 is
																		// between
																		// 1 and
																		// s2a_a.lenghth-1)

		if (isRoleName) {
			String s1 = "/" + s;
			String s2 = s + "/";
			int si;
			String ssi;
			while (s2a_a.get(hash1) != 0) {
				si = s2a_s.get(hash1); // string index
				ssi = strings_get(si);
				if ((ssi != null) && (ssi.endsWith(s1) || ssi.startsWith(s2)))
					if (s2a_a.get(hash1)>0)
						retVal.add(s2a_a.get(hash1));
				hash1 = hash1 + hash2;
				if (hash1 >= s2a_a.limit())
					hash1 -= s2a_a.limit();
			}
		} else {
			int si;
			while (s2a_a.get(hash1) != 0) {
				si = s2a_s.get(hash1); // string index
				if (s.equals(strings_get(si)))
					if (s2a_a.get(hash1)>0)
						retVal.add(s2a_a.get(hash1));
				hash1 = hash1 + hash2;
				if (hash1 >= s2a_a.limit())
					hash1 -= s2a_a.limit();
			}
		}

		return new ActionsIteratorOverArrayList(retVal);
	}

	private void s2a_simple_add(String s, int si, int ai, boolean isRoleName) {
		if (s == null)
			return;
		
		int h = stringHash(s);
		int hash1 = h;
		if (hash1>=s2a_a.limit())
			hash1 = (h % s2a_a.limit());
		int hash2 = h+1;
		if (hash2>=s2a_a.limit()-2)
			hash2 = (h % (s2a_a.limit()-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)

		
		int firstOur = -1;
		int ourLen = 0;
		// searching for the first empty cell...
		
		String s1 = "/"+s;
		String s2 = s+"/";
		int _si;
		String ssi;
		ArrayList<String> arr = new ArrayList<String>();
		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		while ((s2a_a.get(hash1)!=0)&&(s2a_a.get(hash1)!=-1)) {  // !=0 and !=-1
			if (firstOur==-1) {
				_si = s2a_s.get(hash1);
				ssi = strings_get(_si);
				
				boolean check = (ssi!=null) &&
						( (!isRoleName&&ssi.equals(s)) || 
							(isRoleName && (ssi.startsWith(s2)||(ssi.endsWith(s1)))) ); 
				
				if (check) {
					// this is the first "our" cell...
					firstOur = hash1;
					if (s2a_a.get(hash1)<-1) {
						// this cell contains the length of the list...
						ourLen = -s2a_a.get(hash1); // ourLen includes its own cell containing the length
						hash1 = (int) (((long)hash1+(long)hash2*(long)ourLen) % (long)s2a_a.limit()); // !handling overflow via long
						
						continue;
					}
					else
						ourLen=1;
				}
				else {
					arr2.add(s2a_a.get(hash1));
					arr.add(ssi);
				}
			}
			else
				ourLen++;
			hash1 = hash1+hash2;
			if (hash1>=s2a_a.limit())
				hash1-=s2a_a.limit();
		}
		
		// now hash1 is the index of the free cell...
		// ourLen - how many times we have to skip the list to get the pointer to the last element (we will add this last element now),
		// thus, s2a_a[firstOur] shall contain -ourLen
		
		if (firstOur>=0) {
			if (s2a_a.get(firstOur)<-1) {
				
				// the list already contains 2 or more elements...
				s2a_s.put(hash1, si);
				s2a_a.put(hash1, ai);
				s2a_a.put(firstOur, -ourLen);
				
			}
			else {				
				// the list contains only one element...
				int el = s2a_a.get(firstOur);
				s2a_s.put(hash1, s2a_s.get(firstOur));
				s2a_a.put(hash1, el); // moving the first element
				// ourLen now is at least 2 (or more, if we skipped some elements during the previous while)
				
				// adding the second element...
				hash1 = hash1+hash2;
				if (hash1>=s2a_a.limit())
					hash1-=s2a_a.limit();
				ourLen++;

				while ((s2a_a.get(hash1) != 0) && (s2a_a.get(hash2)!=-1)) {  // !=0 and !=-1
					ourLen++;
					hash1 = hash1+hash2;
					if (hash1>=s2a_a.limit())
						hash1-=s2a_a.limit();
				}			
				
				s2a_s.put(hash1, si);
				s2a_a.put(hash1, ai); 
				s2a_a.put(firstOur, -ourLen); // storing the len
			}
		}
		else {
			// adding the first element...
			s2a_s.put(hash1, si);
			s2a_a.put(hash1, ai);			
		}
		
	}

	public int a2s_get(int index) {
		int hash1 = (index % a2s_a.limit());
		int hash2 = (index % (a2s_a.limit() - 2)) + 1; // co-prime with hash1
														// (hash 2 is between 1
														// and s2a_a.lenghth-1)
		while (a2s_a.get(hash1) != 0) {
			if (a2s_a.get(hash1) == index)
				return a2s_s.get(hash1); // found
			hash1 = hash1 + hash2;
			if (hash1 >= a2s_a.limit())
				hash1 -= a2s_a.limit();
		}

		return -1; // not found
	}

	private int a2s_get_and_remove(int ai) {
		int hash1 = (ai % a2s_a.limit());
		int hash2 = (ai % (a2s_a.limit() - 2)) + 1; // co-prime with hash1 (hash
													// 2 is between 1 and
													// s2a_a.lenghth-1)

		while (a2s_a.get(hash1) != 0) {
			if (a2s_a.get(hash1) == ai) {
				a2s_a.put(hash1, -1); // removed
				return a2s_s.get(hash1); // found
			}
			hash1 = hash1 + hash2;
			if (hash1 >= a2s_a.limit())
				hash1 -= a2s_a.limit();
		}

		return -1; // not found
	}

	private void a2s_put(int ai, int si) { // put or replace
		int hash1 = (ai % a2s_a.limit());
		int hash2 = (ai % (a2s_a.limit() - 2)) + 1; // co-prime with hash1 (hash
													// 2 is between 1 and
													// s2a_a.lenghth-1)

		while ((a2s_a.get(hash1) > 0) && (a2s_a.get(hash1) != ai)) { // while
																		// !=0
																		// and
																		// !=-1
			hash1 = hash1 + hash2;
			if (hash1 >= a2s_a.limit())
				hash1 -= a2s_a.limit();
		}

		a2s_a.put(hash1, ai);
		a2s_s.put(hash1, si);
	}

/* excluded:
 	public ArrayList<Integer> r2a_get(double r) {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int hash1 = ((int) r % (int) r2a_a.limit());
		int hash2 = ((int) r % (int) (r2a_a.limit() - 2)) + 1; // co-prime with
																// hash1 (hash 2
																// is between 1
																// and
																// r2a_a.lenghth-1)

		while (r2a_a.get(hash1) != 0) {
			if (r2a_r.get(hash1) == r) {
				if (r2a_a.get(hash1) >= 0) // negative numbers specify the skip
											// length or deleted elements
					retVal.add(r2a_a.get(hash1));
			}
			hash1 = hash1 + hash2;
			if (hash1 >= r2a_a.limit())
				hash1 -= r2a_a.limit();
		}

		return retVal;
	}*/
	
	
	private class R2AImpl implements IARMemory.ActionsIterator {
		private double r;
		private int hash1, hash2, len;
		public R2AImpl(double _r, int _hash1, int _hash2, int _len) {
			r = _r;
			hash1 = _hash1;
			hash2 = _hash2;
			len = _len;
		}

		@Override
		public int next() {
			
			//if (len<=0)
				//return -1;
			
			while (r2a_a.get(hash1) != 0) {
				len--;
				if (r2a_r.get(hash1) == r) {
					if (r2a_a.get(hash1)>=0) { // negative numbers specify the skip length or deleted elements						
						int tmp = r2a_a.get(hash1);
						hash1 = hash1+hash2;
						if (hash1>=r2a_a.limit())
							hash1-=r2a_a.limit();
						return tmp;
					}
				}
				hash1 = hash1+hash2;
				if (hash1>=r2a_a.limit())
					hash1-=r2a_a.limit();
			}			
			
			return -1;
		}
		
		private int get(int index) {
			int h = (int) (((long)hash1+(long)hash2*(long)index)%(long)r2a_a.limit()); // !handling overflow via long
			
			if ((r2a_r.get(h) == r) && (r2a_a.get(h)>=0)) 			
				return r2a_a.get(h);
			
			return -1;
		}
		
		private int findIndex(int l, int r, int rightMost, int value) {
			// searching within [i;j], where get(r) is the end or >=value
			
			while (l<=r) {				
				int m = (l+r)/2;
						
				int m1 = m;
				int v = get(m);
				while ((m1<=r)&(v==-1)) { // skip right
					m1++;
					v = get(m1);
				}
				
				
				if (m1>r) { // skipped to the end
					r=m-1; // continue with the left side, leaving rightMost
					continue;
				}
				
				
				if (v == value) {
					return m1;
				} else {
					if (v < value) {
						l = m1+1;
					} else {
						r=m-1; // r decreases
						rightMost=m1;
					}
				}
			}
			
			int val = get(l);
			if ((val!=-1) && (val>=value))
				return l;
			
			// check the rightMost element
			val = get(rightMost);
			if ((val!=-1) && (val>=value))
				return rightMost;
			
			return -1;
		}

		private void dbg(int i, int j, int value, int index) {
			System.err.print("next >= "+value+" [");
			for (int k=i; k<=j; k++)
				System.err.print(get(k)+" ");
			System.err.println("] found index="+index);
		}
		@Override
		public int nextGreaterOrEqual(int value) {

			/*// check...
			int savedLen = len;
			int savedHash1 = hash1;
			
			int v = next();
			while ((v!=-1)&&(v<value))
				v=next();
			
			int hash1_to_check = hash1;
			int len_to_check = len;
			len = savedLen;
			hash1 = savedHash1;// ...check*/
			
			
			
			int i = findIndex(0, len-1, len, value);
			
			
			if (i==-1) {
				// not found;
				/*// check...
				if (v!=-1) {
					System.err.println("ERROR WHEN NOT FOUND (must be value "+v+"): "+savedLen+"/"+len_to_check+"/"+len);
					dbg(0, len-1, value, i);
				}*/
				
				len = 0;
				return -1;
			}
			
			
			hash1 = (int) (((long)hash1+(long)hash2*(long)i)%(long)r2a_a.limit()); // move forward; // !handling overflow via long
			len = len-i;
			
			int tmp = r2a_a.get(hash1);
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.limit())
				hash1-=r2a_a.limit();
			len--;

			/*// check...
			if ((len!=len_to_check) || (hash1!=hash1_to_check)) {
				System.err.println("ERROR WHEN CHECK (must be len/hash/value "+len_to_check+"/"+hash1_to_check+"/"+v+")");
				System.err.println("  BUY GOT "+len+"/"+hash1+"/"+tmp);				
			}*/
			
			
			return tmp;
		}
		
	}
	
	private class SingleActionIterator implements IARMemory.ActionsIterator {
		private int val;
		public SingleActionIterator(int _val) {
			val = _val;
		}
		@Override
		public int next() {
			if (val==-1)
				return -1;
			int tmp = val;
			val = -1;
			return tmp;
		}

		@Override
		public int nextGreaterOrEqual(int value) {
			if (val==-1)
				return -1;
			if (val < value) {
				val = -1;
				return -1;
			}
			int tmp = val;
			val = -1;
			return tmp;
		}
		
	}
	
	private static class EmptyActionsIterator implements IARMemory.ActionsIterator {
		public static EmptyActionsIterator INSTANCE = new EmptyActionsIterator();
		@Override
		public int next() {
			return -1;
		}

		@Override
		public int nextGreaterOrEqual(int value) {
			return -1;
		}
		
	}
	
	
	
	@Override
	public ActionsIterator r2a_get_iterator(double r) {
		int di = (int)r;
		int hash1 = di;
		if (hash1>=r2a_a.limit())
			hash1 = di%r2a_a.limit();
		int hash2 = di;
		if (hash2>=r2a_a.limit()-2)
			hash2 = di%(r2a_a.limit()-2);
		hash2++;
				
		// searching for the first our cell...
		
		while ((r2a_a.get(hash1)!=0)&&(r2a_a.get(hash1)!=-1)) {  // !=0 and !=-1
			if (r2a_r.get(hash1)==r) {
				// this is the first "our" cell...
				if (r2a_a.get(hash1)<-1) {
					// this cell contains the length of the list...
					int ourLen = -r2a_a.get(hash1);
					
					return new R2AImpl(r, hash1, hash2, ourLen+1);
				}
				else {
					return new SingleActionIterator(r2a_a.get(hash1));
				}
			}
			else {				
				hash1 = hash1+hash2;
				if (hash1>=r2a_a.limit())
					hash1-=r2a_a.limit();
			}
		}
		
		return EmptyActionsIterator.INSTANCE;
	}


	@Override
	public int r2a_get_first(double r) {
		
		int di = (int)r;
		int hash1 = di;
		if (hash1>=r2a_a.limit())
			hash1 = di%r2a_a.limit();
		int hash2 = di;
		if (hash2>=r2a_a.limit()-2)
			hash2 = di%(r2a_a.limit()-2);
		hash2++;

		while (r2a_a.get(hash1) != 0) {
			if (r2a_r.get(hash1) == r) {
				if (r2a_a.get(hash1)>=0) { // negative numbers specify the skip length or deleted elements
					return r2a_a.get(hash1);
				}
			}
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.limit())
				hash1-=r2a_a.limit();
		}			

		return -1; // not found
	}
	

	public ArrayList<Integer> r2a_get_and_remove(double r) {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int hash1 = ((int) r % r2a_a.limit());
		int hash2 = ((int) r % (r2a_a.limit() - 2)) + 1; // co-prime with hash1
															// (hash 2 is
															// between 1 and
															// r2a_a.lenghth-1)

		while (r2a_a.get(hash1) != 0) {
			if (r2a_r.get(hash1) == r) {
				if (r2a_a.get(hash1) >= 0) // negative numbers specify the skip
											// length or deleted elements
					retVal.add(r2a_a.get(hash1));
				r2a_a.put(hash1, -1); // removed
			}
			hash1 = hash1 + hash2;
			if (hash1 >= r2a_a.limit())
				hash1 -= r2a_a.limit();
		}

		return retVal;
	}

	private void r2a_add(double d, int i) {
		int di = (int)d;
		int hash1 = di;
		if (hash1>=r2a_a.limit())
			hash1 = di%r2a_a.limit();
		int hash2 = di;
		if (hash2>=r2a_a.limit()-2)
			hash2 = di%(r2a_a.limit()-2);
		hash2++;
		
		
		int firstOur = -1;
		int ourLen = 0;
		// searching for the first empty cell...
		
		while ((r2a_a.get(hash1)!=0)&&(r2a_a.get(hash1)!=-1)) {  // !=0 and !=-1
			if (firstOur==-1) {
				if (r2a_r.get(hash1)==d) {
					// this is the first "our" cell...
					firstOur = hash1;
					if (r2a_a.get(hash1)<-1) {
						// this cell contains the length of the list...
						ourLen = -r2a_a.get(hash1); // ourLen includes its own cell containing the length
						hash1 = (int) (((long)hash1+(long)hash2*(long)ourLen) % (long)r2a_a.limit()); // !handling overflow via long
						continue;
					}
					else
						ourLen=1;
				}
			}
			else
				ourLen++;
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.limit())
				hash1-=r2a_a.limit();
		}
		
		// now hash1 is the index of the free cell...
		// ourLen - how many times we have to skip the list to get the pointer to the last element (we will add this last element now),
		// thus, r2a_a[firstOur] shall contain -ourLen
		
		if (firstOur>=0) {
			if (r2a_a.get(firstOur)<-1) {
				
				// the list already contains 2 or more elements...
				r2a_r.put(hash1, d);
				r2a_a.put(hash1, i);
				r2a_a.put(firstOur, -ourLen);
				
			}
			else {				
				// the list contains only one element...
				int el = r2a_a.get(firstOur);
				r2a_r.put(hash1, d);
				r2a_a.put(hash1, el); // moving the first element
				// ourLen now is at least 2 (or more, if we skipped some elements during the previous while)
				
				// adding the second element...
				hash1 = hash1+hash2;
				if (hash1>=r2a_a.limit())
					hash1-=r2a_a.limit();
				ourLen++;
				
				while ((r2a_a.get(hash1) != 0) && (r2a_a.get(hash2)!=-1)) {  // !=0 and !=-1
					ourLen++;
					hash1 = hash1+hash2;
					if (hash1>=r2a_a.limit())
						hash1-=r2a_a.limit();
				}			
				
				r2a_r.put(hash1, d);
				r2a_a.put(hash1, i); 
				r2a_a.put(firstOur, -ourLen); // storing the len
			}
		}
		else {
			// adding the first element...
			r2a_r.put(hash1, d);
			r2a_a.put(hash1, i);			
		}
	}

	private void s2a_smart_add(String s, int si, int ai, boolean isRoleString) {
		if (s==null)
			return;				
		
		if (isRoleString) {
			int k = s.indexOf('/'); // for association role name/inverse role name
			if (k>=0) {
				String sub1 = s.substring(0, k);
				String sub2 = s.substring(k+1);
				
				if (!sub1.isEmpty()) {
					s2a_simple_add(sub1, si, ai, true);
				}
				if (!sub2.isEmpty()) {
					s2a_simple_add(sub2, si, ai, true);
				}
			}
		}
		else
			s2a_simple_add(s, si, ai, false);
	}
	

	/**
	 * Re-arranges the actions array (increases/decreases array size, if
	 * needed).
	 * 
	 * @param additionalN
	 *            How many elements in the actions array are to be added. The
	 *            number of elements depends on the length+1 of actions. If <=0,
	 *            then the array is re-arranged for save purposes (e.g.,
	 *            elements of deleted actions are excluded).
	 */
	private void rearrangeActions(int additionalN) {

		int _curmaxN = common.get(I_CURMAX_N);
		int _deletedN = common.get(I_DELETED_N);

		if (additionalN > 0) {
			// check memory

			if (_curmaxN + additionalN <= common.get(I_ACTIONS_LEN))
				return; // room ok

			// do not decrease memory size, since:
			// * in windows there will be an exception, if open shared
			// memory is resized
			// * we also need to move elements to the beginning before
			// shrinking the memory

			do {
				// increase memory before rearrange
/*				try {
					fActions.setLength(fActions.length() * 2);
				} catch (IOException e) {
					// TODO:
					e.printStackTrace();
				}*/
				common.put(I_ACTIONS_LEN, common.get(I_ACTIONS_LEN)*2);
				common.put(I_FACTOR_N_LOG, common.get(I_FACTOR_N_LOG) + 1);
				
			} while (_curmaxN+additionalN>common.get(I_ACTIONS_LEN));
				
			remapActions(true); // remaps actions, r2a_r, r2a_a; increases length
			// TODO: check overflows, max memory 
		}

		// rearrange

		int i, j, n;
		
		// clear s2a map...
		n = s2a_a.limit();
		for (i=0; i<n; i++)
			s2a_a.put(i,0);

		// clear r2a map...
		n = r2a_a.limit();
		for (i=0; i<n; i++)
			r2a_a.put(i,0);

		// do not need to re-arrange a2s, since factorSLog has not changed
		
		i=0; j=0;

		while (j < _curmaxN) {
			if (actions.get(j) == 0x00) { // deleted action
				j++; // skip this action id
				if (j < _curmaxN) {
					j += actions.get(j); // increasing j by action size stored there...
				}
				continue;
			}

			// non-deleted action
			int actionsDelta = RepositoryAdapter.getOpSize(actions.get(j));

			if (RepositoryAdapter.isStringOp(actions.get(j))) {
				if (DEBUG) {
					if (this.a2s_get(j) < 0) {
						System.err.println("!!!! wrong map value for " + j + "->" + i + "; action = " + actions.get(j) + " "
								+ _curmaxN + " arr:" + "->" + actions.limit());
						int opsize = RepositoryAdapter.getOpSize(actions.get(j));
						for (int k = 1; k <= opsize; k++)
							System.err.print(actions.get(j + k) + " ");
						System.err.println();
					}
				}
				int si = a2s_get_and_remove(j);
				s2a_smart_add(strings_get(si), si, i, actions.get(j) == 0x05 || actions.get(j) == 0x15);
				a2s_put(i, si);
			}

			actions.put(i, actions.get(j)); // move action id...

			int ii = i;

			while (actionsDelta > 0) { // move action
				i++;
				j++;
				actions.put(i, actions.get(j));
				actionsDelta--;
				if (actions.get(i) > RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) { // this
																				// is
																				// not
																				// some
																				// primitive
																				// type
																				// reference;
																				// neither
																				// boolean
																				// 0
																				// or
																				// 1
																				// value
					r2a_add(actions.get(i), ii);
				}
			}

			i++;
			j++;
		}
		
		common.put(I_CURMAX_N, i); // curmaxN = i
		common.put(I_DELETED_N, 0); // deletedN = 0
	}

	/**
	 * Re-arranges the strings array (increases/decreases array size, if
	 * needed).
	 * 
	 * @param additionalS
	 *            How many strings are to be added to the strings array. If <=0,
	 *            then the array is re-arranged for save purposes (e.g., deleted
	 *            strings are excluded).
	 */
	private void rearrangeStrings(int additionalS) {
		int _curmaxS = common.get(I_CURMAX_S);
		


		if (additionalS > 0) {
			
			
			// check memory...
			if (_curmaxS + additionalS < strings2.limit()/2) 
				return; // room ok

			// do not decrease memory size, since:
			// * in windows there will be an exception, if open shared
			// memory is resized
			// * we also need to move elements to the beginning before
			// shrinking the memory
			
			do {
				// increase memory before rearrange
				// here we extend only strings; chars will be extended separately (when needed)...
/*				try {
					fStrings.setLength(common.get(I_STRINGS_LEN)*2);					
				} catch (IOException e) {
					// TODO:
					e.printStackTrace();
				}*/
				
				common.put(I_STRINGS_LEN, common.get(I_STRINGS_LEN)*2);
				common.put(I_FACTOR_S_LOG, common.get(I_FACTOR_S_LOG) + 1);
				
				
			} while (_curmaxS + additionalS >= strings2.limit()/2);

			// TODO: check overflows, max memory 
			
			remapStrings(true); // remaps strings2, s2a, a2s, increases lengths 
		}

		
		// getting a2s inverse...
		Map<Integer, Integer> inv = new HashMap<Integer, Integer>();
		for (int k = 0; k < a2s_a.limit(); k++) {
			if (a2s_a.get(k) > 0)
				inv.put(a2s_s.get(k), a2s_a.get(k));
		}
		
		int i, j, n;
		
		// clear a2s map...
		n = a2s_a.limit();
		for (i=0; i<n; i++)
			a2s_a.put(i,0);
		
		// clear s2a map...
		n = s2a_a.limit();
		for (i=0; i<n; i++)
			s2a_a.put(i,0);

		i=0; j=0;
		// rearrange
		while (j < _curmaxS) {
			if (strings_get(j) == null) { // deleted string
				j++; // skip this string
				continue;
			}

			// non-deleted string
			String str = strings_get(j);
			
			// move string...
			strings_move(i, j); // strings[i]<-strings[j]; strings[j]<-null;

			int ai = inv.get(j);

			// remove ai->j, put ai->i
			a2s_put(ai, i);

			s2a_smart_add(str, i, ai, actions.get(ai) == 0x05 || actions.get(ai) == 0x15);

			i++;
			j++;
		}
		
		common.put(I_CURMAX_S, i); // curmaxS = i
		common.put(I_DELETED_S, 0); // deletedS = 0
	}

	public int addAction(double... arr) {
		rearrangeActions(arr.length);
		int retVal = common.get(I_CURMAX_N);
		actions.put(retVal, arr[0]);
		
		if ((arr[0]==0x12) || (arr[0]==0xE2))
			common.put(I_HAS_MULTI_TYPED_OBJECTS, 1);
		
		for (int i = 1; i < arr.length; i++) {
			actions.put(retVal + i, arr[i]);
			if (arr[i] > RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) {
				r2a_add(arr[i], retVal);
				if (arr[i]>maxRef.get(0))
					maxRef.put(0, arr[i]);
			}
		}
		common.put(I_CURMAX_N, retVal+arr.length);
		return retVal;
	}

	public int addStringAction(String s, double... arr) {
		if (s == null)
			s = "";
		if (s.length()>MAX_S_LEN)
			s = s.substring(0, MAX_S_LEN);
		rearrangeActions(arr.length);
		int retVal = common.get(I_CURMAX_N);
		actions.put(retVal, arr[0]);
		for (int i = 1; i < arr.length; i++) {
			actions.put(retVal + i, arr[i]);
			if (arr[i] > RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) {
				r2a_add(arr[i], retVal);
				if (arr[i]>maxRef.get(0))
					maxRef.put(0, arr[i]);
			}
		}
		common.put(I_CURMAX_N, retVal+arr.length);

		rearrangeStrings(1);
		int si = common.get(I_CURMAX_S);
		
		a2s_put(retVal, si);
		s2a_smart_add(s, si, retVal, arr[0] == 0x05 || arr[0] == 0x15);
		
		strings_put(si, s);		
		common.put(I_CURMAX_S, si+1);

		return retVal;
	}

	public int addRoleStringAction(String s, double... arr) {
		if (s == null)
			s = "";
		
		if (s.length()>MAX_S_LEN)
			s = s.substring(0, MAX_S_LEN);
		rearrangeActions(arr.length);
		int retVal = common.get(I_CURMAX_N);
		actions.put(retVal, arr[0]);
		for (int i = 1; i < arr.length; i++) {
			actions.put(retVal + i, arr[i]);
			if (arr[i] > RepositoryAdapter.MAX_PRIMITIVE_REFERENCE)
				r2a_add(arr[i], retVal);
		}
		common.put(I_CURMAX_N, retVal+arr.length);

		rearrangeStrings(1);
		int si = common.get(I_CURMAX_S);
		
		a2s_put(retVal, si);
		s2a_smart_add(s, si, retVal, arr[0] == 0x05 || arr[0] == 0x15);
		strings_put(si, s);
		common.put(I_CURMAX_S, si+1);

		return retVal;
	}

	public void deleteSimpleAction(int index) {

		if (actions.get(index) == 0)
			return; // already deleted

		int size = RepositoryAdapter.getOpSize(actions.get(index));
		actions.put(index, 0);
		actions.put(index + 1, size);
		// other actions[index+i] may remain the same
		common.put(I_DELETED_N, common.get(I_DELETED_N)+1);

		int si = this.a2s_get_and_remove(index);
		if (si >= 0) {
			strings_put(si, null);
		}
	}

	public long[] getAllClasses() {
		ArrayList<Long> arr = new ArrayList<Long>();

		int ai;
		for (int i = 0; i < a2s_a.limit(); i++) {
			ai = a2s_a.get(i);
			if (ai <= 0)
				continue;
			if (actions.get(ai) == 0x01) // createClass
				arr.add((long) actions.get(ai + 1));
			else {
				if (actions.get(ai) == 0x25) { // createAdvancedAssociation
					boolean associationClass = actions.get(ai + 2) == 1.0;
					if (associationClass)
						arr.add((long) actions.get(ai + 3));
				}
			}
		}

		long[] retVal = new long[arr.size()];

		int i = 0;

		for (Long l : arr)
			retVal[i++] = l;

		return retVal;
	}

	public void storeActions(DataOutputStream fw1) throws IOException {
		rearrangeActions(0);
		int n = common.get(I_CURMAX_N);
		for (int i = 0; i < n; i++)
			fw1.writeDouble(actions.get(i));
	}

	public void storeStrings(BufferedWriter fw2) throws IOException {
		rearrangeStrings(0);
		boolean first = true;
		int n = common.get(I_CURMAX_S);
		for (int i = 0; i < n; i++) {
			String s = strings_get(i);
			if (first)
				first = false;
			else
				fw2.write('`');
			fw2.write(s);
		}
	}

	@Override
	public void syncAll(RAAPI_Synchronizer synchronizer) {
		if (synchronizer == null)
			return;
		
		lock();
		
		if (isShmServer)
			this.synchronizer = synchronizer;

/*DEBUG:
 		System.out.println("SYNCING AR ALL... ");

		
		DataOutputStream fw1 = null;
		BufferedWriter fw2 = null;
		    try {
		      fw1 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("d:\\ar.actions"))));
		      storeActions(fw1);
		      
		      fw2 = new BufferedWriter
		    		    (new OutputStreamWriter(new FileOutputStream(new File("d:\\ar.strings")), StandardCharsets.UTF_8));
		      storeStrings(fw2);
		      
		      fw1.close(); fw1=null;
			  fw2.close(); fw2=null;
		    } catch (Throwable t) {
		    	t.printStackTrace();
		    }
*/		

		
		rearrangeActions(0);
		rearrangeStrings(0);

		int curmaxN = common.get(I_CURMAX_N);
		int curmaxS = common.get(I_CURMAX_S);

		double[] actions_arr = new double[curmaxN];
		String[] strings_arr = new String[curmaxS];

		for (int i = 0; i < curmaxN; i++) {
			actions_arr[i] = actions.get(i);
		}
		
		
		for (int i = 0; i < curmaxS; i++) {
			strings_arr[i] = strings_get(i);
		}
		
		unlock();
		synchronizer.syncBulk(curmaxN, actions_arr, curmaxS, strings_arr);		
	}

	@Override
	public double actions_get(int i) {
		if (!isAllocated)
			return 0x00;
		return actions.get(i);
	}

	@Override
	public String strings_get(int i) {
		if (!isAllocated)
			return null;
		int a = strings2.get(i * 2);
		int b = strings2.get(i * 2 + 1);
		if (a == -1)
			return null;

		byte[] arr = new byte[b];
		for (int j=a; j<a+b; j++)
			arr[j-a] = chars.get(j);
				
		return new String(arr, UTF8);		
	}
	
	public void strings_put(int i, String _s) {
		if (!isAllocated)
			return;
		
		if (_s==null) {
			strings2.put(i*2, -1);
			strings2.put(i*2+1, 0);
			return;
		}
			
		byte[] arr = _s.getBytes(UTF8);
			
		int n = common.get(I_CURMAX_CHARS);
		int a = i==0?n:n+1;
		int b = a+arr.length;
		
		if (b>common.get(I_CHARS_LEN)) {
			common.put(I_CHARS_LEN, Math.max(b, common.get(I_CHARS_LEN)*2));
			remapChars(true);
		}
		
		if (i>0)
			chars.put(n, (byte)'`');
		
		chars.position(a);
		chars.put(arr);
		
		common.put(I_CURMAX_CHARS, b);
				
		strings2.put(i*2, a);
		strings2.put(i*2+1, arr.length);
	}
	
	private void strings_move(int i, int j) {
		if (!isAllocated)
			return;
		
		if (i==j)
			return;
		
		assert i<j;
		
		strings2.put(i*2, strings2.get(j*2));
		strings2.put(i*2+1, strings2.get(j*2+1));
		
		strings2.put(j*2, -1);
		strings2.put(j*2+1, 0);
	}


	@Override
	public long getMaxReference() {		
		return (long) maxRef.get(0);
	}

	@Override
	public boolean tryLock() {
		
			boolean wait = true;
			
			int flag = common.get(I_LOCK_FLAG);
			if (isShmServer) {
				if (flag == 2)
					wait = false; // ok, already locked
				else
				if (flag == 1) {
					common.put(I_LOCK_FLAG, 2);
					if (common.get(I_LOCK_FLAG)==2) {
						wait = false; // just locked
					}
				}
				
				if (!wait && (common.get(I_SYNC_ACTIONS_LEN)>0)) { // there are some actions to sync...
					
					
					if (synchronizer!=null) {
						
						syncActions.limit(common.get(I_SYNC_ACTIONS_LEN));
						syncActions.rewind();
						
						byte[] arr = new byte[common.get(I_SYNC_STRINGS_LEN)];
						for (int i=0; i<arr.length; i++)
							arr[i] = syncStrings.get(i);
								
						synchronizer.syncBulk(syncActions, new String(arr, UTF8));
						
						common.put(I_SYNC_ACTIONS_LEN, 0);
						common.put(I_SYNC_STRINGS_LEN, 0);
					}
					else
						System.err.println("AR shm server: no synchronizer to sync. Was syncAll() called?");
					
				}
				
			}
			else {
				if (flag == 3)
					wait = false; // ok, already locked
				else
				if (flag == 1) {
					common.put(I_LOCK_FLAG, 3);
					if (common.get(I_LOCK_FLAG)==3) {
						wait = false; // just locked
					}
				}			
			}
			

			return !wait;
	}
	

	@Override
	public void lock() {
		
		while (!tryLock()) {
			Thread.yield();
		}
		
	}


	@Override
	public void unlock() {
		int flag = common.get(I_LOCK_FLAG);
		if (isShmServer) {
			if (flag == 2)
				common.put(I_LOCK_FLAG, 1);
		}
		else {
			if (flag == 3)
				common.put(I_LOCK_FLAG, 1);
		}
		
	}
	
	@Override
	public boolean hasMultiTypedObjects() {
		return common.get(I_HAS_MULTI_TYPED_OBJECTS)!=0;
	}


	
}
