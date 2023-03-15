package model;

import java.io.Serializable;

public class Stanza implements Serializable {
    private final int maxClients; // numero massimo di client che questa stanza puo' gestire, passato dal server
    private int connectedClients;

    public Stanza(int maxClients, int numeroStanza) {
        this.maxClients = maxClients;
        connectedClients = 0;
    }

    public boolean isFull() {
        return connectedClients == maxClients;
    }

}
