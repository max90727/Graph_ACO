package com.max.graph;

import java.util.List;

public class ACO {
	//Graph informations
	Graph graph;
	static List<Vertex> vertexs;
	static List<Edge> edges;
	
	//ACO parameters
	Ant []ants;
	int antnumber;
	List<Vertex> bestTour;
	int bestLength;
	public ACO(Graph graph) {
		this.graph = graph;
		vertexs=graph.getVertexs();
		edges=graph.getEdges();
	}
	
	public void init(int antnum) {
		// TODO Auto-generated method stub
		int citycount=vertexs.size();
		antnumber=antnum;
		ants = new Ant[antnumber];
		for(int i=0;i<antnumber;i++){
            ants[i]=new Ant();
            ants[i].randomSelect(citycount);
        }
		bestLength=Integer.MAX_VALUE;
	}
	
	public void run(int maxgen){
		int citycount=vertexs.size();
		for (int runtimes = 0; runtimes < maxgen; runtimes++) {
			for (int i = 0; i < antnumber; i++) {
				for (int j = 1; j < citycount; j++) {
					ants[i].selectNext(j);
				}
				ants[i].calTourLength();
				if (ants[i].tourlength < bestLength) {
					bestLength = ants[i].tourlength;
					System.out.println(runtimes+" find best length:" + bestLength);
					bestTour = ants[i].tours;
				}
			}
			update();
			for (int i = 0; i < antnumber; i++) {
				ants[i].randomSelect(citycount);	
			}
		}
	}
	
	public void update(){
		for(Edge e:edges){
			e.setPheromoneLevel(e.getPheromoneLevel()*(1-0.5));
		}
		for(int i=0;i<antnumber;i++){
			for(int j=0;j<vertexs.size();j++){
				ants[i].pathEdges.get(j).setPheromoneLevel(ants[i].pathEdges.get(j).getPheromoneLevel()+1.0/ants[i].tourlength);
			}
		}
	}
	
}
