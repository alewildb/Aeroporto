package aeroporto.aeroportoPackage.gui.dialogs;

import aeroporto.aeroportoPackage.model.Bagaglio;
import aeroporto.aeroportoPackage.model.Prenotazione;
import aeroporto.aeroportoPackage.model.Stato_Bagaglio; // Assicurati che sia importato

import javax.swing.*;
import java.awt.*;
import java.util.List;
// Nessuna modifica diretta necessaria qui per l'AppController,
// poiché la logica di interazione con AppController avviene nel pannello chiamante (UserHomePanel)
// dopo che questo dialogo ha selezionato il bagaglio.

public class LostBaggageDialog extends JDialog {
    private JComboBox<BagaglioWrapper> baggageComboBox;
    private JButton reportButton;
    private JButton cancelButton;
    private Bagaglio selectedBaggage = null;
    private boolean confirmed = false;

    public LostBaggageDialog(Frame owner, Prenotazione prenotazione, List<Bagaglio> bagagliDaMostrare) {
        super(owner, "Segnala Bagaglio Smarrito per Pren. " + (prenotazione != null ? prenotazione.getNumero_Biglietto() : "N/A"), true);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        if (bagagliDaMostrare == null || bagagliDaMostrare.isEmpty()) {
            // Questo caso dovrebbe essere gestito dal chiamante (UserHomePanel)
            // prima di aprire il dialogo, ma per sicurezza:
            JOptionPane.showMessageDialog(owner, "Nessun bagaglio idoneo da segnalare per questa prenotazione.", "Info", JOptionPane.INFORMATION_MESSAGE);
            // Assicura che il dispose avvenga dopo il costruttore
            SwingUtilities.invokeLater(this::dispose);
            return;
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Seleziona il bagaglio da segnalare come smarrito:"));
        add(topPanel, BorderLayout.NORTH);

        baggageComboBox = new JComboBox<>();
        for (Bagaglio b : bagagliDaMostrare) {
            // Mostra solo bagagli che NON sono già SMARRITO
            if (b.getStato_Bagaglio() != Stato_Bagaglio.SMARRITO) {
                baggageComboBox.addItem(new BagaglioWrapper(b));
            }
        }

        if (baggageComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(owner, "Tutti i bagagli per questa prenotazione sono già segnalati o non ci sono bagagli idonei.", "Info", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(this::dispose);
            return;
        }


        add(new JScrollPane(baggageComboBox), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        reportButton = new JButton("Segnala Smarrito");
        cancelButton = new JButton("Annulla");

        reportButton.addActionListener(e -> {
            BagaglioWrapper wrapper = (BagaglioWrapper) baggageComboBox.getSelectedItem();
            if (wrapper != null) {
                selectedBaggage = wrapper.getBagaglio();
                confirmed = true;
            }
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(reportButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(450, 180));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public Bagaglio getSelectedBaggage() {
        return selectedBaggage;
    }
    public boolean isConfirmed() {
        return confirmed;
    }

    // Wrapper per mostrare info utili del bagaglio nella JComboBox
    private static class BagaglioWrapper {
        private Bagaglio bagaglio;
        public BagaglioWrapper(Bagaglio bagaglio) { this.bagaglio = bagaglio; }
        public Bagaglio getBagaglio() { return bagaglio; }
        @Override public String toString() {
            if (bagaglio == null) return "Bagaglio non valido";
            return "Cod: " + bagaglio.getCodice_Bagaglio() + " (Stato: " + bagaglio.getStato_Bagaglio() + ")";
        }
    }
}