package utility;

public enum Outcome {
    Op_ACK, // operazione avvenuta con successo

    // messaggi di errore per la registrazione
    USER, // utente gia' registrato
    PW, // pw non coincidenti
    end // chiudere la connessione
}
