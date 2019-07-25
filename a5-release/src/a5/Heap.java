package a5;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

import junit.framework.AssertionFailedError;

public class Heap<E, P> implements PriorityQueue<E, P>{
	
	//store the data of the heap in an ArrayList
	private ArrayList<E> data;
	
	//the comparator for the priority of the elements
	private Comparator<P> comparator;
	
	//the HashMap storing the location of each element
	private HashMap<E, Integer> locationMap;
	
	//the HashMap storing the priority of the element at given index
	private HashMap<Integer, P> priorityMap;

	public Heap(Comparator<P> comparator) {
		this.comparator = comparator;
		this.data = new ArrayList<E>();
		locationMap = new HashMap<E, Integer>();
		priorityMap = new HashMap<Integer, P>();
	}
	
	@Override
	/**
	 * @return the comparator of the PriorityHeap
	 */
	public Comparator<P> comparator() {
		return comparator;
	}

	/**
	 * @return the size of the heap
	 */
	@Override
	public int size() {
		return data.size();
	}
	
	/** remove and return the largest element of this.
	 * @throws NoSuchElementException if this is empty.
	 */
	@Override
	public E poll() throws NoSuchElementException {
		E maxEle = peek();
		swap(0, size() - 1);
		data.remove(size() - 1);
		locationMap.remove(maxEle);
		priorityMap.remove(size());
		bubbleDown(0);
		return maxEle;
	}
	
	/**
	 * return the largest element of this, according to comparator().
	 * @throws NoSuchElementException if this is empty.
	 */
	@Override
	public E peek() throws NoSuchElementException {
		if(size() == 0)
			throw new NoSuchElementException();
		
		return data.get(0);
	}
	
	@Override
	public void add(E e, P p) throws IllegalArgumentException {
		//throw if e is already contained.
		if(locationMap.containsKey(e))
			throw new IllegalArgumentException();

		int index = data.size();
		data.add(e);
		locationMap.put(e, index);
		priorityMap.put(index, p);
		bubbleUp(index);
	}
	
	/**
	 * Change the priority associated with e to p.
	 *
	 * @throws NoSuchElementException if this does not contain e.
	 */
	@Override
	public void changePriority(E e, P p) throws NoSuchElementException {
		if(!(this.data.contains(e)))
			throw new NoSuchElementException("ArrayList does not contain e.");
		int index = locationMap.get(e);
		P prevPrio = priorityMap.get(index);
		priorityMap.replace(index, p);
		if(comparator.compare(prevPrio, p)>0) {
			bubbleUp(index);
		}
		else if(comparator.compare(prevPrio, p)<0) {
			bubbleDown(index);
		}
		
	}
	
	/**
	 * make sure that the element at the given index satisfies the heap invariant
	 * and check whether the element can rise to a lower level
	 * @param index
	 */
	private void bubbleDown(int index) {
		while(left(index) != -1 || right(index) != -1) {
			int indexLeft = left(index);
			int indexRight = right(index);
			P curEle = priorityMap.get(index);
			P eleLeft = priorityMap.get(indexLeft);
			P eleRight = priorityMap.get(indexRight);
			if((eleLeft != null && comparator.compare(curEle, eleLeft) < 0)
					|| (eleRight != null && comparator.compare(curEle, eleRight) < 0)) {
				if(eleLeft != null && (eleRight == null || comparator.compare(eleLeft, eleRight) > 0)) {
					swap(index, indexLeft);
					index = indexLeft;
				}
				else {
					swap(index, indexRight);
					index = indexRight;
				}
			}
			else {
				break;
			}
		}
	}
	
	/**
	 * make sure that the element at the given index satisfies the heap invariant
	 * and check whether the element can rise to an upper level
	 * @param index
	 */
	private void bubbleUp(int index) {
		for(;parent(index)!= -1; index = parent(index)) 
			if(comparator.compare(priorityMap.get(index), priorityMap.get(parent(index))) > 0)
				swap(index, parent(index));
			else
				break;
	}
	
	/** returns depth of item at index i
	 * 
	 * @param i < data.size();
	 * @return integer
	 * @throws IndexOutOfBoundsException
	 */
	private int depth(int index) throws IndexOutOfBoundsException{
		if(index > size())
			throw new IndexOutOfBoundsException();
		
		return Integer.toBinaryString(index + 1).length();
	}
	
	/**
	 * swap the elements at given indexes
	 * @param a the index of the first element
	 * @param b the index of the other element
	 */
	private void swap(int a, int b) {
		E tempA = data.get(a);
		P tempAPrior = priorityMap.get(a);
		System.out.println("swap " + tempA + " with " + data.get(b));
		priorityMap.replace(a, priorityMap.get(b));
		priorityMap.replace(b, tempAPrior);
		locationMap.replace(tempA, b);
		locationMap.replace(data.get(b), a);
		data.set(a, data.get(b));
		data.set(b, tempA);
	}
	
	/** returns number of element at depth of item at index i
	 * 
	 * @param i < data.size()
	 * @return -1 if the index is invalid
	 */
	private int noOfElements(int index){
		return (int)(Math.pow(2, depth(index) - 1));
	}
	
	/** returns index of left child of item at index i
	 * 
	 * @param i < data.size()
	 * @return -1 if the index is invalid
	 */
	private int left(int index){
		if(index < 0 || index >= size())
			return -1;
		
		int temp = 2 * index + 1;
		return (temp < size()) ? temp : -1;
	}
	
	/** returns index of right child of item at index i
	 * 
	 * @param i < data.size()
	 * @return -1 if the index is invalid
	 */ 
	private int right(int index){
		int temp = left(index);
		if(temp == -1)
			return temp;
		
		return (temp + 1 < size()) ? temp + 1 : -1;
	}
	
