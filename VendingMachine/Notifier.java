package il.co.ilrd.vendingmachine;

public class Notifier implements Notify {
	
	@Override
	public void write (String str) {
		System.out.println(str);
		
	}
}

