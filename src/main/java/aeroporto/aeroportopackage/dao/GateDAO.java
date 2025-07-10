package aeroporto.aeroportopackage.dao;

import aeroporto.aeroportopackage.model.Gate;
import java.util.List;

/**
 * The interface Gate dao.
 */
public interface GateDAO {
    /**
     * Finds a gate by its number.
     *
     * @param numeroGate the numero gate
     * @return the gate
     */
    Gate findByNumero(int numeroGate);

    /**
     * Finds all the gates.
     *
     * @return the list
     */
    List<Gate> findAll();
}