package lv.lumii.diagramlayout;



import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import lv.lumii.layoutengine.ArrangeData;
import lv.lumii.layoutengine.Box;
import lv.lumii.layoutengine.Diagram;
import lv.lumii.layoutengine.LayoutConstraints;
import lv.lumii.layoutengine.Line;
import lv.lumii.layoutengine.OutsideLabel;
import lv.lumii.layoutengine.Line.LineType;



public class IMCSDiagramLayout {

	private lv.lumii.layoutengine.Diagram jDiagram; 
	private Map<Long, lv.lumii.layoutengine.Box> boxesMap = new HashMap<Long, lv.lumii.layoutengine.Box>();		
	private Map<Long, lv.lumii.layoutengine.Line> linesMap = new HashMap<Long, lv.lumii.layoutengine.Line>();
	private Map<lv.lumii.layoutengine.Line, Long> linesInvMap = new HashMap<lv.lumii.layoutengine.Line, Long>();
	private Map<Long, lv.lumii.layoutengine.OutsideLabel.LineLabel> lineLabelsMap = new HashMap<Long, lv.lumii.layoutengine.OutsideLabel.LineLabel>();
	private Map<lv.lumii.layoutengine.OutsideLabel.LineLabel, Long> lineLabelsInvMap = new HashMap<lv.lumii.layoutengine.OutsideLabel.LineLabel, Long>();

	private boolean fastAdd = true;
	private boolean lastLine = false;
	private boolean transposeXY = false; // for horizontal hierarchical layout
	private boolean inverseY = false; // for changing the direction of hierarchical layout
	// (only for y, since x may be transposed to y before)

	private double minX = 0; 
	private double minY = 0; 
	private double maxX = 0;
	private double maxY = 0;
	private boolean transactionStarted = false;


	private static Diagram private_createUniversalDiagram() {
		Diagram d = new Diagram(new Rectangle2D.Double(), ArrangeData.ArrangeStyle.UNIVERSAL, LayoutConstraints.ConstraintType.NONE);
		return d;
	}

	private static Diagram private_createHierarchicalDiagram() {
		Diagram d = new Diagram(new Rectangle2D.Double(), ArrangeData.ArrangeStyle.FLOW, LayoutConstraints.ConstraintType.NONE);
		return d;
	}

	private static Diagram private_createSymmetricDiagram() {
		Diagram d = new Diagram(new Rectangle2D.Double(), ArrangeData.ArrangeStyle.SPRING_EMBEDDED, LayoutConstraints.ConstraintType.NONE);
		return d;
	}

	private static Box private_addBox(Diagram d, double x, double y, double w, double h)
	{
		Box b = d.createBox(new Rectangle2D.Double(x, y, w, h), 5);
		return b;
	}
	
	private static lv.lumii.layoutengine.Line.LineType getLineTypeFromString(String lineType) {
		lv.lumii.layoutengine.Line.LineType t =
				lv.lumii.layoutengine.Line.LineType.ORTHOGONAL;
		if ("POLYLINE".equals(lineType))
			t =  lv.lumii.layoutengine.Line.LineType.POLYLINE;
		else
			if ("STRAIGHT".equals(lineType))
				t =  lv.lumii.layoutengine.Line.LineType.STRAIGHT;
		return t;
	}

	private static Line private_addLine(Box src, Box tgt, String lineType)
	{
		lv.lumii.layoutengine.Line.LineType t = getLineTypeFromString(lineType);
		Line l = src.connectTo(tgt, t, 5);
		
		
		return l;
	}
	
	public static void validateOrthogonalPoints(ArrayList<Point2D.Double> points) {		
        // check by SK
        Point2D.Double prev = points.get(0), curr;
        for (ListIterator<Point2D.Double> it = points.listIterator(1); it.hasNext();) {
            curr = it.next();
            if ((curr.x!=prev.x) && (curr.y!=prev.y))
            	throw new RuntimeException("Non-orthogonal points for orthogonal line: "+prev+" -> "+curr);
            prev = curr;
        }			
	}

