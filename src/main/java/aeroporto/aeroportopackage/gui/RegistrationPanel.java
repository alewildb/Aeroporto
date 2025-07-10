package aeroporto.aeroportopackage.gui;

import aeroporto.aeroportopackage.controller.AppController;
import aeroporto.aeroportopackage.dao.UtenteDAO;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * The Registration panel.
 */
public class RegistrationPanel extends JPanel {
    /**
     * The main GUI frame.
     */
    private final transient MainFrame mainFrame;
    /**
     * The application controller.
     */
    private final transient AppController appController;

    /**
     * Text field for the name.
     */
    private JTextField nomeField;
    /**
     * Text field for the surname.
     */
    private JTextField cognomeField;
    /**
     * Text field for the document number.
     */
    private JTextField numeroDocumentoField;
    /**
     * Text field for the username.
     */
    private JTextField usernameField;
    /**
     * Password field for the password.
     */
    private JPasswordField passwordField;
    /**
     * Password field for confirming the password.
     */
    private JPasswordField confirmPasswordField;
    /**
     * The register button.
     */
    private JButton registerButton;
    /**
     * The back to login button.
     */
    private JButton backToLoginButton;
    /**
     * Label for displaying errors.
     */
    private JLabel errorLabel;

    /**
     * Instantiates a new Registration panel.
     *
     * @param mainFrame     the main frame
     * @param appController the app controller
     */
    public RegistrationPanel(MainFrame mainFrame, AppController appController) {
        this.mainFrame = mainFrame;
        this.appController = appController;

        setLayout(new GridBagLayout());
        setBackground(new Color(230, 235, 240));

        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint paint = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(245, 248, 250));
                g2.setPaint(paint);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(new Color(210, 215, 220));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        formPanel.setPreferredSize(new Dimension(500, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Crea un Nuovo Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 20, 5);
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 5, 8, 5);

        nomeField = new JTextField(20);
        cognomeField = new JTextField(20);
        numeroDocumentoField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        addFormField(formPanel, gbc, "Nome:", nomeField, "edit-3.svg", 1);
        addFormField(formPanel, gbc, "Cognome:", cognomeField, "edit-3.svg", 2);
        addFormField(formPanel, gbc, "Numero Documento:", numeroDocumentoField, "hash.svg", 3);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        addFormField(formPanel, gbc, "Username:", usernameField, "user.svg", 5);
        addFormField(formPanel, gbc, "Password:", passwordField, "lock.svg", 6);
        addFormField(formPanel, gbc, "Conferma Password:", confirmPasswordField, "lock.svg", 7);

        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(errorLabel, gbc);

        registerButton = new JButton("Registrati");
        backToLoginButton = new JButton("Hai già un account? Accedi");

        registerButton.putClientProperty("JButton.buttonType", "roundRect");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(registerButton, gbc);

        backToLoginButton.setOpaque(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setForeground(Color.BLUE);
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 10;
        gbc.insets = new Insets(5, 5, 10, 5);
        formPanel.add(backToLoginButton, gbc);

        add(formPanel, new GridBagConstraints());

        registerButton.addActionListener(e -> performRegistration());
        backToLoginButton.addActionListener(e -> mainFrame.showLoginPanel());
    }

    /**
     * It's the function that gets called each time a form field is created. It's a helper function.
     * It creates an icon-text row for the form.
     * @param panel the panel it's in
     * @param gbc the layout constraints
     * @param labelText the label
     * @param field the field
     * @param iconName the icon
     * @param yPos the vertical position in the panel
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, String iconName, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(new FlatSVGIcon("icons/" + iconName)), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.putClientProperty("JTextField.placeholderText", labelText.replace(":", ""));
        panel.add(field, gbc);
    }

    /**
     * Executes the registration logic when the "Register" (Registrati) button is clicked.
     */
    private void performRegistration() {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String numDoc = numeroDocumentoField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        errorLabel.setText(" ");

        if (nome.isEmpty() || cognome.isEmpty() || numDoc.isEmpty() || username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Tutti i campi sono obbligatori.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Le password non coincidono.");
            passwordField.setText("");
            confirmPasswordField.setText("");
            passwordField.requestFocusInWindow();
            return;
        }

        UtenteDAO utenteDAO = appController.getUtenteDAO();

        if (utenteDAO.findByLogin(username) != null) {
            errorLabel.setText("Username già esistente. Scegline un altro.");
            usernameField.requestFocusInWindow();
            usernameField.selectAll();
            return;
        }

        try {
            utenteDAO.save(nome, cognome, numDoc, username, password);
            JOptionPane.showMessageDialog(this,
                    "Registrazione completata con successo!\nOra puoi effettuare il login.",
                    "Registrazione Riuscita",
                    JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showLoginPanel();
        } catch (RuntimeException ex) {
            errorLabel.setText("Errore di registrazione. Riprova.");
        }
    }

    /**
     * Clears fields.
     */
    public void clearFields() {
        nomeField.setText("");
        cognomeField.setText("");
        numeroDocumentoField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        errorLabel.setText(" ");
        nomeField.requestFocusInWindow();
    }
}