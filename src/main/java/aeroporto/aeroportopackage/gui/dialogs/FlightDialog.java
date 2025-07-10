package aeroporto.aeroportopackage.gui.dialogs;

import aeroporto.aeroportopackage.controller.AppController;
import aeroporto.aeroportopackage.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The Flight dialog. This dialog is crucial in the application.
 * It manages the insertion, update and visualization of a flight.
 */
public class FlightDialog extends JDialog {
    /**
     * The application controller.
     */
    private AppController appController;
    /**
     * The current flight being edited.
     */
    private Volo currentVolo;
    /**
     * Flag indicating if the dialog is in read-only mode.
     */
    private boolean readOnlyMode;
    /**
     * Flag indicating if data has been changed.
     */
    private boolean dataChanged = false;

    /**
     * Text field for the flight code.
     */
    private JTextField codiceField;
    /**
     * Text field for the airline company.
     */
    private JTextField compagniaField;
    /**
     * ComboBox for the flight type (Departure/Arrival).
     */
    private JComboBox<String> tipoVoloComboBox;
    /**
     * Text field for the departure airport.
     */
    private JTextField aeroportoPartenzaField;
    /**
     * Text field for the arrival airport.
     */
    private JTextField aeroportoArrivoField;
    /**
     * Day of the date.
     */
    private JTextField giornoField;
    /**
     * Month of the date.
     */
    private JTextField meseField;
    /**
     * Year of the date.
     */
    private JTextField annoField;
    /**
     * Text field for the scheduled time.
     */
    private JTextField orarioPrevistoField;
    /**
     * Text field for the delay minutes.
     */
    private JTextField minutiRitardoField;
    /**
     * ComboBox for the flight status.
     */
    private JComboBox<StatoVolo> statoVoloComboBox;
    /**
     * ComboBox for the assigned gate.
     */
    private JComboBox<GateWrapper> gateComboBox;
    /**
     * Label for the gate ComboBox.
     */
    private JLabel gateLabel;

    /**
     * The save button.
     */
    private JButton saveButton;
    /**
     * The cancel button.
     */
    private JButton cancelButton;
    /**
     * sdfgsdfg
     */
    private final String validError = "Errore Validazione";
    /**
     * sdfgsdfg
     */
    private final String partenza = "Partenza";


