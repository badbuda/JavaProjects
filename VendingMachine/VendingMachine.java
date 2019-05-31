package il.co.ilrd.vendingmachine;

import java.util.ArrayList;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class VendingMachine {

	private static StateVendingMachine stateVM;
	private static ArrayList<Product> arrProducts;
	private static Notifier note;
	private static LocalTime start_clock;
	static Thread timer_thread;

	enum StateVendingMachine {

		START {

			@Override
			protected StateVendingMachine turnOn() {
				note.write("Vending machine is on");
				start_clock = LocalTime.now();
				timer_thread.start();
				return WAITING_FOR_SELECTION;
			}
			
			@Override
			protected StateVendingMachine cancel() {
				note.write("Machine is Off");
				
				return START;
			}
		},

		WAITING_FOR_SELECTION {
			
			@Override
			protected StateVendingMachine select(String productName) {

				for (Product product : arrProducts) {
					if (product.getName().equals(productName)) {
						myProd = product;
						return WATING_FOR_MONEY;
				}
			
			}
					note.write(productName + " was not foun! please choose a different product\n");
				
				return WATING_FOR_MONEY;

			}

			@Override
			protected StateVendingMachine cancel() {
				note.write("selection canceled\n");

				return WATING_FOR_MONEY;
			}
		},
		  
		WATING_FOR_MONEY {

			@Override
			protected StateVendingMachine inputMoney(double money) {
				note.write(myProd.toString() + " costs "+ myProd.getPrice() +" shekels. \nyou've inserted " + money + "  shekel");

				if (money >= myProd.getPrice())
				{
					note.write("Thank you, Product supplied. \nyour change is: " + (money - myProd.getPrice()));
				}
				else
				{
					note.write("not enough  money");
				}

				return WAITING_FOR_SELECTION;
	       }

			@Override
			protected StateVendingMachine cancel() {
				note.write("waiting for money: canceled");
				return WAITING_FOR_SELECTION;
			}
			
			@Override
			protected StateVendingMachine timeOut() {
				if (ChronoUnit.MILLIS.between(start_clock,LocalTime.now()) >= 6000)
				{
					note.write("Timed_out: Please make new selection!\n");
					return WAITING_FOR_SELECTION;
				}
				return this;
			}
		};

		private static Product myProd;
		
		protected StateVendingMachine turnOn() {
			return this;
		}
		
		protected StateVendingMachine turnOff() {
			return START;
		}
		
		protected StateVendingMachine select(String productName) {
			return this;
		}
		
		protected StateVendingMachine timeOut() {
			return this;
		}

		protected StateVendingMachine inputMoney(double money) {
			return this;
		}

		protected abstract StateVendingMachine cancel();
	}
	
	public VendingMachine(ArrayList<Product> productList, Notifier noter) {
		note = noter;
		arrProducts = productList;
		stateVM = StateVendingMachine.START;
		timer_thread = new Thread ( new ThreadTimer() );
	}

	public void turnOn() {
		stateVM = stateVM.turnOn();
	}
	public void turnOff() {
		stateVM = stateVM.turnOff();
	}
	public void select(String productName) {
		stateVM = stateVM.select(productName);
		
	}

	public void inputMoney(double price) {
		stateVM = stateVM.inputMoney(price);
	}

	public void cancel() {
		stateVM = stateVM.cancel();
	}

	public class ThreadTimer implements Runnable{
				
	    public void run() {
	        while (stateVM != StateVendingMachine.START) {
	            try {
	                Thread.sleep(50);

	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            stateVM = stateVM.timeOut();
	        }
	    }
	}
}
