package aeroporto.aeroportoPackage.gui;

import aeroporto.aeroportoPackage.controller.AppController;
import aeroporto.aeroportoPackage.model.Amministratore;
import aeroporto.aeroportoPackage.model.Utente_Generico;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanelContainer;

    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private AdminHomePanel adminHomePanel;
    private UserHomePanel userHomePanel;

    private AppController appController;

    private static final Dimension MINIMUM_FRAME_SIZE = new Dimension(1250, 800);

    public MainFrame() {
        setTitle("Sistema Aeroportuale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(MINIMUM_FRAME_SIZE);

        setSize(new Dimension(1350, 800));


        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);

        appController = new AppController(this);

        loginPanel = new LoginPanel(this, appController);
        registrationPanel = new RegistrationPanel(this, appController);

        mainPanelContainer.add(loginPanel, "LOGIN_PANEL");
        mainPanelContainer.add(registrationPanel, "REGISTRATION_PANEL");

        add(mainPanelContainer);

        setLocationRelativeTo(null);
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanelContainer, "LOGIN_PANEL");
        if (loginPanel != null) {
            loginPanel.clearFields();
        }
    }

    public void showRegistrationPanel() {
        cardLayout.show(mainPanelContainer, "REGISTRATION_PANEL");
        if (registrationPanel != null) {
            registrationPanel.clearFields();
        }
    }

    public void showAdminHomePanel(Amministratore admin, AppController controller) {
        if (adminHomePanel == null) {
            adminHomePanel = new AdminHomePanel(this, controller);
            mainPanelContainer.add(adminHomePanel, "ADMIN_HOME_PANEL");
        } else {
            adminHomePanel.setAppController(controller);
        }
        adminHomePanel.refreshAllData();
        cardLayout.show(mainPanelContainer, "ADMIN_HOME_PANEL");
    }

    public void showUserHomePanel(Utente_Generico utente, AppController controller) {
        if (userHomePanel == null) {
            userHomePanel = new UserHomePanel(this, controller);
            mainPanelContainer.add(userHomePanel, "USER_HOME_PANEL");
        } else {
            userHomePanel.setAppController(controller);
        }
        userHomePanel.refreshAllData();
        cardLayout.show(mainPanelContainer, "USER_HOME_PANEL");
    }

    public AppController getAppController() {
        return appController;
    }
}