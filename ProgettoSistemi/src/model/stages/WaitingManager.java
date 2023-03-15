package model.stages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;

import view.createRoom.VWaitingRoom;

public class WaitingManager implements ActionListener{
    private StageManager manager;
    private VWaitingRoom view;

    public WaitingManager(StageManager manager) {
        this.manager = manager;
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
