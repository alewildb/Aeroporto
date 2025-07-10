package aeroporto.aeroportopackage.model;

/**
 * The enum that indicates the possible states of a booking.
 */
public enum StatoPrenotazione {
    /**
     * The booking is confirmed (Checked-in).
     */
    CONFERMATA,
    /**
     * The booking is created but not confirmed or cancelled yet.
     */
    IN_ATTESA,
    /**
     * The booking has been cancelled.
     */
    CANCELLATA
}