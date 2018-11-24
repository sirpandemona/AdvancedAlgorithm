import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			//int[][] jobs = null;
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
    	
		//ProblemInstance instance = readInstance(args[1]);
		//LawlerApprox lawler = new LawlerApprox(instance,Double.parseDouble(args[0]));
		//double result =  lawler.SequenceJobs();
    	//System.out.print(result);
    	runTests();
    	//args = new String[]{"1.1","instances/random_RDD=0.2_TF=0.2_#05.dat"};
		ProblemInstance instance = readInstance(args[1]);
		
		LawlerDP lawlerdp = new LawlerDP(instance);
		double resultdp = lawlerdp.SequenceJobsDynamic();
		System.out.println(resultdp);
		
		//LawlerApprox lawlerap = new LawlerApprox(instance,Double.parseDouble(args[0]));
		//double resultap =  lawlerap.SequenceJobs();
    	//System.out.print(resultap);
	}
    
    public static void runTests() {
     	Path currentRelativePath = Paths.get("");
     	String testfolder = currentRelativePath.toAbsolutePath().toString()+"\\Exercise 1\\instances"; 
     	testfolder = testfolder.replace("\\", "/");
     	System.out.println(testfolder);
    	File folder = new File(testfolder);
    	
    	File[] testfiles = folder.listFiles();
    	Arrays.sort(testfiles, new Comparator<File>() {
    		public int compare(File f1, File f2) {
    			String s1 = f1.getName();
    			String s2 = f2.getName();
    			s1 = s1.replace(".dat","");
    			s2 = s2.replace(".dat","");
    			Integer size1 = Integer.parseInt((s1.split("#")[1]));
    			Integer size2 = Integer.parseInt((s2.split("#")[1]));
    			return size1.compareTo(size2);
    		}
    	});
    	FileWriter writer;
    	int numberFiles = testfiles.length;
    	try {
			writer = new FileWriter("results.csv");
    	for (int i = 0; i < numberFiles; i++) {
    		String fileName = testfiles[i].getName();
    		double[][] results = runTest(testfolder+"\\"+fileName);
    		for(int k = 0; k< results.length;k++) {
    			double[] res = results[k];
				for(int j = 0; j<11;j++) {
					writer.append(String.valueOf(res[j])+",");
				}
				writer.append("\n");
    		}
 		}
    	writer.close();
    	}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static double[][]  runTest(String filename) {
    	ProblemInstance BaseInstance = readInstance(filename);
    	ProblemInstance[] mutatedInstances = mutateTests(BaseInstance);
    	String [] splt = filename.split("=");
    	double rdd = Double.parseDouble(splt[1].substring(0, 3));
    	double tf = Double.parseDouble(splt[2].substring(0, 3));
    	double[][] results = new double[mutatedInstances.length][11];
		FileWriter writer;
		try {
			writer = new FileWriter(mutatedInstances.length+ "-"+rdd+"-"+tf+"results.csv");
		
    	for(int i = 0; i<mutatedInstances.length;i++) {
    		ProblemInstance instance = mutatedInstances[i];
    		double[] res = new double[11];
	    	int problemSize = instance.getNumJobs();
    		res[0] = problemSize;
    		res[1] = rdd;
    		res[2] = tf;

	    	Greedy greedy = new Greedy(instance);
	    	long greedyStart = System.nanoTime();
			Schedule greedySchedule = greedy.getSchedule();	
			long greedyEnd = System.nanoTime();	
			res[3] = greedySchedule.getTardiness();
			res[4] = ((greedyEnd-greedyStart)/1000);
			if(problemSize < 11) {
				BestFirst bestFirst = new BestFirst(instance);
		    	long bfStart = System.nanoTime();
				Schedule bestFirstSchedule = bestFirst.getSchedule();
				long bfEnd = System.nanoTime();
				
				res[5] = bestFirstSchedule.getTardiness();
				res[6] = ((bfEnd-bfStart)/1000);
			}
			else {
				res[5] = 0;
				res[6] = 0;
			}
			
			LawlerDP Lawler =new LawlerDP(instance);
			long lawStart = System.nanoTime();
			double LawlerTardiness = Lawler.SequenceJobs();
			long lawEnd = System.nanoTime();
			res[7] = LawlerTardiness;
			res[8] = ((lawEnd-lawStart)/1000);
			
			double epsilon = 0.5;
			LawlerApprox lawapx= new LawlerApprox(instance,epsilon);
			long lawaStart = System.nanoTime();
			double LawleraTardiness = lawapx.SequenceJobs();
			//double LawleraTardiness = 0;
			long lawaEnd = System.nanoTime();
			res[9] = LawleraTardiness;
			res[10] =((lawaEnd-lawaStart)/1000);
			results[i] = res;
			System.out.println(resultsToString(res));
			for(int j = 0; j<11;j++) {
				writer.append(String.valueOf(res[j])+",");
			}
			writer.append("\n");
    	}
    	writer.close();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return results;
    }
    
    public static String resultsToString(double[] res) {
    	String result = "";
    	for(int i =0; i< res.length; i++) {
    		result += "- "+res[i]+ ";";
    	}
    	return result;
    } 
    
    public static ProblemInstance[] mutateTests(ProblemInstance instance) {
    	int size = instance.getNumJobs();
    	double[][] jobs = instance.getJobsDouble();
    	ProblemInstance[] res = new ProblemInstance[size];
    		for(int i = 0; i<size; i++) {
    			int sSize = i+1;
    			double[][] sJobs = Arrays.copyOfRange(jobs,0,sSize);
    			res[i] = new ProblemInstance(sSize,sJobs);
    		}
    	
    	return res;
    }
}
