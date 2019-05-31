package linkedList;

import java.util.Iterator;

public class GenericLLTest {

	public static void main(String[] args) {

		GenericLinkedList<Integer> ll = new GenericLinkedList<Integer>();
		
		GenericLinkedList<Integer> list = new GenericLinkedList<Integer>();

		//isEmpty
		if (ll.isEmpty()){
			System.out.println("isEmpty: SUCCESS");
		}
		else{
			System.out.println("isEmpty: Fail");
		}
		//push

		for (int i = 0; i < 100; ++i){
			ll.pushFront((i));
			list.pushFront((i));
		}
		//size

		if (100 != ll.getSize()){
			System.out.println("pushFront: FAIL " + ll.getSize());
		}
		else{
			System.out.println("PushFront: SUCCESS");
		}
		//isEmpty
		//System.out.println("printit     " + ll.next.data);

		if (!ll.isEmpty()){
			System.out.println("isEmpty: SUCCESS");
		}
		else{
			System.out.println("isEmpty: Fail");
		}

		//check find

		Iterator<Integer> iter = ll.find(6);

		if (!iter.next().equals(5)){
			System.out.println("find: FAIL");
		}
		else{
			System.out.println("find: SUCCESS");
		}
		
		ll.newReverse(ll);
		Iterator<Integer> iterator2 = ll.iterator();
		int j = 99;
		while (iterator2.hasNext()) {
			if (iterator2.next().equals(j)) {
				System.out.println("reverse  SUCCESS");
			}
			else {
				System.out.println("reverse  Fail");
			}
			--j;
		}

		//popFront - clear all
		
		for (long i = 0; i < ll.getSize(); ++i){
			System.out.println(i);

			list.popFront();
		}

		if (!list.isEmpty() && list.getSize() == 0){
			System.out.println("isEmpty after pop: SUCCESS");
		}
		else{
			System.out.println("isEmpty after pop: Fail");
		}

		
	}
}
