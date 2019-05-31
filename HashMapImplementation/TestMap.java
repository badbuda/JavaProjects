package il.co.ilrd.hashmap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class TestMap {
	HashMap<Integer, String> hash;

	@Before
	public void Create() {
		hash = new HashMap<>(10);
		hash.put(1, "Tziona");
		hash.put(2, "Israela");
		hash.put(3, "Yehudit");
		hash.put(4, "Lola");
		hash.put(5, "Shahar");
		hash.put(6, "Bareket");
		hash.put(7, "Ginges");
		hash.put(8, "Sami");
		hash.put(9, "Iris");
		hash.put(10, "Galit");
		hash.put(null, "Galit");
	}

	@Test
	public void basicHashMap() {
		HashMap<Integer, String> emptyHash = new HashMap<>();
		assertEquals(emptyHash.size(), 0);
		assertTrue(emptyHash.isEmpty());
		emptyHash.put(0, "testing 123");
		assertTrue(emptyHash.size() == 1);
		assertFalse(emptyHash.isEmpty());
	}

	@Test
	public void testContainsKeyContainsValue() {
		assertTrue(hash.containsKey(7));
		assertFalse(hash.containsKey(17));
		
		assertTrue(hash.containsValue("Lola"));
		assertFalse(hash.containsValue("lola"));
	}

	@Test
	public void testGetPut() {
		
		assertEquals(hash.put(3, "Yehuda"), "Yehudit");
		assertEquals(hash.size(), 11);
		
		assertEquals(hash.get(1), "Tziona");
		assertEquals(hash.get(null), "Galit");
		
		hash.put(null, null);
		hash.put(5, null);
		assertTrue(hash.containsKey(null));
		assertTrue(hash.containsKey(5));
		assertNull(hash.get(5));
		assertNull(hash.get(null));
		hash.remove(null);
		assertFalse(hash.containsKey(null));

		hash.put(null, null);
		assertNull(hash.put(null, "my key is null!!!"));
		assertEquals(hash.get(null), "my key is null!!!");
	}

	@Test
	public void testRemove() {
		assertEquals(hash.remove(4), "Lola");
		assertEquals(hash.get(4), null);
		assertEquals(hash.size(), 10);

		hash.put(4, "Lola");
		assertEquals(hash.size(), 11);
		assertEquals(hash.get(4), "Lola");

	}

	@Test
	public void testClearPutall() {
		HashMap<Integer, String> hashCpy = new HashMap<>(9);
		assertTrue(hashCpy.isEmpty());
		assertEquals(hashCpy.size(), 0);
		
		hashCpy.putAll(hash);
		
		assertFalse(hashCpy.isEmpty());
		assertEquals(hashCpy.size(), 11);

		hashCpy.clear();
		
		assertTrue(hashCpy.isEmpty());
		assertEquals(hashCpy.size(), 0);

	}

}