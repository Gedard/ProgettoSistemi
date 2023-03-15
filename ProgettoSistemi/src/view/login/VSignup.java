package view.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VSignup extends JFrame {

	private JPanel contentPane;
	private JLabel lblProject, lblUser, lblPw, lblConfirm, lblError1, lblError2;
	private JTextField txtUser;
	private JPasswordField txtPw, txtConfirm;
	private JButton btnSignup, btnBack;

	public VSignup() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 410);
		setLocationRelativeTo(null);
		setVisible(false);
		setResizable(false);
		setAlwaysOnTop(true);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblProject = new JLabel("Disegno a distanza 2.0");
		lblProject.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblProject.setHorizontalAlignment(SwingConstants.CENTER);
		lblProject.setBounds(10, 21, 266, 34);
		contentPane.add(lblProject);
		
		lblUser = new JLabel("Username");
		lblUser.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(10, 66, 266, 24);
		contentPane.add(lblUser);
		
		txtUser = new JTextField();
		txtUser.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtUser.setBounds(63, 101, 162, 24);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		lblPw = new JLabel("Password");
		lblPw.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPw.setHorizontalAlignment(SwingConstants.CENTER);
		lblPw.setBounds(10, 149, 266, 24);
		contentPane.add(lblPw);
		
		txtPw = new JPasswordField();
		txtPw.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPw.setBounds(63, 184, 162, 24);
		contentPane.add(txtPw);
		
		btnSignup = new JButton("Sign up");
		btnSignup.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSignup.setBounds(100, 305, 89, 23);
		contentPane.add(btnSignup);

		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnBack.setBounds(110, 335, 69, 15);
		contentPane.add(btnBack);
		
		lblConfirm = new JLabel("Confirm password");
		lblConfirm.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfirm.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblConfirm.setBounds(10, 219, 266, 24);
		contentPane.add(lblConfirm);
		
		txtConfirm = new JPasswordField();
		txtConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtConfirm.setBounds(63, 254, 162, 24);
		contentPane.add(txtConfirm);
		
		lblError1 = new JLabel("Username already taken");
		lblError1.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblError1.setForeground(new Color(255, 0, 0));
		lblError1.setHorizontalAlignment(SwingConstants.CENTER);
		lblError1.setBounds(10, 125, 266, 16);
		lblError1.setVisible(false);
		contentPane.add(lblError1);
		
		lblError2 = new JLabel("Passwords do not match");
		lblError2.setHorizontalAlignment(SwingConstants.CENTER);
		lblError2.setForeground(Color.RED);
		lblError2.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblError2.setBounds(10, 277, 266, 24);
		lblError2.setVisible(false);
		contentPane.add(lblError2);
	}

    public void addListener(ActionListener reference) {
        btnSignup.addActionListener(reference);
		btnBack.addActionListener(reference);
    }

	// svuota tutti i campi e nasconde tutti gli errori
	public void clear() {
		txtUser.setText(null);
		txtPw.setText(null);
		txtConfirm.setText(null);
		lblError1.setVisible(false);
		lblError2.setVisible(false);
	}

    public JLabel getLblError1() {
        return lblError1;
    }

    public JLabel getLblError2() {
        return lblError2;
    }

    public JTextField getTxtUser() {
        return txtUser;
    }

    public JPasswordField getTxtPw() {
        return txtPw;
    }

    public JPasswordField getTxtConfirm() {
        return txtConfirm;
    }

    public JButton getBtnSignup() {
        return btnSignup;
    }  
	
	public JButton getBtnBack() {
		return btnBack;
	}
}

