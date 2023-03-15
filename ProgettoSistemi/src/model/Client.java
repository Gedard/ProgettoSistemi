package model;

import javax.swing.DefaultListModel;
import javax.swing.ToolTipManager;

import model.stages.StageManager;
import utility.Outcome;

public class Client {
    private String user;
    private StageManager manager;

    public Client() {
        // TODO: connessione al server

        manager = new StageManager(this);
    }

    public Outcome createRoom(String id, int n) {
        if (id == "")
            return Outcome.Op_NACK;

        // TODO: richiesta al server per la creazione di una stanza,
        // per ora ritorno sempre Op_ACK

        return Outcome.Op_ACK;
    }

    public Outcome joinRoom(String id) {
        if (id == "")
            return Outcome.Op_NACK;

        // TODO: richiesta al server per la creazione di una stanza,
        // per ora ritorno sempre Op_ACK

        return Outcome.Op_ACK;
    }

    public void createWaitingRoom() {
        manager.waitingStage();
    }

    public void updateCount(int count, int tot) {
        manager.updateCount(count, tot);
    }

    public void updateList(DefaultListModel<String> model) {
        manager.updateList(model);
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
