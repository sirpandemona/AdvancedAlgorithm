import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LawlerDP {
	private int numJobs;
	private int[][] jobs;
	private HashMap<int[],ArrayList<int[]>> states;
	
	public LawlerDP(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
		states = new HashMap<int[],ArrayList<int[]>>();
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
		ArrayList<int[]> L = new ArrayList<int[]>();
		ArrayList<Integer> S = retrieveSubset(i,j,k);
		if(S.size() == 0) {
			return L;
		}
		else if(S.size() == 1){
			L.add(state);
			return L;
		}
		if(states.containsKey(state)){
			return states.get(state);
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
		states.put(state,L);
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
		int result = 0;
		
		return result;
	}
}
