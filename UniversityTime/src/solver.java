import java.io.*;

public class solver {
	Course[] Co;
	int [][] bin;
	Curricula[] Cu;
	String [] Room_id;
	int [] Room_cap;
	int p;
	int d;
	int [][][] x;
	int [][][] x2;
	StopWatch watch;
	double search_time;
	double removal_pro;
	boolean room_cap_con;
	double obj_best;
	int iter;
	
	public solver(String[] filename,StopWatch watch, double search_time, double removal_pro, boolean room_cap_con) {
		this.watch = watch;
		this.removal_pro = removal_pro;
		this.room_cap_con = room_cap_con;
		this.search_time = search_time;
		try {
			dataReader instance = new dataReader(filename);
			this.Co = instance.getCourses();
			this.Cu = instance.getCurricula();
			this.Room_id = instance.getRoomId();
			this.Room_cap = instance.getRoomCap();
			this.p = instance.getPeriods();
			this.d = instance.getDays();
			//this.x = new int[Co.length][Co[0].getBin_con().length][Room_id.length];
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public void solve() {
		FCFS sol1 = new FCFS(Co,Cu,Room_id,Room_cap,p,d);
		x = sol1.returnX();
		Search sol2 = new Search(x, search_time, room_cap_con, watch, Co,Cu,Room_id,Room_cap,p,d);
		watch = sol2.returnWatch();
		x = sol2.returnX();
		ALNS sol3 = new ALNS(x,removal_pro, room_cap_con,watch,Co,Cu,Room_id,Room_cap,p,d);
		x = sol3.returnX();
		obj_best = sol3.returnObj();
		iter = sol3.returnIter();
		writeSol("test01.sol", x);
		//x2 = sol2.returnX();
		//System.out.print(x.length);
		int day = 0;
		int period = 0;
		/*for (int i = 0; i < x.length; i++) {
			day = 0;
			period = 0;
			for (int j = 0; j <x[i].length; j++) {
				if (j % p == 0 && j != 0) {
					//System.out.println(j);
					day++;
					period = 0;
				}
				for(int k = 0; k < x[i][j].length; k++) {
					if (x[i][j][k] == 1 ) {
						System.out.println(Co[i].getCourse_nr() + " " +  day + " " + period + " " + Room_id[k]);
					}
				}
				period ++;
			}
		}*/
		
		
		/*for(int k = 0; k < x[0][0].length; k++) {
			System.out.print(Room_id[k] + " ");
			for (int i = 0; i < x.length; i++) {
				for (int j = 0; j <x[i].length; j++) {
					if (x[i][j][k] == 1) {
						System.out.print(j + " ");
					}
				}
			}
			System.out.println();
		}*/
	}
	
	
	public void writeSol (String filename, int [][][] x) {
		BufferedWriter writer = null;
		
		try {
			File solFile = new File(filename);
			solFile.createNewFile();
		
			writer = new BufferedWriter(new FileWriter(filename,true));
			
			for (int i = 0; i < x.length; i++) {
				int day = 0;
				int period = 0;
				for (int j = 0; j <x[i].length; j++) {
					if (j % p == 0 && j != 0) {
						//System.out.println(j);
						day++;
						period = 0;
					}
					for(int k = 0; k < x[i][j].length; k++) {
						if (x[i][j][k] == 1 ) {
							writer.write(Co[i].getCourse_nr() + " " +  day + " " + period + " " + Room_id[k]);
							writer.newLine();
						}
					}
					period ++;
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
		writer.close();
		} catch(Exception e) {
			
		}
	}
	public double returnObj () {
		return obj_best;
	}
	public int returnIter () {
		return iter;
	}
}
