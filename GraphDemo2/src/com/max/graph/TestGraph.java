package com.max.graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.max.hamiliton.Hamilitonian;

public class TestGraph {
	public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
		if (n > (max - min + 1) || max < min) {
			return;
		}
		for (int i = 0; i < n; i++) {
			int num = (int) (Math.random() * (max - min)) + min;
			set.add(num);
		}
		int setSize = set.size();
		if (setSize < n) {
			randomSet(min, max, n - setSize, set);
		}
	}

	@Test
	public void test() throws Exception {
		String fileName = "res/d1.txt";
		Graph graph = new Graph(fileName);
		
		graph.createCompleteGraph();
		List<Edge> edges=graph.getEdges();
		System.out.println("Number of edges:"+edges.size());
		ACO aco=new ACO(graph);
		aco.init(50);
		aco.run(2000);
	}

}
