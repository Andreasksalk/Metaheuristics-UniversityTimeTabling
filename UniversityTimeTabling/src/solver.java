import java.io.FileNotFoundException;

public class solver {
	Course[] Co;
	int [][] bin;
	
	public solver(String[] filename) {
		try {
			dataReader instance = new dataReader(filename);
			this.Co = instance.getCourses();
			//bin = new int [Co[0].getBin_con().length][Co[0].getBin_con().length];
			//bin = Co[0].getBin_con();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		//bin = new int[Co[0].getBin_con().length][Co[0].getBin_con()[0].length];
		//bin = Co[0].getBin_con();
		
		/*for(int i =0; i< bin.length;i++) {
			System.out.println();
			for(int j = 0; j<bin[i].length;j++) {
				System.out.print(bin[i][j] + " ");
			}
				
		}*/
		bin = Co[0].getBin_con();
		for(int i = 0; i<bin.length;i++) {
			System.out.println();
			for(int j = 0; j<bin[0].length;j++) {
				System.out.print(bin[i][j]);
			}
		}
		//System.out.print(Co[0].getBin_con()+ " ");
	}
	

}
