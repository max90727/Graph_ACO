package com.max.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private List<Vertex> nodes;
	private List<Edge> edges;
	public Graph(){
		nodes=new ArrayList<Vertex>();
		edges=new ArrayList<Edge>();
	}
	public Graph(List<Vertex> nodes, List<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}
	public List<Vertex> getVertexs() {
		return nodes;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	//For the undirected graph get the adjacents whenever its from source or destination
	public List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighborNodes = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)) {
				neighborNodes.add(edge.getDestination());
			}
			if (edge.getDestination().equals(node)) {
				neighborNodes.add(edge.getSource());
			}
		}
		return neighborNodes;
	}
	
}
