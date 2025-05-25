package aeroporto.aeroportoPackage.gui;

import aeroporto.aeroportoPackage.controller.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame; 
    private AppController appController; 

    private JTextField usernameField; 
    private JPasswordField passwordField; 
    private JButton loginButton; 
    private JButton registerButton; 
    private JLabel errorLabel; 

    public LoginPanel(MainFrame mainFrame, AppController appController) { 
        this.mainFrame = mainFrame; 
        this.appController = appController; 

        setLayout(new GridBagLayout()); 
        setBorder(BorderFactory.createCompoundBorder( 
                BorderFactory.createEmptyBorder(30, 50, 30, 50), 
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Accesso Utente") 
        ));
        setBackground(new Color(240, 245, 250)); 

        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.anchor = GridBagConstraints.LINE_END; 
        add(new JLabel("Username:"), gbc); 

        usernameField = new JTextField(20); 
        gbc.gridx = 1; 
        gbc.gridy = 0; 
        gbc.weightx = 1.0; 
        gbc.anchor = GridBagConstraints.LINE_START; 
        add(usernameField, gbc); 

        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.anchor = GridBagConstraints.LINE_END; 
        add(new JLabel("Password:"), gbc); 

        passwordField = new JPasswordField(20); 
        gbc.gridx = 1; 
        gbc.gridy = 1; 
        gbc.anchor = GridBagConstraints.LINE_START; 
        add(passwordField, gbc); 

        errorLabel = new JLabel(" ", SwingConstants.CENTER); 
        errorLabel.setForeground(Color.RED); 
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 12)); 
        gbc.gridx = 0; 
        gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        add(errorLabel, gbc); 
        gbc.gridwidth = 1; 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        buttonPanel.setOpaque(false); 

        loginButton = new JButton("Login"); 
        loginButton.setPreferredSize(new Dimension(100, 30)); 
        buttonPanel.add(loginButton); 

        registerButton = new JButton("Registrati"); 
        registerButton.setPreferredSize(new Dimension(100, 30)); 
        buttonPanel.add(registerButton); 

        gbc.gridx = 0; 
        gbc.gridy = 3; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.fill = GridBagConstraints.NONE; 
        add(buttonPanel, gbc); 

        LoginActionListener loginActionListener = new LoginActionListener(); 
        loginButton.addActionListener(loginActionListener); 
        usernameField.addActionListener(loginActionListener); 
        passwordField.addActionListener(loginActionListener); 

        registerButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                mainFrame.showRegistrationPanel(); 
            }
        });
    }

    private class LoginActionListener implements ActionListener {
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

    public void clearFields() { 
        usernameField.setText(""); 
        passwordField.setText(""); 
        errorLabel.setText(" "); 
        usernameField.requestFocusInWindow(); 
    }
}