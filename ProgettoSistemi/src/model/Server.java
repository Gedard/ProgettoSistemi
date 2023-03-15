package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

import utility.Outcome;
import utility.Pair;

public class Server extends Thread {
    private ServerSocket server;
    private Socket connection;
    private Stanza currentStanza;
    private ArrayList<Stanza> stanze;
    private final int DEFAULT_MAX_CLIENTS = 3;
    private int stanzaCounter = 0; // contatore di stanze create

    public Server() {
        try {
            server = new ServerSocket(8080);
            stanze = new ArrayList<>();
            currentStanza = createNewStanza();

            System.out.println("Server attivo");
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                connection = server.accept();
                System.out.println("Nuovo client connesso");

                currentStanza = getFirstFreeRoom();

                if (currentStanza == null) {
                    currentStanza = createNewStanza();
                }

                new Connection(connection, currentStanza);

                // System.out.println("stanza " + currentStanza.getNumeroStanza() + " (client
                // attualmente connessi: "
                // + currentStanza.getConnectedClients() + ")");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // gestisce il login di un client
    // ritorna true se il login va a buon fine, false altrimenti
    public static boolean login(String userName, String pw) {
        // campi vuoti
        if (userName == "" || pw == "")
            return false;

        Pair<String, String> user = readUser(userName);

        // nessun utente trovato
        if (user == null)
            return false;

        // match della pw
        return user.second.compareTo(pw) == 0;
    }

    public static Outcome signup(String userName, String pw, String confirm) {
        if (userName == "" || pw == "" || confirm == "")
            return Outcome.USER;

        Pair<String, String> user = readUser(userName);

        // utente gia' registrato
        if (user != null)
            return Outcome.USER;

        // pw non coincidenti
        if (pw.compareTo(confirm) != 0)
            return Outcome.PW;

        // scrivo su file csv
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(new File("ProgettoSistemi\\db\\users.csv"), true));
            String[] data = { userName, pw };
            writer.writeNext(data, false);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Outcome.Op_ACK;
    }

    // controllo se e' presente uno user
    private static Pair<String, String> readUser(String userName) {
        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader("ProgettoSistemi\\db\\users.csv"));

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].compareTo(userName) == 0) {
                    br.close();
                    // ritorno i dati dell'utente trovato
                    return new Pair<String, String>(data[0], data[1]);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // cerca tra tutte le stanze gia' create la prima con un posto libero
    // e la ritorna.
    // ritorna null se sono tutte occupate
    public Stanza getFirstFreeRoom() {
        for (Stanza room : stanze) {
            if (!room.isFull())
                return room;
        }

        return null;
    }

    public Stanza createNewStanza() {
        Stanza tmp = new Stanza(DEFAULT_MAX_CLIENTS, stanzaCounter++);
        // tmp.chooseImage();
        stanze.add(tmp);
        return tmp;
    }

    public static void main(String[] args) {
        new Server();
    }
}
