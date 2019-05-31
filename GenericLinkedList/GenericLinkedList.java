package il.co.ilrd.linkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class GenericLinkedList <T> implements Iterable<T> {
	private Node head = new Node(null, null);
	private int modCount = 0;
	
	public void pushFront(T data){
		
		Node newNode = new Node(data, head);
		head = newNode;
		++modCount;
	}
	
	public T popFront(){
		assert head != null : "list is empty";
		T remove = head.data;
		head = head.next;	
		++modCount;
		return remove;
	}
	
	public long getSize(){
		ListIterator iterRunner = new ListIterator(head, modCount);
		
		int counter = 0;

		while (iterRunner.hasNext())
		{
			++counter;
			iterRunner.next();
		}

		return counter;
	}
	
	public boolean isEmpty(){
		return head.data == null;
	}

	public Iterator<T> getBegin(){
		assert head != null : "list is empty";
		return new ListIterator(head, modCount);
	}
	
	public Iterator<T> find(T toFind){
		assert head != null : "list is empty";
		ListIterator iterRunner = new ListIterator(head, modCount);
		ListIterator iterData = new ListIterator(head, modCount);
		
		for(T data: this) {
			iterData = iterRunner;
			
			if (toFind.equals(data))
			{
				return (iterData);
			}
			iterRunner.next();
		}
		return null;
	}
	
	public Iterator<T> iterator(){
		return (new ListIterator(head, modCount));
	}
	
	public static <E> GenericLinkedList<E> newReverse(GenericLinkedList<E> list){
		GenericLinkedList<E> newList = new GenericLinkedList<E>();

			for(E llData: list) {
				newList.pushFront(llData);
		}
		
		return newList;
	}
	
	public static <E> GenericLinkedList<E> merge(GenericLinkedList<E> list1, GenericLinkedList<E> list2){
		GenericLinkedList<E> retVal = new GenericLinkedList<E>();

			for(E ll1Data: list1) {
				retVal.pushFront(ll1Data);
		}
		
			for(E ll1Data: list2) {
				retVal.pushFront(ll1Data);
		}
		return retVal;
	}
	
	private class Node {
		private T data;
		private Node next;
		
		private Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}
	}
	
	private class ListIterator implements Iterator<T> {
		private int exceptionModCount;
		private Node node;
		
		private ListIterator(Node node, int modCount){
			this.node = node;
			this.exceptionModCount = modCount;
		}
		
		@Override
		public boolean hasNext() {
			return node.next != null;
		}
		
		@Override
		public T next() throws ConcurrentModificationException{
			
			if (exceptionModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			
			T data = node.data;
			node = node.next;
			return data;
		}
	}
}

