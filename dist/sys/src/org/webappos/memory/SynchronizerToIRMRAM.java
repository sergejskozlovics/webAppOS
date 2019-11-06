package org.webappos.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.webappos.bridge.GlobalSenderThread;

import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class SynchronizerToIRMRAM implements RAAPI_Synchronizer {
	
	private String project_id;
	private IRMRAM rmram;
	
	public SynchronizerToIRMRAM(String _project_id, IRMRAM _rmram) {
		project_id = _project_id;
		rmram = _rmram;
	}

		
		//bb.asDoubleBuffer().array()
	
	private long lastActivity = System.currentTimeMillis();
	private void updateLastActivity() {
		lastActivity = System.currentTimeMillis();		
	}
	
	private final static int MAX_BUF_A = 1024;
	private ByteBuffer bufA = ByteBuffer.allocate(MAX_BUF_A); // TODO: max repo size
	private StringBuffer bufS = null;
	

	private ByteBuffer getBuf1() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	private ByteBuffer getBuf2() {
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	private ByteBuffer getBuf3() {
		ByteBuffer bb = ByteBuffer.allocate(24);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	private ByteBuffer getBuf4() {
		ByteBuffer bb = ByteBuffer.allocate(32);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}

	private ByteBuffer getBuf5() {
		ByteBuffer bb = ByteBuffer.allocate(40);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}

	private ByteBuffer getBuf6() {
		ByteBuffer bb = ByteBuffer.allocate(48);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	@Override
	synchronized public void syncCreateClass(String name, long rClass) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0x01);
		buf2.putDouble(rClass);
		buf2.rewind();
		sendBufAndString(buf2, RAAPI_Synchronizer.sharpenString(name));
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteClass(long rClass) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF1);
		buf2.putDouble(rClass);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateGeneralization(long rSub, long rSuper) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x11);
		buf3.putDouble(rSub);
		buf3.putDouble(rSuper);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteGeneralization(long rSub, long rSuper) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xE1);
		buf3.putDouble(rSub);
		buf3.putDouble(rSuper);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateObject(long rClass, long rObject) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x02);
		buf3.putDouble(rClass);
		buf3.putDouble(rObject);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteObject(long rObject) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF2);
		buf2.putDouble(rObject);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncIncludeObjectInClass(long rObject, long rClass) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x12);
		buf3.putDouble(rObject);
		buf3.putDouble(rClass);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncExcludeObjectFromClass(long rObject, long rClass) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xE2);
		buf3.putDouble(rObject);
		buf3.putDouble(rClass);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncMoveObject(long rObject, long rClass) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x22);
		buf3.putDouble(rObject);
		buf3.putDouble(rClass);
		buf3.rewind();
		sendBufAndString(buf3, null);						
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateAttribute(long rClass, String name,
			long rPrimitiveType, long rAttr) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0x03);
		buf4.putDouble(rClass);
		buf4.putDouble(rPrimitiveType);
		buf4.putDouble(rAttr);
		buf4.rewind();
		sendBufAndString(buf4, RAAPI_Synchronizer.sharpenString(name));
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteAttribute(long rAttr) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF3);
		buf2.putDouble(rAttr);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncSetAttributeValue(long rObject, long rAttr, String value, String oldValue) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x04);
		buf3.putDouble(rObject);
		buf3.putDouble(rAttr);
		buf3.rewind();
		if (oldValue == null)
			sendBufAndString(buf3, RAAPI_Synchronizer.sharpenString(value));
		else
			sendBufAndString(buf3, RAAPI_Synchronizer.sharpenString(value)+"/"+RAAPI_Synchronizer.sharpenString(oldValue));
		updateLastActivity();
	}
	
	@Override
	synchronized public void syncDeleteAttributeValue(long rObject, long rAttr) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xF4);
		buf3.putDouble(rObject);
		buf3.putDouble(rAttr);
		buf3.rewind();
		sendBufAndString(buf3, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncValidateAttributeValue(long rObject, long rAttr, String value) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xA4);
		buf3.putDouble(rObject);
		buf3.putDouble(rAttr);
		buf3.rewind();
		sendBufAndString(buf3, RAAPI_Synchronizer.sharpenString(value));
		updateLastActivity();		
	}

	@Override
	synchronized public void syncCreateAssociation(long rSourceClass, long rTargetClass,
			String sourceRoleName, String targetRoleName,
			boolean isComposition, long rAssoc, long rInv) {
		ByteBuffer buf6 = getBuf6();
		buf6.putDouble(0x05);
		buf6.putDouble(rSourceClass);
		buf6.putDouble(rTargetClass);
		buf6.putDouble(isComposition?1:0);
		buf6.putDouble(rAssoc);
		buf6.putDouble(rInv);
		buf6.rewind();
		sendBufAndString(buf6, RAAPI_Synchronizer.sharpenString(sourceRoleName)+"/"+RAAPI_Synchronizer.sharpenString(targetRoleName));
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateDirectedAssociation(long rSourceClass,
			long rTargetClass, String targetRoleName,
			boolean isComposition, long rAssoc) {
		ByteBuffer buf5 = getBuf5();
		buf5.putDouble(0x15);
		buf5.putDouble(rSourceClass);
		buf5.putDouble(rTargetClass);
		buf5.putDouble(isComposition?1:0);
		buf5.putDouble(rAssoc);
		buf5.rewind();
		sendBufAndString(buf5, RAAPI_Synchronizer.sharpenString(targetRoleName));
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateAdvancedAssociation(String name, boolean nAry,
			boolean associationClass, long rAssoc) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0x25);
		buf4.putDouble(nAry?1:0);
		buf4.putDouble(associationClass?1:0);
		buf4.putDouble(rAssoc);
		buf4.rewind();
		sendBufAndString(buf4, RAAPI_Synchronizer.sharpenString(name));
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteAssociation(long rAssoc) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF5);
		buf2.putDouble(rAssoc);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateLink(long rSourceObject, long rTargetObject,
			long rAssociationEnd) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0x06);
		buf4.putDouble(rSourceObject);
		buf4.putDouble(rTargetObject);
		buf4.putDouble(rAssociationEnd);
		buf4.rewind();
		sendBufAndString(buf4, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateOrderedLink(long rSourceObject,
			long rTargetObject, long rAssociationEnd, long targetPosition) {
		ByteBuffer buf5 = getBuf5();
		buf5.putDouble(0x16);
		buf5.putDouble(rSourceObject);
		buf5.putDouble(rTargetObject);
		buf5.putDouble(rAssociationEnd);
		buf5.putDouble(targetPosition);
		buf5.rewind();
		sendBufAndString(buf5, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteLink(long rSourceObject, long rTargetObject,
			long rAssociationEnd) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0xF6);
		buf4.putDouble(rSourceObject);
		buf4.putDouble(rTargetObject);
		buf4.putDouble(rAssociationEnd);
		buf4.rewind();
		sendBufAndString(buf4, null);
		updateLastActivity();
	}
		
	@Override
	synchronized public void syncValidateLink(long rSourceObject, long rTargetObject,
			long rAssociationEnd) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0xA6);
		buf4.putDouble(rSourceObject);
		buf4.putDouble(rTargetObject);
		buf4.putDouble(rAssociationEnd);
		buf4.rewind();
		sendBufAndString(buf4, null);
		updateLastActivity();
	}
	
	@Override
	synchronized public void syncRawAction(double[] arr, String str) {
		
		ByteBuffer bb = ByteBuffer.allocate(arr.length*8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i=0; i<arr.length; i++)
			bb.putDouble(arr[i]);
		
		bb.rewind();
		sendBufAndString(bb, str);
		updateLastActivity();
	}
	
	
	@Override
	synchronized public void syncMaxReference(long r, int bitsCount, long bitsValues) {			
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0xFF);
		buf4.putDouble(r);
		buf4.putDouble(bitsCount);
		buf4.putDouble(bitsValues);
		buf4.rewind();
		sendBufAndString(buf4, null);			

		sendAndClearBuffersNow();
		
		updateLastActivity();
	}
	
	synchronized private void sendBufAndString(ByteBuffer bb/*optional*/, String s/*optional*/) {
//		if (initialSyncDone) {	
			
			checkBuffersOverflow(bb, s);
			
			if (bb!=null) {
				if (bufA.position()==0) {
					bufA.order(ByteOrder.LITTLE_ENDIAN);
					bufA.putDouble(0xEE);
					bufA.put(bb);
				}
				else {
					bufA.put(bb);
				}
			}
			if (s != null) {
				if (bufS==null) {
					bufS = new StringBuffer();
					bufS.append(s);
				}
				else {
					bufS.append("`");
					bufS.append(s);
				}
			}
			
			sendBuffersLater();			
/*		}
		else {
			checkBuffersOverflow(bb, s);
			
			if (bb!=null) {
				if (bufA.position()==0) {
					bufA.order(ByteOrder.LITTLE_ENDIAN);
					bufA.putDouble(0xEE);
					bufA.put(bb);
				}
				else {
					bufA.put(bb);
				}
			}
			if (s != null) {
				if (bufS==null) {
					bufS = new StringBuffer();
					bufS.append(s);
				}
				else {
					bufS.append("`");
					bufS.append(s);
				}
			}
		}*/
		updateLastActivity();
	}
	
	@Override
	synchronized public void sendString(String s) {
		sendBufAndString(null, s);
	}
	@Override
	public synchronized void syncBulk(double[] actions, String[] strings) {
		syncBulk(actions.length, actions, strings.length, strings);

	}
	
	@Override
	public synchronized void syncBulk(int nActions, double[] actions, int nStrings, String[] strings) {
		assert actions[0] == 0xEE;
		sendAndClearBuffersNow();
		
		StringBuffer sb = new StringBuffer();

		boolean first = true;
		for (int i=0; i<nStrings; i++) {
		  	  if (first)
				  first = false;
			  else
				  sb.append('`');
		  	  sb.append(strings[i]);
		}
		
		
		try {
			rmram.syncChanges_R(project_id, nActions, actions, sb.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void syncBulk(int nActions, double[] actions, String delimitedStrings) {
		assert actions[0] == 0xEE;
		sendAndClearBuffersNow();
		
		try {
			rmram.syncChanges_R(project_id, nActions, actions, delimitedStrings);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void flush() {
		sendAndClearBuffersNow();
		updateLastActivity();
	}
	
	private double[] bufAToArray(int len) {
		DoubleBuffer db = bufA.asDoubleBuffer();
		
		double arr[] = new double[len];
		
		for (int i=0; i<len; i++)
			arr[i] = db.get(i);
		
		return arr;		
	}
	
	private synchronized void sendAndClearBuffersNow() {
		String s = "";
		if (bufS!=null)
			s = bufS.toString();
		if ((bufA.position()==0) && (bufS==null))
			return; // nothing to send
		
		try {
			int pos = bufA.position();
			
			bufA.limit(pos);
			bufA.rewind();
			if (pos>0) {
				assert (bufA.getDouble()==0xEE); // bulk
				bufA.rewind();
				
				if (bufS!=null) {
					try {
						// rmram.syncChanges_R(project_id, pos/8, bufA.asDoubleBuffer().array(), s);						
						rmram.syncChanges_R(project_id, pos/8, bufAToArray(pos/8), s);
						
					} catch (RemoteException e) {
						e.printStackTrace();
					}					
				}
				else {
					try {
						//rmram.syncChanges_R(project_id, pos/8, bufA.asDoubleBuffer().array(), "");
						rmram.syncChanges_R(project_id, pos/8, bufAToArray(pos/8), "");
					} catch (RemoteException e) {
						e.printStackTrace();
					}					
				}
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		bufA = ByteBuffer.allocate(MAX_BUF_A);
		// bufA.limit(MAX_BUF_A);
		// bufA.rewind();
		
		bufS = null;		
		
	}

	synchronized private void checkBuffersOverflow(ByteBuffer bb, String s) {
		boolean overflow = false;
		if ((bb!=null) && (bufA.position()+bb.capacity() >= MAX_BUF_A))
			overflow = true;
		
		if ((s!=null) && (bufS!=null) && (bufS.length()+s.length()>MAX_BUF_A))
			overflow = true;
				
		if (overflow) {
			sendAndClearBuffersNow();
		}
	}

	private boolean scheduled = false;
	private synchronized void sendBuffersLater() {
		int delay = 10;
		Runnable r = new Runnable() {

			@Override
			public void run() {
				long curTime = System.currentTimeMillis();
				if (curTime-lastActivity<delay)
					scheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
				else {
					scheduled = false;
					flush();
				}
			}
			
		};
		if (!scheduled)
			scheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

}