	/** returns the index of the parent of item at index i
	 * 
	 * @param i < data.size()
	 * @return integer
	 */
	private int parent(int index) {
		if(index > 0 && index < size())
			return (index - 1) / 2;
		else
			return -1;
	}

public static class Tests {
		
		public static void assertInvariants(Heap<?, Integer> heap) throws AssertionFailedError {
			if(heap.size() > 0) {
				recurTestLeftRightInvariant(heap, 0);
			}
		}
		
		/** recursively tests whether the left and right elements(if exists) of a index is smaller than or equal to their parent */
		private static void recurTestLeftRightInvariant(Heap<?, Integer> heap, int index) throws AssertionFailedError {
			if(index < 0 || index >= heap.size())
				return;
			
			int left = heap.left(index);
			int right = heap.right(index);
			//the priority left, right elements of the element must be either nonexistent or smaller than/the same to the parent
			if((left != -1 && heap.comparator.compare(heap.priorityMap.get(index), heap.priorityMap.get(left)) < 0)
					||(right != -1 && heap.comparator.compare(heap.priorityMap.get(index), heap.priorityMap.get(right)) < 0)) {
				throw new AssertionFailedError(String.format("LeftRightInvariant failed at index %d, depth %d (this:%s left:%s right:%s)",
						index, 
						heap.depth(index), 
						heap.priorityMap.get(index).toString(),
						heap.priorityMap.get(left).toString(),
						heap.priorityMap.get(right).toString()
						));
			}
			recurTestLeftRightInvariant(heap, left);
			recurTestLeftRightInvariant(heap, right);
		}
		
		/** create a heap with the input arguments of increasing priority*/
		Heap<String, Integer> makeHeap(String ...strs) {
			Heap<String, Integer> heap = new Heap<String, Integer>(Comparator.naturalOrder());
			int prior = 0;
			for(String str : strs) {
				heap.add(str, prior--);
			}
			return heap;
		}

		@Test
		void testPeek() {
			Heap<String, Integer> heap = makeHeap("test", "asda", "kkk");
			assertEquals("test", heap.peek());
		}
		
		@Test
		void testPoll() {
			Heap<String, Integer> heap = makeHeap("test", "asda", "kkk");
			assertEquals("test", heap.poll());
			System.out.println();
			assertEquals("asda", heap.poll());
			System.out.println();
			assertEquals("kkk", heap.poll());
			Heap<String, Integer> heap2 = makeHeap();
			Heap<String, Integer> heap3 = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			assertThrows(NoSuchElementException.class,()->{heap.poll();});
			assertEquals("1", heap3.poll());
			while(heap3.poll() != "10") {
				heap3.poll();
			}
			assertEquals("11", heap3.poll());
		}
		
		
		@Test
		void testAdd() {
			Heap<String, Integer> heap = makeHeap("1", "2", "3", "4", "5", "6");
			assertInvariants(heap);
			Heap<String, Integer> heap2 = new Heap<String, Integer>(Comparator.naturalOrder());
			heap.add("9", 10);
			assertEquals(10,heap.priorityMap.get(0));
			
			assertThrows(IllegalArgumentException.class,()->{heap.add("1", 1);});
		
		}
		
		@Test
		void testChangePriority() {
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6");
			heap.changePriority("2", 3);
			heap.changePriority("3", 8);
			heap.changePriority("6", -9);
			assertEquals(3,heap.priorityMap.get(1));
			assertEquals(8,heap.priorityMap.get(2));
			assertEquals(-4, heap.priorityMap.get(4));
			assertEquals(-9, heap.priorityMap.get(5));
		
		}
		
		@Test
		void testLeft() {
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			assertEquals(1, heap.left(0));
			assertEquals(7,heap.left(3));
			assertEquals(11,heap.left((heap.left(2))));
		}
		
		@Test
		void testRight(){
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			assertEquals(2,heap.right(0));
			assertEquals(8,heap.right(3));
			assertEquals(10,heap.right((heap.right(1))));
		}
		
		@Test
		void testParent() {
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			assertEquals(0,heap.parent(2));
			assertEquals(4,heap.parent(9));
			assertEquals(5,heap.parent(11));
			assertEquals(1,heap.parent(4));
			assertEquals(0,heap.parent(heap.parent(4)));
		}
		
		@Test
		void testSwap() {
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			heap.swap(0, 1);
			heap.swap(9, 2);
			heap.swap(8, 1);
			assertEquals(0,heap.locationMap.get("2"));
			assertEquals(2,heap.locationMap.get("10"));
			assertEquals(8,heap.locationMap.get("1"));
			assertEquals(-2,heap.priorityMap.get(9));
			assertEquals(-9,heap.priorityMap.get(2));
			assertEquals(-4, heap.priorityMap.get(4));
			assertEquals(-8, heap.priorityMap.get(1));
			assertEquals(0, heap.priorityMap.get(8));	
			assertEquals(-1,heap.priorityMap.get(0));
		}
		
		@Test
		void testNoOfElements() {
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			assertEquals(4,heap.noOfElements(5));
			assertEquals(8,heap.noOfElements(11));
			assertEquals(2,heap.noOfElements(1));
			assertEquals(1,heap.noOfElements(0));
		}
		
		@Test
		void testDepth() {
			Heap<String, Integer> heap = makeHeap("1","2","3","4","5","6","7","8","9","10","11","12");
			assertEquals(4,heap.depth(11));
			assertEquals(4,heap.depth(7));
			assertEquals(2,heap.depth(1));
			assertEquals(1,heap.depth(0));
			assertEquals(3,heap.depth(5));
		}
	}

}
