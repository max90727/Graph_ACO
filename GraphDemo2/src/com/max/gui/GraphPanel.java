package com.max.gui;

import javax.swing.*;

import com.max.graph.Edge;
import com.max.graph.Graph;
import com.max.graph.Vertex;

import java.awt.*;
import java.util.List;

/**
 * Created by tyr on 15/12/14.
 */
public class GraphPanel extends JPanel {
    private Graph graph;
    public int scale = 6;
    public int r = 6;
    public GraphPanel(Graph graph)

    {
        this.graph = graph;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(700, 650);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<Vertex> vertexs = graph.getVertexs();
        for (Vertex n : vertexs) {

            g.setColor(Color.BLUE);
            g.drawOval((int)(n.getP().x + 10)*scale - r/2, (int)(n.getP().y + 5)*scale - r/2, r, r);
            g.fillOval((int)(n.getP().x + 10)*scale - r/2, (int)(n.getP().y + 5)*scale - r/2, r, r);
            //g.drawLine((int) (n.getP().x)*scale - r, (int) (n.getP().y) *scale + r, (int)(n.getP().x) *scale + r, (int) (n.getP().y) *scale - r);
            //g.drawLine((int) (n.getP().x)*scale + r, (int) (n.getP().y) *scale + r, (int)(n.getP().x) *scale - r, (int) (n.getP().y) *scale - r);

        }
        g.setColor(Color.BLACK);
        for (Vertex n : vertexs) {
            for (Edge edge : n.getEdges()) {
                g.drawLine((int) (n.getP().x + 10)*scale, (int) (n.getP().y + 5) *scale, (int) (edge.getv2().getP().x + 10)*scale, (int) (edge.getv2().getP().y + 5)*scale);
            }
        }

        // todo: implement something to actually draw the graph here. oh, i guess it should update between iterations
        g.drawString("GRAPH HERE!", 10, 20);
    }
}

