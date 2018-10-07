import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			int[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new int[numJobs][2];
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

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		/*ProblemInstance instance = readInstance(args[0]);
		
		Greedy greedy = new Greedy(instance);
		Schedule greedySchedule = greedy.getSchedule();
		System.out.println(greedySchedule.getTardiness());
		
		BestFirst bestFirst = new BestFirst(instance);
		Schedule bestFirstSchedule = bestFirst.getSchedule();
		System.out.println(bestFirstSchedule.getTardiness());*/
    	
    	runTests();
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
    	for (int i = 0; i < testfiles.length; i++) {
    		String fileName = testfiles[i].getName();
    		//System.out.println(fileName);
    		runTest(testfolder+"\\"+fileName);

		}
    }
    
    public static void runTest(String filename) {
    	ProblemInstance BaseInstance = readInstance(filename);
    	ProblemInstance[] mutatedInstances = mutateTests(BaseInstance);
    	for(int i = 0; i<mutatedInstances.length;i++) {
    		ProblemInstance instance = mutatedInstances[i];
	    	int problemSize = instance.getNumJobs();
	    	//System.out.println("Problem of size: "+problemSize);
	    	Greedy greedy = new Greedy(instance);
	    	long greedyStart = System.nanoTime();
			Schedule greedySchedule = greedy.getSchedule();
			long greedyEnd = System.nanoTime();
			//System.out.println("Greedy result: "+greedySchedule.getTardiness()+" in time: "+((greedyEnd-greedyStart)/1000) + " ms ");
			
			/*BestFirst bestFirst = new BestFirst(instance);
	    	long bfStart = System.nanoTime();
			Schedule bestFirstSchedule = bestFirst.getSchedule();
			long bfEnd = System.nanoTime();*/
			//System.out.println("Best First result: "+bestFirstSchedule.getTardiness()+" in time: "+(bfEnd-bfStart) + " nanosec ");
			
			LawlerDP Lawler =new LawlerDP(instance);
			long lawStart = System.nanoTime();
			int LawlerTardiness = Lawler.SequenceJobs();
			long lawEnd = System.nanoTime();
	
			//System.out.println("Lawler result: "+LawlerTardiness+ " in time: "+((lawEnd-lawStart)/1000)+ " ms ");
			//Schedule LawlerSchedule = Lawler.getSchedule();
			System.out.println(problemSize+" "+((greedyEnd-greedyStart)/1000)+ " "+((lawEnd-lawStart)/1000)+ ";" );
			//System.out.println(problemSize+" "+((greedyEnd-greedyStart)/1000)+ " "+((bfEnd-bfStart)/1000)+ " "+((lawEnd-lawStart)/1000)+ ";" );
    	}
    }
    
    public static ProblemInstance[] mutateTests(ProblemInstance instance) {
    	int size = instance.getNumJobs();
    	int[][] jobs = instance.getJobs();
    	ProblemInstance[] res = new ProblemInstance[size];
    		for(int i = 0; i<size; i++) {
    			int sSize = i+1;
    			int[][] sJobs = Arrays.copyOfRange(jobs,0,sSize);
    			res[i] = new ProblemInstance(sSize,sJobs);
    		}
    	
    	return res;
    }
}
