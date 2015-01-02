package com.max.gui;

import javax.swing.*;

import com.max.graph.ACO;
import com.max.graph.Graph;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tyr on 15/12/14.
 */
public class GUI extends JFrame {
    private Graph graph;
    private int x = 800, y = 600;
    String[] decayTypes = {"Polynomial", "Linear"};
    JPanel canvas;
    JPanel settings;
    JButton start;
    JButton stop;
    JLabel alphaLabel;
    JTextField alphaInput;
    JLabel betaLabel;
    JTextField betaInput;
    JComboBox<String> decayType;
    private JLabel decayTypeLabel;
    private JLabel itLabel;
    private JTextField itInput;

    public GUI(Graph graph) {
        this.graph = graph;
        this.setSize(x, y);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        makeComponents();
        this.setLocationRelativeTo(null);
    }

    private void makeComponents() {
        //settings panel
        settings = new JPanel();
        /*
        just hacked rather quickly.
        Refer to http://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        if you wanna make it... I dunno, prettier?
        */
        settings.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        itLabel = new JLabel("Iterations: ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        settings.add(itLabel, constraints);
        itInput = new JTextField("   ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        settings.add(itInput, constraints);
        alphaLabel = new JLabel("Alpha: ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        settings.add(alphaLabel, constraints);
        alphaInput = new JTextField("   ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        settings.add(alphaInput, constraints);
        betaLabel = new JLabel("Beta: ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        settings.add(betaLabel, constraints);
        betaInput = new JTextField("    ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 2;
        settings.add(betaInput, constraints);
        decayTypeLabel = new JLabel("Choose decay type: ");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 3;
        settings.add(decayTypeLabel, constraints);
        decayType = new JComboBox<String>(decayTypes);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 3;
        settings.add(decayType, constraints);
        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* basically paste contents of main here
                 * So, uh, something like this:
                 * acoAlg = new engine.AcoAlg(--parameters from gui here-- )
                 * acoAlg.solve()
                 * and...that's it
                */
            	ACO aco=new ACO(graph,canvas);
        		aco.init(50);
        		aco.run(2000);
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.PAGE_END;
        settings.add(start, constraints);
        stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /* interesting. we can implement something like isRunning,
                 * and check it in a loop
                 * erm, do we even need it?
                 */
                System.out.println("Stop pressed");
            }
        });
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.PAGE_END;
        settings.add(stop, constraints);

        canvas = new GraphPanel(graph);
        this.add(settings, BorderLayout.WEST);
        this.add(canvas, BorderLayout.EAST);
        this.pack();
    }
}
