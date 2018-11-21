import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class LawlerDP {
	private int numJobs;
	private double[][] jobs;
	private HashMap<String,ArrayList<Integer>> jobMapping;
	private HashMap<String,ArrayList<int[]>> states;
		
	public LawlerDP(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobsDouble();
		states = new HashMap<String,ArrayList<int[]>>();
		jobMapping = new HashMap<String,ArrayList<Integer>>();
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
	
	
	public ArrayList<Integer> GenCandidateDeltas(ArrayList<Integer> subset,  int t) {
		int k = retrieveKPrime(subset);
		ArrayList<Integer> sPrime2;
		ArrayList<Integer> deltas = new ArrayList<Integer>();
		double dk = jobs[k][1];
		do {
			ArrayList<Integer> sPrime = retrieveSPrime(subset,dk);		
			int dkPrime = t;
			for(int job : sPrime) {
				dkPrime += jobs[job][0];
			}
			while(dkPrime > dk) {
				dk = dkPrime;
				sPrime = retrieveSPrime(subset,dk);
				dkPrime = t;
				for(int job : sPrime) {
					dkPrime += jobs[job][0];
				}
			}
			int j = retrieveMaxJ(subset,dk);
			int delta  = j-k;
			deltas.add(delta);
			sPrime2 = new ArrayList<Integer>();
			for(int job : subset) {
				if(jobs[job][1] > dk) {
					sPrime2.add(job);
				}
			}
			if(sPrime2.size() >0) {
				int jprime = sPrime2.get(0);
				dk = jobs[jprime][1];
			}
		} while(sPrime2.size() >0 );
		return deltas;	
	}
	
	public int retrieveMaxJ(ArrayList<Integer> subset, double dk) {
		int j = -1;
		for(int job: subset) {
			if(jobs[job][1] <= dk) {
				j = job;
			}
		}
		return j;
	}
	
	public ArrayList<Integer> retrieveSPrime(ArrayList<Integer>subset,double dk){
		ArrayList<Integer> sPrime = new ArrayList<Integer>();
		for(int job : subset) {
			if(jobs[job][1] <= dk) {
				sPrime.add(job);
			}
		}
		return sPrime;
	}
	
	
	public ArrayList<int[]> generateStates(int i, int j, int k, int t) {
		String key  = i + " - " + j + " - "+ k + " - "+t;
		if(states.containsKey(key)){
			return states.get(key);
		}
		ArrayList<int[]> L = new ArrayList<int[]>();
		ArrayList<Integer> subset = retrieveSubset(i,j,k);
		if(subset.size() == 0) {
			return L;
		}
		else if(subset.size() == 1){
			return L;
		}

		
		int kPrime = retrieveKPrime(subset);
		int C = retrieveC(subset,kPrime,t);
		ArrayList<Integer> deltas =  GenCandidateDeltas(subset,t);
		int prevDelta = 0;
		for(int di  = 0;di<deltas.size();di++) {
			int d = deltas.get(di);
			for(int pd = prevDelta+1 ;pd<d;pd++) {
				C += jobs[kPrime+pd][0];
			}
			prevDelta =d;			
			C += jobs[kPrime+d][0];
			
			int[] sLeft = {i,kPrime+d,kPrime,t};
			int[] sRight = {kPrime+d+1, j, kPrime,C};
						
			L.add(sLeft);
			L.add(sRight);
			L.addAll(generateStates(i,kPrime+d,kPrime,t));
			L.addAll(generateStates(kPrime+d+1, j, kPrime,C));
		}
		states.put(key,L);
		return L;
	}
	
	public ArrayList<Integer> retrieveSubset(int i, int j, int k) {
		String key = i+" - "+j+" - "+k;
		if (jobMapping.containsKey(key)){
			return jobMapping.get(key);
		}
		ArrayList<Integer> subset = new ArrayList<Integer>(); 
		if(i> j) {return subset;}
		if(j>= numJobs) {j = numJobs-1;}
		double valueK = Double.MAX_VALUE;
		if(k>-1) {
			valueK = jobs[k][0];
		}
		for(int job =i; job<=j;job++) {
			if(jobs[job][0] < valueK || (jobs[job][0] == valueK && job < k)) {
				subset.add(job);
			}
		}
		jobMapping.put(key,subset);
		return subset;
	}
	
	public int retrieveKPrime(ArrayList<Integer> subset) {
		int kPrime =0;
		double val = 0;
		for(int job: subset) {
			if(jobs[job][0] >= val) {
				kPrime = job;
				val  = jobs[job][0] ;
			}
		}
		return kPrime;
	}
	
	public int retrieveC(ArrayList<Integer> subset, int kPrime, int t) {
		int time = t;
		for(int job : subset) {
			if(job >= kPrime)
			{
				break;
			}
			double p_j = jobs[job][0];
			time+= p_j;
		}
		return time;
	}
	
	public double SequenceJobs() {
		sortJobsByDueTime();
		int[] startState = {0,jobs.length-1,-1,0};
		ArrayList<int[]> L = generateStates(0, jobs.length-1, -1, 0);
		Collections.reverse(L);
		L.add(startState);
		HashMap<String,Double> T = new HashMap<String,Double>();
		
		for(int[] S : L) {							
			int i = S[0];
			int j = S[1];
			int oldk = S[2];
			int t = S[3];
			ArrayList<Integer> subset = retrieveSubset(i,j,oldk);
			int k = retrieveKPrime(subset);
			String key = i + " - " + j + " - " + oldk + " - " + t;
			if(subset.size() < 1) {
				T.put(key,0.0);
			}
			else if(subset.size() == 1) {
				int index = subset.get(0);
				T.put(key, Math.max(0,t+jobs[index][0]-jobs[index][1]));
			}
			else if(S.length > 4) {
				T.put(key, (double)S[4]);
			}
			else {
				T.put(key, Double.MAX_VALUE);
				int C = retrieveC(subset,k,t);
				ArrayList<Integer> deltas =  GenCandidateDeltas(subset,t);
				int prevDelta = 0;
				for(int di  = 0;di<deltas.size();di++) {
					int delta = deltas.get(di);
					for(int pd = prevDelta+1 ;pd<delta;pd++) {
						C += jobs[k+pd][0];
					}
					prevDelta =delta;
					C += jobs[k+delta][0];
					T.put(key, Math.min( T.get(key),	T.get(i + " - " + (k+delta) + " - " + k + " - " + t)		+
												 		Math.max(0, C - jobs[k][1])									+
												 		T.get((k+delta+1) + " - " + j + " - " + k + " - " + C)		));
				}
			}
		}

		return T.get("0 - "+(jobs.length-1)+" - -1 - 0");
	}
}
