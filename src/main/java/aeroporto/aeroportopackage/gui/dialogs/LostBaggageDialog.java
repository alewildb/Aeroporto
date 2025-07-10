package aeroporto.aeroportopackage.gui.dialogs;

import aeroporto.aeroportopackage.model.Bagaglio;
import aeroporto.aeroportopackage.model.Prenotazione;
import aeroporto.aeroportopackage.model.StatoBagaglio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The Lost baggage dialog.
 */
public class LostBaggageDialog extends JDialog {
    /**
     * ComboBox for selecting the baggage to report.
     */
    private JComboBox<BagaglioWrapper> baggageComboBox;
    /**
     * The report button.
     */
    private JButton reportButton;
    /**
     * The cancel button.
     */
    private JButton cancelButton;
    /**
     * The selected baggage.
     */
    private Bagaglio selectedBaggage = null;
    /**
     * Flag indicating if the report is confirmed.
     */
    private boolean confirmed = false;

    /**
     * Instantiates a new Lost baggage dialog.
     *
     * @param owner             the owner
     * @param prenotazione      the prenotazione
     * @param bagagliDaMostrare the bagagli da mostrare
     */
    public LostBaggageDialog(Frame owner, Prenotazione prenotazione, List<Bagaglio> bagagliDaMostrare) {
        super(owner, "Segnala Bagaglio Smarrito per Pren. " + (prenotazione != null ? prenotazione.getNumeroBiglietto() : "N/A"), true);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        if (bagagliDaMostrare == null || bagagliDaMostrare.isEmpty()) {
            JOptionPane.showMessageDialog(owner, "Nessun bagaglio idoneo da segnalare per questa prenotazione.", "Info", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(this::dispose);
            return;
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Seleziona il bagaglio da segnalare come smarrito:"));
        add(topPanel, BorderLayout.NORTH);

        baggageComboBox = new JComboBox<>();
        for (Bagaglio b : bagagliDaMostrare) {
            if (b.getStatoBagaglio() != StatoBagaglio.SMARRITO) {
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

    /**
     * Gets selected baggage.
     *
     * @return the selected baggage
     */
    public Bagaglio getSelectedBaggage() {
        return selectedBaggage;
    }

    /**
     * Checks if the user has confirmed the loss of the baggage.
     *
     * @return the boolean
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * This is an internal class (a wrapper class) used to visualize the baggage objects in the combobox.
     */
    private static class BagaglioWrapper {
        /**
         * The Bagaglio object.
         */
        private Bagaglio bagaglio;

        /**
         * Instantiates a new Bagaglio wrapper.
         *
         * @param bagaglio the bagaglio
         */
        public BagaglioWrapper(Bagaglio bagaglio) { this.bagaglio = bagaglio; }

        /**
         * Gets bagaglio.
         *
         * @return the bagaglio
         */
        public Bagaglio getBagaglio() { return bagaglio; }
        @Override public String toString() {
            if (bagaglio == null) return "Bagaglio non valido";
            return "Cod: " + bagaglio.getCodiceBagaglio() + " (Stato: " + bagaglio.getStatoBagaglio() + ")";
        }
    }
}