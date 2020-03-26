package lv.lumii.dialoglayout.components.utils;

import java.util.ArrayList;

import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

public class RelativeGroup {
	
	/*static RelativeGroup[] join(RelativeGroup[] a, RelativeGroup[] b) {
		if (a==null)
			return b;
		if (b==null)
			return a;
		RelativeGroup[] result=new RelativeGroup[a.length+b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
	
	public static RelativeGroup[] getGroups(DialogEngine dlgEng, Component container) {
		
		if (container.getChildren()==null)
			return null;
		RelativeGroup[] result=new RelativeGroup[4];
		result[0]=new RelativeGroup(dlgEng,container,false,true);
		result[1]=new RelativeGroup(dlgEng,container,true,true);
		result[2]=new RelativeGroup(dlgEng,container,false,false);
		result[3]=new RelativeGroup(dlgEng,container,true,false);
		for (Component child:container.getChildren()) {
			result=join(getGroups(dlgEng,child),result);
		}
		return result;
		//return null;
	}*/
	
	private ArrayList<RelativeInfo> infos = new ArrayList<RelativeInfo>();
	
	public void addRelativeInfo(RelativeInfo info) {
		infos.add(info);
	}
	
	private void updateMinMax() {
		
		double kMin=-1;
		double kMax=-1;
		
		//find k for minimum value and k for maximum value
		for (RelativeInfo info:infos) {
			if (info.getMinK()!=null)
				if (kMin==-1 || info.getMinK()>kMin)
					kMin=info.getMinK();
			if (info.getMaxK()!=null)
				if (kMax==-1 || info.getMaxK()<kMax)
					kMax=info.getMaxK();
		}

		//update minimum and maximum values according to k values
		for (RelativeInfo info:infos) {
			if (kMin!=-1)
				info.updateMin(kMin);
			if (kMax!=-1)
				info.updateMax(kMax);
		}
	}
	
	/*
	//initialize group by adding ungrouped components
	public RelativeGroup(DialogEngine dlgEng, Component container, boolean vertical, boolean toMin) {
		
		//get components count
		int count=0;
		
		for (Component child:container.getChildren()) {
			boolean needsToBeMinimized;
			if (vertical)
				needsToBeMinimized=(child.getComponentBounds().maxH!=null && child.getComponentBounds().maxH==0);
			else
				needsToBeMinimized=(child.getComponentBounds().maxW!=null && child.getComponentBounds().maxW==0);
			
			if (needsToBeMinimized!=toMin)
				continue;
			
			long component=child.getReference();
			
			//get relative info that corresponds to the child
			long itt;
			if (vertical)
				itt=dlgEng.kernel.getIteratorForLinkedObjects(component, dlgEng.mmr.COMPONENT_VERTICALRELATIVEINFO);
			else
				itt=dlgEng.kernel.getIteratorForLinkedObjects(component, dlgEng.mmr.COMPONENT_HORIZONTALRELATIVEINFO);
			long obj=dlgEng.kernel.resolveIteratorFirst(itt);
			dlgEng.kernel.freeIterator(itt);
			
			if (obj!=0) {
				//get group that contains this info
				long itt2=dlgEng.kernel.getIteratorForLinkedObjects(obj, dlgEng.mmr.RELATIVEINFO_RELATIVEINFOGROUP);
				long group=dlgEng.kernel.resolveIteratorFirst(itt2);
				dlgEng.kernel.freeIterator(itt2);
				dlgEng.kernel.freeReference(obj);
				dlgEng.kernel.freeReference(group);
				if (group!=0)
					continue;
			}
			++count;
		}
		
		infos=new RelativeInfo[count];
		count=0;
		
		for (Component child:container.getChildren()) {
			
			boolean needsToBeMinimized;
			if (vertical)
				needsToBeMinimized=(child.getComponentBounds().maxH!=null && child.getComponentBounds().maxH==0);
			else
				needsToBeMinimized=(child.getComponentBounds().maxW!=null && child.getComponentBounds().maxW==0);
			
			if (needsToBeMinimized!=toMin)
				continue;
			
			long component=child.getReference();
			
			//get relative info that corresponds to the child
			long itt;
			if (vertical)
				itt=dlgEng.kernel.getIteratorForLinkedObjects(component, dlgEng.mmr.COMPONENT_VERTICALRELATIVEINFO);
			else
				itt=dlgEng.kernel.getIteratorForLinkedObjects(component, dlgEng.mmr.COMPONENT_HORIZONTALRELATIVEINFO);
			long obj=dlgEng.kernel.resolveIteratorFirst(itt);
			dlgEng.kernel.freeIterator(itt);
			
			
			if (obj!=0) {
				//get group that contains this info
				long itt2=dlgEng.kernel.getIteratorForLinkedObjects(obj, dlgEng.mmr.RELATIVEINFO_RELATIVEINFOGROUP);
				long group=dlgEng.kernel.resolveIteratorFirst(itt2);
				dlgEng.kernel.freeIterator(itt2);
				dlgEng.kernel.freeReference(obj);
				dlgEng.kernel.freeReference(group);
				if (group!=0)
					continue;
			}
			
			//initialize relative info 
			infos[count++]=new RelativeInfo(dlgEng,child.getReference(),vertical);
		}

		updateMinMax();
	}
	
	public RelativeGroup(DialogEngine dlgEng, long group) {
		//get info count
		int count=0;
		
		long itt=dlgEng.kernel.getIteratorForLinkedObjects(group, dlgEng.mmr.RELATIVEINFOGROUP_RELATIVEINFO);
		for (long obj=dlgEng.kernel.resolveIteratorFirst(itt);obj!=0;obj=dlgEng.kernel.resolveIteratorNext(itt)) {
			++count;
			dlgEng.kernel.freeReference(obj);
		}
		
		infos=new RelativeInfo[count];
		count=0;
		
		//initialize infos
		for (long obj=dlgEng.kernel.resolveIteratorFirst(itt);obj!=0;obj=dlgEng.kernel.resolveIteratorNext(itt)) {
			infos[count++]=new RelativeInfo(dlgEng,obj);
			dlgEng.kernel.freeReference(obj);
		}
		dlgEng.kernel.freeIterator(itt);
		
		updateMinMax();
	}*/
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		updateMinMax();
		
