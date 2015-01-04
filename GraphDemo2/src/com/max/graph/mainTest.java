package com.max.graph;

import java.util.List;

public class mainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "res/c1.txt";
		Graph graph = new Graph(fileName);
		
		graph.createCompleteGraph();
		List<Edge> edges=graph.getEdges();
		System.out.println("Number of edges:"+edges.size());
//		GUI gui=new GUI(graph);
		ACO aco=new ACO(graph,30);
		aco.init(50);
		for(int i=0;i<100;i++){
			aco.run();
			System.out.println("Epoch:"+i+" Best:"+aco.getBestLength());
			List<Vertex> bestTours=aco.bestTour;
			for(Vertex v:bestTours){
				System.out.println(v.getP().x+", "+v.getP().y);
			}
			System.out.println();
		}
		
	}

}
