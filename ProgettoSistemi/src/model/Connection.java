package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import com.opencsv.CSVWriter;

import utility.Crypto;
import utility.LoginException;
import utility.OurMath;
import utility.Outcome;
import utility.Pair;
import utility.Request;

public class Connection extends Thread {
	private static Semaphore semaphore;
	private Socket connection;
	private Color color;
	private Dimension dimension;
	private Stanza stanza;

	private CounterThread counter;

	private ObjectInputStream input;
	private ObjectOutputStream output;

	private String key;

	private Object obj;

	public Connection(Socket connection, Stanza stanza) {

		try {

			this.stanza = stanza;
			this.connection = connection;
			this.dimension = Stanza.getClientDimension();
			this.color = stanza.getNewColor();

			Connection.semaphore = stanza.getSemaphore();
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

			handleLogin();

			// DA QUI IN POI IL CODICE E' COPIA INCOLLA DI QUELLO VECCHIO

			// passo al client la dimensione
			output.writeObject(dimension);

			// passo al client il numero di stanza
			semaphore.acquire();
			output.writeObject(stanza.getNumeroStanza());
			semaphore.release();

			// passo al client il colore
			output.writeObject(color);

			// passo al client l'immagine
			semaphore.acquire();
			output.writeObject(stanza.getChoseImagePath());
			semaphore.release();

			while (true) {
				// aspetto fino a quando la stanza non e' piena
				// oppure il client non invia un segnale di ready
				semaphore.acquire();
				if (stanza.isFull() || stanza.getGameStarted()) {
					semaphore.release();
					break;
				}
				semaphore.release();

				Object readySignal = input.readObject();

				if (readySignal instanceof Outcome) {
					if (readySignal == Outcome.ready)
						break;
					else
						output.writeObject(Outcome.notReady);
				}

				Thread.sleep(100);
			}

			// modifico la stanza dicendo che e' pronta a disegnare
			semaphore.acquire();
			stanza.setGameStarted(true);
			semaphore.release();

			// passo al client il segnale di start
			output.writeObject(Outcome.start);

			// inizializzo il counter
			semaphore.acquire();
			stanza.startCounter();
			counter = stanza.getCounter();
			semaphore.release();

			do {

				// il tempo e' scaduto
				if (counter.timeIsUp()) {
					semaphore.acquire();
					if (stanza.getCounter().isAlive())
						stanza.getCounter().interrupt();
					semaphore.release();
					System.out.println("Il client connesso alla porta " + connection.getPort() + " ha finito il tempo");
					break;
				}

				obj = input.readObject();
				semaphore.acquire();

				// il client sta disegnando
				if (obj instanceof Point) {

					Point p = (Point) obj;
					int size = Stanza.getClientCellSize();
					p.setLocation(p.x / size, p.y / size);

					if (isInBounds(p)) {
						stanza.getMatrix()[p.y][p.x] = color;
						output.writeObject(p);
					} else {
						output.writeObject("Out of bounds");
					}
				}

				// il client ha finito di disegnare
				if (obj instanceof Outcome) {
					if (obj == Outcome.finished) {
						semaphore.release();
						System.out.println(
								"Il client connesso alla porta " + connection.getPort() + " ha finito di disegnare");
						break;
					}
				}

				semaphore.release();
			} while (connection.isConnected());

			System.out.println("Il client connesso alla porta " + connection.getPort() + " si sta disconnettendo");

			// segnalo al client che effettivamente e' finito il gioco
			output.writeObject(Outcome.gameOver);

			// invio al client la matrice della stanza
			semaphore.acquire();
			output.writeObject(stanza.getMatrix());
			semaphore.release();

			// ho finito di offrire al client i servizi quindi attendo il suo Outcome.end
			while (true) {
				obj = input.readObject();
				if (obj instanceof Outcome) {
					if (obj == Outcome.end) {
						break;
					}
				}
			}

			removeClientFromStanza();
			disconnect();
		} catch (LoginException logEx) {
			try {
				System.out.println(logEx.getMessage());
				output.writeObject(Outcome.end);
				semaphore.release();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			try {
				System.out.println(e.getMessage());
				output.writeObject(Outcome.end);
				stanza.addColor(color);
				semaphore.release();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
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

	private void handleLogin() throws LoginException {
		try {
			boolean logged = false;
			do {
				Pair obj = (Pair) readInput();

				Request req = (Request) obj.first;
				ArrayList<String> data = (ArrayList<String>) obj.second;
				String user = data.get(0);
				String pw = data.get(1);

				user = Crypto.decrypt(user, key);
				pw = Crypto.decrypt(pw, key);

				if (req == Request.login) {
					Outcome outcome = login(user, pw);

					sendObject(outcome);

					if (outcome == Outcome.Op_ACK)
						logged = true;

				} else if (req == Request.signup) {
					String confirm = data.get(2);
					confirm = Crypto.decrypt(confirm, key);

					Outcome outcome = signup(user, pw, confirm);

					sendObject(outcome);
				}
			} while (!logged);
		} catch (Exception e) {
			throw new LoginException();
		}
	}

	// gestisce il login di un client
	// ritorna true se il login va a buon fine, false altrimenti
	public Outcome login(String userName, String pw) {
		// campi vuoti
		if (userName == "" || pw == "")
			return Outcome.Op_NACK;

		Pair<String, String> user = readUser(userName);

		// nessun utente trovato
		if (user == null)
			return Outcome.Op_NACK;

		// match della pw
		return Crypto.validatePassword(pw, user.second) ? Outcome.Op_ACK : Outcome.Op_NACK;
	}

	public Outcome signup(String userName, String pw, String confirm) {
		if (userName == "" || pw == "" || confirm == "")
			return Outcome.registration_failed;

		// pw non coincidenti
		if (pw.compareTo(confirm) != 0)
			return Outcome.pw_doesnt_match;

		Pair<String, String> user = readUser(userName);

		// utente gia' registrato
		if (user != null)
			return Outcome.user_taken;

		pw = Crypto.hash(pw);

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

	private boolean isInBounds(Point p) {
		return p.x >= 0 && p.y >= 0 && p.x < Stanza.getCol() && p.y < Stanza.getRow();
	}

	public void removeClientFromStanza() throws Exception {
		semaphore.acquire();
		stanza.addColor(color);
		semaphore.release();
	}

	public void disconnect() throws Exception {
		input.close();
		output.close();
		connection.close();
	}
}
