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
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CreateGraph {

	Graph graph;
	private HashMap<Point2D.Double, Vertex> map;// The hash map for finding the
												// duplicate the

	public CreateGraph(Graph graph) {
		this.graph = graph;
		this.map = new HashMap<Point2D.Double, Vertex>();
	}

	public boolean createGraphByFile(String fileName) {
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
						Double.parseDouble(p2[0]),
						Double.parseDouble(p2[1]), weight);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(fileName + " not found!!!");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File Read Error!!!");
			e.printStackTrace();
		}

		return true;
	}

	public void addLane(double sourceX, double sourceY, double destX,
			double destY, double weight) {
		// TODO Auto-generated method stub
		Point2D.Double p1 = new Point2D.Double(sourceX, sourceY);
		Point2D.Double p2 = new Point2D.Double(destX, destY);
		Vertex source;
		Vertex destination;
		if (!map.containsKey(p1)) {
			source = new Vertex(sourceX, sourceY);
			map.put(p1, source);
			graph.getVertexs().add(source);
		} else {
			source = map.get(p1);
		}
		if (!map.containsKey(p2)) {
			destination = new Vertex(destX, destY);
			map.put(p2, destination);
			graph.getVertexs().add(destination);
		} else {
			destination = map.get(p2);
		}
		Edge edge = new Edge(source, destination, weight);
		graph.getEdges().add(edge);
	}

//	public List<Vertex> BFS(int x, int y) {
//		Point rootPoint = new Point(x, y);
//		Vertex rootNode = map.get(rootPoint);
//		if (rootNode == null) {
//			return null;
//		}
//		return BFS(rootNode);
//	}
	
	public List<Vertex> BFS(Vertex root) {
		// TODO Auto-generated method stub
		List<Vertex> bfsResult = new ArrayList<Vertex>();
		Set<Vertex> visited = new HashSet<Vertex>();
		Queue<Vertex> q = new ConcurrentLinkedQueue<Vertex>();
		q.add(root);
		while (q.size() > 0) {
			Vertex n = q.poll();
			if (!visited.contains(n)) {
				visited.add(n);
				bfsResult.add(n);
				List<Vertex> neighbors = graph.getNeighbors(n);
				for (int i = 0; i < neighbors.size(); i++) {
					q.add(neighbors.get(i));

				}
			}
		}
		return bfsResult;
	}

}
