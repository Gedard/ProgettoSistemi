package view.login;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VLogin extends JFrame {

	private JPanel contentPane;
	private JLabel lblProject, lblUser, lblPw, lblError, lblOr;
	private JTextField txtUser;
	private JPasswordField txtPw;
	private JButton btnLogin, btnSignup;
    
	public VLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 400);
		setLocationRelativeTo(null);
		setVisible(false);
		setResizable(false);
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
		lblUser.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(10, 77, 266, 24);
		contentPane.add(lblUser);
		
		txtUser = new JTextField();
		txtUser.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtUser.setBounds(63, 112, 162, 24);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		lblPw = new JLabel("Password");
		lblPw.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPw.setHorizontalAlignment(SwingConstants.CENTER);
		lblPw.setBounds(10, 147, 266, 24);
		contentPane.add(lblPw);
		
		txtPw = new JPasswordField();
		txtPw.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPw.setBounds(63, 182, 162, 24);
		contentPane.add(txtPw);
		
		lblError = new JLabel("Wrong credentials");
		lblError.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(10, 217, 266, 16);
		lblError.setVisible(false);
		contentPane.add(lblError);
		
		btnLogin = new JButton("Log in");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogin.setBounds(101, 244, 89, 23);
		contentPane.add(btnLogin);
		
		lblOr = new JLabel("or");
		lblOr.setHorizontalAlignment(SwingConstants.CENTER);
		lblOr.setBounds(10, 271, 266, 14);
		contentPane.add(lblOr);
		
		btnSignup = new JButton("Sign up");
		btnSignup.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnSignup.setBounds(101, 288, 89, 23);
		contentPane.add(btnSignup);
	}
}
