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
		Queue<Vertex> q = new ConcurrentLinkedQueue<Vertex>();
		q.add(root);
		while (q.size() > 0) {
			Vertex n = q.poll();
			if (!n.visited) {
				bfsResult.add(n);
				n.visited=true;
				List<Edge> adjEdges = n.edges;
				for (int i = 0; i < adjEdges.size(); i++) {
					q.add(adjEdges.get(i).getv2());
				}
			}
		}
		return bfsResult;
	}
	
}
