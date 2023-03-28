package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ClientView extends View {

	private JPanel contentPane;
	private Panel matrixPanel, imgPanel, msgPanel, btnPanel;
	private JButton button;

	private int msgPanelWidth;
	private int side;

	// visto che noi gestiamo solo la dimensione del panel con la matrice,
	// aggiungo 50px in altezza per gli altri panel
	public ClientView(Dimension dimension, String title) {
		super(new Dimension(dimension.width, dimension.height + 50), title, true);

		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setSize(new Dimension(dimension.width, dimension.height + 50));

		side = 40;
		msgPanelWidth = dimension.width * 75 / 100; // 75%
		msgPanel = new MsgPanel(new Dimension(msgPanelWidth, side), "");
		msgPanel.setBounds(0, 0, msgPanelWidth, side);
		contentPane.add(msgPanel);

		btnPanel = new Panel(new Dimension(dimension.width - msgPanelWidth, side));
		btnPanel.setBounds(msgPanelWidth, 0, btnPanel.getWidth(), btnPanel.getHeight());
		button = new JButton("");
		btnPanel.add(button);
		contentPane.add(btnPanel);

		setContentPane(contentPane);
	}

	public void setMsg(String msg) {
		((MsgPanel) msgPanel).setMsg(msg, false);
	}

	public void setMsg(String msg, boolean flag) {
		((MsgPanel) msgPanel).setMsg(msg, flag);
	}

	public void setImgPanel(ImagePanel imgPanel) {
		this.imgPanel = imgPanel;
		imgPanel.setBounds(0, side, imgPanel.getWidth(), imgPanel.getHeight());
		contentPane.add(imgPanel);
	}

	public void setMatrixPanel(MatrixPanel matrixPanel) {
		this.matrixPanel = matrixPanel;
		matrixPanel.setBounds(0, side, matrixPanel.getWidth(), matrixPanel.getHeight());
		contentPane.add(this.matrixPanel);
		contentPane.invalidate();
		contentPane.validate();
		contentPane.repaint();
	}

	public void setButton(JButton button) {
		removeButton();
		this.button = button;
		this.button.setBounds(btnPanel.getBounds());
		btnPanel.add(this.button);
		btnPanel.invalidate();
		btnPanel.validate();
		btnPanel.repaint();
	}

	public void removeButton() {
		btnPanel.remove(button);
	}

	public void setMsgColor(Color color) {
		((MsgPanel) msgPanel).setColor(color);
	}

	public Panel getMatrixPanel() {
		return matrixPanel;
	}

	public void removeImagePanel() {
		contentPane.remove(imgPanel);
	}

}
