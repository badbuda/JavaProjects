package il.co.ilrd.vendingmachine;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Test {
	public static void main(String[] args) throws InterruptedException {
	Notifier note = new Notifier();

	      ArrayList<Product> arrProducts=new ArrayList<Product>();  

    	Product pro1 = new Product ("COCLA COLA" , 1.5);
    	Product pro2 = new Product ("ZERO COLA" , 2.5);
    	Product pro3 = new Product ("FANTA" , 3);
    	Product pro4 = new Product ("SPRIET" , 4.2);
        Product pro5 = new Product ("LEMONADE" , 5.8);
        
        arrProducts.add(pro1);
        arrProducts.add(pro2);
        arrProducts.add(pro3);
        arrProducts.add(pro4);
        arrProducts.add(pro5);
	  	
	      VendingMachine vm = new VendingMachine(arrProducts, note);
	  	
	  		vm.turnOn();
	                
	  		
	  		
	        vm.select("SPRIET");
	        vm.inputMoney(6);
	        vm.cancel();
	        
	        vm.select("FANTA");
	       TimeUnit.SECONDS.sleep(9);
	
	        vm.inputMoney(5);
	                
	        vm.select("ZERO COLA");
	        vm.inputMoney(8);
	
	        vm.select("LEMONADE");
	        vm.inputMoney(8);
	
	        vm.select("LOLA");
	        vm.inputMoney(8);

	        vm.turnOff();
	}
}
