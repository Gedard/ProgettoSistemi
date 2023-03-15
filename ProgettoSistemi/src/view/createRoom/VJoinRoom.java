package view.createRoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VJoinRoom extends JFrame {

    private JPanel contentPane;
    private JLabel lblTitle, lblRoomId, lblError;
    private JButton btnJoin;
    private JTextField txtId;

    public VJoinRoom() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 350, 250);
        setLocationRelativeTo(null);
        setVisible(false);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblTitle = new JLabel("Join a room");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 11, 316, 39);
        contentPane.add(lblTitle);

        lblRoomId = new JLabel("Room ID");
        lblRoomId.setHorizontalAlignment(SwingConstants.CENTER);
        lblRoomId.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblRoomId.setBounds(10, 61, 316, 28);
        contentPane.add(lblRoomId);

        btnJoin = new JButton("Join");
        btnJoin.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnJoin.setBounds(102, 156, 137, 28);
        contentPane.add(btnJoin);

        txtId = new JTextField();
        txtId.setHorizontalAlignment(SwingConstants.CENTER);
        txtId.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtId.setBounds(82, 100, 176, 28);
        contentPane.add(txtId);
        txtId.setColumns(10);

        lblError = new JLabel("This room does not exist");
        lblError.setForeground(new Color(255, 0, 0));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setFont(new Font("Tahoma", Font.ITALIC, 13));
        lblError.setBounds(10, 128, 316, 17);
        lblError.setVisible(false);
        contentPane.add(lblError);
    }

    public void addListener(ActionListener reference) {
        btnJoin.addActionListener(reference);
    }

    public JLabel getLblError() {
        return lblError;
    }

    public JButton getBtnJoin() {
        return btnJoin;
    }

    public JTextField getTxtId() {
        return txtId;
    }
}