package group1a4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/** A heap stores elements of type E with priorities of type P (typically P is Integer or Double) in
 * an ArrayList.
 *
 * This class extends the PriorityQueue interface, causing it to only allow one copy of a given
 * element to be stored. Trying to store more than one copy of an element will throw an exception.
 *
 * The add() and changePriority() methods below operate with log n steps, therefore running in
 * logarithmic time.
 *
 * @param <E> type of Element in the heap.
 * @param <P> the priority, a double or an integer. */
public class Heap<E, P> implements PriorityQueue<E, P> {
	/**
	 * Added this method to Group 5's Heap implementation. 
	 * Returns the Priority of Element e.
	 */
	public P getPriority (E e) {
		if(!(this.store.contains(e)))
			throw new NoSuchElementException("Heap does not contain e.");
		return store.get(key.get(e)).priority;
	}

////////////declaring fields and the constructor//////////////////////////////////

    private Comparator<P> c;
    private ArrayList<Node<E, P>> store;
    private HashMap<E, Integer> key;

    public Heap(Comparator<P> c) {
        this.c= c;
        this.store= new ArrayList<>();
        this.key= new HashMap<>();
    }

////////////a few helper methods//////////////////////////////////////////////////

    /** returns the index of the respective node's left child
     *
     * @param i the index of a node, 0 or positive integer
     * @return the index of that node's left child
     *
     * @throws NoSuchElementException if the supposed left child index does not exist */
    public int left(int i) throws NoSuchElementException {

        if (i + i + 1 > this.store.size()) // if the node does not have a left child...
            throw new NoSuchElementException(); // ...throw an exception
        return i + i + 1;
    }

    /** returns the index of the respective node's right child
     *
     * @param i the index of a node, 0 or positive integer
     * @return the index of that node's right child
     *
     * @throws NoSuchElementException if the supposed right child index does not exist. */
    public int right(int i) {

        if (i + i + 2 > this.store.size()) // if the node does not have a right child...
            throw new NoSuchElementException(); // ...throw an exception
        return i + i + 2;
    }

    /** returns the index of the respective node's parent
     *
     * @param i the index of a node, 0 or positive integer
     * @return the index of that node's parent
     *
     * @throws NoSuchElementException if the supposed parent index does not exist */
    public int parent(int i) {

        if (i == 0) // if the node is the root of the heap (no parent)...
            throw new NoSuchElementException(); // ...throw an exception
        if (i % 2 == 0 && i != 0) {
            return i / 2 - 1;
        } // if the index of node is even...
        else {
            return i / 2;
        } // if the index of node is odd...
    }

    /** exchanges a[i] and a[j]
     *
     * @param a an ArrayList that has Nodes that hold elements a[j] and a[i]
     * @param i index of element to be swapped
     * @param j index of element to be swapped */
    public void swap(int i, int j) {

        this.key.replace(this.store.get(i).element, j);
        this.key.replace(this.store.get(j).element, i);

        Node<E, P> tmp= this.store.get(i);
        this.store.set(i, this.store.get(j));
        this.store.set(j, tmp);

    }

    /** Filters the node a[k] down the heap
     *
     * @param k the index of the node to be filtered down the heap so that the heap satisfies its
     *          invariants
     * @return the index in which the node ends up after method finishes */
    public int filterDown(int k) {

        int childLeft;
        int childRight;

        P priority1= this.store.get(k).priority;

        while (2 * k + 1 <= this.store.size() - 1) { // checking if the children of the k node exist

            if (2 * k + 2 <= this.store.size() - 1) { // checking if the right child exists
                // if so, must find the min of the three nodes

                childLeft= this.left(k); // store array index of left child of k
                childRight= this.right(k); // store array index of right child of k

                P pLeftChild= this.store.get(childLeft).priority; // storing the priority of the
                                                                  // left
                                                                  // child
                P pRightChild= this.store.get(childRight).priority; // storing the priority of the
                                                                    // right

                if (this.c.compare(priority1, pLeftChild) > 0 &&
                    this.c.compare(priority1, pRightChild) > 0)
                    break;
                else { // swap k with the larger of its two children
                    if (this.c.compare(pLeftChild, pRightChild) > 0) { // Left child is larger so...
                        this.swap(k, childLeft); // swap left child with parent node
                        k= childLeft; // "parent" node is childLeft in new iteration
                    } else { // Right child is larger so...
                        this.swap(k, childRight); // swap right child with parent node
                        k= childRight; // "parent" node is childRight in new iteration
                    }
                }
            } else { // right child does not exist; there is only one child

                childLeft= this.left(k); // store array index of left child of k
                P pLeftChild= this.store.get(childLeft).priority; // storing the priority of the
                                                                  // left

                if (this.c.compare(priority1, pLeftChild) > 0)
                    break;
                else { // left child is larger than node
                    this.swap(k, childLeft); // swap left child with parent node
                    k= childLeft; // "parent" node is childLeft in new iteration
                }
            }
        }

        return k;
    }

