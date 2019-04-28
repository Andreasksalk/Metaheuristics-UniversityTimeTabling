import java.util.ArrayList;
import java.util.Random;

class ALNS {
	int [][][] x_original;
	int [][][] x_best;
	int [] unassigned;
	ArrayList <int [][][]> solutions = new ArrayList <int[][][]> ();
	double obj_best;
	int con1 = 0;
	int con2 = 0;
	int con3 = 0;
	int con4 = 0;
	int con5 = 0;
	int con6 = 0;
	ArrayList <int []> swap_change = new ArrayList <int[]> ();
	int all_sol = 0;
	ArrayList <int []> index_change = new ArrayList <int[]> ();
	int[] cost_t;
	
	
	public ALNS (int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		int iter = 0;
		int [][][] x_t = generateX(x); // generateX not implemented
		int [][][] x_b = generateX(x);
		cost_t = new int [x.length];
		x_best = generateX(x);
		obj_best = ObjValue(x_best, Co, Cu, Room_id, Room_cap, p, d);
		
		while (iter < 20) {
			x_b = generateX(x_best);
			System.out.println("Iter: " + iter);
			x_b = worst_removal(x_b, cost_t ,10);
			x_b = insert(x_b, Co, Cu, Room_id, Room_cap, p, d);
			//Search sol2 = new Search(x_b,Co,Cu,Room_id,Room_cap,p,d);
			//x_b = sol2.returnX();
			double x_b_obj = ObjValue(x_b, Co, Cu, Room_id, Room_cap, p, d);
			if (x_b_obj <= obj_best) {
				x_best = generateX(x_b);
				obj_best = x_b_obj;
				System.out.println("Best: " + obj_best);
			}
			iter++;
		}
		
		//while (iter < 100) {
			//rho = roulette_wheel(rho_plus, rho_minus);
			//d = select("Set of destroy",rho); // Roulette wheel function and destroy functions not implemented
			//r = select("Set of repair", rho); // Select not implemented
			//x_t(r(d(x_t)));
			//Search sol = new Search(x_t,Co,Cu,Room_id,Room_cap,p,d);
			//if (ObjValue(x_t,Co,Cu,Room_id,Room_cap,p,d) <= ObjValue(x_b,Co,Cu,Room_id,Room_cap,p,d)) {
			//	x_b = x_t;
			//	x_t = generateX(x_b);
			//}
			//update_w(rho_plus, rho_minus);
			//iter++;
		//}
	}
	
	public int [][][] insert (int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		int [][][] best_x = generateX(x);
		
		
		
		int iter = 0;
		double old_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double min_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double iter_min = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		
		int min_i = 0;

		//while (iter < 1) {
		n_insert(best_x,Co,Cu,Room_id,Room_cap,p,d);
		//System.out.println("Sol size: " + solutions.size());
		min_i = 0;
			
		int iter2 = 1;
		int old_sol = 0;
		while (solutions.size() > 1 && solutions.size() != old_sol) {
			old_sol = solutions.size();
			min_i = 0;
			int q = 0;
			double [] ObjVals;
			ObjVals = new double [solutions.size()];
			for (int [][][] sol: solutions) {
				ObjVals[q] = ObjValue(sol, Co, Cu, Room_id, Room_cap, p, d);
				if (ObjVals[q] < min_obj) {
					min_obj = ObjVals[q];
					min_i = q;
					//best_x = solutions.get(min_i);
				}
				q++;
			}
			
			if (min_obj >= old_obj) {
				iter2++;
			}
			
			//System.out.println("Sol size: " + solutions.size());
			//System.out.println("min obj: " + min_obj);
			if (min_i != 0) {
				best_x = solutions.get(min_i);
				int [] change = index_change.get(min_i);
				int t = 0;
				/*ArrayList <int []> f = new ArrayList <int[]> ();
				for (int [] c: index_change) {
					if (c[1] == change[1] && c[2] == change[2]) {
						solutions.remove(t);
						f.add(c);
						t = t-1;
					}
					t++;
				}
				index_change.removeAll(f);*/
				ArrayList <int [][][]> a = new ArrayList <int[][][]> ();
				int b = 0;
				for (int [][][] sol: solutions) {
					sol[change[0]][change[1]][change[2]] = 1;
					if (available(sol,change[0],change[1],change[2],Co,Cu,Room_id,Room_cap,p,d) == false) {
						a.add(sol);
						index_change.remove(b);
						b = b-1;
					}
					b++;
				}
				solutions.removeAll(a);
			}
			old_obj = min_obj;
			
		}
		if (min_obj >= iter_min) {
			iter++;
		}
		iter_min = min_obj;
		
		//}

		
		return best_x;
	}
	
