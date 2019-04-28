//import java.io.FileNotFoundException;

public class programRun {

	public static void main(String[] args) {
		
		String[] filename = {"basic.utt", "courses.utt", "curricula.utt", "lecturers.utt", "relation.utt", "rooms.utt", "unavailability.utt","Test01//"};
		
		solver data = new solver(filename);
		data.solve();
		//data.printOutput();
	}

}

