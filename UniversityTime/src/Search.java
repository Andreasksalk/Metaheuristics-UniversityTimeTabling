import java.util.ArrayList;

class Search {
	int [][][] x_original;
	int [][][] x_best;
	int [] unassigned;
	ArrayList <int [][][]> solutions = new ArrayList <int[][][]> ();
	int con1 = 0;
	int con2 = 0;
	int con3 = 0;
	int con4 = 0;
	int con5 = 0;
	int con6 = 0;
	ArrayList <int []> swap_change = new ArrayList <int[]> ();
	int all_sol = 0;
	
	public Search(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		
		int [][][] best_x = generateX(x);
		int iter = 1;
		double old_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double min_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double iter_min = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		
		int min_i = 0;
		
		/*for (int i = 0; i<x.length; i++) {
			System.out.print("Course " + i + ":");
			for (int j = 0; j<x[0].length; j++) {
				System.out.print(Co[i].getBin_con()[j] + " ");
			}
			System.out.println();
		}*/
		
		System.out.println("Construction: " + min_obj);
		int num_swaps = 0;
		int num_n = 0;

		while (iter - 1 < 2) {
			num_n++;
			//System.out.println("Number of neighborhoods: " + num_n);
			Neighbourhood(best_x,Co,Cu,Room_id,Room_cap,p,d);
			int num_swaps_n = 0;
			
			min_i = 0;
			
			int iter2 = 1;
			while (iter2 - 1 < 2) {
				int q = 0;
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
					System.out.println("Best solution: " + min_obj);
					System.out.println("Number of swaps: " + num_swaps);
					//System.out.println("Number of swaps in neighborhood: " + num_swaps_n);
					/*System.out.println("Course 1:");
					System.out.println("Course: " + sc[0] + ", Period: " + sc[1] + ", Room: " + sc[2]);
					System.out.println("Course 2:");
					System.out.println("Course: " + sc[3] + ", Period: " + sc[4] + ", Room: " + sc[5]);*/
					
				}
				
				if (min_obj >= old_obj) {
					iter2++;
				}
				
				
				best_x = solutions.get(min_i);
				old_obj = min_obj;
				//sol_change(min_i);
				
			}
			if (min_obj >= iter_min) {
				iter++;
			}
			iter_min = min_obj;
			
		}
		
		System.out.println("Number of solutions generated: " + all_sol);
		System.out.println("Con1: " + con1);
		System.out.println("Con2: " + con2);
		System.out.println("Con3: " + con3);
		System.out.println("Con4: " + con4);
		System.out.println("Con5: " + con5);
		
