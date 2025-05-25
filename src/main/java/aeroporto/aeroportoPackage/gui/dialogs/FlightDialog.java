package aeroporto.aeroportoPackage.gui.dialogs;

import aeroporto.aeroportoPackage.controller.AppController;
import aeroporto.aeroportoPackage.model.Gate;
import aeroporto.aeroportoPackage.model.Stato_Volo;
import aeroporto.aeroportoPackage.model.Volo;
import aeroporto.aeroportoPackage.model.Volo_Arrivo;
import aeroporto.aeroportoPackage.model.Volo_Partenza;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class FlightDialog extends JDialog {
    private AppController appController;
    private Volo currentVolo; // Il volo da modificare, null se è un nuovo inserimento
    private boolean readOnlyMode;
    private boolean dataChanged = false; // Flag per indicare se i dati sono cambiati

    // Campi della UI
    private JTextField codiceField;
    private JTextField compagniaField;
    private JComboBox<String> tipoVoloComboBox;
    private JTextField aeroportoPartenzaField;
    private JTextField aeroportoArrivoField;
    private JTextField giornoField, meseField, annoField;
    private JTextField orarioPrevistoField; // Es. "14:30"
    private JTextField minutiRitardoField;
    private JComboBox<Stato_Volo> statoVoloComboBox;
    private JComboBox<GateWrapper> gateComboBox;
    private JLabel gateLabel;

    private JButton saveButton;
    private JButton cancelButton;

    public FlightDialog(Frame owner, AppController appController, Volo voloToEdit, boolean readOnly) {
        super(owner, true); // Modale
        this.appController = appController;
        this.currentVolo = voloToEdit;
        this.readOnlyMode = readOnly;

        String title = (currentVolo == null) ? "Inserisci Nuovo Volo" :
                (readOnly ? "Dettagli Volo: " : "Modifica Volo: ") + (currentVolo != null ? currentVolo.getCodice_Univoco() : "");
        setTitle(title);

        initComponents();
        populateFields();
        setupInteractions(); // Imposta editabilità e visibilità in base al tipo di volo e modalità

        if (readOnlyMode) {
            setFieldsEditable(false); // Tutti i campi non editabili
            saveButton.setVisible(false); // Nascondi il pulsante salva
            cancelButton.setText("Chiudi"); // Cambia testo del pulsante annulla
        }

        pack();
        setMinimumSize(new Dimension(500, getHeight() + 20)); // Adatta altezza
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;

        int yPos = 0;

        // Codice Volo
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Codice Volo:"), gbc);
        codiceField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(codiceField, gbc); gbc.gridwidth = 1;

        // Compagnia Aerea
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Compagnia Aerea:"), gbc);
        compagniaField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(compagniaField, gbc); gbc.gridwidth = 1;

        // Tipo Volo
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Tipo Volo:"), gbc);
        tipoVoloComboBox = new JComboBox<>(new String[]{"Partenza", "Arrivo"});
        tipoVoloComboBox.addActionListener(e -> setupInteractions()); // Aggiorna UI al cambio
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(tipoVoloComboBox, gbc); gbc.gridwidth = 1;

        // Aeroporto Partenza
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Aeroporto Partenza:"), gbc);
        aeroportoPartenzaField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(aeroportoPartenzaField, gbc); gbc.gridwidth = 1;

        // Aeroporto Arrivo
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Aeroporto Arrivo:"), gbc);
        aeroportoArrivoField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(aeroportoArrivoField, gbc); gbc.gridwidth = 1;

        // Data (Giorno, Mese, Anno)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Data (G-M-A):"), gbc);
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        giornoField = new JTextField(3); meseField = new JTextField(3); annoField = new JTextField(5);
        datePanel.add(giornoField); datePanel.add(new JLabel("-")); datePanel.add(meseField);
        datePanel.add(new JLabel("-")); datePanel.add(annoField);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(datePanel, gbc); gbc.gridwidth = 1;

        // Orario Previsto (HH:MM)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Orario Previsto (HH:MM):"), gbc);
        orarioPrevistoField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(orarioPrevistoField, gbc); gbc.gridwidth = 1;

        // Minuti Ritardo
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Minuti Ritardo:"), gbc);
        minutiRitardoField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(minutiRitardoField, gbc); gbc.gridwidth = 1;


        // Stato Volo
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Stato Volo:"), gbc);
        statoVoloComboBox = new JComboBox<>(Stato_Volo.values());
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(statoVoloComboBox, gbc); gbc.gridwidth = 1;

        // Gate
        gateLabel = new JLabel("Gate Assegnato:");
        gbc.gridx = 0; gbc.gridy = yPos; add(gateLabel, gbc);
        gateComboBox = new JComboBox<>();
        List<Gate> gatesDisponibili = appController.handleAdminGetAllGates(); // Usa AppController
        gateComboBox.addItem(new GateWrapper(null)); // Opzione per nessun gate / non applicabile
        for (Gate gate : gatesDisponibili) {
            gateComboBox.addItem(new GateWrapper(gate));
        }
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(gateComboBox, gbc); gbc.gridwidth = 1;

        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        saveButton = new JButton("Salva");
        cancelButton = new JButton("Annulla");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; add(buttonPanel, gbc);

        saveButton.addActionListener(e -> saveFlight());
        cancelButton.addActionListener(e -> dispose());
    }

    private void populateFields() {
        if (currentVolo != null) { // Modifica o visualizzazione
            codiceField.setText(currentVolo.getCodice_Univoco());
            // codiceField.setEditable(false); // Codice non modificabile dopo la creazione
            compagniaField.setText(currentVolo.getCompagnia_Aerea());
            aeroportoPartenzaField.setText(currentVolo.getAeroporto_Partenza());
            aeroportoArrivoField.setText(currentVolo.getAeroporto_Arrivo());
            giornoField.setText(String.valueOf(currentVolo.getGiorno_Partenza()));
            meseField.setText(String.valueOf(currentVolo.getMese_Partenza()));
            annoField.setText(String.valueOf(currentVolo.getAnno_Partenza()));
            orarioPrevistoField.setText(currentVolo.getOrario_Previsto() != null ? currentVolo.getOrario_Previsto() : "00:00");
            minutiRitardoField.setText(String.valueOf(currentVolo.getMinuti_Ritardo()));
            statoVoloComboBox.setSelectedItem(currentVolo.getStato_Volo());

            if (currentVolo instanceof Volo_Partenza) {
                tipoVoloComboBox.setSelectedItem("Partenza");
                Gate currentGate = ((Volo_Partenza) currentVolo).getGate();
                gateComboBox.setSelectedItem(new GateWrapper(currentGate)); // Gestisce gate nullo
            } else if (currentVolo instanceof Volo_Arrivo) {
                tipoVoloComboBox.setSelectedItem("Arrivo");
                gateComboBox.setSelectedItem(new GateWrapper(null)); // Arrivi non hanno gate in questo contesto
            }
            // tipoVoloComboBox.setEnabled(false); // Il tipo di volo non si cambia dopo la creazione
        } else { // Nuovo Volo
            tipoVoloComboBox.setSelectedItem("Partenza"); // Default
            statoVoloComboBox.setSelectedItem(Stato_Volo.PROGRAMMATO); // Default
            orarioPrevistoField.setText("12:00"); // Default
            minutiRitardoField.setText("0");   // Default
            // Lascia aeroporti e gate da impostare tramite setupInteractions
        }
    }

    private void setupInteractions() {
        boolean isPartenza = "Partenza".equals(tipoVoloComboBox.getSelectedItem());
        gateLabel.setVisible(isPartenza);
        gateComboBox.setVisible(isPartenza);
        gateComboBox.setEnabled(isPartenza && !readOnlyMode); // Gate editabile solo per partenze e non in read-only

        // Logica per aeroporti fissi (Napoli)
        if (!readOnlyMode) { // Solo se non siamo in modalità visualizzazione
            if (currentVolo == null || tipoVoloComboBox.isEnabled()) { // Nuovo volo o tipo modificabile
                if (isPartenza) {
                    aeroportoPartenzaField.setText("Napoli");
                    aeroportoPartenzaField.setEditable(false);
                    aeroportoArrivoField.setEditable(true);
                    if (currentVolo == null) aeroportoArrivoField.setText(""); // Pulisci per nuovo
                } else { // Arrivo
                    aeroportoArrivoField.setText("Napoli");
                    aeroportoArrivoField.setEditable(false);
                    aeroportoPartenzaField.setEditable(true);
                    if (currentVolo == null) aeroportoPartenzaField.setText(""); // Pulisci per nuovo
                }
            } else { // Modifica volo esistente, tipo non modificabile
                if (currentVolo instanceof Volo_Partenza) {
                    aeroportoPartenzaField.setEditable(false); // Napoli fisso
                    aeroportoArrivoField.setEditable(true);
                } else if (currentVolo instanceof Volo_Arrivo) {
                    aeroportoArrivoField.setEditable(false); // Napoli fisso
                    aeroportoPartenzaField.setEditable(true);
                }
            }
        } else { // Read-only mode
            aeroportoPartenzaField.setEditable(false);
            aeroportoArrivoField.setEditable(false);
        }
        // Codice e tipo sono editabili solo per nuovi voli e non in read-only
        codiceField.setEditable(currentVolo == null && !readOnlyMode);
        tipoVoloComboBox.setEnabled(currentVolo == null && !readOnlyMode);
    }

    private void setFieldsEditable(boolean editable) {
        // Questa funzione imposta l'editabilità generale, ma setupInteractions la affina
        codiceField.setEditable(editable && currentVolo == null);
        compagniaField.setEditable(editable);

        // L'editabilità di partenza/arrivo/gate è gestita da setupInteractions
        // Qui ci assicuriamo solo che se 'editable' è false, TUTTO sia non editabile.
        if (!editable) {
            aeroportoPartenzaField.setEditable(false);
            aeroportoArrivoField.setEditable(false);
            gateComboBox.setEnabled(false);
        } else {
            setupInteractions(); // Richiama per ripristinare l'editabilità condizionale
        }

        giornoField.setEditable(editable);
        meseField.setEditable(editable);
        annoField.setEditable(editable);
        orarioPrevistoField.setEditable(editable);
        minutiRitardoField.setEditable(editable);
        statoVoloComboBox.setEnabled(editable);
        tipoVoloComboBox.setEnabled(editable && currentVolo == null);
    }

    private void saveFlight() {
        String codice = codiceField.getText().trim();
        String compagnia = compagniaField.getText().trim();
        String aeroportoP = aeroportoPartenzaField.getText().trim();
        String aeroportoA = aeroportoArrivoField.getText().trim();
        String gStr = giornoField.getText().trim();
        String mStr = meseField.getText().trim();
        String aStr = annoField.getText().trim();
        String orarioStr = orarioPrevistoField.getText().trim();
        String ritardoStr = minutiRitardoField.getText().trim();

        if (codice.isEmpty() || compagnia.isEmpty() || aeroportoP.isEmpty() || aeroportoA.isEmpty() ||
                gStr.isEmpty() || mStr.isEmpty() || aStr.isEmpty() || orarioStr.isEmpty() || ritardoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi principali sono obbligatori.", "Errore Validazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!orarioStr.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Formato orario non valido. Usare HH:MM (es. 09:30).", "Errore Formato Orario", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int giorno, mese, anno, minutiRitardoNum;
        try {
            giorno = Integer.parseInt(gStr);
            mese = Integer.parseInt(mStr);
            anno = Integer.parseInt(aStr);
            minutiRitardoNum = Integer.parseInt(ritardoStr);
            LocalDate.of(anno, mese, giorno); // Valida la data
            if (minutiRitardoNum < 0) throw new NumberFormatException("Il ritardo non può essere negativo.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giorno, Mese, Anno e Ritardo devono essere numeri validi. " + e.getMessage(), "Errore Formato Numerico", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data non valida: " + e.getMessage(), "Errore Data", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) { // Altre eccezioni sulla data
            JOptionPane.showMessageDialog(this, "Errore nella data: " + e.getMessage(), "Errore Data", JOptionPane.ERROR_MESSAGE);
            return;
        }


        Stato_Volo stato = (Stato_Volo) statoVoloComboBox.getSelectedItem();
        String tipoVoloSelezionato = (String) tipoVoloComboBox.getSelectedItem();

        Gate selectedGateObject = null;
        Integer numeroGateSePartenza = null;
        if ("Partenza".equals(tipoVoloSelezionato)) {
            GateWrapper gateWrapper = (GateWrapper) gateComboBox.getSelectedItem();
            if (gateWrapper != null) {
                selectedGateObject = gateWrapper.getGate();
                if (selectedGateObject != null) {
                    numeroGateSePartenza = selectedGateObject.getNumero_Gate();
                }
            }
            if (currentVolo == null && selectedGateObject == null) { // Nuovo volo di partenza richiede un gate
                JOptionPane.showMessageDialog(this, "Per un nuovo Volo di Partenza, selezionare un Gate.", "Errore Validazione", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }


        boolean success;
        if (currentVolo == null) { // Inserimento
            success = appController.handleAdminInserisciVolo(codice, compagnia, aeroportoP, aeroportoA,
                    giorno, mese, anno, orarioStr, minutiRitardoNum, stato,
                    tipoVoloSelezionato, numeroGateSePartenza);
            if (!success) {
                JOptionPane.showMessageDialog(this, "Errore durante l'inserimento del volo.\nPossibile codice duplicato o gate non valido.", "Errore Inserimento", JOptionPane.ERROR_MESSAGE);
            }
        } else { // Aggiornamento
            // Ricrea l'oggetto volo o aggiorna l'esistente, poi passalo al controller
            // E' meglio passare un oggetto Volo aggiornato al controller per l'update
            Volo voloAggiornato;
            if ("Partenza".equals(tipoVoloSelezionato)) { // o currentVolo instanceof Volo_Partenza
                voloAggiornato = new Volo_Partenza(codice, compagnia, aeroportoA, giorno, mese, anno, orarioStr, minutiRitardoNum, selectedGateObject);
            } else {
                voloAggiornato = new Volo_Arrivo(codice, compagnia, aeroportoP, giorno, mese, anno, orarioStr, minutiRitardoNum);
            }
            voloAggiornato.setStato_Volo(stato);
            // Nota: l'aeroporto fisso (Napoli) è gestito dai costruttori di Volo_Partenza/Arrivo

            success = appController.handleAdminAggiornaVolo(voloAggiornato);
            if (!success) {
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento del volo.", "Errore Aggiornamento", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (success) {
            dataChanged = true;
            dispose();
        }
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    private static class GateWrapper {
        private Gate gate;
        public GateWrapper(Gate gate) { this.gate = gate; }
        public Gate getGate() { return gate; }
        @Override public String toString() { return gate == null ? "-- Nessun Gate --" : "Gate " + gate.getNumero_Gate(); }
        @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; GateWrapper that = (GateWrapper) o; return Objects.equals(gate, that.gate); }
        @Override public int hashCode() { return Objects.hash(gate); }
    }
}