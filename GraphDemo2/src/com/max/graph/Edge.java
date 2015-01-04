package com.max.graph;

public class Edge {
	private Vertex v1;
	private Vertex v2;
	private double weight;
	private double pheromoneLevel;
	private int color;
	public Vertex getv1() {
		return v1;
	}

	public Vertex getv2() {
		return v2;
	}

	public double getWeight() {
		return weight;
	}
	
	public double getPheromoneLevel() {
		return pheromoneLevel;
	}
	
	public void setPheromoneLevel(double pheromoneLevel) {
		this.pheromoneLevel = pheromoneLevel;
	}
	public Edge(Vertex v1, Vertex v2, double weight) {
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
}
