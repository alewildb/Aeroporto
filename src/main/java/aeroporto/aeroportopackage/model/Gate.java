package aeroporto.aeroportopackage.model;

import java.util.Objects;

/**
 * Represents an airport's boarding gate.
 */
public class Gate {
    /**
     * The gate identifier.
     */
    private int id;
    /**
     * The gate number.
     */
    private int numeroGate;

    /**
     * Instantiates a new Gate.
     *
     * @param numeroGate the gate number.
     */
    public Gate(int numeroGate) {
        this.numeroGate = numeroGate;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets gate number.
     *
     * @return the gate number.
     */
    public int getNumeroGate() {
        return numeroGate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gate gate = (Gate) o;
        return id == gate.id && numeroGate == gate.numeroGate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroGate);
    }
}