    /**
     * Instantiates a new Flight dialog.
     *
     * @param owner         the owner
     * @param appController the app controller
     * @param voloToEdit    the volo to edit
     * @param readOnly      the read only
     */
    public FlightDialog(Frame owner, AppController appController, Volo voloToEdit, boolean readOnly) {
        super(owner, true);
        this.appController = appController;
        this.currentVolo = voloToEdit;
        this.readOnlyMode = readOnly;

        String title = (currentVolo == null) ? "Inserisci Nuovo Volo" :
                (readOnly ? "Dettagli Volo: " : "Modifica Volo: ") + (currentVolo != null ? currentVolo.getCodiceUnivoco() : "");
        setTitle(title);

        initComponents();
        populateFields();
        setupInteractions();

        if (readOnlyMode) {
            setFieldsEditable(false);
            saveButton.setVisible(false);
            cancelButton.setText("Chiudi");
        }

        pack();
        setMinimumSize(new Dimension(500, getHeight() + 20));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    /**
     * Initializes dialog's components.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;

        int yPos = 0;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Codice Volo:"), gbc);
        codiceField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(codiceField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Compagnia Aerea:"), gbc);
        compagniaField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(compagniaField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Tipo Volo:"), gbc);
        tipoVoloComboBox = new JComboBox<>(new String[]{partenza, "Arrivo"});
        tipoVoloComboBox.addActionListener(e -> setupInteractions());
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(tipoVoloComboBox, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Aeroporto Partenza:"), gbc);
        aeroportoPartenzaField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(aeroportoPartenzaField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Aeroporto Arrivo:"), gbc);
        aeroportoArrivoField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(aeroportoArrivoField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Data (G-M-A):"), gbc);
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        giornoField = new JTextField(3); meseField = new JTextField(3); annoField = new JTextField(5);
        datePanel.add(giornoField); datePanel.add(new JLabel("-")); datePanel.add(meseField);
        datePanel.add(new JLabel("-")); datePanel.add(annoField);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(datePanel, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Orario Previsto (HH:MM):"), gbc);
        orarioPrevistoField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(orarioPrevistoField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Minuti Ritardo:"), gbc);
        minutiRitardoField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(minutiRitardoField, gbc); gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Stato Volo:"), gbc);
        statoVoloComboBox = new JComboBox<>(StatoVolo.values());
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(statoVoloComboBox, gbc); gbc.gridwidth = 1;

        gateLabel = new JLabel("Gate Assegnato:");
        gbc.gridx = 0; gbc.gridy = yPos; add(gateLabel, gbc);
        gateComboBox = new JComboBox<>();
        List<Gate> gatesDisponibili = appController.getGateDAO().findAll();
        gateComboBox.addItem(new GateWrapper(null));
        for (Gate gate : gatesDisponibili) {
            gateComboBox.addItem(new GateWrapper(gate));
        }
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.gridwidth = 2; add(gateComboBox, gbc); gbc.gridwidth = 1;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        saveButton = new JButton("Salva");
        cancelButton = new JButton("Annulla");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; add(buttonPanel, gbc);

        saveButton.addActionListener(e -> saveFlight());
        cancelButton.addActionListener(e -> dispose());
    }
    /**
     * Populates the fields with the flight's value if the flight exists.
     * If the flight does not exist, the fields get initialized with default values.
     * The function checks if the flight is an arrival or a departure.
     */
    private void populateFields() {
        if (currentVolo != null) {
            codiceField.setText(currentVolo.getCodiceUnivoco());
            compagniaField.setText(currentVolo.getCompagniaAerea());
            aeroportoPartenzaField.setText(currentVolo.getAeroportoPartenza());
            aeroportoArrivoField.setText(currentVolo.getAeroportoArrivo());
            giornoField.setText(String.valueOf(currentVolo.getGiornoPartenza()));
            meseField.setText(String.valueOf(currentVolo.getMesePartenza()));
            annoField.setText(String.valueOf(currentVolo.getAnnoPartenza()));
            orarioPrevistoField.setText(currentVolo.getOrarioPrevisto() != null ? currentVolo.getOrarioPrevisto() : "00:00");
            minutiRitardoField.setText(String.valueOf(currentVolo.getMinutiRitardo()));
            statoVoloComboBox.setSelectedItem(currentVolo.getStatoVolo());

            if (currentVolo instanceof VoloPartenza) {
                tipoVoloComboBox.setSelectedItem(partenza);
                Gate currentGate = ((VoloPartenza) currentVolo).getGate();
                gateComboBox.setSelectedItem(new GateWrapper(currentGate));
            } else if (currentVolo instanceof VoloArrivo) {
                tipoVoloComboBox.setSelectedItem("Arrivo");
                gateComboBox.setSelectedItem(new GateWrapper(null));
            }
        } else {
            tipoVoloComboBox.setSelectedItem(partenza);
            statoVoloComboBox.setSelectedItem(StatoVolo.PROGRAMMATO);
            orarioPrevistoField.setText("12:00");
            minutiRitardoField.setText("0");
        }
    }
    /**
     * Sets the dynamic interactions of the window.
     * It checks if the flight is an arrival or a departure in order to make the "Napoli" string from the
     * destination/arrival airport not editable. It also shows or hides the gate selection.
     */
    private void setupInteractions() {
        boolean isPartenza = partenza.equals(tipoVoloComboBox.getSelectedItem());
        gateLabel.setVisible(isPartenza);
        gateComboBox.setVisible(isPartenza);
        gateComboBox.setEnabled(isPartenza && !readOnlyMode);

        if (!readOnlyMode) {
            if (currentVolo == null || tipoVoloComboBox.isEnabled()) {
                if (isPartenza) {
                    aeroportoPartenzaField.setText("Napoli");
                    aeroportoPartenzaField.setEditable(false);
                    aeroportoArrivoField.setEditable(true);
                    if (currentVolo == null) aeroportoArrivoField.setText("");
                } else {
                    aeroportoArrivoField.setText("Napoli");
                    aeroportoArrivoField.setEditable(false);
                    aeroportoPartenzaField.setEditable(true);
                    if (currentVolo == null) aeroportoPartenzaField.setText("");
                }
            } else {
                if (currentVolo instanceof VoloPartenza) {
                    aeroportoPartenzaField.setEditable(false);
                    aeroportoArrivoField.setEditable(true);
                } else if (currentVolo instanceof VoloArrivo) {
                    aeroportoArrivoField.setEditable(false);
                    aeroportoPartenzaField.setEditable(true);
                }
            }
        }
        codiceField.setEditable(currentVolo == null && !readOnlyMode);
        tipoVoloComboBox.setEnabled(currentVolo == null && !readOnlyMode);
    }

    /**
     * Checks if the entered date is valid.
     * @param anno The year.
     * @param mese The month.
     * @param giorno The day.
     * @return true if the date is valid, false otherwise.
     */
    private boolean isDataValida(int anno, int mese, int giorno) {
        if (anno <= 0 || mese <= 0 || giorno <= 0) {
            return false;
        }
        try {
            LocalDate.of(anno, mese, giorno);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the time format is valid (HH:MM).
     * @param orario The time string to validate.
     * @return true if the time is valid, false otherwise.
     */
    private boolean isOrarioValido(String orario) {
        if (orario == null || orario.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$", orario);
    }
    /**
     * Saves form's data into the database.
     * It's used to either update or insert the flight.
     * It checks if the flight is an arrival or a departure and sets the flight type variable.
     */
    private void saveFlight() {
        try {
            String codice = codiceField.getText().trim();
            if (codice.isEmpty()){
                JOptionPane.showMessageDialog(this, "Il codice del volo è obbligatorio.", validError, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (currentVolo == null && appController.getVoloDAO().findByCodice(codice) != null) {
                JOptionPane.showMessageDialog(this, "Codice volo già esistente.", validError, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int giorno = Integer.parseInt(giornoField.getText());
            int mese = Integer.parseInt(meseField.getText());
            int anno = Integer.parseInt(annoField.getText());

            if (!isDataValida(anno, mese, giorno)) {
                JOptionPane.showMessageDialog(this, "Data non valida. Controllare giorno, mese e anno.", "Errore Data", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String orario = orarioPrevistoField.getText().trim();
            if (!isOrarioValido(orario)) {
                JOptionPane.showMessageDialog(this, "Formato orario non valido. Usare HH:MM (es. 14:30).", "Errore Orario", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Volo voloDaSalvare;
            String tipoVolo = (String) tipoVoloComboBox.getSelectedItem();

            if (partenza.equals(tipoVolo)) {
                GateWrapper gateWrapper = (GateWrapper) gateComboBox.getSelectedItem();
                Gate gate = (gateWrapper != null) ? gateWrapper.getGate() : null;
                if (gate == null) {
                    JOptionPane.showMessageDialog(this, "Un volo di partenza deve avere un gate.", validError, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                voloDaSalvare = new VoloPartenza(codice, compagniaField.getText().trim(), aeroportoArrivoField.getText().trim(),
                        giorno, mese, anno,
                        orario, Integer.parseInt(minutiRitardoField.getText()), gate);
            } else {
                voloDaSalvare = new VoloArrivo(codice, compagniaField.getText().trim(), aeroportoPartenzaField.getText().trim(),
                        giorno, mese, anno,
                        orario, Integer.parseInt(minutiRitardoField.getText()));
            }

            voloDaSalvare.setStatoVolo((StatoVolo) statoVoloComboBox.getSelectedItem());

            if (currentVolo == null) {
                appController.getVoloDAO().save(voloDaSalvare);
            } else {
                appController.getVoloDAO().update(voloDaSalvare);
            }

            dataChanged = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giorno, Mese, Anno e Ritardo devono essere numeri validi.", "Errore Formato Numerico", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage(), "Errore Operazione", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets the fields editable or not editable based on what the user is doing.
     * If it's the admin editing the flight, the fields are editable, otherwise they're not.
     * @param editable true if fields should be editable, false otherwise.
     */
    private void setFieldsEditable(boolean editable) {
        codiceField.setEditable(editable && currentVolo == null);
        compagniaField.setEditable(editable);
        if (!editable) {
            aeroportoPartenzaField.setEditable(false);
            aeroportoArrivoField.setEditable(false);
            gateComboBox.setEnabled(false);
        } else {
            setupInteractions();
        }
        giornoField.setEditable(editable);
        meseField.setEditable(editable);
        annoField.setEditable(editable);
        orarioPrevistoField.setEditable(editable);
        minutiRitardoField.setEditable(editable);
        statoVoloComboBox.setEnabled(editable);
        tipoVoloComboBox.setEnabled(editable && currentVolo == null);
    }
    /**
     * Checks if the data has changed.
     *
     * @return the boolean, 1 if they got changed, 0 otherwise.
     */
    public boolean isDataChanged() {
        return dataChanged;
    }
    /**
     * This is an internal class (a wrapper class) used to visualize the gate objects in the combobox and to handle the
     * "no gate" ("-- Nessun Gate --") scenario.
     */
    private static class GateWrapper {
        /**
         * The Gate object.
         */
        private Gate gate;
        /**
         * Instantiates a new Gate wrapper.
         *
         * @param gate the gate
         */
        public GateWrapper(Gate gate) { this.gate = gate; }
        /**
         * Gets gate.
         *
         * @return the gate
         */
        public Gate getGate() { return gate; }
        @Override
        public String toString() {
            return gate == null ? "-- Nessun Gate --" : "Gate " + gate.getNumeroGate();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GateWrapper that = (GateWrapper) o;
            return Objects.equals(gate, that.gate);
        }
        @Override public int hashCode() {
            return Objects.hash(gate);
        }
    }
}