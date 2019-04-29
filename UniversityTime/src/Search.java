import java.util.ArrayList;

class Search {
	int [][][] x_original;
	int [][][] x_best;
	int [][][] best_x;
	int [] unassigned;
	ArrayList <int [][][]> solutions = new ArrayList <int[][][]> ();
	int con1 = 0;
	int con2 = 0;
	int con3 = 0;
	int con4 = 0;
	int con5 = 0;
	int con6 = 0;
	ArrayList <int []> index_change = new ArrayList <int[]> ();
	int all_sol = 0;
	StopWatch watch;
	boolean room_cap_con;
	double search_time;
	int old_sol = -1;
	
	public Search(int [][][] x, double search_time, boolean room_cap_con, StopWatch watch, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		this.watch = watch;
		this.room_cap_con = room_cap_con;
		this.search_time = search_time;
		double elapsedTime = 0.0;
		double startTime = watch.lap();
		
		int [][][] best_x = generateX(x);
		int iter = 1;
		double old_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double min_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double iter_min = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		
		loop:
		while (elapsedTime < search_time && solutions.size() != old_sol) {
		
		
		int min_i = 0;
		
		int num_swaps = 0;
		int num_n = 0;
		
		while (iter < 2 && num_swaps < 50) {
			elapsedTime = watch.lap()-startTime;
			num_n++;
			Neighbourhood(best_x,Co,Cu,Room_id,Room_cap,p,d);
			int num_swaps_n = 0;
			//old_sol = 0;
			while (solutions.size() > 1 && solutions.size() != old_sol) {
				elapsedTime = watch.lap()-startTime;
				if (elapsedTime > search_time) {
					break loop;
				}
				old_sol = solutions.size();
				int q = 0;
				min_i = 0;
				double [] ObjVals;
				ObjVals = new double [solutions.size()];
				for (int [][][] sol: solutions) {
					ObjVals[q] = ObjValue(sol, Co, Cu, Room_id, Room_cap, p, d);
					if (ObjVals[q] < min_obj) {
						min_obj = ObjVals[q];
						min_i = q;
					}
					q++;
				}
				
				if (min_obj < old_obj) {
					num_swaps_n++;
					num_swaps++;
					
				}
				
				if (min_i != 0) {
					best_x = solutions.get(min_i);
					int [] change = index_change.get(min_i);
					//System.out.println("Sol rem: " + solutions.size());
					//System.out.println("Num swaps: " + num_swaps);
					ArrayList <int [][][]> a = new ArrayList <int[][][]> ();
					ArrayList <int []> b = new ArrayList <int[]> ();
					for (int i = 0; i < solutions.size(); i++) {
						int [][][] sol = solutions.get(i);
						int [] c = index_change.get(i);
						sol[change[0]][change[1]][change[2]] = 0;
						sol[change[3]][change[4]][change[5]] = 0;
						sol[change[3]][change[1]][change[2]] = 1;
						sol[change[0]][change[4]][change[5]] = 1;
						if (available(sol,change[3],change[1],change[2],Co,Cu,Room_id,Room_cap,p,d) == false || available(sol,change[0],change[4],change[5],Co,Cu,Room_id,Room_cap,p,d) == false) {
							a.add(sol);
							b.add(c);
						}
					}
					solutions.removeAll(a);
					index_change.removeAll(b);
				}
				old_obj = min_obj;
				
			}
			if (min_obj >= iter_min) {
				iter++;
			}
			iter_min = min_obj;
			//x_best = generateX(best_x);
		
		//elapsedTime = watch.lap()-startTime;
		}
		}
		x_best = generateX(best_x);
	}
	
	public StopWatch returnWatch () {
		return watch;
	}
	
	
	public void Neighbourhood(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		x_original = new int [x.length][x[0].length][x[0][0].length];
		unassigned = new int[x.length];
		int [] dummy = new int [6];
		for (int i = 0; i < 6; i++) {
			dummy[i] = 0;
		}
		
		x_original = generateX(x);
		solutions.clear();
		solutions.add(x);
		index_change.clear();
		index_change.add(dummy);
		
		kSwap(x_original,Co,Cu,Room_id,Room_cap,p,d);
		
		//System.out.println("Number of solutions in neighborhood: " + solutions.size());
		all_sol += solutions.size();

	}
	
