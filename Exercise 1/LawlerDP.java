import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class LawlerDP {
	private int numJobs;
	private int[][] jobs;
	private HashMap<String,ArrayList<int[]>> states;
	
	public LawlerDP(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
		states = new HashMap<String,ArrayList<int[]>>();
	}
	
	public void sortJobsByDueTime() {
		Arrays.sort(jobs, new Comparator<int[]>() {
			
			public int compare(int[] j1, int[] j2) {
				Integer dd1 = j1[1];
				Integer dd2 = j2[1];
				// reverse sort on quantity
				return dd1.compareTo(dd2);
			}
		});
	}
	
	public Schedule getSchedule() {
		Schedule bestSchedule = null;
			
		//magic happens here
		sortJobsByDueTime();
		ArrayList<int[]> states = generateStates(0,numJobs-1,-1,0);
		//System.out.print(states);
		return bestSchedule;
	}
	
	public ArrayList<int[]> generateStates(int i, int j, int k, int t) {
		int [] state = {i,j,k,t};
		String key  = i + " - " + j + " - "+ k + " - "+t;
		System.out.println(key);
		ArrayList<int[]> L = new ArrayList<int[]>();
		ArrayList<Integer> S = retrieveSubset(i,j,k);
		if(S.size() == 0) {
			L.add(state);
			return L;
		}
		else if(S.size() == 1){
			L.add(state);
			return L;
		}
		if(states.containsKey(key)){
			return states.get(key);
		}
		
		int delta = (numJobs-k)-1;
		int kPrime = retrieveKPrime(S);
		if(kPrime+delta+1 > j) { delta = j-(kPrime +1);} //prevent the
		for(int d = 0; d<= delta;d++) {
			//System.out.println("i:"+i+"- delta:"+delta+"- d:"+d+"- j:"+j);
			ArrayList<Integer> left = retrieveSubset(i,kPrime+d,k);
			int[] sLeft = {i,kPrime+d,kPrime,t};
			
			//ArrayList<Integer> right = retrieveSubset(kPrime+d+1, j, kPrime);
			int cRight = retrieveStartingTime(left,t);			
			int[] sRight = {kPrime+d+1, j, kPrime,cRight};
			
			L.add(sLeft);
			L.add(sRight);
			L.addAll(generateStates(i,kPrime+d,kPrime,t));
			L.addAll(generateStates(kPrime+d+1, j, kPrime,cRight));
		}
		states.put(key,L);
		return L;
	}
	
	public ArrayList<Integer> retrieveSubset(int i, int j, int k) {
		ArrayList<Integer> res = new ArrayList<Integer>(); 
		if(i> j) {return res;}
		if(j>= numJobs) {j = numJobs-1;}
		int valueK = Integer.MAX_VALUE;
		if(k>-1) {
			valueK = jobs[k][0];
		}
		for(int a =i; a<=j;a++) {
			if(jobs[a][0] <= valueK && a!=k) {
				res.add(a);
			}
		}
		return res;
	}
	
	public int retrieveKPrime(ArrayList<Integer> S) {
		int kPrime =0;
		int val = 0;
		for(int a = 0; a < S.size();a++) {
			int j = S.get(a);
			if(jobs[j][0] > val) {
				kPrime = j;
				val  = jobs[j][0] ;
			}
		}
		return kPrime;
	}
	
	public int retrieveStartingTime(ArrayList<Integer> S, int t) {
		int time = t;
		for(int a = 0; a < S.size();a++) {
			int j = S.get(a);
			int p_j = jobs[j][0];
			time+= p_j;
		}
		return time;
	}
	
	public int SequenceJobs() {
		sortJobsByDueTime();
		System.out.println("Generate States:");
		ArrayList<int[]> L = generateStates(0, jobs.length, -1, 0);
		Collections.reverse(L);
		
		HashMap<String,Integer> T = new HashMap<String,Integer>();
		
		System.out.println("Dynamic:");
		
		for(int[] S : L) {							
			int i = S[0];
			int j = S[1];
			int oldk = S[2];
			int t = S[3];
			ArrayList<Integer> subset = retrieveSubset(i,j,oldk);
			int k = retrieveKPrime(subset);
			String key = i + " - " + j + " - " + oldk + " - " + t;
			System.out.println(key);
			if(subset.size() < 1) {
				T.put(key,0);
			}
			else if(subset.size() == 1) {
				int index = subset.get(0);
				T.put(key, Math.max(0,t+jobs[index][0]-jobs[index][1]));
			}
			else {
				T.put(key, Integer.MAX_VALUE);
				for(int delta = 0; delta < j-k; delta++) {
					int C = t;
					for(int index : subset) {
						if(index <= k+delta) {
							C += jobs[index][0];
						}
					}
					T.put(key, Math.min( T.get(key),	T.get(i + " - " + (k+delta) + " - " + k + " - " + t)		+
												 		Math.max(0, C - jobs[k][1])									+
												 		T.get((k+delta+1) + " - " + j + " - " + k + " - " + C)		));
				}
			}
		}

		return T.get(new int[]{0,jobs.length,-1,0});
	}
}
