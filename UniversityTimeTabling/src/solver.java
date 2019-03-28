import java.io.FileNotFoundException;

public class solver {
	Course[] Co;
	int [][] bin;
	
	public solver(String[] filename) {
		try {
			dataReader instance = new dataReader(filename);
			this.Co = instance.getCourses();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	

}
