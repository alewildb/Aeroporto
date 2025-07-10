package aeroporto.aeroportopackage.gui;
import aeroporto.aeroportopackage.controller.AppController;
import aeroporto.aeroportopackage.dao.BagaglioDAO;
import aeroporto.aeroportopackage.dao.VoloDAO;
import aeroporto.aeroportopackage.gui.dialogs.BaggageDialog;
import aeroporto.aeroportopackage.gui.dialogs.FlightDialog;
import aeroporto.aeroportopackage.model.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The type Admin home panel.
 */
public class AdminHomePanel extends JPanel {
    /**
     * The Main frame.
     */
    private final MainFrame mainFrame;
    /**
     * The App controller.
     */
    private final AppController appController;
    /**
     * The Welcome label.
     */
    private JLabel welcomeLabel;
    /**
     * The Flights table.
     */
    private JTable flightsTable;
    /**
     * The Flights table model.
     */
    private DefaultTableModel flightsTableModel;
    /**
     * The All baggage table.
     */
    private JTable allBaggageTable;
    /**
     * The All baggage table model.
     */
    private DefaultTableModel allBaggageTableModel;
    /**
     * The Lost baggage table.
     */
    private JTable lostBaggageTable;
    /**
     * The Lost baggage table model.
     */
    private DefaultTableModel lostBaggageTableModel;

    /**
     * The font.
     */
    private final String adminFont = "SansSerif";
    /**
     * dsavjink.
     */
    private final String noFlight = "Nessun Volo Selezionato";
    /**
     * Instantiates a new Admin home panel.
     *
     * @param mainFrame     the main frame
     * @param appController the app controller
     */
    public AdminHomePanel(MainFrame mainFrame, AppController appController) {
        this.mainFrame = mainFrame;
        this.appController = appController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        setBackground(new Color(245, 248, 250));

        initComponents();
        loadAllData();
    }

    /**
     * Initializes and assembles the main components.
     */
    private void initComponents() {
        // Pannello Superiore
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        Amministratore admin = (Amministratore) appController.getUtenteCorrente();
        welcomeLabel = new JLabel("Benvenuto Amministratore: " + (admin != null ? admin.getLogin() : ""), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font(adminFont, Font.BOLD, 22));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);

