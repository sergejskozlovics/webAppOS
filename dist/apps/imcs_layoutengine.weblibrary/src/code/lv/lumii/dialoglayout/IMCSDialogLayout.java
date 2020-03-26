package lv.lumii.dialoglayout;

import java.util.*;
import lv.lumii.dialoglayout.components.Component;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;
import lv.lumii.dialoglayout.components.utils.RelativeInfo;
import lv.lumii.dialoglayout.components.utils.RelativeGroup;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

public class IMCSDialogLayout {
	
	public interface ComponentCallback {
		String getAnchor(long rComponent);
		
		void load(long rComponent, long rParent);
		
		long[] getChildren(long rComponent);
		ComponentBounds getBounds(long rComponent);
		RelativeInfo getHorizontalRelativeInfo(long rComponent);
		RelativeInfo getVerticalRelativeInfo(long rComponent);
		long getHorizontalRelativeInfoGroup(long rComponent); // returns the group ref, or the container ref (container==group);
		long getVerticalRelativeInfoGroup(long rComponent);
		
		String getLayoutName(long rComponent); // null (for leaf components), "VerticalBox", "HorizontalBox", "VerticalScrollBox", "HorizontalScrollBox", "ScrollBox", "Column", "Row", "Stack"
		
		void layout(long rComponent, int x, int y, int w, int h); // relative to parent
		void destroy(long rComponent);		
		
		void beforeLoad(long rRootComponent);
		void afterLoad();
	}
	
	private class JSCallbackWrapper implements ComponentCallback {
		private Object jscallback;
		
		public JSCallbackWrapper(Object _jscallback) {
			jscallback = _jscallback;
		}
		
		native public String getAnchor(double rComponent)/*-{
			var retVal = this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getAnchor(rComponent);
			if (!retVal)
				return "parent";
			return retVal;
		}-*/;

		@Override
		public String getAnchor(long rComponent) {
			return getAnchor((double)rComponent);
		}
		
		native public void load(double rComponent, double rParent)/*-{
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.load(rComponent, rParent);
		}-*/;
		
		@Override
		public void load(long rComponent, long rParent) {
			load((double)rComponent, (double)rParent);
		}
				
		native public ArrayList<Double> getChildren(double rComponent)/*-{
			var arr = this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getChildren(rComponent);
			if (arr) {
				var retVal = @java.util.ArrayList::new()();
				for (var i=0; i<arr.length; i++) {
					var obj = @java.lang.Double::new(D)(arr[i]);
					retVal.@java.util.ArrayList::add(Ljava/lang/Object;)(obj);
				}
				return retVal;
			}
			else
				return @java.util.ArrayList::new()();
		}-*/;

		@Override
		public long[] getChildren(long rComponent) {
			ArrayList<Double> tmp = getChildren((double)rComponent);
			long[] retVal = new long[tmp.size()];
			for (int i=0; i<tmp.size(); i++)
				retVal[i] = tmp.get(i).longValue();
			return retVal;
		}

		native public ComponentBounds _getBounds(double rComponent)/*-{
		  var js = this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getBounds(rComponent); 
		  var j = @lv.lumii.dialoglayout.components.utils.ComponentBounds::new()();
		  
		  if (js.minimumWidth)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::minW = @java.lang.Integer::new(I)(js.minimumWidth);
		  if (js.maximumWidth)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::maxW = @java.lang.Integer::new(I)(js.maximumWidth);
		  if (js.preferredWidth)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::preW = @java.lang.Integer::new(I)(js.preferredWidth);
		  
		  if (js.minimumHeight)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::minH = @java.lang.Integer::new(I)(js.minimumHeight);
		  if (js.maximumHeight)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::maxH = @java.lang.Integer::new(I)(js.maximumHeight);
		  if (j.preferredHeight)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::preH = @java.lang.Integer::new(I)(js.preferredHeight);
		  
		  if (js.leftMargin)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::leftM = @java.lang.Integer::new(I)(js.leftMargin);
		  if (js.rightMargin)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::rightM = @java.lang.Integer::new(I)(js.rightMargin);
		  if (js.topMargin)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::topM = @java.lang.Integer::new(I)(js.topMargin);
		  if (js.bottomMargin)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::bottomM = @java.lang.Integer::new(I)(js.bottomMargin);

		// for containers:
		  if (js.leftPadding)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::leftP = @java.lang.Integer::new(I)(js.leftPadding);
		  if (js.rightPadding)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::rightP = @java.lang.Integer::new(I)(js.rightPadding);
		  if (js.topPadding)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::topP = @java.lang.Integer::new(I)(js.topPadding);
		  if (js.bottomPadding)
		  	j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::bottomP = @java.lang.Integer::new(I)(js.bottomPadding);
		  	
		  if (js.horizontalSpacing)
		    j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::horSpace = @java.lang.Integer::new(I)(js.horizontalSpacing);
		  if (js.verticalSpacing)
		    j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::verSpace = @java.lang.Integer::new(I)(js.verticalSpacing);
		  
		  if ((js.horizontalAlignment=="LEFT")||(js.horizontalAlignment=="RIGHT")||(js.horizontalAlignment=="CENTER"))
		    j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::horAlign = js.horizontalAlignment;
		    
		  if ((js.verticalAlignment=="TOP")||(js.verticalAlignment=="BOTTOM")||(js.verticalAlignment=="CENTER"))
		    j.@lv.lumii.dialoglayout.components.utils.ComponentBounds::verAlign = js.verticalAlignment;
		  
		  return j;
		}-*/;

