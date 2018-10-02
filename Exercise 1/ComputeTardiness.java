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
    	for (int i = 0; i < testfiles.length; i++) {
    		String fileName = testfiles[i].getName();
    		runTest(testfolder+"\\"+fileName);

		}
    }
    
    public static void runTest(String filename) {
    	ProblemInstance instance = readInstance(filename);
    	Greedy greedy = new Greedy(instance);
		Schedule greedySchedule = greedy.getSchedule();
		System.out.println(greedySchedule.getTardiness());
		
		BestFirst bestFirst = new BestFirst(instance);
		Schedule bestFirstSchedule = bestFirst.getSchedule();
		System.out.println(bestFirstSchedule.getTardiness());
		LawlerDP Lawler =new LawlerDP(instance);
		int LawlerTardiness = Lawler.SequenceJobs();
		System.out.println(LawlerTardiness);
		//Schedule LawlerSchedule = Lawler.getSchedule();
    }
}
