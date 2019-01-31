package lv.lumii.tda.adapters.repository.ar;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lv.lumii.tda.raapi.RAAPI_Synchronizer;


public class ARMemoryArrays implements IARMemory {
	
	private static int stringHash(String s) {
		return Math.abs((s+"webAppOS").hashCode());
	}
	private static boolean DEBUG = false;
	

	private static final int[] PRIMES_N = { 
			10007, // >INITIAL_N*1
			20011, // >INITIAL_N*2
			40009, // >INITIAL_N*4
			80021, // >INITIAL_N*8
			160001, // >INITIAL_N*16
			320009, // >INITIAL_N*32
			640007, // ...
			1280023,
			2560021,
			5120029,
			10240033,
			20480011,
			40960001,
			81920021,
			163840007,
			327680009,
			655360001,
			1310720003 // this will be multiplied by sizeof(double)=8; taking into a consideration other maps,
			           // 1310720000 actions will lead to memory consumption up to 32GB (for just this slot)
	};

	private static final int[] PRIMES_N2 = {
			50021, // >INITIAL_N*1/2
			10007, // >INITIAL_N*2/2
			20011, // >INITIAL_N*4/2
			40009, // >INITIAL_N*8/2
			80021, // >INITIAL_N*16/2
			160001, // >INITIAL_N*32/2
			320009, // >INITIAL_N*64/2
			640007, // ...
			1280023,
			2560021,
			5120029,
			10240033,
			20480011,
			40960001,
			81920021,
			163840007,
			327680009,
			655360001
	};
	
	private static final int[] PRIMES_2S = { 
			2003, // >INITIAL_S*2*1
			4001, // >INITIAL_S*2*2
			8009, // >INITIAL_S*2*4
			16001, // >INITIAL_S*2*8
			32003, // >INITIAL_S*2*16
			64007, // >INITIAL_S*2*32
			128021, // ...
			256019,
			512009,
			1024021,
			2048003,
			4096013,
			8192003,
			16384001,
			32768011,
			65536043,
			131072003,
			262144009
	};
	
	// TODO: allow configure (max ops cnt, max mem size..)
	// TODO: check for max size

	
	private static final int INITIAL_N = 10000;
	private static final int INITIAL_S = 1000;
	private int factorNLog = 0; // actual actions size is (2^factorNLog)*INITIAL_N
	private int factorSLog = 0; // actual actions size is (2^factorSLog)*INITIAL_S
	
	private int curmaxN, curmaxS;
	private int deletedN, deletedS; 
	/*package*/ double[] actions;
	/*package*/ String[] strings;
		
	// s2a  string index -> action index (ordinary map for string indices, multi map for string values)
	// r2a  reference -> action index (multi map)
	// a2s  action index-> string index (ordinary map)
	private int[] s2a_s = new int[PRIMES_2S[factorSLog]];
	private int[] s2a_a = new int[PRIMES_2S[factorSLog]]; // s2a_a[i] == 0 <=> empty cell
	
	private double[] r2a_r = new double[PRIMES_N[factorNLog]];
	private int[] r2a_a = new int[PRIMES_N[factorNLog]]; // r2a_a[i] == 0 or -1 <=> empty cell (-1, if removed)

	private int[] a2s_a = new int[PRIMES_2S[factorSLog]]; // a2s_a[i] == 0 or -1 <=> empty cell (-1, if removed)
	private int[] a2s_s = new int[PRIMES_2S[factorSLog]];
	
	public ARMemoryArrays() {
		reallocate(0,0);
	}

	@Override
	public boolean reallocateFromCache() {
		return false;
	}
	
	@Override
	public boolean reallocate(int nActions, int nStrings) {
		
		factorNLog = 0;
		int nSize = INITIAL_N;
		while (nActions > nSize) {
			nSize*=2;
			factorNLog++;
		}
		
		factorSLog = 0;
		int sSize = INITIAL_S;
		while (nStrings > sSize) {
			sSize*=2;
			factorSLog++;
		}
		
		
		actions = new double[nSize];
		actions[0] = 0xEE; // bulk actions
		strings = new String[sSize];
		curmaxN = 1; 
		curmaxS = 0;
		deletedN = 0;
		deletedS = 0;
		
		wasMultiTypedObject = false;
		
		s2a_s = new int[PRIMES_2S[factorSLog]]; 
		s2a_a = new int[PRIMES_2S[factorSLog]]; 
		
		r2a_r = new double[PRIMES_N[factorNLog]]; 
		r2a_a = new int[PRIMES_N[factorNLog]]; 

		a2s_a = new int[PRIMES_2S[factorSLog]]; // PRIMES_S? TODO 
		a2s_s = new int[PRIMES_2S[factorSLog]];
		
		return true;
	}

