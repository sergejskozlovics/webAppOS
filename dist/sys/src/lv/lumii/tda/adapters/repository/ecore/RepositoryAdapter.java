// The RepositoryAdapter class for the ECore repository.

package lv.lumii.tda.adapters.repository.ecore;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.JOptionPane;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.*;

import lv.lumii.tda.raapi.*;
import lv.lumii.tda.kernel.*;
import lv.lumii.tda.util.ReverseObjectsIndexer;


public class RepositoryAdapter extends RepositoryAdapterBase implements IRepository {
	
	private IRepository parentDelegator = null;
	
	private long getPropertyAsLong(String name) {
		try {
			return Long.parseLong(properties.getProperty(name));
		}
		catch (Throwable t) {
			return 0;
		}
	}

	private void putPropertyAsLong(String name, long l) {
		if (l!=0)
			properties.put(name, Long.toString(l));		
	}
	
	private void putCompositionProperty(long rAssoc) {
		Object obj = indexer.getObject(rAssoc);
		if (obj instanceof EStructuralFeature)
			properties.put(((EClass)((EStructuralFeature) obj).eContainer()).getName()+"."+((EStructuralFeature)obj).getName()+".isComposition", "true");
	}
	
	private boolean getCompositionProperty(long rAssoc) {
		Object obj = indexer.getObject(rAssoc);	
		if (!(obj instanceof EStructuralFeature))
			return false;
		
		String value = properties.getProperty(((EClass)((EStructuralFeature) obj).eContainer()).getName()+"."+((EStructuralFeature)obj).getName()+".isComposition");
		
		if (value == null)
			return false;
		if (value.trim().isEmpty())
			return false;
		return value.equalsIgnoreCase("true");
	}
	
	private void deleteCompositionProperty(long rAssoc) {
		Object obj = indexer.getObject(rAssoc);	
		if (obj instanceof EStructuralFeature)					
			deleteProperty(((EClass)((EStructuralFeature) obj).eContainer()).getName()+"."+((EStructuralFeature)obj).getName()+".isComposition");
	}
	
	private void deleteProperty(String name) {
		if (properties.containsKey(name))
			properties.remove(name);
	}

	public class MyReverseObjectsIndexer<T> extends ReverseObjectsIndexer<T> { 
		public long getIndex(T obj) {			
			if (obj instanceof MyIterator)
				return super.getIndex(obj);
			
			Long l = super.findIndex(obj);
			if (l != 0)
				return l.longValue();
						
			// generating...
			long retVal = super.getIndex(obj);
			
			if (obj instanceof EDataType) {
				if (obj == theCorePackage.getEString())
					putPropertyAsLong("String", retVal);
				else
				if (obj == theCorePackage.getEInt())
					putPropertyAsLong("Integer", retVal);
				else
				if (obj == theCorePackage.getEDouble())
					putPropertyAsLong("Real", retVal);
				else
				if (obj == theCorePackage.getEBoolean())
					putPropertyAsLong("Boolean", retVal);				
			}
			else
			if (obj instanceof EClass) {
				putPropertyAsLong(((EClass) obj).getName(), retVal);
			}
			else
			if (obj instanceof EStructuralFeature) {
				putPropertyAsLong(((EClass)((EStructuralFeature) obj).eContainer()).getName()+"."+((EStructuralFeature)obj).getName(), retVal);
			}
			else
			if (obj instanceof EObject) {
				// do not put property...; we will generate object properties during save
			}

			return retVal;
		}
	}
	
	public static IRepository create()
	{
		RepositoryAdapter tthis = new RepositoryAdapter(); 
		IRepository retVal = 
				new DelegatorToRepositoryWithCascadeDelete(new DelegatorToRepositoryWithOptionalOperations(tthis));
		tthis.parentDelegator = retVal;
		return retVal;
	}

	private URI openRepositoryURI = null;
	private Properties properties = new Properties();
		// Storing stable references. The format is:
		// ClassName=123
		// ClassName.attrName=456
		// ClassName.assocRoleName=789
		// ClassName[0]=234
		// ClassName[1]=567
	private ReverseObjectsIndexer<Object> indexer = new MyReverseObjectsIndexer<Object>();
	
	private EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
	private EcorePackage theCorePackage = EcorePackage.eINSTANCE;
	private EPackage rootPackage = null; // TODO: create/find on open
	private Map<String, EPackage > nameToPackageMap
		= new HashMap<String, EPackage>();
	private Map<String, List<EPackage> > ecoreToPackagesMap
		= new HashMap<String, List<EPackage> >();
	
	private Map<Long, ArrayList<EObject> > classToObjects
		= new HashMap<Long, ArrayList<EObject> >();
	