    /** Filters the node a[k] up the heap
     *
     * @param k the index of the node to be filtered up the heap so that the heap satisfies its
     *          invariants
     * @return the index in which the node ends up after the method finishes */
    public int filterUp(int k) {

        while (k > 0) { // only run loop while k is not the first element of the array (the root of
                        // the heap)

            int parentIndex= this.parent(k); // store parent index
            P parentPriority= this.store.get(parentIndex).priority; // store the priority of the
                                                                    // parent element

            P childPriority= this.store.get(k).priority; // store the priority of the current child
                                                         // element

            if (this.c.compare(parentPriority, childPriority) >= 0) {
                // if parentPriority is less than childPriority, then the child is in the right
                // place
                break;
            } else {
                this.swap(parentIndex, k); // if not, swap the parent and child
                k= parentIndex; // set the parent index as the new k for the next iteration
            }
        }

        return k;
    }

////////////PriorityQueue methods//////////////////////////////////////////////////

    /** return the comparator used for ordering priorities */
    @Override
    public Comparator<? super P> comparator() {
        return this.c;
    }

    /** Return the number of elements in this heap. Runs in O(1) time. */
    @Override
    public int size() {
        return this.store.size();
    }

    /** invariant: largest element of heap is at the top, store.get(0)
     *
     * Remove and return the largest element of this heap, according to comparator() Runs in O(log
     * n) time.
     *
     * @throws NoSuchElementException if this heap is empty */
    @Override
    public E poll() throws NoSuchElementException {

        if (this.size() == 0)
            throw new NoSuchElementException();

        Node<E, P> largest= this.store.get(0); // store the largest element of the heap, which
        // is the root of the heap tree or the
        // first element in the array
        Node<E, P> lastLeaf= this.store.get(this.size() - 1); // store the last leaf/last element of
                                                              // the array

        this.store.set(0, lastLeaf); // replace root with the last leaf
        // replace the first element with the last element of the array list

        this.store.remove(this.size() - 1); // remove last leaf of Heap

        this.filterDown(0);  // Use helper method to move the node a[k] down the tree

        return largest.element; // return largest and removed value
    }

    /** Return the largest element of this heap, according to comparator(). Runs in O(1) time.
     *
     * @throws NoSuchElementException if this is empty. */
    @Override
    public E peek() throws NoSuchElementException {

        if (this.size() == 0)
            throw new NoSuchElementException();
        return this.store.get(0).element;
    }

    /** Add the element e with priority p to this. Runs in O(log n + a) time, where a is the time it
     * takes to append an element to an ArrayList of size n.
     *
     * @throws IllegalArgumentException if this already contains an element that is equal to e
     *                                  (according to .equals()) */
    @Override
    public void add(E e, P p) throws IllegalArgumentException {

        if (this.key.containsKey(e)) // check if element e exists in the heap
            throw new IllegalArgumentException(); // if so, throw an IllegalArgumentException

        this.store.add(new Node<>(e, p)); // add element e to end of array list

        int end= this.filterUp(this.store.size() - 1); // filter the newly appended element up the
                                                       // heap to its rightful place

        this.key.put(e, end); // adds the element e with location index in the HashMap object
    }

    /** Change the priority associated with e to p.
     *
     * @throws NoSuchElementException if this does not contain e. */
    @Override
    public void changePriority(E e, P p) throws NoSuchElementException {

        if (!this.key.containsKey(e))
            throw new NoSuchElementException();

        int location= this.key.get(e); // find index of element e
        this.store.get(location).priority= p; // update priority of element e by updating Node
                                              // priority field

        P parentPriority= this.store.get(this.parent(location)).priority; // store priority of
                                                                          // parent

        if (this.c.compare(p, parentPriority) > 0) // if p is more than priority of parent...
            this.filterUp(location); // filter the node upwards
        else if (this.c.compare(p, parentPriority) < 0) // if p is less than priority of parent...
            this.filterDown(location); // filter the node downwards
    }

////////////Additional Methods Implemented//////////////////////////////////////////////////

    /** Change the Element old to Element n.
     *
     * @param old the old element to be changed.
     * @param n   the new element that will be changed to.
     * @throws NoSuchElementException if this does not contain e. */
    public void changeElement(E old, E n) throws NoSuchElementException {

        // check if Element e exists - if not throw noSuchElementException.
        if (!this.key.containsKey(old))
            throw new NoSuchElementException();

        // remove Element old.
        int index= this.key.get(old);
        this.key.remove(old);

        // replace with Element new.
        this.key.put(n, index);
        this.store.get(index).element= n;
    }

    /** Change the Element at index ArrIndex to Element n, and return old element.
     *
     * @param ArrIndex index of the element to be changed.
     * @param n        the new element to be changed to.
     * @throws IndexOutOfBoundsException if index Arrindex does not exist. */
    public void changeElement(int ArrIndex, E n) throws IndexOutOfBoundsException {

        // check if ArrIndex is valid index - if not throw IndexOutOfBoundsException.
        if (ArrIndex > this.store.size() - 1)
            throw new IndexOutOfBoundsException();

        // remove Element with index ArrIndex.
        E old= this.store.get(ArrIndex).element;
        this.key.remove(old);

        // replace with Element new.
        this.key.put(n, ArrIndex);
        this.store.get(ArrIndex).element= n;
    }

