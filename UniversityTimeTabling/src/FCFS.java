
class FCFS {
	
	int [][][] x;
	int p;
	int d;
	
	
	public FCFS(Course[] Co, Curricula[] Cu, int [] Room_id, int [] Room_cap, int p, int d) {
		x = new int [Co.length][Co[0].getBin_con().length][Room_id.length];
		this.p=p;
		this.d=d;
		for (int i=0; i<Co.length; i++) {
			assign(Co[i], Co.length, i, Room_id, Room_cap);
		}
	}

	private void assign(Course course, int num_c, int i, int [] Room_id, int [] Room_cap) {
			int c = -1; // Dummy way to keep index k without reference problems 
			for (int k=0; k<course.getBin_con().length; k++) {
				c+=1;
				for (int l=0; l<Room_id.length; l++) {
					if (Room_cap[l]<course.getNr_students()) {
						continue;
					}
					if (course.getBin_con()[k] == 0) {
						continue;
					}
					for (int m=0; m<num_c; m++) {
						if (x[m][k][l]==1) {
							continue;
						}
					int a = 0;
					x[i][c][l]=1;
					while(a<course.getNr_Lec()-1) {
						for (int n=c; n<course.getBin_con().length; n+=p) {
							if (x[i][n][l]==0) {
								x[i][n][l]=1;
								a+=1;
								
							}
						}
						c-=1;
					}
				}
			}
		}
	}
}
