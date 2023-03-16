package model.stages;

import javax.swing.DefaultListModel;

import model.Client;
import utility.Outcome;

public class StageManager {
    private Client client;
    private LoginM login = null;
    private RoomM room = null;
    private WaitingM waiting = null;

    public StageManager(Client client) {
        this.client = client;
        loginStage();
    }

    // primo stage: fase login
    public void loginStage() {
        if (login == null)
            login = new LoginM(this);
        else
            login.initialize();
    }

    // secondo stage: fase di creazione/partecipazione a una stanza
    public void roomStage() {
        if (room == null)
            room = new RoomM(this);
        else
            room.initialize();
    }

    // terzo stage: fase di wait fino all'inizio della partita
    public void waitingStage() {
        if (waiting == null)
            waiting = new WaitingM(this);
        else
            waiting.initialize();
    } 

    public Outcome createRoom(String id, int n) {
        return client.createRoom(id, n);
    }

    public Outcome joinRoom(String id) {
        return client.joinRoom(id);
    }

    public void updateList(DefaultListModel<String> model) {
        waiting.updateList(model);
    }

    public void updateCount(int count, int tot) {
        waiting.updateCount(count, tot);
    }

    public void setClientUser(String user) {
        client.setUser(user);
    }

    public String getClientUser() {
        return client.getUser();
    }

}
