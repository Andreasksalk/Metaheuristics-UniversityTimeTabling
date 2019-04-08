
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
			assign(Co[i], Co.length, i, Room_id, Room_cap);
		}
	}

	private void assign(Course course, int num_c, int i, String [] Room_id, int [] Room_cap) {
			int a = 0;
			for (int k=0; k<course.getBin_con().length; k++) {
				for (int l=0; l<Room_id.length; l++) {
					// Checking if the Room has capacity for the students
					if (Room_cap[l]<course.getNr_students()) {
						continue;
					}
					// Checking constraint matrix for availability
					if (course.getBin_con()[k] == 0) {
						continue;
					}
					// Checking if another course is using the room
					for (int m=0; m<num_c; m++) {
						if (x[m][k][l]==1) {
							continue;
						}
					}
					
					// Constraints are met, therefore setting lecture
					if(a<course.getNr_Lec()) {
						x[i][k][l]=1;
						a++;
					} else {break;}
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
	
	
}
