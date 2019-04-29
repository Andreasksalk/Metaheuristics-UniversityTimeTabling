import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

//import java.io.FileNotFoundException;

public class programRun {

	public static void main(String[] args) {
		ArrayList <Double> obj = new ArrayList <Double> (); 
		
		//String[] filename1 = {"basic.utt", "courses.utt", "curricula.utt", "lecturers.utt", "relation.utt", "rooms.utt", "unavailability.utt","Test01//"};
		
		double [] s_time = {60.0, 90.0, 120.0, 150.0};
		double [] rem_pro = {0.10, 0.20, 0.30, 0.40};
		boolean [] room_c_con = {true, false};
		for (double search_time: s_time) {
			for (double removal_pro: rem_pro) {
					for (boolean room_cap_con: room_c_con) {
						String[] filename1 = {"basic.utt", "courses.utt", "curricula.utt", "lecturers.utt", "relation.utt", "rooms.utt", "unavailability.utt","Test01//"};
						StopWatch watch1 = new StopWatch();
						watch1.start();
						solver data1 = new solver(filename1,watch1, search_time, removal_pro, room_cap_con);
						data1.solve();
						
						obj.add(data1.returnObj());
						
						//watch1.stop();
						
						StopWatch watch2 = new StopWatch();
						watch2.start();
						
						String[] filename2 = {"basic.utt", "courses.utt", "curricula.utt", "lecturers.utt", "relation.utt", "rooms.utt", "unavailability.utt","Test06//"};
						
						solver data2 = new solver(filename2,watch2, search_time, removal_pro, room_cap_con);
						data2.solve();
						
						obj.add(data2.returnObj());
						
						//watch2.stop();
						
						StopWatch watch3 = new StopWatch();
						watch3.start();
						
						String[] filename3 = {"basic.utt", "courses.utt", "curricula.utt", "lecturers.utt", "relation.utt", "rooms.utt", "unavailability.utt","Test12//"};
						
						solver data3 = new solver(filename3,watch3, search_time, removal_pro, room_cap_con);
						data3.solve();
						
						obj.add(data3.returnObj());
						
						//watch3.stop();
					}
				}
			}
		writeSol("SolutionPar.txt", obj);
		//data.printOutput();
	}
	public static void writeSol (String filename, ArrayList <Double> obj) {
		BufferedWriter writer = null;
		double [] s_time = {30.0, 60.0, 90.0, 120.0};
		double [] rem_pro = {0.10, 0.20, 0.30, 0.40};
		boolean [] room_c_con = {true, false};
		String [] data_sets = {"Test 01", "Test 06", "Test 12"};
		
		try {
			File solFile = new File(filename);
			solFile.createNewFile();
			int i  = 0;
			writer = new BufferedWriter(new FileWriter(filename,true));
			for (double search_time: s_time) {
				for (double removal_pro: rem_pro) {
					for (boolean room_cap_con: room_c_con) {
						for (String s: data_sets) {
							writer.write(s + " &" +  room_cap_con + " &" + removal_pro + " &" + search_time + " &" + obj.get(i) + "\\\\ \\hline");
							writer.newLine();
							i++;
						}
					}
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
		writer.close();
		} catch(Exception e) {
			
		}
	}
}


