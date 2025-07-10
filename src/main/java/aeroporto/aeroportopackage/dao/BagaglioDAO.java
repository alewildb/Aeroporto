package aeroporto.aeroportopackage.dao;

import aeroporto.aeroportopackage.model.Bagaglio;
import aeroporto.aeroportopackage.model.StatoBagaglio;
import java.util.List;

/**
 * The interface Bagaglio dao.
 */
public interface BagaglioDAO {
    /**
     * Finds baggage by its id.
     *
     * @param bagaglioId the bagaglio id
     * @return the bagaglio
     */
    Bagaglio findById(int bagaglioId);

    /**
     * Finds all baggage.
     *
     * @return the list
     */
    List<Bagaglio> findAll();

    /**
     * Finds all baggage related to a booking.
     *
     * @param prenotazioneId the prenotazione id
     * @return the list
     */
    List<Bagaglio> findByPrenotazioneId(int prenotazioneId);

    /**
     * Finds all lost baggage.
     *
     * @return the list
     */
    List<Bagaglio> findSmarriti();

    /**
     * Inserts a baggage into the database.
     *
     * @param prenotazioneId the prenotazione id
     * @param stato          the stato
     * @return the int
     */
    int save(int prenotazioneId, StatoBagaglio stato);

    /**
     * Updates a baggage in the database.
     *
     * @param bagaglioId the bagaglio id
     * @param stato      the stato
     */
    void update(int bagaglioId, StatoBagaglio stato);

    /**
     * Deletes all baggage related to a booking.
     *
     * @param prenotazioneId the prenotazione id
     */
    void deleteByPrenotazioneId(int prenotazioneId); // NUOVO METODO
}