package com.max.graph;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
	private List<Vertex> vertexs;
	private List<Edge> edges;
	private String fileName;
	private HashMap<Point2D.Double, Vertex> map;

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
		edges.add(edge1);
		Edge edge2 = new Edge(destination,source , weight);
		edge2.setPheromoneLevel(0.2);
		edges.add(edge2);
	}

	// For the undirected graph get the adjacents whenever its from source or
	// destination
	public List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighborNodes = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)) {
				neighborNodes.add(edge.getDestination());
			}
//			if (edge.getDestination().equals(node)) {
//				neighborNodes.add(edge.getSource());
//			}
		}
		return neighborNodes;
	}

}
