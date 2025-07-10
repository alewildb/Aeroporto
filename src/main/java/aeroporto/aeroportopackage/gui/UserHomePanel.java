package aeroporto.aeroportopackage.gui;

import aeroporto.aeroportopackage.controller.AppController;
import aeroporto.aeroportopackage.dao.BagaglioDAO;
import aeroporto.aeroportopackage.dao.PrenotazioneDAO;
import aeroporto.aeroportopackage.dao.VoloDAO;
import aeroporto.aeroportopackage.gui.dialogs.BookingDialog;
import aeroporto.aeroportopackage.gui.dialogs.LostBaggageDialog;
import aeroporto.aeroportopackage.model.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The User home panel.
 */
public class UserHomePanel extends JPanel {
    /**
     * The main GUI frame.
     */
    private final MainFrame mainFrame;
    /**
     * The application controller.
     */
    private final AppController appController;

    /**
     * The Welcome label.
     */
    private JLabel welcomeLabel;
    /**
     * The Available flights table.
     */
    private JTable availableFlightsTable;
    /**
     * The Available flights table model.
     */
    private DefaultTableModel availableFlightsTableModel;
    /**
     * The Search available flights field.
     */
    private JTextField searchAvailableFlightsField;
    /**
     * The "My bookings" table.
     */
    private JTable myBookingsTable;
    /**
     * The "My bookings" table model.
     */
    private DefaultTableModel myBookingsTableModel;
    /**
     * The Search my bookings field.
     */
    private JTextField searchMyBookingsField;
    /**
     * The Font name.
     */
    private static final String userFont = "SansSerif";
    /**
     * asfgdobhikju.
     */
    private final String noBookingSelected = "Nessuna Prenotazione Selezionata";
    /**
     * search icon
     */
    private final String searchIcon = "icons/search.svg";
    /**
     * Instantiates a new User home panel.
     *
     * @param mainFrame     the main frame
     * @param appController the app controller
     */
    public UserHomePanel(MainFrame mainFrame, AppController appController) {
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
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        UtenteGenerico user = (UtenteGenerico) appController.getUtenteCorrente();
        welcomeLabel = new JLabel("Benvenuto: " + (user != null ? user.getNome() + " " + user.getCognome() : ""), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font(userFont, Font.BOLD, 22));
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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(userFont, Font.PLAIN, 14));

        JPanel availableFlightsPanel = createAvailableFlightsPanel();
        tabbedPane.addTab("Cerca e Prenota Voli", new FlatSVGIcon(searchIcon), availableFlightsPanel);

