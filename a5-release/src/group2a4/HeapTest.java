package group2a4;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import junit.framework.AssertionFailedError;

class HeapTest {

	@Test
	void testConstructor() {
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		//Node<Integer, Integer> n = new Heap.Node<Integer, Integer>(1, 1);
		assertEquals(h.size(), 0);
		h.add(1, 1);
		assertEquals(h.size(), 1);
		
	}
	
	@Test
	void testComparator() {
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		assertEquals(h.comparator(), c);
	}
	
	@Test
	void testSize() {
		//test 1: Heap<Integer, Integer> h, basic tests
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		h.add(1, 2);
		assertEquals(h.size(), 1);
		h.add(2, 1);
		assertEquals(h.size(), 2);
		h.add(5, 3);
		h.add(6, 7);
		h.add(93, 23);
		assertEquals(h.size(), 5);
	}
	
	@Test
	<E> void testPoll() {
		//test 1: Heap<Integer, Integer> h, basic tests
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		h.add(1, 1);
		assertEquals(h.poll(), (Integer)1);
		assertEquals(h.size(), 0);
		h.add(5, 3);
		h.add(6, 7);
		h.add(93, 23);
		assertEquals(h.size(), 3);
		assertEquals(h.poll(), (Integer)93);
		assertEquals(h.size(), 2);
		assertEquals(h.poll(), (Integer)6);
		assertEquals(h.size(), 1);
		assertEquals(h.poll(), (Integer)5);
		assertEquals(h.size(), 0);
		try {
    		h.poll();
    	} catch (NoSuchElementException e) {
    		System.out.println("NoSuchElementException: " + e.getMessage());
    	}
	}
	
	@Test
	void testPeek() {
		//test 1: Heap<Integer, Integer> h, basic tests
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		
		try {
    		h.peek();
    	} catch (NoSuchElementException e) {
    		System.out.println("NoSuchElementException: " + e.getMessage());
    	}
		
		h.add(1, 1);
		assertEquals(h.peek(), (Integer)1);
		assertEquals(h.size(), 1);
	}
	
	@Test
	void testAdd() {
		//test 1: Heap<Integer, Integer> h, basic tests
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		h.add(1, 2);
		//can they have same priority?
		h.add(5, 3);
		h.add(7, 1);
		assertEquals(h.size(), 3);
		assertEquals(h.peek(), (Integer)5);
		try {
    		h.add(7, 3);
    	} catch (IllegalArgumentException e) {
    		System.out.println("IllegalArgumentException: " + e.getMessage());
    	}
		
		try {
    		h.add(1, 2);
    	} catch (IllegalArgumentException e) {
    		System.out.println("IllegalArgumentException: " + e.getMessage());
    	}
	}
	
	@Test
	void testChangePriority() {
		//test 1: Heap<Integer, Integer> h, basic tests
		Comparator<Integer> c = (int1, int2) -> {return int1 - int2;};
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(c);
		h.add(1, 2);
		h.add(5, 3);
		h.add(90, 4);
		assertEquals(h.size(), 3);
		assertEquals(h.peek(), (Integer)90);
		h.changePriority(5, 1);
		assertEquals(h.size(), 3);
		assertEquals(h.peek(), (Integer)90);
		assertEquals(h.poll(), (Integer)90);
		assertEquals(h.poll(), (Integer)1);
		assertEquals(h.poll(), (Integer)5);
		assertEquals(h.size(), 0);
		try {
    		h.changePriority(7, 1);
    	} catch (NoSuchElementException e) {
    		System.out.println("NoSuchElementException: " + e.getMessage());
    	}
		//test 2: Heap<Character, Integer> h2, basic tests
		Comparator<Character> c2 = (char1, char2) -> {return char1 - char2;};
		Heap<Character,Integer> h2 = new Heap<Character,Integer>(c);
		h2.add('a', 0);
		h2.add('b', 1);
		h2.add('c', 2);
		h2.add('d', 3);
		h2.add('e', 4);
		h2.add('f', 5);
		h2.add('g', 6);
		h2.add('h', 7);
		h2.add('i', 8);
		assertEquals(h2.size(), 9);
		h2.add('j', 8);
		h2.add('k', 8);
		assertEquals(h2.size(), 11);
		assertEquals(h2.peek(), (Character)'i');
		assertEquals(h2.poll(), (Character)'i');
		assertEquals(h2.poll(), (Character)'j');
		assertEquals(h2.poll(), (Character)'k');
	}

}
