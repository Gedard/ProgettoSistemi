package model;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import utility.Outcome;

public class Room {
    private String id;
    private int n;
    private ArrayList<Client> clients;

    public Room(String id, int n) {
        this.id = id;
        this.n = n;
        clients = new ArrayList<>();
    }

    public Outcome addClient(Client client) {
        if (clients.size() == n)
            return Outcome.Op_NACK;

        clients.add(client);
        
        // aggiornamento delle view
        DefaultListModel<String> model = createList();
        for (Client c : clients) {
            c.updateCount(clients.size(), n);
            c.updateList(model);
        }

        return Outcome.Op_ACK;
    }

    private DefaultListModel<String> createList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Client client : clients)
            model.addElement(client.getUser());
        return model;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

}
