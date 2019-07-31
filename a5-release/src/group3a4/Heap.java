package group3a4;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
/**
 * Heap implements a data structure which stores elements in complete binary tree, 
 * where elements in the tree are ordered such that each node is less than its parent 
 * according to a given comparator and associated priorities
 *
 * @param <E> the element type
 * @param <P> the priority type, should be able to be compared with your comparable
 */
public class Heap<E,P> implements PriorityQueue<E, P> {
	ArrayList<E> list; //ArrayList list contains the index and priorities of the heap
	HashMap<E,P> map; //HashMap map contains the index and elements of the heap
	Comparator<? super P> comp; // Comparator comp is the comparators used to compare the priority values of the heap
	int size; // Field to store the size of the arrayList list
	
	/**Constructor for Heap class: Creates an empty heap with no objects*/
	public Heap(Comparator<? super P> c) {
		list = new ArrayList<E>();
		map = new HashMap<E,P>();
		comp = c;
		size=0;
	}
	
	/**
	 * creates a new heap from lists of values and priorities
	 * @param c the comparator to be used to compare priorities
	 * @param values the values to be stored
	 * @param priorities the priorities associated with each value to be added, must obviously be the same length as E, or throws an exception 
	 */
	public Heap(Comparator<? super P> c, ArrayList<E> values, ArrayList<P> priorities) {
		if(values.size() != priorities.size()) {throw new InvalidParameterException();}
		list = new ArrayList<E>(10);
		map = new HashMap<E,P>();
		comp = c;
		size=0;
		for(int i = 0; i < values.size(); i++) {
			add(values.get(i), priorities.get(i));
		}
	}
	
	@Override
	public Comparator<? super P> comparator() {
		return comp;
	}

	@Override
	/**
	 * returns the number of elements contained within the heap
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * returns the depth of the heap
	 */
	public int getDepth() {
		return depthHelper(0, size());
	}
	
	//recursively finds the depth of the heap from its size
	private int depthHelper(int depth, int size) {
		int exp = 1;
		for(int i = 0; i < depth; i++) {exp = exp*2;}
		if (exp > size) {
			return depth;
		}
		return depthHelper(depth + 1, size-exp);
	}

	/**
	 * pulls the highest priority element from the heat, and returns it
	 */
	@Override
	public E poll() throws NoSuchElementException {
		E e;
		if (size() == 0) {throw new NoSuchElementException();}
		
		if (size() == 1) {
			e = list.get(0);
			list.remove(0);
			map.clear();
			size = size - 1;
		}
		else {
			e = list.get(0);
			Collections.swap(list,0,size()-1);
			list.remove(size()-1);
			map.remove(e);
			size = size - 1;
			bubbleDown(0);
		}
		return e;
	}

	private void bubbleDown(int i) {
		// TODO Auto-generated method stub
	//assert i >= 0 && i < size;
	
	int c = getBiggerChild(i);
	while(c < size() && (comp.compare(map.get(list.get(i)),map.get(list.get(c))) < 0)) {
		Collections.swap(list, c, i);
		i=c;
		
		if (this.left(c)<size()) {
		c = getBiggerChild(c);}
		}
	}

	private int getBiggerChild(int i) {
		// TODO Auto-generated method stub
		if (this.left(i)> size() ) {return size();}
		int r = this.left(i);
		if (this.right(i) >= size()) {return r;}
		else if (comp.compare(map.get(list.get(r)),map.get(list.get(this.right(i)))) == -1){
			return this.right(i);
		}
		return r;
	}

	/**
	 * returns the highest priority element from the heap without removing it
	 */
	@Override
	public E peek() throws NoSuchElementException {
		if (size() == 0) {
			throw new NoSuchElementException();
		}
		return list.get(0);
	}

	/**
	 * adds a new element with given priority to the heap
	 * @param e the element to add, throws an exception if already contained in the heap
	 * @param p the priority of the added element
	 */
	@Override
	// add the element, add the corresponding priority in the has table, then update the size parameter, then bubble up
	public void add(E e, P p) throws IllegalArgumentException {
		if (this.contains(e)) {
			throw new IllegalArgumentException();
		}
		list.add(e);
		map.put(e, p);
		//size += 1;
		bubbleUp(list.size() - 1);
	}
	
	private void bubbleUp(int index) {
		P itemP = map.get(list.get(index));
		P parentP = map.get(list.get(parent(index)));
		
		if (parent(index)<0){
			return;
		}
		while(index>0 & comp.compare(itemP, parentP) > 0) {
			Collections.swap(this.list, index, parent(index));
			index = parent(index);
			itemP = map.get(list.get(index));
			parentP = map.get(list.get(parent(index)));
		}
	}

	/**
	 * changes the priority of an element e to p,  throws an exception if e cannot be found within the heap
	 * @param e the element to be changed
	 * @param p the priority to be changed to 
	 */
	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if(!this.contains(e)) {throw new NoSuchElementException();}
		map.put(e, p);
		int index = list.indexOf(e);
		bubbleUp(index);
		bubbleDown(index);
	}
	
	private int left(int i) {
		return 2*i + 1;
	}
	
	private int right(int i) {
		return 2*i +2;
	}
	
	private int parent(int i) {
		return (i-1)/2;
	}
	
	/**
	 * creates a shallow copy of the object
	 */
	public Heap<E,P> clone(){
		Heap<E,P> copy = new Heap<E, P>(this.comp);
		for(int i = 0; i < size(); i++) {
			E store = list.get(i);
			copy.add(store, map.get(store));
		}
		return copy;
	}
	
	/**
	 * returns the priority of the given element
	 * @param e the desired element, throws exception if not in heap
	 * @return the priority of e
	 */
	public P getPriority(E e) throws NoSuchElementException{
		if (!contains(e)) {throw new NoSuchElementException();}
		return map.get(e);
	}
	
	/**
	 * determines if the heap contains a given element
	 * @param e the element to be searched for
	 * @return true if the heap contains e, false otherwise
	 */
	public boolean contains(E e) {
		if(list.contains(e)) {
			return true;
		}
		return false;
	}

	/**
	 * outputs a list of the priorities used in the Heap
	 * @return a list of all the priorities used in the Heap so far (no repeats)
	 */
	public Collection<P> getListOfPriorities() {
		return map.values();
	}

}