	@Override
	public void free(boolean clearCache) {
		reallocate(0, 0);
	}
	
	
	/*
	 We can use just ActionsIteratorImpl over ArrayList, since nextGreaterOrEqual won't be called for actions iterators related to strings.
	private class S2ASimpleImpl implements IARMemory.ActionsIterator {
		private String s;
		private int hash1, hash2;
		public S2ASimpleImpl(String _s, int _hash1, int _hash2) {
			s = _s;
			hash1 = _hash1;
			hash2 = _hash2;
			//len = _len;
		}

		@Override
		public int next() {
			int si;
			while (s2a_a[hash1] != 0) {
				//len--;
				si = s2a_s[hash1];
				if (s.equals(strings[si])) {
					if (s2a_a[hash1]>=0) { // negative numbers specify the skip length or deleted elements						
						int tmp = s2a_a[hash1];
						hash1 = hash1+hash2;
						if (hash1>=s2a_a.length)
							hash1-=s2a_a.length;
						return tmp;
					}
				}
				hash1 = hash1+hash2;
				if (hash1>=s2a_a.length)
					hash1-=s2a_a.length;
			}			
			
			return -1;
		}
		
		@Override
		public int nextGreaterOrEqual(int value) {
			// RepositoryAdapter won't call this function; thus, we do not need efficient implementation
			int i = next();
			while ((i!=-1) && (i<value))
				i = next();
			return i;
		}
	}
	
	private class S2ARolesImpl implements IARMemory.ActionsIterator {
		private String role1, role2;
		private int hash1, hash2;
		public S2ARolesImpl(String _role1, String _role2, int _hash1, int _hash2) {
			role1 = _role1;
			role2 = _role2;
			hash1 = _hash1;
			hash2 = _hash2;
			//len = _len;
		}

		@Override
		public int next() {
			int si;
			String ssi;
			while (s2a_a[hash1] != 0) {
				//len--;
				si = s2a_s[hash1];
				ssi = strings[si];
				if ((ssi!=null) && (ssi.endsWith(role1)||ssi.startsWith(role2))) {
					if (s2a_a[hash1]>=0) { // negative numbers specify the skip length or deleted elements						
						int tmp = s2a_a[hash1];
						hash1 = hash1+hash2;
						if (hash1>=s2a_a.length)
							hash1-=s2a_a.length;
						return tmp;
					}
				}
				hash1 = hash1+hash2;
				if (hash1>=s2a_a.length)
					hash1-=s2a_a.length;
			}			
			
			return -1;
		}
		
		@Override
		public int nextGreaterOrEqual(int value) {
			// RepositoryAdapter won't call this function; thus, we do not need efficient implementation
			int i = next();
			while ((i!=-1) && (i<value))
				i = next();
			return i;
		}
		
	}
	
	*/
	
