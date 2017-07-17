package module5;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;
	//Auxiliary list of both markers
	private List<Marker> cityAndQuakeMarkers = new ArrayList<Marker>();

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	    
	    //auxiliary List containing all markers, cities and quakes.
		cityAndQuakeMarkers.addAll(cityMarkers);
		cityAndQuakeMarkers.addAll(quakeMarkers);
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		//Draw lines between cities and eq if necessary
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		for (Marker marker : markers) {
			if(marker.isInside(map, mouseX	, mouseY)){
				marker.setSelected(true);
				lastSelected = (CommonMarker) marker;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked() {
		//Determine whether any marker has been selected in this last click
		for (Marker marker : cityAndQuakeMarkers) {
			if(marker.isSelected()){
				lastClicked = (CommonMarker) marker;
			}
				
		}
		
		if(lastClicked == null){
			unhideMarkers();
		}else{
			Location lastSelectedLocation = lastClicked.getLocation();
			//Depending on the class, do what correspond
			if(lastClicked instanceof EarthquakeMarker){
				//Unhide all quake markers except the selected one
				for (Marker eqMarker : quakeMarkers) {
					if(!eqMarker.equals(lastClicked)){
						eqMarker.setHidden(true);
					}
				}
				//Also hide all cities not threatened for this EarthQuake
				//Determining the threat circle of this eq
				double threatRadius = ((EarthquakeMarker) lastClicked).threatCircle();
				for (Marker cityMarker : cityMarkers) {
					//Distance to the eq from this city
					double distance = cityMarker.getDistanceTo(lastSelectedLocation);
					//If the distance between markers is more than the threat radius
					//then hide this marker, if not, unhide it
					if(distance > threatRadius){
						cityMarker.setHidden(true);
					}else{
						cityMarker.setHidden(false);
						
						}
				}
			}
			//Do it similar for cityMarkers
			if(lastClicked instanceof CityMarker){
				//Hide all other cities except the selected one
				for (Marker cityMarker : cityMarkers) {
					if(!cityMarker.equals(lastClicked)){
						cityMarker.setHidden(true);
					}
				}
				//Check if some eq has threatened the city, and only show those ones.
				for (Marker eqMarker : quakeMarkers) {
					double threatCircle = ((EarthquakeMarker) eqMarker).threatCircle();
					double distance = eqMarker.getDistanceTo(lastSelectedLocation);
					if(distance > threatCircle){
						eqMarker.setHidden(true);
					}else{
						eqMarker.setHidden(false);
					}
				}
			}
		}
		//deselect all
		lastClicked = null;
		deselectMarkers();
	}
	
//	@Override
//	public void mouseClicked()
//	{
//		System.out.println("Mouse Clicked");
//		// Hint: You probably want a helper method or two to keep this code
//		// from getting too long/disorganized
//		
//		cityAndQuakeMarkers.addAll(cityMarkers);
//		cityAndQuakeMarkers.addAll(quakeMarkers);
//		//Checks whether lastClicked is null or not. If it is null, we have to show
//		//that markers and the affected near ones. If one was selected, we have to
//		//deselect this and show all.
//		
//		if(lastClicked != null){
//			//Put it to null and show all
//			lastClicked = null;
//			unhideMarkers();
//		}else{
//			//Determine which marker is selected is any
//			for (Marker marker : cityAndQuakeMarkers) {
//				if(marker.isSelected()){
//					lastClicked = (CommonMarker) marker;
//				}
//			}
//			
//			//If any
//			if(lastClicked != null){
//			//Once found the selected one, we have to calculate the affected neigh-
//			//borhood
//			Location clickedMarkerLocation = lastClicked.getLocation();
//			//If the lasClicked is a EarthquakeMarker
//			if(lastClicked instanceof EarthquakeMarker){
//				double threatCircleRadius = ((EarthquakeMarker) lastClicked).threatCircle();
//				for (Marker cityMarker : cityMarkers) {
//					//Check if the distance is less than the threat circle
//					double distanceToEq = cityMarker.getDistanceTo(clickedMarkerLocation);
//					if(distanceToEq > threatCircleRadius){
//						cityMarker.setHidden(true);
//					}else{
//						cityMarker.setHidden(false);
//					}
//				}
//			}
//			if(lastClicked instanceof CityMarker){
//				//loop over eqmarkers to determine all eq who contains in its 
//				//threatcircle this city
//				for (Marker quakeMarker : quakeMarkers) {
//					double quakeThreatCircle = ((EarthquakeMarker) quakeMarker).threatCircle();
//					double dist = quakeMarker.getDistanceTo(clickedMarkerLocation);
//					if(dist > quakeThreatCircle){
//						quakeMarker.setHidden(true);
//					}else{quakeMarker.setHidden(false);}
//				}
//				//Also we have to not show all the cities except the selected one
//				for (Marker cityMarker : cityMarkers) {
//						cityMarker.setHidden(true);
//				}
//				lastSelected.setHidden(false);
//			}
//		}
//		}
//	}
	
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	private void deselectMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setSelected(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setSelected(false);
		}
	}

	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);
			
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.	
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}
	
}
