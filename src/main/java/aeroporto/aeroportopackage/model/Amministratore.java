package aeroporto.aeroportopackage.model;

/**
 * An Admin user, it does not add anything from the User class.
 * This class is crucial in the system because it cannot function without admins.
 */
public class Amministratore extends Utente {
    /**
     * Instantiates a new Amministratore.
     *
     * @param id       the id
     * @param login    the login
     * @param password the password
     */
    public Amministratore(int id, String login, String password) {
        super(id, login, password);
    }
}