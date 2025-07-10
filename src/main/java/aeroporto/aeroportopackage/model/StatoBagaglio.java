package aeroporto.aeroportopackage.model;

/**
 * The enum that indicates the possible states for baggage.
 */
public enum StatoBagaglio {
    /**
     * The baggage is ready to be collected.
     */
    RITIRO_DISPONIBILE,
    /**
     * The baggage is loaded up on the plane.
     */
    CARICATO_AEREO,
    /**
     * The baggage has been flagged as lost.
     */
    SMARRITO
}