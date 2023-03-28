package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

public class MsgPanel extends Panel {

	private String points;
	private int count;
	private String msg;
	private boolean flag; // se true, disegna i puntini
	private Color color;

	public MsgPanel(Dimension dimension, String msg) {
		super(dimension);
		this.msg = msg;
		color = Color.black;
	}

	public void paintComponent(Graphics g) {
		points = points();

		g.setFont(new Font("Comic Sans MS", 1, fontSize));
		g.setColor(color);

		if (flag)
			g.drawString(msg + points, marginLeft, marginTop);
		else
			g.drawString(msg, marginLeft, marginTop);

	}

	private String points() {
		int max = 10;

		count++;
		count %= (max * 4);

		return count / max == 0 ? "" : count / max == 1 ? "." : count / max == 2 ? ".." : "...";
	}

	public void setMsg(String msg, boolean flag) {
		this.msg = msg;
		this.flag = flag;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
