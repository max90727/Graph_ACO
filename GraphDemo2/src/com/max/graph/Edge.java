package com.max.graph;

public class Edge {
	private Vertex source;
	private Vertex destination;
	private double weight;
	public Vertex getSource() {
		return source;
	}
	public Vertex getDestination() {
		return destination;
	}
	public double getWeight() {
		return weight;
	}
	public Edge(Vertex source, Vertex destination, double weight) {
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}
	
}
