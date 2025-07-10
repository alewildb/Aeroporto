package aeroporto.aeroportopackage.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Database manager.
 */
public final class DatabaseManager {
    /**
     * The database URL.
     */
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Aeroporto";
    /**
     * The database user.
     */
    private static final String USER = "postgres";
    /**
     * The database password.
     */
    private static final String PASSWORD = "porcodio";

    /**
     * The database connection.
     */
    private static Connection connection = null;

    /**
     * Private parameter-less constructor to avoid class instantiation.
     */
    private DatabaseManager() {
    }

    /**
     * Establishes the connection to the database.
     */
    private static void establishConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connessione al database stabilita con successo.");
        } catch (SQLException e) {
            System.err.println("Impossibile connettersi al database.");
            e.printStackTrace();
            throw new RuntimeException("Impossibile connettersi al database.", e);
        }
    }

    /**
     * Gets database connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                establishConnection();
            }
        } catch (SQLException e) {
            System.err.println("Errore during il controllo dello stato della connessione.");
            e.printStackTrace();
            throw new RuntimeException("Errore during il controllo della connessione.", e);
        }
        return connection;
    }

    /**
     * Closes database connection only if it is already open.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connessione al database chiusa con successo.");
                }
            } catch (SQLException e) {
                System.err.println("Errore during la chiusura della connessione al database.");
                e.printStackTrace();
            }
        }
    }
}