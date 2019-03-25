import java.io.*;
import java.util.Scanner;

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
		for (int n=0;n<filename.length;n++) {
			File data = new File(filename[n]);
			Scanner in = new Scanner(data);
			in.nextLine();
			
			if (filename[n] == "basic.utt") {
				c = in.nextInt(); // Number of courses
				r = in.nextInt(); // Number of rooms
				d = in.nextInt(); // The set of days
				t = in.nextInt(); // Periods per day
				cur = in.nextInt(); // Number of curriculum
				con = in.nextInt(); // Number of constraints
				l = in.nextInt(); // Number of lecturers
			} else if (filename[n] == "courses.utt") {
				Co = new Course [c];
				for (int i=0; i<c; i++) {
					Co[i] = new Course();
					Co[i].setCourse_nr(in.next());
					Co[i].setLecture_nr(in.next());
					Co[i].setNr_Lec(in.nextInt());
					Co[i].setMin_days(in.nextInt());
					Co[i].setNr_students(in.nextInt());
				}
			} else if (filename[n] == "curricula.utt") {
				Cur = new Curricula [cur];
				for (int i=0; i<cur; i++) {
					Cur[i] = new Curricula();
					Cur[i].setCur_nr(in.next());
					Cur[i].setNum_courses(in.nextInt());
				}
			} else if (filename[n] == "lecturers.utt") {
				Lecturers = new String[l];
				for (int i = 0; i<l; i++) {
					Lecturers[i] = in.next();
				}
			} else if (filename[n] == "relation.utt") {
				for (int i =0; i< cur;i++) {
					for(int j =0; j< Cur[i].getNum_courses();j++) {
						in.next();
						Cur[i].addCourse_nr(in.next());
					}
				}
			} else if (filename[n] == "rooms.utt") {
				Room_id = new String[r];
				Room_cap = new int[r];
				for (int i = 0; i<r; i++) {
					Room_id[i] = in.next();
					Room_cap[i] = in.nextInt();
				}
			} else if (filename[n] == "unavailability.utt") {
				int i = 0;
				Co[i].createBin_con(d, t);
				for (int k = 0; k<con;k++) {
					for(int j = 0; j<3; j++) {
						if(Co[i].getCourse_nr() != in.next()) {
							i++;
							Co[i].createBin_con(d, t);
						}
						Co[i].setBin_con(in.nextInt(), in.nextInt());
					}
				}
			} else {
				continue;
			} in.close();
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
}
