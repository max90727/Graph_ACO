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

public class GraphDao{
	Graph graph;
	public GraphDao(Graph graph) {
		this.graph = graph;
		
	}
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
