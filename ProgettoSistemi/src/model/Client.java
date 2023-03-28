package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import utility.Crypto;
import utility.OurMath;
import utility.Outcome;
import utility.Pair;
import utility.Request;
import view.ClientView;
import view.ImagePanel;
import view.MatrixPanel;
import view.Panel;

public class Client implements MouseMotionListener, ActionListener {

	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	private ClientView view;
	private Dimension size;
	private int nStanza;
	private String key;

	private LoginM loginManager = null;
	private boolean logged = false;

	private Color[][] matrix;
	private Color color;
	private BufferedImage image;
	private JButton startButton, finishButton;

	private boolean isReady = false;
	private final String WAITING_MESSAGE = "In attesa di altri giocatori";

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

				loginSetup();

				do {
					Thread.sleep(1000);
				} while (!logged);

				// DA QUI IN POI IL CODICE E' COPIA INCOLLA DI QUELLO VECCHIO

				matrix = new Color[Stanza.getRow()][Stanza.getCol()];
				initializeMatrix();

				Object obj = readInput();

				// setto la dimensione del client
				if (obj instanceof Dimension) {
					this.size = (Dimension) obj;
				}

				obj = readInput();

				// setto il numero di stanza (utile solo per il titolo della view)
				if (obj instanceof Integer) {
					this.nStanza = (Integer) obj;
				}

				createWindow();

				obj = readInput();

				// setto il colore
				if (obj instanceof Color) {
					color = (Color) obj;
				}

				obj = readInput();

				// ricevo il percorso file dell'immagine da disegnare
				if (obj instanceof String) {
					// quindi ricavo l'immagine
					String path = (String) obj;
					image = ImageIO.read(new FileInputStream("ProgettoSistemi\\res" + path + ".png"));
				}

				// aspetto fino a quando non ricevo un segnale di start
				while (true) {
					if (!isReady) {
						output.writeObject(Outcome.notReady);
					} else {
						output.writeObject(Outcome.ready);
					}

					obj = readInput();

					if (obj instanceof Outcome)
						if ((Outcome) obj == Outcome.start) {
							isReady = true;
							break;
						}
				}

				view.setMsgColor(color);
				view.setMsg("Questo e' il tuo colore");
				Thread.sleep(1000);

				modifyWindow();

				do {
					obj = readInput();

					// ricevo il punto da colorare
					if (obj instanceof Point) {
						Point p = (Point) obj;
						matrix[p.y][p.x] = color;
					}

					// gestisco l'esito
					if (obj instanceof Outcome) {
						if ((Outcome) obj == Outcome.gameOver)
							break;
					}

				} while (connection.isConnected());

				obj = readInput();

				// ricevo la matrice finale
				if (obj instanceof Color[][]) {
					Color[][] matrice = (Color[][]) obj;
					for (int i = 0; i < matrice.length; ++i) {
						for (int j = 0; j < matrice[i].length; ++j) {
							this.matrix[i][j] = matrice[i][j];
						}
					}
				}

				// visualizza la matrice finale,
				// ultimo stage della ClientView
				finalWindow();

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

	// primo stage: fase login
	public void loginSetup() {
		if (loginManager == null)
			loginManager = new LoginM(this);
		else
			loginManager.initialize();
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
			return Outcome.registration_failed;
		}
	}

	// readObject "filtrata":
	// funziona come una semplice readObject, solo che quando viene letto
	// un Outcome.end (che significa chiudere la connessione e basta, qualunque cosa
	// sia successa)
	// gestisce subito il problema di chiusura della connessione per evitare errori
	// ed e' molto piu' comodo far cosi' al posto di dover
	// mettere il controllo dopo ogni readObject
	public Object readInput() throws Exception {
		Object obj = ""; // sarebbe meglio istanziarlo a null e gestire
		// quel caso da altre parti
		// ma per ora mettendo stringa vuota non dovrebbero esserci problemi
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

	public void initializeMatrix() {
		for (int i = 0; i < Stanza.getRow(); ++i) {
			for (int j = 0; j < Stanza.getCol(); ++j) {
				matrix[i][j] = Color.white;
			}
		}
	}

	private void finalWindow() {
		view.setMsgColor(Color.black);
		view.setMsg("Ecco il risultato");
		view.removeButton();
	}

	private void createWindow() {
		view = new ClientView(size, "Client " + connection.getLocalPort() + " Stanza " + nStanza);
		view.setMsg(WAITING_MESSAGE, true);

		startButton = new JButton("Inizia");
		startButton.addActionListener(this);
		view.setButton(startButton);
	}

	// rimuove i pannelli precedenti
	// aggiunge un pannello di messaggio
	// visualizza l'immagine
	// aggiunge un pannello con il MouseMotionListener
	private void modifyWindow() throws Exception {
		Panel panel = null;

		view.removeButton();
		view.setMsg("La partita sta per cominciare", true);

		synchronized (this) {
			wait(2000); // aspetto 2 secondi, in seguito ai quali visualizzo l'immagine

			int count = 5;
			view.setMsg("Memorizza l'immagine");
			panel = new ImagePanel(size, image);
			view.setImgPanel((ImagePanel) panel);

			do { // aspetto 'count' secondi, in seguito ai quali inizio il gioco
				view.setMsg("Memorizza l'immagine (" + count + ")");
				wait(1000);
				count--;
			} while (count >= 0);
		}

		finishButton = new JButton("Finisci");
		finishButton.addActionListener(this);
		view.setButton(finishButton);

		view.setMsgColor(color);
		view.setMsg("E ora disegna!");
		view.removeImagePanel();
		panel = new MatrixPanel(size, this.matrix, Stanza.getRow(), Stanza.getCol());
		panel.addMouseMotionListener(this);
		view.setMatrixPanel((MatrixPanel) panel);
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		sendObject(evt.getPoint());
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == startButton) {
			isReady = true;
			System.out.println("Inizia adesso");
		}

		if (evt.getSource() == finishButton) {
			sendObject(Outcome.finished);
			System.out.println("Finisci adesso");
		}
	}

	public void sendObject(Object obj) {
		try {
			output.writeObject(obj);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	@Override
	public void mouseMoved(MouseEvent evt) {

	}

	public static void main(String[] args) {
		new Client();
	}

}