		x_best = generateX(best_x);
	}
	
	public void Neighbourhood(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		x_original = new int [x.length][x[0].length][x[0][0].length];
		unassigned = new int[x.length];
		int [] dummy = new int [7];
		for (int i = 0; i < 7; i++) {
			dummy[i] = 0;
		}
		
		x_original = generateX(x);
		solutions.clear();
		solutions.add(x);
		swap_change.clear();
		swap_change.add(dummy);
		
		
		/* course 1 - now: day1 period 1-6. ---> course 1 - solution 1: day1 period 1-5 period 6 ---> day 5 period 6. (all available periods with all courses)
		 * new solutions: swap to all available rooms for all courses and save ALL solutions
		 * new new solutions: swap courses with all feasible courses, save ALL solutions 
		 * all (new and new new solutions) objective value calculated and keep doing the above until no improvement
		 * Pick best solution and move on with our life.
		 * REMEMBER TO CHECK FOR FEASIABILITY IN ALL SOLUTIONS (FIRST TASK TOMORROW)
		 * 
		 * Construction: All unassigned lectures in courses try to assign (if possible)
		 * SEARCH AND DESTROY NITWELVE
		 */
		
		kSwap(x_original,Co,Cu,Room_id,Room_cap,p,d);
		
		System.out.println("Number of solutions in neighborhood: " + solutions.size());
		all_sol += solutions.size();
		
		/*
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
		
		for (int[][][] sol: solutions) {
			for (int i = 0; i < x.length; i++) {
				for(int j = 0; j < x[i].length; j++) {
					for (int k = 0; k < x[i][j].length;k++){
						if (x_original[i][j][k] == 1) {
							for(int l = 0; l <x[i].length; l++) {
								x_new = generateX(sol);
								x_new[i][j][k] = 0;
								if(l != j) {
									x_new[i][l][k] = 1;
									if(available(x_new,i,j,l,k,k,Co,Cu,Room_id,Room_cap,p,d) == true) {
										newsol1.add(x_new);
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		for (int[][][] sol: solutions) {
			for (int i = 0; i < x.length; i++) {
				for(int j = 0; j < x[i].length; j++) {
					for (int k = 0; k < x[i][j].length;k++){
						if (x_original[i][j][k] == 1) {
							for(int l = 0; l <x[i][j].length; l++) {
								x_new = generateX(sol);
								x_new[i][j][k] = 0;
								if(l != k) {
									x_new[i][j][l] = 1;
									if(available(x_new,i,j,j,k,l,Co,Cu,Room_id,Room_cap,p,d) == true) {
										newsol2.add(x_new);
									}
								}
							}
						}
					}
				}
			}
		}
		solutions.addAll(newsol1);
		solutions.addAll(newsol2);*/

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
	
	
	/*public int[][][] returnX(){
		return x_new;
	}*/
	
	public void kSwap(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d){
		ArrayList <int [][][]> newsol = new ArrayList <int[][][]> ();
		//for (int[][][] sol: solutions) {
			
			/*for(int i = 0; i < unassigned.length;i++) {
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
			}*/
			
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
											
											
											int [] o = new int [7];
											o[0] = i;
											o[1] = j;
											o[2] = k;
											o[3] = l;
											o[4] = m;
											o[5] = n;
											o[6] = 1; // 1 for course swap
											
											if(available(x_new,l,j,k,Co,Cu,Room_id,Room_cap,p,d) == true && available(x_new,i,m,n,Co,Cu,Room_id,Room_cap,p,d) == true ) {
												newsol.add(x_new);
												swap_change.add(o);
											}
										}
									}
								}
							}
							/*	for (int l = 0; l < x.length; l++) {
								if (unassigned[l] > 0 && l != i) {
									for (int q = 0; q < unassigned[l]; q++) {
										int [][][] x_new = generateX(x);
										x_new[i][j][k] = 0;
										x_new[l][j][k] = 1;
									
										int [] o = new int [7];
										o[0] = i;
										o[1] = j;
										o[2] = k;
										o[3] = l;
										o[4] = j;
										o[5] = k;
										o[6] = 0; // 0 for unassigned
									
									
										if(available(x_new,l,j,k,Co,Cu,Room_id,Room_cap,p,d) == true) {
											//newsol.add(x_new);
											//swap_change.add(o);
										}
									}
								}
							}*/
						}
					}
				}
			}
		//}
		solutions.addAll(newsol);
		//System.out.println("Sol Size: " + solutions.size());
		//System.out.println("Sol Size: " + swap_change.size());
	}
	
	
	
	public double ObjValue(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap,int p, int d) {
		double Obj = 0;
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
			if (U[i]-tempU < 0) {
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
			}
		}
		
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
			for (int j = 0; j < x[0].length; j++) {
				int sumQ = 0;
				dayCounter = (j-j%p)/p;
				int index = -1;
				int tempt2 = 1;
				int tempA = 0;
				roomLoop:
				for(int k = 0; k < x[0][0].length; k++) {
					for (int i: CoIdx) {
						if (x[i][j][k] == 1)
							sumQ += 1;
							index = i;
							break roomLoop;
					}
				}
				if (sumQ == 1) {
					for(int k = 0; k <x[0][0].length;k++) {
						for(int i: CoIdx) {
							if (i != index) {
							if(j - 1 < dayCounter*p && j+1 != d*p) {
								if(x[i][j+1][k] == 0) {
									tempt2 = 0;
								}
							} else if (j+1 >= (dayCounter+1)*p) {
								if(x[i][j-1][k] == 0) {
									tempt2 = 0;
								}
							} else if(j -1 >= dayCounter*p && j+1 < (dayCounter+1)*p) { 
								if (x[i][j-1][k] == 0 && x[i][j+1][k] == 0) {
									tempt2 = 0;
								}
							} else {
								tempt2 = 1;
							}
						}
						}
					}
				}
				if(sumQ == 1 && tempt2 == 0) {
					tempA = 1;
				}
				A[q][j] = tempA;
				Obj+= 2* A[q][j];
				
			}
		}
		
		
		
		return Obj;
		
	}
	
	public void sol_change (int i) {
		int [] sc = swap_change.get(i);
		
		for (int j = 0; j <solutions.size(); j++) {
			int [][][] sol = solutions.get(j);
			if (sc[6] == 1) {
				sol[sc[0]][sc[1]][sc[2]] = 0;
				sol[sc[3]][sc[4]][sc[5]] = 0;
				sol[sc[3]][sc[1]][sc[2]] = 1;
				sol[sc[1]][sc[4]][sc[5]] = 1;
			} else if (sc[6] == 0) {
				sol[sc[0]][sc[1]][sc[2]] = 0;
				sol[sc[3]][sc[1]][sc[2]] = 1;
			}
		}
	}
	public int[][][] returnX(){
		return x_best;
	}
}



