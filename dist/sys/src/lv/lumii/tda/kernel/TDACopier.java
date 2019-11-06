package lv.lumii.tda.kernel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import lv.lumii.tda.raapi.*;

public class TDACopier {
	
	private static boolean DEBUG = false;

	private RAAPI sourceModel, targetModel;
	private Set<Long> sourceReferencesToFree = new HashSet<Long>();
	private Set<Long> targetReferencesToFree = new HashSet<Long>();
	
	private Map<Long, Long> sourceToTargetMap = new HashMap<Long, Long>();
		// all found elements and the mappings to their copies
	
	private List<String> errorMessages = null;

	public static void readln()
	{
		if (!DEBUG)
			return;
		System.out.println("Press Enter...");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	private TDACopier(RAAPI _sourceModel, RAAPI _targetModel, List<String> _errorMessages) {
		sourceModel = _sourceModel;
		targetModel = _targetModel;
		errorMessages = _errorMessages;
	}
	
	private void putErrorMessage(String errorMessage)
	{
		System.err.println(errorMessage);
		if (errorMessages != null)
			errorMessages.add(errorMessage);
	}

	private void freeReferences()
	{
		for (Long r : sourceReferencesToFree)
			sourceModel.freeReference(r);
		sourceReferencesToFree.clear();
		
		for (Long r : targetReferencesToFree)
			targetModel.freeReference(r);
		targetReferencesToFree.clear();
	}
	
	private boolean findOrCreateClass(long sClass)
	{
		assert(!sourceToTargetMap.containsKey(sClass));
		String name = sourceModel.getClassName(sClass);
		long tClass = targetModel.findClass(name);
		if (tClass == 0)
			tClass = targetModel.createClass(name);
		if (tClass != 0) {
			targetReferencesToFree.add(tClass);
			sourceToTargetMap.put(sClass, tClass);
			return true;
		}
		else
			return false;
	}
	private boolean findOrCreateAssociation(long sFromCls, long sToCls, long sAssoc, long sInvAssoc)
	{		
		System.out.println(" create assoc "+sFromCls+"."+sAssoc);
		boolean bidi = (sInvAssoc!=0);
		assert(!sourceToTargetMap.containsKey(sAssoc));
		assert((sInvAssoc==0) || !sourceToTargetMap.containsKey(sInvAssoc));
		
		// checking, whether the association already exists...
		long tAssoc = targetModel.findAssociationEnd(sourceToTargetMap.get(sFromCls), sourceModel.getRoleName(sAssoc));
		long tInvAssoc = 0;
		if (bidi)
			tInvAssoc = targetModel.findAssociationEnd(sourceToTargetMap.get(sToCls), sourceModel.getRoleName(sInvAssoc));
		if (bidi)
			// checking whether both roles exist or do not exist...
			if ((tAssoc == 0) != (tInvAssoc == 0)) {
			
				tAssoc = targetModel.findAssociationEnd(sourceToTargetMap.get(sFromCls), sourceModel.getRoleName(sAssoc));
				String role1 = targetModel.getClassName(sourceToTargetMap.get(sFromCls)) + "." +sourceModel.getRoleName(sAssoc);
				String role2 = targetModel.getClassName(sourceToTargetMap.get(sToCls)) + "." +sourceModel.getRoleName(sInvAssoc);
				if (tAssoc == 0) {
					String tmp = role1;
					role1 = role2;
					role2 = tmp;
				}
				
				// one target role already exists, while the other does not
				putErrorMessage("TDACopier could not find or create an association in the target repository: one target role ("+role1+") already exists, while the other ("+role2+") does not.");
				if (tAssoc != 0)
					targetModel.freeReference(tAssoc);
				if (tInvAssoc != 0)
					targetModel.freeReference(tInvAssoc);
				sourceModel.freeReference(sInvAssoc);
				return false;																		
			}																
					
		if (tAssoc != 0) {
			// already exists;
			if  (bidi) {
				//checking whether the inversion is correct...
				//if (DEBUG) System.out.println("Association already exists; checking whether the inversion is correct...");
				long tgtInvAssoc2 = targetModel.getInverseAssociationEnd(tAssoc);
				
				if (tgtInvAssoc2 != 0)
					targetModel.freeReference(tgtInvAssoc2);
				
				if (tgtInvAssoc2 != tInvAssoc) {							
					
					targetModel.freeReference(tAssoc);
					targetModel.freeReference(tInvAssoc);
					return false;
				}
			}
		} else {																															
			// the association does not exist; creating...			
			if (bidi) {
				// trying to keep the composition flag...
				if (sourceModel.isComposition(sInvAssoc)) {
					System.err.println("create inv compos "+sourceModel.getClassName(sToCls)+sToCls+"."+sourceModel.getRoleName(sAssoc)+"->"+sourceModel.getClassName(sFromCls)+sFromCls+"."+sourceModel.getRoleName(sInvAssoc));
					tAssoc = targetModel.createAssociation(							
							sourceToTargetMap.get(sToCls),
							sourceToTargetMap.get(sFromCls),
							sourceModel.getRoleName(sAssoc),
							sourceModel.getRoleName(sInvAssoc),
							true);
					tAssoc = targetModel.getInverseAssociationEnd(tAssoc);
				}
				else {
					System.err.println("create assoc "+sourceModel.getClassName(sFromCls)+sFromCls+"|"+sourceToTargetMap.get(sFromCls)+"."+sourceModel.getRoleName(sInvAssoc)+"->"+sToCls+"|"+sourceToTargetMap.get(sToCls)+"."+sourceModel.getRoleName(sAssoc));
					tAssoc = targetModel.createAssociation(
						sourceToTargetMap.get(sFromCls),
						sourceToTargetMap.get(sToCls),
						sourceModel.getRoleName(sInvAssoc),
						sourceModel.getRoleName(sAssoc),
						sourceModel.isComposition(sAssoc));
				}
			}
			else {						
				System.err.println("create directed assoc "+sFromCls+"->"+sToCls+"."+sourceModel.getRoleName(sAssoc));
				tAssoc = targetModel.createDirectedAssociation(
						sourceToTargetMap.get(sFromCls),
						sourceToTargetMap.get(sToCls),
						sourceModel.getRoleName(sAssoc),
						sourceModel.isComposition(sAssoc));
			}
			if (tAssoc == 0) {
				if (bidi)
					sourceModel.freeReference(sInvAssoc);	
				return false;																		
			}
			if (bidi) {
				tInvAssoc = targetModel.getInverseAssociationEnd(tAssoc);
				if (tInvAssoc == 0) {
					targetModel.freeReference(tAssoc);
					return false;																		
				}
			}
		}
		
		assert(tAssoc != 0);
		assert((!bidi) || (tInvAssoc != 0));
		
		targetReferencesToFree.add(tAssoc);
		if (bidi)
			targetReferencesToFree.add(tInvAssoc);
		
		sourceToTargetMap.put(sAssoc, tAssoc);
		if (bidi)
			sourceToTargetMap.put(sInvAssoc, tInvAssoc);
		return true;
	}
	
	

	private boolean findOrCreateAttribute(long sCls, long sAttr)
	{
		
		//assert(!sourceToTargetMap.containsKey(sAttr));			
		
		long tAttr = targetModel.findAttribute(sourceToTargetMap.get(sCls), sourceModel.getAttributeName(sAttr));
		if (tAttr != 0) {
			// checking the type of the existing attribute...
			
			String name1 = sourceModel.getPrimitiveDataTypeName(sourceModel.getAttributeType(sAttr));
			String name2 = targetModel.getPrimitiveDataTypeName(targetModel.getAttributeType(tAttr));
			
			if (name1 == null) {
				putErrorMessage(
						"Could not get the type name for attribute "+sourceModel.getAttributeName(sAttr)+
						" of class "+sourceModel.getClassName(sCls)+".");
				targetModel.freeReference(tAttr);
				return false;											
			}
			else			
			if (!name1.equals(name2)) {
				putErrorMessage(
						"Target attribute "+sourceModel.getAttributeName(sAttr)+
						" of class "+sourceModel.getClassName(sCls)+
						" found, but it has the type "+name2+
						" instead of "+name1+".");
				targetModel.freeReference(tAttr);
				return false;							
			}
			// attribute exists
		}
		else {
			// attribute does not exist; creating...
			String typeName = null;
			long rSrcType = sourceModel.getAttributeType(sAttr);
			if (rSrcType != 0) {
				typeName = sourceModel.getPrimitiveDataTypeName(rSrcType);
				sourceModel.freeReference(rSrcType);
			}
			System.err.println("create attr "+sourceModel.getClassName(sCls)+"."+sourceModel.getAttributeName(sAttr)+":"+typeName);
			if (typeName != null) {
				long rTgtType = targetModel.findPrimitiveDataType(typeName);
				if (rTgtType != 0) {
					tAttr = targetModel.createAttribute(sourceToTargetMap.get(sCls), sourceModel.getAttributeName(sAttr), rTgtType);
					targetModel.freeReference(rTgtType);
				}
				else
					tAttr = 0;
			}
			else
				tAttr = 0;
			if (tAttr == 0) {
				putErrorMessage(
						"Could not create attribute "+sourceModel.getAttributeName(sAttr)+
						" of class "+sourceModel.getClassName(sCls)+" of type "+typeName+" (type="+rSrcType+").");
				return false;
			}
			
			// attribute created
		}
		
		// attribute exists or created; all ok
		
		targetReferencesToFree.add(tAttr);
		//sourceToTargetMap.put(sAttr, tAttr);
		return true;
	}
	
	private final class StructForWaitingAssociation
	{
		long sFromCls;
		long sAssoc;
		long sInvAssoc;
		StructForWaitingAssociation(long _sFromCls, long _sAssoc, long _sInvAssoc) {
			sFromCls = _sFromCls;
			sAssoc = _sAssoc;
			sInvAssoc = _sInvAssoc;
		}
	}
	
	private Map<Long, List<StructForWaitingAssociation> > waitingAssociations = new HashMap<Long, List<StructForWaitingAssociation> >();
	
	private final class StructForMetamodelElement
	{
		static final byte CLASS = 1;
		static final byte ASSOCIATION = 2;
		static final byte ATTRIBUTE = 3;
		byte kind;
		long reference;
		StructForMetamodelElement(byte _kind, long _reference) {
			kind = _kind;
			reference = _reference;
		}
	}	
	
	private boolean checkAssociationsWaitingFor(long sToCls, boolean stopOnFirstError) {
		List<StructForWaitingAssociation> waiting = waitingAssociations.remove(sToCls);
		if (waiting == null)
			return true; // no association is waiting, OK!
		
		boolean result = true;
		
		for (StructForWaitingAssociation w : waiting) {
			if (!findOrCreateAssociation(w.sFromCls, sToCls, w.sAssoc, w.sInvAssoc)) {
				putErrorMessage("Could not find/create a delayed association "+sourceModel.getRoleName(w.sAssoc)+" from class "+sourceModel.getClassName(w.sFromCls)+".");
				if (stopOnFirstError)
					return false;
				else
					result = false;
			}
		}
		
		return result;
	}
	
	
	private boolean classNameMatches(String className, Pattern toMatch[], Pattern notToMatch[])
	{
		boolean matches = false;
		
		if (className != null) {
			matches = (toMatch == null);
			if (toMatch != null)
				for (int i=0; i<toMatch.length; i++) {
					if (toMatch[i].matcher(className).matches()) {
						matches = true;
						break;
					}
				}
			
			if ((matches) && (notToMatch!=null)) {
				for (int i=0; i<notToMatch.length; i++) {
					if (notToMatch[i].matcher(className).matches()) {
						matches = false;
						break;
					}
				}				
			}
		}
		
		return matches;
		
	}
	
	/**
	 * @param stopOnFirstError whether to stop on the first error, or continue copying
	 * @param regExToMatch an array of regular expressions (which will be passed to java.util.regex.Pattern) for the class names to match;
	 *        it is sufficient to match at least one expression to be included
	 * @param regExNotToMatch an array of regular expressions not to match;
	 *        it is sufficient to match at least one expression for the class name to be excluded (event it matched one of the regExToMatch)
	 * @return whether there were no errors
	 */
	private boolean internalMakeCopy(boolean stopOnFirstError, String[] regExToMatch, String[] regExNotToMatch,
			boolean copyInstances)
	{
		Pattern toMatch[] = null;
		if (regExToMatch != null) {
			toMatch = new Pattern[regExToMatch.length];
			for (int i=0; i<regExToMatch.length; i++)
				toMatch[i] = Pattern.compile(regExToMatch[i]);
		}
		Pattern notToMatch[] = null;
		if (regExNotToMatch != null) {
			notToMatch = new Pattern[regExNotToMatch.length];
			for (int i=0; i<regExNotToMatch.length; i++)
				notToMatch[i] = Pattern.compile(regExNotToMatch[i]);
		}
		
		List<StructForMetamodelElement> foundMetamodelElements = new LinkedList<StructForMetamodelElement>();
		
		// INITIALLY, COPYING CLASSES...
		long itClasses = sourceModel.getIteratorForClasses();
		if (itClasses == 0)
			return false;		
		long sClass = sourceModel.resolveIteratorFirst(itClasses);		
		while (sClass != 0) {
			
			String className = sourceModel.getClassName(sClass);
									
			if (!classNameMatches(className, toMatch, notToMatch)) {
				if (DEBUG) System.out.println("Class "+className+" does not conform to the given regular expressions to match/not to match.");
				sourceModel.freeReference(sClass);
				sClass = sourceModel.resolveIteratorNext(itClasses);
				continue;
			}
			else
				if (DEBUG) System.out.println("Class "+className+" matches.");
			
			sourceReferencesToFree.add(sClass);
			if (!findOrCreateClass(sClass)) {
				putErrorMessage("Could not find or create target class "+sourceModel.getClassName(sClass));
				if (stopOnFirstError) {
					freeReferences();
					sourceModel.freeIterator(itClasses);
					return false;
				}
			} else {				
				foundMetamodelElements.add(new StructForMetamodelElement(StructForMetamodelElement.CLASS, sClass));
			}
			
			sClass = sourceModel.resolveIteratorNext(itClasses);
		}
		sourceModel.freeIterator(itClasses);
		
		// COPYING ASSOCIATIONS/ATTRIBUTES BETWEEN CLASSES,
		// THEN ASSOCIATIONS/ATTRIBUTES BETWEEN CLASSES AND ASSOCIATIONS/ATTRIBUTES FOUND SO FAR,
		// THEN ASSOCIATIONS/ATTRIBUTES BETWEEN CLASSES AND ASSOCIATIONS/ATTRIBUTES FOUND SO FAR,
		// ETC.
		// If some association cannot be create because some of its end points has not
		// been created yet, that association is being placed into waitingAssociations map.
		Collection<Long> currentElements = new LinkedList<Long>();
		for (StructForMetamodelElement elmnt : foundMetamodelElements)
			currentElements.add(elmnt.reference);
		while (!currentElements.isEmpty()) {
			// since associations and attributes may also be classes, we put them into a
			// nextMMelements collection; they will be processed in the next while-iteration
			Collection<Long> nextElements = new LinkedList<Long>(); 
				
			for (Long elmnt : currentElements) {
				// copying associations and attributes of the given elmnt
				
				// associations...
				long sAssocIt = sourceModel.getIteratorForDirectOutgoingAssociationEnds(elmnt);
				if (sAssocIt != 0) {					
					long sAssoc = sourceModel.resolveIteratorFirst(sAssocIt);
					while (sAssoc != 0) {																		
						
						// checking, whether srcAssoc has already been processed (e.g., when its inverse had been processed)
						if (sourceToTargetMap.containsKey(sAssoc))
							sourceModel.freeReference(sAssoc);
						else {
							// checking, whether the target class of the association matches the patterns...
							long rTargetClass = sourceModel.getTargetClass(sAssoc);
							String targetName = sourceModel.getClassName(rTargetClass);
							sourceModel.freeReference(rTargetClass);
							if (!classNameMatches(targetName, toMatch, notToMatch)) {
								sourceModel.freeReference(sAssoc);
								sAssoc = sourceModel.resolveIteratorNext(sAssocIt);
								continue;
							}
							
							// processing...
							sourceReferencesToFree.add(sAssoc);
							long sInvAssoc = sourceModel.getInverseAssociationEnd(sAssoc);
							
							boolean bidi = (sInvAssoc != 0);
							
/*							if (sInvAssoc == 0) {
								// TODO: currently, we can copy only bi-directional associations...
								putErrorMessage("Association "+sourceModel.getRoleName(sAssoc)+" from class "+sourceModel.getClassName(elmnt)+" is not bidirectional.");
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeIterator(sAssocIt);
									return false;
								}
								else {
									sourceReferencesToFree.remove(sAssoc);
									sourceModel.freeReference(sAssoc);
									sAssoc = sourceModel.resolveIteratorNext(sAssocIt);
									continue;
								}
							}*/
							if (bidi)
								sourceReferencesToFree.add(sInvAssoc);
							
							if (bidi && sourceToTargetMap.containsKey(sInvAssoc)) {
								// sAssoc is not in the map, but sInvAssoc is --- something is wrong here... 
								putErrorMessage("Association "+sourceModel.getRoleName(sAssoc)+" from class "+sourceModel.getClassName(elmnt)+" has been processed only in one direction.");
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeIterator(sAssocIt);
									return false;
								}
								else {
									sourceReferencesToFree.remove(sAssoc);
									sourceModel.freeReference(sAssoc);
									sourceReferencesToFree.remove(sInvAssoc);
									sourceModel.freeReference(sInvAssoc);
									sAssoc = sourceModel.resolveIteratorNext(sAssocIt);
									continue;									
								}
							}
							
							long sFromCls = sourceModel.getSourceClass(sAssoc);
							if (sFromCls != 0)
								sourceModel.freeReference(sFromCls);
							if (sFromCls != elmnt) {
								putErrorMessage("Association "+sourceModel.getRoleName(sAssoc)+" from class "+sourceModel.getClassName(elmnt)+" has been processed only in one direction."+elmnt+" "+sFromCls);
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeIterator(sAssocIt);
									return false;
								}
								else {									
									sourceReferencesToFree.remove(sAssoc);
									sourceModel.freeReference(sAssoc);
									sourceReferencesToFree.remove(sInvAssoc);
									sourceModel.freeReference(sInvAssoc);
									sAssoc = sourceModel.resolveIteratorNext(sAssocIt);
									continue;									
								}
							}
								
							long sToCls = sourceModel.getTargetClass(sAssoc);
							if (sToCls == 0) {
								putErrorMessage("Could not get the target class of association "+sourceModel.getRoleName(sAssoc)+" from class "+sourceModel.getClassName(elmnt)+".");
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeIterator(sAssocIt);
									return false;
								}
								else {
									sourceReferencesToFree.remove(sAssoc);
									sourceModel.freeReference(sAssoc);
									sourceReferencesToFree.remove(sInvAssoc);
									sourceModel.freeReference(sInvAssoc);
									sAssoc = sourceModel.resolveIteratorNext(sAssocIt);
									continue;																		
								}
							}
							
							assert(!sourceToTargetMap.containsKey(sAssoc));
							assert(bidi || !sourceToTargetMap.containsKey(sInvAssoc));
							assert(elmnt.equals(sFromCls));
							assert(sourceToTargetMap.containsKey(sFromCls));
							
							if (sourceToTargetMap.containsKey(sToCls))
							{ // both fromCls and toCls have already been copied;
							  // now we can copy the association...
								sourceModel.freeReference(sToCls); // we already are keeping this reference
								if (!findOrCreateAssociation(sFromCls, sToCls, sAssoc, sInvAssoc)) {
									// findOrCreateAssociation(sFromCls, sToCls, sAssoc, sInvAssoc);
									try {
										throw new RuntimeException("aaa");
									}
									catch(Throwable t) {
										t.printStackTrace();
									}
									putErrorMessage("Could not find/create association "+sourceModel.getRoleName(sAssoc)+" from class "+sourceModel.getClassName(elmnt)+".");
									if (stopOnFirstError) {
										freeReferences();
										sourceModel.freeIterator(sAssocIt);
										return false;
									}
									else {
										sourceReferencesToFree.remove(sAssoc);
										sourceModel.freeReference(sAssoc);
										sourceReferencesToFree.remove(sInvAssoc);
										sourceModel.freeReference(sInvAssoc);
										sAssoc = sourceModel.resolveIteratorNext(sAssocIt);
										continue;																												
									}
								}
								
								// now the association must exist...
								
								foundMetamodelElements.add(new StructForMetamodelElement(StructForMetamodelElement.ASSOCIATION, sAssoc));
								if (bidi)
									foundMetamodelElements.add(new StructForMetamodelElement(StructForMetamodelElement.ASSOCIATION, sInvAssoc));
								
								nextElements.add(sAssoc);
								if (bidi)
									nextElements.add(sInvAssoc);
																							
								if ((!checkAssociationsWaitingFor(sAssoc, stopOnFirstError)) || (bidi&&!checkAssociationsWaitingFor(sInvAssoc, stopOnFirstError))) {
									if (stopOnFirstError) {
										freeReferences();
										sourceModel.freeIterator(sAssocIt);
										return false;
									}
								}
							}
							else {
								sourceReferencesToFree.add(sToCls);
								
								// adding to the waiting associations...
								if (!waitingAssociations.containsKey(sToCls))
									waitingAssociations.put(sToCls, new LinkedList<StructForWaitingAssociation>());
								
								waitingAssociations.get(sToCls).add(new StructForWaitingAssociation(sFromCls, sAssoc, sInvAssoc));
							}
													
						}
						
						sAssoc = sourceModel.resolveIteratorNext(sAssocIt); 
					}
					sourceModel.freeIterator(sAssocIt);
				}

				// attributes...
				long sAttrIt = sourceModel.getIteratorForDirectAttributes(elmnt);
				if (sAttrIt != 0) {				
					long sAttr = sourceModel.resolveIteratorFirst(sAttrIt);
					while (sAttr != 0) {
						if (DEBUG) System.out.println(" attr "+sourceModel.getClassName(elmnt)+"."+sourceModel.getAttributeName(sAttr));
						
/*						if (sourceToTargetMap.containsKey(sAttr)) { // already processed, but this should not be the case
							System.out.println(" attr exists!");
							readln();
							sourceModel.freeReference(sAttr);
						}
						else*/ {
							if (!findOrCreateAttribute(elmnt, sAttr)) {
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeIterator(sAttrIt);
									return false;
								}
								else {
									sourceReferencesToFree.remove(sAttr);
									sourceModel.freeReference(sAttr);									
								}
							}
							else {
								// attribute found/created
								foundMetamodelElements.add(new StructForMetamodelElement(StructForMetamodelElement.ATTRIBUTE, sAttr));
								nextElements.add(sAttr);
								
								if (!checkAssociationsWaitingFor(sAttr, stopOnFirstError)) {
									if (stopOnFirstError) {
										freeReferences();
										sourceModel.freeIterator(sAttrIt);
										return false;
									}
									else {
										sourceReferencesToFree.remove(sAttr);
										sourceModel.freeReference(sAttr);
									}
								}
							}
						}
						
						sAttr = sourceModel.resolveIteratorNext(sAttrIt);
					}
				}
				sourceModel.freeIterator(sAttrIt);				
			}
			
			currentElements = nextElements;
		}
		
		/// COPYING GENERALIZATIONS (BETWEEN CLASSES/ATTRIBUTES/ASSOCIATIONS)...
		for (StructForMetamodelElement elmnt : foundMetamodelElements) {
			long sSuperClsIt = sourceModel.getIteratorForDirectSuperClasses(elmnt.reference);
			if (sSuperClsIt != 0) {
				long sSuperCls = sourceModel.resolveIteratorFirst(sSuperClsIt);
				while (sSuperCls != 0) {
					
					// checking, whether the superclass matches the patterns...
					if (!classNameMatches(sourceModel.getClassName(sSuperCls), toMatch, notToMatch)) {
						sourceModel.freeReference(sSuperCls);
						sSuperCls = sourceModel.resolveIteratorNext(sSuperClsIt);
						continue;
					}
					
					
					long tCls = sourceToTargetMap.get(elmnt.reference);
					Long tSuperCls = sourceToTargetMap.get(sSuperCls);
					if (tSuperCls == null) { // the super class must have been already created...
						putErrorMessage("Target super class "+sourceModel.getClassName(sSuperCls)+" was not found.");
						if (stopOnFirstError) {
							freeReferences();
							sourceModel.freeReference(sSuperCls);
							sourceModel.freeIterator(sSuperClsIt);
							return false;
						}
					}
					else {
						// tSuperCls ok...
						if (!targetModel.isDirectSubClass(tCls, tSuperCls)) {					
							if (!targetModel.createGeneralization(tCls, tSuperCls)) {
								putErrorMessage("Could not create the generalization from "+targetModel.getClassName(tCls)+" to "+targetModel.getClassName(tSuperCls)+".");
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeReference(sSuperCls);
									sourceModel.freeIterator(sSuperClsIt);
									return false; // could not create the generalization
								}
							}
						}
					}
					
					
					sourceModel.freeReference(sSuperCls);
					sSuperCls = sourceModel.resolveIteratorNext(sSuperClsIt);
				}				
				sourceModel.freeIterator(sSuperClsIt);
			}
		}
		
		if (!copyInstances) {
			freeReferences();
			return true;
		}
		
		// COPYING OBJECTS AND LINKS/ATTRIBUTE VALUES
		for (StructForMetamodelElement elmnt : foundMetamodelElements) {
			if (elmnt.kind == StructForMetamodelElement.CLASS) {
				long sCls = elmnt.reference;
				assert(sourceToTargetMap.containsKey(sCls));
				long tCls = sourceToTargetMap.get(sCls);				
				
				long sObjIt = sourceModel.getIteratorForDirectClassObjects(sCls);
				long sObj = sourceModel.resolveIteratorFirst(sObjIt);
				while (sObj != 0) {
					//if (DEBUG) System.out.println("obj "+sObj+" of type "+sourceModel.getClassName(sCls));
					if (sourceToTargetMap.containsKey(sObj)) {
						sourceModel.freeReference(sObj);
						
						long tObj = sourceToTargetMap.get(sObj); 
						if (!targetModel.isTypeOf(tObj, tCls)) {
							if (!targetModel.includeObjectInClass(tObj, tCls)) {
								putErrorMessage("Could not include object into class "+targetModel.getClassName(tCls));
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeIterator(sObjIt);
									return false;
								}
							}
						}
					}
					else {
						long tObj = targetModel.createObject(tCls);
						if (tObj == 0) {
							sourceModel.freeReference(sObj);
							putErrorMessage("!!! "+sourceModel.getClassName(sCls)+" "+sCls+"|"+tCls);
							putErrorMessage("Could not create an object of class "+targetModel.getClassName(tCls));
							if (stopOnFirstError) {
								freeReferences();
								sourceModel.freeIterator(sObjIt);
								return false;
							}
						}
						else {
							sourceToTargetMap.put(sObj, tObj);
						}
					}
					
					sObj = sourceModel.resolveIteratorNext(sObjIt);
				}
				sourceModel.freeIterator(sObjIt);
			}
			if (elmnt.kind == StructForMetamodelElement.ATTRIBUTE) {
				//long sCls = sourceModel.getAttributeDomain(elmnt.reference);
				// mii_rep patch:
				// for all classes having this attribute...
				long clsit = sourceModel.getIteratorForClasses();
				for (long sCls = sourceModel.resolveIteratorFirst(clsit); sCls!=0; sCls = sourceModel.resolveIteratorNext(clsit)) {
					
					if (!classNameMatches(sourceModel.getClassName(sCls), toMatch, notToMatch))
						continue;
					
					long attrref = sourceModel.findAttribute(sCls, sourceModel.getAttributeName(elmnt.reference));
					
					
					if (attrref!=0) {
						sourceModel.freeReference(attrref);
						
						long sObjIt = sourceModel.getIteratorForAllClassObjects(sCls);
						if (sObjIt != 0) {
							long sObj = sourceModel.resolveIteratorFirst(sObjIt);
							while (sObj != 0) {
								
								if (!sourceToTargetMap.containsKey(sObj)) {						
									putErrorMessage("Could not find the corresponding target object for a source object of class "+sourceModel.getClassName(sCls));
									if (stopOnFirstError) {
										freeReferences();
										sourceModel.freeReference(sObj);
										sourceModel.freeReference(sCls);
										sourceModel.freeIterator(sObjIt);
										return false;
									}
								}
								else {
									long tObj = sourceToTargetMap.get(sObj);
									//assert(sourceToTargetMap.containsKey(elmnt.reference));
									//long tAttr = sourceToTargetMap.get(elmnt.reference);
									// mii_rep patch:
									long tAttr = targetModel.findAttribute(sourceToTargetMap.get(sCls), sourceModel.getAttributeName(elmnt.reference));
									
									String value = sourceModel.getAttributeValue(sObj, elmnt.reference);
									if (sourceModel.getAttributeType(elmnt.reference)==sourceModel.findPrimitiveDataType("Integer")) {
										try {
											Integer.parseInt(value);
										}
										catch(Throwable t) {
											value = null;
										}
									}
									if (sourceModel.getAttributeType(elmnt.reference)==sourceModel.findPrimitiveDataType("Real")) {
										try {
											Double.parseDouble(value);
										}
										catch(Throwable t) {
											value = null;
										}
									}
									if (sourceModel.getAttributeType(elmnt.reference)==sourceModel.findPrimitiveDataType("Boolean")) {
										if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
											value = null;
										}
									}
									if (value != null) {
										int z = value.indexOf('\0');
										if (z>=0)
											value=value.substring(0, z);
										if (!targetModel.setAttributeValue(tObj, tAttr, value)) {											
											putErrorMessage("Could not set the attribute "+targetModel.getAttributeName(tAttr)+" value for an object of class "+sourceModel.getClassName(sCls));
											if (stopOnFirstError) {
												freeReferences();
												sourceModel.freeReference(sObj);
												sourceModel.freeReference(sCls);
												sourceModel.freeIterator(sObjIt);
												return false;
											}									
										}
									}
								}
								
								sourceModel.freeReference(sObj);
								sObj = sourceModel.resolveIteratorNext(sObjIt);
							}
							sourceModel.freeIterator(sObjIt);
						}
						sourceModel.freeReference(sCls);
					}
				}
				sourceModel.freeIterator(clsit);
			}
			
			if (elmnt.kind == StructForMetamodelElement.ASSOCIATION) {				
				long sCls = sourceModel.getSourceClass(elmnt.reference);
				if (sCls != 0) {
					long sObjIt = sourceModel.getIteratorForAllClassObjects(sCls);
					if (sObjIt != 0) {
						long sObj = sourceModel.resolveIteratorFirst(sObjIt);
						while (sObj != 0) {
							//if (DEBUG) System.out.println("obj "+sObj+" of source class "+sourceModel.getClassName(sCls)+" for assoc "+sourceModel.getRoleName(elmnt.reference));
							
							
							
							if (!sourceToTargetMap.containsKey(sObj)) {
								
								putErrorMessage("Could not find the corresponding target object for a source object of class "+sourceModel.getClassName(sCls));
								if (stopOnFirstError) {
									freeReferences();
									sourceModel.freeReference(sObj);
									sourceModel.freeReference(sCls);
									sourceModel.freeIterator(sObjIt);
									return false;
								}
							}
							else {
								long tObj = sourceToTargetMap.get(sObj);
								assert(sourceToTargetMap.containsKey(elmnt.reference));
								long tAssoc = sourceToTargetMap.get(elmnt.reference);
								
								long sLinkedObjIt = sourceModel.getIteratorForLinkedObjects(sObj, elmnt.reference);
								if (sLinkedObjIt != 0) {
									long sLinkedObj = sourceModel.resolveIteratorFirst(sLinkedObjIt);
									while (sLinkedObj != 0) {
										
										Long ttLinkedObj = sourceToTargetMap.get(sLinkedObj);
										if (ttLinkedObj != null) {
										
											long tLinkedObj = ttLinkedObj; //sourceToTargetMap.get(sLinkedObj);
											
											if (targetModel.linkExists(tObj, tLinkedObj, tAssoc)) {
												//if (DEBUG) System.out.println("No error: a link corresponding to the "+targetModel.getRoleName(tAssoc)+" association from class "+sourceModel.getClassName(sCls)+" already exists.");
											}
											else										
											if (!targetModel.createLink(tObj, tLinkedObj, tAssoc)) {
												
												targetModel.createLink(tObj, tLinkedObj, tAssoc);
												long rTargetClass = sourceModel.getTargetClass(elmnt.reference);
												putErrorMessage("Could not create a link corresponding to the "+targetModel.getRoleName(tAssoc)+" association from class "+sourceModel.getClassName(sCls)
														+"; target class in the source model = "+sourceModel.getClassName(rTargetClass));
												sourceModel.freeReference(rTargetClass);
												
												if (stopOnFirstError) {
													freeReferences();
													sourceModel.freeReference(sLinkedObj);
													sourceModel.freeIterator(sLinkedObjIt);
													sourceModel.freeReference(sObj);
													sourceModel.freeReference(sCls);
													sourceModel.freeIterator(sObjIt);
													return false;
												}									
											}
										}
										
										sourceModel.freeReference(sLinkedObj);
										sLinkedObj = sourceModel.resolveIteratorNext(sLinkedObjIt);
									}
									sourceModel.freeIterator(sLinkedObjIt);
								}
																
							}
							
							sourceModel.freeReference(sObj);
							sObj = sourceModel.resolveIteratorNext(sObjIt);
						}
						sourceModel.freeIterator(sObjIt);
					}
					sourceModel.freeReference(sCls);
				}
			}
		}
		
		freeReferences();
		return true;
		
	}
	
	// TODO: rollback, if something gone wrong;
	// TODO: return information about errors (what class/link/... could not be created)
	// TODO: forgettable copy (with error list)
	public static boolean makeCopy(RAAPI sourceModel, RAAPI targetModel, List<String> errorMessages, boolean stopOnFirstError)
	// the source and the target kernels must be open;
	// metamodel is also copied; however, if some class exists in the target kernel,
	// it will not be created once again
	{
		TDACopier copier = new TDACopier(sourceModel, targetModel, errorMessages); 
		return copier.internalMakeCopy(stopOnFirstError, null, null, true);
	}

	public static boolean makeCopy(RAAPI sourceModel, RAAPI targetModel, List<String> errorMessages, boolean stopOnFirstError, String regExToMatch[], String regExNotToMatch[], boolean copyInstances)
	// the source and the target kernels must be open;
	// metamodel is also copied; however, if some class exists in the target kernel,
	// it will not be created once again
	{
		TDACopier copier = new TDACopier(sourceModel, targetModel, errorMessages); 
		return copier.internalMakeCopy(stopOnFirstError, regExToMatch, regExNotToMatch, copyInstances);
	}
	
	public static boolean makeCopy(RAAPI sourceModel, RAAPI targetModel, boolean stopOnFirstError)
	{
		return makeCopy(sourceModel, targetModel, null, stopOnFirstError);
	}
	
	public static long copyObject(RAAPI raapi, long rObject)
	{
		long itCls = raapi.getIteratorForDirectObjectClasses(rObject);
		if (itCls == 0)
			return 0;
		long rCls = raapi.resolveIteratorFirst(itCls);
		if (rCls == 0) {
			raapi.freeIterator(itCls);
			return 0;
		}
		
		long retVal = raapi.createObject(rCls);
		if (retVal == 0) {
			raapi.freeReference(rCls);
			raapi.freeIterator(itCls);
			return 0;
		}
		
		
		while (rCls!=0) {			
			long itA;
			long rA;
						
			// copying attribute values...
			itA = raapi.getIteratorForAllAttributes(rCls);
			rA = raapi.resolveIteratorFirst(itA);
			while (rA != 0) {
				String val  = raapi.getAttributeValue(rObject, rA);
				if (val != null)
					raapi.setAttributeValue(retVal, rA, val);
				raapi.freeReference(rA);
				rA = raapi.resolveIteratorNext(itA);
			}
			raapi.freeIterator(itA);
			
			// copying links corresponding to all outgoing associations (including bi-directional)...
			itA = raapi.getIteratorForAllOutgoingAssociationEnds(rCls);
			rA = raapi.resolveIteratorFirst(itA);
			while (rA != 0) {
				long itLinked;
				long rLinked;
				
				boolean isComposition = raapi.isComposition(rA);
				
				itLinked = raapi.getIteratorForLinkedObjects(rObject, rA);
				rLinked = raapi.resolveIteratorFirst(itLinked);
				while (rLinked != 0) {
					
					if (isComposition) {
						long rLinked2 = copyObject(raapi, rLinked);
						raapi.createLink(retVal, rLinked2, rA);
					}
					else					
						raapi.createLink(retVal, rLinked, rA);
					
					rLinked = raapi.resolveIteratorNext(itLinked);
				}
				raapi.freeIterator(itLinked);
				
				
				raapi.freeReference(rA);
				
				rA = raapi.resolveIteratorNext(itA);
			}
			raapi.freeIterator(itA);
			

			// copying links corresponding to remaining unidirectional associations...
			// (bi-directional ones have just been processed)
			itA = raapi.getIteratorForAllIngoingAssociationEnds(rCls);
			rA = raapi.resolveIteratorFirst(itA);
			while (rA != 0) {				
				
				long rAInv = raapi.getInverseAssociationEnd(rA);
				if (rAInv == 0) {
				
					long itSrcObj;
					long rSrcObj;

					boolean isCompositionInv = raapi.isComposition(rAInv);
					
					long rSrcCls = raapi.getSourceClass(rA);
					
					itSrcObj = raapi.getIteratorForAllClassObjects(rSrcCls);
					rSrcObj = raapi.resolveIteratorFirst(itSrcObj);
					while (rSrcObj != 0) {
						if (raapi.linkExists(rSrcObj, rObject, rA)) {
							
							if (isCompositionInv) {
								long rSrcObj2 = copyObject(raapi, rSrcObj);
								raapi.createLink(rSrcObj2, retVal, rA);
							}
							else												
								raapi.createLink(rSrcObj, retVal, rA);
						}
						
						rSrcObj = raapi.resolveIteratorNext(itSrcObj);
					}
					raapi.freeIterator(itSrcObj);
					raapi.freeReference(rA);
				}
				else {
					raapi.freeReference(rAInv);
					raapi.freeReference(rA);
				}
				
				rA = raapi.resolveIteratorNext(itA);
			}
			raapi.freeIterator(itA);
			
			raapi.freeReference(rCls);
			
			rCls = raapi.resolveIteratorNext(itCls);
			
			if (rCls != 0)
				raapi.includeObjectInClass(retVal, rCls);
		}
		raapi.freeIterator(itCls);

		return retVal;		
	}
}
