package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.opencsv.CSVWriter;

import utility.Outcome;
import utility.Pair;

public class Server {

    public static void main(String[] args) {
        // TODO: connessione con i client
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

}
