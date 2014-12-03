package com.max.graph;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TestGraph {

	@Test
	public void test() {
		String fileName="a1.txt";
		Graph graph=new Graph(fileName);
		GraphDao cg=new GraphDao(graph);
		List<Vertex> vertexs=graph.getVertexs();
		List<Vertex> bfs=cg.BFS(vertexs.get(1));
		for(Vertex n:bfs){
			System.out.println("("+n.getP().x+", "+n.getP().y+")");
		}
	}

}