        JButton logoutButton = new JButton(new FlatSVGIcon("icons/log-out.svg"));
        logoutButton.setToolTipText("Effettua il logout");
        logoutButton.putClientProperty("JButton.buttonType", "toolBarButton");
        logoutButton.addActionListener(e -> appController.logout());
        rightPanel.add(logoutButton);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Struttura a schede
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(adminFont, Font.PLAIN, 14));

        JPanel flightsManagementPanel = createFlightsManagementPanel();
        tabbedPane.addTab("Gestione Voli", new FlatSVGIcon("icons/plane.svg"), flightsManagementPanel);

        JPanel baggageManagementPanel = createBaggageManagementPanel();
        tabbedPane.addTab("Gestione Bagagli", new FlatSVGIcon("icons/briefcase.svg"), baggageManagementPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates the flights management panel creating the table and its buttons.
     * @return returns the JPanel configured for the flights' management.
     */
    private JPanel createFlightsManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] flightColumns = {"Codice", "Compagnia", "Partenza", "Arrivo", "Data", "Orario Prev.", "Ritardo (min)", "Stato"};
        flightsTableModel = new DefaultTableModel(flightColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        flightsTable = new JTable(flightsTableModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsTable.setAutoCreateRowSorter(true);
        flightsTable.setRowHeight(28);
        flightsTable.getTableHeader().setFont(new Font(adminFont, Font.BOLD, 14));
        flightsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedFlightAction();
                }
            }
        });
        panel.add(new JScrollPane(flightsTable), BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JButton addFlightButton = new JButton("Inserisci", new FlatSVGIcon("icons/plus-circle.svg"));
        JButton updateFlightButton = new JButton("Aggiorna", new FlatSVGIcon("icons/edit.svg"));
        JButton viewFlightButton = new JButton("Visualizza", new FlatSVGIcon("icons/eye.svg"));
        JButton deleteFlightButton = new JButton("Elimina", new FlatSVGIcon("icons/trash-2.svg"));

        addFlightButton.addActionListener(e -> openFlightDialog(null, false));
        updateFlightButton.addActionListener(e -> updateSelectedFlightAction());
        viewFlightButton.addActionListener(e -> viewSelectedFlightAction());
        deleteFlightButton.addActionListener(e -> deleteSelectedFlightAction());

        toolBar.add(addFlightButton);
        toolBar.add(updateFlightButton);
        toolBar.add(viewFlightButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(deleteFlightButton);
        panel.add(toolBar, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the baggage management panel creating two tables: one for all the baggage and one for the lost baggage.
     * @return returns the JPanel configured for the baggage's management.
     */
    private JPanel createBaggageManagementPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.60);
        splitPane.setBorder(null);

        JPanel allBaggagePanel = new JPanel(new BorderLayout(5, 5));
        allBaggagePanel.setBorder(BorderFactory.createTitledBorder("Tutti i Bagagli Registrati"));
        String[] baggageColumns = {"Cod. Bagaglio", "Num. Biglietto", "Passeggero", "Cod. Volo", "Stato Bag."};
        allBaggageTableModel = new DefaultTableModel(baggageColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        allBaggageTable = new JTable(allBaggageTableModel);
        allBaggageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allBaggageTable.setAutoCreateRowSorter(true);
        allBaggageTable.setRowHeight(28);
        allBaggageTable.getTableHeader().setFont(new Font(adminFont, Font.BOLD, 14));
        allBaggagePanel.add(new JScrollPane(allBaggageTable), BorderLayout.CENTER);
        JButton updateBaggageStatusButton = new JButton("Aggiorna Stato Bagaglio Selezionato");
        updateBaggageStatusButton.addActionListener(e -> updateSelectedBaggageStatusAction());
        JPanel southButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        southButtonPanel.add(updateBaggageStatusButton);
        allBaggagePanel.add(southButtonPanel, BorderLayout.SOUTH);
        splitPane.setTopComponent(allBaggagePanel);

        JPanel lostBaggagePanel = new JPanel(new BorderLayout(5, 5));
        lostBaggagePanel.setBorder(BorderFactory.createTitledBorder("Bagagli Attualmente Smarriti"));
        String[] lostBaggageColumns = {"Cod. Bagaglio", "Num. Biglietto", "Passeggero", "Ultimo Volo Noto"};
        lostBaggageTableModel = new DefaultTableModel(lostBaggageColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        lostBaggageTable = new JTable(lostBaggageTableModel);
        lostBaggageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lostBaggageTable.setAutoCreateRowSorter(true);
        lostBaggageTable.setRowHeight(28);
        lostBaggageTable.getTableHeader().setFont(new Font(adminFont, Font.BOLD, 14));
        lostBaggagePanel.add(new JScrollPane(lostBaggageTable), BorderLayout.CENTER);
        splitPane.setBottomComponent(lostBaggagePanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Loads all data.
     */
    public void loadAllData() {
        loadFlightsData();
        loadAllBaggageData();
        loadLostBaggageData();
    }

    /**
     * Refreshes all data after a login.
     */
    public void refreshAllData() {
        Amministratore admin = (Amministratore) appController.getUtenteCorrente();
        if (admin != null) {
            welcomeLabel.setText("Benvenuto Amministratore: " + admin.getLogin());
        } else {
            welcomeLabel.setText("Benvenuto Amministratore");
        }
        loadAllData();
    }

    /**
     * Gets all flights' data from the database and adds it to the table.
     */
    private void loadFlightsData() {
        flightsTableModel.setRowCount(0);
        VoloDAO voloDAO = appController.getVoloDAO();
        List<Volo> voli = voloDAO.findAll();
        for (Volo volo : voli) {
            flightsTableModel.addRow(new Object[]{
                    volo.getCodiceUnivoco(),
                    volo.getCompagniaAerea(),
                    volo.getAeroportoPartenza(),
                    volo.getAeroportoArrivo(),
                    volo.getGiornoPartenza() + "/" + volo.getMesePartenza() + "/" + volo.getAnnoPartenza(),
                    volo.getOrarioPrevisto(),
                    volo.getMinutiRitardo(),
                    volo.getStatoVolo()
            });
        }
    }

    /**
     * Gets all baggage's data from the database and adds it to the table.
     */
    private void loadAllBaggageData() {
        allBaggageTableModel.setRowCount(0);
        BagaglioDAO bagaglioDAO = appController.getBagaglioDAO();
        List<Bagaglio> bagagli = bagaglioDAO.findAll();
        for (Bagaglio bagaglio : bagagli) {
            Prenotazione p = bagaglio.getPrenotazione();
            String numBiglietto = "N/D";
            String passeggeroNomeCognome = "N/D";
            String codiceVoloAssociato = "N/D";
            if (p != null) {
                numBiglietto = p.getNumeroBiglietto() != null ? p.getNumeroBiglietto() : "N/D";
                passeggeroNomeCognome = (p.getNomePasseggero() != null ? p.getNomePasseggero() : "") + " " +
                        (p.getCognomePasseggero() != null ? p.getCognomePasseggero() : "");
                passeggeroNomeCognome = passeggeroNomeCognome.trim().isEmpty() ? "Dati Passeggero N/D" : passeggeroNomeCognome.trim();
                codiceVoloAssociato = AppController.extractCodiceVoloFromNumeroBiglietto(numBiglietto);
                codiceVoloAssociato = codiceVoloAssociato != null ? codiceVoloAssociato : "N/D";
            }
            allBaggageTableModel.addRow(new Object[]{
                    bagaglio.getCodiceBagaglio(),
                    numBiglietto,
                    passeggeroNomeCognome,
                    codiceVoloAssociato,
                    bagaglio.getStatoBagaglio()
            });
        }
    }

    /**
     * Gets all lost baggage's data from the database and adds it to the table.
     */
    private void loadLostBaggageData() {
        lostBaggageTableModel.setRowCount(0);
        BagaglioDAO bagaglioDAO = appController.getBagaglioDAO();
        List<Bagaglio> smarriti = bagaglioDAO.findSmarriti();
        for (Bagaglio bagaglio : smarriti) {
            Prenotazione p = bagaglio.getPrenotazione();
            String numBiglietto = "N/D";
            String passeggeroNomeCognome = "N/D";
            String ultimoVoloNoto = "N/D";
            if (p != null) {
                numBiglietto = p.getNumeroBiglietto() != null ? p.getNumeroBiglietto() : "N/D";
                passeggeroNomeCognome = (p.getNomePasseggero() != null ? p.getNomePasseggero() : "") + " " +
                        (p.getCognomePasseggero() != null ? p.getCognomePasseggero() : "");
                passeggeroNomeCognome = passeggeroNomeCognome.trim().isEmpty() ? "Dati Passeggero N/D" : passeggeroNomeCognome.trim();
                ultimoVoloNoto = AppController.extractCodiceVoloFromNumeroBiglietto(numBiglietto);
                ultimoVoloNoto = ultimoVoloNoto != null ? ultimoVoloNoto : "N/D";
            }
            lostBaggageTableModel.addRow(new Object[]{
                    bagaglio.getCodiceBagaglio(),
                    numBiglietto,
                    passeggeroNomeCognome,
                    ultimoVoloNoto
            });
        }
    }

    /**
     * Gets the flight object corresponding to the selected row.
     * @return returns the selected flight if a row is selected.
     */
    private Volo getSelectedFlightFromTable() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = flightsTable.convertRowIndexToModel(selectedRow);
            String codiceVolo = (String) flightsTableModel.getValueAt(modelRow, 0);
            return appController.getVoloDAO().findByCodice(codiceVolo);
        }
        return null;
    }

    /**
     * Gets the baggage object corresponding to the selected row from the all baggage table.
     * @return returns the selected baggage if a row is selected.
     */
    private Bagaglio getSelectedBaggageFromAllTable() {
        int selectedRow = allBaggageTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = allBaggageTable.convertRowIndexToModel(selectedRow);
            int codiceBagaglio = (Integer) allBaggageTableModel.getValueAt(modelRow, 0);
            return appController.getBagaglioDAO().findById(codiceBagaglio);
        }
        return null;
    }

    /**
     * Opens the flight dialog to edit / insert / view a flight.
     * @param volo the selected flight to update. If it is null, the insert flight dialog is opened.
     * @param readOnly false if we are editing/inserting, true if we are visualizing the flight.
     */
    private void openFlightDialog(Volo volo, boolean readOnly) {
        FlightDialog dialog = new FlightDialog(mainFrame, appController, volo, readOnly);
        dialog.setVisible(true);
        if (dialog.isDataChanged()) {
            loadFlightsData();
        }
    }

    /**
     * Gets the selected flight and opens the flight dialog.
     * It's the "update flight" (Aggiorna) button.
     */
    private void updateSelectedFlightAction() {
        Volo selectedVolo = getSelectedFlightFromTable();
        if (selectedVolo != null) {
            openFlightDialog(selectedVolo, false);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla tabella per aggiornarlo.", noFlight, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Gets the selected flight and opens the flight dialog.
     * It's the "view flight" (Visualizza) button or the double-clicking of the selected row.
     */
    private void viewSelectedFlightAction() {
        Volo selectedVolo = getSelectedFlightFromTable();
        if (selectedVolo != null) {
            openFlightDialog(selectedVolo, true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla tabella per visualizzarlo.", noFlight, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Gets the selected flight and is used to delete it.
     * It's the "delete flight" (Elimina) button.
     */
    private void deleteSelectedFlightAction() {
        Volo selectedVolo = getSelectedFlightFromTable();
        if (selectedVolo != null) {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Sei sicuro di voler eliminare il volo " + selectedVolo.getCodiceUnivoco() + "?\nQuesta azione è irreversibile.",
                    "Conferma Eliminazione Volo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    appController.getVoloDAO().delete(selectedVolo.getCodiceUnivoco());
                    JOptionPane.showMessageDialog(this, "Volo eliminato con successo.", "Eliminazione Completata", JOptionPane.INFORMATION_MESSAGE);
                    loadAllData();
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione: " + e.getMessage(), "Errore Eliminazione", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla tabella per eliminarlo.", noFlight, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Opens the dialog to change the selected baggage's status.
     * It's the "update status" (Aggiorna stato bagaglio selezionato) button.
     */
    private void updateSelectedBaggageStatusAction() {
        Bagaglio selectedBagaglio = getSelectedBaggageFromAllTable();
        if (selectedBagaglio != null) {
            BaggageDialog dialog = new BaggageDialog(mainFrame, appController, selectedBagaglio);
            dialog.setVisible(true);
            if (dialog.isDataChanged()) {
                loadAllBaggageData();
                loadLostBaggageData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un bagaglio dalla tabella 'Tutti i Bagagli' per aggiornarne lo stato.", "Nessun Bagaglio Selezionato", JOptionPane.WARNING_MESSAGE);
        }
    }
}