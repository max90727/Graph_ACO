package com.max.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Ant {
	List<Vertex> tours;
	Set<Vertex> visited;
	int tourLength;
	Graph graph;
	List<Vertex> vertexs;
	List<Edge> edges;
	List<Edge> pathEdges;
	public int tourlength;

	public Ant(Graph graph) {
		this.graph = graph;
		this.vertexs = graph.getVertexs();
		this.edges = graph.getEdges();
		this.tours = new ArrayList<Vertex>();
		visited = new HashSet<Vertex>();
	}

	public void randomSelect(int num) {
		long r1 = System.currentTimeMillis();
		Random random = new Random(r1);
		int firstcity = random.nextInt(num);
		visited.add(vertexs.get(firstcity));
		tours.add(vertexs.get(firstcity));
	}

	public void selectNext(int index) {
		Vertex current = tours.get(index-1);
		List<Vertex> adjcents = graph.getNeighbors(current);
		double alpha = 1.0;
		double beta = 2.0;
		double sum = 0.0;
		double[] p;
		List<Edge> adjEdges = new ArrayList<Edge>();
		for (Edge e : edges) {
			if (e.getSource().equals(current)) {
				adjEdges.add(e);
			}
		}
		p = new double[adjcents.size()];
		for (int i = 0; i < adjcents.size(); i++) {
			if (!visited.contains(adjcents.get(i))) {
				sum += Math.pow(adjEdges.get(i).getPheromoneLevel(), alpha)
						* Math.pow(1.0 / adjEdges.get(i).getWeight(), beta);
			}
		}
		for (int i = 0; i < adjcents.size(); i++) {
			if (visited.contains(adjcents.get(i))) {
				p[i] = 0;
			} else {
				p[i] = (Math.pow(adjEdges.get(i).getPheromoneLevel(), alpha) * Math
						.pow(1.0 / adjEdges.get(i).getWeight(), beta)) / sum;
			}
		}
		long r1 = System.currentTimeMillis();
		Random rnd = new Random(r1);
		double selectp = rnd.nextDouble();
		double sumselect = 0;
		int selectcity = -1;
		for (int i = 0; i < adjcents.size(); i++) {
			sumselect += p[i];
			if (sumselect >= selectp) {
				selectcity = i;
				break;
			}
		}
		if (selectcity == -1)
			System.out.println();
		tours.add(adjcents.get(selectcity));
		visited.add(adjcents.get(selectcity));
		for(Edge e:edges){
			if(e.getSource().equals(current) && e.getDestination().equals(adjcents.get(selectcity))){
				pathEdges.add(e);
				break;
			}
		}
	}

	public void calTourLength() {
		for(Edge e:pathEdges){
			tourlength+=e.getWeight();
		}
	}
}
