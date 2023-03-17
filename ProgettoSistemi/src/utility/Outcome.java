package utility;

public enum Outcome {
    Op_ACK, // operazione avvenuta con successo
    Op_NACK, // errore durante l'operazione

    end, // termine connessione

    // messaggi di errore per la registrazione
    user_taken, // utente gia' registrato (o stringa vuota)
    pw_doesnt_match, // pw non coincidenti
}
