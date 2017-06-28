package guimodule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;


public class MyApplet extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PImage backGroundImage ;
	//private String URL = "http://www.lajollabluebook.com/blog/wp-content/uploads/2014/07/0361115306.jpg";
	private String filename = "palmTrees.jpg";
	
	
	public void setup() {
		
		size(400, 400);
		//Add the background Image
		backGroundImage = loadImage(filename, "jpg");
	}
	
	public void draw() {
		backGroundImage.resize(0, height);
		image(backGroundImage, 0, 0);
		
		//Select the colour of the shape
		HashMap<String, Integer> shapeColour = sunBrightness();
		fill(shapeColour.get("Red"),shapeColour.get("Green"),0);
		//We now want to draw a sun centered at (50,50) coordinates
		ellipse(width/4, height/5, width/5, height/5);
		//And we want to draw it yellow not white,for now, then we are going to make the
		//color change dynamically depending on the hour. (fill method above)
		
		
	}
	
	public HashMap<String, Integer> sunBrightness(){
		//The sun colour and brightness is going to change 
		//depending on the seconds of this minute
		int incremRed =(int) 255/60;
		int incremGreen = (int) 209/60;
		
		
		int redValue = incremRed * (getSeconds()+1);
		int greenValue = incremGreen * (getSeconds()+1);
		
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		result.put("Red", redValue);
		result.put("Green", greenValue);
		return result;
	}

	public int getSeconds(){
		//Obtaining the current time
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String dateS = dateFormat.format(date); //2016/11/16 12:08:43
				int seconds = Integer.parseInt(dateS.substring(dateS.length()-2));
				System.out.println("Seconds of the current time are: "+seconds);
				return seconds;
	}


	
}
