package mycode;

import processing.core.PApplet;
import processing.core.PGraphics;

public class StartingWithPGraphics extends PApplet{

	
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PGraphics pg;
	int radius = 5;
	int centerX = 50;
	int centerY = 50;
	float diagCoeff = (float) Math.sqrt(2);

	public void setup() {
		size(200, 200);
		pg = createGraphics(40, 40);
	}

	public void draw() {
	  pg.beginDraw();
	  pg.background(100);
	  pg.stroke(255);
	  pg.line(centerX - diagCoeff*radius, centerY - diagCoeff*radius, centerX + diagCoeff*radius, centerY - diagCoeff*radius);
	  pg.line(centerX + diagCoeff*radius, centerY - diagCoeff*radius, centerX + diagCoeff*radius, centerY + diagCoeff*radius);
	  pg.line(centerX + diagCoeff*radius, centerY + diagCoeff*radius,centerX - diagCoeff*radius, centerY + diagCoeff*radius);
	  pg.line(centerX - diagCoeff*radius, centerY + diagCoeff*radius,centerX - diagCoeff*radius, centerY - diagCoeff*radius);
	  pg.endDraw();
	  image(pg, centerX+radius, centerY+radius); 
	}

	
}
