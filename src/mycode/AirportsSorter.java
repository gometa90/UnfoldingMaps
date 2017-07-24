package mycode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsing.CSVReader;

public class AirportsSorter {

	public List<Airport> airports = new ArrayList<Airport>();
	public List<Airport> airportsSortedByCountry = new ArrayList<Airport>();

	public AirportsSorter() {
		List<String[]> airportsList = CSVReader.airportReaders();
		for (String[] airport : airportsList) {
			airports.add(new Airport(airport[2], airport[3], airport[4]));
		}
	}

	public static void main(String[] args) {
		AirportsSorter airportsSorter = new AirportsSorter();
		Collections.sort(airportsSorter.airports);
		System.out.println(airportsSorter);
	}
}
