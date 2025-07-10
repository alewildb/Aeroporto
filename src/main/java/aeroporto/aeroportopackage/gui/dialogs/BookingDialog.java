package aeroporto.aeroportopackage.gui.dialogs;

import aeroporto.aeroportopackage.model.UtenteGenerico;
import aeroporto.aeroportopackage.model.Volo;

import javax.swing.*;
import java.awt.*;

/**
 * The Booking dialog that appears when the user is booking a flight.
 * The user can book a flight for himself or for someone else.
 */
public class BookingDialog extends JDialog {
    /**
     * The user who is booking.
     */
    private UtenteGenerico utentePrenotante;
    /**
     * The selected flight.
     */
    private Volo voloSelezionato;
    /**
     * Text field for the passenger's name.
     */
    private JTextField nomePasseggeroField;
    /**
     * Text field for the passenger's surname.
     */
    private JTextField cognomePasseggeroField;
    /**
     * Text field for the passenger's document number.
     */
    private JTextField numeroDocumentoField;
    /**
     * Text field for the desired seat.
     */
    private JTextField postoField;
    /**
     * Checkbox to book for oneself.
     */
    private JCheckBox prenotaPerMeCheckBox;

    /**
     * The confirm button.
     */
    private JButton confermaButton;
    /**
     * The cancel button.
     */
    private JButton annullaButton;

    /**
     * Flag indicating if the booking is confirmed.
     */
    private boolean prenotazioneConfermata = false;
    /**
     * The passenger's name.
     */
    private String nomePasseggero;
    /**
     * The passenger's surname.
     */
    private String cognomePasseggero;
    /**
     * The passenger's document number.
     */
    private String numeroDocumento;
    /**
     * The passenger's assigned seat.
     */
    private String postoAssegnato;

    /**
     * Instantiates a new Booking dialog.
     *
     * @param owner         the owner
     * @param volo          the volo
     * @param utenteLoggato the utente loggato
     */
    public BookingDialog(Frame owner, Volo volo, UtenteGenerico utenteLoggato) {
        super(owner, "Dettagli Passeggero per Volo: " + (volo != null ? volo.getCodiceUnivoco() : "N/A"), true);
        this.voloSelezionato = volo;
        this.utentePrenotante = utenteLoggato;

        initComponents();
        pack();
        setMinimumSize(new Dimension(450, 300));
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

        prenotaPerMeCheckBox = new JCheckBox("Prenota per me stesso/a");
        prenotaPerMeCheckBox.setSelected(true);
        prenotaPerMeCheckBox.addActionListener(e -> fillFieldsForSelf(prenotaPerMeCheckBox.isSelected()));
        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 2; add(prenotaPerMeCheckBox, gbc);
        gbc.gridwidth = 1; yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Nome Passeggero:"), gbc);
        nomePasseggeroField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; add(nomePasseggeroField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Cognome Passeggero:"), gbc);
        cognomePasseggeroField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; add(cognomePasseggeroField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Numero Documento:"), gbc);
        numeroDocumentoField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; add(numeroDocumentoField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Posto Desiderato (opz.):"), gbc);
        postoField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = yPos++; add(postoField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confermaButton = new JButton("Conferma Prenotazione");
        annullaButton = new JButton("Annulla");
        buttonPanel.add(confermaButton);
        buttonPanel.add(annullaButton);

        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        add(buttonPanel, gbc);

        confermaButton.addActionListener(e -> {
            if (validateInput()) {
                this.nomePasseggero = nomePasseggeroField.getText().trim();
                this.cognomePasseggero = cognomePasseggeroField.getText().trim();
                this.numeroDocumento = numeroDocumentoField.getText().trim();
                this.postoAssegnato = postoField.getText().trim().toUpperCase();
                if (this.postoAssegnato.isEmpty()) {
                    this.postoAssegnato = null;
                }
                this.prenotazioneConfermata = true;
                dispose();
            }
        });

        annullaButton.addActionListener(e -> dispose());

        fillFieldsForSelf(prenotaPerMeCheckBox.isSelected());
    }

    /**
     * Sets the dialog fields to the users' data when he's booking the flight for himself.
     * Empties the fields when he wants to book the flight for someone else.
     * @param forSelf true if booking for self, false otherwise.
     */
    private void fillFieldsForSelf(boolean forSelf) {
        if (utentePrenotante == null) return;

        if (forSelf) {
            nomePasseggeroField.setText(utentePrenotante.getNome());
            cognomePasseggeroField.setText(utentePrenotante.getCognome());
            numeroDocumentoField.setText(utentePrenotante.getNumeroDocumento());
            nomePasseggeroField.setEditable(false);
            cognomePasseggeroField.setEditable(false);
            numeroDocumentoField.setEditable(false);
        } else {
            nomePasseggeroField.setText("");
            cognomePasseggeroField.setText("");
            numeroDocumentoField.setText("");
            nomePasseggeroField.setEditable(true);
            cognomePasseggeroField.setEditable(true);
            numeroDocumentoField.setEditable(true);
        }
    }

    /**
     * Checks if all the necessary data is inserted.
     * @return returns true if the input is valid, false otherwise.
     */
    private boolean validateInput() {
        if (nomePasseggeroField.getText().trim().isEmpty() ||
                cognomePasseggeroField.getText().trim().isEmpty() ||
                numeroDocumentoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, Cognome e Numero Documento del passeggero sono obbligatori.", "Input Incompleto", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Checks if the booking is confirmed.
     *
     * @return the boolean
     */
    public boolean isPrenotazioneConfermata() { return prenotazioneConfermata; }

    /**
     * Gets booked user's name.
     *
     * @return passenger's name.
     */
    public String getNomePasseggero() { return nomePasseggero; }

    /**
     * Gets booked user's surname.
     *
     * @return passenger's surname.
     */
    public String getCognomePasseggero() { return cognomePasseggero; }

    /**
     * Gets booked user's document id.
     *
     * @return passenger's document id.
     */
    public String getNumeroDocumento() { return numeroDocumento; }

    /**
     * Gets booked user's assigned airplane seat.
     *
     * @return passenger's assigned seat.
     */
    public String getPostoAssegnato() { return postoAssegnato; }
}