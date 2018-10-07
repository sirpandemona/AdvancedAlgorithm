import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class LawlerDP {
	private int numJobs;
	private int[][] jobs;
	private HashMap<String,ArrayList<Integer>> jobMapping;
	private HashMap<String,ArrayList<int[]>> states;
	
	private ArrayList<int[]> L;
	private HashMap<String, Integer> jobOrderMapping;
	private ArrayList<Integer> ordering;
	
	public LawlerDP(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
		states = new HashMap<String,ArrayList<int[]>>();
		jobMapping = new HashMap<String,ArrayList<Integer>>();
		jobOrderMapping = new HashMap<String,Integer>();
		ordering = new ArrayList<Integer>();
		L =new ArrayList<int[]>();
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
		ArrayList<Integer> order = new ArrayList<Integer>();
		HashMap<String,Integer> jobOrderMapping = new HashMap<String,Integer>();
		
		queue.add(startState);
		int idx = 0;
		while(idx != queue.size()) {
			int[] S = queue.get(idx);
			int i = S[0];
			int j = S[1];
			int k = S[2];
			int t = S[3];
			String key  = i + " - " + j + " - "+ k + " - "+t;
			if(jobOrderMapping.containsKey(key)){
				int dept = states.get(key).size();
				int orderidx = jobOrderMapping.get(key);
				for (int a = orderidx; a<dept;a++) {
					order.set(orderidx+a, idx+a);
				}
			}
			else 
			{
				queue.addAll(generateStates(i,j,k,t));
				jobOrderMapping.put(key,idx);
				System.out.println("idx: "+idx +" sizeQueue"+queue.size());
			}
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
	
	public ArrayList<Integer> GenCandidateDeltas(ArrayList<Integer> S,  int t) {
		int k = retrieveKPrime(S);
		ArrayList<Integer> sPrime2;
		ArrayList<Integer> deltas = new ArrayList<Integer>();
		int dk = jobs[k][1];
		do {
			ArrayList<Integer> sPrime = retrieveSPrime(S,dk);		
			int dkPrime = t;
			for(int job : sPrime) {
				dkPrime += jobs[job][0];
			}
			while(dkPrime > dk) {
				dk = dkPrime;
				sPrime = retrieveSPrime(S,dk);
				dkPrime = t;
				for(int job : sPrime) {
					dkPrime += jobs[job][0];
				}
			}
			int j = retrieveMaxJ(S,dk);
			int delta  = j-k;
			deltas.add(delta);
			sPrime2 = new ArrayList<Integer>();
			for(int job : S) {
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
	
	public int retrieveMaxJ(ArrayList<Integer> S, int dk) {
		int j = -1;
		for(int job: S) {
			if(jobs[job][1] <= dk) {
				j = job;
			}
		}
		return j;
	}
	
	public ArrayList<Integer> retrieveSPrime(ArrayList<Integer>S,int dk){
		ArrayList<Integer> sPrime = new ArrayList<Integer>();
		for(int job : S) {
			if(jobs[job][1] <= dk) {
				sPrime.add(job);
			}
		}
		return sPrime;
	}
	
	
	public ArrayList<int[]> generateStates(int i, int j, int k, int t) {
		int [] state = {i,j,k,t};
		String key  = i + " - " + j + " - "+ k + " - "+t;
		if(states.containsKey(key)){
			//System.out.println("Got state: "+ key);
			return states.get(key);
		}
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

		
		int kPrime = retrieveKPrime(S);
		//if(kPrime+delta+1 > j) { delta = j-(kPrime +1);} //prevent the
		int C = retrieveC(S,kPrime,t);
		ArrayList<Integer> deltas =  GenCandidateDeltas(S,t);
		int prevDelta = 0;
		for(int di  = 0;di<deltas.size();di++) {
			int d = deltas.get(di);
			for(int pd = prevDelta+1 ;pd<d;pd++) {
				C += jobs[kPrime+pd][0];
			}
			prevDelta =d;
		//for(int d = 0; d <= j-kPrime;d++) {
			//System.out.println("i:"+i+"- delta:"+delta+"- d:"+d+"- j:"+j);
			
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
		ArrayList<Integer> res = new ArrayList<Integer>(); 
		if(i> j) {return res;}
		if(j>= numJobs) {j = numJobs-1;}
		int valueK = Integer.MAX_VALUE;
		if(k>-1) {
			valueK = jobs[k][0];
		}
		for(int a =i; a<=j;a++) {
			if(jobs[a][0] < valueK || (jobs[a][0] == valueK && a < k)) {
				res.add(a);
			}
		}
		jobMapping.put(key,res);
		return res;
	}
	
	public int retrieveKPrime(ArrayList<Integer> S) {
		int kPrime =0;
		int val = 0;
		for(int a = 0; a < S.size();a++) {
			int j = S.get(a);
			if(jobs[j][0] >= val) {
				kPrime = j;
				val  = jobs[j][0] ;
			}
		}
		return kPrime;
	}
	
	public int retrieveC(ArrayList<Integer> S, int kPrime, int t) {
		int time = t;
		for(int j : S) {
			if(j >= kPrime)
			{
				break;
			}
			int p_j = jobs[j][0];
			time+= p_j;
		}
		return time;
	}
	
	public int SequenceJobs() {
		sortJobsByDueTime();
		//System.out.println("Generate States:");
		int[] startState = {0,jobs.length-1,-1,0};
		ArrayList<int[]> L = generateStates(0, jobs.length-1, -1, 0);
		//ArrayList<int[]>L = generateStatesIt();
		Collections.reverse(L);
		L.add(startState);
		HashMap<String,Integer> T = new HashMap<String,Integer>();
		
	//	System.out.println("Dynamic:");
		
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
				int C = retrieveC(subset,k,t);
				ArrayList<Integer> deltas =  GenCandidateDeltas(subset,t);
				int prevDelta = 0;
				for(int di  = 0;di<deltas.size();di++) {
					int delta = deltas.get(di);
					for(int pd = prevDelta+1 ;pd<delta;pd++) {
						C += jobs[k+pd][0];
					}
					prevDelta =delta;
				//for(int delta = 0; delta <= j-k; delta++) {
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
