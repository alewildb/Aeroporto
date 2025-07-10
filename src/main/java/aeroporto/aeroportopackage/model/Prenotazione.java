package aeroporto.aeroportopackage.model;

/**
 * Represents a flight booked by a user.
 * Contains information related to the passengers, the baggage and the flight.
 */
public class Prenotazione {
    /**
     * The booking identifier.
     */
    private int id;
    /**
     * The user who made the booking.
     */
    private UtenteGenerico utenteCheHaPrenotato;
    /**
     * The passenger's name.
     */
    private String nomePasseggero;
    /**
     * The passenger's surname.
     */
    private String cognomePasseggero;
    /**
     * The passenger's document number.
     */
    private String numeroDocumentoPasseggero;
    /**
     * The booking status.
     */
    private StatoPrenotazione statoPrenotazione = StatoPrenotazione.IN_ATTESA;
    /**
     * The ticket number.
     */
    private String numeroBiglietto;
    /**
     * The assigned seat.
     */
    private String postoAssegnato;
    /**
     * The flight code.
     */
    private String voloCodice;

    /**
     * Instantiates a new booking.
     */
    public Prenotazione() {
        // è vuoto.
    }

    /**
     * Gets id.
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
     * Gets the user who booked the flight.
     *
     * @return the user who booked the flight.
     */
    public UtenteGenerico getUtenteCheHaPrenotato() { return utenteCheHaPrenotato; }

    /**
     * Sets the user who booked the flight.
     *
     * @param utenteCheHaPrenotato the user who booked the flight.
     */
    public void setUtenteCheHaPrenotato(UtenteGenerico utenteCheHaPrenotato) { this.utenteCheHaPrenotato = utenteCheHaPrenotato; }

    /**
     * Gets the passenger's name.
     *
     * @return the passenger's name.
     */
    public String getNomePasseggero() { return nomePasseggero; }

    /**
     * Sets the passenger's name.
     *
     * @param nomePasseggero the passenger's name.
     */
    public void setNomePasseggero(String nomePasseggero) { this.nomePasseggero = nomePasseggero; }

    /**
     * Gets the passenger's surname.
     *
     * @return the passenger's surname.
     */
    public String getCognomePasseggero() { return cognomePasseggero; }

    /**
     * Sets the passenger's surname.
     *
     * @param cognomePasseggero the passenger's surname.
     */
    public void setCognomePasseggero(String cognomePasseggero) { this.cognomePasseggero = cognomePasseggero; }

    /**
     * Gets the passenger's document number.
     *
     * @return the passenger's document number.
     */
    public String getNumeroDocumentoPasseggero() { return numeroDocumentoPasseggero; }

    /**
     * Sets the passenger's document number.
     *
     * @param numeroDocumentoPasseggero the passenger's document number.
     */
    public void setNumeroDocumentoPasseggero(String numeroDocumentoPasseggero) { this.numeroDocumentoPasseggero = numeroDocumentoPasseggero; }

    /**
     * Gets the booking's status.
     *
     * @return the booking's status.
     */
    public StatoPrenotazione getStatoPrenotazione() { return statoPrenotazione; }

    /**
     * Sets the booking's status.
     *
     * @param statoPrenotazione the booking's status.
     */
    public void setStatoPrenotazione(StatoPrenotazione statoPrenotazione) { this.statoPrenotazione = statoPrenotazione; }

    /**
     * Gets the ticket number.
     *
     * @return the ticket number.
     */
    public String getNumeroBiglietto() { return numeroBiglietto; }

    /**
     * Sets the ticket number.
     *
     * @param numeroBiglietto the ticket number.
     */
    public void setNumeroBiglietto(String numeroBiglietto) { this.numeroBiglietto = numeroBiglietto; }

    /**
     * Gets the assigned seat in the airplane.
     *
     * @return the assigned seat in the airplane.
     */
    public String getPostoAssegnato() { return postoAssegnato; }

    /**
     * Sets the assigned seat in the airplane.
     *
     * @param postoAssegnato the assigned seat in the airplane.
     */
    public void setPostoAssegnato(String postoAssegnato) { this.postoAssegnato = postoAssegnato; }

    /**
     * Gets the flight id.
     *
     * @return the flight id.
     */
    public String getVoloCodice() { return voloCodice; }

    /**
     * Sets the flight id.
     *
     * @param voloCodice the flight id.
     */
    public void setVoloCodice(String voloCodice) { this.voloCodice = voloCodice; }
}