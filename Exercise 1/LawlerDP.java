import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LawlerDP {
	private int numJobs;
	private int[][] jobs;
	
	public LawlerDP(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();		
	}
	
	public Schedule getSchedule() {
		Schedule bestSchedule = null;
		
		//magic happens here
		
		return bestSchedule;
	}
	
	public List<int[]> generateStates(int i, int j, int k, int t) {
		List<int[]> L = new ArrayList<int[]>();
		ArrayList<Integer> S = retrieveSubset(i,j,k);
		if(S.size() == 0) {
			return L;
		}
		else if(S.size() == 1){
			int [] job = {i,j,k,t};
			L.add(job);
			return L;
		}
		
		int delta = numJobs-k;
		for(int d = 0; d<= delta;d++) {
			int kPrime = retrieveKPrime(S);
			int[] sLeft = {i,kPrime+d,kPrime,t};
			
			ArrayList<Integer> right = retrieveSubset(kPrime+d+1, j, kPrime);
			int cRight = retrieveStartingTime(right, delta,t);			
			int[] sRight = {kPrime+d+1, j, kPrime,cRight};
			
			L.add(sLeft);
			L.add(sRight);
			L.addAll(generateStates(i,kPrime+d,kPrime,t));
			L.addAll(generateStates(kPrime+d+1, j, kPrime,cRight));
		}
		return L;
	}
	
	public ArrayList<Integer> retrieveSubset(int i, int j, int k) {
		ArrayList<Integer> res = new ArrayList<Integer>(); 
		int valueK = jobs[k][0];
		for(int a =i; a<j;a++) {
			if(jobs[a][0] <= valueK) {
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
	
	public int retrieveStartingTime(ArrayList<Integer> S, int delta, int t) {
		int time = t;
		for(int a = 0; a < S.size();a++) {
			int j = S.get(a);
			int p_j = jobs[j][0];
			t+= p_j;
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
