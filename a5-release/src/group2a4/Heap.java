package group2a4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Heap<E,P> implements PriorityQueue<E,P>{
	
	/**
	 * Added this method to Group 2's Heap implementation. 
	 * Returns whether the Heap contains Element e.
	 */
	public boolean contains(E e) {
		return map.containsKey(e);
	}
	
	/**
	 * Added this method to Group 2's Heap implementation. 
	 * Returns the Priority of Element e.
	 */
	public P getPriority (E e) {
		if(!(this.map.containsKey(e)))
			throw new NoSuchElementException("Heap does not contain e.");
		return tree.get(map.get(e)).priority;
	}

	private int size;
	private HashMap<E,Integer> map;
	private ArrayList<Node> tree;
	private Comparator<P> comp;
	
	public class Node{
		E value;
		P priority;
		
		/** Creates a new node to be inserted into the heap
		 * 
		 * @param e Value of the node
		 * @param p Priority of the node
		 */
		public Node(E e, P p) {
			this.value = e;
			this.priority = p;
		}
	}
	
	/** Creates a new empty heap
	 * 
	 * @param c The comparator to be used in this heap
	 */
	public Heap(Comparator<P> c) {
		comp = c;
		map = new HashMap<E, Integer>();
		tree = new ArrayList<Node>();
	}
	
	/** Returns the comparator of the heap
	 * 
	 */
	@Override
	public Comparator<? super P> comparator() {
		return comp;
	}

	/** Returns the size of the heap
	 * Cannot be less than 0
	 */
	@Override
	public int size() {
		return this.size;
	}

	/** Removes and returns the value of the lowest priority node of the heap
	 * 
	 * @throws NoSuchElementException when heap size == 0
	 */
	@Override
	public E poll() throws NoSuchElementException {
		if(size == 0) {
			throw new NoSuchElementException();
		}
		else {
			E e = tree.get(0).value;
			tree.remove(0);
			this.size--;
			swap(0, this.size - 1);
			map.remove(e);
			if(this.size != 1) {
				siftDown(0);
			}
			return e;
		}
	}

	/** Returns the value of the lowest priority node of the heap
	 * 
	 * @throws NoSuchElementException when heap size == 0
	 */
	@Override
	public E peek() throws NoSuchElementException {
		if(this.size == 0) {
			throw new NoSuchElementException();
		}
		else {
			return tree.get(0).value;
		}
	}


	/** Adds the node with the given value and priority to the heap and sorts the heap
	 * 
	 * @param e The value of the node to be added
	 * @param p The priority of the node to be added
	 * 
	 * @throws IllegalArgumentException when the heap already contains the same value
	 */
	public @Override void add(E e, P p) throws IllegalArgumentException {
		if(map.containsKey(e)) {
			throw new IllegalArgumentException();
		}
		else {
			tree.add(new Node(e, p));
			map.put(e, this.size);
			this.size++;
			if(this.size > 1)
				siftUp(this.size - 1);
		}
		
	}
	
	/** Swaps two given nodes in the heap
	 * 
	 * @param firstNode The first node to be swapped
	 * @param secondNode The second node to be swapped
	 * 
	 * @throws IllegalArgumentException when either node is not within the bounds of the heap
	 */
	private void swap(int firstNode, int secondNode) throws IllegalArgumentException {
		if(firstNode < 0 || firstNode > tree.size() - 1 || secondNode < 0 || secondNode > tree.size() - 1) {
			throw new IllegalArgumentException();
		} else {
			Node temp = new Node(tree.get(firstNode).value, tree.get(firstNode).priority);
			tree.get(firstNode).value = tree.get(secondNode).value;
			tree.get(firstNode).priority = tree.get(secondNode).priority;
			map.put(tree.get(firstNode).value, firstNode);
			tree.get(secondNode).value = temp.value;
			tree.get(secondNode).priority = temp.priority;
			map.put(tree.get(secondNode).value, secondNode);
		}
	}

	/** Changes the priority of the given node
	 * 
	 * @param e The value of the node whose priority is to be changed
	 * @param p The new priority of the given node
	 * 
	 * @throws NoSuchElementException when the heap does not contain a node with the given value
	 */
	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if(map.containsKey(e) == false) {
			throw new NoSuchElementException();
		} else {
			int index = map.get(e);
			P oldP = tree.get(index).priority;
			tree.get(index).priority = p;
			if(comp.compare(oldP, p) < 0) {
				siftUp(index);
			} else if(comp.compare(oldP, p) > 0) {
				siftDown(index);
			}
		}
	}
	
	/** Sorting method for sorting a node up the heap towards the root
	 * 
	 * @param index The index of the node to be sorted in the ArrayList
	 */
	private void siftUp(int index) {
		int parentNode = (index - 1) / 2;
		while((parentNode >= 0) && comp.compare(tree.get(index).priority, tree.get(parentNode).priority) > 0) {
			swap(index, parentNode);
			index = parentNode;
			parentNode = (index - 1) / 2;
		}
	}
	
	/** Sorting method for sorting a node down the heap away from the root
	 * 
	 * @param index The index of the node to be sorted in the ArrayList
	 */
	private void siftDown(int index) {
		int largerChild = ((index * 2) + 2);
		if(this.size - 1 > ((index * 2) + 2) && comp.compare(tree.get(largerChild - 1).priority, tree.get(largerChild).priority) > 0) {
			largerChild--;
		}
		while(this.size - 1 > ((index * 2) + 1) && comp.compare(tree.get(index).priority, tree.get(largerChild).priority) < 0) {
			swap(index, largerChild);
			index = largerChild;
			largerChild = ((index * 2) + 2);
			if(this.size - 1 > ((index * 2) + 2) && comp.compare(tree.get(largerChild - 1).priority, tree.get(largerChild).priority) > 0) {
				largerChild--;
			}
		}
	}
}
