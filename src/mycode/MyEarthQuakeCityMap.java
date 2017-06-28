package mycode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class MyEarthQuakeCityMap extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Unfolding map as a field
	private UnfoldingMap map;
	
	//The URL where the RSS is located
	private String rssURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	@Override
	public void setup() {
		//Setting the app window size
		size(1080, 720, OPENGL);
		//Creating the map to be shown, with a google map renderized
		map = new UnfoldingMap(this, 275,50, 745, 550, new Microsoft.RoadProvider());
		//Creating the dispatcher for the map
		MapUtils.createDefaultEventDispatcher(this, map);
		//Setting the zoom level
		map.zoomToLevel(2);
		
//		//Adding desired markers on the map in a certain location
//		Location marker1Loc = new Location(-38.14f, -73.03f);
//		SimplePointMarker marker1 = new SimplePointMarker(marker1Loc);
//		map.addMarker(marker1);
		
		//---------------------------------------------------------------------------
		
//		//Using Feature for define the markers
//		Location marker1Loc = new Location(-38.14f, -73.03f);
//		Feature featMk1 = new PointFeature(marker1Loc);
//		//Adding data to describe the marker into the feature
//		featMk1.addProperty("Title", "Valdivia, Chile");
//		featMk1.addProperty("Magnitude", "9.5");
//		featMk1.addProperty("Date", "May 22, 1960");
//		featMk1.addProperty("Year", "1960");
//		
//		//Creating the marker
//		Marker mk1 = new SimplePointMarker(marker1Loc, featMk1.getProperties());
//		//Showing the marker
//		map.addMarker(mk1);
		
		//------------------------------------------------------------------------------
		
//		//But now i want to have more than one marker with its features so I´m going to implement
//		// a list of it to represent the top5 highest earthquake in history.
//		
//		List<PointFeature> listPointfeatures = new ArrayList<PointFeature>();
//		Location locMk1 = new Location(-38.29f,-73.05f);
//		Feature featMk1 = new PointFeature(locMk1);
//		Location locMk2 = new Location(61.02f,-147.65f);
//		Feature featMk2 = new PointFeature(locMk2);
//		Location locMk3 = new Location(3.30f,95.78f);
//		Feature featMk3 = new PointFeature(locMk3);
//		Location locMk4 = new Location(38.322f,142.369f);
//		Feature featMk4 = new PointFeature(locMk4);
//		Location locMk5 = new Location(52.76f,160.06f);
//		Feature featMk5 = new PointFeature(locMk5);
//		
//		//feature1
//		featMk1.addProperty("Title", "Valdivia, Chile");
//		featMk1.addProperty("Magnitude", "9.5");
//		featMk1.addProperty("Date", "May 22, 1960");
//		featMk1.addProperty("Year", "1960");
//		//feature2
//		featMk2.addProperty("Title", "Great Alaska");
//		featMk2.addProperty("Magnitude", "9.2");
//		featMk2.addProperty("Date", "March 28, 1964");
//		featMk2.addProperty("Year", "1964");
//		//feature3
//		featMk3.addProperty("Title", "Sumatra West Coast");
//		featMk3.addProperty("Magnitude", "9.1");
//		featMk3.addProperty("Date", "Dec 26, 2004");
//		featMk3.addProperty("Year", "2004");
//		//feature4
//		featMk4.addProperty("Title", "Honshu, Japan");
//		featMk4.addProperty("Magnitude", "9.0");
//		featMk4.addProperty("Date", "March 11, 2011");
//		featMk4.addProperty("Year", "2011");
//		//feature5
//		featMk5.addProperty("Title", "Kamchatka");
//		featMk5.addProperty("Magnitude", "9.0");
//		featMk5.addProperty("Date", "Nov 04, 1952");
//		featMk5.addProperty("Year", "1952");
//		
//		//Adding it to the list
//		listPointfeatures.add((PointFeature) featMk1);
//		listPointfeatures.add((PointFeature) featMk2);
//		listPointfeatures.add((PointFeature) featMk3);
//		listPointfeatures.add((PointFeature) featMk4);
//		listPointfeatures.add((PointFeature) featMk5);
//		
//		//I want to create markers dynamically for each one PointFeature
//		//For each PointFeature i want to create a marker asociated with it and add it to the map 
//		List<Marker> markers = new ArrayList<Marker>();
//		for (PointFeature pFeature : listPointfeatures) {
//			markers.add(new SimplePointMarker(pFeature.getLocation(), pFeature.getProperties()));
//		}
//		
//		//Customizing the color depending on the year of the eq
//		setMarkersColour(markers);
//		map.addMarkers(markers);
		
		//------------------------------------------------------------------------------------
	
		//But again I don´t want to feed that list manually. Instead of that I want to use live data
		//which update my list of eq. for that there is a parser which reads data from a text file
		//and update the list of features, and in consequence, the map.
		List<PointFeature> eqPF = ParseFeed.parseEarthquake(this, rssURL);
		//For each earthquake PointFeature, add a marker in the map
		List<Marker> eqMarkers = new ArrayList<Marker>();
		for (PointFeature pointFeature : eqPF) {
			Location eqLoc = pointFeature.getLocation();
			HashMap<String, Object> eqProperties = pointFeature.getProperties();
			System.out.println("This is the hashMap toString:          "+eqProperties.toString());
			SimplePointMarker eqMarker = new SimplePointMarker(eqLoc, eqProperties);
//			//Setting the color (IF THE EARTHQUAKE IS TODAY RED, IF NOT OTHER)
//			setEqMarkersColor(eqMarker);
			//CUSTOMIZING THE COLOR BASED ON THE INSTRUCTIONS OF MODULE 3
			setEqMarkersColorAndSizeM3(eqMarker);
			
			eqMarkers.add(eqMarker);
			
			
		}
		
		map.addMarkers(eqMarkers);
		
	}
	
	
	@Override
	public void draw() {
		//Setting the background
		background(10);
		map.draw();
		addKey();
		
	}
	
