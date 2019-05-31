package il.co.ilrd.hashmap;


import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HashMap<K,V> implements Map<K,V> {
	/************************Static Variables ******************************************/
	private static final int DEFAULT_CAPACITY = 64;
	
	/************************Instance ******************************************/
    private List<List<Entry<K, V>>> buckets;
    private final int capacity;
    private int modCount = 0;
	private Collection<V> valueCollection;
	private Set<Entry<K, V>> entrySet;
	private Set<K> keySet;
	
	/************************CTOR ******************************************/
	public HashMap() {
		this(DEFAULT_CAPACITY);
	}
	
	public HashMap(int capacity) {
		if (capacity < 0) { throw new IllegalArgumentException(); }

		this.capacity = capacity;
		
		buckets = new ArrayList<List<Entry<K,V>>>();
		
		for (int i = 0; i < capacity; ++i) {
			this.buckets.add(new ArrayList<Map.Entry<K,V>>());
		}
	}
	
	/************************API Functions ******************************************/

	@Override
	public boolean isEmpty () {
		return 0 == size();
	}
	
	@Override
	public int size () {
		return buckets.stream().mapToInt(List::size).sum();
	}
	
	@Override
	public void clear () {
		for (int i = 0; i < capacity; ++i) {
			this.buckets.clear();
		}
		++modCount;
	}
	
	@Override
	
	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for (V v : values()) {
			if (Objects.equals(value,v)) {
				return true;
			}
		}		
		
		return false;	
	}
	
	@Override
	public V get(Object key) {
		return getEntry(key) == null ? null : getEntry(key).getValue();
	}

	@Override
	public V put(K key, V value) {
		Entry<K,V> tempEntry = getEntry(key);
		
		if (tempEntry != null) {
			V prevValue = tempEntry.getValue();
			tempEntry.setValue(value);
			
			return prevValue;			
		}

		getBucket(key).add(Pair.of(key, value));
		++modCount;
		
		return null;
	}

	@Override
	public V remove(Object key) {
		Entry<K,V> tempEntry = getEntry(key);

		if (tempEntry != null) {
			V temp = tempEntry.getValue();
			getBucket(key).remove(tempEntry);
			++modCount;
			
			return temp;
		}

		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Objects.requireNonNull(m);

		for (Map.Entry<? extends K, ? extends V> data : m.entrySet()) {
			put(data.getKey(), data.getValue());
		}
		++modCount;
	}

	@Override
	public Set<K> keySet() {
		return (keySet == null ? keySet = new KeySet() : keySet);
	}

	@Override
	public Collection<V> values() {
		return (valueCollection == null ? valueCollection = new ValueCollection() : valueCollection);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return (entrySet == null ? entrySet = new EntrySet() : entrySet);
	}
	
	/************************** Utilities *************************************/
	/**
	 * this function gets the appropriate bucket according to the users key
	 * @param key  
	 * @return a bucket list
	 */
	private List<Entry<K, V>> getBucket (Object key) {
		int keyHash = Math.abs((key == null ? 0 : (key.hashCode() % capacity)));
		return buckets.get(keyHash);
	}
	/**
	 * this function gets the appropriate entry according to the user key
	 * @param key
	 * @return the correct Entry
	 */
	private Entry<K,V> getEntry(Object key) {
		for (Entry<K,V> e : entrySet()) {
			if (Objects.equals(key, e.getKey())) {
				return e;
			}
		}
		
		++modCount;

		return null;
	}
	
	/************************Set Classes ******************************************/

	private class EntrySet extends AbstractSet<Entry<K, V>>{
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntrySetIter();
		}
		
		@Override
		public int size() {
			return HashMap.this.size();
		}		
		
		private class EntrySetIter implements Iterator<Entry<K,V>> {
			private Iterator<List<Entry<K,V>>> bucketIter = buckets.iterator();
			private Iterator<Entry<K,V>> entryIter = bucketIter.next().iterator();
			private final int expectedModCount = modCount;

			@Override
			public boolean hasNext() {
				
				while (!entryIter.hasNext()) {
					if (!bucketIter.hasNext()) {
						return false;
					}
					
					else {
						entryIter = bucketIter.next().iterator();
					}
				}
				
				return true;
			}
			
			@Override
			public Entry<K, V> next() {
				if (modCount != expectedModCount) {
					throw new ConcurrentModificationException();
				}

				return entryIter.next();
			}	
		}
	}
	
	private class ValueCollection extends AbstractCollection<V>{

		@Override
		public Iterator<V> iterator() {
			return new valueIter();
		}

		@Override
		public int size() {
			return HashMap.this.size();
		}
		
		private class valueIter implements Iterator<V> {
			private Iterator<Entry<K,V>> bucketIter = entrySet().iterator();

			@Override
			public boolean hasNext() {
				
				return bucketIter.hasNext();
			}

			@Override
			public V next() {
				if (!bucketIter.hasNext()) {
					throw new NullPointerException();
				}
				
				return bucketIter.next().getValue();
			}
		}
	}
	
	private class KeySet extends AbstractSet<K> {

		@Override
		public Iterator<K> iterator() {
			return new keySetIter();
		}

		@Override
		public int size() {
			return HashMap.this.size();
		}
		
		private class keySetIter implements Iterator<K> {
			private Iterator<Entry<K,V>> bucketIter = entrySet().iterator();

			@Override
			public boolean hasNext() {
				return bucketIter.hasNext();
			}

			@Override
			public K next() {
				
				if (!bucketIter.hasNext()) {
					throw new NullPointerException();
				}
				return bucketIter.next().getKey();
			}
		}
	}
}