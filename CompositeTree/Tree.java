 package Tree;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import il.co.ilrd.factory
import il.co.ilrd.factory.Factory;
import il.co.ilrd.factory.Shape;

public class Tree {

	private CompositeFolder root;
	public Tree(String path) throws FileNotFoundException{
		Factory<String, Shape> factory = new Factory<String, Shape>();

		File folderName = new File(path);

		if (!(folderName.exists())) {
			throw new FileNotFoundException();
		}
		root = new CompositeFolder(path);
	}
	
	public void printTree() {
		root.print(0);
	}
	
	private abstract class Component {

		private String name;
		
		void print(int depth) {}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	private class CompositeFolder extends Component {	
		private List<Component> array = new ArrayList<Component>();
		
		private CompositeFolder(String path) {	

			File folderName = new File(path);
			setName(folderName.getName());
			File[] directoryListing = folderName.listFiles();
			
		    for (File sub : directoryListing) {
		    	if (true == sub.isDirectory()) {
		    		array.add(new CompositeFolder(sub.getAbsolutePath()));
		    	}
		    	else {

		    		array.add(new CompositeFile(sub.getName()));
		    	}
		    }				
		}
		
		@Override
		void print(int depth) {

			for (int i = 0; i < depth; ++i) {
				System.out.print("     ");
			}
			
			System.out.print("├── ");
			System.out.println(getName());

			for(int j = 0; j < array.size(); ++j) {
				array.get(j).print(depth + 1);
			}
		}
	}

	private class CompositeFile extends Component{
		
		private CompositeFile(String path) {
			setName(path);
		}

		@Override
		void print(int depth) {

			for (int i = 0; i < depth; ++i) {
				
				if (i + 1 == depth) {
					System.out.print("│    ");
				}
				else
				{
					System.out.print("     ");	
				}
			}
		
			System.out.print("├── ");
			System.out.println(getName());
		}
	}
	public static void main(String[] args) throws FileNotFoundException{
		Tree tree = new Tree ("/home");
		tree.printTree();
	}
}

