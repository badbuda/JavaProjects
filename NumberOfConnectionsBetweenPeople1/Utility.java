package il.co.ilrd.tut;


import java.util.*;


public class Utility {
	
	List <Node> listOfNodes; 
	
	public void init(Person[] persons) {
		// Create a list for each person with it's connections to the other persons  
		listOfNodes = new LinkedList<Utility.Node>();
		
		for (int i = 0; i < persons.length; ++i) {
			listOfNodes.add(new Node(i, persons));
		}
	}
	
	public int findMinRelationLevel(Person personA, Person personB) {
		//this array represents the distance between the source and each node
		int[] distanceArr = new int[listOfNodes.size()];
		
		for (int i = 0; i < distanceArr.length; ++i) {
			// Initialize the array to max distance 
			distanceArr[i] = Integer.MAX_VALUE; 
		}
		
		Node nodeA = getNodeByPerson(personA);
		Node nodeB = getNodeByPerson(personB);
		
		// Initialize the source node distance to zero 
		distanceArr[nodeA.nodeId] = 0;
		
		int distanceFromAtoB = recPath(nodeA, nodeB, distanceArr);
		
		return distanceFromAtoB;
	}
	
	private int recPath(Node src, Node dst, int[] distanceArr) {
		//the function receives source and destination and checks all the connection of the source and recursively updates the distances
		
		// stop condition - reached the destination node 
		if (src.nodeId == dst.nodeId) {
			return distanceArr[src.nodeId];
		}
		
		int distance = Integer.MAX_VALUE;
		
		for (int conId : src.connections) {
			// In case there will be an improvement in the distance from source node to the  destination through con node			
			if (distanceArr[conId] > (distanceArr[src.nodeId] + 1)) {
				
				distanceArr[conId] = distanceArr[src.nodeId] + 1;
				Node connectionNode = getNodeById(conId);
				distance = Math.min(distance, recPath(connectionNode, dst, distanceArr));
			}
		}
		
		//in case there is no relationship return -1
		if (distance == Integer.MAX_VALUE) {
			return -1;
		}
		
		return distance;
	}
	
	private Node getNodeById(int nodeId) {
		for (Node node : listOfNodes) {
			if (node.nodeId == nodeId) {
				return node;				
			}
		}
		
		return null;
	}
	
	private Node getNodeByPerson(Person person) {
		for (Node node : listOfNodes) {
			if (person.equals(node.nodePerson)) {
				return node;				
			}
		}
		
		return null;
	}
	
	private class Node {

		private Person nodePerson;
		private int nodeId;
		private List<Integer> connections;
		
		Node(int nodeId, Person[] person) {
			nodePerson = person[nodeId];
			this.nodeId = nodeId;
			
			connections = new LinkedList<Integer>();
			
			for (int i = 0; i < person.length; ++i) {
				if (i != nodeId) {
					if (Person.isConnection(person[nodeId], person[i])) {
						connections.add(i);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {

		Person[] personArr = new Person[4];
		Person[] personArr2 = new Person[4];

		personArr[0] = (new Person(new Name("Jerry", "Se"), new Address("NYC", "ny")));
		personArr[1] = (new Person(new Name("Jerry", "Se"), new Address("TLV", "tl")));
		personArr[2] = (new Person(new Name("George", "Co"), new Address("TLV", "tl")));
		personArr[3] = (new Person(new Name("George", "Co"), new Address("JSL", "js")));
		
		Utility utility = new Utility();
		utility.init(personArr);
		
		int distance = utility.findMinRelationLevel(personArr[0], personArr[3]);
		
		System.out.println("The minimal distance is " + distance);
	
		personArr2[0] = (new Person(new Name("arik", "a"), new Address("tlv", "")));
		personArr2[1] = (new Person(new Name("josie", "a"), new Address("modiin", "")));
		personArr2[2] = (new Person(new Name("shahar", ""), new Address("jerusalem", "")));
		personArr2[3] = (new Person(new Name("moshe", ""), new Address("haifa", "")));
		
		utility.init(personArr2);
		distance = utility.findMinRelationLevel(personArr2[0], personArr2[3]);
		System.out.println("The minimal distance is " + distance);
	}
}