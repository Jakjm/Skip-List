import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.*;
class SkipTest {
	SkipList<Integer> list;
	@BeforeEach
	void load() {
		list = new SkipList<Integer>();
		
	}
	/**
	 * Testing that the list adds items properly to the list, and they can be accessed properly. 
	 */
	@Test
	void test1() {
		
		//Adding an element
		list.add(4);
		assertEquals(list.toString(),"[4]"); //Checking string representation
		assertSame(1,list.size());  //Checking size
		assertSame(4,list.get(0));  //Checking indexes of elements
		
		
		//Adding an element at 0
		list.add(0,3);
		assertEquals(list.toString(),"[3, 4]"); //Checking string representation
		assertEquals(2,list.size()); //Checking size
		assertSame(3,list.get(0)); //Checking indexes of elements
		assertSame(4,list.get(1)); //Checking indexes of elements
		
		
		//Adding an element at the end of the list
		list.add(2,5);
		assertEquals(list.toString(),"[3, 4, 5]"); //Checking string representation
		assertEquals(3,list.size()); //Checking size
		for(int i = 3;i <= 5;i++)assertSame(i,list.get(i-3)); //Checking indexes of elements
		
		//Adding an element at the start of the list
		list.add(0,1);
		assertEquals(list.toString(),"[1, 3, 4, 5]"); //Checking string representation
		assertEquals(4,list.size()); //Checking size
		assertSame(1,list.get(0)); //Checking indexes of elements
		for(int i = 3;i <= 5;i++)assertSame(list.get(i-2),i); //Checking indexes of elements
		
		//Now adding 2 in between 1 and 3
		list.add(1,2);
		assertEquals(list.toString(),"[1, 2, 3, 4, 5]"); //Checking string representation
		assertEquals(5,list.size()); //Checking size
		for(int i = 1;i <= 5;i++)assertSame(list.get(i-1),i); //Checking indexes of elements
	}
	/**
	 * Testing that exceptions are thrown when adding outside of the legal range for the list.
	 */
	@Test
	void test2() {
		for(int i = 0;i < 10;i++) {
			list.add(i);
		}
		
		try {
			list.add(-1,10);
			fail("Exception should be thrown");
		}
		catch(Exception e) {
			
		}
		
		try {
			list.add(11,10);
			fail("Exception should be thrown");
		}
		catch(Exception e) {
			
		}
		
		try {
			list.get(-1);
			fail("Exception should be thrown");
		}
		catch(Exception e) {
			
		}
		
		try {
			list.get(10);
			fail("Exception should be thrown");
		}
		catch(Exception e) {
			
		}
	}
	@Test
	public void test3() {
		
		for(int i = 0; i < 10000;i++) {
			list.add(i);
		}
		for(int i = 0;i < 10000;i++) {
			assertEquals((int)list.get(i),(int)i);
		}
		list = new SkipList<Integer>();
		for(int i = 9999;i >= 0;--i) {
			list.add(0,i);
		}
		for(int i = 0;i < 10000;i++) {
			assertEquals((int)list.get(i),(int)i);
		}
	}
	//@Test
	public void testLists() {
		LinkedList<Integer> linked = new LinkedList<Integer>();
		
		long testSize = 100000;
		System.out.println(String.format("\n\n~~~~ SkipList vs LinkedList Test ~~~~\n"));
		System.out.println(String.format("Adding %d Items Test",testSize));
		
		//Testing adding time for skip list at start, middle, and end.
		System.out.print(String.format("SkipList - Adding to Start: %dms   ",testListStart(list,testSize)));
		list = new SkipList<Integer>();
		System.out.print(String.format("Middle: %dms   ", testListMiddle(list,testSize)));
		list = new SkipList<Integer>();
		System.out.print(String.format("End: %dms  \n\n", testListEnd(list,testSize)));
		
		//Testing adding time for linked list at start, middle and end.
		System.out.print(String.format("java.util.LinkedList - Adding to Start: %dms   ",testListStart(linked,testSize)));
		linked = new LinkedList<Integer>();
		System.out.print(String.format("Middle: %dms   ", testListMiddle(linked,testSize)));
		linked = new LinkedList<Integer>();
		System.out.print(String.format("End: %dms  \n", testListEnd(linked,testSize)));
		
		System.out.println(String.format("Retrieving %d Items",testSize));
		
		System.out.println(String.format("Skip List %dms",testRetrieval(list,testSize)));
		System.out.println(String.format("LinkedList %dms",testRetrieval(linked,testSize)));
	}
	public long testRetrieval(List<Integer> numList,long size) {
		long startTime = System.currentTimeMillis();
		int item; 
		for(int i = 0;i < size;i++) {
			item = numList.get(i);
		}
		return System.currentTimeMillis() - startTime;
	}
	public long testListStart(List<Integer> numList,long size) {
		long startTime = System.currentTimeMillis();
		for(int i = 0;i < size;i++) {
			numList.add(0,i);
		}
		return System.currentTimeMillis() - startTime;
	}
	public long testListMiddle(List<Integer> numList,long size) {
		long startTime = System.currentTimeMillis();
		for(int i = 0;i < size;i++) {
			numList.add(numList.size()/2,i);
		}
		return System.currentTimeMillis() - startTime;
	}
	public long testListEnd(List<Integer> numList,long size) {
		long startTime = System.currentTimeMillis();
		for(int i = 0;i < size;i++) {
			numList.add(i);
		}
		return System.currentTimeMillis() - startTime;
	}
	
	
}
