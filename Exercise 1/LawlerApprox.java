import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class LawlerApprox {
	private int numJobs;
	private double[][] jobs;
	private HashMap<String,ArrayList<Integer>> jobMapping;
	private HashMap<String,ArrayList<int[]>> states;
	private double epsilon;
		
	public LawlerApprox(ProblemInstance instance,double epsilon) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobsDouble();
		states = new HashMap<String,ArrayList<int[]>>();
		jobMapping = new HashMap<String,ArrayList<Integer>>();
		this.epsilon = epsilon;
	}
	
	public void sortJobsByDueTime() {
		Arrays.sort(jobs, new Comparator<double[]>() {
			
			public int compare(double[] j1, double[] j2) {
				Double dd1 = j1[1];
				Double dd2 = j2[1];
				return dd1.compareTo(dd2);
			}
		});
	}
	
	public double getTardinessMax() {
		double t = 0;
		double maxTardiness = 0;
		for(double[] j : jobs) {
			t += j[0];
			double tardiness = Math.max(t - j[1],0);
			maxTardiness = Math.max(tardiness, maxTardiness);
		}
		return maxTardiness;
	}
	
	public double SequenceJobs() {
		sortJobsByDueTime();
		double maxTardiness = getTardinessMax();
		if(maxTardiness == 0) {
			return 0;
		}
		double k = 2*epsilon/numJobs/(numJobs+1)*maxTardiness;
		double[][] scaledJobs = new double[numJobs][2];
		for(int i = 0; i < numJobs; i++) {
			scaledJobs[i][0] = Math.floor(jobs[i][0]/k);
			scaledJobs[i][1] = jobs[i][1]/k;
		}
		ProblemInstance scaledInstance = new ProblemInstance(numJobs,scaledJobs);
		LawlerDP lawler = new LawlerDP(scaledInstance);
		lawler.SequenceJobs();
		return -1;
	}
}
