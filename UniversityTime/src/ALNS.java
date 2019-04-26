
class ALNS {
	public ALNS (int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		int iter = 0;
		int [][][] x_t = generateX(x); // generateX not implemented
		int [][][] x_b = generateX(x);
		
		
		
		while (iter < 100) {
			//rho = roulette_wheel(rho_plus, rho_minus);
			//d = select("Set of destroy",rho); // Roulette wheel function and destroy functions not implemented
			//r = select("Set of repair", rho); // Select not implemented
			//x_t(r(d(x_t)));
			//Search sol = new Search(x_t,Co,Cu,Room_id,Room_cap,p,d);
			if (ObjValue(x_t,Co,Cu,Room_id,Room_cap,p,d) <= ObjValue(x_b,Co,Cu,Room_id,Room_cap,p,d)) {
				x_b = x_t;
				x_t = generateX(x_b);
			}
			//update_w(rho_plus, rho_minus);
			iter++;
		}
	}
	public int[][][] generateX (int [][][] x){
		int [][][] x_gen = new int [x.length][x[0].length][x[0][0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j <x[i].length; j++) {
				for(int k = 0; k < x[i][j].length; k++) {
					x_gen[i][j][k] = x[i][j][k];
				}
			}
		}
		
		return x_gen;
	}
	
	public double ObjValue(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap,int p, int d) {
		double Obj = 0;
		int [] U = new int[Co.length]; //unassigned courses
		int [][] V = new int [x[0].length][x[0][0].length]; //exceeds room cap
		int [] P = new int [Co.length]; // number of time a course changes room
		int [] W = new int [Co.length]; // number of days courses are below min_work_days 
		int [][] A = new int [Cu.length][x[0].length]; // curriculum in a time slot has secluded lecture
		
		for(int i = 0; i < U.length;i++) {
			U[i] = Co[i].getNr_Lec();
			W[i] = Co[i].getMin_days();
			
			
			int tempU = 0;
			int tempP = 0;
			int tempW = 0;
			int tempDay = 0;
			String tempRoom = "";
			for(int j = 0; j < x[0].length; j++) {
				for(int k = 0; k < x[0][0].length;k++) {
					if(x[i][j][k]==1) {
						tempU+= 1;
					}
					if (tempRoom == "" && x[i][j][k] == 1) {
						tempRoom = Room_id[k];
					}
					if(x[i][j][k] == 1 && Room_id[k] != tempRoom) {
						tempRoom = Room_id[k];
						tempP += 1;
					}
					if(j % p == 0) {
						tempDay = 0;
					}
					if (x[i][j][k] == 1 && tempDay == 0) {
						tempDay = 1;
						tempW += 1;
					}
				}
			}
			U[i] = U[i]-tempU;
			if (U[i] < 0) {
				U[i] = 0;
			}
			P[i] = tempP;
			W[i] = W[i] - tempW;
			if (W[i] < 0) {
				W[i] = 0;
			}
			
			Obj+= 10*U[i];
			Obj+= 1*P[i];
			Obj+= 5*W[i];
			
		}
		for(int j = 0; j< x[0].length; j++) {
			for(int k = 0; k < x[0][0].length; k++) {
				int tempV = 0;
				for(int i = 0; i < x.length; i++) {
					if(x[i][j][k] == 1 && Room_cap[k] < Co[i].getNr_students()) {
						tempV += Co[i].getNr_students() - Room_cap[k];
							if (tempV < 0) {
								tempV = 0;
							}
					}
				}
				V[j][k] = tempV;
				Obj+= 1*V[j][k];
			}
		}
		
		for(int q = 0; q <Cu.length; q++) {
			int dayCounter = 0;
			for(int j = 0; j < x[0].length; j++) {
				int tempA = 0;
				int tempt = 0;
				int tempt2 = 1;
				if(j% p == 0) {
					dayCounter += 1;
				}
				for(String Course: Cu[q].getCourse_nr()) {
					int index = -1;
					for(int i = 0; i < x.length; i++) {
						//System.out.println(Course + " " + Co[i].getCourse_nr());
						//System.out.println(C);
						if(Course.equals(Co[i].getCourse_nr())) {
							index = i;
						}
					}
					for(int k = 0; k < x[0][0].length; k++) {
						//System.out.println(j + " " + (dayCounter)*p);
						if(x[index][j][k] ==1) {
							tempt = 1;
						}
						if(j - 1 < dayCounter*p && j+1 != d*p) {
							if(x[index][j+1][k] == 0) {
								tempt2 = 0;
							}
						} else if (j+1 >= (dayCounter)*p) {
							if(x[index][j-1][k] == 0) {
								tempt2 = 0;
							}
						} else if (x[index][j-1][k] == 0 && x[index][j+1][k] == 0) {
							tempt2 = 0;
						} else {
							tempt2 = 1;
						}
					}
				}
				if (tempt == 1 && tempt2 == 0) {
					tempA = 1;
				}
				A[q][j] = tempA;
				Obj+= 2*A[q][j];
			}
		}
		
		/*for(int q = 0; q <Cu.length; q++) {
			for(int j = 0; j < x[0].length; j++) {
				System.out.print(A[q][j] + " ");
			}
			System.out.println();
		}*/
		
		
		return Obj;
	}
	
}
