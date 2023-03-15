package view.createRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VRoomManager extends JFrame {

    private JPanel contentPane;
    private JLabel lblTitle;
    private JButton btnCreate, btnJoin, btnLogout;

    public VRoomManager() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 200);
        setLocationRelativeTo(null);
        setVisible(false);
        setResizable(false);
        setAlwaysOnTop(true);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblTitle = new JLabel("Logged in as ");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 11, 316, 41);
        contentPane.add(lblTitle);

        btnCreate = new JButton("Create room");
        btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnCreate.setBounds(97, 63, 139, 23);
        contentPane.add(btnCreate);

        btnJoin = new JButton("Join room");
        btnJoin.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnJoin.setBounds(97, 97, 139, 23);
        contentPane.add(btnJoin);

        btnLogout = new JButton("Logout");
		btnLogout.setBounds(123, 131, 89, 23);
		contentPane.add(btnLogout);
    }

    public void addListener(ActionListener reference) {
        btnCreate.addActionListener(reference);
        btnJoin.addActionListener(reference);
        btnLogout.addActionListener(reference);
    }

    public JLabel gteLblTitle() {
        return lblTitle;
    }

    public JButton getBtnCreate() {
        return btnCreate;
    }

    public JButton getBtnJoin() {
        return btnJoin;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

}