        JPanel myBookingsPanel = createMyBookingsPanel();
        tabbedPane.addTab("Le Mie Prenotazioni", new FlatSVGIcon("icons/book-open.svg"), myBookingsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates the panel that contains the available flights that the user can book.
     * @return returns the configured JPanel.
     */
    private JPanel createAvailableFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cerca per Codice, Partenza o Arrivo:"));
        searchAvailableFlightsField = new JTextField(25);
        searchPanel.add(searchAvailableFlightsField);
        JButton searchButton = new JButton("Cerca", new FlatSVGIcon(searchIcon));
        searchButton.addActionListener(e -> loadAvailableFlightsData());
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        String[] flightColumns = {"Codice", "Compagnia", "Partenza", "Arrivo", "Data", "Orario Prev.", "Ritardo (min)", "Stato"};
        availableFlightsTableModel = new DefaultTableModel(flightColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        availableFlightsTable = new JTable(availableFlightsTableModel);
        availableFlightsTable.setRowHeight(28);
        availableFlightsTable.getTableHeader().setFont(new Font(userFont, Font.BOLD, 14));
        availableFlightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableFlightsTable.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(availableFlightsTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bookFlightButton = new JButton("Prenota Volo Selezionato", new FlatSVGIcon("icons/plus.svg"));
        bookFlightButton.setFont(new Font(userFont, Font.BOLD, 14));
        bookFlightButton.addActionListener(e -> bookSelectedFlightAction());
        buttonPanel.add(bookFlightButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the panel to visualize and manage the user's bookings.
     * @return the configured JPanel.
     */
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cerca per Nome Passeggero:"));
        searchMyBookingsField = new JTextField(25);
        searchPanel.add(searchMyBookingsField);
        JButton searchButton = new JButton("Cerca", new FlatSVGIcon(searchIcon));
        searchButton.addActionListener(e -> loadMyBookingsData());
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        String[] bookingColumns = {"Num. Biglietto", "Passeggero", "Tratta Volo", "Data Volo", "Orario Prev.", "Stato Pren.", "Posto"};
        myBookingsTableModel = new DefaultTableModel(bookingColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        myBookingsTable = new JTable(myBookingsTableModel);
        myBookingsTable.setRowHeight(28);
        myBookingsTable.getTableHeader().setFont(new Font(userFont, Font.BOLD, 14));
        myBookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myBookingsTable.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(myBookingsTable), BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JButton checkInButton = new JButton("Check-In", new FlatSVGIcon("icons/check-square.svg"));
        JButton modifyBookingButton = new JButton("Modifica Posto", new FlatSVGIcon("icons/edit.svg"));
        JButton viewBaggageButton = new JButton("Bagagli", new FlatSVGIcon("icons/briefcase.svg"));
        JButton reportLostBaggageButton = new JButton("Segnala Smarrimento", new FlatSVGIcon("icons/alert-triangle.svg"));
        JButton cancelBookingButton = new JButton("Cancella Prenotazione", new FlatSVGIcon("icons/x-circle.svg"));

        checkInButton.addActionListener(e -> performCheckInAction());
        modifyBookingButton.addActionListener(e -> modifySelectedBookingAction());
        viewBaggageButton.addActionListener(e -> viewOrAddBaggageForSelectedBookingAction());
        reportLostBaggageButton.addActionListener(e -> reportLostBaggageAction());
        cancelBookingButton.addActionListener(e -> cancelSelectedBookingAction());

        toolBar.add(checkInButton);
        toolBar.add(modifyBookingButton);
        toolBar.add(viewBaggageButton);
        toolBar.add(reportLostBaggageButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(cancelBookingButton);

        panel.add(toolBar, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Loads all data.
     */
    public void loadAllData() {
        loadAvailableFlightsData();
        loadMyBookingsData();
    }

    /**
     * Refreshes all data after a login.
     */
    public void refreshAllData() {
        UtenteGenerico user = (UtenteGenerico) appController.getUtenteCorrente();
        if (user != null) {
            welcomeLabel.setText("Benvenuto: " + user.getNome() + " " + user.getCognome());
        } else {
            welcomeLabel.setText("Benvenuto Utente");
        }
        if (searchAvailableFlightsField != null) {
            searchAvailableFlightsField.setText("");
        }

        if (searchMyBookingsField != null) {
            searchMyBookingsField.setText("");
        }
        loadAllData();
    }

    /**
     * Loads every flight from the database, filtering them based on what the user is looking for, adding them to the table.
     */
    private void loadAvailableFlightsData() {
        availableFlightsTableModel.setRowCount(0);
        VoloDAO voloDAO = appController.getVoloDAO();
        List<Volo> voli = voloDAO.findAll().stream()
                .filter(v -> v.getStatoVolo() == StatoVolo.PROGRAMMATO || v.getStatoVolo() == StatoVolo.IN_RITARDO)
                .collect(Collectors.toList());

        String searchTerm = searchAvailableFlightsField.getText().trim().toLowerCase();

        for (Volo volo : voli) {
            if (!searchTerm.isEmpty()) {
                boolean matchesCodice = volo.getCodiceUnivoco().toLowerCase().contains(searchTerm);
                boolean matchesPartenza = volo.getAeroportoPartenza().toLowerCase().contains(searchTerm);
                boolean matchesArrivo = volo.getAeroportoArrivo().toLowerCase().contains(searchTerm);
                if (!matchesCodice && !matchesPartenza && !matchesArrivo) {
                    continue;
                }
            }
            availableFlightsTableModel.addRow(new Object[]{
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
     * Loads all the user's bookings from the database and adds it to the table.
     */
    private void loadMyBookingsData() {
        myBookingsTableModel.setRowCount(0);
        PrenotazioneDAO prenotazioneDAO = appController.getPrenotazioneDAO();
        UtenteGenerico currentUser = (UtenteGenerico) appController.getUtenteCorrente();
        if (currentUser == null) return;

        List<Prenotazione> prenotazioniUtente = prenotazioneDAO.findByUtente(currentUser);

        String searchTerm = searchMyBookingsField.getText().trim().toLowerCase();
        if (!searchTerm.isEmpty()) {
            prenotazioniUtente = prenotazioniUtente.stream()
                    .filter(p -> {
                        String nomeCompleto = (p.getNomePasseggero() + " " + p.getCognomePasseggero()).toLowerCase();
                        return nomeCompleto.contains(searchTerm);
                    })
                    .collect(Collectors.toList());
        }


        for (Prenotazione p : prenotazioniUtente) {
            String trattaDisplay;
            String dataVoloDisplay;
            String orarioVoloDisplay;
            String codiceVolo = p.getVoloCodice();

            if (codiceVolo != null && !codiceVolo.isBlank()) {
                Volo voloAssociato = appController.getVoloDAO().findByCodice(codiceVolo);
                if (voloAssociato != null) {
                    trattaDisplay = voloAssociato.getAeroportoPartenza() + " - " + voloAssociato.getAeroportoArrivo();
                    dataVoloDisplay = voloAssociato.getGiornoPartenza() + "/" + voloAssociato.getMesePartenza() + "/" + voloAssociato.getAnnoPartenza();
                    orarioVoloDisplay = voloAssociato.getOrarioPrevisto();
                } else {
                    trattaDisplay = "Volo " + codiceVolo + " non trovato";
                    dataVoloDisplay = "N/D";
                    orarioVoloDisplay = "N/D";
                }
            } else {
                trattaDisplay = "Codice volo mancante";
                dataVoloDisplay = "N/D";
                orarioVoloDisplay = "N/D";
            }

            myBookingsTableModel.addRow(new Object[]{
                    p.getNumeroBiglietto(),
                    p.getNomePasseggero() + " " + p.getCognomePasseggero(),
                    trattaDisplay,
                    dataVoloDisplay,
                    orarioVoloDisplay,
                    p.getStatoPrenotazione(),
                    p.getPostoAssegnato() != null ? p.getPostoAssegnato() : "N/A"
            });
        }
    }

    /**
     * Gets the flight object related to the selected row.
     * @return the flight object.
     */
    private Volo getSelectedAvailableFlightFromTable() {
        int selectedRow = availableFlightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = availableFlightsTable.convertRowIndexToModel(selectedRow);
            String codiceVolo = (String) availableFlightsTableModel.getValueAt(modelRow, 0);
            return appController.getVoloDAO().findByCodice(codiceVolo);
        }
        return null;
    }

    /**
     * Gets the booking object related to the selected row.
     * @return the booking object.
     */
    private Prenotazione getSelectedMyBookingFromTable() {
        int selectedRow = myBookingsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = myBookingsTable.convertRowIndexToModel(selectedRow);
            String numeroBiglietto = (String) myBookingsTableModel.getValueAt(modelRow, 0);

            UtenteGenerico currentUser = (UtenteGenerico) appController.getUtenteCorrente();
            if (currentUser == null) return null;

            return appController.getPrenotazioneDAO().findByUtente(currentUser).stream()
                    .filter(p -> p.getNumeroBiglietto().equals(numeroBiglietto))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * Opens the booking dialog for the selected flight.
     */
    private void bookSelectedFlightAction() {
        Volo selectedVolo = getSelectedAvailableFlightFromTable();
        if (selectedVolo == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla lista per prenotarlo.", "Nessun Volo Selezionato", JOptionPane.WARNING_MESSAGE);
            return;
        }
        UtenteGenerico currentUser = (UtenteGenerico) appController.getUtenteCorrente();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Errore: Utente non loggato.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BookingDialog bookingDialog = new BookingDialog(mainFrame, selectedVolo, currentUser);
        bookingDialog.setVisible(true);

        if (bookingDialog.isPrenotazioneConfermata()) {
            try {
                UtenteGenerico passeggero = appController.getUtenteDAO().findGenericoByDetails(
                        bookingDialog.getNomePasseggero(),
                        bookingDialog.getCognomePasseggero(),
                        bookingDialog.getNumeroDocumento());
                if (passeggero == null) passeggero = currentUser;

                Prenotazione p = new Prenotazione();
                p.setUtenteCheHaPrenotato(passeggero);
                p.setNomePasseggero(bookingDialog.getNomePasseggero());
                p.setCognomePasseggero(bookingDialog.getCognomePasseggero());
                p.setNumeroDocumentoPasseggero(bookingDialog.getNumeroDocumento());
                p.setPostoAssegnato(bookingDialog.getPostoAssegnato());
                p.setStatoPrenotazione(StatoPrenotazione.IN_ATTESA);
                p.setVoloCodice(selectedVolo.getCodiceUnivoco());
                p.setNumeroBiglietto(appController.generateNumeroBiglietto(selectedVolo.getCodiceUnivoco()));

                appController.getPrenotazioneDAO().save(p);

                JOptionPane.showMessageDialog(this, "Volo prenotato con successo!", "Prenotazione Effettuata", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore during la prenotazione: " + e.getMessage(), "Errore Prenotazione", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Opens a dialog to check in the flight.
     * Checking in a flight lets the user add baggage to its booking.
     */
    private void performCheckInAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione per il Check-In.", noBookingSelected, JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedBooking.getStatoPrenotazione() == StatoPrenotazione.CANCELLATA) {
            JOptionPane.showMessageDialog(this, "Impossibile effettuare il check-in per una prenotazione cancellata.", "Azione Non Permessa", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStatoPrenotazione() == StatoPrenotazione.CONFERMATA) {
            JOptionPane.showMessageDialog(this, "Check-in già effettuato per questa prenotazione.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String bagsStr = JOptionPane.showInputDialog(this, "Numero di bagagli da registrare per " + selectedBooking.getNomePasseggero() + " " + selectedBooking.getCognomePasseggero() + ":", "Check-In Bagagli", JOptionPane.PLAIN_MESSAGE);
        if (bagsStr == null) return;

        try {
            int numBags = Integer.parseInt(bagsStr);
            if (numBags < 0) {
                JOptionPane.showMessageDialog(this, "Il numero di bagagli non può essere negativo.", "Input Errato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            appController.getPrenotazioneDAO().update(selectedBooking.getId(), StatoPrenotazione.CONFERMATA, selectedBooking.getPostoAssegnato());
            for (int i = 0; i < numBags; i++) {
                appController.getBagaglioDAO().save(selectedBooking.getId(), StatoBagaglio.CARICATO_AEREO);
            }

            JOptionPane.showMessageDialog(this, "Check-in effettuato e " + numBags + " bagagli registrati.\nPrenotazione CONFERMATA.", "Check-In Riuscito", JOptionPane.INFORMATION_MESSAGE);
            loadMyBookingsData();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Inserire un numero valido di bagagli.", "Input Errato", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Errore during il check-in: " + e.getMessage(), "Errore Check-In", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens a dialog to let the user modify the airplane seat for the selected booking.
     */
    private void modifySelectedBookingAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione da modificare.", noBookingSelected, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStatoPrenotazione() == StatoPrenotazione.CANCELLATA) {
            JOptionPane.showMessageDialog(this, "Impossibile modificare una prenotazione cancellata.", "Azione Non Permessa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuovoPosto = JOptionPane.showInputDialog(this, "Inserisci il nuovo posto desiderato (opzionale):", selectedBooking.getPostoAssegnato() != null ? selectedBooking.getPostoAssegnato() : "");
        if (nuovoPosto != null) {
            try {
                String postoFinale = nuovoPosto.trim().isEmpty() ? null : nuovoPosto.trim().toUpperCase();
                appController.getPrenotazioneDAO().update(selectedBooking.getId(), selectedBooking.getStatoPrenotazione(), postoFinale);
                JOptionPane.showMessageDialog(this, "Dati prenotazione (posto) aggiornati.", "Modifica Riuscita", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Errore during l'aggiornamento della prenotazione: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lets the user see the baggage related to a booking and, if possible, lets him add more baggage.
     */
    private void viewOrAddBaggageForSelectedBookingAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione per gestire i bagagli.", noBookingSelected, JOptionPane.WARNING_MESSAGE);
            return;
        }

        BagaglioDAO bagaglioDAO = appController.getBagaglioDAO();
        List<Bagaglio> bagagli = bagaglioDAO.findByPrenotazioneId(selectedBooking.getId());
        StringBuilder baggageInfo = new StringBuilder("Bagagli per Prenotazione " + selectedBooking.getNumeroBiglietto() + " (" + selectedBooking.getNomePasseggero() + " " + selectedBooking.getCognomePasseggero() + "):\n");
        if (bagagli.isEmpty()) {
            baggageInfo.append("Nessun bagaglio registrato.\n");
        } else {
            for (Bagaglio b : bagagli) {
                baggageInfo.append(" - Codice: ").append(b.getCodiceBagaglio())
                        .append(", Stato: ").append(b.getStatoBagaglio()).append("\n");
            }
        }

        if (selectedBooking.getStatoPrenotazione() == StatoPrenotazione.CONFERMATA) {
            baggageInfo.append("\nVuoi aggiungere un nuovo bagaglio a questa prenotazione confermata?");
            int option = JOptionPane.showConfirmDialog(this, baggageInfo.toString(), "Gestione Bagagli", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    int nuovoBagaglioId = bagaglioDAO.save(selectedBooking.getId(), StatoBagaglio.CARICATO_AEREO);
                    JOptionPane.showMessageDialog(this, "Bagaglio con codice " + nuovoBagaglioId + " aggiunto.", "Bagaglio Aggiunto", JOptionPane.INFORMATION_MESSAGE);
                    loadMyBookingsData();
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(this, "Errore during l'aggiunta del bagaglio: " + e.getMessage(), "Errore Aggiunta Bagaglio", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            baggageInfo.append("\nPer aggiungere bagagli, la prenotazione deve essere CONFERMATA (effettua prima il Check-In).");
            JOptionPane.showMessageDialog(this, baggageInfo.toString(), "Gestione Bagagli", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Lets the user report a baggage missing from the baggage related to a booking.
     */
    private void reportLostBaggageAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione per segnalare un bagaglio smarrito.", noBookingSelected, JOptionPane.WARNING_MESSAGE);
            return;
        }

        BagaglioDAO bagaglioDAO = appController.getBagaglioDAO();
        List<Bagaglio> bagagliDellaPrenotazione = bagaglioDAO.findByPrenotazioneId(selectedBooking.getId());
        if (bagagliDellaPrenotazione.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun bagaglio registrato per questa prenotazione da poter segnalare.", "Nessun Bagaglio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Bagaglio> bagagliSegnalabili = bagagliDellaPrenotazione.stream()
                .filter(b -> b.getStatoBagaglio() != StatoBagaglio.SMARRITO)
                .collect(Collectors.toList());

        if (bagagliSegnalabili.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i bagagli per questa prenotazione sono già stati segnalati come smarriti.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        LostBaggageDialog dialog = new LostBaggageDialog(mainFrame, selectedBooking, bagagliSegnalabili);
        dialog.setVisible(true);

        if (dialog.isConfirmed() && dialog.getSelectedBaggage() != null) {
            Bagaglio bagaglioDaSegnalare = dialog.getSelectedBaggage();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confermi di voler segnalare il bagaglio con codice " + bagaglioDaSegnalare.getCodiceBagaglio() + " come SMARRITO?",
                    "Conferma Segnalazione Smarrimento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    bagaglioDAO.update(bagaglioDaSegnalare.getCodiceBagaglio(), StatoBagaglio.SMARRITO);
                    JOptionPane.showMessageDialog(this, "Bagaglio " + bagaglioDaSegnalare.getCodiceBagaglio() + " segnalato come smarrito.", "Segnalazione Effettuata", JOptionPane.INFORMATION_MESSAGE);
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(this, "Errore during la segnalazione dello smarrimento: " + e.getMessage(), "Errore Segnalazione", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Lets the user cancel the selected booking.
     */
    private void cancelSelectedBookingAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione da cancellare.", noBookingSelected, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStatoPrenotazione() == StatoPrenotazione.CANCELLATA) {
            JOptionPane.showMessageDialog(this, "Questa prenotazione è già stata cancellata.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler cancellare la prenotazione " + selectedBooking.getNumeroBiglietto() + " per " + selectedBooking.getNomePasseggero() + "?",
                "Conferma Cancellazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                appController.getPrenotazioneDAO().update(selectedBooking.getId(), StatoPrenotazione.CANCELLATA, selectedBooking.getPostoAssegnato());
                JOptionPane.showMessageDialog(this, "Prenotazione cancellata con successo.", "Cancellazione Effettuata", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Errore during la cancellazione della prenotazione: " + e.getMessage(), "Errore Cancellazione", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}