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
	List<Vertex> tours;//
	List<Edge> pathEdges;
	int tourlength;
	private Set<Vertex> visited;
	//private Queue<Vertex> visiteQueue;
	public Ant() {
		this.tours = new ArrayList<Vertex>();
		this.visited = new HashSet<Vertex>();
		this.pathEdges = new ArrayList<Edge>();
	}

	public void randomSelect(int num) {
		tourlength=0;
		visited.clear();
		tours.clear();
		pathEdges.clear();
		long r1 = System.currentTimeMillis();
		Random random = new Random(r1);
		int firstcity = random.nextInt(num);
		visited.add(ACO.vertexs.get(firstcity));
		tours.add(ACO.vertexs.get(firstcity));
	}

	public void selectNext(int index) {
		Vertex current= tours.get(index-1);
		List<Vertex> adjcents=new ArrayList<Vertex>();
		double alpha = 1.0;
		double beta = 5.0;
		double sum = 0.0;
		double[] p;
		List<Edge> adjEdges = findAdjecent(current);
		for (int i = 0; i < adjEdges.size(); i++) {
			adjcents.add(adjEdges.get(i).getv2()==current?adjEdges.get(i).getv1():adjEdges.get(i).getv2());
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
		if (selectcity == -1){
			//Do something here!!!!
			System.out.println("");    
			
		}
		tours.add(adjcents.get(selectcity));
		visited.add(adjcents.get(selectcity));
		
		connectEdges(current, adjcents.get(selectcity));
		//Connect to the first vertext
		if(index==ACO.vertexs.size()-1){
			connectEdges(adjcents.get(selectcity), tours.get(0));
		}
	}
	public void connectEdges(Vertex v1,Vertex v2){
		for(Edge e:ACO.edges){
			if(e.getv1().equals(v1) && e.getv2().equals(v2)){
				pathEdges.add(e);
				break;
			}
			if(e.getv1().equals(v2) && e.getv2().equals(v1)){
				pathEdges.add(e);
				break;
			}
		}
	}
	public void calTourLength() {
		tourlength=0;
		for(Edge e:pathEdges){
			tourlength+=e.getWeight();
		}
	}
	public void refesh(){
		tours.clear();
		pathEdges.clear();
		visited.clear();
	}
	public List<Edge> findAdjecent(Vertex v){
		List<Edge> adjEdges=new ArrayList<Edge>();
		for (Edge edge : ACO.edges) {
			if (edge.getv1().equals(v) || edge.getv2().equals(v)) {
				//vertexs.add(edge.getv2());
				adjEdges.add(edge);
			}

		}
		return adjEdges;
	}
}
