package il.co.ilrd.tut;


public class Person {
	
	private Name name;
	private Address address;
	
	public Person(Name name, Address address) {
		this.name = name; 
		this.address = address;
	}
	
	public static boolean isConnection(Person p1, Person p2) {
		return (p1.name.getName().equals(p2.name.getName()) && p1.name.getLastName().equals(p2.name.getLastName())) || 
				(p1.address.getCity().equals(p2.address.getCity()) && p1.address.getStreet().equals(p2.address.getStreet()));
	}
}

class Name {

	private String name;
	private String lastName;
	
	public Name(String name, String lastName) {
		this.name = name; 
		this.lastName = lastName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}

class Address {

	private String street;
	private String city;
		
	public Address(String street, String city) {
		this.street = street;
		this.city = city;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public void setCity(String city) {
		this.city = city;
	}	
}
 