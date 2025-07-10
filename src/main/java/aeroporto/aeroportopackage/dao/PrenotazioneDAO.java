package aeroporto.aeroportopackage.dao;

import aeroporto.aeroportopackage.model.Prenotazione;
import aeroporto.aeroportopackage.model.StatoPrenotazione;
import aeroporto.aeroportopackage.model.UtenteGenerico;
import java.util.List;

/**
 * The interface Prenotazione dao.
 */
public interface PrenotazioneDAO {
    /**
     * Saves a booking in the database.
     *
     * @param prenotazione the prenotazione
     */
    void save(Prenotazione prenotazione);

    /**
     * Updates a booking in the database.
     *
     * @param prenotazioneId the prenotazione id
     * @param stato          the stato
     * @param posto          the posto
     */
    void update(int prenotazioneId, StatoPrenotazione stato, String posto);

    /**
     * Finds all bookings related to a user.
     *
     * @param utente the utente
     * @return the list
     */
    List<Prenotazione> findByUtente(UtenteGenerico utente);
}