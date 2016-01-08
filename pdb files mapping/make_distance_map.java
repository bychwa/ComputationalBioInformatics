import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*

	Author: Jackson Isack - 19900710-7510
	To compile run: javac make_distance_map.java
	To run with standard output: java make_distance_map filename.pdb

*/

public class make_distance_map{
	
	private static String strLine;
		
	public static void main(String[] args) throws Exception{
		if(args.length==0){
			System.err.println("usage: java make_distance_map file.pdb");
			System.exit(0);
		}

		FileInputStream fstream = new FileInputStream(args[0]);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		List<String[]> residueMatrix = new ArrayList<String[]>();
		List<int[]> alpharesidueMatrix = new ArrayList<int[]>();


		while ((strLine = br.readLine()) != null)   {
			
			if(strLine.substring(0,4).equals("ATOM")){

				String[] splited = strLine.split("\\s+");
				
				if(splited[2].equals("CA")){

					String[] residueRow  = new String[4];
	                residueRow[0] = splited[5];
	                residueRow[1] = splited[6];
	                residueRow[2] = splited[7];
	                residueRow[3] = splited[8];	                
	                residueMatrix.add(residueRow);
				}
			
			}
		}

		for(int i=0; i<residueMatrix.size(); i++){
			
			for(int j=0; j < residueMatrix.size(); j++){
				
				if(i!=j){

					double x1=Double.parseDouble(residueMatrix.get(i)[1]);
			  		double y1=Double.parseDouble(residueMatrix.get(i)[2]);
			  		double z1=Double.parseDouble(residueMatrix.get(i)[3]);

			  		double x2=Double.parseDouble(residueMatrix.get(j)[1]);
			  		double y2=Double.parseDouble(residueMatrix.get(j)[2]);
			  		double z2=Double.parseDouble(residueMatrix.get(j)[3]);
		  		

			  		double distance=Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)) + ((z2-z1)*(z2-z1)));
			  		if(distance < 8){
			  			
			  			boolean exists=false;
			  			for(int t=0; t < alpharesidueMatrix.size(); t++){
							if(alpharesidueMatrix.get(t)[1]==Integer.parseInt(residueMatrix.get(i)[0])){
								exists=true;
							}
						}
						if(!exists){
							int[] validresidueRow  = new int[2];
		                    validresidueRow[0] = Integer.parseInt(residueMatrix.get(i)[0]);
			                validresidueRow[1] = Integer.parseInt(residueMatrix.get(j)[0]);	                
			                alpharesidueMatrix.add(validresidueRow);	
						}
			  			
			  		}
			  	}	
			}
		}
		for(int i=0; i < alpharesidueMatrix.size(); i++){
			System.out.println(alpharesidueMatrix.get(i)[0]+"\t"+alpharesidueMatrix.get(i)[1]);
		}
		br.close();

	}
}