
public class ProblemInstance {
	private int numJobs;
	private double[][] jobs; //size = [num_jobs][2], for every job [0] is the length, [1] is the due time
	private int[][] Ijobs;
	
	public ProblemInstance(int numJobs, double[][] jobs) {
		this.numJobs = numJobs;
		this.jobs = jobs;
		genIntJobs();
	}
	
	private void genIntJobs() {
		Ijobs = new int[numJobs][2];
		for(int i =0; i<numJobs;i++) {
			Ijobs[i][0] =(int) jobs[i][0]; 
			Ijobs[i][1] =(int) jobs[i][1]; 
		}
		
	}
	
	public int getNumJobs() {
		return numJobs;
	}
	
	public double[][] getJobsDouble() {
		return jobs;
	}
	
	public int[][] getJobs(){
		return Ijobs;
	}
}
