package model.stages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Server;
import utility.Outcome;
import view.login.VLogin;
import view.login.VSignup;

public class LoginManager implements ActionListener {
    private StageManager stageManager;
    private VLogin login;
    private VSignup signup;

    public LoginManager(StageManager stageManager) {
        this.stageManager = stageManager;
        login = new VLogin();
        signup = new VSignup();

        initialize();
    }

    public void initialize() {
        login.setVisible(true);
        login.addListener(this);
        signup.addListener(this);

        login.clear();
        signup.clear();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        // PULSANTE LOGIN
        if (evt.getSource() == login.getBtnLogin()) {
            String user = login.getTxtUser().getText();
            String pw = toString(login.getTxtPw().getPassword());

            // TODO: encrypt dati

            // invio dati al server per login
            boolean outcome = Server.login(user, pw);

            if (outcome) {
                login.setVisible(false);
                // passaggio alla fase successiva
                stageManager.setClientUser(user);
                stageManager.roomStage();
            } else
                login.getLblError().setVisible(true);
        }

        // PULSANTE SIGNUP (DELLA LOGIN)
        if (evt.getSource() == login.getBtnSignup()) {
            login.clear();
            switchState();
        }

        // PULSANTE SIGNUP (DEL SIGNUP)
        if (evt.getSource() == signup.getBtnSignup()) {
            String user = signup.getTxtUser().getText();
            String pw = toString(signup.getTxtPw().getPassword());
            String confirm = toString(signup.getTxtConfirm().getPassword());

            // TODO: encrypt dati

            // invio dati al server per signup
            Outcome outcome = Server.signup(user, pw, confirm);
            switch (outcome) {
                case Op_ACK:
                    signup.clear();
                    switchState();
                    break;

                // visualizzo i messaggi di errore
                case user_taken:
                    signup.getLblError2().setVisible(false);
                    signup.getLblError1().setVisible(true);
                    break;
                case pw_doesnt_match:
                    signup.getLblError2().setVisible(true);
                    signup.getLblError1().setVisible(false);
                    break;
                default:
                    break;
            }

        }

        // PULSATE BACK
        if (evt.getSource() == signup.getBtnBack()) {
            signup.clear();
            switchState();
        }
    }

    // passa dalla view di login a quella di sign up
    private void switchState() {
        login.setVisible(!login.isVisible());
        signup.setVisible(!signup.isVisible());
    }

    // converte un array di chars in string
    // utilizzato per le passwords
    private String toString(char[] chars) {
        String string = "";
        for (int i = 0; i < chars.length; i++) {
            string += chars[i];
        }
        return string;
    }

}
