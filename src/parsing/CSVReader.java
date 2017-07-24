package parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

	public static List<String[]> airportReaders() {

		String csvFile = "D:/Users/mgomez12/workspace/UCSDUnfoldingMaps/data/airports.dat";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<String[]> airports = new ArrayList<String[]>();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] airport = line.split(cvsSplitBy);
				airports.add(airport);

				// System.out.println("Country [code= " + airport[4] +
				// " , name="
				// + airport[5] + "]");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return airports;

	}

	public static void main(String[] args) {
		List<String[]> airportsList = CSVReader.airportReaders();
		System.out.println(Arrays.toString(airportsList.toArray()));
	}
}