		RelativeInfo first=null;
		RelativeInfo prev=null;

		for (RelativeInfo curr:infos) {
			if (prev!=null) {
				
				double r1=curr.rPre/(curr.rPre+prev.rPre);
				double r2=prev.rPre/(curr.rPre+prev.rPre);

				int curr_i=curr.obj.getComponentBounds().xr;
				int curr_j=curr.obj.getComponentBounds().xl;
				if (curr.vertical) {
					curr_i=curr.obj.getComponentBounds().xb;
					curr_j=curr.obj.getComponentBounds().xt;
				}
				int prev_i=prev.obj.getComponentBounds().xr;
				int prev_j=prev.obj.getComponentBounds().xl;
				if (prev.vertical) {
					prev_i=prev.obj.getComponentBounds().xb;
					prev_j=prev.obj.getComponentBounds().xt;
				}
				
				double weight=Layout.RELATIVEPREFERREDWEIGHT;
				eqo.addQuadraticDifference(curr_i, curr_j, r2*r2*weight);
				eqo.addQuadraticDifference(prev_i, prev_j, r1*r1*weight);
				eqo.addCombinedTerm(curr_i, prev_i, -2*r1*r2*weight);
				eqo.addCombinedTerm(curr_j, prev_j, -2*r1*r2*weight);
				eqo.addCombinedTerm(curr_i, prev_j, 2*r1*r2*weight);
				eqo.addCombinedTerm(curr_j, prev_i, 2*r1*r2*weight);
			} else
				first=curr;
			prev=curr;
		}
		
		if (first==null || first==prev || infos.size()==2)
			return;
		
		
		//finalize the cycle
		double r1=first.rPre/(first.rPre+prev.rPre);
		double r2=prev.rPre/(first.rPre+prev.rPre);
		
		int curr_i=first.obj.getComponentBounds().xr;
		int curr_j=first.obj.getComponentBounds().xl;
		if (first.vertical) {
			curr_i=first.obj.getComponentBounds().xb;
			curr_j=first.obj.getComponentBounds().xt;
		}
		int prev_i=prev.obj.getComponentBounds().xr;
		int prev_j=prev.obj.getComponentBounds().xl;
		if (prev.vertical) {
			prev_i=prev.obj.getComponentBounds().xb;
			prev_j=prev.obj.getComponentBounds().xt;
		}
		
		double weight=Layout.RELATIVEPREFERREDWEIGHT;
		eqo.addQuadraticDifference(curr_i, curr_j, r2*r2*weight);
		eqo.addQuadraticDifference(prev_i, prev_j, r1*r1*weight);
		eqo.addCombinedTerm(curr_i, prev_i, -2*r1*r2*weight);
		eqo.addCombinedTerm(curr_j, prev_j, -2*r1*r2*weight);
		eqo.addCombinedTerm(curr_i, prev_j, 2*r1*r2*weight);
		eqo.addCombinedTerm(curr_j, prev_i, 2*r1*r2*weight);
	}
	
}
