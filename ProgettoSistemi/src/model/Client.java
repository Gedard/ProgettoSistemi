package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.DefaultListModel;

import model.stages.LoginM;
import model.stages.RoomM;
import model.stages.WaitingM;
import utility.Outcome;

public class Client {
    private String user;
    
    // diverse view del client, che rappresentano i diversi stage
    private LoginM login = null;
    private RoomM room = null;
    private WaitingM waiting = null;

    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client() {
        try {

            connection = new Socket(InetAddress.getLocalHost(), 8080);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            // inizializzo la view
            loginStage();

        } catch (Exception e) {
            System.out.println("Impossibile connettersi al server");
        }

    }

    public Outcome login(String user, String pw) {
        // TODO: richiesta al server

        return Outcome.Op_ACK;
    }

    public Outcome signup(String user, String pw, String confirm) {
        // TODO: richiesta al server

        return Outcome.Op_ACK;
    }

    public Outcome createRoom(String id, int n) {
        // TODO: richiesta al server per la creazione di una stanza,
        // per ora ritorno sempre Op_ACK

        return Outcome.Op_ACK;
    }

    public Outcome joinRoom(String id) {
        // TODO: richiesta al server per la creazione di una stanza,
        // per ora ritorno sempre Op_ACK

        return Outcome.Op_ACK;
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

    public void updateList(DefaultListModel<String> model) {
        waiting.updateList(model);
    }

    public void updateCount(int count, int tot) {
        waiting.updateCount(count, tot);
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
