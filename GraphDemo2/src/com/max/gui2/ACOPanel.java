package com.max.gui2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.max.graph.ACO;
import com.max.graph.Edge;
import com.max.graph.Graph;
import com.max.graph.Vertex;

public class ACOPanel extends JPanel {
	/*--------------------------------------------------------------------*/

	private static final long serialVersionUID = 0x00010000L;

	/* --- instance variables --- */

	private double xoff, yoff; /* translation parameters */
	private double scale; /* scaling factor */
	private int[] xs, ys; /* screen coordinates */
	private Color[] cols; /* colors for trail */
	private Stroke thick; /* stroke for trail drawing */
	private Stroke thin; /* stroke for tour drawing */
	private Graph graph;
	private List<Edge> edges;
	private List<Vertex> vertexs;
	private ACO aco = null; /* The ant colony optimization */
	/*------------------------------------------------------------------*/

	public ACOPanel() { /* --- create an ACO panel */
		this.xoff = this.yoff = 0; /* initialize offset */
		this.scale = 64.0; /* and scale */
		this.cols = new Color[256];/* initialize the colors */
		for (int i = 256; --i >= 0;)
			this.cols[255 - i] = new Color(i / 255.0F, i / 255.0F, i / 255.0F);
		this.thick = new BasicStroke(7.0F);
		this.thin = new BasicStroke(2.0F);
	} /* ACOPanel() */

	public void setGraph(Graph graph) {
		this.graph = graph;
		this.edges = graph.getEdges();
		this.vertexs = graph.getVertexs();
		this.xoff = graph.getbbX();
		this.yoff = graph.getbbY(); /* set default offset */
		this.setScale(6.0); /* and default scale */
		this.revalidate(); /* adapt the enclosing scroll pane */
		this.repaint(); /* and redraw the TSP */
	}/* setGraph() */
	//
	public void initAnts(int antcnt, double phero) { 
		if (this.graph == null) {
			return;
		}
		this.aco = new ACO(this.graph, antcnt);
		this.aco.init(phero);
		this.repaint();

	} /* initAnts() */


	public void setParams(double alpha, double beta) {
		this.aco.setAlpha(alpha);
		this.aco.setBeta(beta);
	}/* setParames */

	public void setScale(double scale) {
		Dimension d; /* preferred size of panel */
		int w, h; /* size of background rectangle */
		this.scale = scale; /* set new scaling factor */
		d = new Dimension(); /* compute new preferred size */
		d.width = (int) (this.graph.getWidth() * scale + 16.5);
		d.height = (int) (this.graph.getHeight() * scale + 16.5);
		this.setPreferredSize(d); /* set new preferred size */
		w = 8;
		h = d.height - 8; /* get the coordinates of the origin */
		int n = vertexs.size();
		// scaleVertexs=new ArrayList<Vertex>(n);

		for (int i = 0; i < n; i++) {
			vertexs.get(i).setP(
					(w + scale * (vertexs.get(i).getP().x - this.xoff) + 0.5),
					(h - scale * (vertexs.get(i).getP().y - this.yoff) + 0.5));
		}
	}/* setScale */

	public void runAnts() {
		if (this.aco == null) {
			return;
		}
		this.aco.run();
		this.repaint();
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		int i, j, k, n; /* loop variables, number of vertices */
		Dimension d; /* (preferred) size of panel */
		int w, h; /* size of background rectangle */
		int x, y, ox, oy; /* coordinates of points */
		double trl, avg, max; /* (average/maximal) trail on edge */
		double scl; /* scaling factor */
		
		// /Edge e; /* to traverse the edges */

		d = this.getSize(); /* get the (preferred) panel size */
		w = d.width;
		h = d.height; /* (whichever is larger) */
		d = this.getPreferredSize();
		if (d.width > w)
			w = d.width;
		if (d.height > h)
			h = d.height;
		g.setColor(Color.white); /* set the background color */
		g.fillRect(0, 0, w, h); /* draw the background */
		if (this.graph == null) {
			return;
		}
		w = 8;
		h = d.height - 8;
		n = vertexs.size();

		/* draw the trail */
		if (this.aco != null) {
			for (k=0;k<edges.size();k++) {
				int color = (int) (255 * edges.get(k).getPheromoneLevel());
				if (color > 255) {
					color = 255;
				}
				edges.get(k).setColor(color);
			}
			((Graphics2D) g).setStroke(this.thick);
			for (k=0;k<edges.size();k++) {
				g.setColor(this.cols[edges.get(k).getColor()]);
				g.drawLine((int) edges.get(k).getv1().getP().x, (int) edges.get(k).getv1().getP().y,
						(int) edges.get(k).getv2().getP().x, (int) edges.get(k).getv2().getP().y);
			}
			
			((Graphics2D) g).setStroke(this.thin);
			g.setColor(Color.blue); /* draw the best tour in red */
			List<Vertex> bestTours;
			
			bestTours = this.aco.getBestTour();
			if(bestTours!=null){
				for (i = 0; i < bestTours.size()-1; i++) {
					g.drawLine((int) (bestTours.get(i).getP().x), (int) (bestTours.get(i)
							.getP().y), (int) (bestTours.get(i + 1).getP().x),
							(int) (bestTours.get(i + 1).getP().y));
				}
			}
			
		}
		/* --- draw the vertices --- */
		for (i = n; --i >= 0;) { /* traverse the vertices */
			g.setColor(Color.black); /* black outline */
			g.fillOval((int) vertexs.get(i).getP().getX() - 4, (int) vertexs
					.get(i).getP().getY() - 4, 9, 9);
			g.setColor(Color.red); /* red interior */
			g.fillOval((int) vertexs.get(i).getP().getX() - 3, (int) vertexs
					.get(i).getP().getY() - 3, 7, 7);
		} /* draw a circle */
	}

	public BufferedImage makeImage() { /* --- create an image of contents */
		BufferedImage img; /* created image */
		Dimension d; /* size of panel */

		d = this.getPreferredSize();
		img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_3BYTE_BGR);
		this.paint(img.getGraphics());
		return img; /* draw window contents to image */
	} /* BufferedImage() *//* and return the image */

	public ACO getAco() {
		return aco;
	}

} /* class ACOPanel */