	public IARMemory.ActionsIterator s2a_get(String s, boolean isRoleName) {
		if (s == null)
			return null;
		
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int h = stringHash(s);
		int hash1 = (h % s2a_a.length);
		int hash2 = (h % (s2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)

		if (isRoleName) {
			String s1 = "/"+s;
			String s2 = s+"/";
			int si;
			String ssi;
			while (s2a_a[hash1] != 0) {
				si = s2a_s[hash1]; // string index
				ssi = strings[si];
				if ((ssi!=null) && (ssi.endsWith(s1)||ssi.startsWith(s2)))
					if (s2a_a[hash1]>0)
						retVal.add(s2a_a[hash1]);
				hash1 = hash1+hash2;
				if (hash1>=s2a_a.length)
					hash1-=s2a_a.length;
			}			
		}
		else {
			int si;
			while (s2a_a[hash1] != 0) {				
				si = s2a_s[hash1]; // string index
				if (s.equals(strings[si]))
					if (s2a_a[hash1]>0)
						retVal.add(s2a_a[hash1]);
				hash1 = hash1+hash2;
				if (hash1>=s2a_a.length)
					hash1-=s2a_a.length;
			}			
		}
		
		return new ActionsIteratorOverArrayList(retVal);
	}
	
	/*
	private void s2a_simple_add(String s, int si, int ai, boolean isRoleName) {
		if (s == null)
			return;
		
		int h = stringHash(s);
		if (h<0)
			h=-h;
		int hash1 = h;
		if (hash1>=s2a_a.length)
			hash1 = (h % s2a_a.length);
		int hash2 = h+1;
		if (hash2>=s2a_a.length-2)
			hash2 = (h % (s2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)

		while (s2a_a[hash1] > 0) {
			sskips++;
			hash1 = hash1+hash2;
			if (hash1>=s2a_a.length)
				hash1-=s2a_a.length;
		}
		
		s2a_s[hash1] = si;
		s2a_a[hash1] = ai;
		
		scalls++;

		if ((scalls&255)==0) {
			System.err.println("S2a: "+scalls+" "+sskips+" "+(sskips*1.0/scalls)+" s="+s+" h="+h+" hash1/2="+hash1+"/"+hash2+" len="+s2a_a.length);
		}
		
	}*/

/*
	public IARMemory.ActionsIterator s2a_get(String s, boolean isRoleName) {
		if (s == null)
			return null;
		
		int h = stringHash(s);
		int hash1 = (h % s2a_a.length);
		int hash2 = (h % (s2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)
		
		if (isRoleName) {
			
			String s1 = "/"+s;
			String s2 = s+"/";
			
			// searching for the first our cell...
			int si;
			String ssi;
			while ((s2a_a[hash1]!=0)&&(s2a_a[hash1]!=-1)) {  // !=0 and !=-1
				si = s2a_s[hash1]; // string index
				ssi = strings[si];
				if ((ssi!=null) && (ssi.endsWith(s1)||ssi.startsWith(s2))) {
					
					// this is the first "our" cell...
					if (s2a_a[hash1]<-1) {
						// this cell contains the length of the list...
						int ourLen = -s2a_a[hash1];
						
						return new S2ARolesImpl(s1, s2, hash1, hash2);
					}
					else {
						return new SingleActionIterator(s2a_a[hash1]);
					}
				}
				else {				
					hash1 = hash1+hash2;
					if (hash1>=s2a_a.length)
						hash1-=s2a_a.length;
				}
			}
		}
		else {
			
			// searching for the first our cell...
			int si;
			while ((s2a_a[hash1]!=0)&&(s2a_a[hash1]!=-1)) {  // !=0 and !=-1
				si = s2a_s[hash1]; // string index
				if (s.equals(strings[si])) {
					
					// this is the first "our" cell...
					if (s2a_a[hash1]<-1) {
						// this cell contains the length of the list...
						int ourLen = -s2a_a[hash1];
						
						return new S2ASimpleImpl(s, hash1, hash2);
					}
					else {
						return new SingleActionIterator(s2a_a[hash1]);
					}
				}
				else {				
					hash1 = hash1+hash2;
					if (hash1>=s2a_a.length)
						hash1-=s2a_a.length;
				}
			}			
		}
		
		
		return EmptyActionsIterator.INSTANCE;
	}*/
	
	long scalls=0;
	long sskips=0;
	
	int lastEmpty=0;
	

	private void s2a_simple_add(String s, int si, int ai, boolean isRoleName) {
		if (s == null)
			return;
		
		int h = stringHash(s);
		if (h<0)
			h=-h;
		int hash1 = h;
		if (hash1>=s2a_a.length)
			hash1 = (h % s2a_a.length);
		int hash2 = h+1;
		if (hash2>=s2a_a.length-2)
			hash2 = (h % (s2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)

		
		int firstOur = -1;
		int ourLen = 0;
		// searching for the first empty cell...
		
		String s1 = "/"+s;
		String s2 = s+"/";
		int _si;
		String ssi;
		int qskips=0;
		int askips=0;
		int bskips=0;
		ArrayList<String> arr = new ArrayList<String>();
		ArrayList<Integer> arr2 = new ArrayList<Integer>();
		while ((s2a_a[hash1]!=0)&&(s2a_a[hash1]!=-1)) {  // !=0 and !=-1
			if (firstOur==-1) {
				_si = s2a_s[hash1];
				ssi = strings[_si];
				
/*				boolean check = (ssi!=null) &&
						( ssi.equals(s) || ssi.startsWith(s2)||(ssi.endsWith(s1)));*/ 
				boolean check = (ssi!=null) &&
						( (!isRoleName&&ssi.equals(s)) || 
							(isRoleName && (ssi.startsWith(s2)||(ssi.endsWith(s1)))) ); 
				
				if (check) {
					// this is the first "our" cell...
					firstOur = hash1;
					if (s2a_a[hash1]<-1) {
						// this cell contains the length of the list...
						ourLen = -s2a_a[hash1]; // ourLen includes its own cell containing the length
						hash1 = (int) (((long)hash1+(long)hash2*(long)ourLen) % (long)s2a_a.length); // !handling overflow via long
						continue;
					}
					else
						ourLen=1;
				}
				else {
					arr2.add(s2a_a[hash1]);
					arr.add(ssi);
				}
			}
			else
				ourLen++;
			if (firstOur==-1)
				askips++;
			else
				bskips++;
			sskips++;
			qskips++;
			hash1 = hash1+hash2;
			if (hash1>=s2a_a.length)
				hash1-=s2a_a.length;
		}
		
		/*if (qskips>10) {
			System.err.println("qskips "+qskips+"/"+askips+"/"+bskips+" `"+s+"' hash="+h);
			System.err.println("arr was "+arr);
			System.err.println("arr2 was "+arr2);
		}*/
		// now hash1 is the index of the free cell...
		// ourLen - how many times we have to skip the list to get the pointer to the last element (we will add this last element now),
		// thus, s2a_a[firstOur] shall contain -ourLen
		
		if (firstOur>=0) {
			if (s2a_a[firstOur]<-1) {
				
				// the list already contains 2 or more elements...
				s2a_s[hash1] = si;
				s2a_a[hash1] = ai;
				s2a_a[firstOur] = -ourLen;
				
			}
			else {				
				// the list contains only one element...
				int el = s2a_a[firstOur];
				s2a_s[hash1] = s2a_s[firstOur];
				s2a_a[hash1] = el; // moving the first element
				// ourLen now is at least 2 (or more, if we skipped some elements during the previous while)
				
				// adding the second element...
				hash1 = hash1+hash2;
				if (hash1>=s2a_a.length)
					hash1-=s2a_a.length;
				ourLen++;

				while ((s2a_a[hash1] != 0) && (s2a_a[hash2]!=-1)) {  // !=0 and !=-1
					ourLen++;
					hash1 = hash1+hash2;
					if (hash1>=s2a_a.length)
						hash1-=s2a_a.length;
					sskips++;
				}			
				
				s2a_s[hash1] = si;
				s2a_a[hash1] = ai; 
				s2a_a[firstOur] = -ourLen; // storing the len
			}
		}
		else {
			// adding the first element...
			s2a_s[hash1] = si;
			s2a_a[hash1] = ai;			
		}
		
		scalls++;
		sskips++;
		
/*		if ((scalls&255)==0) {
			System.err.println("S2a: "+scalls+" "+sskips+" "+(sskips*1.0/scalls)+" s="+s+" h="+h+" hash1/2="+hash1+"/"+hash2+" len="+s2a_a.length);
		}*/
		
	}
	
	public int a2s_get(int index) {
		int hash1 = (index % a2s_a.length);
		int hash2 = (index % (a2s_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)
		
		while (a2s_a[hash1] != 0) {
			if (a2s_a[hash1]==index)
				return a2s_s[hash1]; // found 
			hash1 = hash1+hash2;
			if (hash1>=a2s_a.length)
				hash1-=a2s_a.length;
		}			
		
		return -1; // not found
	}

	
	private int a2s_get_and_remove(int ai) {
		int hash1 = (ai % a2s_a.length);
		int hash2 = (ai % (a2s_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)
		
		while (a2s_a[hash1] != 0) {
			if (a2s_a[hash1]==ai) {
				a2s_a[hash1] = -1; // removed
				return a2s_s[hash1]; // found
			}
			hash1 = hash1+hash2;
			if (hash1>=a2s_a.length)
				hash1-=a2s_a.length;
		}			
		
		return -1; // not found
	}

	private void a2s_put(int ai, int si) { // put or replace
		int hash1 = (ai % a2s_a.length);
		int hash2 = (ai % (a2s_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and s2a_a.lenghth-1)
		
		while ((a2s_a[hash1] > 0) && (a2s_a[hash1]!=ai)) { // while !=0 and !=-1
			hash1 = hash1+hash2;
			if (hash1>=a2s_a.length)
				hash1-=a2s_a.length;
		}
		
		a2s_a[hash1] = ai;
		a2s_s[hash1] = si;
	}
	
	private long totalskips = 0;
	private long totalcalls = 0;
	private long totalelms = 0;
	
/* excluded:
 	public List<Integer> r2a_get(double r) {
		List<Integer> retVal = new ArrayList<Integer>();
		int hash1 = ((int)r % (int)r2a_a.length);
		int hash2 = ((int)r % (int)(r2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and r2a_a.length-2)

		int elms=0, skips=0;
		while (r2a_a[hash1] != 0) {
			if (r2a_r[hash1] == r) {
				if (r2a_a[hash1]>=0) { // negative numbers specify the skip length or deleted elements
					retVal.add(r2a_a[hash1]);
					elms++;
				}
			}
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.length)
				hash1-=r2a_a.length;
			skips++;
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
			
			while (r2a_a[hash1] != 0) {
				len--;
				if (r2a_r[hash1] == r) {
					if (r2a_a[hash1]>=0) { // negative numbers specify the skip length or deleted elements						
						int tmp = r2a_a[hash1];
						hash1 = hash1+hash2;
						if (hash1>=r2a_a.length)
							hash1-=r2a_a.length;
						return tmp;
					}
				}
				hash1 = hash1+hash2;
				if (hash1>=r2a_a.length)
					hash1-=r2a_a.length;
			}			
			
			return -1;
		}
		
		private int get(int index) {
			int h = (int) (((long)hash1+(long)hash2*(long)index)%(long)r2a_a.length); // !handling overflow via long
			
			if ((r2a_r[h] == r) && (r2a_a[h]>=0)) 			
				return r2a_a[h];
			
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
			
			
			hash1 = (int) (((long)hash1+(long)hash2*(long)i)%(long)r2a_a.length); // move forward; !handling overflow via long
			len = len-i;
			
			int tmp = r2a_a[hash1];
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.length)
				hash1-=r2a_a.length;
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
	
	public ActionsIterator r2a_get_iterator(double r) {

		int di = (int)r;
		int hash1 = di;
		if (hash1>=r2a_a.length)
			hash1 = di%r2a_a.length;
		int hash2 = di;
		if (hash2>=r2a_a.length-2)
			hash2 = di%(r2a_a.length-2);
		hash2++;
				
		// searching for the first our cell...
		
		while ((r2a_a[hash1]!=0)&&(r2a_a[hash1]!=-1)) {  // !=0 and !=-1
			if (r2a_r[hash1]==r) {
				// this is the first "our" cell...
				if (r2a_a[hash1]<-1) {
					// this cell contains the length of the list...
					int ourLen = -r2a_a[hash1];
					
					return new R2AImpl(r, hash1, hash2, ourLen+1);
				}
				else {
					return new SingleActionIterator(r2a_a[hash1]);
				}
			}
			else {				
				hash1 = hash1+hash2;
				if (hash1>=r2a_a.length)
					hash1-=r2a_a.length;
			}
		}
		
		return EmptyActionsIterator.INSTANCE;
	}
	
	public int r2a_get_first(double r) {
		int hash1 = ((int)r % (int)r2a_a.length);
		int hash2 = ((int)r % (int)(r2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and r2a_a.lenghth-1)

		while (r2a_a[hash1] != 0) {
			if (r2a_r[hash1] == r) {
				if (r2a_a[hash1]>=0) { // negative numbers specify the skip length or deleted elements
					return r2a_a[hash1];
				}
			}
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.length)
				hash1-=r2a_a.length;
		}			

		return -1; // not found
	}
	
	public List<Integer> r2a_get_and_remove(double r) {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int hash1 = ((int)r % r2a_a.length);
		int hash2 = ((int)r % (r2a_a.length-2))+1; // co-prime with hash1 modulus (hash 2 is between 1 and r2a_a.lenghth-1)

		while (r2a_a[hash1] != 0) {
			if (r2a_r[hash1] == r) {
				if (r2a_a[hash1]>=0) // negative numbers specify the skip length or deleted elements
					retVal.add(r2a_a[hash1]);
				r2a_a[hash1] = -1; // removed
			}
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.length)
				hash1-=r2a_a.length;
		}			
		
		return retVal;
//		return whereReferenceMap.remove((double)r);
	}
	
	public void r2a_add(double d, int i) {
		int di = (int)d;
		int hash1 = di;
		if (hash1>=r2a_a.length)
			hash1 = di%r2a_a.length;
		int hash2 = di;
		if (hash2>=r2a_a.length-2)
			hash2 = di%(r2a_a.length-2);
		hash2++;
		
		
		int firstOur = -1;
		int ourLen = 0;
		// searching for the first empty cell...
		
		while ((r2a_a[hash1]!=0)&&(r2a_a[hash1]!=-1)) {  // !=0 and !=-1
			if (firstOur==-1) {
				if (r2a_r[hash1]==d) {
					// this is the first "our" cell...
					firstOur = hash1;
					if (r2a_a[hash1]<-1) {
						// this cell contains the length of the list...
						ourLen = -r2a_a[hash1]; // ourLen includes its own cell containing the length
						hash1 = (int) (((long)hash1+(long)hash2*(long)ourLen) % (long)r2a_a.length); // !handling overflow via long
						continue;
					}
					else
						ourLen=1;
				}
			}
			else
				ourLen++;
			hash1 = hash1+hash2;
			if (hash1>=r2a_a.length)
				hash1-=r2a_a.length;
		}
		
		// now hash1 is the index of the free cell...
		// ourLen - how many times we have to skip the list to get the pointer to the last element (we will add this last element now),
		// thus, r2a_a[firstOur] shall contain -ourLen
		
		if (firstOur>=0) {
			if (r2a_a[firstOur]<-1) {
				
				// the list already contains 2 or more elements...
				r2a_r[hash1] = d;
				r2a_a[hash1] = i;
				r2a_a[firstOur] = -ourLen;
				
			}
			else {				
				// the list contains only one element...
				int el = r2a_a[firstOur];
				r2a_r[hash1] = d;
				r2a_a[hash1] = el; // moving the first element
				// ourLen now is at least 2 (or more, if we skipped some elements during the previous while)
				
				// adding the second element...
				hash1 = hash1+hash2;
				if (hash1>=r2a_a.length)
					hash1-=r2a_a.length;
				ourLen++;
				
				while ((r2a_a[hash1] != 0) && (r2a_a[hash2]!=-1)) {  // !=0 and !=-1
					ourLen++;
					hash1 = hash1+hash2;
					if (hash1>=r2a_a.length)
						hash1-=r2a_a.length;
				}			
				
				r2a_r[hash1] = d;
				r2a_a[hash1] = i; 
				r2a_a[firstOur] = -ourLen; // storing the len
			}
		}
		else {
			// adding the first element...
			r2a_r[hash1] = d;
			r2a_a[hash1] = i;			
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
	 * Re-arranges the actions array (increases/decreases array size, if needed).
	 * @param additionalN How many elements in the actions array are to be added. The number of elements depends on the length+1 of actions.
	 * If <=0, then the array is re-arranged for save purposes (e.g., elements of deleted actions are excluded).
	 */
	private void rearrangeActions(int additionalN) {
		double[] old = actions;
		
		if (additionalN > 0) {
			// check memory
			
			if (curmaxN+additionalN < actions.length)
				return; // room ok			
			
			if ((deletedN>actions.length/4) && (curmaxN-deletedN+additionalN < actions.length)) {			
				if (curmaxN-deletedN+additionalN <= actions.length/4) {
					// decrease memory before rearrange...
					actions = new double[actions.length/2];
					factorNLog--;
				}
			}
			else {
				// increase memory before rearrange
				actions = new double[actions.length*2];
				factorNLog++;				
			}
		}
		
		// rearrange
		
		System.err.println("REARRANGE");
		this.totalcalls=0;
		this.totalskips=0;
		this.totalelms=0;
		
		int i=0; int j=0;
		s2a_s = new int[PRIMES_2S[factorSLog]]; 
		s2a_a = new int[PRIMES_2S[factorSLog]];
		//= whereStringMap.clear();
				
		r2a_r = new double[PRIMES_N[factorNLog]]; 
		r2a_a = new int[PRIMES_N[factorNLog]];
		// = whereReferenceMap.clear();
		
		// do not need to re-arrange a2s, since factorSLog has not changed 
		
		while (j<curmaxN) {
			if (old[j] == 0x00) { // deleted action
				j++; // skip this action id
				if (j<curmaxN) {
					j += old[j]; // increasing j by action size stored there...
				}
				continue;
			}
			
			// non-deleted action
			int actionsDelta = RepositoryAdapter.getOpSize(old[j]);
			
			if (RepositoryAdapter.isStringOp(old[j])) {
				if (DEBUG) {
					if (this.a2s_get(j)<0) {
						System.err.println("!!!! wrong map value for "+j+"->"+i+"; action = "+old[j]+" "+curmaxN+" arr:"+old.length+"->"+actions.length);
						int opsize = RepositoryAdapter.getOpSize(old[j]);
						for (int k=1; k<=opsize; k++)
							System.err.print(old[j+k]+" ");
						System.err.println();
						
						//System.err.println("className "+this.getClassName((long)old[j+1]));
					}
				}
				int si = a2s_get_and_remove(j);
				s2a_smart_add(strings[si], si, i, old[j]==0x05 || old[j]==0x15);
				a2s_put(i, si);
				
			}
			
			actions[i] = old[j]; // move action id...
			
			int ii = i;
			
			while (actionsDelta>0) { // move action
				i++; j++;
				actions[i] = old[j];
				actionsDelta--;
				if (actions[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) { // this is not some primitive type reference; neither boolean 0 or 1 value
					r2a_add(actions[i], ii);
				}
			}
			
			i++;
			j++;
		}
		curmaxN = i;
		deletedN = 0;
	}
	
	
	/**
	 * Re-arranges the strings array (increases/decreases array size, if needed).
	 * @param additionalS How many strings are to be added to the strings array. If <=0, then the array is re-arranged for save purposes (e.g., deleted strings are excluded).
	 */
	private void rearrangeStrings(int additionalS) {
		
		
		String[] old = strings;
		
		if (additionalS > 0) {		
			// check memory...
			if (curmaxS+additionalS < strings.length)
				return; // room ok
			
			if ((deletedS>strings.length/4) && (curmaxS-deletedS+additionalS < strings.length)) {			
				if (curmaxS-deletedS+additionalS <= strings.length/4) {
					// decrease memory before rearrange...
					strings = new String[strings.length/2];
					factorSLog--;
				}
			}
			else {
				// increase memory before rearrange
				strings = new String[strings.length*2];
				factorSLog++;
			}
		}
		
		// re-arranging a2s and getting inverse...
		Map<Integer, Integer> inv = new HashMap<Integer, Integer>();
/*		for (Integer k : a2sMap.keySet())
			inv.put(a2sMap.get(k), k);*/		
		for (int k=0; k<a2s_a.length; k++) {
			if (a2s_a[k]>0)
				inv.put(a2s_s[k], a2s_a[k]);
		}
		a2s_a = new int[PRIMES_2S[factorSLog]]; // PRIMES_S? TODO 
		a2s_s = new int[PRIMES_2S[factorSLog]];		
		
		// re-arranging s2a...
		s2a_s = new int[PRIMES_2S[factorSLog]]; 
		s2a_a = new int[PRIMES_2S[factorSLog]]; 
		
		// rearrange
		int i=0; int j=0;
		while (j<curmaxS) {
			if (old[j] == null) { // deleted string
				j++; // skip this string
				continue;
			}
			
			// non-deleted string
			strings[i] = old[j]; // move string...

			int ai = inv.get(j);
			
			// remove ai->j, put ai->i			
			a2s_put(ai, i);

			s2a_smart_add(strings[i], i, ai, actions[ai]==0x05 || actions[ai]==0x15);
			
			i++;
			j++;
		}
		curmaxS = i;
		deletedS = 0;
		
		scalls=0;
		sskips=0;

	}
	
	/*	private int firstIndexWhere(double r) {
	ArrayList<Integer> arr = whereReferenceMap.get(r);
	if ((arr == null) || (arr.isEmpty()))
		return -1;
	return arr.get(0); // the 0th must be createClass		
}

private double firstActionWhere(double r) {
	ArrayList<Integer> arr = whereReferenceMap.get(r);
	if ((arr == null) || (arr.isEmpty()))
		return 0;
	int i = arr.get(0);
	return actions[i];
}*/
	

	
	public int addAction(double... arr) {
		rearrangeActions(arr.length);
		int retVal = curmaxN;		
		actions[retVal] = arr[0];		
		if ((arr[0]==0x12) || (arr[0]==0xE2))
			wasMultiTypedObject = true;
		for (int i=1; i<arr.length; i++) {
			actions[retVal+i] = arr[i];
			if (arr[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) {
				r2a_add(arr[i], retVal);
			}
		}
		curmaxN+=arr.length;
		return retVal;
	}
	
	public int addStringAction(String s, double... arr) {
		if (s==null)
			s="";
		rearrangeActions(arr.length);
		int retVal = curmaxN;
		actions[retVal] = arr[0];
		for (int i=1; i<arr.length; i++) {
			actions[retVal+i] = arr[i];
			if (arr[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE)
				r2a_add(arr[i], retVal);
		}
		curmaxN+=arr.length;
		
		rearrangeStrings(1);
		a2s_put(retVal, curmaxS);
		strings[curmaxS] = s;
		s2a_smart_add(s, curmaxS, retVal, arr[0]==0x05 || arr[0]==0x15);
		curmaxS++;
		
		return retVal;
	}

	public int addRoleStringAction(String s, double... arr) {
		if (s==null)
			s="";
		rearrangeActions(arr.length);
		int retVal = curmaxN;
		actions[retVal] = arr[0];
		for (int i=1; i<arr.length; i++) {
			actions[retVal+i] = arr[i];
			if (arr[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE)
				r2a_add(arr[i], retVal);
		}
		curmaxN+=arr.length;
		
		rearrangeStrings(1);
		a2s_put(retVal, curmaxS);
		s2a_smart_add(s, curmaxS, retVal, arr[0]==0x05 || arr[0]==0x15);
		strings[curmaxS++] = s;
		
		return retVal;
	}

	public void deleteSimpleAction(int index) {
	
		if (actions[index]==0)
			return; // already deleted
		
		int size = RepositoryAdapter.getOpSize(actions[index]);
		actions[index] = 0;
		actions[index+1] = size;		
		// other actions[index+i] may remain the same
		deletedN++;
		
		int si = this.a2s_get_and_remove(index);
		if (si>=0) {
			// whereReferenceMap, whereStringMap - do not modify, it will be modified during re-arranging actions
			strings[si] = null;
		}
	}
	
	public long[] getAllClasses() {
		ArrayList<Long> arr = new ArrayList<Long>();
		
		int ai;
		for (int i=0; i<a2s_a.length; i++) {
			ai = a2s_a[i];
			if (ai<=0)
				continue;
			if (actions[ai]==0x01) // createClass
				arr.add((long)actions[ai+1]);
			else {
				if (actions[ai]==0x25) { // createAdvancedAssociation
					boolean associationClass = actions[ai+2]==1.0;
					if (associationClass)
						arr.add((long)actions[ai+3]);			
				}
			}
		}
		
		long [] retVal = new long[arr.size()];
		
		int i=0;
		
		for (Long l : arr)
			retVal[i++] = l;

		return retVal; 		
	}

	public void storeActions(DataOutputStream fw1) throws IOException {
	      rearrangeActions(0);
	      for (int i=0; i<curmaxN; i++)
	    	  fw1.writeDouble(actions[i]);	      
	}
	
	public void storeStrings(BufferedWriter fw2) throws IOException {
		rearrangeStrings(0);
	      boolean first = true;
	      for (int i=0; i<curmaxS; i++) {
	    	  String s = strings[i];
	    	  if (first)
	    		  first = false;
	    	  else
	    		  fw2.write('`');
	    	  fw2.write(RAAPI_Synchronizer.sharpenString(s));	    	  
	      }		
	}

	@Override
	public void syncAllEx(RAAPI_Synchronizer synchronizer, int bitsCount, long bitsValues) {
		if (synchronizer == null)
			return;
	    rearrangeActions(0);
	    rearrangeStrings(0);
	    
	    System.err.println("syncall actions="+curmaxN+" strings arr="+curmaxS+" bitsCount="+bitsCount+" bitsValue="+bitsValues);
	    
	    synchronizer.syncBulk(curmaxN, actions, curmaxS, strings);
	    synchronizer.syncMaxReference(this.getMaxReference(), bitsCount, bitsValues);
	}

	@Override
	public double actions_get(int i) {
		return actions[i];
	}

	@Override
	public String strings_get(int i) {
		return strings[i];
	}

	
	@Override
	public boolean tryLock() {
		return true;
	}

	@Override
	public void lock() {
		// do nothing
	}

	@Override
	public void unlock() {
		// do nothing
	}

	private boolean wasMultiTypedObject = false;
	@Override
	public boolean hasMultiTypedObjects() {
		return wasMultiTypedObject;
	}
	
	private long maxReference = -1;

	@Override
	public long getMaxReference() {
		if (maxReference < 0) {
			long rMax = RepositoryAdapter.MAX_PRIMITIVE_REFERENCE;
	
			for (int i = 0; i < curmaxN; i++) {
				double d = actions[i];
				if (d > rMax)
					rMax = (long)d;
			}
			maxReference = rMax;
		}
		return maxReference;
	}
	
	@Override
	public boolean setMaxReference(long ref) {
		maxReference = ref;
		return true;
	}
	
	private int predefinedBitsCount = 0;
	private long predefinedBitsValues = 0;

	@Override
	public boolean setPredefinedBits(int bitsCount, long bitsValues) {
		predefinedBitsCount = bitsCount;
		predefinedBitsValues = (bitsValues & ((1<<bitsCount)-1)); // keeping only lowest bitsCount bits
		return true;
	}

	@Override
	public int getPredefinedBitsCount() {
		return predefinedBitsCount;
	}

	@Override
	public long getPredefinedBitsValues() {
		return predefinedBitsValues;
	}

	@Override
	public long newReference() {
		maxReference = (((maxReference>>predefinedBitsCount)+1) << predefinedBitsCount) | predefinedBitsValues;
		return maxReference;
	}

	@Override
	public double getMemoryUsageFactor() {
		return 0.0;
	}

	@Override
	public boolean memoryFault() {
		return false;
	}
	
}
