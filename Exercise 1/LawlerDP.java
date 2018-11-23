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
		jobs = instance.getJobs();
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
	
	public double totalCompletionTime() {
		double sum = 0;
		for(double[] job : jobs) {
			sum += job[0];
		}
		return sum;
	}
	
	public double smallestProcessingTime() {
		double smallestProcessingTime = Double.MAX_VALUE;
		for(double[] job: jobs) {
			if(job[0] < smallestProcessingTime) {
				smallestProcessingTime = job[0];
			}
		}
		return smallestProcessingTime;
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
				ArrayList<Integer> deltas = GenCandidateDeltas(subset,t);
				int prevDelta = 0;
				for(int di  = 0;di<deltas.size();di++) {
					int delta = deltas.get(di);
					for(int pd = prevDelta+1 ;pd<delta;pd++) {
						C += jobs[k+pd][0];
					}
					prevDelta = delta;
					C += jobs[k+delta][0];
					T.put(key, Math.min( T.get(key),	T.get(i + " - " + (k+delta) + " - " + k + " - " + t)		+
												 		Math.max(0, C - jobs[k][1])									+
												 		T.get((k+delta+1) + " - " + j + " - " + k + " - " + C)		));
				}
			}
		}

		return T.get("0 - "+(jobs.length-1)+" - -1 - 0");
	}
	
	public double SequenceJobsDynamic()
	{
		sortJobsByDueTime();
		
		HashMap<String,Double> T = new HashMap<String,Double>();
		HashMap<String,Integer> O = new HashMap<String,Integer>();
		double totalCompletionTime = totalCompletionTime();
		
		ArrayList<int[]> states = new ArrayList<int[]>();
		for(int i = 0; i < numJobs; i++)
		{
			for(int j = i; j < numJobs; j++)
			{
				for(int oldk = 0; oldk < numJobs; oldk++)
				{
					ArrayList<Integer> subset = retrieveSubset(i,j,oldk);
					int len = subset.size();
					states.add(new int[]{i,j,oldk,len});
				}
				
			}
		}
		states.sort(new Comparator<int[]>() {
			public int compare(int[] s1, int[] s2) {
				Integer dd1 = s1[3];
				Integer dd2 = s2[3];
				return dd1.compareTo(dd2);
			}
		});
		states.add(new int[]{numJobs,numJobs-1,numJobs-1,0});
		states.add(new int[]{0,numJobs-1,-1,numJobs});
		
		for(int[] state : states)
		{
			int i = state[0];
			int j = state[1];
			int oldk = state[2];
			ArrayList<Integer> subset = retrieveSubset(i,j,oldk);
					int k = retrieveKPrime(subset);
					for(int t = 0; t <= totalCompletionTime; t++)
					{
						String key = i + " - " + j + " - " + oldk + " - " + t;
						if(subset.size() < 1) {
							T.put(key,0.0);
						}
						else if(subset.size() == 1) {
							int index = subset.get(0);
							T.put(key, Math.max(0,t+jobs[index][0]-jobs[index][1]));
						}
						else {
							T.put(key, Double.MAX_VALUE);
							int C = retrieveC(subset,k,t);
							ArrayList<Integer> deltas = GenCandidateDeltas(subset,t);
							int prevDelta = -1;
							for(int di  = 0;di<deltas.size();di++) {
								int delta = deltas.get(di);
								for(int pd = prevDelta+1 ;pd<delta;pd++) {
									C += jobs[k+pd][0];
								}
								prevDelta = delta;
								C += jobs[k+delta][0];
								if(C <= totalCompletionTime) {
									double firstSubProblem;
									double secondSubProblem;
									if(i > k+delta) {
										firstSubProblem = 0;
									} else {
										firstSubProblem = T.get(i + " - " + (k+delta) + " - " + k + " - " + t);
									}
									if(k+delta+1 > j) {
										secondSubProblem = 0;
									} else {
										secondSubProblem = T.get((k+delta+1) + " - " + j + " - " + k + " - " + C);
									}
									double tardiness = firstSubProblem + Math.max(0, C - jobs[k][1]) + secondSubProblem;
									if(tardiness < T.get(key)) {
										T.put(key, tardiness);
										O.put(key, k+delta);
									}
								}
							}
						}
					}
		}
		
		ArrayList<Integer> schedule = getSchedule(O,"0 - "+(jobs.length-1)+" - -1 - 0");

		return T.get("0 - "+(jobs.length-1)+" - -1 - 0");
	}
	
	public ArrayList<Integer> getSchedule(HashMap<String,Integer> O, String key) {
		ArrayList<Integer> schedule = new ArrayList<Integer>();
		String[] splitKey = key.split(" - ");
		int i = Integer.parseInt(splitKey[0]);
		int j = Integer.parseInt(splitKey[1]);
		int oldk = Integer.parseInt(splitKey[2]);
		int t = Integer.parseInt(splitKey[3]);
		ArrayList<Integer> subset = retrieveSubset(i,j,oldk);
		if(subset.size() < 1) {
			return new ArrayList<Integer>();
		}
		else if(subset.size() == 1) {
			schedule.add(subset.get(0));
			return schedule;
		}
		int optimalPosition = O.get(key);
		int k = retrieveKPrime(subset);
		int C = retrieveC(subset,k,t);
		schedule.addAll(getSchedule(O,i + " - " + (optimalPosition-1) + " - " + k + " - " + t));
		schedule.add(k);
		schedule.addAll(getSchedule(O,(optimalPosition+1) + " - " + j + " - " + k + " - " + C));
		return schedule;
	}
}