	private static Line private_addLineWithPoints(Box src, Box tgt, String lineType, ArrayList<Point2D.Double> points)
	{
		
        
		lv.lumii.layoutengine.Line.LineType t = getLineTypeFromString(lineType);
		
		if (t == LineType.ORTHOGONAL) {
			validateOrthogonalPoints(points);
		}
		
		double eps = 0.01;
		Point2D.Double first=points.get(0), last=points.get(points.size()-1);
		if ( (first.getX()>=src.getLeft()-eps) && (first.getX()<=src.getRight()+eps) &&
				(first.getY()>=src.getTop()-eps) && (first.getY()<=src.getBottom()+eps) ) 
			;
		else
			throw new RuntimeException("Wrong line connection point: first point="+first+"; source box="+src.getLeft()+" "+src.getRight()+" "+src.getTop()+" "+src.getBottom());

		if ( (last.getX()>=tgt.getLeft()-eps) && (last.getX()<=tgt.getRight()+eps) &&
				(last.getY()>=tgt.getTop()-eps) && (last.getY()<=tgt.getBottom()+eps) ) 
			;
		else
			throw new RuntimeException("Wrong line connection point: last point="+last+"; target box="+tgt.getLeft()+" "+tgt.getRight()+" "+tgt.getTop()+" "+tgt.getBottom());
		Line l = src.connectTo(tgt, t, 5, points);
		return l;
	}

	private static lv.lumii.layoutengine.OutsideLabel.LineLabel private_addLineLabel(Line l, double w, double h, String placement)
	{
		OutsideLabel.LineLabel.Orientation o = OutsideLabel.LineLabel.Orientation.COUNTERCLOCKWISE;
		double p = 0.5;

		if ("start-left".equalsIgnoreCase(placement)) {
			p = 0;
			o = OutsideLabel.LineLabel.Orientation.COUNTERCLOCKWISE;
		}
		else
			if ("start-right".equalsIgnoreCase(placement)) {
				p = 0;
				o = OutsideLabel.LineLabel.Orientation.CLOCKWISE;
			}
			else
				if ("end-left".equalsIgnoreCase(placement)) {
					p = 1.0;
					o = OutsideLabel.LineLabel.Orientation.COUNTERCLOCKWISE;
				}
				else
					if ("end-right".equalsIgnoreCase(placement)) {
						p = 1.0;
						o = OutsideLabel.LineLabel.Orientation.CLOCKWISE;
					}
					else
						if ("middle-left".equalsIgnoreCase(placement)) {
							o = OutsideLabel.LineLabel.Orientation.COUNTERCLOCKWISE;
						}
						else
							if ("middle-right".equalsIgnoreCase(placement)) {
								o = OutsideLabel.LineLabel.Orientation.CLOCKWISE;
							}


		return l.createLabel(w, h, p, o,
				lv.lumii.layoutengine.LayoutConstraints.ConstraintType.NONE,
				2.0);
	}

	private double myRound(double x) {
		return Math.round(x);
	}

	public IMCSDiagramLayout(String arrangeStyle) {

		if ((arrangeStyle!=null) && ((arrangeStyle.charAt(0)=='H')||(arrangeStyle.charAt(0)=='V')||(arrangeStyle.charAt(0)=='I'))) {
			jDiagram = private_createHierarchicalDiagram();
			if (arrangeStyle.charAt(0) == 'H')
				this.transposeXY = true; // for horizontal
			if (arrangeStyle.charAt(0) == 'I') { // for inverse
				this.inverseY = true;
				if (arrangeStyle.charAt(8) == 'H')
					this.transposeXY = true; // for horizontal
			}
		}
		else
			if ((arrangeStyle!=null) && (arrangeStyle.charAt(0)=='S')) {
				jDiagram = private_createSymmetricDiagram();
			}
			else
				jDiagram = private_createUniversalDiagram();

	}


	public boolean addBox(long boxId, double x, double y, double w, double h) {
		// adds a box with the given id, x, y, width, and height to the layout;
		// returns whether the operation succeeded;
		if (this.transposeXY) {
			double t = x; x = y; y = t;
			t = w; w = h; h = t;
		}
		if (this.inverseY)
			y = -y;

		if (this.boxesMap.get(boxId)!=null)
			return false;

		if (!this.transactionStarted) {
			jDiagram.startTransaction();
			this.transactionStarted = true;
		}


		Box jBox = private_addBox(this.jDiagram, x, y, w, h);
		this.boxesMap.put(boxId, jBox);
		this.lastLine = false;
		return true;
	}

	public boolean addBox(double boxId, double x, double y, double w, double h) { // compatibility with JavaScript
		return addBox((long)boxId, x, y, w, h);
	}
	
