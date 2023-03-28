package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class ImagePanel extends Panel {

	private Image image;

	public ImagePanel(Dimension dimension, Image image) {
		super(dimension);
		this.image = image;
		this.setBackground(Color.white);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, dimension.width - 15, dimension.height - 40, null);
	}
}
