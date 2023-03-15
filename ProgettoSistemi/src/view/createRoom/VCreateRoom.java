package view.createRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VCreateRoom extends JFrame {

    private JPanel contentPane, btnsPane;
	private JLabel lblTitle, lblNrPlayers, lblRoomId, lblError;
	private JRadioButton rdbtn1, rdbtn2, rdbtn3, rdbtn4, rdbtn5;
	private JButton btnCreate, btnBack;
	private ButtonGroup btnGroup;
	private JTextField txtId;

    public VCreateRoom() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 350);
		setLocationRelativeTo(null);
		setVisible(false);
		setResizable(false);
		setAlwaysOnTop(true);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblTitle = new JLabel("Create a room");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 11, 316, 39);
		contentPane.add(lblTitle);
		
		lblNrPlayers = new JLabel("Number of players");
		lblNrPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		lblNrPlayers.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNrPlayers.setBounds(10, 157, 316, 28);
		contentPane.add(lblNrPlayers);
		
		btnsPane = new JPanel();
		btnsPane.setBackground(new Color(255, 255, 255));
		btnsPane.setBounds(45, 196, 267, 28);
		contentPane.add(btnsPane);
		btnsPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		rdbtn1 = new JRadioButton("2");
		rdbtn1.setBackground(new Color(255, 255, 255));
		rdbtn1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtn1.setSelected(true);
		rdbtn1.setActionCommand("2");
		btnsPane.add(rdbtn1);
		
		rdbtn2 = new JRadioButton("3");
		rdbtn2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtn2.setBackground(new Color(255, 255, 255));
		rdbtn2.setActionCommand("3");
		btnsPane.add(rdbtn2);
		
		rdbtn3 = new JRadioButton("4");
		rdbtn3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtn3.setBackground(new Color(255, 255, 255));
		rdbtn3.setActionCommand("4");
		btnsPane.add(rdbtn3);
		
		rdbtn4 = new JRadioButton("5");
		rdbtn4.setBackground(new Color(255, 255, 255));
		rdbtn4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtn4.setActionCommand("5");
		btnsPane.add(rdbtn4);
		
		rdbtn5 = new JRadioButton("6");
		rdbtn5.setBackground(new Color(255, 255, 255));
		rdbtn5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtn5.setActionCommand("6");
		btnsPane.add(rdbtn5);
		
		btnCreate = new JButton("Create");
		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCreate.setBounds(102, 246, 137, 28);
		contentPane.add(btnCreate);
		
		btnGroup = new ButtonGroup();
		btnGroup.add(rdbtn1);
		btnGroup.add(rdbtn2);
		btnGroup.add(rdbtn3);
		btnGroup.add(rdbtn4);
		btnGroup.add(rdbtn5);
		
		txtId = new JTextField();
		txtId.setHorizontalAlignment(SwingConstants.CENTER);
		txtId.setBounds(68, 118, 199, 28);
		contentPane.add(txtId);
		txtId.setColumns(10);
		
		lblRoomId = new JLabel("Room ID");
		lblRoomId.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRoomId.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomId.setBounds(10, 79, 316, 28);
		contentPane.add(lblRoomId);
		
		lblError = new JLabel("The room is occupied");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblError.setBounds(10, 54, 316, 14);
		lblError.setVisible(false);
		contentPane.add(lblError);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(128, 279, 89, 23);
		contentPane.add(btnBack);
	}

    public void addListener(ActionListener reference) {
        btnCreate.addActionListener(reference);
        btnBack.addActionListener(reference);
    }

	public void clear() {
		txtId.setText(null);
		lblError.setVisible(false);
	}

    public JButton getBtnCreate() {
        return btnCreate;
    }

    public ButtonGroup getBtnGroup() {
        return btnGroup;
    }

    public JLabel getLblError() {
        return lblError;
    }

    public JButton getBtnBack() {
        return btnBack;
    }

    public JTextField getTxtId() {
        return txtId;
    }

    
}
