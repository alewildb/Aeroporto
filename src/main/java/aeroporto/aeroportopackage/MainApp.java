package aeroporto.aeroportopackage;

import aeroporto.aeroportopackage.database.DatabaseManager;
import aeroporto.aeroportopackage.gui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * The main class to start the application.
 */
public class MainApp {

    /**
     * Instantiates a new Main app.
     */
    public MainApp() {
        // Default constructor
    }

    /**
     * The main method that starts the application.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Impossibile impostare il tema FlatLaf. Si userà il default.");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeConnection));
    }
}