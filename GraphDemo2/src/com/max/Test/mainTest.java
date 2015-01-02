package com.max.Test;

import java.util.List;

import com.max.graph.ACO;
import com.max.graph.Edge;
import com.max.graph.Graph;
import com.max.gui.GUI;

public class mainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileName = "res/b1.txt";
		Graph graph = new Graph(fileName);
		
		graph.createCompleteGraph();
		List<Edge> edges=graph.getEdges();
		System.out.println("Number of edges:"+edges.size());
		GUI gui=new GUI(graph);
//		ACO aco=new ACO(graph);
//		aco.init(50);
//		aco.run(2000);
	}

}
