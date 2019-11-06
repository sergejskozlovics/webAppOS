// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class TDAKernelMetamodel_RAAPILinkedObjectsList<E extends RAAPIReferenceWrapper> implements List<E> {

	private TDAKernelMetamodelFactory factory;
	private long rSourceObject;
	private long rAssociationEnd;
	TDAKernelMetamodel_RAAPILinkedObjectsList(TDAKernelMetamodelFactory _factory, long _rSourceObject, long _rAssociationEnd)
	{
		factory = _factory;
		rSourceObject = _rSourceObject;
		rAssociationEnd = _rAssociationEnd;
	}
	
	private ArrayList<Long> getLinkedObjects()
	{
		ArrayList<Long> retVal = new ArrayList<Long>(); 
		long it = factory.raapi.getIteratorForLinkedObjects(rSourceObject, rAssociationEnd);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
			retVal.add(r);
			r =factory.raapi.resolveIteratorNext(it);
		}		
		factory.raapi.freeIterator(it);
		return retVal;
	}
	private void freeReferences(Collection<Long> c)
	{
		for (Long l : c)
			factory.raapi.freeReference(l);
	}
	
	@Override
	public boolean add(E e) {
		if (e.getRAAPI() != factory.raapi)
			return false;
		return factory.raapi.createLink(rSourceObject, e.getRAAPIReference(), rAssociationEnd);		
	}

	@Override
	public void add(int index, E element) {
		if (element.getRAAPI() != factory.raapi)
			return;
		if (index < 0)
			factory.raapi.createLink(rSourceObject, element.getRAAPIReference(), rAssociationEnd);
		else
			factory.raapi.createOrderedLink(rSourceObject, element.getRAAPIReference(), rAssociationEnd, index);		
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean ok = true;
		for (E element : c)
			if (!add(element))
				ok = false;
		return ok;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0)
			return addAll(c);
		
		ArrayList<Long> list = getLinkedObjects();
		ArrayList<Long> listToFree = new ArrayList<Long>(list);
		clear();
		for (E element : c) {
			list.add(index, element.getRAAPIReference());
			index++;
		}
		boolean ok = true;
		for (Long l : list)
			if (!factory.raapi.createLink(rSourceObject, l, rAssociationEnd))
				ok = false;
		
		freeReferences(listToFree);
		return ok;
	}

	@Override
	public void clear() {
		for (Long l : getLinkedObjects())
			factory.raapi.deleteLink(rSourceObject, l, rAssociationEnd);
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof RAAPIReferenceWrapper))
			return false;
		if (((RAAPIReferenceWrapper)o).getRAAPI() != factory.raapi)
			return false;
		ArrayList<Long> list = getLinkedObjects();
		boolean retVal = list.contains( ((RAAPIReferenceWrapper)o).getRAAPIReference() );
		freeReferences(list);
		return retVal;
	}

	@Override
	public boolean containsAll(Collection<?> c) {		
		ArrayList<Long> list = getLinkedObjects();
		
		Collection<Long> c2 = new ArrayList<Long>();
		for (Object o : c) {
			if (!(o instanceof RAAPIReferenceWrapper))
				continue;
			if (((RAAPIReferenceWrapper)o).getRAAPI() != factory.raapi)
				continue;
			c2.add(((RAAPIReferenceWrapper)o).getRAAPIReference());
		}
		
		boolean retVal = list.containsAll(c2);
		freeReferences(list);
		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		long it = factory.raapi.getIteratorForLinkedObjects(rSourceObject, rAssociationEnd);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIterator(it, index);
		factory.raapi.freeIterator(it);
		E retVal = (E)factory.findOrCreateRAAPIReferenceWrapper(r, true);
		if (retVal == null)
			retVal = (E)factory.findOrCreateRAAPIReferenceWrapper(factory.findClosestType(r), r, true);
		return retVal;
	}

	@Override
	public int indexOf(Object o) {
		if (!(o instanceof RAAPIReferenceWrapper))
			return -1;
		if (((RAAPIReferenceWrapper)o).getRAAPI() != factory.raapi)
			return -1;
		ArrayList<Long> list = getLinkedObjects();
		int retVal = list.indexOf( ((RAAPIReferenceWrapper)o).getRAAPIReference() );
		freeReferences(list);
		return retVal;
	}

	@Override
	public boolean isEmpty() {
		long it = factory.raapi.getIteratorForLinkedObjects(rSourceObject, rAssociationEnd);
		if (it == 0)
			return true;
		long r = factory.raapi.resolveIteratorFirst(it);
		if (r != 0)
			factory.raapi.freeReference(r);
		factory.raapi.freeIterator(it);
		return (r==0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
		ArrayList<E> retVal = new ArrayList<E>();
		for (Long l : getLinkedObjects()) {
			E obj = (E)factory.findOrCreateRAAPIReferenceWrapper(l, true);
			if (obj == null)
				obj = (E)factory.findOrCreateRAAPIReferenceWrapper(factory.findClosestType(l), l, true);
			if (obj != null)
				retVal.add(obj);
		}
		return retVal.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return indexOf(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListIterator<E> listIterator() {
		ArrayList<E> retVal = new ArrayList<E>();
		for (Long l : getLinkedObjects()) {
			E obj = (E)factory.findOrCreateRAAPIReferenceWrapper(l, true);
			if (obj == null)
				obj = (E)factory.findOrCreateRAAPIReferenceWrapper(factory.findClosestType(l), l, true);
			if (obj != null)
				retVal.add(obj);
		}
		return retVal.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		ListIterator<E> retVal = listIterator();
		for (int i=0; i<index-1; i++)
			if (retVal.hasNext())
				retVal.next();
		return retVal;
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof RAAPIReferenceWrapper))
			return false;
		if (((RAAPIReferenceWrapper)o).getRAAPI() != factory.raapi)
			return false;
		return factory.raapi.deleteLink(rSourceObject, ((RAAPIReferenceWrapper)o).getRAAPIReference(), rAssociationEnd);
	}

	@Override
	public E remove(int index) {
		E o = get(index);
		if (o == null)
			return null;
		if (remove(o))
			return o;
		else
			return null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ok = true;
		for (Object o : c)
			if (!remove(o))
				ok = false;
		return ok;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		clear();
		boolean ok = true;
		for (Object o : c) {
			if (o instanceof RAAPIReferenceWrapper) { // TODO: check for E more precisely				
				if (!add((E)o))
					ok = false;
			}
			else
				ok = false;
		}
		return ok;
	}

	@Override
	public E set(int index, E element) {
		E old = remove(index);
		if (old == null)
			return null;
		add(index, element);
		return old;
	}

	@Override
	public int size() {
		long it = factory.raapi.getIteratorForLinkedObjects(rSourceObject, rAssociationEnd);
		if (it == 0)
			return 0;
		int retVal = factory.raapi.getIteratorLength(it);
		factory.raapi.freeIterator(it);
		return retVal;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("subList not implemented for RAAPI wrappers");
	}

	@Override
	public Object[] toArray() {
		return toArray(new Object[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		ArrayList<Long> list = getLinkedObjects();				
		ArrayList<T> list2 = new ArrayList<T>();
		for (Long l : list) {
			T o = (T)factory.findOrCreateRAAPIReferenceWrapper(l, true);
			if (o == null) {
				try {
					o = (T)factory.findOrCreateRAAPIReferenceWrapper(
							(Class<? extends RAAPIReferenceWrapper>)getClass().getMethod("toArray", java.lang.reflect.Array.class).getGenericParameterTypes()[0],
							l, true);
				} catch (Throwable t) {
					o = null;
				}
			}
			if (o != null)
				list2.add(o);
			else
				factory.raapi.freeReference(l);
		}
		return list2.toArray(a);
	}

}
