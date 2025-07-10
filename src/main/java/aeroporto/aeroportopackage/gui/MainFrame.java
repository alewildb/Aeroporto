package aeroporto.aeroportopackage.gui;

import aeroporto.aeroportopackage.controller.AppController;
import aeroporto.aeroportopackage.database.DatabaseManager;
import aeroporto.aeroportopackage.model.Amministratore;
import aeroporto.aeroportopackage.model.UtenteGenerico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * The Main frame.
 */
public class MainFrame extends JFrame {
    /**
     * The Card layout for switching panels.
     */
    private final CardLayout cardLayout;
    /**
     * The Main panel container.
     */
    private final JPanel mainPanelContainer;

    /**
     * The Login panel.
     */
    private LoginPanel loginPanel;
    /**
     * The Registration panel.
     */
    private RegistrationPanel registrationPanel;
    /**
     * The Admin home panel.
     */
    private AdminHomePanel adminHomePanel;
    /**
     * The User home panel.
     */
    private UserHomePanel userHomePanel;

    /**
     * The application controller.
     */
    private final transient AppController appController;

    /**
     * The minimum size for the frame.
     */
    private static final Dimension MINIMUM_FRAME_SIZE = new Dimension(1250, 800);

    /**
     * The current panel name.
     */
    private String panelCorrente;

    /**
     * The constant for login panel name.
     */
    private static final String LOGIN_PANEL_NAME = "LOGIN_PANEL";
    /**
     * The constant for registration panel name.
     */
    private static final String REGISTRATION_PANEL_NAME = "REGISTRATION_PANEL";
    /**
     * The name for the admin home panel.
     */
    private static final String ADMIN_HOME_PANEL_NAME = "ADMIN_HOME_PANEL";
    /**
     * The constant for user home panel name.
     */
    private static final String USER_HOME_PANEL_NAME = "USER_HOME_PANEL";


    /**
     * Instantiates a new Main frame.
     */
    public MainFrame() {
        setTitle("Sistema Aeroportuale di Napoli");

        URL iconURL = getClass().getClassLoader().getResource("icons/icon.png");
        if (iconURL != null) {
            ImageIcon appIcon = new ImageIcon(iconURL);
            setIconImage(appIcon.getImage());
        }

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DatabaseManager.closeConnection();
                System.exit(0);
            }
        });

        setMinimumSize(MINIMUM_FRAME_SIZE);
        setSize(new Dimension(1350, 800));

        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);

        appController = AppController.getInstance();
        appController.setMainFrame(this);

        loginPanel = new LoginPanel(this, appController);
        registrationPanel = new RegistrationPanel(this, appController);

        mainPanelContainer.add(loginPanel, LOGIN_PANEL_NAME);
        mainPanelContainer.add(registrationPanel, REGISTRATION_PANEL_NAME);

        this.panelCorrente = LOGIN_PANEL_NAME;

        add(mainPanelContainer);
        setLocationRelativeTo(null);

    }

    /**
     * Gets current panel.
     *
     * @return the current panel
     */
    public String getPanelCorrente() {
        return panelCorrente;
    }

    /**
     * Shows login panel.
     */
    public void showLoginPanel() {
        this.panelCorrente = LOGIN_PANEL_NAME;
        cardLayout.show(mainPanelContainer, LOGIN_PANEL_NAME);
        if (loginPanel != null) {
            loginPanel.clearFields();
        }
    }

    /**
     * Shows registration panel.
     */
    public void showRegistrationPanel() {
        this.panelCorrente = REGISTRATION_PANEL_NAME;
        cardLayout.show(mainPanelContainer, REGISTRATION_PANEL_NAME);
        if (registrationPanel != null) {
            registrationPanel.clearFields();
        }
    }

    /**
     * Shows admin home panel.
     *
     * @param admin      the admin
     * @param controller the controller
     */
    public void showAdminHomePanel(Amministratore admin, AppController controller) {
        this.panelCorrente = ADMIN_HOME_PANEL_NAME;
        if (adminHomePanel == null) {
            adminHomePanel = new AdminHomePanel(this, controller);
            mainPanelContainer.add(adminHomePanel, ADMIN_HOME_PANEL_NAME);
        }
        adminHomePanel.refreshAllData();
        cardLayout.show(mainPanelContainer, ADMIN_HOME_PANEL_NAME);
    }

    /**
     * Shows user home panel.
     *
     * @param utente     the utente
     * @param controller the controller
     */
    public void showUserHomePanel(UtenteGenerico utente, AppController controller) {
        this.panelCorrente = USER_HOME_PANEL_NAME;
        if (userHomePanel == null) {
            userHomePanel = new UserHomePanel(this, controller);
            mainPanelContainer.add(userHomePanel, USER_HOME_PANEL_NAME);
        }
        userHomePanel.refreshAllData();
        cardLayout.show(mainPanelContainer, USER_HOME_PANEL_NAME);
    }
}