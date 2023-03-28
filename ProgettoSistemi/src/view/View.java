package view;

import java.awt.Dimension;

import javax.swing.JFrame;

import model.RefreshThread;

public class View extends JFrame {

	public View(Dimension dimension, String title) {
		this(dimension, title, false);
	}

	public View(Dimension dimension, String title, boolean exitOnClose) {

		this.setSize(dimension);
		this.setTitle(title);
		this.setVisible(true);

		if (exitOnClose) {

			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // va bene per i client ma non per i server
			// perche' killano il processo server, e quindi chiudono tutto
		}

		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setResizable(false);

		new RefreshThread(this);
	}

	public void addPanel(Panel panel) {
		this.add(panel);
	}
}