	public boolean addLine(long lineId, long srcId, long tgtId, String lineType, int startSides, int endSides, ArrayList<Point2D.Double> points) {
		// adds an orthogonal line connecting the two boxes with the given ids to the layout;
		// returns whether the operation succeeded;
		if (this.linesMap.get(lineId)!=null)
			return false;

		Box jSrc = this.boxesMap.get(srcId);
		Box jTgt = this.boxesMap.get(tgtId);
		if ((jSrc!=null) && (jTgt!=null)) {

			if (!this.transactionStarted) {
				this.jDiagram.startTransaction();
				this.transactionStarted = true;
			}

			if (lineType==null)
				lineType = "ORTHOGONAL";

			Line jLine;
			if (this.fastAdd && (points!=null)) {
				ArrayList<Point2D.Double> jPoints = new ArrayList<Point2D.Double>();
				for (int i=0; i<points.size(); i++) {
					Point2D.Double p = new Point2D.Double();
					p.setLocation(points.get(i));

					if (this.transposeXY) {
						double t=p.x; p.x=p.y; p.y=t;
					}
					if (this.inverseY) {
						p.y = this.maxY - p.y;
					}
					else {
						p.y = this.minY + p.y;
					}
					p.x = this.minX + p.x;

					jPoints.add(p);
				}
				try { // try-catch by SK
					jLine = private_addLineWithPoints(jSrc, jTgt, lineType, jPoints);
					points=null;					
				}
				catch(Throwable t){
//					System.err.println("p");
					jLine = private_addLine(jSrc, jTgt, lineType); //with tracing
					points=null;
				}
			}
			else {
				jLine = private_addLine(jSrc, jTgt, lineType); //with tracing                   
			}
			
			if ((startSides <= 0) || (startSides > 15))
				startSides = 15;
			if ((endSides <= 0) || (endSides > 15))
				endSides = 15;

			if (startSides != 15) {
				if (this.transposeXY) {
					int t = startSides & 1;                // get bit0
					if ((startSides & 8) != 0)
						startSides = startSides | 1; // set bit0 to bit3=1
					else
						startSides = (startSides & 14); // set bit0 to bit3=0
					if (t!=0)
						startSides = startSides | 8; // set bit3 to bit0=1
					else
						startSides = startSides & 7; // set bit3 to bit0=0

					t = startSides & 2;                // get bit1
					if ((startSides & 4)!=0)
						startSides = startSides | 2; // set bit1 to bit2=1
					else
						startSides = (startSides & 13); // set bit1 to bit2=0
					if (t!=0)
						startSides = startSides | 4; // set bit2 to bit1=1
					else
						startSides = startSides & 11; // set bit2 to bit1=0
				}
				if (this.inverseY) {
					int t = startSides & 1;                // get bit0
					if ((startSides & 4)!=0)
						startSides = startSides | 1; // set bit0 to bit2=1
					else
						startSides = (startSides & 14); // set bit0 to bit2=0
					if (t!=0)
						startSides = startSides | 4; // set bit2 to bit0=1
					else
						startSides = startSides & 11; // set bit2 to bit0=0
				}

				//ArrayList<Double> points = jLine.getPoints();
				if (this.fastAdd)
					jLine.setStartSides(startSides);
				else {
					this.jDiagram.endTransaction();
					this.jDiagram.startTransaction();
					jLine.setStartSides(startSides);
					this.jDiagram.endTransaction();
					this.jDiagram.startTransaction();
				}
			}
			if (endSides != 15) {
				if (this.transposeXY) {
					int t = endSides & 1;                // get bit0
					if ((endSides & 8)!=0)
						endSides = endSides | 1; // set bit0 to bit3=1
					else
						endSides = (endSides & 14); // set bit0 to bit3=0
					if (t!=0)
						endSides = endSides | 8; // set bit3 to bit0=1
					else
						endSides = endSides & 7; // set bit3 to bit0=0

					t = endSides & 2;                // get bit1
					if ((endSides & 4)!=0)
						endSides = endSides | 2; // set bit1 to bit2=1
					else
						endSides = (endSides & 13); // set bit1 to bit2=0
					if (t!=0)
						endSides = endSides | 4; // set bit2 to bit1=1
					else
						endSides = endSides & 11; // set bit2 to bit1=0
				}
				if (this.inverseY) {
					int t = endSides & 1;                // get bit0
					if ((endSides & 4)!=0)
						endSides = endSides | 1; // set bit0 to bit2=1
					else
						endSides = (endSides & 14); // set bit0 to bit2=0
					if (t!=0)
						endSides = endSides | 4; // set bit2 to bit0=1
					else
						endSides = endSides & 11; // set bit2 to bit0=0
				}

				if (this.fastAdd)
					jLine.setEndSides(endSides);
				else {
					this.jDiagram.endTransaction();
					this.jDiagram.startTransaction();
					jLine.setEndSides(endSides);
					this.jDiagram.endTransaction();
					this.jDiagram.startTransaction();
				}
			}
			this.linesMap.put(lineId, jLine);
			this.linesInvMap.put(jLine, lineId);

			if (points!=null) {
				ArrayList<Point2D.Double> jPoints = new ArrayList<Point2D.Double>();
				for (int i=0; i<points.size(); i++) {
					Point2D.Double p = new Point2D.Double();
					p.setLocation(points.get(i));
					if (this.transposeXY) {
						double t=p.x; p.x=p.y; p.y=t;
					}
					if (this.inverseY) {
						p.y = this.maxY - p.y;
					}
					else {
						p.y = this.minY + p.y;
					}
					p.x = this.minX + p.x;

					jPoints.add(p);
				}

				jLine.setPoints(jPoints);

/*				if (!this.lastLine)
					if (this.transactionStarted) {
						this.jDiagram.endTransaction();
						this.jDiagram.startTransaction();
					}*/  

			}

			if (!this.fastAdd) {
				if (!this.lastLine)
					if (this.transactionStarted) {
						this.jDiagram.endTransaction();
						this.jDiagram.startTransaction();
					}
			}
			this.lastLine = true;
			return true;
		}
		else
			return false;
	}


