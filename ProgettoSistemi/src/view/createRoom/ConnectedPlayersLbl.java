package view.createRoom;

import java.awt.Font;

import javax.swing.JLabel;

public class ConnectedPlayersLbl extends JLabel{

	public ConnectedPlayersLbl() {
		setFont(new Font("Tahoma", Font.BOLD, 15));
		setBounds(10, 68, 466, 27);
	}
	
	public void updateText(String text) {
		setText(text);
	}
}