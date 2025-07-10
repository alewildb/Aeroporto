package aeroporto.aeroportopackage.gui;

import aeroporto.aeroportopackage.controller.AppController;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * The Login panel.
 */
public class LoginPanel extends JPanel {
    /**
     * The main GUI frame.
     */
    private MainFrame mainFrame;
    /**
     * The application controller.
     */
    private AppController appController;

    /**
     * Text field for the username.
     */
    private JTextField usernameField;
    /**
     * Password field for the password.
     */
    private JPasswordField passwordField;
    /**
     * The login button.
     */
    private JButton loginButton;
    /**
     * The register button.
     */
    private JButton registerButton;
    /**
     * Label for displaying errors.
     */
    private JLabel errorLabel;

    /**
     * Instantiates a new Login panel.
     *
     * @param mainFrame     the main frame
     * @param appController the app controller
     */
    public LoginPanel(MainFrame mainFrame, AppController appController) {
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
        formPanel.setPreferredSize(new Dimension(450, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        JLabel titleLabel = new JLabel("Bentornato", SwingConstants.CENTER);
        titleLabel.setIcon(new FlatSVGIcon("icons/log-in.svg", 32, 32));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setIconTextGap(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 30, 5);
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 5, 10, 5);

        JLabel userIcon = new JLabel(new FlatSVGIcon("icons/user.svg"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(userIcon, gbc);

        usernameField = new JTextField(20);
        usernameField.putClientProperty("JTextField.placeholderText", "Username");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);

        JLabel passIcon = new JLabel(new FlatSVGIcon("icons/lock.svg"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passIcon, gbc);

        passwordField = new JPasswordField(20);
        passwordField.putClientProperty("JTextField.placeholderText", "Password"); // Testo segnaposto
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(errorLabel, gbc);

        loginButton = new JButton("Login");
        loginButton.putClientProperty("JButton.buttonType", "roundRect"); // Stile bottone
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(loginButton, gbc);

        registerButton = new JButton("Non hai un account? Registrati");
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setForeground(Color.BLUE);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 10, 5);
        formPanel.add(registerButton, gbc);

        add(formPanel, new GridBagConstraints());

        LoginActionListener loginActionListener = new LoginActionListener();
        loginButton.addActionListener(loginActionListener);
        usernameField.addActionListener(loginActionListener);
        passwordField.addActionListener(loginActionListener);
        registerButton.addActionListener(e -> mainFrame.showRegistrationPanel());
    }

    /**
     * Internal action listener to handle the login event.
     */
    private class LoginActionListener implements ActionListener {
        /**
         * Instantiates a new Login action listener.
         */
        public LoginActionListener() {
            // Default constructor
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            errorLabel.setText(" ");

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Username e Password sono obbligatori.");
                return;
            }
            boolean loginSuccess = appController.login(username, password);
            if (!loginSuccess) {
                errorLabel.setText("Credenziali non valide. Riprova.");
                passwordField.setText("");
                usernameField.requestFocusInWindow();
                usernameField.selectAll();
            }
        }
    }

    /**
     * Clear fields.
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
        usernameField.requestFocusInWindow();
    }
}