	public boolean addLine(double lineId, double srcId, double tgtId, String lineType, int startSides, int endSides, ArrayList<Point2D.Double> points) {
		return addLine((long)lineId, (long)srcId, (long)tgtId, lineType, startSides, endSides, points);
	}
	
	//          "start-left", i.e., left, if we follow the line direction (from start to end), near the start
	//          "start-right",
	//          "end-left", i.e., left, if we follow the line direction, near the end
	//          "end-right",
	//          "middle-left",
	//          "middle-right",

	public boolean addLineLabel(long labelId, long lineId, double w, double h, String placement) {
		if (this.lineLabelsMap.get(labelId)!=null)
			return false;

		if (this.transposeXY) {
			double t = w; w = h; h = t;
		}

		this.lastLine = false;
		Line jLine = this.linesMap.get(lineId);
		if (jLine!=null) {

			if (!jLine.hasGeometry()) {
				if (this.transactionStarted) { // by SK
					this.jDiagram.endTransaction();
					this.transactionStarted = false;
				}
			}
			
			if (!this.transactionStarted) {
				this.jDiagram.startTransaction();
				this.transactionStarted = true;
			}
			

			OutsideLabel.LineLabel jLabel = private_addLineLabel(jLine, w, h, placement);			
			
			this.lineLabelsMap.put(labelId, jLabel);
			this.lineLabelsInvMap.put(jLabel, labelId);
			

			return true;
		}
		else
			return false;


	}

	public boolean addLineLabel(double labelId, double lineId, double w, double h, String placement) {
		return addLineLabel((long)labelId, (long)lineId, w, h, placement);
	}
	
	public ArrayList<Long> removeLine(long lineId) {
		// removes the given line from the layout;
		// the layout is not re-arranged;
		// returns the array of id-s of the removed line labels (perhaps, an empty array);
		Line jLine = this.linesMap.get(lineId);
		if (jLine!=null) {
			ArrayList<Long> retVal = new ArrayList<Long>();

			// delete labels...
			ArrayList<OutsideLabel.LineLabel> jLabels = jLine.getLabels();
			int n = jLabels.size();
			for (int i=0; i<n; i++) {
				OutsideLabel.LineLabel jLabel = jLabels.get(i);
				Long labelId = this.lineLabelsInvMap.get(jLabel);
				if (labelId!=null) {
					lineLabelsMap.remove(labelId);
					lineLabelsInvMap.remove(jLabel);
					retVal.add(labelId);
				}
			}
			this.linesMap.remove(lineId);
			this.linesInvMap.remove(jLine);
			jLine.remove(false);
			// removes also the labels

			this.lastLine = true;
			return retVal;
		}
		else
			return null;
	}

	public ArrayList<Long> removeLine(double lineId) {
		return removeLine((long)lineId);
	}
	
