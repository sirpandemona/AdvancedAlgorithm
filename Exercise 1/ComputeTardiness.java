import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			double[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new double[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = sc.nextInt();
					jobs[nextJobID][1] = sc.nextInt();
					nextJobID++;
				}
			}
			sc.close();
			
			instance = new ProblemInstance(numJobs, jobs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return instance;
	}

    public static void main (String args[]) {
    	args = new String[]{"1.1","instances/random_RDD=0.2_TF=0.2_#05.dat"};
		ProblemInstance instance = readInstance(args[1]);
		
		LawlerDP lawlerdp = new LawlerDP(instance);
		double resultdp = lawlerdp.SequenceJobsDynamic();
		System.out.println(resultdp);
		
		//LawlerApprox lawlerap = new LawlerApprox(instance,Double.parseDouble(args[0]));
		//double resultap =  lawlerap.SequenceJobs();
    	//System.out.print(resultap);
	}
    
}
