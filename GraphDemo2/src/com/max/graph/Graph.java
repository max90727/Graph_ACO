package com.max.graph;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
	private List<Vertex> vertexs;
	private List<Edge> edges;
	private String fileName;
	private HashMap<Point2D.Double, Vertex> map;
	private Set<Edge> setEdges;

	public Graph(String fileName) {
		vertexs = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		this.map = new HashMap<Point2D.Double, Vertex>();
		this.fileName = fileName;
		createGraphByFile();
	}

	public Graph(List<Vertex> nodes, List<Edge> edges) {
		this.vertexs = nodes;
		this.edges = edges;
	}

	public List<Vertex> getVertexs() {
		return vertexs;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void createGraphByFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			while (line != null) {
				String[] split = line.split("\\s+");
				split[0] = split[0].substring(1, split[0].length() - 1);
				split[1] = split[1].substring(1, split[1].length() - 1);
				String[] p1 = split[0].split(",\\s*");
				String[] p2 = split[1].split(",\\s*");
				double weight = Double.parseDouble(split[2]);
				addLane(Double.parseDouble(p1[0]), Double.parseDouble(p1[1]),
						Double.parseDouble(p2[0]), Double.parseDouble(p2[1]),
						weight);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(fileName + " not found!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File Read Error!!!");
			e.printStackTrace();
		}

	}

	private void addLane(double sourceX, double sourceY, double destX,
			double destY, double weight) {
		// TODO Auto-generated method stub
		Point2D.Double p1 = new Point2D.Double(sourceX, sourceY);
		Point2D.Double p2 = new Point2D.Double(destX, destY);
		Vertex source;
		Vertex destination;
		if (!map.containsKey(p1)) {
			source = new Vertex(sourceX, sourceY);
			map.put(p1, source);
			vertexs.add(source);
		} else {
			source = map.get(p1);
		}
		if (!map.containsKey(p2)) {
			destination = new Vertex(destX, destY);
			map.put(p2, destination);
			vertexs.add(destination);
		} else {
			destination = map.get(p2);
		}
		Edge edge1 = new Edge(source, destination, weight);
		edge1.setPheromoneLevel(0.1);
		source.edges.add(edge1);
		edges.add(edge1);
		Edge edge2 = new Edge(destination, source, weight);
		edge2.setPheromoneLevel(0.1);
		edges.add(edge2);
		destination.edges.add(edge2);
	}

	public void addLane2(Vertex source, Vertex destination) {
		if(containEdges(source,destination)){
			Edge edge1 = new Edge(source, destination, distance(source.getP(),
					destination.getP()));
			edge1.setPheromoneLevel(0.5);
			source.edges.add(edge1);
			edges.add(edge1);
			setEdges.add(edge1);
		}
		
//		
//		Edge edge2 = new Edge(destination, source, distance(source.getP(),
//				destination.getP()));
//		edge2.setPheromoneLevel(0.1);
//		edges.add(edge2);
//		destination.edges.add(edge2);

	}
	private boolean containEdges(Vertex v1, Vertex v2){
		for(Edge e:edges){
			if(e.getv1().equals(v1) && e.getv2().equals(v2)){
				return false;
			}
			if(e.getv1().equals(v2) && e.getv2().equals(v1)){
				return false;
			}
		}
		return true;
	}
	public void createCompleteGraph() {
		edges.clear();
		setEdges = new HashSet<Edge>();
		for (Vertex v1 : vertexs) {
			for (Vertex v2 : vertexs) {
				if (!v1.equals(v2)) {
					// addLane(sourceX, sourceY, destX, destY, weight);
					addLane2(v1, v2);
				}
			}
		}
	}

	
	private static double distance(Point2D.Double p1, Point2D.Double p2) {
		double xDiff = p1.x - p2.x;
		double xSqr = Math.pow(xDiff, 2);

		double yDiff = p1.y - p1.y;
		double ySqr = Math.pow(yDiff, 2);

		double output = Math.sqrt(xSqr + ySqr);
		return output;
	}

}
