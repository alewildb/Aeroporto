package aeroporto.aeroportoPackage.model;

import aeroporto.aeroportoPackage.controller.AppController; // Import AppController
import java.util.List;

public class Utente {
    protected String login;
    protected String password;

    public Utente(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Volo> visualizzaVoli(AppController appController){
        return appController.internalGetAllVoli();
    }
}