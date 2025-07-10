package aeroporto.aeroportopackage.model;

/**
 * Baggage in the system.
 * Each baggage is identified by an id, a state and is associated to a booking
 */
public class Bagaglio {
    /**
     * The baggage identifier.
     */
    private int codiceBagaglio; // Questo funge da ID
    /**
     * The state of the baggage.
     */
    private StatoBagaglio statoBagaglio;
    /**
     * The booking associated with the baggage.
     */
    private Prenotazione prenotazione;

    /**
     * Instantiates a new Bagaglio.
     *
     * @param codiceBagaglio the baggage id
     * @param prenotazione   the associated booking
     */
    public Bagaglio(int codiceBagaglio, Prenotazione prenotazione) {
        this.codiceBagaglio = codiceBagaglio;
        this.prenotazione = prenotazione;
    }

    /**
     * Instantiates a new Bagaglio.
     *
     * @param codiceBagaglio the baggage id
     */
    public Bagaglio(int codiceBagaglio) {
        this.codiceBagaglio = codiceBagaglio;
    }

    /**
     * Gets baggage id.
     *
     * @return the baggage id
     */
    public int getCodiceBagaglio() { return codiceBagaglio; }

    /**
     * Gets baggage state.
     *
     * @return the baggage state.
     */
    public StatoBagaglio getStatoBagaglio() { return statoBagaglio; }

    /**
     * Sets baggage state.
     *
     * @param statoBagaglio the baggage state.
     */
    public void setStatoBagaglio(StatoBagaglio statoBagaglio) { this.statoBagaglio = statoBagaglio; }

    /**
     * Gets the associated booking.
     *
     * @return the the associated booking.
     */
    public Prenotazione getPrenotazione() { return prenotazione; }

    /**
     * Sets the associated booking.
     *
     * @param prenotazione the associated booking.
     */
    public void setPrenotazione(Prenotazione prenotazione) { this.prenotazione = prenotazione; }
}