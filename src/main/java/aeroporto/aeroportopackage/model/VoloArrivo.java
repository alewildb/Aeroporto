package aeroporto.aeroportopackage.model;

/**
 * The class that represents an arriving flight.
 * Extends the flight (Volo) class.
 */
public class VoloArrivo extends Volo {
    /**
     * Instantiates a new Volo arrivo.
     * Sets the destination airport to "Napoli".
     *
     * @param codiceUnivoco     the univocal id of the flight.
     * @param compagniaAerea    the flight company.
     * @param aeroportoPartenza the departure airport.
     * @param giornoPartenza    the arrival/departure day.
     * @param mesePartenza      the arrival/departure month.
     * @param annoPartenza      the arrival/departure year.
     * @param orarioPrevisto    the expected time of arrival/departure.
     * @param minutiRitardo     the delay minutes.
     */
    @SuppressWarnings("java:S107")
    public VoloArrivo(String codiceUnivoco, String compagniaAerea, String aeroportoPartenza, int giornoPartenza, int mesePartenza, int annoPartenza, String orarioPrevisto, int minutiRitardo) {
        super(codiceUnivoco, compagniaAerea, aeroportoPartenza, "Napoli",
                giornoPartenza, mesePartenza, annoPartenza, orarioPrevisto, minutiRitardo);
    }
}