	private void reloadModelStart() {
		if (openRepositoryURI == null)
			return;
		
		// Saving backup of the current model on the disk...
		File fNextVersion = new File(uriToFileString(openRepositoryURI));
		
		URI previousVersion = URI.createURI(openRepositoryURI.toString()+".reload_bak");
		File fPreviousVersion = new File(uriToFileString(previousVersion));
		if (fPreviousVersion.exists())
			fPreviousVersion.delete();
		if (fNextVersion.exists()) 
			fNextVersion.renameTo(fPreviousVersion);

		try {
			// Saving the model...
			ResourceSet resourceSet = new ResourceSetImpl();
			/*resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			    "xml", new  XMLResourceFactoryImpl());*/
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				"*", new  XMIResourceFactoryImpl());
			
			for (EPackage pkg : this.nameToPackageMap.values()) {
				resourceSet.getPackageRegistry().put(
						pkg.getNsURI(),
						pkg);
			}
	
			Resource resource = resourceSet.createResource(openRepositoryURI);
			for (Long rCls : classToObjects.keySet()) {
				for (EObject eObj : classToObjects.get(rCls)) {
					
					
					if ((eObj.eContainer() == null) || (eObj.eResource()==null)) {
						EClass eCls = (EClass)indexer.getObject(rCls);
						
						if (eCls != null) {
							if (!resource.getContents().contains(eCls)) {
									resource.getContents().add(eCls);
							}
						}
						
						if (!resource.getContents().contains(eObj))
							resource.getContents().add(eObj);
						
					}							
				}
			}
		
			if (resource instanceof XMLResource) {
				((XMLResource) resource).setEncoding("UTF-8");
			}
			Map options = new HashMap();
			options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			options.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);
			options.put(XMLResource.OPTION_SKIP_ESCAPE_URI, Boolean.FALSE);
			options.put(XMLResource.OPTION_SKIP_ESCAPE, Boolean.FALSE);
			//options.put(XMLResource.OPTION_FORMATTED, Boolean.TRUE);
			try{
				resource.save(options);
			}
			catch (Throwable t) {
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				System.err.println(sw.toString());
			}
			
			
			for (Long rClass : classToObjects.keySet()) {
				ArrayList<EObject> list = classToObjects.get(rClass);
				for (int i=0; i<list.size(); i++) {
					String name = this.getClassName(rClass)+"["+i+"]";
					long index = indexer.getIndex(list.get(i));
					this.putPropertyAsLong(name, index);
					indexer.freeIndex(index);
				}
			}
			
/*			System.err.println("press enter...");
			 try
		        {
		            System.in.read();
		            System.in.read();
		        }  
		        catch(Exception e)
		        {}*/  
			
		}
		finally {
/*			// Restoring the backup...
			if (fNextVersion.exists()) 
				fNextVersion.delete();
			if (fPreviousVersion.exists())
				fPreviousVersion.renameTo(fNextVersion);*/
		}
	}

	
	private void reloadModelEnd() {
		if (openRepositoryURI == null)
			return;
		
		File fNextVersion = new File(uriToFileString(openRepositoryURI));
		
		URI previousVersion = URI.createURI(openRepositoryURI.toString()+".reload_bak");
		File fPreviousVersion = new File(uriToFileString(previousVersion));

		//try {
			// Reading the model...
			classToObjects.clear();
			
			ResourceSet resourceSet2 = new ResourceSetImpl();		
			resourceSet2.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl()); // .xml, .xmi, and others
			
			for (EPackage pkg : this.nameToPackageMap.values()) {
				resourceSet2.getPackageRegistry().put(
						pkg.getNsURI(),
						pkg);
			}
	
			Resource resource2 = null;
			try {
				resource2 = resourceSet2.getResource(openRepositoryURI,true);
			}
			catch(Throwable t) {
				if (!exists(openRepositoryURI.toString())) // .xmi
					resource2 = null;
				else
					throw t; // .xmi exists, but there was an error, when opening
			}
			if (resource2 != null) {
				Iterator<EObject> it = resource2.getAllContents(); // getting all objects recursively... 
				while (it.hasNext()) {		
					EObject eObj = it.next();
					registerEObject(eObj);                        // ...and registering them in our data structures
				}
			}

			// update object indices...
			for (Long rClass : classToObjects.keySet()) {				
				ArrayList<EObject> list = classToObjects.get(rClass);
				for (int i=0; i<list.size(); i++) {
					String name = this.getClassName(rClass)+"["+i+"]";
					long l = getPropertyAsLong(name);
					if (l!=0)
						indexer.set(l, list.get(i));
					this.deleteProperty(name);
				}
			}			
		//}
		//finally {
			// Restoring the backup...
			if (fNextVersion.exists()) 
				fNextVersion.delete();
			if (fPreviousVersion.exists())
				fPreviousVersion.renameTo(fNextVersion);
		//}
	}
	
	private class MyIterator {
		private int curPos = 0;
		
		ArrayList list;
		
		public MyIterator(ArrayList _list)
		{
			list = _list;
		}
		
		public long resolveFirst()
		{
			curPos = 0;
			if (curPos < list.size())
				return indexer.getIndex(list.get(curPos));
			else {
				return 0;
			}
		}
		
		public long resolveNext()
		{
			curPos++;
			if (curPos < list.size())
				return indexer.getIndex(list.get(curPos));
			else {
				return 0;
			}
		}
		
		public long resolve(int pos)
		{
			curPos = pos;
			if (curPos < list.size())
				return indexer.getIndex(list.get(curPos));
			else {
				return 0;
			}
		}
		
		public int length()
		{
			return list.size();
		}
		
	}
	
	public RepositoryAdapter()
	{
    }

	@Override
	public long findPrimitiveDataType (String name)
	{
		if (name.toLowerCase().equals("string"))
			return indexer.getIndex(theCorePackage.getEString());
		if (name.toLowerCase().equals("int") || name.toLowerCase().equals("integer"))
			return indexer.getIndex(theCorePackage.getEInt());
		if (name.toLowerCase().equals("float") || name.toLowerCase().equals("real")) 			
			return indexer.getIndex(theCorePackage.getEDouble());
		if (name.toLowerCase().equals("bool") || name.toLowerCase().equals("boolean")) 
			return indexer.getIndex(theCorePackage.getEBoolean());
		return 0;
	}
	
	@Override
	public String getPrimitiveDataTypeName (long rDataType)
	{
		if (rDataType == 0)
			return null;
		Object obj = indexer.getObject(rDataType);
		
		if (obj == theCorePackage.getEString())
			return "String";
		if (obj == theCorePackage.getEInt())
			return "Integer";
		if (obj == theCorePackage.getEDouble())
			return "Real";
		if (obj == theCorePackage.getEBoolean())
			return "Boolean";
			
		return null;		
	}
	
	@Override
	public boolean isPrimitiveDataType (long r)
	{
		return (getPrimitiveDataTypeName(r) != null);
	}
	
	private class PackageInfo {
		EPackage ePackage;
		String packageName;
		String className;
	}
	
	private PackageInfo getPackageInfo(String fullName)
	{
		PackageInfo retVal = new PackageInfo();
		int i = fullName.indexOf("::");
		if (i>=0) {
			retVal.packageName = fullName.substring(0, i);
			retVal.className = fullName.substring(i+2);
			retVal.ePackage = nameToPackageMap.get(retVal.packageName);
			if (retVal.ePackage == null) {
				retVal.packageName = "";
				retVal.className = fullName;
				if (rootPackage == null)
					this.createRootPackage();
				retVal.ePackage = rootPackage;				
			}
		}
		else {
			retVal.packageName = "";
			retVal.className = fullName;
			if (rootPackage == null)
				this.createRootPackage();
			retVal.ePackage = rootPackage;
		}
		return retVal;			
	}
	
	@Override
	public long createClass(String name) {
		long r = findClass(name);
		if (r!=0)
			return 0; // already exists...
		
		name = name.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(" ", "_BLANK_").replaceAll("::", "_COLONS_");
		EClass eCls = theCoreFactory.createEClass();
		//eCls.getEStructuralFeatures().add(theCorePackage.getEClass_EIDAttribute());
		PackageInfo info = getPackageInfo(name);		
		eCls.setName(info.className); 
		info.ePackage.getEClassifiers().add(eCls);
		return indexer.getIndex(eCls);
	}

	@Override
	public long findClass(String name) {
		name = name.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(" ", "_BLANK_").replaceAll("::", "_COLONS_");
		
		PackageInfo info = getPackageInfo(name);
		
		EClassifier eCls = info.ePackage.getEClassifier(info.className);
		if (!(eCls instanceof EClass)) {
			
			if (rootPackage!=null) {
				// trying to check the root package...
				eCls = rootPackage.getEClassifier(name);
				if (!(eCls instanceof EClass))
					return 0;
				
				// TODO other packages!
			}
			else			
				return 0; // ignoring non-classes
		}
		
		
		return indexer.getIndex(eCls);		
	}
	
	@Override
	public String getClassName(long rClass) {
		if (!isClass(rClass))
			return null;
		EClass eCls = (EClass)indexer.getObject(rClass);
		if (eCls != null) {
			String name = eCls.getName();
			if (eCls.getEPackage()!=null && eCls.getEPackage() != rootPackage) {
				name = eCls.getEPackage().getNsPrefix() + "::" + name;
			}
			return name.replaceAll("_SHARP_", "#").replaceAll("_DOT_", ".").replaceAll("_BLANK_", " ").replaceAll("_COLONS_", "::");
		}
		else
			return null;
	}

	@Override
	public boolean deleteClass(long rClass) {		
		if (!isClass(rClass))
			return false;
		
		classToObjects.remove(rClass);
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		
		if (eCls != null) {
			EcoreUtil.delete(eCls, true);
			EcoreUtil.remove(eCls);
			indexer.freeIndex(rClass);
		}
		return true;
		//return (eCls != null) && rootPackage.getEClassifiers().remove(eCls);
	}
	
	@Override
	public boolean isClass (long r)
	{
		Object obj = indexer.getObject(r);
		return (obj != null) && (obj instanceof EClass);
	}
	
	@Override
	public long createObject(long rClass) {
		if (!isClass(rClass))
			return 0;

		EClass eCls = (EClass)indexer.getObject(rClass);
		if (eCls == null)
			return 0;
		
		PackageInfo info = getPackageInfo(getClassName(rClass));
		
		EObject eObj = info.ePackage.getEFactoryInstance().create(eCls);
		
		registerEObject(eObj);
		
		long retVal = indexer.getIndex(eObj);
		return retVal;
	}

	
    private void unregisterObjectAndContained(final EObject object) {
        for (EObject contained : object.eContents()) {
            unregisterObjectAndContained(contained);
        }
        unregisterEObject(object);
    }
	
	// TODO: there are problems with delete (? in the ECore internal implementation);
	// all the object's properties and dependent elements have to cascade deleted before deleteObject 
	@Override
	public boolean deleteObject(long rObject) {
		
		
		
		Object obj = indexer.getObject(rObject);
		if ((obj != null) && (obj instanceof EObject)) {
			
			
			//unregisterObjectAndContained((EObject)obj);
			//EcoreUtil.delete((EObject)obj, true);
			unregisterEObject((EObject)obj);
			EcoreUtil.delete((EObject)obj, false);
			
			indexer.freeIndex(rObject);
			
			return true;
		}
		else
			return false;
	}
		
	@Override
	public boolean includeObjectInClass(long rObject, long rClass) {
		return false;
	}

	@Override
	public boolean excludeObjectFromClass(long rObject, long rClass) {
		return false;
	}

	@Override
	public long createAttribute(long rClass, String name, long type) {
		
		name = name.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(":", "_COLON_").replaceAll(" ", "_BLANK_").replaceAll("::","_COLONS_");
		Object t = indexer.getObject(type);
		if (!(t instanceof EDataType))
			return 0;
		EDataType eType = (EDataType)t; 
		if (!isClass(rClass))
			return 0;
		
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		if (eCls.getEStructuralFeature(name) != null)  {// already exists
			System.err.println("crAt "+this.getClassName(rClass)+"."+name+" property already exists (attribute/association)");
			return 0;
		}
	
		boolean needReload=needToReloadForClass(rClass);  
		if (needReload) {
			System.err.println("crAt "+this.getClassName(rClass)+"."+name+" reload start");
			this.reloadModelStart();
		}
		
		
		EAttribute eAttr = theCoreFactory.createEAttribute();
		eAttr.setName(name);
		eAttr.setEType(eType);
		
		eAttr.setUnsettable(true);
		eAttr.setChangeable(true);
		eAttr.setLowerBound(0);
		eAttr.setUpperBound(1);
		eCls.getEStructuralFeatures().add(eAttr);			
		
		//updateObjects(rClass);
		
		long retVal = indexer.getIndex(eAttr);
		if (needReload) {
			System.err.println("crAt "+this.getClassName(rClass)+"."+name+" reload end");
			this.reloadModelEnd();
		}
		return retVal;
	}

	@Override
	public long findAttribute(long rClass, String name) {
		name = name.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(":", "_COLON_").replaceAll(" ", "_BLANK_").replaceAll("::", "_COLONS_");
		if (!isClass(rClass))
			return 0;
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		
		EStructuralFeature eSF = eCls.getEStructuralFeature(name);
		if ((eSF != null) && (eSF instanceof EAttribute))
			return indexer.getIndex(eSF);
		else
			return 0;
	}

	@Override
	public String getAttributeName(long rAttribute) {
		if (!isAttribute(rAttribute))
			return null;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		return eAttr.getName().replaceAll("_SHARP_", "#").replaceAll("_DOT_", ".").replaceAll("_COLON_",":").replaceAll("_BLANK_", " ").replaceAll("_COLONS_","::");
		
	}

	@Override
	public long getAttributeDomain (long rAttribute)
	{
		if (!isAttribute(rAttribute))
			return 0;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		return indexer.getIndex(eAttr.getEContainingClass());
	}
	
	@Override
	public long getAttributeType(long rAttribute) {
		if (!isAttribute(rAttribute))
			return 0;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		
		return indexer.getIndex(eAttr.getEType());
	}
	
	@Override
	public boolean isAttribute (long r)
	{		
		Object obj = indexer.getObject(r);
		return (obj != null) && (obj instanceof EAttribute);
	}
	

	@Override
	public boolean deleteAttribute(long rAttribute) {
		if (!isAttribute(rAttribute))
			return false;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		if (eAttr == null)
			return false;
		
		boolean needReload=needToReloadForClass(this.getAttributeDomain(rAttribute));  
		if (needReload) {
			System.err.println("delAt reload start");
			this.reloadModelStart();
		}
		
		EClass eClass = eAttr.getEContainingClass();
		EcoreUtil.delete(eAttr, true);
		EcoreUtil.remove(eAttr);
		eClass.getEStructuralFeatures().remove(eAttr);
		
		indexer.freeIndex(rAttribute);

		/*EObject container = eAttr.eContainer();
		if (container != null)
			container.eContents().remove(eAttr);*/

		//updateObjects(indexer.getIndex(eClass));
		
//		System.err.println("delAt reload start");
//		this.reloadModel();
//		System.err.println("delAt reload end");
		if (needReload) {
			System.err.println("delAt reload end");
			this.reloadModelEnd();
		}
		
		return true;
	}

	@Override
	public boolean setAttributeValue(long rObject, long rAttribute, String value) {
		
		if (value == null)
			return deleteAttributeValue(rObject, rAttribute);
		
		Object obj = indexer.getObject(rObject);
		if (!(obj instanceof EObject))
			return false;
		
		if (!isAttribute(rAttribute))
			return false;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		
		if (eAttr.getEType() == theCorePackage.getEString())
			try {
				if (eAttr.isMany()) {
					EObject e;
					EList list = (EList)((EObject)obj).eGet(eAttr);
					list.clear();
					list.add(value);
				}
				else
					((EObject)obj).eSet(eAttr, value);
			}
			catch (Throwable t) {
				return false;
			}
		else
		if (eAttr.getEType() == theCorePackage.getEInt()) {
			try {
				if (eAttr.isMany()) {
					EList list = (EList)((EObject)obj).eGet(eAttr);
					list.clear();
					list.add(Integer.parseInt(value));
				}
				else
					((EObject)obj).eSet(eAttr, Integer.parseInt(value));
			}
			catch (Throwable t) {
				return false;
			}
		}
		else
		if (eAttr.getEType() == theCorePackage.getEDouble()) {
			try {
				if (eAttr.isMany()) {
					EList list = (EList)((EObject)obj).eGet(eAttr);
					list.clear();
					list.add(Double.valueOf(value));
				}
				else
				//((EObject)obj).eSet(eAttr, Double.parseDouble(value));
					((EObject)obj).eSet(eAttr, Double.valueOf(value));
			}
			catch (Throwable t) {
				return false;
			}
		}
		else
		if (eAttr.getEType() == theCorePackage.getEBoolean()) {
			try {
				if (eAttr.isMany()) {
					EList list = (EList)((EObject)obj).eGet(eAttr);
					list.clear();
					list.add(Boolean.parseBoolean(value));
				}
				else
					((EObject)obj).eSet(eAttr, Boolean.parseBoolean(value));
			}
			catch (Throwable t) {
				return false;
			}
		}
		else
			return false;
		return true;
	}

	@Override
	public String getAttributeValue(long rObject, long rAttribute) {
		Object obj = indexer.getObject(rObject);
		if (!(obj instanceof EObject))
			return null;
		
		if (!isAttribute(rAttribute))
			return null;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		
		try {
			if (!((EObject)obj).eIsSet(eAttr))
				return null;
		}
		catch(Throwable t) {
			return null;
		}
		
		if (eAttr.isMany()) {
			EList list = (EList)((EObject)obj).eGet(eAttr);
			if (list == null)
				return null;
			String retVal = null;
			for (Object o : list) {
				if (retVal == null)
					retVal = o.toString();
				else
					retVal += "\u001f"+o.toString();
			}
			return retVal;
		}
		else {		
			Object retVal = ((EObject)obj).eGet(eAttr);
			if (retVal == null)
				return null;
			else {
				return retVal.toString();
			}
		}
	}

	@Override
	public boolean deleteAttributeValue(long rObject, long rAttribute) {
		Object obj = indexer.getObject(rObject);
		if (!(obj instanceof EObject))
			return false;
		
		if (!isAttribute(rAttribute))
			return false;
		EAttribute eAttr = (EAttribute)indexer.getObject(rAttribute);
		
		boolean hadValue = false;
		try {
			hadValue = ((EObject)obj).eIsSet(eAttr);
		}
		catch(Throwable t) {
			
		}
		
		if (!hadValue)
			return false;
		
		//EcoreUtil.remove((EObject)obj, eAttr, ((EObject)obj).eGet(eAttr));//
		//EcoreUtil.remove((EObject)obj, eAttr, ((EObject)obj).eGet(eAttr));//
		
		if (eAttr.isUnsettable())
			((EObject)obj).eUnset(eAttr);
		else
			((EObject)obj).eSet(eAttr, null);
		
		return hadValue && !((EObject)obj).eIsSet(eAttr);
	}

	private boolean needToReloadForClass(long rClass) {
		if (this.parentDelegator==null)
			return true;
		long it = this.parentDelegator.getIteratorForAllClassObjects(rClass);
		long r = this.parentDelegator.resolveIteratorFirst(it);
		this.parentDelegator.freeIterator(it);
		this.parentDelegator.freeReference(r);
		return (r!=0);
	}
	
	@Override
	public long createAssociation(long rSourceClass, long rTargetClass,
			String sourceRole, String targetRole, boolean isComposition) {
		
		if (!isClass(rSourceClass) && !isClass(rTargetClass))
			return 0;		

		
		EClass eSrcCls = (EClass)indexer.getObject(rSourceClass);
		EClass eTgtCls = (EClass)indexer.getObject(rTargetClass);
		if ((eSrcCls == null) || (eTgtCls == null))
			return 0;

		boolean needReload = isComposition || needToReloadForClass(rSourceClass) || needToReloadForClass(rTargetClass);
		if (needReload) {
			System.err.println("crAs "+this.getClassName(rSourceClass)+"."+targetRole+" reload start");
			this.reloadModelStart();
		}
		
		sourceRole = sourceRole.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(":", "_COLON_").replaceAll(" ", "_BLANK_").replaceAll("::", "_COLONS_");
		targetRole = targetRole.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(":", "_COLON_").replaceAll(" ", "_BLANK_").replaceAll("::", "_COLONS_");
		
		EReference eAssoc = theCoreFactory.createEReference();
		
		eAssoc.setName(targetRole);
		eAssoc.setEType(eTgtCls);
		eAssoc.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		eAssoc.setOrdered(true);
		eSrcCls.getEStructuralFeatures().add(eAssoc);
		
		EReference eInvAssoc = theCoreFactory.createEReference();
		eInvAssoc.setName(sourceRole);
		eInvAssoc.setEType(eSrcCls);
		
		if (isComposition) {
			// eAssoc.setContainment(true); -- produces errors on save
			eInvAssoc.setUpperBound(1);
		}
		else
			eInvAssoc.setUpperBound(EStructuralFeature.UNBOUNDED_MULTIPLICITY);
		
		eInvAssoc.setOrdered(true);
		eTgtCls.getEStructuralFeatures().add(eInvAssoc);
		
		eAssoc.setEOpposite(eInvAssoc);
		eInvAssoc.setEOpposite(eAssoc);
	
		long retVal = indexer.getIndex(eAssoc);
		long inv = indexer.getIndex(eInvAssoc);
	

		if (isComposition)
			this.putCompositionProperty(retVal);
		
		/*updateObjects(rSourceClass);
		if (rTargetClass != rSourceClass)
			updateObjects(rTargetClass);*/
		
		if (needReload) {
			System.err.println("crAs "+this.getClassName(rSourceClass)+"."+targetRole+"/"+sourceRole+" reload end");
			this.reloadModelEnd();
		}

		return retVal;
	}

	@Override
	public long findAssociationEnd(long rSourceClass, String targetRole) {
		if (targetRole == null)
			return 0;
		
		if (!isClass(rSourceClass))
			return 0;
		
		targetRole = targetRole.replaceAll("#", "_SHARP_").replaceAll("\\.", "_DOT_").replaceAll(":", "_COLON_").replaceAll(" ", "_BLANK_").replaceAll("::", "_COLONS_");
		
		EClass eCls = (EClass)indexer.getObject(rSourceClass);
		EStructuralFeature eSF = eCls.getEStructuralFeature(targetRole);
		if (eSF != null) {
			if (eSF instanceof EReference)
				return indexer.getIndex(eSF);
			else {
				for (EStructuralFeature sf : eCls.getEStructuralFeatures() ) {
					if (targetRole.equals(sf.getName()) && (sf instanceof EReference))
						return indexer.getIndex(sf);
				}		
				return 0;
			}
		}
		else {
			return 0;
		}
	}

	@Override
	public long getInverseAssociationEnd(long rAssociation) {
		Object obj = indexer.getObject(rAssociation);
		if (!(obj instanceof EReference))
			return 0;
		
		long r = indexer.getIndex(((EReference)obj).getEOpposite());
		return r;
	}

	@Override
	public long getSourceClass(long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return 0;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		return indexer.getIndex(eAssoc.getEContainingClass());
	}

	@Override
	public long getTargetClass(long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return 0;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		
		return indexer.getIndex(eAssoc.getEReferenceType());
	}

	@Override
	public String getRoleName(long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return null;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		return eAssoc.getName().replaceAll("_SHARP_", "#").replaceAll("_DOT_", ".").replaceAll("_COLON_",":").replaceAll("_BLANK_", " ").replaceAll("_COLONS_","::");
	}

	@Override
	public boolean isComposition(long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return false;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		if (eAssoc.isContainment())
			return true;
		
		return this.getCompositionProperty(rAssociation);
	}
	
	@Override
	public boolean isAdvancedAssociation (long r)
	{
		return false;
	}
	 
	@Override
	public boolean isAssociationEnd (long r)
	{
		Object obj = indexer.getObject(r);
		return (obj instanceof EReference);
	}

	@Override
	public boolean deleteAssociation(long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return false;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		
		if (eAssoc != null) {
			long rCls1 = this.getSourceClass(rAssociation);
			long rCls2 = this.getTargetClass(rAssociation);
			
		
			boolean needReload=needToReloadForClass(rCls1) || needToReloadForClass(rCls2);  
			if (needReload) {
				System.err.println("delAs reload start");
				this.reloadModelStart();
			}
			
			EReference eInvAssoc = eAssoc.getEOpposite();
			if (eInvAssoc != null) {
				EcoreUtil.delete(eInvAssoc, true);
				EcoreUtil.remove(eInvAssoc);
				
				long rInvAssoc = indexer.getIndex(eInvAssoc);
				if (rInvAssoc != 0) {
					indexer.freeIndex(rInvAssoc);				
					this.deleteCompositionProperty(rInvAssoc);
				}
			}
					
			EcoreUtil.delete(eAssoc, true);
			EcoreUtil.remove(eAssoc);
			indexer.freeIndex(rAssociation);			
			this.deleteCompositionProperty(rAssociation);
			
			
//			System.err.println("delAs reload start");
//			this.reloadModel();
//			System.err.println("delAs reload end");
/*			updateObjects(rCls1);
			if (rCls2 != rCls1)
				updateObjects(rCls2);*/
			
			if (needReload) {
				System.err.println("delAs reload end");
				this.reloadModelEnd();
			}
			
			return true;
		}
		
		return false;	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean createLink(long rSourceObject, long rTargetObject,
			long rAssociation) {
		
		long rrrr = this.getInverseAssociationEnd(rAssociation);
		
		if (linkExists(rSourceObject, rTargetObject, rAssociation)) {
			return false;
		}
		if ((rrrr!=0) && (linkExists(rTargetObject, rSourceObject, rrrr)))
			return false;
		
		if (!isAssociationEnd(rAssociation))
			return false;
		Object obj1 = indexer.getObject(rSourceObject);
		Object obj2 = indexer.getObject(rTargetObject);
		if ((obj1 == null) || (obj2 == null) || !(obj1 instanceof EObject) || !(obj2 instanceof EObject))
			return false;
		
		EObject eObj1 = (EObject)obj1;
		EObject eObj2 = (EObject)obj2;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		EReference eInvAssoc = eAssoc.getEOpposite();
		
		if (eAssoc.isMany()) {
			//if (eInvAssoc != null)
			//	((List) eObj2.eGet(eInvAssoc)).add(eObj1);
			//boolean b = ((List) eObj1.eGet(eAssoc)).add(eObj2);
			boolean retVal = ((List) eObj1.eGet(eAssoc)).add(eObj2);
			
			
			
			if (retVal && (eInvAssoc!=null)) {
				if (eInvAssoc.isMany()) {
					if (!(((List) eObj2.eGet(eInvAssoc)).contains(eObj1)))
						((List) eObj2.eGet(eInvAssoc)).add(eObj1);
				}
				else {
					eObj2.eSet(eInvAssoc, eObj1);
				}
				//assert(((List) eObj2.eGet(eInvAssoc)).contains(eObj1)); // assert that the inverse link has also been created
			}
			if (retVal) {
				if (eAssoc.isContainment()) {
					assert(eObj1.eContents().contains(eObj2));
					//assert eObj2.eContainer() == eObj1 : "problem with container "+eObj2.eContainer()+" "+eObj2.eContainingFeature();
				}
				if (eAssoc.isContainer()) {
					assert(eObj2.eContents().contains(eObj1));
					//assert(eObj1.eContainer() == eObj2);
				}				
			}
			return retVal;
		}
		else {
			//if (eInvAssoc != null)
			//	eObj2.eSet(eAssoc, eObj1);
			eObj1.eSet(eAssoc, eObj2);
			if (eInvAssoc != null)
				((List) eObj2.eGet(eInvAssoc)).add(eObj1);
			//assert(eObj2.eGet(eAssoc) == eObj1);
			return true;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean linkExists(long rSourceObject, long rTargetObject,
			long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return false;
		Object obj1 = indexer.getObject(rSourceObject);
		Object obj2 = indexer.getObject(rTargetObject);
		if ((obj1 == null) || (obj2 == null) || !(obj1 instanceof EObject) || !(obj2 instanceof EObject))
			return false;
		EObject eObj1 = (EObject)obj1;
		EObject eObj2 = (EObject)obj2;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		
		EReference eInv = eAssoc.getEOpposite();
		
		if (eAssoc.isMany()) {
			if (((List) eObj1.eGet(eAssoc)).contains(eObj2))
				return true;
			
			if (eInv!=null) {
				if (eInv.isMany()) {
					Object list = eObj2.eGet(eInv);
					return (list instanceof List) && ((List)list).contains(eObj1);
				}
				else {
					return eObj2.eGet(eInv) == eObj1;
				}
			}
			else
				return false;
		}
		else
			return eObj1.eGet(eAssoc)==eObj2;
	}

	@Override
	public boolean deleteLink(long rSourceObject, long rTargetObject,
			long rAssociation) {
		//JOptionPane.showMessageDialog(null, "delete link called");
		if (!isAssociationEnd(rAssociation))
			return false;
		
		if (!linkExists(rSourceObject, rTargetObject, rAssociation))
			return false;
		
		Object obj1 = indexer.getObject(rSourceObject);
		Object obj2 = indexer.getObject(rTargetObject);
		if ((obj1 == null) || (obj2 == null) || !(obj1 instanceof EObject) || !(obj2 instanceof EObject))
			return false;
		
		
		EObject eObj1 = (EObject)obj1;
		EObject eObj2 = (EObject)obj2;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		
		//if (eAssoc.isMany()) 
		{
			
			
			EReference eInvAssoc = eAssoc.getEOpposite();
			
			/*EList list = ((EList) eObj1.eGet(eAssoc));
			//EcoreUtil.remove(eObj1, eAssoc, list);
			list.remove(eObj2);
			eObj1.eSet(eAssoc, list);

			if (eInvAssoc != null) {
				EList list2 = ((EList) eObj2.eGet(eInvAssoc));
				//EcoreUtil.remove(eObj2, eInvAssoc, list2);
				list2.remove(eObj1);
				eObj2.eSet(eInvAssoc, list2);
			}*/
			
			EcoreUtil.remove(eObj1, eAssoc, eObj2);
			if (eInvAssoc != null) {
				try {
					EcoreUtil.remove(eObj2, eInvAssoc, eObj1);
				}
				catch(Throwable t) {
					
				}
			}
			
			/*--
			((List) eObj1.eGet(eAssoc)).remove(eObj2);
			
			if (eInvAssoc != null) {
				
				//EcoreUtil.remove(eObj2, eInvAssoc, eObj1);
				((List) eObj2.eGet(eInvAssoc)).remove(eObj1);
			}*/
			

            /*if (eAssoc.isContainment()) 
			{
                eObj1.eResource().getContents().remove(eObj2);
            	eObj1.eContents().remove(eObj2);
            }
            else 
            if (eAssoc.isContainer()) 
			{ 
            	eObj2.eResource().getContents().remove(eObj1);
            	eObj2.eContents().remove(eObj1);
            }*/
			
			
			boolean retVal = false;
			
			try {
				retVal = !((List) eObj1.eGet(eAssoc)).contains(eObj2);
			}
			catch(Throwable t) {
				
			}
			

			
			//if (eInvAssoc != null)
				//EcoreUtil.remove(eObj2, eInvAssoc, eObj1);
			//((List) eObj2.eGet(eInvAssoc)).remove(eObj1);
			

			//((List) eObj1.eGet(eAssoc)).remove(eObj2);
			//if (eInvAssoc != null)
				//((List) eObj2.eGet(eInvAssoc)).remove(eObj1);

			
//			assert(!((List) eObj1.eGet(eAssoc)).contains(eObj2));
			
			if (eAssoc.isContainment())
				assert(!eObj1.eContents().contains(eObj2));
			if (eInvAssoc.isContainment())
				assert(!eObj2.eContents().contains(eObj1));
			return retVal;
			//return ((List) eObj1.eGet(eAssoc)).remove(eObj2);
		}
/*		else {
            //if (eAssoc.isContainment()) 
			{
                //eObj1.eResource().getContents().remove(eObj2);
                //eObj1.eContents().remove(eObj2);
            }
            //else 
            //if (eAssoc.isContainer())
            { 
            	//eObj2.eResource().getContents().remove(eObj1);
            	//eObj2.eContents().remove(eObj1);
            }
			
			EReference eInvAssoc = eAssoc.getEOpposite();
			if (eInvAssoc != null) {
				if (eObj2.eGet(eInvAssoc) == eObj1) 
					eObj2.eUnset(eInvAssoc);				
			}
			if (eObj1.eGet(eAssoc) == eObj2) {
				eObj1.eUnset(eAssoc);
				return true;
			}
			else
				return false;
		}*/
	}	
	
	@Override
	public boolean createGeneralization(long rSubClass, long rSuperClass) {
		if (!isClass(rSubClass) && !isClass(rSuperClass))
			return false;
		EClass eSubCls = (EClass)indexer.getObject(rSubClass);
		EClass eSuperCls = (EClass)indexer.getObject(rSuperClass);
		if ((eSubCls == null) || (eSuperCls == null))
			return false;
		
		//updateObjects(rSubClass);
		
		return eSubCls.getESuperTypes().add(eSuperCls);
	}

	@Override
	public boolean isDirectSubClass(long rSubClass, long rSuperClass) {
		if (!isClass(rSubClass) && !isClass(rSuperClass))
			return false;
		EClass eSubCls = (EClass)indexer.getObject(rSubClass);
		EClass eSuperCls = (EClass)indexer.getObject(rSuperClass);
		if ((eSubCls == null) || (eSuperCls == null))
			return false;
		
		return eSubCls.getESuperTypes().contains(eSuperCls);
	}

	// Let TDA Kernel implement this:
	// public boolean isDerivedClass(long rDirectlyOrIndirectlyDerivedClass,
	//		long rSuperClass);

	@Override
	public boolean deleteGeneralization(long rSubClass, long rSuperClass) {
		if (!isClass(rSubClass) && !isClass(rSuperClass))
			return false;
		EClass eSubCls = (EClass)indexer.getObject(rSubClass);
		EClass eSuperCls = (EClass)indexer.getObject(rSuperClass);
		if ((eSubCls == null) || (eSuperCls == null))
			return false;
		
		//updateObjects(rSubClass);
		
		return eSubCls.getESuperTypes().remove(eSuperCls);
	}
	
	private long ArrayList2Iterator(ArrayList list)
	{
		MyIterator it = new MyIterator(list);
		return indexer.getIndex(it);
	}

	@Override
	public long getIteratorForClasses() {
		ArrayList<EClass> list = new ArrayList<EClass>();
		
		for (EPackage pkg : this.nameToPackageMap.values()) {
			for (EClassifier e : pkg.getEClassifiers()) {
				if (e instanceof EClass) {
					list.add((EClass)e);
				}
			}
		}
		
/*		for (EClassifier e : rootPackage.getEClassifiers()) {
			if (e instanceof EClass) {
				list.add((EClass)e);
			}
		}*/
		return ArrayList2Iterator(list);
	}

	/*@Override
	public long getIteratorForAllClassObjects(long rClass) {
		if (!isClass(rClass))
			return 0;
		EClass eCls = (EClass)indexer.getObject(rClass);
		ArrayList list = new ArrayList();
		
		TreeIterator<EObject> it = eCls.eAllContents();
		
		while (it.hasNext()) {
			EObject eObj = it.next();
			if (eObj.eClass() == eCls)
				list.add(eObj);
			System.out.println("->"+eObj.eClass().getName()+" "+eObj);
		}
		
		//for (EObject eObj : eCls.)
		
		return ArrayList2Iterator(list);
	}*/

	@Override
	public long getIteratorForDirectClassObjects(long rClass) {
		if (!isClass(rClass))
			return 0;
		
		ArrayList<EObject> list = classToObjects.get(rClass);
		if (list == null)
			list = new ArrayList<EObject>();
		else
			list = new ArrayList<EObject>(list); // copying
		return ArrayList2Iterator(list);
	}
	
	@Override
	public long getIteratorForDirectObjectClasses(long rObject) {					
		
		Object obj = indexer.getObject(rObject);
		if (!(obj instanceof EObject))
			return 0;
		EObject eObj = (EObject)obj;
		ArrayList<EClass> list = new ArrayList<EClass>();
		list.add(eObj.eClass());
		
		return ArrayList2Iterator(list);
	}

	@Override
	public long getIteratorForAllAttributes(long rClass) {
		if (!isClass(rClass))
			return 0;
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (EStructuralFeature f : eCls.getEAllStructuralFeatures()) {
			if (f instanceof EAttribute) {
				list.add(f);
			}
		}
		return ArrayList2Iterator(list);
	}

	@Override
	public long getIteratorForDirectAttributes(long rClass) {
		if (!isClass(rClass))
			return 0;
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (EStructuralFeature f : eCls.getEStructuralFeatures()) {
			if (f instanceof EAttribute) {
				list.add(f);
			}
		}
		return ArrayList2Iterator(list);
	}

	@Override
	public long getIteratorForAllOutgoingAssociationEnds(long rClass) {
		if (!isClass(rClass))
			return 0;
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (EStructuralFeature f : eCls.getEAllStructuralFeatures()) {
			if ((f instanceof EReference))
				list.add(f);
		}
		return ArrayList2Iterator(list);
	}

	@Override
	public long  getIteratorForDirectOutgoingAssociationEnds(long rClass) {
		if (!isClass(rClass))
			return 0;
		
		EClass eCls = (EClass)indexer.getObject(rClass);
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (EStructuralFeature f : eCls.getEStructuralFeatures()) {
			if ((f instanceof EReference))
				list.add(f);
		}
		return ArrayList2Iterator(list);
	}

	@Override
	public long getIteratorForLinkedObjects(long rObject, long rAssociation) {
		if (!isAssociationEnd(rAssociation))
			return 0;
		Object obj = indexer.getObject(rObject);
		if (!(obj instanceof EObject))
			return 0;
		
		EObject eObj = (EObject)obj;
		EReference eAssoc = (EReference)indexer.getObject(rAssociation);
		
		if (eAssoc.isMany()) {
			try {
				ArrayList list = new ArrayList();
				
				Object prev = null;
				for (Object o : (List) eObj.eGet(eAssoc)) {					
					if (prev != o) {
						list.add(o);
						prev = o;
					}
				}
				return ArrayList2Iterator(list);
			}
			catch (Throwable t) {
				return 0;
			}
			/*try {
				return ArrayList2Iterator(new ArrayList((List) eObj.eGet(eAssoc)));
			}
			catch (Throwable t) {
				return 0;
			}*/
		}
		else {
			ArrayList<Object> list = new ArrayList<Object>();
			Object obj2 = eObj.eGet(eAssoc);
			if (obj2 instanceof EObject)
				list.add(obj2);
			return ArrayList2Iterator(list);
		}
	}

	@Override
	public long getIteratorForDirectSuperClasses(long rSubClass) {
		if (!isClass(rSubClass))
			return 0;
		
		EClass eSubClass = (EClass)indexer.getObject(rSubClass);

		ArrayList<EClass> list = new ArrayList<EClass>( eSubClass.getESuperTypes() );		
		return ArrayList2Iterator(list);
	}
	
	@Override
	public long getIteratorForDirectSubClasses(long rSuperClass) {
		if (!isClass(rSuperClass))
			return 0;
		
		EClass eSuperClass = (EClass)indexer.getObject(rSuperClass);
		ArrayList<EClass> list = new ArrayList<EClass>();		
		
		for (EPackage pkg : this.nameToPackageMap.values()) {
			for (EClassifier e : pkg.getEClassifiers()) {
				if (e instanceof EClass) {
					EClass eCls = (EClass)e;
					for (EClass eCls2 : eCls.getESuperTypes()) {
						if (eCls2 == eSuperClass)
							list.add(eCls);
					}
				}
			}
		}

		return ArrayList2Iterator(list);
	}

	// TODO: getIteratorForObjectsByAttributeValue
	
	@Override
	public long resolveIteratorFirst(long it) {
		Object obj = indexer.get(it);
		if (!(obj instanceof MyIterator))
			return 0;
		return ((MyIterator)obj).resolveFirst();
	}

	@Override
	public long resolveIteratorNext(long it) {
		Object obj = indexer.get(it);
		if (!(obj instanceof MyIterator))
			return 0;
		return ((MyIterator)obj).resolveNext();
	}

	/*@Override
	public String serializeReference(long r)
	{
		return new Long(r).toString();
	}
	
	@Override
	public long deserializeReference(String s)
	{
		try {
			return Long.parseLong(s);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}*/

	@Override
	public void freeReference(long r) {
	}

	@Override
	public void freeIterator(long it) {
		Object obj = indexer.get(it);
		if (!(obj instanceof MyIterator))
			return;
		indexer.freeIndex(it);
	}
	
	private URI getECoreURI(URI xmlURI)
	{
		String s = xmlURI.toString();
		int i = s.lastIndexOf('.');
		if (i>=0)
			s = s.substring(0,  i) + ".ecore";
		else
			s += ".ecore";
		
		return URI.createURI(s);
	}
	
	private String getSchemaLocation(String xmiURI) {
		final StringBuffer retVal = new StringBuffer();
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				public void startElement(String uri, String localName,String qName,
			                Attributes attributes) throws SAXException {
					// just reading schemaLocation of the first tag
					retVal.append( attributes.getValue("xsi:schemaLocation") );
					throw new SAXException("break saxParser; attribute found!");
				}
	
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
				}
	
				public void characters(char ch[], int start, int length) throws SAXException {
				}

			};

		    saxParser.parse(xmiURI, handler);
		    return retVal.toString(); // dead code, which the compiler does not understand

	     } catch (Exception e) {
	    	 return retVal.toString();
	     }		
	}
	
	private void createRootPackage()
	{
		if (rootPackage != null)
			return;
		
		// creating empty model...
		rootPackage = theCoreFactory.createEPackage();
		
		String name = new File(openRepositoryURI.toFileString()).getName();
		int i = name.lastIndexOf('.');
		if (i>=0)
			name = name.substring(0, i);
		
		if (name.length() == 0)
			name = "Some";
		else
			name = name.substring(0,1).toUpperCase()+name.substring(1);
		
		rootPackage.setName("root");//name+"Package");
		rootPackage.setNsURI("http:///lv.lumii.tda."+name.toLowerCase()+"."+java.util.UUID.randomUUID().toString());
		rootPackage.setNsPrefix("");//name.toLowerCase());
		
		URI ecoreURI = this.getECoreURI(openRepositoryURI);
		
		this.nameToPackageMap.put("", rootPackage);
		
		if (ecoreToPackagesMap.get(ecoreURI.toString())==null) {
			ecoreToPackagesMap.put(ecoreURI.toString(), new LinkedList<EPackage>());
		}
		
		ecoreToPackagesMap.get(ecoreURI.toString()).add(rootPackage);
		
	}
	
	private void initMetamodelReferences() {
		long l;
		l = getPropertyAsLong("String");
		if (l != 0)
			indexer.set(l, theCorePackage.getEString());
		l = getPropertyAsLong("Integer");
		if (l != 0)
			indexer.set(l, theCorePackage.getEInt());
		l = getPropertyAsLong("Real");
		if (l != 0)
			indexer.set(l, theCorePackage.getEDouble());
		l = getPropertyAsLong("Boolean");
		if (l != 0)
			indexer.set(l, theCorePackage.getEBoolean());
					
		for (EPackage pkg : this.nameToPackageMap.values()) {
			for (EClassifier e : pkg.getEClassifiers()) {
				if (e instanceof EClass) {
					EClass eCls = (EClass)e;
					String name = e.getName();
					l = getPropertyAsLong(name);
					if (l!=0)
						indexer.set(l, e);
					

					for (EStructuralFeature f : eCls.getEStructuralFeatures()) {
						if ((f instanceof EReference) || (f instanceof EAttribute)) {
							l=getPropertyAsLong(name+"."+f.getName());
							if (l!=0)
								indexer.set(l, f);
						}
					}
					
				}
			}
		}
		
	}
	
	private void initModelReferences() {
		long l;
		for (Long rClass : classToObjects.keySet()) {
			ArrayList<EObject> list = classToObjects.get(rClass);
			for (int i=0; i<list.size(); i++) {
				String name = this.getClassName(rClass)+"["+i+"]";
				l = getPropertyAsLong(name);
				if (l!=0)
					indexer.set(l, list.get(i));
				this.deleteProperty(name); // the name may change when objects are deleted/created
				i++;
			}
		}
	}
	
	private boolean saveReferences(OutputStream stream) {
		long l;
		for (Long rClass : classToObjects.keySet()) {
			ArrayList<EObject> list = classToObjects.get(rClass);
			for (int i=0; i<list.size(); i++) {
				String name = this.getClassName(rClass)+"["+i+"]";
				long index = indexer.getIndex(list.get(i));
				this.putPropertyAsLong(name, index);
			}
		}
		try{
			try {
				properties.store(stream, "");
				return true;
			}		
			catch(Throwable t) {
				return false;
			}
		}
		finally {			
			for (Long rClass : classToObjects.keySet()) {
				ArrayList<EObject> list = classToObjects.get(rClass);
				for (int i=0; i<list.size(); i++) {
					String name = this.getClassName(rClass)+"["+i+"]";
					this.deleteProperty(name);
				}
			}			
		}
	}
	
	@Override
	public boolean open(String location)
	{

		if ((location == null) || (openRepositoryURI != null)) 
			return false; 

		if (new File(location).isDirectory())
			location += "/data.xmi";
		
		if (location.indexOf("://")==-1) {
			if (location.startsWith("file:/"))
				location = "file:///"+location.substring(6);
			else
				location = "file:///"+location;
		}
		
		location = location.replace('\\', '/');
		
		boolean needToCreate = (!exists(location));
		
		// in case there is only one .ecore file (without .xmi), the clients may
		// pass the .ecore file; we correct that (if .xmi does not exist, we will
		// assume that there are no objects in the repository)
		if (location.toLowerCase().endsWith(".ecore"))
			location = location.substring(0, location.length()-6)+".xmi";
		
		
		openRepositoryURI = URI.createURI(location);//new File(path).toURI().toString());
		
		System.err.println("location="+location);

		try {
			properties.clear();
			properties.load( new URL(openRepositoryURI.toString()+"_refs").openStream() ) ;
		} catch (IOException e) {
			// perhaps, the xmi_refs file does not exist yet; it's OK
		}
		
		if (needToCreate) {
			createRootPackage();
			return true;
		}
		
		try {
			ResourceSet metaResourceSet = new ResourceSetImpl();
			ResourceSet resourceSet = new ResourceSetImpl();
						
	
			// Registering extensions...
			metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				    "ecore", new  XMIResourceFactoryImpl());
			  //resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			  //  "xml", new XMLResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				"*", new XMIResourceFactoryImpl()); // .xml, .xmi, and others		
	

			// READING THE METAMODELS (CONTAINING PACKAGES...) >>>
			String parentLocation = new File(location).getParent().replace('\\', '/');
			
			String schemaLocation = this.getSchemaLocation(location);
			//System.out.println("SCHEMA LOCATION "+ schemaLocation);
			if (schemaLocation != null) {
				String[] arr = schemaLocation.split("\\s+");
				int i = 0;
				while (i+2 <= arr.length) {
					// process arr[i] and arr[i+1]...
					String key = arr[i];
					String value = arr[i+1];									
					
					if (value.startsWith("file:/") || (value.indexOf(':')==-1)) { // file or file protocol
						// check for the path "as is"...
						File f = new File(value);
						if (!f.exists()) {
							// check for the relative path...
							value = parentLocation+"/"+value;
							f = new File(value);
							if (!f.exists()) {
								// relative file name only...
								value = parentLocation+"/"+f.getName();
							}
						}
						
					}
					
					//System.out.println("MAP: "+key+" -> "+value);
					
					URI valueURI = URI.createURI(value);
					// loading the corresponding package as a resource...
					Resource metaResource =
						metaResourceSet.getResource(valueURI, true);
					
					int n=metaResource.getContents().size();
					for (int j=0; j<n; j++) {
						
						EPackage newPackage = (EPackage)metaResource.getContents().get(j);
						
						// ...and registering the root package (so, while reading the model, we can find this package)
						resourceSet.getPackageRegistry().put(
							    newPackage.getNsURI(),
							    newPackage);
						
						String nsPrefix = newPackage.getNsPrefix(); 			
						// nsPrefix == "data", "root", "rootPackage", or "" -> root
						if ((nsPrefix==null) || nsPrefix.equals("data") || nsPrefix.equals("root") || nsPrefix.equalsIgnoreCase("rootPackage") || nsPrefix.isEmpty()) {
							//System.out.println("loading rootPackage "+valueURI);
							rootPackage = newPackage;
							nameToPackageMap.put("", rootPackage);
							if (ecoreToPackagesMap.get(valueURI.toString())==null) {
								ecoreToPackagesMap.put(valueURI.toString(), new LinkedList<EPackage>());
							}							
							ecoreToPackagesMap.get(valueURI.toString()).add(rootPackage);
						}
						else {
							//System.out.println("loading package "+valueURI);
							nameToPackageMap.put(nsPrefix, newPackage);
							if (ecoreToPackagesMap.get(valueURI.toString())==null) {
								ecoreToPackagesMap.put(valueURI.toString(), new LinkedList<EPackage>());
							}							
							ecoreToPackagesMap.get(valueURI.toString()).add(newPackage);
						}
						
					}
					
					i+=2;
				}
			}
			else {
				
			}
			// <<< READING THE METAMODELS (CONTAINING PACKAGES...)
			
			initMetamodelReferences();
			
			// Reading the model...
			Resource resource = null;
			try {
				resource = resourceSet.getResource(openRepositoryURI,true);
			}
			catch(Throwable t) {
				if (!exists(location)) // .xmi
					resource = null;
				else
					throw t; // .xmi exists, but there was an error, when opening
			}
			if (resource != null) {
				Iterator<EObject> it = resource.getAllContents(); // getting all objects recursively... 
				while (it.hasNext()) {		
					EObject eObj = it.next();
					registerEObject(eObj);                        // ...and registering them in our data structures
				}
			}
			// else assume there is empty repository
			
		}
		catch (Throwable t) {
			t.printStackTrace();
			openRepositoryURI = null;
			rootPackage = null;
			nameToPackageMap.clear();
			ecoreToPackagesMap.clear();
			return false;
		}
		
		initModelReferences();
		
		return true;
	}

	@Override
	public void close()
	{
		indexer = new ReverseObjectsIndexer<Object>();
		classToObjects.clear();
		openRepositoryURI = null;
		properties.clear();
		rootPackage = null;
		nameToPackageMap.clear();
		ecoreToPackagesMap.clear();
	}
	
	@Override
	public boolean exists (String location)
	{
		try {
			if (location.indexOf("://")==-1)
				location = "file:///"+location;
			URL url = new URL(location);
			InputStream t = url.openStream();
			t.close();
			return true;
		} catch (Throwable t) {
			return false;
		}
		
		/*path += File.separator+"jr_data";
		File previousVersion = new File(path+".prev");
		File lastVersion = new File(path);
		return (previousVersion.exists() || lastVersion.exists());*/
//		return new File(path).exists();
	}
	
	private void displayStackTrace()
	{
		try {
			throw new RuntimeException("");
		}
		catch(Throwable t) 
		{
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			JOptionPane.showMessageDialog(null, "Stack trace: \n"+sw.toString());
			//throw t;
		}
		
	}
	
	private void checkFeatures(EObject eObj, EClass eCls)
	{
        for (EObject contained : eObj.eContents()) {
        	try {
            checkFeatures(contained, contained.eClass());
        	}
        	catch(Throwable t) {
				JOptionPane.showMessageDialog(null, "error2 "+eCls.getName());        		
        	}
        }
		for (EStructuralFeature f: eCls.getEStructuralFeatures()) {
			try {
				eObj.eIsSet(f);
			}
			catch(Throwable t) 
			{
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				long it = this.getIteratorForDirectObjectClasses(indexer.getIndex(eObj));
				long rCls = this.resolveIteratorFirst(it);
				this.freeIterator(it);
				JOptionPane.showMessageDialog(null, "error "+eCls.getName()+" "+f.getName()+" "+this.getClassName(rCls)+" "+classToObjects.containsKey(rCls)+"\n"+sw.toString());
				throw t;
			}
		}
	}


	private String uriToFileString(URI uri)
	{
		String retVal = uri.toFileString();
		/*if (retVal == null)
			retVal = uri.toString();
		if (retVal.startsWith("ecore:"))
			retVal = retVal.substring(6);
		if (retVal.startsWith("file://"))
			retVal = retVal.substring(7);*/
		return retVal;
	}
	
	@Override
	public boolean startSave ()
	{
		if (openRepositoryURI == null)
			return false;
		
		URI nextVersion = openRepositoryURI;
		try {			
			File fNextVersion = new File(uriToFileString(nextVersion));
			
			List<File> fNextVersionMMs = new LinkedList<File>();
			List<URI> uriNextVersionMMs = new LinkedList<URI>();
			for (String s : ecoreToPackagesMap.keySet()) {
				URI nextVersionMM = URI.createURI(s);
				File fNextVersionMM = new File(uriToFileString(nextVersionMM));
				fNextVersionMMs.add(fNextVersionMM);
				uriNextVersionMMs.add(nextVersionMM);
			}
			
			
			URI previousVersion = URI.createURI(nextVersion.toString()+".prev");
			File fPreviousVersion = new File(uriToFileString(previousVersion));
			if (fPreviousVersion.exists())
				fPreviousVersion.delete();
			if (fNextVersion.exists()) 
				fNextVersion.renameTo(fPreviousVersion);
			
			URI previousReferences = URI.createURI(openRepositoryURI.toString()+"_refs.prev");
			File fPreviousReferences = new File(uriToFileString(previousReferences));
			if (fPreviousReferences.exists())
				fPreviousReferences.delete();
			URI nextReferences = URI.createURI(openRepositoryURI.toString()+"_refs");
			File fNextReferences = new File(uriToFileString(nextReferences));
			if (fNextReferences.exists()) 
				fNextReferences.renameTo(fPreviousReferences);
			
			List<File> fPreviousVersionMMs = new LinkedList<File>();
			for (String s : ecoreToPackagesMap.keySet()) {
				URI previousVersionMM = URI.createURI(s+".prev");
				File fPreviousVersionMM = new File(uriToFileString(previousVersionMM));				
				fPreviousVersionMMs.add(fPreviousVersionMM);
				if (fPreviousVersionMM.exists())
					fPreviousVersionMM.delete();
				
				URI nextVersionMM = URI.createURI(s);
				File fNextVersionMM = new File(uriToFileString(nextVersionMM));
				if (fNextVersionMM.exists()) 
					fNextVersionMM.renameTo(fPreviousVersionMM);
			}							
						
	
			///// Saving the metamodels... /////
			ResourceSet metaResourceSet = new ResourceSetImpl();
			metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			    "ecore", new  XMIResourceFactoryImpl());
			
			try {
				for (URI uri : uriNextVersionMMs) {
					Resource metaResource =	metaResourceSet.createResource(uri);
					
					for (EPackage pkg : ecoreToPackagesMap.get(uri.toString())) {					
						metaResource.getContents().add(pkg);
					}
					
					if (metaResource instanceof XMLResource)
						((XMLResource) metaResource).setEncoding("UTF-8"); 
			
					Map options = new HashMap();
					options.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);
					options.put(XMLResource.OPTION_SKIP_ESCAPE_URI, Boolean.FALSE);
					options.put(XMLResource.OPTION_SKIP_ESCAPE, Boolean.FALSE);
					metaResource.save(options);
				}
			}
			catch (IOException e) {
				System.err.println(e.getMessage());
				if (fPreviousVersion.exists()) 
					fPreviousVersion.renameTo(fNextVersion);
				
				if (fPreviousReferences.exists()) 
					fPreviousReferences.renameTo(fNextReferences);
				
				Iterator<File> it1 = fPreviousVersionMMs.iterator();
				Iterator<File> it2 = fNextVersionMMs.iterator();
				while (it1.hasNext()) {
					File fPreviousVersionMM = it1.next();
					File fNextVersionMM = it2.next();
					if (fPreviousVersionMM.exists()) 
						fPreviousVersionMM.renameTo(fNextVersionMM);
				}
				return false;
			}
			
			///// Saving the model... /////
			ResourceSet resourceSet = new ResourceSetImpl();
			/*resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
			    "xml", new  XMLResourceFactoryImpl());*/
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
				"*", new  XMIResourceFactoryImpl());
			
			for (EPackage pkg : this.nameToPackageMap.values()) {
				System.out.println("SAVE PKG "+pkg.getNsURI()+" `"+pkg.getName()+"' vs root=`"+rootPackage.getName()+"'");
				resourceSet.getPackageRegistry().put(
						pkg.getNsURI(),
						pkg);
			}
	
			Resource resource = resourceSet.createResource(nextVersion);
			//resource.getContents().add(rootPackage);
			for (Long rCls : classToObjects.keySet()) {
				for (EObject eObj : classToObjects.get(rCls)) {
					
					
					if ((eObj.eContainer() == null) || (eObj.eResource()==null)) {
						EClass eCls = (EClass)indexer.getObject(rCls);						
						
						if (eCls != null) {
							if (!resource.getContents().contains(eCls)) {
									resource.getContents().add(eCls);
							}
						}
						
						if (!resource.getContents().contains(eObj))
							resource.getContents().add(eObj);
					}							
				}
				
			}
			

			if (resource instanceof XMLResource)
				((XMLResource) resource).setEncoding("UTF-8"); 
			Map options = new HashMap();
			options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			options.put(XMLResource.OPTION_ESCAPE_USING_CDATA, Boolean.TRUE);
			options.put(XMLResource.OPTION_SKIP_ESCAPE_URI, Boolean.FALSE);
			options.put(XMLResource.OPTION_SKIP_ESCAPE, Boolean.FALSE);
			try{
				resource.save(options);
			}
			catch (Throwable t) {
				System.err.println("REPO SAVE ERROR2");
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				System.err.println(sw.toString());
				
				if (fPreviousVersion.exists()) 
					fPreviousVersion.renameTo(fNextVersion);

				if (fPreviousReferences.exists()) 
					fPreviousReferences.renameTo(fNextReferences);
				
				Iterator<File> it1 = fPreviousVersionMMs.iterator();
				Iterator<File> it2 = fNextVersionMMs.iterator();
				while (it1.hasNext()) {
					File fPreviousVersionMM = it1.next();
					File fNextVersionMM = it2.next();
					if (fPreviousVersionMM.exists()) 
						fPreviousVersionMM.renameTo(fNextVersionMM);
				}

				return false;				
			}
			
			///// Saving the references... /////

			OutputStream out = null;
			try {
				try {
					out = new FileOutputStream(fNextReferences);
					this.saveReferences( out );
				}
				catch (Throwable t) {				
					System.err.println(t.getMessage());
					if (fPreviousVersion.exists()) 
						fPreviousVersion.renameTo(fNextVersion);
					
					if (fPreviousReferences.exists()) 
						fPreviousReferences.renameTo(fNextReferences);
					
					Iterator<File> it1 = fPreviousVersionMMs.iterator();
					Iterator<File> it2 = fNextVersionMMs.iterator();
					while (it1.hasNext()) {
						File fPreviousVersionMM = it1.next();
						File fNextVersionMM = it2.next();
						if (fPreviousVersionMM.exists()) 
							fPreviousVersionMM.renameTo(fNextVersionMM);
					}
					return false;
				}
			}
			finally {
				if (out != null)
					out.close();
			}
			
			
			
			/*if (openRepositoryURI.toString().toLowerCase().endsWith(".xml") && (resource.getContents().size()>1)) {
				System.err.println("Could not save the ECore model to "+openRepositoryURI.toString()
						+" in XML format since the model does not form a tree.");
				if (fPreviousVersion.exists()) 
					fPreviousVersion.renameTo(fNextVersion);
				if (fPreviousVersionMM.exists()) {
					fNextVersionMM.delete();
					fPreviousVersionMM.renameTo(fNextVersionMM);
				}
				return false;
			}*/
			
			
			return true;
		}
		catch (Throwable t) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			System.err.println(sw.toString());
			System.err.println(t.getMessage());
			return false;
		}
	}

	@Override
	public boolean finishSave ()
	{
		if (openRepositoryURI == null)
			return false;
		URI previousVersion = URI.createURI(openRepositoryURI.toString()+".prev");
		File fPreviousVersion = new File(uriToFileString(previousVersion));
		if (fPreviousVersion.exists()) 
			fPreviousVersion.delete();
		
		URI previousReferences = URI.createURI(openRepositoryURI.toString()+"_refs.prev");
		File fPreviousReferences = new File(uriToFileString(previousReferences));
		if (fPreviousReferences.exists()) 
			fPreviousReferences.delete();

		for (String s : ecoreToPackagesMap.keySet()) {
			URI previousVersionMM = URI.createURI(s+".prev");
			File fPreviousVersionMM = new File(uriToFileString(previousVersionMM));				
			if (fPreviousVersionMM.exists()) 
				fPreviousVersionMM.delete();
		}							
		
		return true;
	}

	@Override
	public boolean cancelSave ()
	{
		if (openRepositoryURI == null)
			return false;
		URI nextVersion = openRepositoryURI;
		File fNextVersion = new File(uriToFileString(nextVersion));

		//URI nextVersionMM = getECoreURI(openRepositoryURI);
		//File fNextVersionMM = new File(uriToFileString(nextVersionMM));
		
		URI previousVersion = URI.createURI(openRepositoryURI.toString()+".prev");
		File fPreviousVersion = new File(uriToFileString(previousVersion));
		//URI previousVersionMM = URI.createURI(this.getECoreURI(openRepositoryURI).toString()+".prev");
		//File fPreviousVersionMM = new File(uriToFileString(previousVersionMM));
		
		
		URI previousReferences = URI.createURI(openRepositoryURI.toString()+"_refs.prev");
		File fPreviousReferences = new File(uriToFileString(previousReferences));
		URI nextReferences = URI.createURI(openRepositoryURI.toString()+"_refs");
		File fNextReferences = new File(uriToFileString(nextReferences));
		
		
		boolean ok = true;
		if (fPreviousVersion.exists()) { 
			fNextVersion.delete();
			if (!fPreviousVersion.renameTo(fNextVersion))
				ok = false;
		}
		else
			ok = false;
		
		if (fPreviousReferences.exists()) { 
			fNextReferences.delete();
			if (!fPreviousReferences.renameTo(fNextReferences))
				ok = false;
		}
		
		for (String s : ecoreToPackagesMap.keySet()) {
			URI nextVersionMM = URI.createURI(s);
			File fNextVersionMM = new File(uriToFileString(nextVersionMM));
			URI previousVersionMM = URI.createURI(s+".prev");
			File fPreviousVersionMM = new File(uriToFileString(previousVersionMM));
			
			if (fPreviousVersionMM.exists()) { 
				fNextVersionMM.delete();
				if (!fPreviousVersionMM.renameTo(fNextVersionMM))
					ok = false;
			}
			else
				ok = false;
		}							
		
		
		return ok;
	}
	
	@Override
	public boolean drop(String location) {
		try {
			if (location.indexOf("://")==-1)
				location = "file:///"+location;
			URI uri = URI.createURI(location);
			File f1 = new File(uriToFileString(uri));
			File f2 = new File(uriToFileString(getECoreURI(uri)));
		
			return (!f1.exists() || f1.delete()) && (!f2.exists() || f2.delete());
			// we delete only the .ecore file that corresponds to the given .xmi file
			// other referenced .ecore files are not deleted
		}
		catch (Throwable t)
		{
			return false;
		}
	}
	
	/*
	private void updateObjects(long rClass)
	{
		Object cls = indexer.getObject(rClass);
		if (!(cls instanceof EClass))
			return;
		EClass eCls = (EClass)cls;
		
		IRepository repo = this.parentDelegator;
		if (repo == null)
			repo = this;
		
		long it = repo.getIteratorForDirectSubClasses(rClass);
		long rSubCls = repo.resolveIteratorFirst(it);
		while (rSubCls != 0) {
			updateObjects(rSubCls);
			repo.freeReference(rSubCls);
			rSubCls = repo.resolveIteratorNext(it);
		}
		repo.freeIterator(it);

		//
		ArrayList<EObject> list = classToObjects.remove(rClass);
		if (list == null) {
			return;
		}
		for (EObject eObj : list) {
			
			EObject eObjNew = rootPackage.getEFactoryInstance().create(eCls);
			indexer.set(indexer.getIndex(eObj), eObjNew);
			
			EList<EStructuralFeature> elist = eCls.getEStructuralFeatures();
			for (EStructuralFeature f : elist) {
				try {
					eObjNew.eSet(f, eObj.eGet(f));
					if (f instanceof EReference) {
						// cyclic references
						List l = (List)eObjNew.eGet(f);
						if (l.contains(eObj)) {
							int i = l.indexOf(eObj);
							l.remove(eObj);
							if (!l.contains(eObjNew))
								l.add(i, eObjNew);
						}
					}
				}
				catch(Throwable t){
					// nothing; do not set
				}				
			}
			
			List<EObject> mylist = new LinkedList<EObject>();
			for (EObject o : eObj.eContents())
				mylist.add(o);
			
			//eObj.eContents().clear();
			for (EObject eobj : mylist) {
				System.out.println("EOBJ ADD " + eObjNew + " "+eCls.getName()+" "+eobj.eContainer());
				//eObj.eContents().remove(eobj);
				if (eobj.eContainer() == null)
					this.theCorePackage.eContents().add(eobj);
				//eObjNew.eContents().add(eobj);
			}
			
			/*try {
			eObjNew.eContents().addAll(eObj.eContents());
			}
			catch(Throwable t) {
				
			}*\
			
			EObject container = eObj.eContainer();
			if (container != null) {
				//container.eContents().remove(eObj);
				//container.eContents().add(eObjNew);
				System.err.println("W/O CONTAINER1 "+eObjNew);
			}
			else {
				System.err.println("W/O CONTAINER "+eObjNew);
			}
			
			// for: reverse features
			for (EStructuralFeature f : elist/*eCls.getEStructuralFeatures()*\) {
				if (!(f instanceof EReference))
					continue;
				EReference eInvAssoc = ((EReference)f).getEOpposite();
				if (eInvAssoc == null)
					continue;
				EClass c = eInvAssoc.getEContainingClass();
				ArrayList<EObject> objects = classToObjects.get(indexer.getIndex(c));
				if (objects == null)
					continue;
				for (EObject o : objects) {
					try {
					List l = (List)o.eGet(eInvAssoc);
					if (l.contains(eObj)) {
						int i = l.indexOf(eObj);
						l.remove(eObj);
						if (!l.contains(eObjNew))
							l.add(i, eObjNew);
					}
					}
					catch(Throwable t) {
						// eInvAssoc does not exist, then ignore...
					}
				}
			}
			
			registerEObject(eObjNew);
			
			long r = indexer.getIndex(eObj);
			
			lv.lumii.tda.kernel.DelegatorToRepositoryWithCascadeDelete.cascadeDeleteObject(r, repo, true, true);
			
			indexer.set(r, eObjNew);
			//EcoreUtil.delete(eObj, true);
			//EcoreUtil.remove(eObj);
		}
		
	}
	*/
	private void registerEObject(EObject eObj)
	{
		long rClass = indexer.getIndex(eObj.eClass());
		if (rClass == 0)
			return; // something wrong...
		ArrayList<EObject> list = classToObjects.get(rClass);
		if (list == null) {
			list = new ArrayList<EObject>();
			classToObjects.put(rClass, list);
		}
		list.add(eObj);
	}
	
	private void unregisterEObject(EObject eObj)
	{
		long rClass = indexer.getIndex( eObj.eClass() );
		if (rClass == 0)
			return; // something wrong...
		ArrayList<EObject> list = classToObjects.get(rClass);
		if (list != null) {
			list.remove(eObj);
			if (list.isEmpty())
				classToObjects.remove(rClass);
		}
		
	}
}
