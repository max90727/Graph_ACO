package com.max.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.max.gui.GUI;

public class ACO {
	//Graph informations
	Graph graph;
	static List<Vertex> vertexs;
	static List<Edge> edges;
	
	//ACO parameters
	Ant []ants;
	int antnumber;
	int citycount;
	int bestLength;
	//Draw lines on GUI
	JPanel canvas;
	public ACO(Graph graph,JPanel canvas) {
		this.graph = graph;
		vertexs=graph.getVertexs();
		edges=graph.getEdges();
		citycount=vertexs.size();
		this.canvas=canvas;
	}
	
	public void init(int antnum) {
		// TODO Auto-generated method stub
		//citycount=vertexs.size();
		antnumber=antnum;
		ants = new Ant[antnumber];
		for(int i=0;i<antnumber;i++){
            ants[i]=new Ant();
        }
		bestLength=Integer.MAX_VALUE;
	}
	
	public void run(int maxgen){
		List<Vertex> bestTour=new ArrayList<Vertex>();
		for (int runtimes = 0; runtimes < maxgen; runtimes++) {
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
					bestTour = ants[i].tours;
					drawGui(bestTour);
				}
			}
			System.out.println("The "+(runtimes+1)+"th time."+"The best length is:" + bestLength);
			update();
		}
		printBestTour(bestTour);
	}
	
	public void drawGui(List<Vertex> bestTour) {
		// TODO Auto-generated method stub
		int scale = 6;
		Graphics graphics= canvas.getGraphics();
		graphics.setColor(Color.black);
		 for (Vertex n : vertexs) {
	            for (Edge edge : n.getEdges()) {
	            	graphics.drawLine((int) (n.getP().x + 10)*scale, (int) (n.getP().y + 5) *scale, (int) (edge.getv2().getP().x + 10)*scale, (int) (edge.getv2().getP().y + 5)*scale);
	            }
	        }
		graphics.setColor(Color.red);
		for(int i=0;i<bestTour.size()-1;i++){
			graphics.drawLine((int) (bestTour.get(i).getP().x + 10)*scale, (int) (bestTour.get(i).getP().y + 5) *scale, (int) (bestTour.get(i+1).getP().x + 10)*scale, (int) (bestTour.get(i+1).getP().y + 5)*scale);
		}
	}

	private void printBestTour(List<Vertex> bestTour) {
		// TODO Auto-generated method stub
		System.out.println("The best tours are: ");
		for(Vertex v:bestTour){
			System.out.println("("+v.getP().x+","+v.getP().y+") ");
		}
		
	}

	public void update(){
		for(Edge e:edges){
			e.setPheromoneLevel(e.getPheromoneLevel()*(1-0.8));
		}
		for(int i=0;i<antnumber;i++){
			for(int j=0;j<vertexs.size();j++){
				ants[i].pathEdges.get(j).setPheromoneLevel(ants[i].pathEdges.get(j).getPheromoneLevel()+1.0/ants[i].tourlength);
			}
		}
	}
	
}
