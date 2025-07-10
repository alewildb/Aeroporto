package aeroporto.aeroportopackage.controller;

import aeroporto.aeroportopackage.dao.*;
import aeroporto.aeroportopackage.dao.implementations.postgres.*;
import aeroporto.aeroportopackage.database.DatabaseManager;
import aeroporto.aeroportopackage.gui.MainFrame;
import aeroporto.aeroportopackage.model.Amministratore;
import aeroporto.aeroportopackage.model.Utente;
import aeroporto.aeroportopackage.model.UtenteGenerico;

import java.sql.Connection;

/**
 * The type App controller.
 * Defines the controller that manages the entire application.
 */
public final class AppController {

    /**
     * The single instance of the controller.
     */
    private static AppController instance = null;
    /**
     * The main GUI frame.
     */
    private MainFrame mainFrame;
    /**
     * The currently logged-in user.
     */
    private Utente utenteCorrente;

    /**
     * The data access object for users.
     */
    private final UtenteDAO utenteDAO;
    /**
     * The data access object for flights.
     */
    private final VoloDAO voloDAO;
    /**
     * The data access object for bookings.
     */
    private final PrenotazioneDAO prenotazioneDAO;
    /**
     * The data access object for baggage.
     */
    private final BagaglioDAO bagaglioDAO;
    /**
     * The data access object for gates.
     */
    private final GateDAO gateDAO;

    /**
     * Instantiates a new App controller.
     * Private constructor to follow the Singleton pattern.
     */
    private AppController() {
        Connection dbConnection = DatabaseManager.getConnection();
        this.utenteDAO = new UtentePostgresDAO(dbConnection);
        this.voloDAO = new VoloPostgresDAO(dbConnection);
        this.prenotazioneDAO = new PrenotazionePostgresDAO(dbConnection);
        this.bagaglioDAO = new BagaglioPostgresDAO(dbConnection);
        this.gateDAO = new GatePostgresDAO(dbConnection);
    }

    /**
     * Gets instance of the controller, the keyword synchronized is used to prevent strange things happening when two
     * users try to do the same thing at the same time.
     * @return the instance
     */
    public static synchronized AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    /**
     * Sets main frame of the gui.
     *
     * @param mainFrame the main frame
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Login boolean.
     * This is the function that manages the login of a user. It works as an "interface" since the major work is done by
     * the functions it calls.
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public boolean login(String username, String password) {
        if (isStringNullOrEmpty(username) || isStringNullOrEmpty(password)) {
            return false;
        }
        Utente utente = utenteDAO.findByLoginAndPassword(username.trim(), password);
        if (utente != null) {
            this.utenteCorrente = utente;
            if (utente instanceof Amministratore amministratore) {
                mainFrame.showAdminHomePanel(amministratore, this);
            } else if (utente instanceof UtenteGenerico utenteGenerico) {
                mainFrame.showUserHomePanel(utenteGenerico, this);
            }
            return true;
        }
        return false;
    }

    /**
     * Logout.
     */
    public void logout() {
        this.utenteCorrente = null;
        if (mainFrame != null) {
            mainFrame.showLoginPanel();
        }
    }

    /**
     * Gets utente dao.
     *
     * @return the utente dao
     */
    public UtenteDAO getUtenteDAO() { return utenteDAO; }

    /**
     * Gets volo dao.
     *
     * @return the volo dao
     */
    public VoloDAO getVoloDAO() { return voloDAO; }

    /**
     * Gets prenotazione dao.
     *
     * @return the prenotazione dao
     */
    public PrenotazioneDAO getPrenotazioneDAO() { return prenotazioneDAO; }

    /**
     * Gets bagaglio dao.
     *
     * @return the bagaglio dao
     */
    public BagaglioDAO getBagaglioDAO() { return bagaglioDAO; }

    /**
     * Gets gate dao.
     *
     * @return the gate dao
     */
    public GateDAO getGateDAO() { return gateDAO; }

    /**
     * Gets utente corrente.
     *
     * @return the utente corrente
     */
    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    /**
     * Is string null or empty boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public boolean isStringNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Generate numero biglietto string.
     * This function generates a valid and mostly unique code for a ticket.
     * If it somehow refers to an unknow flight, it sets the "codiceVolo" to SCONOSCIUTO, then generates a pseudo-randomic
     * number and adds that to the "TKT" string, as well as the flight number.
     * @param codiceVolo the codice volo
     * @return the string
     */
    public String generateNumeroBiglietto(String codiceVolo) {
        if (isStringNullOrEmpty(codiceVolo)) codiceVolo = "SCONOSCIUTO";
        long uniquePart = System.currentTimeMillis() % 1000000;
        return "TKT" + uniquePart + "-" + codiceVolo.toUpperCase();
    }

    /**
     * Extract codice volo from numero biglietto string.
     *
     * @param numeroBiglietto the numero biglietto
     * @return the string
     */
    public static String extractCodiceVoloFromNumeroBiglietto(String numeroBiglietto) {
        if (numeroBiglietto != null && numeroBiglietto.contains("-")) {
            try {
                return numeroBiglietto.substring(numeroBiglietto.lastIndexOf('-') + 1);
            } catch (IndexOutOfBoundsException exception) {
                System.out.println("Formato numero biglietto non valido: " + numeroBiglietto);
                return null;
            }
        }
        return null;
    }
}