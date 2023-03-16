package model.stages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;

import model.Client;
import view.createRoom.VWaitingRoom;

public class WaitingM implements ActionListener {
    private Client client;
    private VWaitingRoom view;

    public WaitingM(Client client) {
        this.client = client;
        view = new VWaitingRoom();
        initialize();
    }

    public void initialize() {
        view.setVisible(true);
    }

    public void updateList(DefaultListModel<String> model) {
        view.getList().setModel(model);
        view.repaint();
    }

    public void updateCount(int count, int tot) {
        view.getlblCount().setText("Connected players: " + count + "/" + tot);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // TODO: action performed
    }
}
