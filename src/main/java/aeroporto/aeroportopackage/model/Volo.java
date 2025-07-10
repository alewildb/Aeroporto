package aeroporto.aeroportopackage.model;

/**
 * The abstract class that represents a flight.
 * Contains every common data between arriving and departing flights.
 */
public abstract class Volo {
    /**
     * The flight identifier.
     */
    private int id;
    /**
     * The univocal id.
     */
    protected String codiceUnivoco;
    /**
     * The flight company.
     */
    protected String compagniaAerea;
    /**
     * The departing airport.
     */
    protected String aeroportoPartenza;
    /**
     * The destination airport.
     */
    protected String aeroportoArrivo;
    /**
     * The day of the departure/arrival.
     */
    protected int giornoPartenza;
    /**
     * The month of the departure/arrival.
     */
    protected int mesePartenza;
    /**
     * The year of the departure/arrival.
     */
    protected int annoPartenza;
    /**
     * The expected time of departure / arrival.
     */
    protected String orarioPrevisto;
    /**
     * The minutes of the flight's delay.
     */
    protected int minutiRitardo;
    /**
     * The flight state.
     */
    protected StatoVolo statoVolo = StatoVolo.PROGRAMMATO;

    /**
     * Instantiates a new Volo.
     *
     * @param codiceUnivoco     the univocal id of the flight.
     * @param compagniaAerea    the flight company.
     * @param aeroportoPartenza the departure airport.
     * @param aeroportoArrivo   the destination airport.
     * @param giornoPartenza    the arrival/departure day.
     * @param mesePartenza      the arrival/departure month.
     * @param annoPartenza      the arrival/departure year.
     * @param orarioPrevisto    the expected time of arrival/departure.
     * @param minutiRitardo     the delay minutes.
     */
    @SuppressWarnings("java:S107")
    protected Volo(String codiceUnivoco, String compagniaAerea, String aeroportoPartenza, String aeroportoArrivo, int giornoPartenza, int mesePartenza, int annoPartenza, String orarioPrevisto, int minutiRitardo) {
        this.codiceUnivoco = codiceUnivoco;
        this.compagniaAerea = compagniaAerea;
        this.aeroportoPartenza = aeroportoPartenza;
        this.aeroportoArrivo = aeroportoArrivo;
        this.giornoPartenza = giornoPartenza;
        this.mesePartenza = mesePartenza;
        this.annoPartenza = annoPartenza;
        this.orarioPrevisto = orarioPrevisto;
        this.minutiRitardo = minutiRitardo;
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
     * Gets the univocal id of the flight.
     *
     * @return the univocal id of the flight.
     */
    public String getCodiceUnivoco() { return codiceUnivoco; }

    /**
     * Gets the flight company.
     *
     * @return the flight company.
     */
    public String getCompagniaAerea() { return compagniaAerea; }

    /**
     * Gets the departure airport.
     *
     * @return the departure airport.
     */
    public String getAeroportoPartenza() { return aeroportoPartenza; }

    /**
     * Gets the destination airport.
     *
     * @return the destination airport.
     */
    public String getAeroportoArrivo() { return aeroportoArrivo; }

    /**
     * Gets the arrival/departure day.
     *
     * @return the arrival/departure day.
     */
    public int getGiornoPartenza() { return giornoPartenza; }

    /**
     * Gets the arrival/departure month.
     *
     * @return the arrival/departure month.
     */
    public int getMesePartenza() { return mesePartenza; }

    /**
     * Gets the arrival/departure year.
     *
     * @return the arrival/departure year.
     */
    public int getAnnoPartenza() { return annoPartenza; }

    /**
     * Gets the expected time of arrival/departure.
     *
     * @return the expected time of arrival/departure.
     */
    public String getOrarioPrevisto() { return orarioPrevisto; }

    /**
     * Gets the delay minutes.
     *
     * @return the delay minutes.
     */
    public int getMinutiRitardo() { return minutiRitardo; }

    /**
     * Gets the flight's state.
     *
     * @return the flight's state.
     */
    public StatoVolo getStatoVolo() { return statoVolo; }

    /**
     * Sets the flight's state.
     *
     * @param statoVolo the flight's state.
     */
    public void setStatoVolo(StatoVolo statoVolo) { this.statoVolo = statoVolo; }
}