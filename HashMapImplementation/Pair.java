package il.co.ilrd.hashmap;

import java.util.Comparator;
import java.util.Objects;
import java.util.Map.Entry;

public class Pair<K, V> implements Entry<K, V> {
	private final K key;
	private V value;
	
	private Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V prevValue = this.value;
		this.value = value;
		return prevValue;
	}
	
	@Override
	public boolean equals (Object obj) {

		if (!(obj instanceof Pair)) { return false; }
		
		Pair<?,?> objc= (Pair<?,?>)obj;
		
		boolean keyEquals = (objc.getKey() == null ? this.getKey() == null : 
										objc.getKey().equals(this.getKey()));
		
		boolean valueEquals = (objc.getValue() == null ? this.getValue() == null : 
										objc.getValue().equals(this.getValue()));	
		
		return (keyEquals && valueEquals);	
		}
	
	@Override
	public int hashCode() {
		return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^
			((this.getValue() == null ? 0 : this.getValue().hashCode()));
	}
	
	@Override
	public String toString() {
		return ("toString- key: " + getKey() + "value : " + getValue());
	}
	
	public static <K, V> Pair<V, K> swap(Pair<K, V> pair){
		Objects.requireNonNull(pair);

		return new Pair <>(pair.getValue(), pair.getKey());
	}
	
	public static <K, V> Pair<K, V> of(K key, V value){
		return new Pair <>(key, value);
	}
	
	public static <T extends Comparable<? super T>> Pair<T, T> minmax(T[] arr){
		Objects.requireNonNull(arr);

		return minMaxImp(arr, (T cmp1, T cmp2)-> { 
			return cmp1.compareTo(cmp2);
		});
	}

	public static <T> Pair<T,T> minmax (T[] arr, Comparator<? super T> comp) {
		Objects.requireNonNull(comp);

		return minMaxImp(arr, comp);
	}
	
	private static <T> Pair<T,T> minMaxImp (T[] arr, Comparator<? super T> comp) {
	
		if (0 == arr.length) { return null; }
		
		T min = arr[0];
		T max = arr[0];
		
		for (int i = arr.length % 2; i < arr.length - 1; i += 2) {
			if (comp.compare(arr[i], arr[i + 1]) < 0) {
				if (comp.compare(arr[i], min) < 0) {
					min = arr[i];
				}
				if (comp.compare(arr[i + 1], max) > 0) {
					max = arr[i + 1];
				}
			}
			else {
				if (comp.compare(arr[i], max) > 0) {
					max = arr[i];
				}
				if (comp.compare(arr[i + 1], min) < 0){
					min = arr[i + 1];
				}
			}
		}
		
		return new Pair<>(min,max);
	}
}