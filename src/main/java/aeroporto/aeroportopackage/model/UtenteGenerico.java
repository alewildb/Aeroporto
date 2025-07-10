package aeroporto.aeroportopackage.model;

import java.util.Objects;

/**
 * Represent a standard non-admin user.
 * It extends the User class adding new information, such as personal data.
 */
public class UtenteGenerico extends Utente {
    /**
     * The user's name.
     */
    private String nome;
    /**
     * The user's surname.
     */
    private String cognome;
    /**
     * The user's document number.
     */
    private String numeroDocumento;

    /**
     * Instantiates a new Utente generico.
     *
     * @param id              the id
     * @param login           the login
     * @param password        the password
     * @param nome            the nome
     * @param cognome         the cognome
     * @param numeroDocumento the numero documento
     */
    public UtenteGenerico(int id, String login, String password, String nome, String cognome, String numeroDocumento) {
        super(id, login, password);
        this.nome = nome;
        this.cognome = cognome;
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getNome() { return nome; }

    /**
     * Gets surname.
     *
     * @return the surname
     */
    public String getCognome() { return cognome; }

    /**
     * Gets document number.
     *
     * @return the document number.
     */
    public String getNumeroDocumento() { return numeroDocumento; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UtenteGenerico that = (UtenteGenerico) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(cognome, that.cognome) &&
                Objects.equals(numeroDocumento, that.numeroDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nome, cognome, numeroDocumento);
    }
}