package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import utility.Crypto;
import utility.OurMath;
import utility.Outcome;

public class Client {
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String key;

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

                System.out.println("The key is: " + key);
                String message = "Hello World! 1234";
                System.out.println(message);
                System.out.println(Crypto.encrypt(message, key));
                sendObject(Crypto.encrypt(message, key));
                System.exit(1);

                // LoginManager login = new LoginManager();

                do {
                    break;

                } while (connection.isConnected());

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!connected);
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
            e.getMessage();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
