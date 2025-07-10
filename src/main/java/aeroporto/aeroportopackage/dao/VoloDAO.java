package aeroporto.aeroportopackage.dao;

import aeroporto.aeroportopackage.model.Volo;
import java.util.List;

/**
 * The interface Volo dao.
 */
public interface VoloDAO {
    /**
     * Finds a flight by its code.
     *
     * @param codiceVolo the codice volo
     * @return the volo
     */
    Volo findByCodice(String codiceVolo);

    /**
     * Finds all flights.
     *
     * @return the list
     */
    List<Volo> findAll();

    /**
     * Inserts a flight in the database.
     *
     * @param volo the volo
     */
    void save(Volo volo);

    /**
     * Updates a flight in the database.
     *
     * @param volo the volo
     */
    void update(Volo volo);

    /**
     * Deletes a flight from the database.
     *
     * @param codiceVolo the codice volo
     */
    void delete(String codiceVolo);
}