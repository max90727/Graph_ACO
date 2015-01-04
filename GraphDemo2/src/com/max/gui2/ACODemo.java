package com.max.gui2;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

import com.max.graph.ACO;
import com.max.graph.Graph;

public class ACODemo extends JFrame implements Runnable {
	/*--------------------------------------------------------------------*/

	private static final long serialVersionUID = 0x00010005L;
	public static final String VERSION = "1.5 (2014.10.23)";

	private static final Font font = new Font("Dialog", Font.BOLD, 12);
	private static final Font small = new Font("Dialog", Font.PLAIN, 10);

	private boolean isprog = false; /* whether run as a program */
	private JScrollPane scroll = null; /* scroll pane viewport */
	private ACOPanel panel = null; /* ACO viewer panel */
	private JTextField stat = null; /* status bar for messages */
	private JDialog randtsp = null; /* TSP generation dialog box */
	private JDialog antcol = null; /* ant colony dialog box */
	private JDialog runopt = null; /* run opt. dialog box */
	private JDialog params = null; /* parameter dialog box */
	private JDialog about = null; /* "About..." dialog box */
	private JFileChooser chooser = null; /* a file chooser */
	private File curr = null; /* current TSP file */

	private Timer timer = null; /* timer for repeated update */
	private int cnt = -1; /* counter for epochs */
	private double scale, factor; /* buffer for scaling factor */
	private Graph graph = null; /* Create graph by loading the file */

	/*------------------------------------------------------------------*/

	private JFileChooser createChooser() { /* --- create a file chooser */
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileHidingEnabled(true);
		fc.setAcceptAllFileFilterUsed(true);
		fc.setMultiSelectionEnabled(false);
		fc.setFileView(null); /* create a standard file chooser */
		return fc; /* without customized filters */
	} /* createChooser() */

	/*------------------------------------------------------------------*/

