package view.createRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VWaitingRoom extends JFrame {

	private JPanel contentPane;
	private JLabel lblTitle, lblError, lblCount;
	private JScrollPane scrollPane;
	private JList<String> list;
	private JButton btnStart;

	public VWaitingRoom() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		setLocationRelativeTo(null);
		setVisible(false);
		setResizable(false);
		setAlwaysOnTop(true);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitle = new JLabel("Waiting room");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblTitle.setBounds(10, 11, 466, 46);
		contentPane.add(lblTitle);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 106, 466, 157);
		contentPane.add(scrollPane);

		list = new JList<>();
		list.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPane.setViewportView(list);

		btnStart = new JButton("Start game");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnStart.setBounds(340, 274, 136, 28);
		btnStart.setEnabled(false);
		contentPane.add(btnStart);

		lblError = new JLabel("You must wait for other players to connect");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblError.setBounds(10, 274, 278, 28);
		lblError.setVisible(false);
		contentPane.add(lblError);

		lblCount = new JLabel();
		lblCount.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCount.setBounds(10, 68, 466, 27);
		contentPane.add(lblCount);
	}

	public void addListener(ActionListener reference) {
		btnStart.addActionListener(reference);
	}

	public JLabel getLblError() {
		return lblError;
	}

	public JLabel getlblCount() {
		return lblCount;
	}

	public JList<String> getList() {
		return list;
	}

	public JButton getBtnStart() {
		return btnStart;
	}
}