		@Override
		public ComponentBounds getBounds(long rComponent) {
			ComponentBounds retVal = _getBounds((double)rComponent);
			return retVal;
		}
		
		native public RelativeInfo _getHorizontalRelativeInfo(double rComponent)/*-{
		  var js = this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getHorizontalRelativeInfo(rComponent); 
		  var j = @lv.lumii.dialoglayout.components.utils.RelativeInfo::new()();
		  
		  if (js.minimumRelativeWidth)
		  	j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::rMin = @java.lang.Double::new(D)(js.minimumRelativeWidth);
		  if (js.preferredRelativeWidth)
		  	j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::rPre = @java.lang.Double::new(D)(js.preferredRelativeWidth);
		  if (js.maximumRelativeWidth)
		  	j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::rMax = @java.lang.Double::new(D)(js.maximumRelativeWidth);
		  j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::vertical = false;
		  
		  return j;
		}-*/;
		
		@Override
		public RelativeInfo getHorizontalRelativeInfo(long rComponent) {
			return _getHorizontalRelativeInfo((double)rComponent);
		}
		
		native public RelativeInfo _getVerticalRelativeInfo(double rComponent)/*-{
		  var js = this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getVerticalRelativeInfo(rComponent); 
		  var j = @lv.lumii.dialoglayout.components.utils.RelativeInfo::new()();
		  
		  if (js.minimumRelativeHeight)
		  	j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::rMin = @java.lang.Double::new(D)(js.minimumRelativeHeight);
		  if (js.preferredRelativeHeight)
		  	j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::rPre = @java.lang.Double::new(D)(js.preferredRelativeHeight);
		  if (js.maximumRelativeHeight)
		  	j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::rMax = @java.lang.Double::new(D)(js.maximumRelativeHeight);
		  j.@lv.lumii.dialoglayout.components.utils.RelativeInfo::vertical = true;
		  
		  return j;
		}-*/;
		
		@Override
		public RelativeInfo getVerticalRelativeInfo(long rComponent) {
			return _getVerticalRelativeInfo((double)rComponent);			
		}

		native public double _getHorizontalRelativeInfoGroup(double rComponent)/*-{
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getHorizontalRelativeInfoGroup(rComponent);
		}-*/;
		
		@Override
		public long getHorizontalRelativeInfoGroup(long rComponent) {
			return (long)_getHorizontalRelativeInfoGroup((double)rComponent);
		}		

		native public double _getVerticalRelativeInfoGroup(double rComponent)/*-{
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getVerticalRelativeInfoGroup(rComponent);
		}-*/;

		@Override
		public long getVerticalRelativeInfoGroup(long rComponent) {
			return (long)_getVerticalRelativeInfoGroup((double)rComponent);			
		}

		native public String _getLayoutName(double rComponent)/*-{
			return this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.getLayoutName(rComponent);
		}-*/;
		
		@Override
		public String getLayoutName(long rComponent) {
			return _getLayoutName((double)rComponent);
		}
		
		native public void layout(double rComponent, int x, int y, int w, int h)/*-{
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.layout(rComponent, x, y, w, h);
		}-*/;
		
		@Override
		public void layout(long rComponent, int x, int y, int w, int h) {
			layout((double)rComponent, x, y, w, h);
		}

		native public void _destroy(double rComponent)/*-{
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.destroy(rComponent);
		}-*/;
		
		@Override
		public void destroy(long rComponent) {
			_destroy((double)rComponent);
		}

		native public void beforeLoad(double rRootComponent)/*-{
		if (this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.beforeLoad)
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.beforeLoad(rRootComponent);
		}-*/;
		
		@Override
		public void beforeLoad(long rRootComponent) {
			beforeLoad((double)rRootComponent);
		}

		@Override
		native public void afterLoad()/*-{
		if (this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.afterLoad)
			this.@lv.lumii.dialoglayout.IMCSDialogLayout.JSCallbackWrapper::jscallback.afterLoad();
		}-*/;
	}
	
	private ComponentCallback callback; // used only from Component	
	private Map<Long, RelativeGroup> groups = new HashMap<Long, RelativeGroup>();
	private double[] solution = null;
	
	public RelativeGroup getRelativeGroup(long id) {
		RelativeGroup retVal = groups.get(id);
		if (retVal == null)
			retVal = new RelativeGroup();
		groups.put(id, retVal);
		return retVal;
	}
	
    private native static boolean _inJavaScript() 
    /*-{ return true }-*/;

    public static boolean inJavaScript() {
    	boolean retVal = false;
    	try {
    		retVal = _inJavaScript();
    	}
    	catch(Throwable t) {
    		
    	}
    	return retVal;
    }

