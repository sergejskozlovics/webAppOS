package lv.lumii.dialoglayout.components.utils;

import lv.lumii.dialoglayout.components.Component;

public class RelativeInfo {
	public Component obj;
	public Double rMin=null;
	public Double rPre=null;
	public Double rMax=null;
	public boolean vertical=false; 
	
	
	public void updateMin(Double k) {
		if (k==null || rMin==null)
			return;
		if (vertical)
			obj.getComponentBounds().minH=(int) (k*rMin);
		else
			obj.getComponentBounds().minW=(int) (k*rMin);
	}
	
	public void updateMax(Double k) {
		if (k==null || rMax==null)
			return;
		if (vertical)
			obj.getComponentBounds().maxH=(int)(k*rMax);
		else
			obj.getComponentBounds().maxW=(int)(k*rMax);
	}
	
	public Double getMinK() {
		if (rMin==null)
			return null;
		if (vertical) {
			if (obj.getComponentBounds().minH==null)
				return null;
			return obj.getComponentBounds().minH/rMin;
		}
		if (obj.getComponentBounds().minW==null)
			return null;
		return obj.getComponentBounds().minW/rMin;
	}
	
	public Double getMaxK() {
		if (rMax==null)
			return null;
		if (vertical) {
			if (obj.getComponentBounds().maxH==null)
				return null;
			return obj.getComponentBounds().maxH/rMax;
		}
		if (obj.getComponentBounds().maxW==null)
			return null;
		return obj.getComponentBounds().maxW/rMax;
	}
	
}
