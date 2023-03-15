package model.stages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utility.Outcome;
import view.createRoom.VCreateRoom;
import view.createRoom.VJoinRoom;
import view.createRoom.VRoomManager;

public class RoomManager implements ActionListener {
    private StageManager stageManager;
    private VRoomManager manager;
    private VCreateRoom create;
    private VJoinRoom join;

    public RoomManager(StageManager stageManager) {
        this.stageManager = stageManager;
        manager = new VRoomManager();
        create = new VCreateRoom();
        join = new VJoinRoom();

        initialize();
    }

    public void initialize() {
        manager.addListener(this);
        create.addListener(this);
        join.addListener(this);

        manager.gteLblTitle().setText("Logged in as " + stageManager.getClientUser());
        switchState(manager);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        // PULSANTE CREATE ROOM (DA MANAGER)
        if (evt.getSource() == manager.getBtnCreate())
            switchState(create);

        // PULSANTE JOIN ROOM (DA MANAGER)
        if (evt.getSource() == manager.getBtnJoin())
            switchState(join);

        // PULSANTE CREATE ROOM
        if (evt.getSource() == create.getBtnCreate()) {
            String id = create.getTxtId().getText();
            int n = Integer.parseInt(create.getBtnGroup().getSelection().getActionCommand());

            Outcome outcome = stageManager.createRoom(id, n);
            switch (outcome) {
                case Op_ACK:
                    switchState(null);
                    stageManager.waitingStage();
                    break;
                case Op_NACK:
                    create.getLblError().setVisible(true);
                    break;
                default:
                    break;
            }
        }

        // PULSANTE JOIN ROOM
        if (evt.getSource() == join.getBtnJoin()) {
            String id = join.getTxtId().getText();

            Outcome outcome = stageManager.joinRoom(id);
            switch (outcome) {
                case Op_ACK:
                    switchState(null);
                    stageManager.waitingStage();
                    break;
                case Op_NACK:
                    join.getLblError().setVisible(true);
                    break;
                default:
                    break;
            }
        }

        // PULSATE BACK
        if (evt.getSource() == create.getBtnBack() || evt.getSource() == join.getBtnBack())
            switchState(manager);

        // PULSANTE LOGOUT
        if (evt.getSource() == manager.getBtnLogout()) {
            switchState(null);
            stageManager.loginStage();
        }

    }

    // alterna la visibilita' delle varie finestre
    private void switchState(Object view) {
        // rende invisibili tutte le finestre e le resetta
        manager.setVisible(false);
        create.setVisible(false);
        create.clear();
        join.setVisible(false);
        join.clear();

        // rende visibile solo quella passata come parametro
        if (view instanceof VRoomManager)
            manager.setVisible(true);
        if (view instanceof VCreateRoom)
            create.setVisible(true);
        if (view instanceof VJoinRoom)
            join.setVisible(true);
    }
}
