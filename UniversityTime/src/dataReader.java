import java.io.*;
import java.util.*;

class dataReader {
	private Course [] Co;
	private Curricula [] Cur;
	private int c = 0; // Number of courses
	private int r = 0; // Number of rooms
	private int d = 0; // The set of days
	private int t = 0; // Periods per day
	private int cur = 0; // Number of curriculum
	private int con = 0; // Number of constraints
	private int l = 0; // Number of lecturers
	private String[] Room_id;
	private int[] Room_cap;
	private String [] Lecturers;
	
	
	public dataReader(String[] filename ) throws FileNotFoundException{
		for (int n=0;n<filename.length-1;n++) {
			File data = new File(filename[filename.length-1] + filename[n]);
			Scanner in = new Scanner(data);
			in.nextLine();
			
			if (n == 0) {
				c = in.nextInt(); // Number of courses
				r = in.nextInt(); // Number of rooms
				d = in.nextInt(); // The set of days
				t = in.nextInt(); // Periods per day
				cur = in.nextInt(); // Number of curriculum
				con = in.nextInt(); // Number of constraints
				l = in.nextInt(); // Number of lecturers
			} else if (n == 1) {
				Co = new Course [c];
				for (int i=0; i<c; i++) {
					Co[i] = new Course();
					Co[i].setCourse_nr(in.next());
					Co[i].setLecture_nr(in.next());
					Co[i].setNr_Lec(in.nextInt());
					Co[i].setMin_days(in.nextInt());
					Co[i].setNr_students(in.nextInt());
					Co[i].createBin_con(d, t);
					Co[i].setIndex(i);
				}
			} else if (n == 2) {
				Cur = new Curricula [cur];
				for (int i=0; i<cur; i++) {
					Cur[i] = new Curricula();
					Cur[i].setCur_nr(in.next());
					Cur[i].setNum_courses(in.nextInt());
				}
			} else if (n == 3) {
				Lecturers = new String[l];
				for (int i = 0; i<l; i++) {
					Lecturers[i] = in.next();
				}
			} else if (n == 4) {
				for (int i =0; i< cur;i++) {
					for(int j =0; j< Cur[i].getNum_courses();j++) {
						in.next();
						Cur[i].addCourse_nr(in.next());
					}
				}
			} else if (n == 5) {
				Room_id = new String[r];
				Room_cap = new int[r];
				for (int i = 0; i<r; i++) {
					Room_id[i] = in.next();
					Room_cap[i] = in.nextInt();
				}
			} else if (n == 6) {
				String i = "";
				int j = 0;
				for (int k = 0; k<con;k++) {
					i = in.next();
					i = i.substring(1);
					j = Integer.parseInt(i);
					Co[j].setBin_con(in.nextInt()*t+in.nextInt());
				}
			} else {
				continue;
			} in.close();
		}
		
		//ArrayList <ArrayList<Integer>> CoCu = new ArrayList <ArrayList <Integer>>();
		for(int c = 0; c< Co.length; c++) {
			for(int h = 0; h < Cur.length; h++) {
				for(int o = 0; o < Cur[h].getNum_courses(); o++) {
					if(Cur[h].getCourse_nr().get(o).equals(Co[c].getCourse_nr())) {
						ArrayList <Integer> IndexCo = new ArrayList <Integer>();
						for(String cos: Cur[h].getCourse_nr()) {
							for(int n = 0; n < Co.length; n++) {
								if(cos.equals(Co[n].getCourse_nr())) {
									IndexCo.add(n);
								}
							}
						}
						Co[c].setCoCu(IndexCo);
					}
				}
			}	
		}
		
	}
	
	public Course[] getCourses() {
		return Co;
	}
	public Curricula[] getCurricula() {
		return Cur;
	}
	public String [] getRoomId () {
		return Room_id;
	}
	
	public int [] getRoomCap() {
		return Room_cap;
	}
	public String [] getLecturers() {
		return Lecturers;
	}
	public int getDays() {
		return d;
	}
	public int getPeriods() {
		return t;
	}
}
