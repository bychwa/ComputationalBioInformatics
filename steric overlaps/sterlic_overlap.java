import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/*
	Author: Jackson Isack - 19900710-7510
	To compile run: javac sterlic_overlap.java
	To run with standard output: java sterlic_overlap 1CDH.pdb 2CSN.pdb
	To run with file output: java sterlic_overlap 1CDH.pdb 2CSN.pdb > 1CDH_2CSN.txt

*/

public class sterlic_overlap{
	
	private static FileInputStream fstream;
	private static BufferedReader br;

	private static int ATOM_RADIUS=2;
	private static int no_comparisons,no_clashes=0;
	private static String strLine;
		
	public static void main(String[] args) throws Exception{
		
		if(args.length==0 || args.length==1){
			System.err.println("usage: java sterlic_overlap file1.pdb file2.pdb");
			System.exit(0);
		}

		fstream = new FileInputStream(args[0]);
		br = new BufferedReader(new InputStreamReader(fstream));
		
		// Data structure to store the first set of atoms
		List<String[]> residueMatrix_one = new ArrayList<String[]>();
		
		while ((strLine = br.readLine()) != null)   {

			if(strLine.substring(0,4).equals("ATOM")){

				String[] splited = strLine.split("\\s+");
				
				String[] residueRow  = new String[7];
                residueRow[0] = splited[1];
                residueRow[4] = splited[2];
                residueRow[5] = splited[3];
                residueRow[6] = splited[5];
                
                residueRow[1] = splited[6];
                residueRow[2] = splited[7];
                residueRow[3] = splited[8];	                
                residueMatrix_one.add(residueRow);
			
			}
		}

		fstream = new FileInputStream(args[1]);
		br = new BufferedReader(new InputStreamReader(fstream));
		
		while ((strLine = br.readLine()) != null)   {

			if(strLine.substring(0,4).equals("ATOM")){

				String[] splited = strLine.split("\\s+");
			
				boolean overlaps=false;
				
				// Go through the first file atoms to check for collisions
				for(int j=0; j<residueMatrix_one.size(); j++){
					
					double x2=Double.parseDouble(splited[6]);
			  		double y2=Double.parseDouble(splited[7]);
			  		double z2=Double.parseDouble(splited[8]);

			  		double x1=Double.parseDouble(residueMatrix_one.get(j)[1]);
			  		double y1=Double.parseDouble(residueMatrix_one.get(j)[2]);
			  		double z1=Double.parseDouble(residueMatrix_one.get(j)[3]);
		  		
			  		double distance=Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)) + ((z2-z1)*(z2-z1)));
			  		

			  		no_comparisons+=1;
			  		// check if the distance between atom surfaces is less than zero i.e they overlap 
			  		if( (distance - (2 * ATOM_RADIUS) ) < 0){
			  			overlaps=true;
			  			break;
			  		}

				}

				if(overlaps){
					
					no_clashes+=1;
					System.out.println(splited[1]+"\t"+splited[3]+"\t"+splited[5]+"\t"+splited[2]);
			
				}

			}
		}


	
		System.out.println("Number of clashing atoms: "+no_clashes);
		System.out.println("Number of comparisons:"+no_comparisons);
		
		br.close();

	}
}