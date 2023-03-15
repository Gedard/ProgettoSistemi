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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VCreateRoom extends JFrame {

    private JPanel contentPane, btnsPane;
    private JLabel lblTitle, lblNrPlayers;
    private JRadioButton rdbtn1, rdbtn2, rdbtn3, rdbtn4, rdbtn5;
    private JButton btnCreate;
    private ButtonGroup btnGroup;

    public VCreateRoom() {
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

        lblTitle = new JLabel("Create a room");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 11, 316, 39);
        contentPane.add(lblTitle);

        lblNrPlayers = new JLabel("Number of players");
        lblNrPlayers.setHorizontalAlignment(SwingConstants.CENTER);
        lblNrPlayers.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNrPlayers.setBounds(10, 61, 316, 28);
        contentPane.add(lblNrPlayers);

        btnsPane = new JPanel();
        btnsPane.setBackground(new Color(255, 255, 255));
        btnsPane.setBounds(43, 100, 267, 33);
        contentPane.add(btnsPane);
        btnsPane.setLayout(new GridLayout(1, 0, 0, 0));

        rdbtn1 = new JRadioButton("2");
        rdbtn1.setBackground(new Color(255, 255, 255));
        rdbtn1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtn1.setSelected(true);
        btnsPane.add(rdbtn1);

        rdbtn2 = new JRadioButton("3");
        rdbtn2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtn2.setBackground(new Color(255, 255, 255));
        btnsPane.add(rdbtn2);

        rdbtn3 = new JRadioButton("4");
        rdbtn3.setFont(new Font("Tahoma", Font.PLAIN, 15));
        rdbtn3.setBackground(new Color(255, 255, 255));
        btnsPane.add(rdbtn3);

        rdbtn4 = new JRadioButton("5");
        rdbtn4.setBackground(new Color(255, 255, 255));
        rdbtn4.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnsPane.add(rdbtn4);

        rdbtn5 = new JRadioButton("6");
        rdbtn5.setBackground(new Color(255, 255, 255));
        rdbtn5.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnsPane.add(rdbtn5);

        btnCreate = new JButton("Create");
        btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnCreate.setBounds(102, 149, 137, 28);
        contentPane.add(btnCreate);

        btnGroup = new ButtonGroup();
        btnGroup.add(rdbtn1);
        btnGroup.add(rdbtn2);
        btnGroup.add(rdbtn3);
        btnGroup.add(rdbtn4);
        btnGroup.add(rdbtn5);
    }

    public void addListener(ActionListener reference) {
        btnCreate.addActionListener(reference);
    }

    public JButton getBtnCreate() {
        return btnCreate;
    }

    public ButtonGroup getBtnGroup() {
        return btnGroup;
    }
}