    /** Remove node with Element rem and rearrange Heap according to priority.
     *
     * @param rem the element to be removed and returned.
     * @throws NoSuchElementException if Element rem does not exist. */
    public void removeElement(E rem) throws NoSuchElementException {

        // check if Element rem exists - if not throw NoSuchElementException.
        if (!this.key.containsKey(rem))
            throw new NoSuchElementException();

        // replace node containing Element rem
        // with node of largest index(=lowest priority)
        Node smallest= this.store.get(this.size() - 1);
        int remIndex= this.key.get(rem);
        this.store.set(remIndex, smallest);
        this.store.remove(this.size() - 1);
        this.key.remove(rem);

        // rearrange Heap according to priority.
        if (remIndex * 2 < this.store.size())// rearrange children if they exist.
            this.filterDown(remIndex);
        if (remIndex != 0) // rearrange parents if they exist.
            this.filterUp(remIndex);
    }

    /** Check if Element ele exists in Heap; returns true if so.
     *
     * @param ele the element that will be examined if it exists in the heap.
     * @return true or false, depending on if the heap contains element ele. */
    public boolean contains(E ele) {

        // check if Element ele exists - returns true if so.
        if (this.key.containsKey(ele))
            return true;
        return false;
    }

    /** Returns a visual representation of the Heap as an ArrayList and Tree.
     *
     * @return a representation of the heap in an ArrayList and a tree. */
    @Override
    public String toString() {

        // returning a visual representation as an ArrayList.
        String temp= "";
        temp= "ArrayList: [";
        for (Node n : this.store)
            temp+= n.element.toString() + ", ";
        temp= temp.substring(0, temp.length() - 2) + "] \n";

        // returning a visual representation as a Tree.
        int n= this.size();
        for (int i= 0; i < n; i++ )
            for (int j= 0; j < Math.pow(2, i) && j + Math.pow(2, i) < n; j++ ) {
                temp+= this.store.get(j + (int) Math.pow(2, i) - 1) + " ";
            }
        return temp;
    }

////////////inner Node class//////////////////////////////////////////////////

    /** An instance is a node of this list. */
    public static class Node<E, P> {

        /*value of Node*/
        private E element;
        /*priority of element*/
        private P priority;

        public Node(E e, P p) {
            this.element= e;
            this.priority= p;
        }
    }

///////////inner TestComparator class for tests///////////////////////

    public static class TestComparator implements Comparator<Double> {

        @Override
        public int compare(Double o1, Double o2) {
            return o1.compareTo(o2);
        }
    }

////////////test cases/////////////////////////////////////////////////////

    /** Glass-box tests for Heap. Since this is an inner class, it has access to both Heap and
     * Node's private types, fields, and methods. */

    public static class HeapTests {

        @Test
        public void HeapTests() {

            // initialization

            Comparator<Double> c= new TestComparator();
            Heap<String, Double> heap= new Heap(c);
            heap.add("abc", 4.4);
            heap.add("yy", 3.4);
            heap.add("xyz", 3.3);
            heap.add("kk", 2.0);
            heap.add("aa", 1.0);

            // helper methods tests.
            // testLeft(),Right(),Parent()
            assertEquals(3, heap.left(1));
            assertEquals(4, heap.right(1));
            assertEquals(0, heap.parent(1));
            assertEquals(0, heap.parent(2));
            // testSwap()
            heap.swap(2, 4);
            assertEquals("aa", heap.store.get(2).element);
            assertEquals("xyz", heap.store.get(4).element);

            // heap methods tests.
            // testSize()
            assertEquals(5, heap.size());
            // testPoll()
            assertEquals("abc", heap.poll());
            assertEquals(4, heap.size());
            // testPeek()
            assertEquals("yy", heap.peek());
            // testAdd()
            heap.add("why?", 4.444);
            assertEquals(5, heap.size());
            assertEquals("why?", heap.store.get(0).element);
            assertEquals(4.444, (double) heap.store.get(0).priority);

            // additional methods tests.
            // testChangePriority()
            heap.changePriority("xyz", 5.0);
            assertEquals("xyz", heap.peek());
            // testChangeElement1()
            heap.changeElement("why?", "how?");
            assertEquals("how?", heap.store.get(1).element);
            // testChangeElement2()
            heap.changeElement(0, "end");
            assertEquals("end", heap.peek());
            // testRemoveElement()
            heap.removeElement("end");
            assertEquals("how?", heap.peek());
            assertEquals(4, heap.size());
            // testContains()
            assertEquals(false, heap.contains("end"));
            assertEquals(true, heap.contains("yy"));
            // testToString()
            heap.toString();
        }
    }
}
