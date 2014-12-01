package com.max.graph;

import java.awt.geom.Point2D;

public class Vertex {
	private Point2D.Double p;
	
	Vertex(double x, double y){
		p=new Point2D.Double(x,y);
	}

	public Point2D.Double getP() {
		return p;
	}

	

	
	
}
