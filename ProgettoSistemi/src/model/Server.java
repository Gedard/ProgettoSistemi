package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

import utility.Outcome;
import utility.Pair;

public class Server {
    private ArrayList<Room> rooms;

    public Server() {
        rooms = new ArrayList<>();

        // TODO: connessione con i client
        // gia' implementato: creazione di stanza e partecipazione a stanza gia' creata
    }

    // gestisce il login di un client
    // ritorna true se il login va a buon fine, false altrimenti
    public static boolean login(String userName, String pw) {
        // TODO: controllare che l'utente non sia gia' connesso

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
            return Outcome.user_taken;

        Pair<String, String> user = readUser(userName);

        // utente gia' registrato
        if (user != null)
            return Outcome.user_taken;

        // pw non coincidenti
        if (pw.compareTo(confirm) != 0)
            return Outcome.pw_doesnt_match;

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

    public Outcome createRoom(String id, int n) {
        if (id == "")
            return Outcome.Op_NACK;

        // stanza gia' esistente
        if (findRoom(id) != null)
            return Outcome.Op_NACK;

        rooms.add(new Room(id, n));

        return Outcome.Op_ACK;
    }

    public Outcome joinRoom(String id, Client client) {
        if (id == "" || client == null)
            return Outcome.Op_NACK;
        
        // cerco la stanza
        Room room = findRoom(id);
        if (room == null)
            return null;
        
        return room.addClient(client);
    }

    private Room findRoom(String id) {
        for (Room room : rooms)
            if (room.getId().compareTo(id) == 0)
                return room;

        return null;
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
