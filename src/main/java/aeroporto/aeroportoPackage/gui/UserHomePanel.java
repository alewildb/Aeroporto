package aeroporto.aeroportoPackage.gui;

import aeroporto.aeroportoPackage.controller.AppController;
import aeroporto.aeroportoPackage.model.*;
import aeroporto.aeroportoPackage.gui.dialogs.BookingDialog;
import aeroporto.aeroportoPackage.gui.dialogs.LostBaggageDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserHomePanel extends JPanel {
    private MainFrame mainFrame;
    private AppController appController;

    private JLabel welcomeLabel;
    private JButton logoutButton;

    private JTable availableFlightsTable;
    private DefaultTableModel availableFlightsTableModel;
    private JTextField searchAvailableFlightsField;
    private JButton searchAvailableFlightsButton;
    private JButton bookFlightButton;

    private JTable myBookingsTable;
    private DefaultTableModel myBookingsTableModel;
    private JTextField searchMyBookingsByNameField;
    private JButton searchMyBookingsByNameButton;
    private JTextField searchMyBookingsByFlightCodeField;
    private JButton searchMyBookingsByFlightCodeButton;
    private JButton modifyBookingButton;
    private JButton viewBaggageButton;
    private JButton cancelBookingButton;
    private JButton checkInButton;
    private JButton reportLostBaggageButton;

    public UserHomePanel(MainFrame mainFrame, AppController appController) {
        this.mainFrame = mainFrame;
        this.appController = appController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadAllData();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        Utente_Generico user = (Utente_Generico) appController.getUtenteCorrente();
        welcomeLabel = new JLabel("Benvenuto Utente: " + (user != null ? user.getNome() + " " + user.getCognome() : ""), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> appController.logout());
        JPanel logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButtonPanel.add(logoutButton);
        topPanel.add(logoutButtonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.55);

        JPanel availableFlightsPanel = createAvailableFlightsPanel();
        mainSplitPane.setLeftComponent(availableFlightsPanel);

        JPanel myBookingsPanel = createMyBookingsPanel();
        mainSplitPane.setRightComponent(myBookingsPanel);

        add(mainSplitPane, BorderLayout.CENTER);
    }
    private JPanel createAvailableFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Lista Voli Disponibili (PROGRAMMATO o IN RITARDO)"));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cerca (Codice/Partenza/Arrivo):"));
        searchAvailableFlightsField = new JTextField(20);
        searchPanel.add(searchAvailableFlightsField);
        searchAvailableFlightsButton = new JButton("Cerca");
        searchAvailableFlightsButton.addActionListener(e -> loadAvailableFlightsData());
        searchPanel.add(searchAvailableFlightsButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        String[] availableFlightColumns = {"Codice", "Compagnia", "Partenza", "Arrivo", "Data", "Orario Prev.", "Ritardo (min)", "Stato"};
        availableFlightsTableModel = new DefaultTableModel(availableFlightColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        availableFlightsTable = new JTable(availableFlightsTableModel);
        availableFlightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableFlightsTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(availableFlightsTable);
        scrollPane.setMinimumSize(new Dimension(500, 150));

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookFlightButton = new JButton("Prenota Volo Selezionato");
        bookFlightButton.addActionListener(e -> bookSelectedFlightAction());
        buttonPanel.add(bookFlightButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Le Mie Prenotazioni"));

        JPanel searchPanelOuter = new JPanel(new BorderLayout());
        JPanel searchFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,5,2,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; searchFieldsPanel.add(new JLabel("Cerca per Nome Passeggero:"), gbc);
        searchMyBookingsByNameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; searchFieldsPanel.add(searchMyBookingsByNameField, gbc);
        searchMyBookingsByNameButton = new JButton("Cerca Nome");
        searchMyBookingsByNameButton.addActionListener(e -> {
            searchMyBookingsByFlightCodeField.setText("");
            loadMyBookingsData();
        });
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; searchFieldsPanel.add(searchMyBookingsByNameButton, gbc);

        gbc.gridx = 0; gbc.gridy = 1; searchFieldsPanel.add(new JLabel("Cerca per Cod. Volo:"), gbc);
        searchMyBookingsByFlightCodeField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; searchFieldsPanel.add(searchMyBookingsByFlightCodeField, gbc);
        searchMyBookingsByFlightCodeButton = new JButton("Cerca Volo");
        searchMyBookingsByFlightCodeButton.addActionListener(e -> {
            searchMyBookingsByNameField.setText("");
            loadMyBookingsData();
        });
        gbc.gridx = 2; gbc.gridy = 1; searchFieldsPanel.add(searchMyBookingsByFlightCodeButton, gbc);

        JButton clearSearchButton = new JButton("Pulisci Ricerca");
        clearSearchButton.addActionListener(e -> {
            searchMyBookingsByNameField.setText("");
            searchMyBookingsByFlightCodeField.setText("");
            loadMyBookingsData();
        });
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.CENTER; searchFieldsPanel.add(clearSearchButton, gbc);

        searchPanelOuter.add(searchFieldsPanel, BorderLayout.CENTER);
        panel.add(searchPanelOuter, BorderLayout.NORTH);

        String[] myBookingColumns = {"Num. Biglietto", "Passeggero", "Tratta Volo", "Data Volo", "Orario Prev.", "Stato Pren.", "Posto"};
        myBookingsTableModel = new DefaultTableModel(myBookingColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        myBookingsTable = new JTable(myBookingsTableModel);
        myBookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myBookingsTable.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(myBookingsTable), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,5));
        checkInButton = new JButton("Check-In");
        modifyBookingButton = new JButton("Modifica Dati Pren.");
        viewBaggageButton = new JButton("Vedi/Aggiungi Bagagli");
        reportLostBaggageButton = new JButton("Segnala Smarrimento");
        cancelBookingButton = new JButton("Cancella Pren.");

        checkInButton.addActionListener(e -> performCheckInAction());
        modifyBookingButton.addActionListener(e -> modifySelectedBookingAction());
        viewBaggageButton.addActionListener(e -> viewOrAddBaggageForSelectedBookingAction());
        reportLostBaggageButton.addActionListener(e -> reportLostBaggageAction());
        cancelBookingButton.addActionListener(e -> cancelSelectedBookingAction());

        buttonsPanel.add(checkInButton);
        buttonsPanel.add(modifyBookingButton);
        buttonsPanel.add(viewBaggageButton);
        buttonsPanel.add(reportLostBaggageButton);
        buttonsPanel.add(cancelBookingButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void loadAllData() {
        loadAvailableFlightsData();
        loadMyBookingsData();
    }

    public void refreshAllData() {
        Utente_Generico user = (Utente_Generico) appController.getUtenteCorrente();
        if (user != null) {
            welcomeLabel.setText("Benvenuto Utente: " + user.getNome() + " " + user.getCognome());
        } else {
            welcomeLabel.setText("Benvenuto Utente");
        }
        searchAvailableFlightsField.setText("");
        searchMyBookingsByNameField.setText("");
        searchMyBookingsByFlightCodeField.setText("");
        loadAllData();
    }

    private void loadAvailableFlightsData() {
        availableFlightsTableModel.setRowCount(0);
        List<Volo> voliDisponibili = appController.handleUserGetVoliDisponibili();
        String searchTerm = searchAvailableFlightsField.getText().trim().toLowerCase();

        for (Volo volo : voliDisponibili) {
            if (!searchTerm.isEmpty()) {
                boolean matchesCodice = volo.getCodice_Univoco().toLowerCase().contains(searchTerm);
                boolean matchesPartenza = volo.getAeroporto_Partenza().toLowerCase().contains(searchTerm);
                boolean matchesArrivo = volo.getAeroporto_Arrivo().toLowerCase().contains(searchTerm);
                if (!matchesCodice && !matchesPartenza && !matchesArrivo) {
                    continue;
                }
            }
            availableFlightsTableModel.addRow(new Object[]{
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

    private void loadMyBookingsData() {
        myBookingsTableModel.setRowCount(0);
        List<Prenotazione> prenotazioniUtente;
        String searchName = searchMyBookingsByNameField.getText().trim();
        String searchFlightCode = searchMyBookingsByFlightCodeField.getText().trim();

        if (!searchName.isEmpty()) {
            prenotazioniUtente = appController.handleUserCercaPrenotazioniPerNomePasseggero(searchName);
        } else if (!searchFlightCode.isEmpty()) {
            prenotazioniUtente = appController.handleUserCercaPrenotazioniPerCodiceVolo(searchFlightCode);
        } else {
            prenotazioniUtente = appController.handleUserGetMiePrenotazioni();
        }

        for (Prenotazione p : prenotazioniUtente) {
            String trattaDisplay = "N/D";
            String dataVoloDisplay = "N/D";
            String orarioVoloDisplay = "N/D";
            String codiceVoloEstratto = AppController.extractCodiceVoloFromNumeroBiglietto(p.getNumero_Biglietto());

            if (codiceVoloEstratto != null) {
                Optional<Volo> voloOpt = appController.internalFindVoloByCodice(codiceVoloEstratto);
                if (voloOpt.isPresent()) {
                    Volo voloAssociato = voloOpt.get();
                    trattaDisplay = voloAssociato.getAeroporto_Partenza() + " - " + voloAssociato.getAeroporto_Arrivo() + " (" + voloAssociato.getCodice_Univoco() + ")";
                    dataVoloDisplay = voloAssociato.getGiorno_Partenza() + "/" + voloAssociato.getMese_Partenza() + "/" + voloAssociato.getAnno_Partenza();
                    orarioVoloDisplay = voloAssociato.getOrario_Previsto();
                } else {
                    trattaDisplay = "Dati volo (" + codiceVoloEstratto + ") non trovati";
                }
            }

            myBookingsTableModel.addRow(new Object[]{
                    p.getNumero_Biglietto(),
                    p.getNome_Passeggero() + " " + p.getCognome_Passeggero(),
                    trattaDisplay,
                    dataVoloDisplay,
                    orarioVoloDisplay,
                    p.getStato_Prenotazione(),
                    p.getPosto_Assegnato() != null ? p.getPosto_Assegnato() : "N/A"
            });
        }
    }

    private Volo getSelectedAvailableFlightFromTable() {
        int selectedRow = availableFlightsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = availableFlightsTable.convertRowIndexToModel(selectedRow);
            String codiceVolo = (String) availableFlightsTableModel.getValueAt(modelRow, 0);
            return appController.handleUserGetVoliDisponibili().stream()
                    .filter(v -> v.getCodice_Univoco().equals(codiceVolo))
                    .findFirst().orElse(null);
        }
        return null;
    }

    private Prenotazione getSelectedMyBookingFromTable() {
        int selectedRow = myBookingsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = myBookingsTable.convertRowIndexToModel(selectedRow);
            String numeroBiglietto = (String) myBookingsTableModel.getValueAt(modelRow, 0);
            return appController.handleUserGetMiePrenotazioni().stream()
                    .filter(p -> p.getNumero_Biglietto().equals(numeroBiglietto))
                    .findFirst().orElse(null);
        }
        return null;
    }

    private void bookSelectedFlightAction() {
        Volo selectedVolo = getSelectedAvailableFlightFromTable();
        if (selectedVolo == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un volo dalla lista per prenotarlo.", "Nessun Volo Selezionato", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Utente_Generico currentUser = (Utente_Generico) appController.getUtenteCorrente();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Errore: Utente non loggato.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BookingDialog bookingDialog = new BookingDialog(mainFrame, selectedVolo, currentUser);
        bookingDialog.setVisible(true);

        if (bookingDialog.isPrenotazioneConfermata()) {
            String nomeP = bookingDialog.getNomePasseggero();
            String cognomeP = bookingDialog.getCognomePasseggero();
            String docP = bookingDialog.getNumeroDocumento();
            String postoP = bookingDialog.getPostoAssegnato();

            if (appController.handleUserPrenotaVolo(selectedVolo, nomeP, cognomeP, docP, postoP)) {
                JOptionPane.showMessageDialog(this, "Volo prenotato con successo per " + nomeP + " " + cognomeP + "!", "Prenotazione Effettuata", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante la prenotazione.\nIl passeggero potrebbe avere già una prenotazione per questo volo, oppure i dati sono incompleti.", "Errore Prenotazione", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void performCheckInAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione per il Check-In.", "Nessuna Prenotazione Selezionata", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            JOptionPane.showMessageDialog(this, "Impossibile effettuare il check-in per una prenotazione cancellata.", "Azione Non Permessa", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStato_Prenotazione() == Stato_Prenotazione.CONFERMATA) {
            JOptionPane.showMessageDialog(this, "Check-in già effettuato per questa prenotazione.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String bagsStr = JOptionPane.showInputDialog(this, "Numero di bagagli da registrare per " + selectedBooking.getNome_Passeggero() + " " + selectedBooking.getCognome_Passeggero() + ":", "Check-In Bagagli", JOptionPane.PLAIN_MESSAGE);
        if (bagsStr == null) return;

        try {
            int numBags = Integer.parseInt(bagsStr);
            if (numBags < 0) {
                JOptionPane.showMessageDialog(this, "Il numero di bagagli non può essere negativo.", "Input Errato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (appController.handleUserCheckInPrenotazione(selectedBooking, numBags)) {
                JOptionPane.showMessageDialog(this, "Check-in effettuato e " + numBags + " bagagli registrati.\nPrenotazione CONFERMATA.", "Check-In Riuscito", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante il check-in. Verifica che la prenotazione sia valida e appartenga all'utente.", "Errore Check-In", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Inserire un numero valido di bagagli.", "Input Errato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifySelectedBookingAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione da modificare.", "Nessuna Prenotazione Selezionata", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            JOptionPane.showMessageDialog(this, "Impossibile modificare una prenotazione cancellata.", "Azione Non Permessa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuovoPosto = JOptionPane.showInputDialog(this, "Inserisci il nuovo posto desiderato (opzionale):", selectedBooking.getPosto_Assegnato() != null ? selectedBooking.getPosto_Assegnato() : "");
        if (nuovoPosto != null) {
            Prenotazione prenotazioneAggiornata = new Prenotazione(selectedBooking.get_Passeggero());

            prenotazioneAggiornata.setNumero_Biglietto(selectedBooking.getNumero_Biglietto());
            prenotazioneAggiornata.setNome_Passeggero(selectedBooking.getNome_Passeggero());
            prenotazioneAggiornata.setCognome_Passeggero(selectedBooking.getCognome_Passeggero());
            prenotazioneAggiornata.setNumero_Documento_Passeggero(selectedBooking.getNumero_Documento_Passeggero());
            prenotazioneAggiornata.setStato_Prenotazione(selectedBooking.getStato_Prenotazione());
            prenotazioneAggiornata.setBagagli(selectedBooking.getBagagli());

            prenotazioneAggiornata.setPosto_Assegnato(nuovoPosto.trim().isEmpty() ? null : nuovoPosto.trim().toUpperCase());

            if (appController.handleUserModificaPrenotazione(prenotazioneAggiornata)) {
                JOptionPane.showMessageDialog(this, "Dati prenotazione (posto) aggiornati.", "Modifica Riuscita", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento della prenotazione.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewOrAddBaggageForSelectedBookingAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione per gestire i bagagli.", "Nessuna Prenotazione Selezionata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Bagaglio> bagagli = appController.handleUserGetBagagliPerPrenotazione(selectedBooking);
        StringBuilder baggageInfo = new StringBuilder("Bagagli per Prenotazione " + selectedBooking.getNumero_Biglietto() + " (" + selectedBooking.getNome_Passeggero() + " " + selectedBooking.getCognome_Passeggero() + "):\n");
        if (bagagli.isEmpty()) {
            baggageInfo.append("Nessun bagaglio registrato.\n");
        } else {
            for (Bagaglio b : bagagli) {
                baggageInfo.append(" - Codice: ").append(b.getCodice_Bagaglio())
                        .append(", Stato: ").append(b.getStato_Bagaglio()).append("\n");
            }
        }

        if (selectedBooking.getStato_Prenotazione() == Stato_Prenotazione.CONFERMATA) {
            baggageInfo.append("\nVuoi aggiungere un nuovo bagaglio a questa prenotazione confermata?");
            int option = JOptionPane.showConfirmDialog(this, baggageInfo.toString(), "Gestione Bagagli", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                Bagaglio nuovoBagaglio = appController.handleUserAggiungiBagaglioAPrenotazione(selectedBooking);
                if (nuovoBagaglio != null) {
                    JOptionPane.showMessageDialog(this, "Bagaglio con codice " + nuovoBagaglio.getCodice_Bagaglio() + " aggiunto.", "Bagaglio Aggiunto", JOptionPane.INFORMATION_MESSAGE);
                    loadMyBookingsData();
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta del bagaglio. Assicurati che la prenotazione sia confermata.", "Errore Aggiunta Bagaglio", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            baggageInfo.append("\nPer aggiungere bagagli, la prenotazione deve essere CONFERMATA (effettua prima il Check-In).");
            JOptionPane.showMessageDialog(this, baggageInfo.toString(), "Gestione Bagagli", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void reportLostBaggageAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione per segnalare un bagaglio smarrito.", "Nessuna Prenotazione Selezionata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Bagaglio> bagagliDellaPrenotazione = appController.handleUserGetBagagliPerPrenotazione(selectedBooking);
        if (bagagliDellaPrenotazione.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun bagaglio registrato per questa prenotazione da poter segnalare.", "Nessun Bagaglio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Bagaglio> bagagliSegnalabili = bagagliDellaPrenotazione.stream()
                .filter(b -> b.getStato_Bagaglio() != Stato_Bagaglio.SMARRITO)
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
                    "Confermi di voler segnalare il bagaglio con codice " + bagaglioDaSegnalare.getCodice_Bagaglio() + " come SMARRITO?",
                    "Conferma Segnalazione Smarrimento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (appController.handleUserSegnalaSmarrimentoBagaglio(bagaglioDaSegnalare)) {
                    JOptionPane.showMessageDialog(this, "Bagaglio " + bagaglioDaSegnalare.getCodice_Bagaglio() + " segnalato come smarrito.", "Segnalazione Effettuata", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Errore durante la segnalazione dello smarrimento.", "Errore Segnalazione", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    private void cancelSelectedBookingAction() {
        Prenotazione selectedBooking = getSelectedMyBookingFromTable();
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(this, "Seleziona una prenotazione da cancellare.", "Nessuna Prenotazione Selezionata", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBooking.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            JOptionPane.showMessageDialog(this, "Questa prenotazione è già stata cancellata.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler cancellare la prenotazione " + selectedBooking.getNumero_Biglietto() + " per " + selectedBooking.getNome_Passeggero() + "?",
                "Conferma Cancellazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (appController.handleUserCancellaPrenotazione(selectedBooking)) {
                JOptionPane.showMessageDialog(this, "Prenotazione cancellata con successo.", "Cancellazione Effettuata", JOptionPane.INFORMATION_MESSAGE);
                loadMyBookingsData();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante la cancellazione della prenotazione.", "Errore Cancellazione", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
        Utente_Generico user = (Utente_Generico) appController.getUtenteCorrente();
        if (user != null) {
            welcomeLabel.setText("Benvenuto Utente: " + user.getNome() + " " + user.getCognome());
        } else {
            welcomeLabel.setText("Benvenuto Utente");
        }
        refreshAllData();
    }

    public AppController getAppController() {
        return this.appController;
    }
}