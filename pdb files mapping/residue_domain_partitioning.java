import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class residue_domain_partitioning{

	private static String strLine;
		
	public static void main(String[] args) throws Exception{
		if(args.length==0){
			System.err.println("usage: java plot_distance_map file.pdb");
			System.exit(0);
		}

		FileInputStream fstream = new FileInputStream(args[0]);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		List<String[]> residueMatrix = new ArrayList<String[]>();
		List<int[]> alpharesidueMatrix = new ArrayList<int[]>();
		List<int[]> domakScoreMatrix = new ArrayList<int[]>();


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

		// small change is split size
		int dt=0; 
		//our minimum size of a domain
		int MIN_SIZE=2;
				
		while(dt + MIN_SIZE < residueMatrix.size()){

			//divide segment to two parts
				int split_index=dt+MIN_SIZE;
				
				//initial values
				int INT_A=0;
				int INT_B=0;
				int EXT_AB=0;
				
				//int a segment = 0 --> split_index;
				alpharesidueMatrix = new ArrayList<int[]>();
				for (int i=0;i < split_index; i++) {
					for(int j=0; j < split_index; j++){
					
						if(i!=j){

							double x1=Double.parseDouble(residueMatrix.get(i)[1]);
					  		double y1=Double.parseDouble(residueMatrix.get(i)[2]);
					  		double z1=Double.parseDouble(residueMatrix.get(i)[3]);

					  		double x2=Double.parseDouble(residueMatrix.get(j)[1]);
					  		double y2=Double.parseDouble(residueMatrix.get(j)[2]);
					  		double z2=Double.parseDouble(residueMatrix.get(j)[3]);
				  			
					  		// calculate if they are in interacting
					  		double distance=Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)) + ((z2-z1)*(z2-z1)));
					  		if(distance < 8){
					  			
					  			boolean exists=false; // to avoid repeating atoms i.e 1-2 be equal to 2-1
					  			for(int t=0; t < alpharesidueMatrix.size(); t++){
									if(alpharesidueMatrix.get(t)[1]==Integer.parseInt(residueMatrix.get(i)[0])){
										exists=true;
									}
								}
								if(!exists){
									INT_A++; 
									int[] validresidueRow  = new int[2];
				                    validresidueRow[0] = Integer.parseInt(residueMatrix.get(i)[0]);
					                validresidueRow[1] = Integer.parseInt(residueMatrix.get(j)[0]);	                
					                alpharesidueMatrix.add(validresidueRow);
								}
					  			
					  		}
					  	}	
					}
				}

				//int b segment = split_index+1 --> alpharesidueMatrix.size()
				alpharesidueMatrix = new ArrayList<int[]>();
				for (int i=split_index; i < residueMatrix.size(); i++) {
					for(int j=split_index; j < residueMatrix.size(); j++){
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
									INT_B++;
									int[] validresidueRow  = new int[2];
				                    validresidueRow[0] = Integer.parseInt(residueMatrix.get(i)[0]);
					                validresidueRow[1] = Integer.parseInt(residueMatrix.get(j)[0]);	                
					                alpharesidueMatrix.add(validresidueRow);
								}
					  			
					  		}
					  	}	
					}	
				}

				// ext ab segment i.e for each atom in seg a, check if interacting with atom in seg b 
				alpharesidueMatrix = new ArrayList<int[]>();
				for (int i=0;i < split_index; i++) {
					for(int j=split_index; j < residueMatrix.size(); j++){
					
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
									EXT_AB++;
									int[] validresidueRow  = new int[2];
				                    validresidueRow[0] = Integer.parseInt(residueMatrix.get(i)[0]);
					                validresidueRow[1] = Integer.parseInt(residueMatrix.get(j)[0]);	                
					                alpharesidueMatrix.add(validresidueRow);
								}
					  			
					  		}
					  	}	
					}
				}

				// recording results for later use
				int[] scroreRow  = new int[5];
	                scroreRow[0] = split_index;
	                scroreRow[1] = INT_A;
	                scroreRow[2] = INT_B;
	                scroreRow[3] = EXT_AB;
	                scroreRow[4] = ((INT_A/EXT_AB)*(INT_B/EXT_AB));	 
	            domakScoreMatrix.add(scroreRow);

			// increment split value before going for another iteration
			dt+=1;		
		}

		//check the values from domakScoreMatrix
		// for (int i=0; i<domakScoreMatrix.size();i++) {
		// 	System.out.println("SPLIT="+domakScoreMatrix.get(i)[0]+"\t INT_A="+domakScoreMatrix.get(i)[1]+"\t INT_B="+domakScoreMatrix.get(i)[2]+"\t EXT_AB="+domakScoreMatrix.get(i)[3]+ "\t SCORE="+domakScoreMatrix.get(i)[4]);
		// }

		// check split value with maximum score
		int max_score_index=maximum_score_index(domakScoreMatrix);

		System.out.println("\nBest Score:");
		System.out.println("SPLIT="+domakScoreMatrix.get(max_score_index)[0]+"\t INT_A="+domakScoreMatrix.get(max_score_index)[1]+"\t INT_B="+domakScoreMatrix.get(max_score_index)[2]+"\t EXT_AB="+domakScoreMatrix.get(max_score_index)[3]+ "\t SCORE="+domakScoreMatrix.get(max_score_index)[4]);
		System.out.println("\nMost Clear Partition is at Residue:");
		System.out.println("CA\t"+residueMatrix.get(max_score_index)[0]);
				

		br.close();

	}

	public static int maximum_score_index(List domakScoreMatrix){
	    int max = Integer.MIN_VALUE;
	    int max_index=0;
	    for(int i=0; i<domakScoreMatrix.size(); i++){
	        int[] scroreRow = (int[])domakScoreMatrix.get(i);
	        if(scroreRow[4] > max){
	            max = scroreRow[4];
	            max_index=i;
	        }
	    }
	    return max_index;
	}
}