	public void n_insert (int [][][] x,  Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d)  {
		ArrayList <int []> index_pos = new ArrayList <int[]> ();
		
		solutions.clear();
		solutions.add(x);
		
		index_change.clear();
		int [] dummy = new int [3];
		for (int i = 0; i < 3; i++) {
			dummy[i] = 0;
		}
		index_change.add(dummy);
		
		for (int l = 0; l<x.length; l++) {
			for (int m = 0; m<x[0].length; m++) {
				for (int n = 0; n<x[0][0].length; n++) {
					if (x[l][m][n] == 0) {
						int [] o = new int [3];
						o[0] = l;
						o[1] = m;
						o[2] = n;
						index_pos.add(o);
					}
				}
			}
		}
		unassigned = new int[x.length];
		for(int i = 0; i < unassigned.length;i++) {
			unassigned[i] = Co[i].getNr_Lec();
			int temp = 0;
			for(int j = 0; j < x[0].length; j++) {
				for(int k = 0; k < x[0][0].length;k++) {
					if(x[i][j][k]==1) {
						temp+= 1;
					}
				}
			}
			unassigned[i] = unassigned[i]-temp;
		}
		
		for (int i = 0; i< unassigned.length; i++) {
			if (unassigned[i] > 0) {
				for (int k = 0; k<index_pos.size(); k++) {
					int [][][] x_new = generateX(x);
					int [] o = index_pos.get(k);
					x_new[o[0]][o[1]][o[2]] = 1;
					if (available(x_new,o[0],o[1],o[2],Co,Cu,Room_id,Room_cap,p,d) == true) {
						solutions.add(x_new);
						index_change.add(o);
					}
				}
			}
		}
	}
	
	public int [][][] worst_removal(int [][][] x, int[] Cost, int amount){
		int [][][] x_gen = generateX(x);
		ArrayList <Integer> cost_index = new ArrayList <Integer>();
		while (cost_index.size() < amount) {
			int max = -1;
			int index = -1;
			for(int i = 0; i < Cost.length; i++) {
				if (max < Cost[i] && !cost_index.contains(i)) {
					max = Cost[i];
					index = i;
				}
			}
			if (index >= 0) {
			cost_index.add(index);
			}
		}
			
		for(int i: cost_index) {
			for(int j = 0; j < x[0].length; j++) {
				for (int k = 0; k <x[0][0].length; k++) {	
					x_gen[i][j][k] = 0;
				}
			}
		}
		
		return x_gen;
	}
	
	public int [][][] random_removal (int [][][] x, int n_rem) {
		int [][][] x_gen = generateX(x);
		ArrayList <int []> index_sol = new ArrayList <int[]> (); // index of assigned [l][m][n]
		for (int l = 0; l<x_gen.length; l++) {
			for (int m = 0; m<x_gen[0].length; m++) {
				for (int n = 0; n<x_gen[0][0].length; n++) {
					if (x_gen[l][m][n] == 1) {
						int [] o = new int [3];
						o[0] = l;
						o[1] = m;
						o[2] = n;
						index_sol.add(o);
					}
				}
			}
		}
		int iter = 0; 
		while (iter < n_rem) {
			Random rand = new Random();
			int a_rand = rand.nextInt(index_sol.size());
			int [] o = index_sol.get(a_rand);
			x_gen[o[0]][o[1]][o[2]] = 0;
			index_sol.remove(a_rand);
			iter++; 
		}
		
		
		return x_gen;
	}
	
	public int[][][] returnX(){
		return x_best;
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
	
	public boolean available (int [][][] x,int i, int j, int r, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		
		boolean feasible = false;
		
		//x[class][period1][room] -> x[i][j][r] 
		if (Room_cap[r]<Co[i].getNr_students()) {
			con1++;
			feasible = false;
			return feasible;
		}
			
		
		if (Co[i].getBin_con()[j] == 0) {
			con2++;
			feasible = false;
			return feasible;
		}
		
		// Checking if another course is using the room
		for (int m=0; m<x.length; m++) {
			if (x[m][j][r]==1 && m != i) {
				feasible = false;
				return feasible;
			}
		}
		
		for(int n = 0; n < x.length; n++) {
			for(int room = 0; room < x[0][0].length; room++) {
				if(x[n][j][room] == 1 && Co[n].getLecture_nr().equals(Co[i].getLecture_nr()) && !Co[n].getCourse_nr().equals(Co[i].getCourse_nr())) {
					feasible = false;
					con3++;
					return feasible;
				}
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
						return feasible;
					}
				}
			}
		}
		feasible = true;
		return feasible;
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
			//System.out.println(days);
			if ((U[i]-tempU) < 0) {
				U[i] = 0;
			} else {
			U[i] = U[i]-tempU;
			}
			
			P[i] = tempP;
			if((W[i] - tempW) < 0) {
				W[i] = 0;
			} else {
			//System.out.println(W[i] + " " + tempW);
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
		int con1 = 0;
		int con2 = 0;
		int con3 = 0;
		int con4 = 0;
		for(int q = 0; q < Cu.length; q++) {
			int dayCounter = 0;
			ArrayList <Integer> CoIdx = new ArrayList <Integer>();
			for(String Course: Cu[q].getCourse_nr()) {
				for(int i = 0; i < x.length; i++) {
					//System.out.println(C);
					if(Course.equals(Co[i].getCourse_nr()) && !CoIdx.contains(i)) {
						CoIdx.add(i);
					}
				}
			}
			for(int j = 0; j<x[0].length; j++) {
				int tempQ = 0;
				int tempt2 = 0;
				dayCounter = (j-j%p)/p;
				//System.out.println(dayCounter);
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
		
		cost_t = Cost;
		return Obj;
		
	}
}
