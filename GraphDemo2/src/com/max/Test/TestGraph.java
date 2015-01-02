package com.max.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.max.graph.ACO;
import com.max.graph.Edge;
import com.max.graph.Graph;
import com.max.gui.GUI;
import com.max.hamiliton.Hamilitonian;

public class TestGraph {
	@Test
	public void test() throws Exception {
		String fileName = "res/a1.txt";
		Graph graph = new Graph(fileName);
		
		graph.createCompleteGraph();
		List<Edge> edges=graph.getEdges();
		System.out.println("Number of edges:"+edges.size());
		GUI gui=new GUI(graph);
//		ACO aco=new ACO(graph);
//		aco.init(50);
//		aco.run(2000);
	}

	@Test
	public void testGui() {
		String fileName = "res/a1.txt";
		Graph graph = new Graph(fileName);
		GUI gui=new GUI(graph);
	}
}
