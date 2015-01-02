package com.max.hamiliton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.max.graph.Edge;
import com.max.graph.Graph;
import com.max.graph.Vertex;

public class Hamilitonian {

	Graph graph;
	int num_verteces, pathCount = 0;
	List<Vertex> tours;
	List<Vertex> verteces;
	List<Edge> edges;
	private Set<Vertex> visited;

	public Hamilitonian(Graph graph) {
		this.graph = graph;
		this.verteces = graph.getVertexs();
		this.edges = graph.getEdges();
		this.tours = new ArrayList<Vertex>();
		this.visited = new HashSet<Vertex>();
	}

	public boolean findHamiltonianCycle() {
		num_verteces = verteces.size();
		// Random select first vertex
		long r1 = System.currentTimeMillis();
		Random random = new Random(r1);
		int firstcity = random.nextInt(num_verteces);
		visited.add(verteces.get(firstcity));
		tours.add(verteces.get(firstcity));
		if (solveHamiltonianCycle(0)) {
			return true;
		} else {
			System.out.println("No solution!! Visited " + tours.size()
					+ " verteces");

			return false;
		}

	}
	
	public boolean solveHamiltonianCycle(int select) {
		Vertex current = tours.get(pathCount);
		while (tours.size()==num_verteces) {
			if (findEdge(tours.get(tours.size() - 1), tours.get(0))
					&& pathCount == num_verteces - 1) {
				System.out.println("Found Solution!!");
				return true;
			}
			if (pathCount == num_verteces - 1 || pathCount < 0) {
				return false;
			}
			List<Vertex> adjcents = new ArrayList<Vertex>();
			List<Edge> adjEdges = current.getEdges();
			for (int i = 0; i < adjEdges.size(); i++) {
				adjcents.add(adjEdges.get(i).getv2());
			}
			double alpha = 1.0;
			double beta = 2.0;
			double sum = 0.0;
			double[] p;
			p = new double[adjcents.size()];
			// Ant select city by probability
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
					p[i] = (Math
							.pow(adjEdges.get(i).getPheromoneLevel(), alpha) * Math
							.pow(1.0 / adjEdges.get(i).getWeight(), beta))
							/ sum;
				}
			}
			Random random = new Random(System.currentTimeMillis());
			float sleectP = random.nextFloat();
			int selectcity = -1;
			float sum1 = 0.f;
			for (int i = 0; i < adjcents.size(); i++) {
				sum1 += p[i];
				if (sum1 >= sleectP) {
					selectcity = i;
					break;
				}
			}
			if (selectcity == -1) {
				for (Edge e : tours.get(pathCount).getEdges()) {
					if (visited.contains(e.getv1())) {
						visited.remove(e.getv1());
						tours.remove(e.getv1());
						pathCount--;
					}
					if (visited.contains(e.getv2())) {
						visited.remove(e.getv2());
						tours.remove(e.getv2());
						pathCount--;
					}

				}
				// restore connections
				if (pathCount > 0) {
					visited.remove(tours.get(pathCount));
					tours.remove(pathCount);
					pathCount--;
					current = tours.get(pathCount);
					
				}else {
					return false;
				}

			} else {
				pathCount++;
				tours.add(adjcents.get(selectcity));
				visited.add(adjcents.get(selectcity));
				current=adjcents.get(selectcity);
			}
			System.out.println(pathCount);
		}

		return false;
	}

	public boolean findEdge(Vertex source, Vertex destination) {
		// Vertex current = tours.get(pathCount);
		List<Vertex> adjcents = new ArrayList<Vertex>();
		List<Edge> adjEdges = source.getEdges();
		for (int i = 0; i < adjEdges.size(); i++) {
			adjcents.add(adjEdges.get(i).getv2());
		}
		if (adjcents.contains(destination)) {
			return true;
		}
		return false;
	}

	public List<Vertex> getTours() {
		return tours;
	}

}
