package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Panel extends JPanel {

	protected Dimension dimension;
	protected int fontSize;
	protected int marginLeft;
	protected int marginTop;

	public Panel(Dimension dimension) {
		this.dimension = dimension;
		this.fontSize = 15;
		this.marginLeft = 10;
		this.marginTop = 25;

		this.setPreferredSize(dimension);
		this.setBounds(0, 0, dimension.width, dimension.height);
		this.setDoubleBuffered(true);
		this.setVisible(true);
		this.setBorder(BorderFactory.createLineBorder(Color.black));

	}

}
