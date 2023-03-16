package model.stages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Client;
import utility.Outcome;
import view.createRoom.VCreateRoom;
import view.createRoom.VJoinRoom;
import view.createRoom.VRoomManager;

public class RoomM implements ActionListener {
    private Client client;
    private VRoomManager choose;
    private VCreateRoom create;
    private VJoinRoom join;

    public RoomM(Client client) {
        this.client = client;
        choose = new VRoomManager();
        create = new VCreateRoom();
        join = new VJoinRoom();

        initialize();
    }

    public void initialize() {
        choose.addListener(this);
        create.addListener(this);
        join.addListener(this);

        choose.gteLblTitle().setText("Logged in as " + client.getUser());
        switchState(choose);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        // PULSANTE CREATE ROOM (DA CHOOSE)
        if (evt.getSource() == choose.getBtnCreate())
            switchState(create);

        // PULSANTE JOIN ROOM (DA CHOOSE)
        if (evt.getSource() == choose.getBtnJoin())
            switchState(join);

        // PULSANTE CREATE ROOM
        if (evt.getSource() == create.getBtnCreate()) {
            String id = create.getTxtId().getText();
            int n = Integer.parseInt(create.getBtnGroup().getSelection().getActionCommand());

            Outcome outcome = client.createRoom(id, n);
            switch (outcome) {
                case Op_ACK:
                    switchState(null);
                    client.waitingStage();
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

            Outcome outcome = client.joinRoom(id);
            switch (outcome) {
                case Op_ACK:
                    switchState(null);
                    client.waitingStage();
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
            switchState(choose);

        // PULSANTE LOGOUT
        if (evt.getSource() == choose.getBtnLogout()) {
            switchState(null);
            client.loginStage();
        }

    }

    // alterna la visibilita' delle varie finestre
    private void switchState(Object view) {
        // rende invisibili tutte le finestre e le resetta
        choose.setVisible(false);
        create.setVisible(false);
        create.clear();
        join.setVisible(false);
        join.clear();

        // rende visibile solo quella passata come parametro
        if (view instanceof VRoomManager)
            choose.setVisible(true);
        if (view instanceof VCreateRoom)
            create.setVisible(true);
        if (view instanceof VJoinRoom)
            join.setVisible(true);
    }
}
