package aeroporto.aeroportopackage.dao.implementations.postgres;

import aeroporto.aeroportopackage.dao.BagaglioDAO;
import aeroporto.aeroportopackage.dao.PrenotazioneDAO;
import aeroporto.aeroportopackage.dao.UtenteDAO;
import aeroporto.aeroportopackage.model.Prenotazione;
import aeroporto.aeroportopackage.model.StatoPrenotazione;
import aeroporto.aeroportopackage.model.UtenteGenerico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Prenotazione postgres dao.
 */
public class PrenotazionePostgresDAO implements PrenotazioneDAO {
    /**
     * The database connection.
     */
    private final Connection conn;
    /**
     * The Utente DAO.
     */
    private final UtenteDAO utenteDAO;

    /**
     * Instantiates a new Prenotazione postgres dao.
     * The constructor initializes the connection to the database, using a "singleton" method.
     *
     * @param connection the connection
     */
    public PrenotazionePostgresDAO(Connection connection) {
        this.conn = connection;
        this.utenteDAO = new UtentePostgresDAO(connection);
    }

    /**
     * This function takes the data retrieved by the sql query and puts it into an object.
     * @param rs The ResultSet from the query.
     * @return A Prenotazione object.
     * @throws SQLException if a database access error occurs.
     */
    private Prenotazione mapRowToPrenotazione(ResultSet rs) throws SQLException {
        Prenotazione p = new Prenotazione();
        p.setId(rs.getInt("prenotazione_id"));
        p.setNumeroBiglietto(rs.getString("numero_biglietto"));
        p.setNomePasseggero(rs.getString("nome_passeggero"));
        p.setCognomePasseggero(rs.getString("cognome_passeggero"));
        p.setNumeroDocumentoPasseggero(rs.getString("numero_documento_passeggero"));
        p.setStatoPrenotazione(StatoPrenotazione.valueOf(rs.getString("stato_prenotazione")));
        p.setPostoAssegnato(rs.getString("posto_assegnato"));
        p.setVoloCodice(rs.getString("volo_codice"));

        int utenteId = rs.getInt("utente_id");
        UtenteGenerico ug = utenteDAO.findGenericoById(utenteId);
        if (ug != null) {
            p.setUtenteCheHaPrenotato(ug);
        }

        return p;
    }

    @Override
    public void save(Prenotazione p) {
        String sql = "INSERT INTO prenotazioni (numero_biglietto, utente_id, volo_codice, nome_passeggero, cognome_passeggero, numero_documento_passeggero, stato_prenotazione, posto_assegnato) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, p.getNumeroBiglietto());
            pstmt.setInt(2, p.getUtenteCheHaPrenotato().getId());
            pstmt.setString(3, p.getVoloCodice());
            pstmt.setString(4, p.getNomePasseggero());
            pstmt.setString(5, p.getCognomePasseggero());
            pstmt.setString(6, p.getNumeroDocumentoPasseggero());
            pstmt.setString(7, p.getStatoPrenotazione().name());
            pstmt.setString(8, p.getPostoAssegnato());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore during il salvataggio della prenotazione: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(int prenotazioneId, StatoPrenotazione stato, String posto) {
        if (stato == StatoPrenotazione.CANCELLATA) {
            BagaglioDAO bagaglioDAO = new BagaglioPostgresDAO(this.conn);
            bagaglioDAO.deleteByPrenotazioneId(prenotazioneId);
        }

        String sql = "UPDATE prenotazioni SET stato_prenotazione = ?, posto_assegnato = ? WHERE prenotazione_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stato.name());
            pstmt.setString(2, posto);
            pstmt.setInt(3, prenotazioneId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during l'aggiornamento della prenotazione: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Prenotazione> findByUtente(UtenteGenerico utente) {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        String sql = "SELECT * FROM prenotazioni WHERE utente_id = ? OR " +
                "(nome_passeggero = ? AND cognome_passeggero = ? AND numero_documento_passeggero = ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, utente.getId());
            pstmt.setString(2, utente.getNome());
            pstmt.setString(3, utente.getCognome());
            pstmt.setString(4, utente.getNumeroDocumento());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    prenotazioni.add(mapRowToPrenotazione(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca delle prenotazioni per utente");
            e.printStackTrace();
        }
        return prenotazioni;
    }
}