	public ArrayList<Long> removeBox(long boxId) {
		// remove the box as well as the incident lines from the layout;
		// the layout is not re-arranged;
		// returns the array of id-s of the removed lines (perhaps, an empty array);
		// on error, returns false
		Box jBox = this.boxesMap.get(boxId);
		if (jBox!=null) {
			ArrayList<Long> retVal = new ArrayList<Long>();
			ArrayList<Line> jLines = jBox.getIncidentLines();
			int n = jLines.size();
			for (int i=0; i<n; i++) {
				Line jLine = jLines.get(i);
				Long lineId = this.linesInvMap.get(jLine);
				if (lineId!=null) {
					if (this.removeLine(lineId)!=null)
						retVal.add(lineId);
				}
			}
			this.boxesMap.remove(boxId);
			jBox.remove(false);
			this.lastLine = false;
			return retVal;
		}
		else
			return null;
	}

	public ArrayList<Long> removeBox(double boxId) {
		return removeBox((long)boxId);
	}
	
	public boolean moveBox(long boxId, double newX, double newY) {
		// sets new desired coordinates for the box with the given id;
		// the layout is not re-arranged (call arrange() after
		// the desired coordinates of all the desired boxes are set);
		// returns whether the operation succeeded;
		Box jBox = this.boxesMap.get(boxId);
		if (jBox!=null) {
			if (this.transactionStarted) {
				this.jDiagram.endTransaction();
				this.transactionStarted = false;
			}

			if (!this.transactionStarted) {
				this.jDiagram.startTransaction();
				this.transactionStarted = true;
			}

			double h = myRound(jBox.getHeight());
			if (this.transposeXY) {
				double t=newX; newX=newY; newY=t;
				h = myRound(jBox.getWidth());
			}

			double oldX = myRound(jBox.getLeft());
			double oldY = myRound(jBox.getTop());

			oldX = oldX-this.minX;
			oldY = oldY-this.minY;


			if (this.inverseY) {
				newY = (this.maxY-this.minY) - newY - h;
			}

			double dx = newX - oldX;
			double dy = newY - oldY;

			Point2D.Double jPointVector = new Point2D.Double(dx, dy);
			lv.lumii.layoutengine.Container owner = jBox.getOwner();
			jBox.move(jPointVector, owner);

			this.lastLine = false;
			return true;
		}
		else
			return false;
	}
	
	public boolean moveBox(double boxId, double newX, double newY) {
		return moveBox((long)boxId, newX, newY);
	}

	public boolean resizeBox(long boxId, double w, double h) {
		// sets new desired dimensions for the box with the given id;
		// the layout is not re-arranged (call arrange() after
		// the desired dimensions of all the desired boxes are set);
		// returns whether the operation succeeded;
		Box jBox = this.boxesMap.get(boxId);
		if (jBox!=null) {
			double hh = myRound(jBox.getHeight());
			double ww = myRound(jBox.getWidth());
			if ((Math.abs(hh-h)<0.01) && (Math.abs(ww-w)<0.01))
				return true;

			if (!this.transactionStarted) {
				this.jDiagram.startTransaction();
				this.transactionStarted = true;
			}

			if (this.transposeXY) {
				double t=w; w=h; h=t;
			}

			double x = myRound(jBox.getLeft());
			double y = myRound(jBox.getTop());

			Rectangle2D.Double jRect = new Rectangle2D.Double(x, y, w, h);
			jBox.resize(jRect);

			return true;
		}
		else
			return false;
	}
	
	public boolean resizeBox(double boxId, double w, double h) {
		return resizeBox((long)boxId, w, h);
	}

