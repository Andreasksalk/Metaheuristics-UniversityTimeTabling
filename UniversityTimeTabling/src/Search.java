import java.util.ArrayList;
import java.util.Arrays;

class Search {
	int [][][] x_original;
	int [] unassigned;
	ArrayList <int [][][]> solutions = new ArrayList <int[][][]> ();
	int con1 = 0;
	int con2 = 0;
	int con3 = 0;
	int con4 = 0;
	int con5 = 0;
	int con6 = 0;
	
	public Search(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		
		int [][][] best_x = generateX(x);
		int iter = 0;
		double old_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		double min_obj = ObjValue(best_x, Co, Cu, Room_id, Room_cap, p, d);
		
		System.out.println("Construction: " + min_obj);
		int num_swaps = 0;
		
		while (iter - 1 < 2) {
			Neighbourhood(best_x,Co,Cu,Room_id,Room_cap,p,d);
			int i = 0;
			int min_i = 0;
			
			double [] ObjVals;
			ObjVals = new double [solutions.size()];
			for (int [][][] sol: solutions) {
				ObjVals[i] = ObjValue(sol, Co, Cu, Room_id, Room_cap, p, d);
				if (ObjVals[i] < min_obj) {
					min_obj = ObjVals[i];
					min_i = i;
				}
				i++;
			}
			best_x = solutions.get(min_i);
			if (min_obj >= old_obj) {
				iter++;
			}
			if (min_obj <= old_obj) {
				num_swaps++;
			}
			System.out.println("Best solution: " + min_obj);
			System.out.println("Number of swaps: " + num_swaps);
			old_obj = min_obj;
			
		}
		
		System.out.println("Con1: " + con1);
		System.out.println("Con2: " + con2);
		System.out.println("Con3: " + con3);
		System.out.println("Con4: " + con4);
		System.out.println("Con5: " + con5);
		System.out.println("Con6: " + con6);
	}
	
	public void Neighbourhood(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		x_original = new int [x.length][x[0].length][x[0][0].length];
		unassigned = new int[x.length];
		
		x_original = generateX(x);
		solutions.clear();
		solutions.add(x);
		
		
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
			//System.out.println(Co[i].getCourse_nr() + ": " + unassigned[i]);
		}
		
		for (int i = 0; i < x.length; i++) {
			for(int j = 0; j < x[i].length; j++) {
				for (int k = 0; k < x[i][j].length;k++){
					if (x_original[i][j][k] == 1) {
						for(int l = 0; l <x[i].length; l++) {
							x_new = generateX(x);
							x_new[i][j][k] = 0;
							if(l != j) {
								x_new[i][l][k] = 1;
								if(available(x_new,i,j,l,k,k,Co,Cu,Room_id,Room_cap,p,d) == true) {
									solutions.add(x_new);
								}
							}
						}
					}
				}
			}
		}
		System.out.println(solutions.size());
		
		
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
										solutions.add(x_new);
									}
								}
							}
						}
					}
				}
			}
		}
		*/
		//System.out.println(solutions.size());
		
		kSwap(x_original,solutions,Co,Cu,Room_id,Room_cap,p,d);
		//System.out.println(solutions.size());
	}
	
	public boolean available (int [][][] x,int i, int j,int j2, int r1, int r2, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		
		boolean feasible = true;
		ArrayList <Integer> index = new ArrayList <Integer> ();
		ArrayList <Integer> index2 = new ArrayList <Integer> ();
		
		index.add(j);
		index.add(j2);
		index2.add(r1);
		index2.add(r2);
		//x[class][period1][room] -> x[class][period2][room] 
		

		
		periodLoop:
		for (Integer k: index) {
			for (Integer l: index2) {
				// Checking if the Room has capacity for the students
				if (Room_cap[l]<Co[i].getNr_students()) {
					con1++;
					feasible = false;
					break periodLoop;
				}
				for (int c = 0; c< Co.length;c++) {
					if(x[c][k][l] == 0) {
						continue;
					}
					if (x[c][k][l] == 1 && Co[c].getLecture_nr() == Co[i].getLecture_nr() && Co[c].getCourse_nr() != Co[i].getCourse_nr()) {
						con2++;
						feasible = false;
						break periodLoop;
					}

					for (int h = 0; h < Cu.length; h++) {
						for(int o =0; o < Cu[h].getNum_courses();o++) {
							if(x[c][k][l] == 1 && Cu[h].getCourse_nr().get(o) == Co[i].getCourse_nr()) {
								con3++;
								feasible = false;
								break periodLoop;
							}
						}
					}
				}
				
				// Checking constraint matrix for availability
				if (Co[i].getBin_con()[k] == 0) {
					con4++;
					feasible = false;
					break periodLoop;
				}
				// Checking if another course is using the room
				/*
				for (int m=0; m<Co.length; m++) {
					if (x[m][k][l]==1) {
						con5++;
						feasible = false;
					break periodLoop;
					}
				}
				*/
				
				// Checking if course in that time period is used in other rooms
				for(int room = 0; room < Room_id.length; room ++) {
					if (x[i][k][room] == 1 && room != l) {
						con6++;
						feasible = false;
						break periodLoop;
					}
				}		
			}
		}
		
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
	
	public void kSwap(int [][][] x, ArrayList<int [][][]> solutions, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d){
		int [][][] x_new = new int [x.length][x[0].length][x[0][0].length];
		ArrayList <int [][][]> newsol = new ArrayList <int[][][]> ();
		for (int[][][] sol: solutions) {
			
			for(int i = 0; i < unassigned.length;i++) {
				unassigned[i] = Co[i].getNr_Lec();
				int temp = 0;
				for(int j = 0; j < x[0].length; j++) {
					for(int k = 0; k < x[0][0].length;k++) {
						if(sol[i][j][k]==1) {
							temp+= 1;
						}
					}
				}
				unassigned[i] = unassigned[i]-temp;
			}
			
			for (int i = 0; i < x.length; i++) {
				for(int j = 0; j < x[i].length; j++) {
					for (int k = 0; k < x[i][j].length;k++){
						if (x_original[i][j][k] == 1) {
							for (int l = 0; l < x.length; l++) {
								for(int m = 0; m < x[l].length; m++) {
									for (int n = 0; n < x[l][m].length;n++){
										if (x_original[l][m][n] == 1 && l != i) {
											x_new = generateX(sol);
											x_new[i][j][k] = 0;
											x_new[l][m][n] = 0;
											x_new[l][j][k] = 1;
											x_new[i][m][n] = 1;
											if(available(x_new,l,j,j,n,k,Co,Cu,Room_id,Room_cap,p,d) == true && available(x_new,i,m,m,n,k,Co,Cu,Room_id,Room_cap,p,d) == true ) {
												newsol.add(x_new);
											}
										}
									}
								}
							}
							for (int l = 0; l < x.length; l++) {
								if (unassigned[l] > 0 && l != i) {
									x_new[i][j][k] = 0;
									x_new[l][j][k] = 1;
									if(available(x_new,l,j,j,k,k,Co,Cu,Room_id,Room_cap,p,d) == true && available(x_new,i,j,j,k,k,Co,Cu,Room_id,Room_cap,p,d) == true ) {
										newsol.add(x_new);
									}
								}
							}
						}
					}
				}
			}
		}
		solutions.addAll(newsol);
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
			P[i] = tempP;
			W[i] = W[i] - tempW;
			
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



