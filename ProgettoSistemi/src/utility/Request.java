package utility;

import java.io.Serializable;

// tipi di richiesta che il client puo' effettuare al server
public enum Request implements Serializable {
    login,
    signup,
}
