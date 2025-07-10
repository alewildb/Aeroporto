package aeroporto.aeroportopackage.dao.implementations.postgres;

import aeroporto.aeroportopackage.dao.BagaglioDAO;
import aeroporto.aeroportopackage.model.Bagaglio;
import aeroporto.aeroportopackage.model.Prenotazione;
import aeroporto.aeroportopackage.model.StatoBagaglio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Bagaglio postgres dao.
 */
public class BagaglioPostgresDAO implements BagaglioDAO {
    /**
     * The database connection.
     */
    private final Connection conn;

    /**
     * Instantiates a new Bagaglio postgres dao.
     * The constructor initializes the connection to the database, using a "singleton" method.
     *
     * @param connection the connection
     */
    public BagaglioPostgresDAO(Connection connection) {
        this.conn = connection;
    }

    /**
     * This function takes the data retrieved by the sql query and puts it into an object.
     * @param rs The ResultSet from the query.
     * @return A Bagaglio object.
     * @throws SQLException if a database access error occurs.
     */
    private Bagaglio mapRowToBagaglio(ResultSet rs) throws SQLException {
        Bagaglio b = new Bagaglio(rs.getInt("bagaglio_id"));
        b.setStatoBagaglio(StatoBagaglio.valueOf(rs.getString("stato_bagaglio")));
        return b;
    }

    /**
     * This function takes the data retrieved by the sql query and puts it into an object.
     * @param rs The ResultSet from the query.
     * @return A Bagaglio object with its associated Prenotazione.
     * @throws SQLException if a database access error occurs.
     */
    private Bagaglio mapJoinedRowToBagaglio(ResultSet rs) throws SQLException {
        Prenotazione p = new Prenotazione();
        p.setId(rs.getInt("prenotazione_id"));
        p.setNumeroBiglietto(rs.getString("numero_biglietto"));
        p.setNomePasseggero(rs.getString("nome_passeggero"));
        p.setCognomePasseggero(rs.getString("cognome_passeggero"));
        p.setVoloCodice(rs.getString("volo_codice"));

        Bagaglio b = mapRowToBagaglio(rs);
        b.setPrenotazione(p);
        return b;
    }

    @Override
    public Bagaglio findById(int bagaglioId) {
        String sql = "SELECT b.bagaglio_id, b.stato_bagaglio, p.prenotazione_id, p.numero_biglietto, p.nome_passeggero, p.cognome_passeggero, p.volo_codice " +
                "FROM bagagli b JOIN prenotazioni p ON b.prenotazione_id = p.prenotazione_id WHERE b.bagaglio_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bagaglioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapJoinedRowToBagaglio(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca del bagaglio per ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Bagaglio> findAll() {
        List<Bagaglio> bagagli = new ArrayList<>();
        String sql = "SELECT b.bagaglio_id, b.stato_bagaglio, p.prenotazione_id, p.numero_biglietto, p.nome_passeggero, p.cognome_passeggero, p.volo_codice " +
                "FROM bagagli b JOIN prenotazioni p ON b.prenotazione_id = p.prenotazione_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bagagli.add(mapJoinedRowToBagaglio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore during il recupero di tutti i bagagli");
            e.printStackTrace();
        }
        return bagagli;
    }

    @Override
    public List<Bagaglio> findByPrenotazioneId(int prenotazioneId) {
        List<Bagaglio> bagagli = new ArrayList<>();
        String sql = "SELECT * FROM bagagli WHERE prenotazione_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prenotazioneId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bagagli.add(mapRowToBagaglio(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca dei bagagli per ID prenotazione");
            e.printStackTrace();
        }
        return bagagli;
    }

    @Override
    public List<Bagaglio> findSmarriti() {
        List<Bagaglio> bagagli = new ArrayList<>();
        String sql = "SELECT b.bagaglio_id, b.stato_bagaglio, p.prenotazione_id, p.numero_biglietto, p.nome_passeggero, p.cognome_passeggero, p.volo_codice " +
                "FROM bagagli b JOIN prenotazioni p ON b.prenotazione_id = p.prenotazione_id WHERE b.smarrito = TRUE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bagagli.add(mapJoinedRowToBagaglio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errore during il recupero dei bagagli smarriti");
            e.printStackTrace();
        }
        return bagagli;
    }

    @Override
    public int save(int prenotazioneId, StatoBagaglio stato) {
        String sql = "INSERT INTO bagagli(prenotazione_id, stato_bagaglio, smarrito) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, prenotazioneId);
            pstmt.setString(2, stato.name());
            pstmt.setBoolean(3, stato == StatoBagaglio.SMARRITO);
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Errore during il salvataggio del bagaglio: " + exception.getMessage(), exception);
        }
        throw new RuntimeException("Errore: Impossibile ottenere l'ID del bagaglio salvato.");
    }

    @Override
    public void update(int bagaglioId, StatoBagaglio stato) {
        String sql = "UPDATE bagagli SET stato_bagaglio = ?, smarrito = ? WHERE bagaglio_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stato.name());
            pstmt.setBoolean(2, stato == StatoBagaglio.SMARRITO);
            pstmt.setInt(3, bagaglioId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during l'aggiornamento del bagaglio: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByPrenotazioneId(int prenotazioneId) {
        String sql = "DELETE FROM bagagli WHERE prenotazione_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prenotazioneId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during l'eliminazione dei bagagli per prenotazione: " + e.getMessage(), e);
        }
    }
}