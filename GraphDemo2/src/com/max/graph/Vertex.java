package com.max.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Vertex {
	private Point2D.Double p;
	ArrayList<Edge> edges;
	boolean visited;

	public Vertex(double x, double y){
		p=new Point2D.Double(x,y);
		this.edges = new ArrayList<Edge>();
		this.visited = false; 
	}

	public Point2D.Double getP() {
		return p;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setP(double x, double y) {
		p=new Point2D.Double(x,y);
	}
	
}
