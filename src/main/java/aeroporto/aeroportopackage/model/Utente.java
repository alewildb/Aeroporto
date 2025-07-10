package aeroporto.aeroportopackage.model;

import java.util.Objects;

/**
 * The abstract class for all the users in the system.
 * It contains crucial information such as username and password.
 */
public class Utente {
    /**
     * The user identifier.
     */
    private int id;
    /**
     * The Login.
     */
    protected String login;
    /**
     * The Password.
     */
    protected String password;

    /**
     * Instantiates a new Utente.
     *
     * @param login    the login
     * @param password the password
     */
    public Utente(String login, String password){
        this.login = login;
        this.password = password;
    }

    /**
     * Instantiates a new Utente.
     *
     * @param id       the id
     * @param login    the login
     * @param password the password
     */
    public Utente(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    /**
     * Gets  id.
     *
     * @return the id
     */
    public int getId() { return id; }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getLogin() { return login; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return id == utente.id && Objects.equals(login, utente.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}