package aeroporto.aeroportopackage.dao.implementations.postgres;

import aeroporto.aeroportopackage.dao.UtenteDAO;
import aeroporto.aeroportopackage.model.Amministratore;
import aeroporto.aeroportopackage.model.Utente;
import aeroporto.aeroportopackage.model.UtenteGenerico;

import java.sql.*;

/**
 * The type Utente postgres dao.
 */
public class UtentePostgresDAO implements UtenteDAO {
    /**
     * The database connection.
     */
    private final Connection conn;
    /**
     * The columns to select from the 'utenti' table.
     */
    private static final String COLUMNS = "utente_id, login, password, ruolo, nome, cognome, numero_documento";

    /**
     * Instantiates a new Utente postgres dao.
     * The constructor initializes the connection to the database, using a "singleton" method.
     *
     * @param connection the connection
     */
    public UtentePostgresDAO(Connection connection) {
        this.conn = connection;
    }

    /**
     * This function takes the data retrieved by the sql query and puts it into an object.
     * @param rs The ResultSet from the query.
     * @return An Utente object.
     * @throws SQLException if a database access error occurs.
     */
    private Utente mapRowToUtente(ResultSet rs) throws SQLException {
        int id = rs.getInt("utente_id");
        String login = rs.getString("login");
        String password = rs.getString("password");
        String ruolo = rs.getString("ruolo");

        if ("ADMIN".equals(ruolo)) {
            return new Amministratore(id, login, password);
        } else {
            String nome = rs.getString("nome");
            String cognome = rs.getString("cognome");
            String numeroDocumento = rs.getString("numero_documento");
            return new UtenteGenerico(id, login, password, nome, cognome, numeroDocumento);
        }
    }

    @Override
    public Utente findByLoginAndPassword(String login, String password) {
        String sql = "SELECT " + COLUMNS + " FROM utenti WHERE login = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUtente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca dell'utente per login e password");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utente findByLogin(String login) {
        String sql = "SELECT " + COLUMNS + " FROM utenti WHERE login = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUtente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca dell'utente per login");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UtenteGenerico findGenericoById(int id) {
        String sql = "SELECT " + COLUMNS + " FROM utenti WHERE utente_id = ? AND ruolo = 'GENERICO'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return (UtenteGenerico) mapRowToUtente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca dell'utente generico per ID");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UtenteGenerico findGenericoByDetails(String nome, String cognome, String numeroDocumento) {
        String sql = "SELECT " + COLUMNS + " FROM utenti WHERE nome = ? AND cognome = ? AND numero_documento = ? AND ruolo = 'GENERICO'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, numeroDocumento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return (UtenteGenerico) mapRowToUtente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore during la ricerca dell'utente generico per dettagli");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(String nome, String cognome, String numeroDocumento, String login, String password) {
        String sql = "INSERT INTO utenti(login, password, nome, cognome, numero_documento, ruolo) VALUES(?, ?, ?, ?, ?, 'GENERICO')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, nome);
            pstmt.setString(4, cognome);
            pstmt.setString(5, numeroDocumento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore during la registrazione dell'utente: " + e.getMessage(), e);
        }
    }
}