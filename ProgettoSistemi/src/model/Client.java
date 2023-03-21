package model;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;

import model.stages.LoginM;
import model.stages.RoomM;
import model.stages.WaitingM;
import utility.Crypto;
import utility.OurMath;
import utility.Outcome;
import utility.Pair;
import utility.Request;

public class Client {
    private String user;
    private String key;

    // diverse view del client, che rappresentano i diversi stage
    private LoginM login = null;
    private RoomM room = null;
    private WaitingM waiting = null;

    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client() {
        boolean connected = false; // permette di ritentare la connessione se il server
        // non e' ancora attivo quando il client richiede il servizio
        do {
            try {
                connection = new Socket(InetAddress.getLocalHost(), 8080);
                connected = true;
                output = new ObjectOutputStream(connection.getOutputStream());
                input = new ObjectInputStream(connection.getInputStream());

                diffieHellmanInit();

                String msg = "sksk";
                String encrypted = Crypto.encrypt(msg, key);
                System.out.println(encrypted);
                System.out.println(Crypto.decrypt(encrypted, key));

                output.writeObject(encrypted);

                // inizializzo la view
                loginStage();

                disconnect();
            } catch (ConnectException e) {
                // catch per le eccezioni causate dall'attivazione di un client
                // quando il server non e' ancora stato acceso
                System.out.println("Connessione fallita. Nuovo tentativo in un secondo");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (EOFException e) {
                // EOFException - if this input stream reaches the end before reading eight
                // bytes.
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (!connected);
    }

    public Object readInput() throws Exception {
        Object obj = null;
        obj = input.readObject();

        if (obj instanceof Outcome) {
            if (obj == Outcome.end) {
                System.out.println("Il server ha chiesto di chiudere la connessione");
                disconnect();
                // attenzione perche' non sono sicuro (ma spero) che
                // questa funzione killi unicamente questo client senza
                // causare altri problemi
                System.exit(0);
            }
        }

        return obj;
    }

    public void disconnect() throws Exception {
        // invio un messaggio di richiesta chiusura
        sendObject(Outcome.end);

        Object response = null;

        // attendo la risposta dal client di conferma disconnessione
        do {
            response = input.readObject();
        } while (response != Outcome.end);

        input.close();
        output.close();
        connection.close();
    }

    public void sendObject(Object obj) {
        try {
            output.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // invia una lista di oggetti
    // se l'oggetto e' una stringa la crypta
    public void sendObject(ArrayList<Object> objs) {
        try {
            ArrayList<Object> data = new ArrayList<>();

            for (Object obj : objs) {
                if (obj instanceof String)
                    data.add(Crypto.encrypt((String) obj, key));
                else
                    data.add(obj);
            }

            output.writeObject(objs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void diffieHellmanInit() {
        Random random = new Random();
        // generatore ed esponente
        long generator = Math.abs(random.nextLong());
        int privateExp = Math.abs(random.nextInt());

        // calcolo chiave pubblica da condividere
        long publicKey = OurMath.modPow(generator, privateExp, OurMath.MOD);

        // invio dati pubblici
        sendObject(generator);
        sendObject(publicKey);

        long serverPublicKey = 1;

        // lettura della chiave pubblica del server
        try {
            serverPublicKey = (Long) readInput();
        } catch (Exception e) {
            serverPublicKey = 1;
        }

        // calcolo finale della chiave segreta
        long privateKey = OurMath.modPow(serverPublicKey, privateExp, OurMath.MOD);

        // conversione in hexString
        key = Long.toHexString(privateKey);
    }

    public Outcome login(String user, String pw) {
        try {
            ArrayList<String> data = new ArrayList<>();
            data.add(Crypto.encrypt(user, key));
            data.add(Crypto.encrypt(pw, key));

            Pair<Request, ArrayList<String>> req = new Pair<>(Request.login, data);

            // invio al server
            sendObject(req);
            // leggo la risposta
            return (Outcome) readInput();

        } catch (Exception e) {
            e.printStackTrace();
            return Outcome.Op_NACK;
        }
    }

    public Outcome signup(String user, String pw, String confirm) {
        try {
            ArrayList<String> data = new ArrayList<>();
            data.add(Crypto.encrypt(user, key));
            data.add(Crypto.encrypt(pw, key));
            data.add(Crypto.encrypt(confirm, key));

            Pair<Request, ArrayList<String>> req = new Pair<>(Request.signup, data);

            // invio al server
            output.writeObject(req);
            // leggo la risposta
            return (Outcome) readInput();

        } catch (Exception e) {
            e.printStackTrace();
            return Outcome.Op_NACK;
        }
    }

    public Outcome createRoom(String id, int n) {
        try {
            Pair<String, Integer> data = new Pair<>(Crypto.encrypt(id, key), n);
            Pair<Request, Pair<String, Integer>> req = new Pair<>(Request.login, data);

            // invio al server
            sendObject(req);
            // leggo la risposta
            return (Outcome) readInput();

        } catch (Exception e) {
            e.printStackTrace();
            return Outcome.Op_NACK;
        }
    }

    public Outcome joinRoom(String id) {
        try {
            Pair<Request, String> req = new Pair<>(Request.login, Crypto.encrypt(id, key));

            // invio al server
            sendObject(req);
            // leggo la risposta
            return (Outcome) readInput();

        } catch (Exception e) {
            e.printStackTrace();
            return Outcome.Op_NACK;
        }
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
