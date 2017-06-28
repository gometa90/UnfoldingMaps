package mycode;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class AirportsMap extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UnfoldingMap map;

	
	@Override
	public void setup() {

		//Setting the GUI window size
		size(800, 600, OPENGL);
		
		//SETTING UP THE MAP
		map = new UnfoldingMap(this, 150, 100, 600, 400, new Google.GoogleMapProvider());
		//event map dispatcher
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomToLevel(2);
		
		//Reading from airports.dat and adding markers to the map
		List<PointFeature> airportsPFeat = ParseFeed.parseAirports(this, "airports.dat");
		//For each airport Pointfeature, I´m going to create a Marker
		List<Marker> airportsMarkers = new ArrayList<Marker>();
		for (PointFeature pointFeat : airportsPFeat) {
			airportsMarkers.add(new SimplePointMarker(pointFeat.getLocation(), pointFeat.getProperties()));
		}
		//Add the list of markers to the map to be shown
		map.addMarkers(airportsMarkers);
	}
	
	@Override
	public void draw() {
		background(150);
		map.draw();
	}


	
	
	
	

}
