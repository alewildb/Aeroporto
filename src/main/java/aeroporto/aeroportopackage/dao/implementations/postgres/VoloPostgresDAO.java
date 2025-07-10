package aeroporto.aeroportopackage.dao.implementations.postgres;

import aeroporto.aeroportopackage.dao.VoloDAO;
import aeroporto.aeroportopackage.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Volo postgres dao.
 */
public class VoloPostgresDAO implements VoloDAO {
    /**
     * The database connection.
     */
    private final Connection conn;

    /**
     * Instantiates a new Volo postgres dao.
     * The constructor initializes the connection to the database, using a "singleton" method.
     *
     * @param connection the connection
     */
    public VoloPostgresDAO(Connection connection) {
        this.conn = connection;
    }

    /**
     * This function takes the data retrieved by the sql query and puts it into an object.
     * @param rs The ResultSet from the query.
     * @return A Volo object.
     * @throws SQLException if a database access error occurs.
     */
    private Volo mapRowToVolo(ResultSet rs) throws SQLException {
        String tipoVolo = rs.getString("tipo_volo");
        Volo volo;
        if ("PARTENZA".equals(tipoVolo)) {
            Gate gate = null;
            int gateId = rs.getInt("gate_id");
            if (!rs.wasNull()) {
                gate = new Gate(rs.getInt("numero_gate"));
                gate.setId(gateId);
            }
            volo = new VoloPartenza(
                    rs.getString("codice_univoco"),
                    rs.getString("compagnia_aerea"),
                    rs.getString("aeroporto_arrivo"),
                    rs.getInt("giorno_partenza"),
                    rs.getInt("mese_partenza"),
                    rs.getInt("anno_partenza"),
                    rs.getString("orario_previsto"),
                    rs.getInt("minuti_ritardo"),
                    gate);
        } else {
            volo = new VoloArrivo(
                    rs.getString("codice_univoco"),
                    rs.getString("compagnia_aerea"),
                    rs.getString("aeroporto_partenza"),
                    rs.getInt("giorno_partenza"),
                    rs.getInt("mese_partenza"),
                    rs.getInt("anno_partenza"),
                    rs.getString("orario_previsto"),
                    rs.getInt("minuti_ritardo"));
        }
        volo.setId(rs.getInt("volo_id"));
        volo.setStatoVolo(StatoVolo.valueOf(rs.getString("stato_volo")));
        return volo;
    }

    @Override
    public Volo findByCodice(String codiceVolo) {
        String sql = "SELECT v.volo_id, v.codice_univoco, v.compagnia_aerea, v.aeroporto_partenza, v.aeroporto_arrivo, " +
                "v.giorno_partenza, v.mese_partenza, v.anno_partenza, v.orario_previsto, v.minuti_ritardo, " +
                "v.stato_volo, v.tipo_volo, v.gate_id, g.numero_gate " +
                "FROM voli v LEFT JOIN gates g ON v.gate_id = g.gate_id WHERE v.codice_univoco = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codiceVolo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVolo(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca del volo per codice");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Volo> findAll() {
        List<Volo> voli = new ArrayList<>();
        String sql = "SELECT v.volo_id, v.codice_univoco, v.compagnia_aerea, v.aeroporto_partenza, v.aeroporto_arrivo, " +
                "v.giorno_partenza, v.mese_partenza, v.anno_partenza, v.orario_previsto, v.minuti_ritardo, " +
                "v.stato_volo, v.tipo_volo, v.gate_id, g.numero_gate " +
                "FROM voli v LEFT JOIN gates g ON v.gate_id = g.gate_id " +
                "ORDER BY v.anno_partenza DESC, v.mese_partenza DESC, v.giorno_partenza DESC, v.orario_previsto";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                voli.add(mapRowToVolo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore during il recupero di tutti i voli");
            e.printStackTrace();
        }
        return voli;
    }

    /**
     * Sets the parameters for a PreparedStatement for a Volo object.
     * @param pstmt The PreparedStatement to set parameters for.
     * @param volo The Volo object.
     * @throws SQLException if a database access error occurs.
     */
    private void setVoloStatementParameters(PreparedStatement pstmt, Volo volo) throws SQLException {
        pstmt.setString(1, volo.getCodiceUnivoco());
        pstmt.setString(2, volo.getCompagniaAerea());
        pstmt.setString(3, volo.getAeroportoPartenza());
        pstmt.setString(4, volo.getAeroportoArrivo());
        pstmt.setInt(5, volo.getGiornoPartenza());
        pstmt.setInt(6, volo.getMesePartenza());
        pstmt.setInt(7, volo.getAnnoPartenza());
        pstmt.setString(8, volo.getOrarioPrevisto());
        pstmt.setInt(9, volo.getMinutiRitardo());
        pstmt.setString(10, volo.getStatoVolo().name());

        if (volo instanceof VoloPartenza voloPartenza) {
            pstmt.setString(11, "PARTENZA");
            Gate gate = voloPartenza.getGate();
            if (gate != null && gate.getId() > 0) {
                pstmt.setInt(12, gate.getId());
            } else {
                pstmt.setNull(12, Types.INTEGER);
            }
        } else {
            pstmt.setString(11, "ARRIVO");
            pstmt.setNull(12, Types.INTEGER);
        }
    }

    @Override
    public void save(Volo volo) {
        String sql = "INSERT INTO voli (codice_univoco, compagnia_aerea, aeroporto_partenza, aeroporto_arrivo, " +
                "giorno_partenza, mese_partenza, anno_partenza, orario_previsto, minuti_ritardo, stato_volo, " +
                "tipo_volo, gate_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setVoloStatementParameters(pstmt, volo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during il salvataggio del volo: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Volo volo) {
        String sql = "UPDATE voli SET compagnia_aerea = ?, aeroporto_partenza = ?, aeroporto_arrivo = ?, " +
                "giorno_partenza = ?, mese_partenza = ?, anno_partenza = ?, orario_previsto = ?, minuti_ritardo = ?, " +
                "stato_volo = ?, tipo_volo = ?, gate_id = ? WHERE codice_univoco = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, volo.getCompagniaAerea());
            pstmt.setString(2, volo.getAeroportoPartenza());
            pstmt.setString(3, volo.getAeroportoArrivo());
            pstmt.setInt(4, volo.getGiornoPartenza());
            pstmt.setInt(5, volo.getMesePartenza());
            pstmt.setInt(6, volo.getAnnoPartenza());
            pstmt.setString(7, volo.getOrarioPrevisto());
            pstmt.setInt(8, volo.getMinutiRitardo());
            pstmt.setString(9, volo.getStatoVolo().name());

            if (volo instanceof VoloPartenza voloPartenza) {
                pstmt.setString(10, "PARTENZA");
                Gate gate = voloPartenza.getGate();
                if (gate != null && gate.getId() > 0) {
                    pstmt.setInt(11, gate.getId());
                } else {
                    pstmt.setNull(11, Types.INTEGER);
                }
            } else {
                pstmt.setString(10, "ARRIVO");
                pstmt.setNull(11, Types.INTEGER);
            }

            pstmt.setString(12, volo.getCodiceUnivoco()); // Imposta il codice_univoco per la clausola WHERE
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during l'aggiornamento del volo: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String codiceVolo) {
        String sql = "DELETE FROM voli WHERE codice_univoco = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codiceVolo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during l'eliminazione del volo: " + e.getMessage(), e);
        }
    }
}