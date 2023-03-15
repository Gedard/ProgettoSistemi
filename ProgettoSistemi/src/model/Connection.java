package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Semaphore;

import utility.Crypto;
import utility.OurMath;

public class Connection extends Thread {
    private static Semaphore semaphore;
    private Socket connection;
    private Stanza stanza;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String key;

    public Connection(Socket connection, Stanza stanza) {
        try {
            this.stanza = stanza;
            this.connection = connection;
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            this.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            diffieHellmanInit();
            System.out.println("The key is: " + key);
            String message = "";
            try {
                message = (String) readInput();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(message);
            System.out.println(Crypto.decrypt(message, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void diffieHellmanInit() {
        Random random = new Random();
        // esponente
        int privateExp = Math.abs(random.nextInt());
        long generator = 1;

        // lettura del generatore dal client
        try {
            generator = (Long) readInput();
        } catch (Exception e) {
            generator = 1;
        }

        // lettura della chiave pubblica del client
        long clientPublicKey = 1;
        try {
            clientPublicKey = (Long) readInput();
        } catch (Exception e) {
            clientPublicKey = 1;
        }

        // calcolo chiave pubblica da condividere
        long publicKey = OurMath.modPow(generator, privateExp, OurMath.MOD);

        sendObject(publicKey);

        // calcolo della chiave segreta
        long privateKey = OurMath.modPow(clientPublicKey, privateExp, OurMath.MOD);

        // conversione in hexString
        key = Long.toHexString(privateKey);
    }

    public void removeClientFromStanza() throws Exception {
        semaphore.acquire();
        // stanza.addColor(color);
        semaphore.release();
    }

    public void disconnect() throws Exception {
        input.close();
        output.close();
        connection.close();
    }

    private Object readInput() throws Exception {
        return input.readObject();
    }

    public void sendObject(Object obj) {
        try {
            output.writeObject(obj);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
