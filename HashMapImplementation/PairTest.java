package il.co.ilrd.hashmap;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

class PairTest {
	@Test
	void BasicTests() {
		
		Pair<Integer, Double> extremeCheck = Pair.of(null, null);
		int obj = 5;
		assertFalse(extremeCheck.equals(obj));
		
		Pair<Integer, String> pair = Pair.of(new Integer(2), new String("TEST"));
		assertTrue(pair.getKey() == 2);
		pair.setValue("TEST2");
		assertTrue(pair.getValue() == "TEST2");
		
		Pair<Integer, String> pair2 = Pair.of(new Integer(2), new String("TEST2"));
		
		assertTrue(pair.hashCode() == pair.hashCode());
		assertTrue(pair.equals(pair2));
		
		Pair<String, Integer> swapPair = Pair.swap(pair2);
		assertTrue(swapPair.getKey() == pair2.getValue());
		assertTrue(swapPair.getValue() == pair2.getKey());
	}

	@Test
	void MinMaxTest() {
		Integer[] array = new Integer[5];
		
		array[0] = new Integer(2);
		array[1] = new Integer(-5);
		array[2] = new Integer(12);
		array[3] = new Integer(22);
		array[4] = new Integer(-30);
		//array[5] = new Integer(100);

		Pair<Integer, Integer> minMax = Pair.minmax(array);
		assertTrue(minMax.getKey() == -30);
		assertTrue(minMax.getValue() == 22);
		
		Comparator<Integer> comp = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 > o2) {
					return 1;
				} else if (o1 < o2) {
					return -1;
				}
				return 0;
			}
		};
		
		Pair<Integer, Integer> minMax2 = Pair.minmax(array, comp);
		assertTrue(minMax2.getKey() == -30);
		assertTrue(minMax2.getValue() == 22);
		
		Integer[] array2 = new Integer[1];
		array2[0] = new Integer(2);
		Pair<Integer, Integer> minMax3 = Pair.minmax(array2);
		assertTrue(minMax3.getKey() == 2);
		assertTrue(minMax3.getValue() == 2);
		
		Integer[] array3 = new Integer[4];
		array3[0] = new Integer(2);
		array3[1] = new Integer(22);
		array3[2] = null;
		array3[3] = new Integer(-5);
		
	}
}