	public boolean moveLine(long lineId, long srcId, long tgtId, ArrayList<Point2D.Double> points) {
		// sets new line start and end boxes and (optionally) line points;
		// the points are specified as an array of objects with the x and y attributes;
		// the layout is not re-arranged (call arrange() after all
		// desired manipulations are called);
		// returns whether the operation succeeded;
		Line jLine = this.linesMap.get(lineId);
		Box jSrc = this.boxesMap.get(srcId);
		Box jTgt = this.boxesMap.get(tgtId);

		if ((jLine!=null) && (jSrc!=null) && (jTgt!=null)) {
			lv.lumii.layoutengine.Element jPrevSrc = jLine.getStart();
			lv.lumii.layoutengine.Element jPrevTgt = jLine.getEnd();
			if (!this.transactionStarted) {
				this.jDiagram.startTransaction();
				this.transactionStarted = true;
			}

			ArrayList<OutsideLabel.LineLabel> jLabels = jLine.getLabels();
			int n = jLabels.size();
			boolean hasLabels = (n>0);
			if (hasLabels) {
				// delete and re-create the line...
				Line newLine;
				
				if (points != null) {
					ArrayList<Point2D.Double> jPoints = new ArrayList<Point2D.Double>();
					for (int i=0; i<points.size(); i++) {
						Point2D.Double p = new Point2D.Double(); 
						p.setLocation( points.get(i) );
						if (this.transposeXY) {
							double t=p.x; p.x=p.y; p.y=t;
						}
						if (this.inverseY) {
							p.y = this.maxY - p.y;
						}
						else {
							p.y = this.minY + p.y;
						}
						p.x = this.minX + p.x;
						jPoints.add(p);
					}
					newLine = ((Box) jPrevSrc).connectTo((Box) jPrevTgt, jLine.getType(), 5, points);
				}
				else
					newLine = ((Box) jPrevSrc).connectTo((Box) jPrevTgt, jLine.getType(), 5);
				
				
				for (int i=0; i<n; i++) {
					OutsideLabel.LineLabel jLabel = jLabels.get(i);
					
					double w = jLabel.getWidth();
					double h = jLabel.getHeight();
					double p = jLabel.getPosition();
					OutsideLabel.LineLabel.Orientation o = jLabel.getOrientation();
					
					OutsideLabel.LineLabel newLabel = newLine.createLabel(w, h, p, o,
							lv.lumii.layoutengine.LayoutConstraints.ConstraintType.NONE,
							2.0);
										
					Long labelId = this.lineLabelsInvMap.get(jLabel);
					if (labelId!=null) {
						lineLabelsMap.remove(labelId);
						lineLabelsInvMap.remove(jLabel);
					}
					
					lineLabelsMap.put(labelId, newLabel);
					lineLabelsInvMap.put(newLabel, labelId);
				}
				
				// removing the previous line (with old labels)...
				this.linesMap.remove(lineId);
				this.linesInvMap.remove(jLine);
				jLine.remove(false);
				
				// associating the new line...
				this.linesMap.put(lineId, newLine);
				this.linesInvMap.put(newLine, lineId);
			}
			else			
			// retrace...
			if (points!=null) {
				ArrayList<Point2D.Double> jPoints = new ArrayList<Point2D.Double>();
				for (int i=0; i<points.size(); i++) {
					Point2D.Double p = new Point2D.Double(); 
					p.setLocation( points.get(i) );
					if (this.transposeXY) {
						double t=p.x; p.x=p.y; p.y=t;
					}
					if (this.inverseY) {
						p.y = this.maxY - p.y;
					}
					else {
						p.y = this.minY + p.y;
					}
					p.x = this.minX + p.x;
					jPoints.add(p);
				}

				if ((jSrc == jPrevSrc) && (jTgt == jPrevTgt))
					jLine.setPoints(jPoints);
				else
					jLine.setPoints(jPoints, jSrc, jTgt);
				// or better call retrace?
			}
			else
				jLine.retrace(jSrc, jTgt);			

			this.lastLine = true;
			return true;
		}
		else
			return false;
	};

	public boolean moveLine(double lineId, double srcId, double tgtId, ArrayList<Point2D.Double> points) {
		return moveLine((long)lineId, (long)srcId, (long)tgtId, points);
	}

	public static class LayoutInfo {
		public Map<Long,	Rectangle2D.Double> boxes = new HashMap<Long, Rectangle2D.Double>();
		public Map<Long,	ArrayList<Point2D.Double>> lines = new HashMap<Long, ArrayList<Point2D.Double> >();
		public Map<Long,	Rectangle2D.Double> labels = new HashMap<Long, Rectangle2D.Double>();
		public double width = 0.0;
		public double height = 0.0;
	}

	public LayoutInfo arrangeIncrementally() {
		// arranges the diagram taking into a consideration recently added elements
		// and trying to preserve existing coordinates;
		// returns an objects with the "boxes", "lines", and "labels" maps
		// containing information about the layout;
		// the boxes map is in the form <id> -> {x, y, width, height};
		// the lines map is in the form <id> -> [ {x:x1,y:y1}, {x:x2,y:y2}, ... ]
		// the labels map is in the form <id> -> {x, y, width, height};


		if (this.transactionStarted) {			
			this.jDiagram.endTransaction();
			this.transactionStarted = false;
		}
		
		return this.getLayout();
	}

