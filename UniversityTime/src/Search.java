import java.util.ArrayList;
import java.util.Arrays;

class Search {
	int [][][] x_original;
	int [] unassigned;
	ArrayList <int [][][]> solutions = new ArrayList <int[][][]> ();
	
	public Search(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		Neighbourhood(x,Co,Cu,Room_id,Room_cap,p,d);
	}
	
	public void Neighbourhood(int [][][] x, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		x_original = new int [x.length][x[0].length][x[0][0].length];
		int [][][] x_new = new int [x.length][x[0].length][x[0][0].length];
		unassigned = new int[x.length];
		
		x_original = generateX(x);
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
								if(available(x_new,i,j,l,Co,Cu,Room_id,Room_cap,p,d) == true) {
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
									if(available(x_new,i,j,j,Co,Cu,Room_id,Room_cap,p,d) == true) {
										solutions.add(x_new);
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println(solutions.size());
		
		kSwap(x_original,solutions,Co,Cu,Room_id,Room_cap,p,d);
		System.out.println(solutions.size());
	}
	
	public boolean available (int [][][] x,int i, int j,int j2, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d) {
		
		boolean feasible = true;
		ArrayList <Integer> index = new ArrayList <Integer> ();
		
		index.add(j);
		index.add(j2);
		//x[class][period1][room] -> x[class][period2][room] 
		periodLoop:
		for (Integer k: index) {
			for (int l=0; l<Room_id.length; l++) {
				// Checking if the Room has capacity for the students
				if (Room_cap[l]<Co[i].getNr_students()) {
					//System.out.println("hej1");
					feasible = false;
					break periodLoop;
				}
				for (int c = 0; c< Co.length;c++) {
					if(x[c][k][l] == 0) {
						continue;
					}
					if (x[c][k][l] == 1 && Co[c].getLecture_nr() == Co[i].getLecture_nr() && Co[c].getCourse_nr() != Co[i].getCourse_nr()) {
						//System.out.println("hej2");
						feasible = false;
						break periodLoop;
					}

					for (int h = 0; h < Cu.length; h++) {
						for(int o =0; o < Cu[h].getNum_courses();o++) {
							if(x[c][k][l] == 1 && Cu[h].getCourse_nr().get(o) == Co[i].getCourse_nr()) {
								//System.out.println("hej3");
								feasible = false;
								break periodLoop;
							}
						}
					}
				}
				
				// Checking constraint matrix for availability
				if (Co[i].getBin_con()[k] == 0) {
					//System.out.println("hej4");
					feasible = false;
					break periodLoop;
				}
				// Checking if another course is using the room
				for (int m=0; m<Co.length; m++) {
					if (x[m][k][l]==1) {
						//System.out.println("hej5");
						feasible = false;
						break periodLoop;
					}
				}
				
				// Checking if course in that time period is used in other rooms
				for(int room = 0; room < Room_id.length; room ++) {
					if (x[i][k][room] == 1) {
						//System.out.println("hej6");
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
	
	public ArrayList<int [][][]> kSwap(int [][][] x, ArrayList<int [][][]> solutions, Course[] Co, Curricula[] Cu, String [] Room_id, int [] Room_cap, int p, int d){
		int [][][] x_new = new int [x.length][x[0].length][x[0][0].length];
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
										if (x_new[l][m][n] == 1 && l != i) {
											x_new = generateX(sol);
											x_new[i][j][k] = 0;
											x_new[l][m][n] = 0;
											x_new[l][j][k] = 1;
											x_new[i][m][n] = 1;
											if(available(x_new,l,j,j,Co,Cu,Room_id,Room_cap,p,d) == true && available(x_new,i,m,m,Co,Cu,Room_id,Room_cap,p,d) == true ) {
												solutions.add(x_new);
											}
										}
									}
								}
							}
							// for all unassigned swap
						}
					}
				}
			}
		}
		return solutions;
	}
	
	
	
	public double ObjValue(int [][][] x, Course[] Co, Curricula[] Cu, int [] Room_cap) {
		double Obj = 0;
		int [] U = new int[Co.length]; //unassigned courses
		int [][] V = new int [x[0].length][x[0][0].length]; //exceeds room cap
		int [] P = new int [Co.length]; // number of time a course changes room
		int [] W = new int [Co.length]; // number of days courses exceed min_work_days 
		int [][] A = new int [Cu.length][x[0].length]; // curriculum in a time slot has secluded lecture
		
		for(int i = 0; i < U.length;i++) {
			U[i] = Co[i].getNr_Lec();
			
			
			
			int tempU = 0;
			int tempV = 0;
			for(int j = 0; j < x[0].length; j++) {
				for(int k = 0; k < x[0][0].length;k++) {
					if(x[i][j][k]==1) {
						tempU+= 1;
					}
					if(x[i][j][k] == 1 && Room_cap[k] < Co[i].getNr_students()) {
						
					}
				}
			}
			
			
			U[i] = U[i]-tempU;
		}
		
		
		return Obj;
		
	}
}