    public static native void _consoleLog( String s ) 
    /*-{ console.log( s ); }-*/;
    
    public static native void _printTime( String s ) 
    /*-{ var d = new Date(); console.log( s+" "+d.getTime() ); }-*/;
    
    public static void consoleLog( String s ) {
    	if (inJavaScript())
    		_consoleLog(s);
    	else
    		System.out.println(s);
    }
	
    public static void printTime( String s ) {
    	if (inJavaScript())
    		_printTime(s);
    	else
    		System.out.println(s+" "+System.currentTimeMillis());
    }
    
	public IMCSDialogLayout(Object _callback) {
		if (inJavaScript()) {
			callback = new JSCallbackWrapper(_callback);
		}
		else
			callback = (ComponentCallback)_callback;
	}
	
	private Component root = null;
	

	private native boolean notAllLoadedAndLayoutScheduled(boolean needsResize, int prefFormWidth, int prefFormHeight) /*-{
		if (this.@lv.lumii.dialoglayout.IMCSDialogLayout::counter != 0) {
		  setTimeout(function(myThis) {
		  	myThis.@lv.lumii.dialoglayout.IMCSDialogLayout::getBoundsAndLayoutComponents(ZII)(needsResize, prefFormWidth, prefFormHeight);
		  }, 50, this);
		  return true;
		}
		return false;
	}-*/;
	
	
	public void getBoundsAndLayoutComponents(final boolean needsResize, final int prefFormWidth, final int prefFormHeight) { //form needs to be resized only for the first time
		if (inJavaScript()) {
			if (notAllLoadedAndLayoutScheduled(needsResize, prefFormWidth, prefFormHeight))
				return;
		}
		
		this.callback.afterLoad();
		printTime("imcs_de step 2");
		
		ComponentBounds bounds = root.getComponentBounds();
		
		// trying to minimize the form size
		if (bounds.preW == null)
			bounds.preW = prefFormWidth;
		if (bounds.preH == null) 
			bounds.preH = prefFormHeight;
		
		//get number of variables
		
		int n;
		Map<Integer, Double> initialValues = new HashMap<Integer, Double>();
		if (solution!=null) {
			n = Layout.updateIndicesFor(0,root,solution,initialValues); // use the previous solution to set the initial values for the variables
		}
		else
			n = Layout.updateIndicesFor(0,root);
    	
    	//initialize optimizer
    	ExtendedQuadraticOptimizer eqo=new ExtendedQuadraticOptimizer(n);
    	for (Integer k : initialValues.keySet()) {
    		eqo.setVariable(k, initialValues.get(k));
    	}
    	eqo.setEpsilon(0.1);
    	
    	//consider form and it's children
    	root.writeConstraints(eqo);
    	
    	//consider groups
		if (needsResize)
	    	for (RelativeGroup group:groups.values()) {
	    		group.writeConstraints(eqo);
	    	}
    	
    	//get a solution
		
		eqo.addConstantEquality(0, 0); // form left
		eqo.addConstantEquality(1, 0); // form top
		
		
 		solution=eqo.performOptimization(true); // isDialog=true; writing result into global solution
		
		//if there is no solution do nothing
		if (solution==null) {
			consoleLog("No solution found for the dialog window.");
			throw new RuntimeException("No solution found for the dialog window.");
		}
		
		root.resize(solution, null);
	}
	
	private int counter = 0;
	public void loadStarted(long rComponent) {
		counter++;
	}
	
	public void loadStarted(double rComponent) { // compatibility with JavaScript
		counter++;
	}
	
	public void loadFinished(long rComponent) {
		counter--;
	}
	
	public void loadFinished(double rComponent) { // compatibility with JavaScript
		counter--;
	}
	
	public void loadAndLayout(long rForm) {
		
		if (root == null) {
			root = Component.createComponent(callback, rForm, null);
						
			this.callback.beforeLoad(rForm);
			getBoundsAndLayoutComponents(true, 0, 0);
		}
		else
			refreshAndLayout(rForm, 0, 0);
	}

	public void loadAndLayout(double rForm) { // compatibility with JavaScript
		loadAndLayout((long)rForm);
	}
	

	public void refreshAndLayout(long rComponent, int formWidth, int formHeight) {
		// TODO delete not only subtrees, but also relativeinfo-s of deleted components
		
		Component cmpnt = root.find(rComponent);
		cmpnt.destroyChildrenRecursively(callback);
		

		consoleLog("in refreshAndLayout "+formWidth+" "+formHeight);
		// TODO: resetBoundsRecursively()
		//root.clearBoundsRecursively();
		
		this.callback.beforeLoad(rComponent);
		cmpnt.reinitialize(); // re-loads this and children
		
		getBoundsAndLayoutComponents(true, formWidth, formHeight);
	}
	
	public void refreshAndLayout(double rRootComponent, int formWidth, int formHeight) { // compatibility with JavaScript
		refreshAndLayout((long)rRootComponent, formWidth, formHeight);
	}
	
}
