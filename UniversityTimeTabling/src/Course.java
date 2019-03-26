import java.util.*;

class Course {
	private String Course_nr;
	private String Lecture_nr;
	private int nr_Lec;
	private int min_days;
	private int nr_students;
	private int[][] bin_con;
	private int index;
	
	public Course() {
		Course_nr = "";
		Lecture_nr = "";
		nr_Lec = -1;
		min_days = -1;
		nr_students = -1;
	}
	
	public String getCourse_nr() {
		return Course_nr;
	}
	public void setCourse_nr(String course_nr) {
		this.Course_nr = course_nr;
	}
	public String getLecture_nr() {
		return Lecture_nr;
	}
	public void setLecture_nr(String lecture_nr) {
		this.Lecture_nr = lecture_nr;
	}
	public int getNr_Lec() {
		return nr_Lec;
	}
	public void setNr_Lec(int nr_Lec) {
		this.nr_Lec = nr_Lec;
	}
	public int getMin_days() {
		return min_days;
	}
	public void setMin_days(int min_days) {
		this.min_days = min_days;
	}
	public int getNr_students() {
		return nr_students;
	}
	public void setNr_students(int nr_students) {
		this.nr_students = nr_students;
	}
	
	public void createBin_con(int d, int p) {
		bin_con = new int [d][p];
		for (int i = 0; i<d;i++) {
			for ( int j = 0; j<p; j++) {
				bin_con[i][j] = 1;
			}
		}
	}
	
	public void setBin_con(int d, int p) {
		bin_con[d][p] = 0;
	}
	public int [][] getBin_con() {
		return bin_con;
	}
	public void setIndex(int i) {
		index = i;
	}
	public int getIndex() {
		return index;
	}
}
