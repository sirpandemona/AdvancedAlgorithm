import java.util.ArrayList;
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
	
	public Schedule getSchedule() {
		Schedule bestSchedule = null;
			
		//magic happens here
		ArrayList<int[]> states = generateStates(0,numJobs-1,retrieveK(),0);
		System.out.print(states);
		return bestSchedule;
	}
	
	public ArrayList<int[]> generateStates(int i, int j, int k, int t) {
		int [] state = {i,j,k,t};
		String key  = i + " - " + j + " - "+ k + " - "+t;
		ArrayList<int[]> L = new ArrayList<int[]>();
		ArrayList<Integer> S = retrieveSubset(i,j,k);
		if(S.size() == 0) {
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
			ArrayList<Integer> left = retrieveSubset(i,kPrime+d,kPrime);
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
		if(j> numJobs) {j = numJobs;}
		int valueK = jobs[k][0];
		for(int a =i; a<j;a++) {
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
	
	public int retrieveK() {
		int k =0;
		int val = 0;
		for(int j = 0; j < numJobs;j++) {
			if(jobs[j][0] > val) {
				k = j;
				val  = jobs[j][0] ;
			}
		}
		return k;
		
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
		
		List<int[]> L = generateStates(0, jobs.length, 0, 0);
		
		HashMap<int[],Integer> T = new HashMap<int[],Integer>();
		
		for(int i = 0; i < L.size(); i++) {
			int[] S = L.get(i);
			if(S[1]-S[0] == 0) {
				T.put(S,0);
			}
			if(S[1]-S[0] == 1) {
				T.put(S, Math.max(0,S[3]+jobs[i][0]-jobs[i][1]));
			}
			T.put(S, Integer.MAX_VALUE);
			for(int delta = 0; delta < S[1]-S[2]; delta++) {
				int C = S[3];
				for(int j = 0; j < S[2]+delta; j++) {
					C += jobs[j][0];
				}
				T.put(S, Math.min( T.get(S), T.get(new int[]{S[0],S[2]+delta,S[5],S[3]}) +
											 Math.max(0, C - jobs[S[2]][1]) +
											 T.get(new int[]{S[5]+delta+1,S[1],S[5],C})));
			}
		}
		int Tmin = Integer.MAX_VALUE;
		for(int i = 0; i < jobs.length; i++) {
			Tmin = Math.min(Tmin, T.get(new int[]{0,jobs.length,i,0}));
		}
		return Tmin;
	}
}
