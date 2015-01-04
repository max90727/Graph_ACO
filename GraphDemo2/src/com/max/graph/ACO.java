package com.max.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ACO {
	// Graph informations
	Graph graph;
	static List<Vertex> vertexs;
	static List<Edge> edges;

	// ACO parameters
	Ant[] ants;
	private int antnumber;
	private int citycount;
	private int bestLength;
	private int epoch;
	List<Vertex> bestTour;
	// Draw lines on GUI
	JPanel canvas;
	private double alpha;
	private double beta;

	public ACO(Graph graph, JPanel canvas, double alpha, double beta,
			int antnumber) {
		this.graph = graph;
		vertexs = graph.getVertexs();
		edges = graph.getEdges();
		citycount = vertexs.size();
		this.canvas = canvas;
		this.antnumber = antnumber;
		//this.bestTour = new ArrayList<Vertex>();
		this.epoch = 0;
	}

	public ACO(Graph graph, int antnumber) {
		this.graph = graph;
		vertexs = graph.getVertexs();
		edges = graph.getEdges();
		citycount = vertexs.size();
		this.antnumber = antnumber;
		this.epoch = 0;
	}

	public void init(double pher) {
		// TODO Auto-generated method stub
		for (Edge e : edges) {
			e.setPheromoneLevel(pher);
		}
		ants = new Ant[antnumber];
		for (int i = 0; i < antnumber; i++) {
			ants[i] = new Ant(alpha, beta);
//			ants[i].randomSelect(citycount);
		}
		bestLength = Integer.MAX_VALUE;
		bestTour = new ArrayList<Vertex>(citycount+1);
	}

	public void run() {
		// for (int runtimes = 0; runtimes < maxgen; runtimes++) {
		for (int i = 0; i < antnumber; i++) {
			ants[i].randomSelect(citycount);
		}
		for (int i = 0; i < antnumber; i++) {
			for (int j = 1; j < citycount; j++) {
				ants[i].selectNext(j);
			}
			ants[i].calTourLength();
			if (ants[i].tourlength < bestLength) {
				bestLength = ants[i].tourlength;
				bestTour = new ArrayList<Vertex>();
				for(int k=0;k<ants[i].tours.size();k++){
					bestTour.add(ants[i].tours.get(k));
				}
				//bestTour = ants[i].tours;
			}
		}
		update();
		
		epoch++;
		// }
	}

	public void update() {
		for (Edge e : edges) {
			e.setPheromoneLevel(e.getPheromoneLevel() * (1 - 0.5));
		}
		for (int i = 0; i < antnumber; i++) {
			for (int j = 0; j < vertexs.size(); j++) {
				ants[i].pathEdges.get(j).setPheromoneLevel(
						ants[i].pathEdges.get(j).getPheromoneLevel() + 1.0
								/ ants[i].tourlength);
			}
		}
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public List<Vertex> getBestTour() {
		return bestTour;
	}

	public int getBestLength() {
		return bestLength;
	}

	public int getEpoch() {
		return epoch;
	}

}
