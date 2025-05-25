package aeroporto.aeroportoPackage.gui;

import aeroporto.aeroportoPackage.controller.AppController;
import aeroporto.aeroportoPackage.gui.dialogs.BaggageDialog;
import aeroporto.aeroportoPackage.gui.dialogs.FlightDialog;
import aeroporto.aeroportoPackage.model.Amministratore;
import aeroporto.aeroportoPackage.model.Bagaglio;
import aeroporto.aeroportoPackage.model.Prenotazione;
import aeroporto.aeroportoPackage.model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminHomePanel extends JPanel {
    private MainFrame mainFrame;
    private AppController appController;

    private JLabel welcomeLabel;
    private JButton logoutButton;

    private JTable flightsTable;
    private DefaultTableModel flightsTableModel;
    private JButton addFlightButton;
    private JButton updateFlightButton;
    private JButton viewFlightButton;
    private JButton deleteFlightButton;

    private JTable allBaggageTable;
    private DefaultTableModel allBaggageTableModel;
    private JButton updateBaggageStatusButton;

    private JTable lostBaggageTable;
    private DefaultTableModel lostBaggageTableModel;

    public AdminHomePanel(MainFrame mainFrame, AppController appController) {
        this.mainFrame = mainFrame;
        this.appController = appController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadAllData();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        Amministratore admin = (Amministratore) appController.getUtenteCorrente();
        welcomeLabel = new JLabel("Benvenuto Amministratore: " + (admin != null ? admin.getLogin() : ""), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> appController.logout());
        JPanel logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButtonPanel.add(logoutButton);
        topPanel.add(logoutButtonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.60);

        JPanel flightsManagementPanel = createFlightsManagementPanel();
        mainSplitPane.setLeftComponent(flightsManagementPanel);

        JPanel baggageAreaPanel = new JPanel(new BorderLayout());
        JSplitPane baggageSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        baggageSplitPane.setResizeWeight(0.60);

        JPanel allBaggagePanel = createAllBaggagePanel();
        baggageSplitPane.setTopComponent(allBaggagePanel);

        JPanel lostBaggagePanel = createLostBaggagePanel();
        baggageSplitPane.setBottomComponent(lostBaggagePanel);

        baggageAreaPanel.add(baggageSplitPane, BorderLayout.CENTER);
        mainSplitPane.setRightComponent(baggageAreaPanel);

        add(mainSplitPane, BorderLayout.CENTER);
    }

    private JPanel createFlightsManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Tabella dei Voli"));

        String[] flightColumns = {"Codice", "Compagnia", "Partenza", "Arrivo", "Data", "Orario Prev.", "Ritardo (min)", "Stato"};
        flightsTableModel = new DefaultTableModel(flightColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        flightsTable = new JTable(flightsTableModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightsTable.setAutoCreateRowSorter(true);
        flightsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedFlightAction();
                }
            }
        });
        panel.add(new JScrollPane(flightsTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addFlightButton = new JButton("Inserisci Volo");
        updateFlightButton = new JButton("Aggiorna Volo");
        viewFlightButton = new JButton("Visualizza Volo");
        deleteFlightButton = new JButton("Elimina Volo");

        addFlightButton.addActionListener(e -> openFlightDialog(null, false));
        updateFlightButton.addActionListener(e -> updateSelectedFlightAction());
        viewFlightButton.addActionListener(e -> viewSelectedFlightAction());
        deleteFlightButton.addActionListener(e -> deleteSelectedFlightAction());

        buttonsPanel.add(addFlightButton);
        buttonsPanel.add(updateFlightButton);
        buttonsPanel.add(viewFlightButton);
        buttonsPanel.add(deleteFlightButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAllBaggagePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Tutti i Bagagli Registrati"));

        String[] baggageColumns = {"Cod. Bagaglio", "Num. Biglietto", "Passeggero", "Cod. Volo", "Stato Bag."};
        allBaggageTableModel = new DefaultTableModel(baggageColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        allBaggageTable = new JTable(allBaggageTableModel);
        allBaggageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allBaggageTable.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(allBaggageTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateBaggageStatusButton = new JButton("Aggiorna Stato Bagaglio Selezionato");
        updateBaggageStatusButton.addActionListener(e -> updateSelectedBaggageStatusAction());
        buttonPanel.add(updateBaggageStatusButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLostBaggagePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Bagagli Attualmente Smarriti (Stato: SMARRITO)"));

        String[] lostBaggageColumns = {"Cod. Bagaglio", "Num. Biglietto", "Passeggero", "Ultimo Volo Noto"};
        lostBaggageTableModel = new DefaultTableModel(lostBaggageColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        lostBaggageTable = new JTable(lostBaggageTableModel);
        lostBaggageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lostBaggageTable.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(lostBaggageTable), BorderLayout.CENTER);
        return panel;
    }

    public void loadAllData() {
        loadFlightsData();
        loadAllBaggageData();
        loadLostBaggageData();
    }

    public void refreshAllData(){
        Amministratore admin = (Amministratore) appController.getUtenteCorrente();
        if (admin != null) {
            welcomeLabel.setText("Benvenuto Amministratore: " + admin.getLogin());
        } else {
            welcomeLabel.setText("Benvenuto Amministratore");
        }
        loadAllData();
    }

    private void loadFlightsData() {
        flightsTableModel.setRowCount(0);
        List<Volo> voli = appController.handleVisualizzaVoli();
        for (Volo volo : voli) {
            flightsTableModel.addRow(new Object[]{
                    volo.getCodice_Univoco(),
                    volo.getCompagnia_Aerea(),
                    volo.getAeroporto_Partenza(),
                    volo.getAeroporto_Arrivo(),
                    volo.getGiorno_Partenza() + "/" + volo.getMese_Partenza() + "/" + volo.getAnno_Partenza(),
                    volo.getOrario_Previsto(),
                    volo.getMinuti_Ritardo(),
                    volo.getStato_Volo()
            });
        }
    }

    private void loadAllBaggageData() {
        allBaggageTableModel.setRowCount(0);
        List<Bagaglio> bagagli = appController.handleAdminVisualizzaBagagliTutti();
        for (Bagaglio bagaglio : bagagli) {
            Prenotazione p = bagaglio.getPrenotazione();
            String numBiglietto = "N/D";
            String passeggeroNomeCognome = "N/D";
            String codiceVoloAssociato = "N/D";
            if (p != null) {
                numBiglietto = p.getNumero_Biglietto() != null ? p.getNumero_Biglietto() : "N/D";
                passeggeroNomeCognome = (p.getNome_Passeggero() != null ? p.getNome_Passeggero() : "") + " " +
                        (p.getCognome_Passeggero() != null ? p.getCognome_Passeggero() : "");
                passeggeroNomeCognome = passeggeroNomeCognome.trim().isEmpty() ? "Dati Passeggero N/D" : passeggeroNomeCognome.trim();
                codiceVoloAssociato = AppController.extractCodiceVoloFromNumeroBiglietto(numBiglietto);
                codiceVoloAssociato = codiceVoloAssociato != null ? codiceVoloAssociato : "N/D";
            }
            allBaggageTableModel.addRow(new Object[]{
                    bagaglio.getCodice_Bagaglio(),
                    numBiglietto,
                    passeggeroNomeCognome,
                    codiceVoloAssociato,
                    bagaglio.getStato_Bagaglio()
            });
        }
    }

    private void loadLostBaggageData() {
        lostBaggageTableModel.setRowCount(0);
        List<Bagaglio> smarriti = appController.handleAdminVisualizzaSmarrimenti();
        for (Bagaglio bagaglio : smarriti) {
            Prenotazione p = bagaglio.getPrenotazione();
            String numBiglietto = "N/D";
            String passeggeroNomeCognome = "N/D";
            String ultimoVoloNoto = "N/D";
            if (p != null) {
                numBiglietto = p.getNumero_Biglietto() != null ? p.getNumero_Biglietto() : "N/D";
                passeggeroNomeCognome = (p.getNome_Passeggero() != null ? p.getNome_Passeggero() : "") + " " +
                        (p.getCognome_Passeggero() != null ? p.getCognome_Passeggero() : "");
                passeggeroNomeCognome = passeggeroNomeCognome.trim().isEmpty() ? "Dati Passeggero N/D" : passeggeroNomeCognome.trim();
                ultimoVoloNoto = AppController.extractCodiceVoloFromNumeroBiglietto(numBiglietto);
                ultimoVoloNoto = ultimoVoloNoto != null ? ultimoVoloNoto : "N/D";
            }
            lostBaggageTableModel.addRow(new Object[]{
                    bagaglio.getCodice_Bagaglio(),
                    numBiglietto,
                    passeggeroNomeCognome,
                    ultimoVoloNoto
            });
        }
    }

    private Volo getSelectedFlightFromTable() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = flightsTable.convertRowIndexToModel(selectedRow);
            String codiceVolo = (String) flightsTableModel.getValueAt(modelRow, 0);
            return appController.internalFindVoloByCodice(codiceVolo).orElse(null);
        }
        return null;
    }

    private Bagaglio getSelectedBaggageFromAllTable() {
        int selectedRow = allBaggageTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = allBaggageTable.convertRowIndexToModel(selectedRow);
            int codiceBagaglio = (Integer) allBaggageTableModel.getValueAt(modelRow, 0);
            return appController.internalFindBagaglioByCodice(codiceBagaglio).orElse(null);
        }
        return null;
    }

    private void openFlightDialog(Volo volo, boolean readOnly) {
        FlightDialog dialog = new FlightDialog(mainFrame, appController, volo, readOnly);
        dialog.setVisible(true);
        if (dialog.isDataChanged()) {
            loadFlightsData();
        }
    }

    private void updateSelectedFlightAction() {
        Volo selectedVolo = getSelectedFlightFromTable();
        if (selectedVolo != null) {
            openFlightDialog(selectedVolo, false);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla tabella per aggiornarlo.", "Nessun Volo Selezionato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void viewSelectedFlightAction() {
        Volo selectedVolo = getSelectedFlightFromTable();
        if (selectedVolo != null) {
            openFlightDialog(selectedVolo, true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla tabella per visualizzarlo.", "Nessun Volo Selezionato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedFlightAction() {
        Volo selectedVolo = getSelectedFlightFromTable();
        if (selectedVolo != null) {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Sei sicuro di voler eliminare il volo " + selectedVolo.getCodice_Univoco() + "?\nQuesta azione cancellerà anche le prenotazioni associate e i relativi bagagli (logicamente).",
                    "Conferma Eliminazione Volo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (appController.handleAdminEliminaVolo(selectedVolo.getCodice_Univoco())) {
                    JOptionPane.showMessageDialog(this, "Volo eliminato con successo.", "Eliminazione Completata", JOptionPane.INFORMATION_MESSAGE);
                    loadAllData();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione del volo.", "Errore Eliminazione", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla tabella per eliminarlo.", "Nessun Volo Selezionato", JOptionPane.WARNING_MESSAGE);
        }
    }

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

    public void setAppController(AppController appController) {
        this.appController = appController;
        Amministratore admin = (Amministratore) appController.getUtenteCorrente();
        if (admin != null) {
            welcomeLabel.setText("Benvenuto Amministratore: " + admin.getLogin());
        } else {
            welcomeLabel.setText("Benvenuto Amministratore");
        }
        refreshAllData();
    }

    public AppController getAppController() {
        return this.appController;
    }
}