	public boolean available (int [][][] x,int i, int j, int r, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		
		boolean feasible = false;
		if (room_cap_con == true) {
		//x[class][period1][room] -> x[i][j][r] 
		if (Room_cap[r]<Co[i].getNr_students()) {
			con1++;
			feasible = false;
			return feasible;
		}
		}
		
		if (Co[i].getBin_con()[j] == 0) {
			con2++;
			feasible = false;
			return feasible;
		}
		
		for(int n = 0; n < x.length; n++) {
			for(int room = 0; room < x[0][0].length; room++) {
				if(x[n][j][room] == 1 && Co[n].getLecture_nr().equals(Co[i].getLecture_nr()) && !Co[n].getCourse_nr().equals(Co[i].getCourse_nr())) {
					feasible = false;
					//con3++;
					return feasible;
				}
			}
		}
		
		// Checking if another course is using the room
				for (int m=0; m<x.length; m++) {
					if (x[m][j][r]==1 && m != i) {
						feasible = false;
						con3++;
						return feasible;
					}
				}
		
		//Checking if a course in the same curriculum is going on at the same time slot
		for(ArrayList<Integer> CoS: Co[i].getCoCu()) {
			for(int c: CoS) {
				for(int room = 0; room < x[0][0].length; room++) {
					if(x[c][j][room] == 1 && c !=i) {
						feasible = false;
						con4++;
						return feasible;
					}
				}
			}
		}
		
		// Checking if course in that time period is used in other rooms
		for(int room = 0; room < Room_id.length; room ++) {
			if (x[i][j][room] == 1 && room != r) {
				con5++;
				feasible = false;
				return feasible;
			}
		}
		int f = 0;
		for (int m = 0; m < x[0].length; m++) {
			for (int l = 0; l < x[0][0].length; l++) {
				if (x[i][m][l] == 1) {
					f++;
					if (f > Co[i].getNr_Lec()) {
						feasible = false;
						con6++;
						return feasible;
					}
				}
			}
		}
		
		feasible = true;
		return feasible;
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
	
	public void kSwap(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d){
		ArrayList <int [][][]> newsol = new ArrayList <int[][][]> ();
		int t = -1;
		loop:
		while (newsol.size() < 3000 && t == -1) {
			for (int i = 0; i < x.length; i++) {
				for(int j = 0; j < x[i].length; j++) {
					for (int k = 0; k < x[i][j].length;k++){
						if (x_original[i][j][k] == 1) {
							for (int l = 0; l < x.length; l++) {
								for(int m = 0; m < x[l].length; m++) {
									for (int n = 0; n < x[l][m].length;n++){
										if (x_original[l][m][n] == 1 && l != i) {
											int [][][] x_new = generateX(solutions.get(0));
											x_new[i][j][k] = 0;
											x_new[l][m][n] = 0;
											x_new[l][j][k] = 1;
											x_new[i][m][n] = 1;
											
											
											int [] o = new int [6];
											o[0] = i;
											o[1] = j;
											o[2] = k;
											o[3] = l;
											o[4] = m;
											o[5] = n;
											
											if(available(x_new,l,j,k,Co,Cu,Room_id,Room_cap,p,d) == true && available(x_new,i,m,n,Co,Cu,Room_id,Room_cap,p,d) == true ) {
												newsol.add(x_new);
												index_change.add(o);
												if (newsol.size() > 3000) {
													break loop;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			t=1;
			if (t == 1) {
				break loop;
			}
		}
		solutions.addAll(newsol);
		
	}
	
	
	
	public double ObjValue(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap,int p, int d) {
		double Obj = 0;
		double ObjU = 0;
		double ObjV = 0;
		double ObjP = 0;
		double ObjW = 0;
		double ObjA = 0;
		int [] U = new int[Co.length]; //unassigned courses
		int [][] V = new int [x[0].length][x[0][0].length]; //exceeds room cap
		int [] P = new int [Co.length]; // number of time a course changes room
		int [] W = new int [Co.length]; // number of days courses are below min_work_days 
		int [][] A = new int [Cu.length][x[0].length]; // curriculum in a time slot has secluded lecture
		int [] Cost = new int [Co.length];
		for(int i = 0; i < Co.length;i++) {
			U[i] = Co[i].getNr_Lec();
			W[i] = Co[i].getMin_days();
			
			
			int tempU = 0;
			int tempP = 0;
			int tempW = 0;
			String tempRoom = "";
			ArrayList <String> RoomCo = new ArrayList <String>();
			ArrayList <Integer> days = new ArrayList <Integer>();
			for(int j = 0; j < x[0].length; j++) {
				for(int k = 0; k < x[0][0].length;k++) {
					if(x[i][j][k]==1) {
						tempU+= 1;
					}
					if (tempRoom == "" && x[i][j][k] == 1) {
						tempRoom = Room_id[k];
						RoomCo.add(tempRoom);
					}
					if(x[i][j][k] == 1 && Room_id[k] != tempRoom && !RoomCo.contains(Room_id[k])) {
						tempRoom = Room_id[k];
						tempP += 1;
						RoomCo.add(tempRoom);
					}
					if (x[i][j][k] == 1 && !days.contains((j - j%p)/p)) {
						days.add((j - j%p)/p);
						tempW += 1;
					}
				}
			}
			if ((U[i]-tempU) < 0) {
				U[i] = 0;
			} else {
			U[i] = U[i]-tempU;
			}
			
			P[i] = tempP;
			if((W[i] - tempW) < 0) {
				W[i] = 0;
			} else {
			W[i] = W[i] - tempW;
			}
			
			Obj+= 10*U[i];
			Obj+= 1*P[i];
			Obj+= 5*W[i];
			
			Cost[i] = 10*U[i]+1*P[i]+5*W[i];
			
			ObjU += U[i];
			ObjW += W[i];
			ObjP += P[i];
			
		}
		for(int j = 0; j< x[0].length; j++) {
			for(int k = 0; k < x[0][0].length; k++) {
				int tempV = 0;
				for(int i = 0; i < x.length; i++) {
					if(x[i][j][k] == 1 && Room_cap[k] < Co[i].getNr_students()) {
						tempV += Co[i].getNr_students() - Room_cap[k];
						if (tempV < 0) {
							Cost[i] += tempV;
						}
					}
				}
				if (tempV < 0) {
					V[j][k] = 0;
				} else {
				V[j][k] = tempV;
				}
				Obj+= 1*V[j][k];
				ObjV += V[j][k];
			}
		}
		for(int q = 0; q < Cu.length; q++) {
			int dayCounter = 0;
			ArrayList <Integer> CoIdx = new ArrayList <Integer>();
			for(String Course: Cu[q].getCourse_nr()) {
				for(int i = 0; i < x.length; i++) {
					if(Course.equals(Co[i].getCourse_nr()) && !CoIdx.contains(i)) {
						CoIdx.add(i);
					}
				}
			}
			for(int j = 0; j<x[0].length; j++) {
				int tempQ = 0;
				int tempt2 = 0;
				dayCounter = (j-j%p)/p;
				for(int k = 0; k <x[0][0].length; k++) {
					for(int i: CoIdx) {
						if(x[i][j][k] == 1) {
							tempQ = 1;
							for(int r = 0 ; r <x[0][0].length; r++) {
								if(tempt2 == 0) {
								for(int c: CoIdx) {
											if((j-1) < dayCounter*p && j+1!= d*p) {
													tempt2 += x[c][j+1][r];
											} else if((j+1)>= (dayCounter+1)*p) {
													tempt2 += x[c][j-1][r];
											} else if((j-1)>= dayCounter*p && (j+1)< (dayCounter+1)*p) {
													tempt2 += x[c][j-1][r] + x[c][j+1][r];
											}
									}
								}
							
							}
						}
					}
				}
				if (tempt2 == 0 && tempQ == 1) {
					A[q][j] = 1;
				}
				Obj += 2*A[q][j];
				ObjA += A[q][j];
			}
		}
		
		
		return Obj;
		
	}
	

	public int[][][] returnX(){
		return x_best;
	}
}



