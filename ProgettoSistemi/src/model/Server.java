package model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    private ServerSocket server;
    private Socket connection;
    private ArrayList<Room> rooms;

    public Server() {
        try {
            server = new ServerSocket(8080);
            rooms = new ArrayList<>();

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
                System.out.println("Client connesso");
                new Connection(this, connection);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
