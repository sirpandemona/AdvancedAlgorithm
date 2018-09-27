import java.util.ArrayList;
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
		return L;
	}
	
	public int SequenceJobs() {		
		int result = 0;
		
		return result;
	}
}