	public void loadTSP(File file) { /* --- load a traveling salesman p. */
		if (file == null) { /* if no file name is given */
			if (chooser == null)
				chooser = createChooser();
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			int r = chooser.showDialog(this, null);
			if (r != JFileChooser.APPROVE_OPTION)
				return;
			file = chooser.getSelectedFile();
		} /* get the selected file */
		try {
			curr = chooser.getSelectedFile();
			this.graph = new Graph(new FileReader(curr));

			this.panel.setGraph(this.graph);
		} catch (Exception e) {
			String msg = e.getMessage();
			this.stat.setText(msg);
			System.err.println(msg);
			JOptionPane.showMessageDialog(this, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		this.curr = file; /* note the new file name */
	} /* loadTSP() */

	/*------------------------------------------------------------------*/

	public void saveTSP(File file) { /* --- save a traveling salesman p. */
		if (file == null) { /* if no file name is given */
			if (chooser == null)
				chooser = createChooser();
			chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			int r = chooser.showDialog(this, null);
			if (r != JFileChooser.APPROVE_OPTION)
				return;
			file = chooser.getSelectedFile();
		} /* get the selected file */
		// try { /* save the current TSP */
		// FileWriter writer = new FileWriter(file);
		// writer.write(this.tsp.toString());
		// writer.close(); }
		// catch (IOException e) {
		// String msg = e.getMessage();
		// this.stat.setText(msg); System.err.println(msg);
		// JOptionPane.showMessageDialog(this, msg,
		// "Error", JOptionPane.ERROR_MESSAGE);
		// } /* set the status text */
		this.curr = file; /* note the new file name */
	} /* saveTSP() */

	/*------------------------------------------------------------------*/

	public void saveImage(File file) { /* --- save image to a file */
		if (file == null) { /* if no file name is given */
			if (this.chooser == null)
				this.chooser = this.createChooser();
			this.chooser.setDialogType(JFileChooser.SAVE_DIALOG);
			int r = this.chooser.showDialog(this, null);
			if (r != JFileChooser.APPROVE_OPTION)
				return;
			file = this.chooser.getSelectedFile();
		} /* let the user choose a file name */
		try { /* open an output stream */
			FileOutputStream stream = new FileOutputStream(file);
			ImageIO.write(this.panel.makeImage(), "png", stream);
			stream.close();
		} /* save the decision tree image */
		catch (IOException e) { /* catch and report i/o errors */
			String msg = e.getMessage();
			stat.setText(msg);
			System.err.println(msg);
			JOptionPane.showMessageDialog(this, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
		} /* set the status text */
	} /* saveImage() */

	/*------------------------------------------------------------------*/

	private JDialog createAnts() { /* --- create ant colony dialog */
		final JDialog dlg = new JDialog(this, "Create Ant Colony...");
		GridBagLayout g = new GridBagLayout();
		GridBagConstraints lc = new GridBagConstraints();
		GridBagConstraints rc = new GridBagConstraints();
		JPanel grid = new JPanel(g);
		JPanel bbar;
		JLabel lbl;
		JTextArea help;
		JButton btn;

		grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lc.fill = /* fill fields in both directions */
		rc.fill = GridBagConstraints.BOTH;
		rc.weightx = 1.0; /* resize only the input fields, */
		lc.weightx = 0.0; /* but not the labels */
		lc.weighty = 0.0; /* resize lines equally */
		rc.weighty = 0.0; /* in vertical direction */
		lc.ipadx = 10; /* gap between labels and inputs */
		lc.ipady = 10; /* make all lines of the same height */
		rc.gridwidth = GridBagConstraints.REMAINDER;
		lbl = new JLabel("Number of ants:");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		final JSpinner antcnt = new JSpinner(new SpinnerNumberModel(30, 1,
				999999, 1));
		g.setConstraints(antcnt, rc);
		grid.add(antcnt);

		lbl = new JLabel("Initial pheromone:");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		final JTextField phinit = new JTextField("0");
		phinit.setFont(font);
		g.setConstraints(phinit, rc);
		grid.add(phinit);

		lbl = new JLabel("Pheromone trail weight(Alpha): ");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		final JTextField alphaInit = new JTextField("1");
		alphaInit.setFont(font);
		g.setConstraints(alphaInit, rc);
		grid.add(alphaInit);

		lbl = new JLabel("Inverse distance weight(Beta): ");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		final JTextField betaInit = new JTextField("2");
		betaInit.setFont(font);
		g.setConstraints(betaInit, rc);
		grid.add(betaInit);

		lbl = new JLabel("Choose decay type: ");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		String[] decayTypes = { "Polynomial", "Linear" };
		final JComboBox<String> decayType = new JComboBox<String>(decayTypes);
		decayType.setFont(font);
		g.setConstraints(decayType, rc);
		grid.add(decayType);

		bbar = new JPanel(new GridLayout(1, 2, 5, 5));
		bbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
		btn = new JButton("Ok");
		bbar.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
				ACODemo.this.panel.initAnts(
						((Integer) antcnt.getValue()).intValue(),
						Double.parseDouble(phinit.getText())==0.0?1:Double.parseDouble(phinit.getText()));
				ACODemo.this.panel.setParams(
						Double.parseDouble(alphaInit.getText()),
						Double.parseDouble(betaInit.getText()));
			}
		});
		btn = new JButton("Apply");
		bbar.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.panel.initAnts(
						((Integer) antcnt.getValue()).intValue(),
						Double.parseDouble(phinit.getText())==0.0?1:Double.parseDouble(phinit.getText()));
				ACODemo.this.panel.setParams(
						Double.parseDouble(alphaInit.getText()),
						Double.parseDouble(betaInit.getText()));
			}
		});
		btn = new JButton("Close");
		bbar.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
			}
		});

		dlg.getContentPane().add(grid, BorderLayout.CENTER);
		dlg.getContentPane().add(bbar, BorderLayout.SOUTH);
		dlg.setLocationRelativeTo(this);
		dlg.setLocation(664, 145);
		dlg.pack();
		return dlg;
	} /* createAnts() */

	/*------------------------------------------------------------------*/

	private void runAnts(int epochs, int delay) { /* --- run the ants */
		if (this.cnt >= 0) { /* check for running update */
			this.timer.stop();
			this.cnt = -1;
			return;
		}
		ACO aco = this.panel.getAco();
		if (aco == null) {
			return;
		}
		if (delay <= 0) { /* if to update at the end */
			while (--epochs >= 0)
				/* while more epochs to compute, */
				this.panel.runAnts(); /* run the ants again */
			this.panel.repaint(); /* redraw the window contents */
			this.stat.setText("epoch: " + aco.getEpoch() + ", best tour: "
					+ aco.getBestLength());
			return; /* show a status message, */
		} /* then abort the function */
		this.cnt = epochs; /* note the epochs */
		this.timer = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (--ACODemo.this.cnt < 0) {
					ACODemo.this.timer.stop();
					return;
				}
				ACODemo.this.panel.runAnts(); /* run the ants and */
				ACODemo.this.panel.repaint(); /* redraw the window contents */
				ACO aco = ACODemo.this.panel.getAco();
				ACODemo.this.stat.setText("epoch: " + aco.getEpoch()
						+ ", best tour: " + aco.getBestLength());
			}
		}); /* update the status text */
		this.timer.start(); /* start the status update timer */
		// this.panel.runAnts();
	} /* runAnts() */

	private JDialog createRunOpt() { /* --- create run dialog */
		final JDialog dlg = new JDialog(this, "Run Optimization...");
		GridBagLayout g = new GridBagLayout();
		GridBagConstraints lc = new GridBagConstraints();
		GridBagConstraints rc = new GridBagConstraints();
		JPanel grid = new JPanel(g);
		JPanel bbar;
		JLabel lbl;
		JTextArea help;
		JButton btn;

		grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lc.fill = /* fill fields in both directions */
		rc.fill = GridBagConstraints.BOTH;
		rc.weightx = 1.0; /* resize only the input fields, */
		lc.weightx = 0.0; /* but not the labels */
		lc.weighty = 0.0; /* resize lines equally */
		rc.weighty = 0.0; /* in vertical direction */
		lc.ipadx = 10; /* gap between labels and inputs */
		lc.ipady = 10; /* make all lines of the same height */
		rc.gridwidth = GridBagConstraints.REMAINDER;

		lbl = new JLabel("Number of epochs:");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		final JSpinner epochs = new JSpinner(new SpinnerNumberModel(5000, 1,
				999999, 1));
		g.setConstraints(epochs, rc);
		grid.add(epochs);

		lbl = new JLabel("Delay between epochs:");
		g.setConstraints(lbl, lc);
		grid.add(lbl);
		final JSpinner delay = new JSpinner(new SpinnerNumberModel(200, 0,
				999999, 10));
		g.setConstraints(delay, rc);
		grid.add(delay);

		bbar = new JPanel(new GridLayout(1, 2, 5, 5));
		bbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
		btn = new JButton("Ok");
		bbar.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
				ACODemo.this.runAnts(((Integer) epochs.getValue()).intValue(),
						((Integer) delay.getValue()).intValue());
			}
		});
		btn = new JButton("Apply");
		bbar.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.runAnts(((Integer) epochs.getValue()).intValue(),
						((Integer) delay.getValue()).intValue());
			}
		});
		btn = new JButton("Close");
		bbar.add(btn);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
			}
		});

		dlg.getContentPane().add(grid, BorderLayout.CENTER);
		dlg.getContentPane().add(bbar, BorderLayout.SOUTH);
		dlg.setLocationRelativeTo(this);
		dlg.setLocation(664, 465);
		dlg.pack();
		return dlg;
	} /* createRunOpt() */

	private JDialog createAbout() { /* --- create "About..." dialog box */
		final JDialog dlg = new JDialog(this, "About ACODemo...", true);
		Container pane = dlg.getContentPane();
		JButton btn = new JButton("Ok");
		JPanel rest = new JPanel(new BorderLayout(2, 2));
		JTextArea text = new JTextArea("ACODemo\n"
				+ "An Ant Colony Optimization Demo\n" + "Version "
				+ ACODemo.VERSION + "\n\n" + "written by Christian Borgelt\n"
				+ "Otto-von-Guericke-University of Magdeburg\n"
				+ "Universitatsplatz 2, D-39106 Magdeburg\n"
				+ "e-mail: borgelt@iws.cs.uni-magdeburg.de");
		text.setBackground(this.getBackground());
		text.setFont(new Font("Dialog", Font.BOLD, 12));
		text.setEditable(false);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
			}
		});
		rest.add(btn, BorderLayout.SOUTH);
		pane.setLayout(new FlowLayout());
		pane.add(text);
		pane.add(rest);
		dlg.setLocationRelativeTo(this);
		dlg.pack();
		dlg.setResizable(false);
		return dlg;
	} /* createAbout() */

	/*------------------------------------------------------------------*/

	public void run() { /* --- create GUI of ACO viewer */
		JMenuBar mbar; /* menu bar */
		JMenu menu; /* to create menu titles */
		JMenuItem item; /* to create menu items */

		this.getContentPane().setLayout(new BorderLayout());

		/* --- create and set the menu bar --- */
		mbar = new JMenuBar();
		this.getContentPane().add(mbar, BorderLayout.NORTH);

		menu = mbar.add(new JMenu("File"));
		menu.setMnemonic('f');
		item = menu.add(new JMenuItem("Load TSP..."));
		item.setMnemonic('l');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.loadTSP(null);
			}
		});
		item = menu.add(new JMenuItem("Reload TSP"));
		item.setMnemonic('r');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.loadTSP(ACODemo.this.curr);
			}
		});
		item = menu.add(new JMenuItem("Save TSP"));
		item.setMnemonic('s');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.saveTSP(ACODemo.this.curr);
			}
		});
		item = menu.add(new JMenuItem("Save TSP as..."));
		item.setMnemonic('a');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.saveTSP(null);
			}
		});
		item = menu.add(new JMenuItem("Save PNG Image..."));
		item.setMnemonic('i');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.saveImage(null);
			}
		});
		menu.addSeparator();
		item = menu.add(new JMenuItem("Quit"));
		item.setMnemonic('q');
		if (this.isprog) { /* if stand-alone program */
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		} /* terminate the program */
		else { /* if only visualization module */
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (ACODemo.this.about != null)
						ACODemo.this.about.setVisible(false);
					if (ACODemo.this.randtsp != null)
						ACODemo.this.randtsp.setVisible(false);
					if (ACODemo.this.antcol != null)
						ACODemo.this.antcol.setVisible(false);
					if (ACODemo.this.runopt != null)
						ACODemo.this.runopt.setVisible(false);
					if (ACODemo.this.params != null)
						ACODemo.this.params.setVisible(false);
					ACODemo.this.setVisible(false);
				}
			}); /* only close the window */
		} /* and the dialog boxes */

		menu = mbar.add(new JMenu("Actions"));

		item = menu.add(new JMenuItem("Create Ant Colony..."));
		item.setMnemonic('c');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ACODemo.this.antcol == null)
					ACODemo.this.antcol = createAnts();
				ACODemo.this.antcol.setVisible(true);
			}
		});
		item = menu.add(new JMenuItem("Run Optimization..."));
		item.setMnemonic('o');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ACODemo.this.runopt == null)
					ACODemo.this.runopt = createRunOpt();
				ACODemo.this.runopt.setVisible(true);
			}
		});
		item = menu.add(new JMenuItem("Stop Optimization"));
		item.setMnemonic('s');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ACODemo.this.timer == null)
					return;
				ACODemo.this.timer.stop();
				ACODemo.this.cnt = -1;
			}
		});
		menu.addSeparator();
		item = menu.add(new JMenuItem("Redraw"));
		item.setMnemonic('r');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ACODemo.this.panel.repaint();
			}
		});

		menu = mbar.add(new JMenu("Help"));
		menu.setMnemonic('h');
		item = menu.add(new JMenuItem("About..."));
		item.setMnemonic('a');
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ACODemo.this.about == null)
					ACODemo.this.about = ACODemo.this.createAbout();
				ACODemo.this.about.setVisible(true);
			}
		});

		/* --- create and set the main panel --- */
		this.panel = new ACOPanel();
		this.panel.setLayout(new BorderLayout());
		this.panel.setPreferredSize(new Dimension(656, 656));
		this.scroll = new JScrollPane(this.panel);
		this.getContentPane().add(this.scroll, BorderLayout.CENTER);

		/* --- create and set a status bar --- */
		this.stat = new JTextField("");
		this.stat.setEditable(false);
		this.getContentPane().add(this.stat, BorderLayout.SOUTH);

		/* --- show the frame window --- */
		this.setTitle("ACODemo");
		this.setDefaultCloseOperation(this.isprog ? JFrame.EXIT_ON_CLOSE
				: JFrame.HIDE_ON_CLOSE);
		this.setLocation(0, 0);
		this.pack();
		if (this.isprog)
			this.setVisible(true);
		this.stat.setText("ACODemo is up and running.");
	} /* run() */

	/* Following the recommendations in the Java tutorial, the user */
	/* interface is created in the "run" method, which is invoked */
	/* from the event queue, in order to avoid problems with threads. */

	/*------------------------------------------------------------------*/

	public ACODemo(boolean isProg) {
		this.isprog = isProg;
		try {
			EventQueue.invokeAndWait(this);
		} catch (Exception e) {
		}
	}

	public ACODemo() {
		this.isprog = false;
		try {
			EventQueue.invokeAndWait(this);
		} catch (Exception e) {
		}
	}

	public ACODemo(String title) {
		this(false);
		this.setTitle(title);
	}

	public ACODemo(File file) {
		this(false);
		this.loadTSP(file);
	}

	public ACODemo(String title, File file) {
		this(title);
		this.loadTSP(file);
	}

	/*------------------------------------------------------------------*/

	public static void main(String args[]) { /* --- main function */
		ACODemo v = new ACODemo(true); /* create an ACO demo viewer */
		if (args.length > 0)
			v.loadTSP(new File(args[0]));
	} /* main() *//* load traveling salesman problem */

} /* class ACODemo */
