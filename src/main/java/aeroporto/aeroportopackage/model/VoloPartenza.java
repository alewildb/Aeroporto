package aeroporto.aeroportopackage.model;

/**
 * The class that represents a departing flight.
 * Extends the flight (Volo) class.
 * Adds the boarding gate attribute.
 */
public class VoloPartenza extends Volo {
    /**
     * The boarding gate.
     */
    private Gate gate;

    /**
     * Instantiates a new Volo partenza.
     * Sets the departure airport to "Napoli".
     *
     * @param codiceUnivoco     the univocal id of the flight.
     * @param compagniaAerea    the flight company.
     * @param aeroportoArrivo   the destination airport.
     * @param giornoPartenza    the arrival/departure day.
     * @param mesePartenza      the arrival/departure month.
     * @param annoPartenza      the arrival/departure year.
     * @param orarioPrevisto    the expected time of arrival/departure.
     * @param minutiRitardo     the delay minutes.
     * @param gate              the boarding gate.
     */
    @SuppressWarnings("java:S107")
    public VoloPartenza(String codiceUnivoco, String compagniaAerea, String aeroportoArrivo, int giornoPartenza, int mesePartenza, int annoPartenza, String orarioPrevisto, int minutiRitardo, Gate gate) {
        super(codiceUnivoco, compagniaAerea, "Napoli", aeroportoArrivo,
                giornoPartenza, mesePartenza, annoPartenza, orarioPrevisto, minutiRitardo);
        this.gate = gate;
    }

    /**
     * Gets the boarding gate.
     *
     * @return the boarding gate
     */
    public Gate getGate() { return gate; }
}