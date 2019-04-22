import java.util.ArrayList;

class Curricula {
	private String cur_nr;
	private ArrayList <String> Course_nr;
	private int num_courses;
	
	public int getNum_courses() {
		return num_courses;
	}
	public void setNum_courses(int num_courses) {
		this.num_courses = num_courses;
		Course_nr = new ArrayList <String> ();
	}
	public String getCur_nr() {
		return cur_nr;
	}
	public void setCur_nr(String cur_nr) {
		this.cur_nr = cur_nr;
	}
	public ArrayList <String> getCourse_nr() {
		return Course_nr;
	}
	public void addCourse_nr(String course_nr) {
		Course_nr.add(course_nr);
	}
	
}