	public LayoutInfo arrangeFromScratch() {
		// arranges the diagram from scratch not preserving existing coordinates;
		// returns an objects with the "boxes", "lines", and "labels" maps
		// containing information about the layout;
		// the boxes map is in the form <id> -> {x, y, width, height};
		// the lines map is in the form <id> -> [ {x:x1,y:y1}, {x:x2,y:y2}, ... ]
		// the labels map is in the form <id> -> {x, y, width, height};

		if (this.transactionStarted) {
			//    this.jDiagram.@lv.lumii.layoutengine.Diagram::endTransaction()();
			this.transactionStarted = false;
		}

		this.jDiagram.arrange();
		return this.getLayout();
	}
	
	private long getStartBox(Line o) {
		for (Long i : this.boxesMap.keySet()) {
			Box b = this.boxesMap.get(i);
			if (o.getStart() == b)
				return i;
		}
		
		return -1;
	}
	
	private long getEndBox(Line o) {
		for (Long i : this.boxesMap.keySet()) {
			Box b = this.boxesMap.get(i);
			if (o.getEnd() == b)
				return i;
		}
		
		return -1;
		
	}

	public LayoutInfo getLayout() {
		// returns an objects with the "boxes", "lines", and "labels" maps
		// containing information about the layout;
		// the boxes map is in the form <id> -> {x, y, width, height};
		// the lines map is in the form <id> -> [ {x:x1,y:y1}, {x:x2,y:y2}, ... ]
		// the labels map is in the form <id> -> {x, y, width, height};
		
		
		
		boolean retraced=false;
		for (Long i : this.linesMap.keySet()) {
			Line o = this.linesMap.get(i);
			ArrayList<Point2D.Double> points = o.getPoints();
			if (points==null) {
				this.jDiagram.startTransaction();
				o.retrace();
				this.jDiagram.endTransaction();
				this.transactionStarted = false;
				points = o.getPoints();
				this.moveLine(i, getStartBox(o), getEndBox(o), points);
				retraced=true;
			}
			if (o.getType()==LineType.ORTHOGONAL) {
				try {
					validateOrthogonalPoints(points);
				}
				catch(Throwable t) {
					this.jDiagram.endTransaction();
					o.retrace();
					this.jDiagram.endTransaction();
					this.transactionStarted = false;
					points = o.getPoints();
//					this.moveLine(i, getStartBox(o), getEndBox(o), points);
					retraced=true;					
				}
			}
		}
		
		// moving some box - this will adjust line labels correctly
/*		for (Long i : this.boxesMap.keySet()) {
			Box o = this.boxesMap.get(i);		
			try {
				this.moveBox(i, o.getLeft(), o.getTop());
				this.jDiagram.endTransaction();
			}
			catch(Throwable t) {				
			}
			break;
		}*/

		if (retraced) {
/*			for (Long i : this.boxesMap.keySet()) {
				Box o = this.boxesMap.get(i);
				
				this.moveBox(i, o.getLeft(), o.getTop());
				
				break;
			}*/
			
			this.jDiagram.endTransaction();
			this.transactionStarted = false;
			
			// validating once again...
			for (Long i : this.linesMap.keySet()) {
				Line o = this.linesMap.get(i);
				ArrayList<Point2D.Double> points = o.getPoints();
				if (o.getType()==LineType.ORTHOGONAL) {
					validateOrthogonalPoints(points);
				}
			}
		}
		
		
		LayoutInfo retVal = new LayoutInfo();

		this.minX = 1000000000.1;
		this.minY = 1000000000.1;
		this.maxX = -1000000000.1;
		this.maxY = -1000000000.1;

		for (Long i : this.boxesMap.keySet()) {
			Box o = this.boxesMap.get(i);
			double x = myRound(o.getLeft());
			double y = myRound(o.getTop());
			if (x < this.minX)
				this.minX = x;
			if (y < this.minY)
				this.minY = y;
			if (x+myRound(o.getWidth()) > this.maxX)
				this.maxX = x + myRound(o.getWidth());
			if (y+myRound(o.getHeight()) > this.maxY)
				this.maxY = y + myRound(o.getHeight());

			Rectangle2D.Double coos = new Rectangle2D.Double(x, y, 
					myRound(o.getWidth()),
					myRound(o.getHeight()));
			retVal.boxes.put(i, coos);

		}
		

		for (Long i : this.linesMap.keySet()) {
			Line o = this.linesMap.get(i);
			ArrayList<Point2D.Double> points = o.getPoints();
			if (points==null) {
				o.retrace();
				points = o.getPoints();
			}
			int n = points.size();
			ArrayList<Point2D.Double> line = new ArrayList<Point2D.Double>();
			for (int j=0; j<n; j++) {
				Point2D.Double point = points.get(j);
				double x = point.getX();
				double y = point.getY();
				if (x < this.minX)
					this.minX = x;
				if (y < this.minY)
					this.minY = y;
				if (x > this.maxX)
					this.maxX = x;
				if (y > this.maxY)
					this.maxY = y;

				line.add( new Point2D.Double(x, y) );
			}

			ArrayList<OutsideLabel.LineLabel> jLabels = o.getLabels();
			n = jLabels.size();
			for (int j=0; j<n; j++) {
				OutsideLabel.LineLabel jLabel = jLabels.get(j);
				long labelId = this.lineLabelsInvMap.get(jLabel);
				double x = myRound(jLabel.getLeft());
				double y = myRound(jLabel.getTop());
				if (x < this.minX)
					this.minX = x;
				if (y < this.minY)
					this.minY = y;
				if (x+myRound(jLabel.getWidth()) > this.maxX)
					this.maxX = x + myRound(jLabel.getWidth());
				if (y+myRound(jLabel.getHeight()) > this.maxY)
					this.maxY = y + myRound(jLabel.getHeight());

				retVal.labels.put(labelId,
						new Rectangle2D.Double(x, y,
								myRound(jLabel.getWidth()),
								myRound(jLabel.getHeight())));
			}

			retVal.lines.put(i, line);
		}

		if (this.minX > 1000000000.0)
			this.minX = 0;
		if (this.minY > 1000000000.0)
			this.minY = 0;
		if (this.maxX < -1000000000.0)
			this.maxX = 0;
		if (this.maxY < -1000000000.0)
			this.maxY = 0;

		for (Long i : retVal.boxes.keySet()) {
			retVal.boxes.get(i).x = myRound(retVal.boxes.get(i).x-this.minX);
			if (this.inverseY)
				retVal.boxes.get(i).y = myRound(this.maxY-retVal.boxes.get(i).y-retVal.boxes.get(i).height);
			else
				retVal.boxes.get(i).y = myRound(retVal.boxes.get(i).y-this.minY);
			if (this.transposeXY) {
				double t = retVal.boxes.get(i).x; retVal.boxes.get(i).x = retVal.boxes.get(i).y; retVal.boxes.get(i).y = t;
				t = retVal.boxes.get(i).width; retVal.boxes.get(i).width = retVal.boxes.get(i).height; retVal.boxes.get(i).height = t;
			}
		}

		for (Long i : retVal.lines.keySet()) {
			ArrayList<Point2D.Double> line = retVal.lines.get(i);
			for (int j=0; j<line.size(); j++) {
				line.get(j).x = myRound(line.get(j).x-this.minX);
				if (this.inverseY)
					line.get(j).y = myRound(this.maxY-line.get(j).y);
				else
					line.get(j).y = myRound(line.get(j).y-this.minY);
				if (this.transposeXY) {
					double t = line.get(j).x; line.get(j).x = line.get(j).y; line.get(j).y = t;
				}

			}
		}

		for (Long i : retVal.labels.keySet()) {
			retVal.labels.get(i).x = myRound(retVal.labels.get(i).x-this.minX);
			if (this.inverseY)
				retVal.labels.get(i).y = myRound(this.maxY-retVal.labels.get(i).y-retVal.labels.get(i).height);
			else
				retVal.labels.get(i).y = myRound(retVal.labels.get(i).y-this.minY);
			if (this.transposeXY) {
				double t = retVal.labels.get(i).x; retVal.labels.get(i).x = retVal.labels.get(i).y; retVal.labels.get(i).y = t;
				t = retVal.labels.get(i).width; retVal.labels.get(i).width = retVal.labels.get(i).height; retVal.labels.get(i).height = t;
			}
		}

		if (this.transposeXY) {
			retVal.width = this.maxY-this.minY+1;
			retVal.height = this.maxX-this.minX+1;
		}
		else {
			retVal.width = this.maxX-this.minX+1;
			retVal.height = this.maxY-this.minY+1;
		}

		return retVal;

	};


}

