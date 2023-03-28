package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class MatrixPanel extends Panel {

	private Color[][] matrix;
	private int row, col;
	private BufferedImage image;

	public MatrixPanel(Dimension dimension, Color[][] referenceMatrix, int row, int col) {
		super(dimension);
		this.matrix = referenceMatrix;
		this.row = row;
		this.col = col;
		this.image = null;
	}

	// costruttore per l'eventuale immagine di 'sfondo': non utilizzato
	public MatrixPanel(Dimension dimension, Color[][] referenceMatrix, int row, int col, BufferedImage image) {
		this(dimension, referenceMatrix, row, col);
		this.image = image;
	}

	public void paintComponent(Graphics g) {

		int width = dimension.width / col;
		int height = dimension.height / row;

		if (image != null)
			g.drawImage(image, 0, 0, dimension.width - 15, dimension.height - 40, null);

		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < col; ++j) {
				if (image == null) { // se non ho l'immagine disegno anche le celle bianche
					g.setColor(matrix[i][j]);
					g.fillRect(j * width, i * height, width, height);
				} else if (matrix[i][j] != Color.white) { // altrimenti disegno solo quelle colorate
					g.setColor(matrix[i][j]);
					g.fillRect(j * width, i * height, width, height);
				}
			}
		}
	}

}
