package aeroporto.aeroportopackage.model;

/**
 * The enum that indicates the possible states of a flight.
 */
public enum StatoVolo {
    /**
     * The flight is scheduled as programmed.
     */
    PROGRAMMATO,
    /**
     * The flight has departed.
     */
    DECOLLATO,
    /**
     * The flight is late.
     */
    IN_RITARDO,
    /**
     * The flight landed at its destination successfully.
     */
    ATTERRATO,
    /**
     * The flight has been cancelled.
     */
    CANCELLATO
}