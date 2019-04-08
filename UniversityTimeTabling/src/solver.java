import java.io.FileNotFoundException;

public class solver {
	Course[] Co;
	int [][] bin;
	Curricula[] Cu;
	String [] Room_id;
	int [] Room_cap;
	int p;
	int d;
	
	public solver(String[] filename) {
		try {
			dataReader instance = new dataReader(filename);
			this.Co = instance.getCourses();
			this.Cu = instance.getCurricula();
			this.Room_id = instance.getRoomId();
			this.Room_cap = instance.getRoomCap();
			this.p = instance.getPeriods();
			this.d = instance.getDays();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public void solve() {
		FCFS sol1 = new FCFS(Co,Cu,Room_id,Room_cap,p,d);
	}
	
	
}
