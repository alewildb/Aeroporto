package aeroporto.aeroportoPackage.gui.dialogs;

import aeroporto.aeroportoPackage.controller.AppController;
import aeroporto.aeroportoPackage.model.Bagaglio;
import aeroporto.aeroportoPackage.model.Prenotazione;
import aeroporto.aeroportoPackage.model.Stato_Bagaglio;
import aeroporto.aeroportoPackage.model.Volo;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class BaggageDialog extends JDialog {
    private AppController appController;
    private Bagaglio currentBagaglio; // Il bagaglio da modificare
    private boolean dataChanged = false;

    // Componenti UI
    private JTextField codiceBagaglioField;
    private JTextField numeroBigliettoField;
    private JTextField passeggeroField;
    private JTextField infoVoloField;
    private JComboBox<Stato_Bagaglio> statoBagaglioComboBox;

    private JButton saveButton;
    private JButton cancelButton;

    public BaggageDialog(Frame owner, AppController appController, Bagaglio bagaglioToEdit) {
        super(owner, "Aggiorna Stato Bagaglio: " + (bagaglioToEdit != null ? bagaglioToEdit.getCodice_Bagaglio() : "N/A"), true);
        this.appController = appController;
        this.currentBagaglio = bagaglioToEdit;

        if (this.currentBagaglio == null) {
            // Non dovrebbe succedere se il dialogo è chiamato correttamente
            JOptionPane.showMessageDialog(owner, "Errore: Bagaglio non specificato.", "Errore Interno", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(this::dispose);
            return;
        }

        initComponents();
        populateFields();

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

        // Codice Bagaglio (Non editabile)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Codice Bagaglio:"), gbc);
        codiceBagaglioField = new JTextField(10);
        codiceBagaglioField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(codiceBagaglioField, gbc);

        // Numero Biglietto della Prenotazione associata (Non editabile)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Numero Biglietto:"), gbc);
        numeroBigliettoField = new JTextField(15);
        numeroBigliettoField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(numeroBigliettoField, gbc);

        // Passeggero della Prenotazione (Non editabile)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Passeggero:"), gbc);
        passeggeroField = new JTextField(20);
        passeggeroField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(passeggeroField, gbc);

        // Info Volo (Codice e Tratta - Non editabile)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Info Volo:"), gbc);
        infoVoloField = new JTextField(25);
        infoVoloField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(infoVoloField, gbc);

        // Stato Bagaglio (Editabile)
        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Nuovo Stato:"), gbc);
        statoBagaglioComboBox = new JComboBox<>(Stato_Bagaglio.values());
        gbc.gridx = 1; gbc.gridy = yPos++; add(statoBagaglioComboBox, gbc);


        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        saveButton = new JButton("Salva Stato");
        cancelButton = new JButton("Annulla");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; add(buttonPanel, gbc);

        saveButton.addActionListener(e -> saveBaggageState());
        cancelButton.addActionListener(e -> dispose());
    }

    private void populateFields() {
        if (currentBagaglio != null) {
            codiceBagaglioField.setText(String.valueOf(currentBagaglio.getCodice_Bagaglio()));
            Prenotazione p = currentBagaglio.getPrenotazione();
            if (p != null) {
                numeroBigliettoField.setText(p.getNumero_Biglietto() != null ? p.getNumero_Biglietto() : "N/D");
                passeggeroField.setText(p.getNome_Passeggero() + " " + p.getCognome_Passeggero());

                String codiceVoloEstratto = AppController.extractCodiceVoloFromNumeroBiglietto(p.getNumero_Biglietto());
                if (codiceVoloEstratto != null) {
                    Optional<Volo> voloOpt = appController.internalFindVoloByCodice(codiceVoloEstratto);
                    if (voloOpt.isPresent()) {
                        Volo voloAssociato = voloOpt.get();
                        infoVoloField.setText(voloAssociato.getCodice_Univoco() + " (" +
                                voloAssociato.getAeroporto_Partenza() + " - " +
                                voloAssociato.getAeroporto_Arrivo() + ")");
                    } else {
                        infoVoloField.setText("Volo " + codiceVoloEstratto + " non trovato");
                    }
                } else {
                    infoVoloField.setText("Info volo non disponibili dalla prenotazione");
                }
            } else {
                numeroBigliettoField.setText("Prenotazione N/D");
                passeggeroField.setText("Passeggero N/D");
                infoVoloField.setText("Info Volo N/D");
            }
            statoBagaglioComboBox.setSelectedItem(currentBagaglio.getStato_Bagaglio());
        }
    }

    private void saveBaggageState() {
        if (currentBagaglio != null) {
            Stato_Bagaglio nuovoStato = (Stato_Bagaglio) statoBagaglioComboBox.getSelectedItem();

            // Creiamo un oggetto Bagaglio temporaneo con il nuovo stato per passarlo al controller,
            // oppure modifichiamo currentBagaglio e lo passiamo.
            // Il metodo del modello Amministratore.aggiorna_Bagaglio prende codice e nuovo stato.
            // AppController.handleAdminAggiornaStatoBagaglio prende l'oggetto bagaglio.

            // Per coerenza con AppController.handleAdminAggiornaStatoBagaglio(Bagaglio bagaglio)
            // modifichiamo l'oggetto corrente e lo passiamo.
            currentBagaglio.setStato_Bagaglio(nuovoStato);

            if (appController.handleAdminAggiornaStatoBagaglio(currentBagaglio)) {
                dataChanged = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento dello stato del bagaglio.", "Errore Salvataggio", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isDataChanged() {
        return dataChanged;
    }
}