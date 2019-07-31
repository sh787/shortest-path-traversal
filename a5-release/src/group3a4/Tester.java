package group3a4;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import java.lang.Integer;

class Tester {

	@Test
	void testCloneAndAdd() {
		//fail("Not yet implemented");
		CompareStuff<Integer> c = new CompareStuff<Integer>();
		Heap<String, Integer> words = new Heap<String, Integer>(c);
		words.add("b", 2);
		words.add("a", 1);
		words.add("c", 3);
		words.add("d", 4);
		words.add("f", 6);
		words.add("e", 5);
		assertEquals(words.size(), 6);
		Heap<String, Integer> wordsCopy = words.clone();
		assertEquals(wordsCopy.size(), 6);;
		wordsCopy.add("A", 1);
		wordsCopy.add("B", 2);
		wordsCopy.add("D", 4);
		String[] correct = {"f", "e", "d", "D", "c", "b", "B", "a", "A"};
		for(int i = 0; i <9; i++) {assertEquals(wordsCopy.poll().toString(), correct[i]);}
	}


	
	@Test
	void testSize(){
		Heap<Integer, Integer> h = new Heap<Integer,Integer>(new CompareStuff<Integer>());
		assertEquals(h.size(),0);
		h.add(1, 1);
		h.add(2, 2);
		h.add(3, 3);
		assertEquals(h.size(), 3);
		/*h.poll();
		assertEquals(h.size(),2);
		h.add(4, 4); h.add(5,5); h.add(6, 6); h.add(7, 7); h.add(8, 8); h.add(9, 9); h.add(10,10); h.add(11, 11);
		assertEquals(h.size(), 11);*/
	}
	
	@Test
	void testPoll() {
		Heap<Integer, Integer> h = new Heap<Integer,Integer>(new CompareStuff<Integer>());
		//Test that poll() behaves properly when given a heap of size 1
		h.add(1, 1); 
		int i = h.poll();
		assertEquals(i , 1);
		assertTrue(h.map.isEmpty());
		
		//Test that poll() behaves properly when given a heap of size > 1
		h.add(1, 1); h.add(2, 2); h.add(3, 3);
		i = h.poll();
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(2); l.add(1);
		assertEquals(i,3);
		checkPriority(h,l);
//		
		Heap<Integer, Integer> h1 = new Heap<Integer,Integer>(new CompareStuff<Integer>());
		h1.add(1, 1); h1.add(3, 3); h1.add(2, 2); h1.add(4, 4); h1.add(0, 0); h1.add(-1, -1);
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		l2.add(-3); l2.add(1); l2.add(2);l2.add(-1); l2.add(0);
		i = h1.poll();
		assertEquals(i, 4);
		checkPriority(h,l);
	}
	
	void checkPriority(Heap<Integer,Integer> h, ArrayList<Integer> l){
		ArrayList<Integer> x = new ArrayList<Integer>();
		for (int i = 0; i< h.size(); i++) {
			x.add(h.map.get(h.list.get(i)));
		}
		assertEquals(x,l);
	}
	
	@Test
	void testPeek() {
		Heap<Integer,Integer> h2 = new Heap<Integer,Integer>(new CompareStuff<Integer>());
		assertThrows(NoSuchElementException.class, ()->h2.peek());
		h2.add(1, 1);
		assertEquals(h2.peek(), (Integer)1);
		h2.add(2, 2);
		assertEquals(h2.peek(),(Integer)2);
	}
	@Test
	void testchangePriority() {
		Heap<Integer,Integer> h = new Heap<Integer,Integer>(new CompareStuff<Integer>());
		h.add(1, 1); h.add(2, 2);
		h.changePriority(1, 3);
		assertEquals((Integer)1,h.peek());
		

		h.changePriority(1,1);
		assertEquals((Integer)2, h.peek());
		h.add(0, 0); h.add(-1, -1);
		
		h.changePriority(-1,3);
		assertEquals((Integer)(-1),h.peek());
		
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		l2.add(3);l2.add(2);l2.add(0);l2.add(1);
		checkPriority(h,l2);
	}
}

class CompareStuff<T> implements Comparator<Integer>{

	@Override
	public int compare(Integer o1, Integer o2) {
		if (o1 > o2) {return 1;}
		else if (o1 < o2) {return - 1;}
		else {return 0;}
	}
	
}
