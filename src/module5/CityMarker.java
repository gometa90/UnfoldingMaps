package module5;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author M. Alejandro Gómez Carmona
 *
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */
//	public void draw(PGraphics pg, float x, float y) {
//		// Save previous drawing style
//		pg.pushStyle();
//		
//		// IMPLEMENT: drawing triangle for each city
//		pg.fill(150, 30, 30);
//		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
//		
//		//showTitle
//		showTitle(pg,x, y);
//		
//		// Restore previous drawing style
//		pg.popStyle();
//		
//
//	}
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		if(this.isSelected()){
			//Obtain the city features
			String cityName = "City: "+this.getStringProperty("name");
			String cityCountry = " Country: "+this.getStringProperty("country");
			String cityPopulation = " Population: "+this.getStringProperty("population");
			//Draw the square with the information about the city
			pg.fill(250, 250, 76);;
			pg.rect(x, y, 120, 40);
			pg.fill(0,0,0);
			pg.textSize(9);
			pg.text(cityName, x+5, y+10);
			pg.text(cityCountry, x+5, y+20);
			pg.text(cityPopulation, x+5, y+30);
		}
	}
	
	
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}


	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		//showTitle
		showTitle(pg,x, y);
		
		// Restore previous drawing style
		pg.popStyle();
	}


	@Override
	public void drawLines(PGraphics pg, List<Marker> threatened, UnfoldingMap map ) {
		if(this.isSelected()){
		//Position on the canvas of this marker
			ScreenPosition sp1 = this.getScreenPosition(map);
			Location thisLocation = this.getLocation();
			for (Marker threatenedMarker : threatened) {
				double threatCircle = ((EarthquakeMarker)threatenedMarker).threatCircle();
				double distance = threatenedMarker.getDistanceTo(thisLocation);
				if(distance < threatCircle){
					ScreenPosition sp2 = ((EarthquakeMarker)threatenedMarker).getScreenPosition(map);
					pg.line(sp1.x, sp1.y, sp2.x, sp2.y);
				}
			}
		}
	}
}
