package aeroporto.aeroportopackage.gui.dialogs;

import aeroporto.aeroportopackage.controller.AppController;
import aeroporto.aeroportopackage.model.Bagaglio;
import aeroporto.aeroportopackage.model.Prenotazione;
import aeroporto.aeroportopackage.model.StatoBagaglio;
import aeroporto.aeroportopackage.model.Volo;

import javax.swing.*;
import java.awt.*;

/**
 * The Admin Update Baggage dialog.
 */
public class BaggageDialog extends JDialog {
    /**
     * The application controller.
     */
    private AppController appController;
    /**
     * The current baggage being edited.
     */
    private Bagaglio currentBagaglio;
    /**
     * Flag indicating if data has been changed.
     */
    private boolean dataChanged = false;

    /**
     * Text field for the baggage code.
     */
    private JTextField codiceBagaglioField;
    /**
     * Text field for the ticket number.
     */
    private JTextField numeroBigliettoField;
    /**
     * Text field for the passenger name.
     */
    private JTextField passeggeroField;
    /**
     * Text field for the flight info.
     */
    private JTextField infoVoloField;
    /**
     * ComboBox for selecting the baggage status.
     */
    private JComboBox<StatoBagaglio> statoBagaglioComboBox;
    /**
     * The save button.
     */
    private JButton saveButton;
    /**
     * The cancel button.
     */
    private JButton cancelButton;

    /**
     * Instantiates a new Baggage dialog.
     *
     * @param owner          the owner
     * @param appController  the app controller
     * @param bagaglioToEdit the bagaglio to edit
     */
    public BaggageDialog(Frame owner, AppController appController, Bagaglio bagaglioToEdit) {
        super(owner, "Aggiorna Stato Bagaglio: " + (bagaglioToEdit != null ? bagaglioToEdit.getCodiceBagaglio() : "N/A"), true);
        this.appController = appController;
        this.currentBagaglio = bagaglioToEdit;

        if (this.currentBagaglio == null) {
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

    /**
     * Initializes all dialog windows' components.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;

        int yPos = 0;

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Codice Bagaglio:"), gbc);
        codiceBagaglioField = new JTextField(10);
        codiceBagaglioField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(codiceBagaglioField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Numero Biglietto:"), gbc);
        numeroBigliettoField = new JTextField(15);
        numeroBigliettoField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(numeroBigliettoField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Passeggero:"), gbc);
        passeggeroField = new JTextField(20);
        passeggeroField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(passeggeroField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Info Volo:"), gbc);
        infoVoloField = new JTextField(25);
        infoVoloField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = yPos++; add(infoVoloField, gbc);

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Nuovo Stato:"), gbc);
        statoBagaglioComboBox = new JComboBox<>(StatoBagaglio.values());
        gbc.gridx = 1; gbc.gridy = yPos++; add(statoBagaglioComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        saveButton = new JButton("Salva Stato");
        cancelButton = new JButton("Annulla");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; add(buttonPanel, gbc);

        saveButton.addActionListener(e -> saveBaggageState());
        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Populates the fields in the update baggage dialog (admin side).
     */
    private void populateFields() {
        if (currentBagaglio != null) {
            codiceBagaglioField.setText(String.valueOf(currentBagaglio.getCodiceBagaglio()));
            Prenotazione p = currentBagaglio.getPrenotazione();
            if (p != null) {
                numeroBigliettoField.setText(p.getNumeroBiglietto() != null ? p.getNumeroBiglietto() : "N/D");
                passeggeroField.setText(p.getNomePasseggero() + " " + p.getCognomePasseggero());

                String codiceVoloEstratto = AppController.extractCodiceVoloFromNumeroBiglietto(p.getNumeroBiglietto());
                if (codiceVoloEstratto != null) {
                    Volo voloAssociato = appController.getVoloDAO().findByCodice(codiceVoloEstratto);
                    if (voloAssociato != null) {
                        infoVoloField.setText(voloAssociato.getCodiceUnivoco() + " (" +
                                voloAssociato.getAeroportoPartenza() + " - " +
                                voloAssociato.getAeroportoArrivo() + ")");
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
            statoBagaglioComboBox.setSelectedItem(currentBagaglio.getStatoBagaglio());
        }
    }

    /**
     * Saves the current baggage information in the database.
     * This method is invoked when the "Save" button is pressed.
     */
    private void saveBaggageState() {
        if (currentBagaglio != null) {
            StatoBagaglio nuovoStato = (StatoBagaglio) statoBagaglioComboBox.getSelectedItem();
            try {
                appController.getBagaglioDAO().update(currentBagaglio.getCodiceBagaglio(), nuovoStato);
                dataChanged = true;
                dispose();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Errore during l'aggiornamento: " + e.getMessage(), "Errore Salvataggio", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Checks if the data has changed.
     *
     * @return the boolean, 1 if they got changed, 0 otherwise.
     */
    public boolean isDataChanged() {
        return dataChanged;
    }
}