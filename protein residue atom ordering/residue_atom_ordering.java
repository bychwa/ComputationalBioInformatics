import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*

	Author: Jackson Isack - 19900710-7510
	To compile run: javac residue_atom_ordering.java
	To run with standard output: java residue_atom_ordering p3.pdb > output.txt

*/

public class residue_atom_ordering{
	
	private static class Point{
		public double x, y, z;
		public Point(double x,double y,double z){
			this.x=x; this.y=y; this.z=z;
		}

	}

	private static class Atom{
		public String name;
		public Point	position;
		public Atom(String name, Point p){
			this.name=name; this.position=p;
		}

	}

	private static String strLine;

	//calculates the square of the value
	private static double square (double a) {
	    return a*a;
	}

	// calculates the distance btn atoms
	private static double distance (Point a, Point b) {
	    return Math.sqrt(square(a.x - b.x) + square(a.y - b.y) + square(a.z - b.z));
	}

	//checks if the value already exists in a list
	private static boolean value_exists(int value, int[] list) {
	    
	    int size=list.length;

	    for (int i=0; i < list.length; i++) {
	        if (value == list[i]) {
	            return true;
	        }
	    }
	    return false;
	}

	// finds and return the next nearest atom in the atomMatrix list
	private static int next_nearest_atom(int i, int[] orderedAtomList, List<Atom> atomMatrix) {
	   	
	   	double shortest=1000;  // initial max value
	   	int shortest_index=0;  // initial index value

	    for (int j=0; j < orderedAtomList.length; j++){
	    	
	    	if(i != j && value_exists(j, orderedAtomList) == false){
	    		
	    		double distance = distance(atomMatrix.get(i).position, atomMatrix.get(j).position) - 3.8;
	    		
	    		if (shortest > distance){
	    			
	    			shortest=distance;
	    			shortest_index=j;

	    		}

	    	}else{
	    		continue;
	    	}
	        
	    }

	    return shortest_index;
	}

	public static void main(String[] args) throws Exception{
		
		if(args.length==0){
			System.err.println("usage: java make_distance_map file.pdb");
			System.exit(0);
		}

		FileInputStream fstream = new FileInputStream(args[0]);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		List<Atom> atomMatrix = new ArrayList<Atom>();

		while ((strLine = br.readLine()) != null)   {
			
			if(strLine.substring(0,4).equals("ATOM")){

				String[] splited = strLine.split("\\s+");
				
				if(splited[2].equals("CA")){

					Point p = new Point(Double.parseDouble(splited[6]),Double.parseDouble(splited[7]),Double.parseDouble(splited[8]));
					
					Atom atom = new Atom(splited[5],p); // Atom(name,point);
						 atomMatrix.add(atom);

				}
			
			}
		}

		int numAtoms=atomMatrix.size();
		int starting_index=numAtoms/2; //sets starting value. can be altered to find perfect solution
		
		// initialize the orderedAtomlist
		int[] orderedAtomList = new int[numAtoms];
			  orderedAtomList[0] = Integer.parseInt(atomMatrix.get(starting_index).name); 

    	for (int i=1; i < numAtoms; i++) {
    		//populates orderedAtomList on each iteration
	    	orderedAtomList[i] = next_nearest_atom (orderedAtomList[i-1], orderedAtomList, atomMatrix);
	   
	    }

	    for (int i=0; i < numAtoms; i++) {

            System.out.println(atomMatrix.get(orderedAtomList[i]).name);

	    }

		br.close();

	}

}