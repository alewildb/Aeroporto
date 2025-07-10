package aeroporto.aeroportopackage.dao.implementations.postgres;

import aeroporto.aeroportopackage.dao.GateDAO;
import aeroporto.aeroportopackage.model.Gate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Gate postgres dao.
 */
public class GatePostgresDAO implements GateDAO {
    /**
     * The database connection.
     */
    private final Connection conn;

    /**
     * Instantiates a new Gate postgres dao.
     * The constructor initializes the connection to the database, using a "singleton" method.
     *
     * @param connection the connection
     */
    public GatePostgresDAO(Connection connection) {
        this.conn = connection;
    }

    @Override
    public Gate findByNumero(int numeroGate) {
        String sql = "SELECT * FROM gates WHERE numero_gate = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numeroGate);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Gate gate = new Gate(rs.getInt("numero_gate"));
                    gate.setId(rs.getInt("gate_id"));
                    return gate;
                }
            }
        } catch(SQLException e) {
            System.err.println("Errore during la ricerca del gate per numero");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Gate> findAll() {
        List<Gate> gates = new ArrayList<>();
        String sql = "SELECT * FROM gates ORDER BY numero_gate";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Gate gate = new Gate(rs.getInt("numero_gate"));
                gate.setId(rs.getInt("gate_id"));
                gates.add(gate);
            }
        } catch (SQLException e) {
            System.err.println("Errore during il recupero di tutti i gate");
            e.printStackTrace();
        }
        return gates;
    }
}