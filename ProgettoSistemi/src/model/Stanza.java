package model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import view.MatrixPanel;
import view.Panel;
import view.View;

public class Stanza implements Serializable {
	private final int maxClients; // numero massimo di client che questa stanza puo' gestire, passato dal server
	private int connectedClients;
	private Semaphore semaphore;
	private int numeroStanza; // valore assegnato dal server che aiuta ad identificare la stanza

	// quando un client clicca il bottone di inzio gioco,
	// tramite la connection questa variabile
	// verra' settata a true
	private boolean gameStarted = false;

	private ArrayList<Color> colors = new ArrayList<>(); // colori da assegnare a un client quando si connette

	private static int row = 90;
	private static int col = 130;
	private static int clientCellSize = 3;
	private static int stanzaCellSize = 7;
	private static Dimension stanzaDimension = new Dimension(col * stanzaCellSize, row * stanzaCellSize);
	private static Dimension clientDimension = new Dimension(col * clientCellSize, row * clientCellSize);

	private View view;
	private Panel panel;

	private ArrayList<String> imagePaths; // utilizzo il percorso file dell'immagine perche' l'oggetto Image non e'
											// Serializable, quindi non potrei passarlo al client
	private String choseImagePath = null; // l'immagine per ogni stanza deve essere solo una tra quelle proposte
	private Color[][] matrix = new Color[row][col];

	private CounterThread counter = null;
	private final int SECONDS_TO_WAIT = 9; // sono i secondi per visualizzare la view completa
	private final int SECONDS_TO_DRAW = 30; // sono i secondi per disegnare
	private int secondsToDraw = SECONDS_TO_WAIT + SECONDS_TO_DRAW; // sono i secondi effettivi da contare

	public Stanza(int maxClients, int numeroStanza) {
		this.maxClients = maxClients;
		connectedClients = 0;
		this.numeroStanza = numeroStanza;
		semaphore = new Semaphore(1);
		loadColors();
		initializeMatrix();
		loadImages();
		createView();
	}

	public void initializeMatrix() {
		for (int i = 0; i < row; ++i) {
			for (int j = 0; j < col; ++j) {
				matrix[i][j] = Color.white;
			}
		}
	}

	private void createView() {
		view = new View(stanzaDimension, "Stanza " + numeroStanza);
		panel = new MatrixPanel(stanzaDimension, matrix, row, col);
		view.addPanel(panel);
	}

	// caricamento dinamico dei colori, in riferimento ai colori disponibili dal
	// server
	// e al numero di client massimi per questa stanza
	private void loadColors() {
		for (int i = 0; i < maxClients; ++i) {
			colors.add(Server.colors[i]);
		}
	}

	private void loadImages() {
		imagePaths = new ArrayList<>();

		imagePaths.add("/images/image1");
		imagePaths.add("/images/image2");
		imagePaths.add("/images/image3");
	}

	public void chooseImage() {
		if (choseImagePath == null) {
			int idx = (int) (Math.random() * imagePaths.size());
			choseImagePath = imagePaths.get(idx);
		}
	}

	public boolean isFull() {
		return connectedClients == maxClients;
	}

	public Color getNewColor() {
		if (isFull())
			return null;
		connectedClients++;

		return colors.remove(0);
	}

	public void startCounter() {
		if (counter == null)
			counter = new CounterThread(secondsToDraw);
	}

	public CounterThread getCounter() {
		return counter;
	}

	public void setCounter(CounterThread counter) {
		this.counter = counter;
	}

	public void setGameStarted(boolean b) {
		this.gameStarted = b;
	}

	public boolean getGameStarted() {
		return this.gameStarted;
	}

	public Panel getPanel() {
		return panel;
	}

	public void addColor(Color color) {
		connectedClients--;
		colors.add(color);
	}

	public int getNumeroStanza() {
		return numeroStanza;
	}

	public int getMaxClients() {
		return maxClients;
	}

	public int getConnectedClients() {
		return connectedClients;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public ArrayList<Color> getColors() {
		return colors;
	}

	public Color[][] getMatrix() {
		return matrix;
	}

	public static Dimension getStanzaDimension() {
		return stanzaDimension;
	}

	public static Dimension getClientDimension() {
		return clientDimension;
	}

	public static int getRow() {
		return row;
	}

	public static int getCol() {
		return col;
	}

	public static int getClientCellSize() {
		return clientCellSize;
	}

	public String getChoseImagePath() {
		return choseImagePath;
	}
}
