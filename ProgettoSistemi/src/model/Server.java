package model;

import java.awt.Color;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

	private ServerSocket server;
	private Socket connection;
	private Stanza currentStanza;
	private final int DEFAULT_MAX_CLIENTS = 3;
	private int stanzaCounter = 0; // contatore di stanze create
	private ArrayList<Stanza> stanze;

	// colori disponibili per i vari client
	public static final Color colors[] = { Color.red, Color.orange, Color.blue };

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
				System.out.print("Nuovo client connesso: ");

				currentStanza = getFirstFreeRoom();

				if (currentStanza == null) {
					currentStanza = createNewStanza();
				}

				new Connection(connection, currentStanza);

				System.out.println("stanza " + currentStanza.getNumeroStanza() + " (client attualmente connessi: "
						+ currentStanza.getConnectedClients() + ")");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
		tmp.chooseImage();
		stanze.add(tmp);
		return tmp;
	}

	public static void main(String[] args) {
		new Server();
	}

}
