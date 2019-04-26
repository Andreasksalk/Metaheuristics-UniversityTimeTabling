import java.util.ArrayList;

class FCFS {
	
	int [][][] x;
	int p;
	int d;
	Course [] Co;
	
	
	
	public FCFS(Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		x = new int [Co.length][Co[0].getBin_con().length][Room_id.length];
		this.Co = Co;
		this.p=p;
		this.d=d;
		for (int i=0; i<Co.length; i++) {
			assign(Co[i], Co.length, i, Room_id, Room_cap, Co,Cu);
		}
		//System.out.println(x.length + " " + x[0].length + " " + x[0][0].length);
		//System.out.print(1 + " + " +  2 + " = " + 3);
	}

	private void assign(Course course, int num_c, int i, String [] Room_id, int [] Room_cap, Course[] Co, Curricula [] Cu) {
			int a = 0;
			periodLoop:
			for (int k=0; k<course.getBin_con().length; k++) {
				roomLoop:
				for (int l=0; l<Room_id.length; l++) {
					// Checking if the Room has capacity for the students
					if (Room_cap[l]<course.getNr_students()) {
						continue roomLoop;
					}
					
					
					// Checking constraint matrix for availability
					if (course.getBin_con()[k] == 0) {
						continue periodLoop;
					}
					// Checking if another course is using the room
					for (int m=0; m<num_c; m++) {
						if (x[m][k][l]==1) {
							continue roomLoop;
						}
					}
					
					//Checking if a lecturer is teaching a course at the same time slot
					for(int n = 0; n < x.length; n++) {
						for(int r = 0; r < x[0][0].length; r++) {
							if(x[n][k][r] == 1 && Co[n].getLecture_nr().equals(course.getLecture_nr()) && !Co[n].getCourse_nr().equals(course.getCourse_nr())) {
								continue periodLoop;
							}
						}
					}
					
					//Checking if a course in the same curriculum is going on at the same time slot
					for(ArrayList<Integer> CoS: course.getCoCu()) {
						for(int c: CoS) {
							for(int r = 0; r < x[0][0].length; r++) {
								if(x[c][k][r] == 1) {
									continue periodLoop;
								}
							}
						}
					}
					
					
					// Checking if course in that time period is used in other rooms
					for(int room = 0; room < Room_id.length; room ++) {
						if (x[i][k][room] == 1) {
							continue periodLoop;
						}
					}
					
					// Constraints are met, therefore setting lecture
					if (a < course.getNr_Lec()) {
						x[i][k][l]=1;
						a++;
					} else {
						break;
					}
			}
		}
		//for (int y=0; y<course.getBin_con().length; y++) {
		//	//System.out.println("Course nr" + i);
		//	for(int l=0; l<Room_id.length; l++) {
		//		System.out.print(x[i][y][l]+ " ");
		//	}
		//System.out.println(course.getNr_Lec());
		//}
	}
	
	public int[][][] returnX(){
		return x;
	}
	
}
