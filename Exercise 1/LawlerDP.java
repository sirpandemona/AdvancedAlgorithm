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
	
	public ArrayList<int[]> generateStatesIt(){
		int[] startState = {0,jobs.length-1,-1,0};
		ArrayList<int[]> queue = new ArrayList<int[]>();
		queue.add(startState);
		int idx = 0;
		while(idx != queue.size()) {
			int[] S = queue.get(idx);
			int i = S[0];
			int j = S[1];
			int k = S[2];
			int t = S[3];
			queue.addAll(generateStates(i,j,k,t));
			idx++;
		}
		return queue;
	}
	
	public boolean noTardyJob(ArrayList<Integer> subset, int t)
	{
		int C = t;
		for(int job : subset) {
			C += jobs[job][0];
			if(jobs[job][1] > C)
			{
				return false;
			}
		}
		return true;
	}
	
	public int allTardyJobs(ArrayList<Integer> subset, int t)
	{
		int C = t;
		int T = 0;
		for(int job : subset) {
			C += jobs[job][0];
			if(jobs[job][1] < C)
			{
				return 0;
			}
			T += jobs[job][1] - C;
		}
		return T;
	}
	
	public int shortcut(int i, int j, int k, int t)
	{
		ArrayList<Integer> subset = retrieveSubset(i,j,k);
		if(subset.size() > 0 && t + jobs[subset.get(0)][0] > jobs[subset.get(0)][1]) {
			int T = allTardyJobs(subset,t);
			if(T > 0) {
				return T;
			}
		} else {
			if(noTardyJob(subset,t)) {
				return 0;
			}
		}
		return -1;
	}
	
	public ArrayList<int[]> generateStates(int i, int j, int k, int t) {
		int [] state = {i,j,k,t};
		String key  = i + " - " + j + " - "+ k + " - "+t;
		//System.out.println(key);
		ArrayList<int[]> L = new ArrayList<int[]>();
		ArrayList<Integer> S = retrieveSubset(i,j,k);
		if(S.size() == 0) {
			//L.add(state);
			return L;
		}
		else if(S.size() == 1){
			//L.add(state);
			return L;
		}
		if(states.containsKey(key)){
			//System.out.println("Got state: "+ key);
			return states.get(key);
		}
		
		int kPrime = retrieveKPrime(S);
		int delta = (numJobs-kPrime);
		//if(kPrime+delta+1 > j) { delta = j-(kPrime +1);} //prevent the
		for(int d = 0; d<= delta;d++) {
			//System.out.println("i:"+i+"- delta:"+delta+"- d:"+d+"- j:"+j);
			ArrayList<Integer> left = retrieveSubset(i,kPrime+d,k);
			
			int shortcutleft = -1;//shortcut(i,kPrime+d,kPrime,t);
			
			if(shortcutleft != -1) {
				int[] sLeft = {i,kPrime+d,kPrime,t,shortcutleft};
				L.add(sLeft);
			} else {
				int[] sLeft = {i,kPrime+d,kPrime,t};
				L.add(sLeft);
			}
			
			//ArrayList<Integer> right = retrieveSubset(kPrime+d+1, j, kPrime);
			int cRight = retrieveStartingTime(left,t);			
			
			int shortcutright = -1;//shortcut(kPrime+d+1, j, kPrime,cRight);
			
			if(shortcutright != -1) {
				int[] sRight = {kPrime+d+1, j, kPrime,cRight,shortcutright};
				L.add(sRight);
			} else {
				int[] sRight = {kPrime+d+1, j, kPrime,cRight};
				L.add(sRight);
			}
			
			//L.addAll(generateStates(i,kPrime+d,kPrime,t));
			//L.addAll(generateStates(kPrime+d+1, j, kPrime,cRight));
			
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
		int[] startState = {0,jobs.length-1,-1,0};
		//ArrayList<int[]> L = generateStates(0, jobs.length-1, -1, 0);
		ArrayList<int[]>L = generateStatesIt();
		Collections.reverse(L);
		//L.add(startState);
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
			//System.out.println(key);
			if(subset.size() < 1) {
				T.put(key,0);
			}
			else if(subset.size() == 1) {
				int index = subset.get(0);
				T.put(key, Math.max(0,t+jobs[index][0]-jobs[index][1]));
			}
			else if(S.length > 4) {
				T.put(key, S[4]);
			}
			else {
				T.put(key, Integer.MAX_VALUE);
				for(int delta = 0; delta <= j-k; delta++) {
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

		return T.get("0 - "+(jobs.length-1)+" - -1 - 0");
	}
}