//	private void setMarkersColour(List<Marker> mkList){
//		int yellow = color(255,255,0);
//		int gray = color(150,150,150);
//		for(Marker mk : mkList){
//			if(Integer.parseInt(mk.getStringProperty("Year")) > 2000){
//				mk.setColor(yellow);
//			}else{
//				mk.setColor(gray);
//			}
//		}
//	}
	
//	private void setEqMarkersColor(Marker marker){
//		int yellow = color(255,255,0);
//		int gray = color(150,150,150);
//		
//		String yearProperty = marker.getStringProperty("age");
//		System.out.println("The year of this eq is: "+yearProperty);
//		if(yearProperty.contains("Hour")){
//			marker.setColor(yellow);
//		}else{marker.setColor(gray);}
//		
//	}

	private void setEqMarkersColorAndSizeM3(SimplePointMarker marker){
		//Blue, yellow and red color for small, medium, and high eq magnitude respectively
		
		int blue = color(0,0,255);
		int yellow = color(255,255,0);
		int red = color(255,0,0);
		
		Object magnitudeObj = marker.getProperty("magnitude");
		float magnitude = Float.parseFloat(magnitudeObj.toString());
		
		if(magnitude < 4){
			marker.setRadius(5);
			marker.setColor(blue);
		}else{
			if( magnitude < 5 && magnitude >= 4){
				marker.setRadius(10);
				marker.setColor(yellow);
			}else{
				marker.setRadius(15);
				marker.setColor(red);
			}
		}
	}
	
	public void addKey(){
		//Background key color
		fill(color(240,240,200));
		quad(25, 150, 25, 500, 225, 500, 225, 150);
		//Red color
		fill(color(255,0,0));
		ellipse(50, 250, 15, 15);
		//Yellow color
		fill(color(255,255,0));
		ellipse(50, 300, 10, 10);
		//Blue color
		fill(color(0,0,255));
		ellipse(50, 350, 5, 5);
		//Title and text
		text("Earthquake Key",75,200);
		text("5.0+ magnitude",90,250);
		text("4.0+ magnitude",90,300);
		text("Below 4.0",90,350);
	}
	
}
