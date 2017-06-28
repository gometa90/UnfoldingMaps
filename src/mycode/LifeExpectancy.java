package mycode;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UnfoldingMap map ;
	
	private HashMap<String, Float> lifeExpectByCountry;
	private List<Feature> countriesFeatures;
	private List<Marker> countriesMarkers;
	
	
	public void setup(){
		//Setting the window size and render
		size(800, 600, OPENGL);
		//Creating a map
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		//Configuring it to be interactive
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomToLevel(2);
		
		//We are going to collect data from a csv archive. For that we can use a method in 
		//parseFeed Class which returns a HashMap<String country, Float>
		lifeExpectByCountry = ParseFeed.loadLifeExpectancyFromCSV(this, "LifeExpectancyWorldBank.csv");
		System.out.println("The hashMap is: "+lifeExpectByCountry.toString());
		
		//Now we want to color each country depending on its life expectancy.
		//For that we are going to read all the features (borders and more) from a JSON archive,
		//and create a marker based on it. Then colorize it depending on the life expectancy of 
		//the same country
		
		//We are gonna have two lists, one for countries features and another for countries markers

		//Reading the features from the JSON file.
		countriesFeatures = GeoJSONReader.loadData(this, "countries.geo.json");
		countriesMarkers = MapUtils.createSimpleMarkers(countriesFeatures);
		//now I´m going to paint every country marker depending in its life expectancy
		countryPaintingLE(countriesMarkers);
		
		//Finally add all them to the world map
		map.addMarkers(countriesMarkers);
		
	}
	
	public void draw(){
		background(10);
		map.draw();
	}
	
	private void countryPaintingLE(List<Marker> countryMarkers){
		
		for (Marker marker : countryMarkers) {
			String wichCountry = marker.getId();
			//If we have data of this country, paint it depending on its life expectancy
			if(lifeExpectByCountry.containsKey(wichCountry)){
				float lifeExpec = lifeExpectByCountry.get(wichCountry);
				int colorLevel = (int) map(lifeExpec, 45, 90, 10, 255);
				marker.setColor(color((255-colorLevel),100,colorLevel));
				
			}else{
				//If not, default color
				marker.setColor(color(150, 150, 150));
			}
		}
	}
}
