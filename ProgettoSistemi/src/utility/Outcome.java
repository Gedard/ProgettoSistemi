package utility;

public enum Outcome {
    Op_ACK, // operazione avvenuta con successo
    Op_NACK, // errore durante l'operazione

    // messaggi di errore per la registrazione
    user_taken, // utente gia' registrato (o stringa vuota)
    pw_doesnt_match, // pw non coincidenti

    // messaggi per la gestione della stanza
    room_taken, // stanza gia' esistente (o stringa vuota)
    room_doesnt_exit // stanza non esistente
}
