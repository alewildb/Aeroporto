package aeroporto.aeroportopackage.dao;

import aeroporto.aeroportopackage.model.Utente;
import aeroporto.aeroportopackage.model.UtenteGenerico;

/**
 * The interface Utente dao.
 */
public interface UtenteDAO {
    /**
     * Finds a user by its username and its password.
     * This function is used in the login function.
     * @param login    the login
     * @param password the password
     * @return the utente
     */
    Utente findByLoginAndPassword(String login, String password);

    /**
     * Finds a user by its username.
     *
     * @param login the login
     * @return the utente
     */
    Utente findByLogin(String login);

    /**
     * Finds a generic user from a given id.
     *
     * @param id the id
     * @return the utente generico
     */
    UtenteGenerico findGenericoById(int id);

    /**
     * Finds a generic user by its name, surname and document number.
     *
     * @param nome            the nome
     * @param cognome         the cognome
     * @param numeroDocumento the numero documento
     * @return the utente generico
     */
    UtenteGenerico findGenericoByDetails(String nome, String cognome, String numeroDocumento);

    /**
     * Inserts a user in the database. (Registration)
     *
     * @param nome            the nome
     * @param cognome         the cognome
     * @param numeroDocumento the numero documento
     * @param login           the login
     * @param password        the password
     */
    void save(String nome, String cognome, String numeroDocumento, String login, String password);
}