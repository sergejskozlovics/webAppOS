package lv.lumii.tda.adapters.repository.ar;

import java.util.ArrayList;

import lv.lumii.tda.adapters.repository.ar.IARMemory.ActionsIterator;

public class ActionsIteratorOverArrayList implements ActionsIterator {

	private ArrayList<Integer> list;
	private int i=0;
	
	public ActionsIteratorOverArrayList(ArrayList<Integer> _list) {
		list = _list;
	}
	

	@Override
	public int next() {
		if (i<list.size()) {
			return list.get(i++);
		}
		else
			return -1;
	}

	private int get(int index) {
		return (index>=0)&&(index<list.size())?list.get(index):-1;
	}
	
	private int findValue(int l, int r, int value) {
		// searching within [i;j), where get(j) is the end or >=value
		
		while (l<r) {				
			int m = (l+r)/2;
					
			int v = get(m);
			
			if (v == value) {
				i=m+1;
				return v;
			} else {
				if (v < value) {
					l = m+1;
				} else {
					r=m;
				}
			}
		}
		
		int res = get(r);
		if (res==-1) {
			i=list.size();
			return -1;
		}
		
		if (res>=value) {
			i = r+1;
			return res;
		}
		else {
			i=list.size();
			return -1;
		}
	}

	@Override
	public int nextGreaterOrEqual(int value) {

		return findValue(i, list.size(), value);
	}
	
	
}

