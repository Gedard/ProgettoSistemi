package utility;

public enum Outcome {
	finished, // il client ha finito di disegnare
	ready, // il client non ha ancora cliccato il bottone per iniziare
	notReady, // il client non ha ancora cliccato il bottone per iniziare
	start, // inizia il disegno
	end, // il server ha finito di offrire il servizio di disegno al client
	gameOver, // gameOver -> il server ha finito di offrire il servizio di disegno al client
	Op_ACK, // operazione avvenuta con successo
	Op_NACK, // errore durante l'operazione
	// messaggi di errore per la registrazione
	user_taken, // utente gia' registrato (o stringa vuota)
	pw_doesnt_match, // pw non coincidenti
	registration_failed // errore generico in registrazione
}
