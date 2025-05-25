package aeroporto.aeroportoPackage.gui.dialogs;

import aeroporto.aeroportoPackage.model.Utente_Generico;
import aeroporto.aeroportoPackage.model.Volo;

import javax.swing.*;
import java.awt.*;
// Nessuna modifica diretta necessaria qui per l'AppController,
// poiché la logica di interazione con AppController avviene nel pannello chiamante (UserHomePanel)
// dopo che questo dialogo ha raccolto i dati.

public class BookingDialog extends JDialog {
    private Volo voloSelezionato;
    private Utente_Generico utentePrenotante; // L'utente che sta effettuando la prenotazione (loggato)

    private JTextField nomePasseggeroField;
    private JTextField cognomePasseggeroField;
    private JTextField numeroDocumentoField;
    private JTextField postoField;
    private JCheckBox prenotaPerMeCheckBox;

    private JButton confermaButton;
    private JButton annullaButton;

    private boolean prenotazioneConfermata = false;
    private String nomePasseggero, cognomePasseggero, numeroDocumento, postoAssegnato;

    public BookingDialog(Frame owner, Volo volo, Utente_Generico utenteLoggato) {
        super(owner, "Dettagli Passeggero per Volo: " + (volo != null ? volo.getCodice_Univoco() : "N/A"), true);
        this.voloSelezionato = volo;
        this.utentePrenotante = utenteLoggato; // Utente che sta usando l'applicazione

        initComponents();
        pack();
        setMinimumSize(new Dimension(450, 300));
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

        // CheckBox per pre-compilare con i dati dell'utente loggato
        prenotaPerMeCheckBox = new JCheckBox("Prenota per me stesso/a");
        prenotaPerMeCheckBox.setSelected(true);
        prenotaPerMeCheckBox.addActionListener(e -> fillFieldsForSelf(prenotaPerMeCheckBox.isSelected()));
        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 2; add(prenotaPerMeCheckBox, gbc);
        gbc.gridwidth = 1; yPos++;


        // Nome Passeggero
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Nome Passeggero:"), gbc);
        nomePasseggeroField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; add(nomePasseggeroField, gbc);

        // Cognome Passeggero
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Cognome Passeggero:"), gbc);
        cognomePasseggeroField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; add(cognomePasseggeroField, gbc);

        // Numero Documento
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Numero Documento:"), gbc);
        numeroDocumentoField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = yPos++; add(numeroDocumentoField, gbc);

        // Posto Assegnato
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Posto Desiderato (opz.):"), gbc);
        postoField = new JTextField(5);
        gbc.gridx = 1; gbc.gridy = yPos++; add(postoField, gbc);

        // Pulsanti
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
                    this.postoAssegnato = null; // O un valore di default se necessario
                }
                this.prenotazioneConfermata = true;
                dispose();
            }
        });

        annullaButton.addActionListener(e -> dispose());

        // Chiamare fillFieldsForSelf dopo che i campi sono stati inizializzati
        fillFieldsForSelf(prenotaPerMeCheckBox.isSelected());
    }

    private void fillFieldsForSelf(boolean forSelf) {
        if (utentePrenotante == null) return; // Safety check

        if (forSelf) {
            nomePasseggeroField.setText(utentePrenotante.getNome());
            cognomePasseggeroField.setText(utentePrenotante.getCognome());
            numeroDocumentoField.setText(utentePrenotante.getNumero_Documento());
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


    private boolean validateInput() {
        if (nomePasseggeroField.getText().trim().isEmpty() ||
                cognomePasseggeroField.getText().trim().isEmpty() ||
                numeroDocumentoField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, Cognome e Numero Documento del passeggero sono obbligatori.", "Input Incompleto", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Ulteriori validazioni (es. formato documento, unicità posto se gestita qui) possono essere aggiunte
        return true;
    }

    public boolean isPrenotazioneConfermata() { return prenotazioneConfermata; }
    public String getNomePasseggero() { return nomePasseggero; }
    public String getCognomePasseggero() { return cognomePasseggero; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public String getPostoAssegnato() { return postoAssegnato; }
    public Volo getVoloSelezionato() { return voloSelezionato; }
    public Utente_Generico getUtentePrenotante() { return utentePrenotante; }
}