package aeroporto.aeroportoPackage.gui;

import aeroporto.aeroportoPackage.controller.AppController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPanel extends JPanel {
    private MainFrame mainFrame; 
    private AppController appController; 

    private JTextField nomeField; 
    private JTextField cognomeField; 
    private JTextField numeroDocumentoField; 
    private JTextField usernameField; 
    private JPasswordField passwordField; 
    private JPasswordField confirmPasswordField; 
    private JButton registerButton; 
    private JButton backToLoginButton; 
    private JLabel errorLabel; 

    public RegistrationPanel(MainFrame mainFrame, AppController appController) { 
        this.mainFrame = mainFrame; 
        this.appController = appController; 

        setLayout(new GridBagLayout()); 
        setBorder(BorderFactory.createCompoundBorder( 
                BorderFactory.createEmptyBorder(20, 40, 20, 40), 
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Registrazione Nuovo Utente") 
        ));
        setBackground(new Color(245, 250, 240)); 

        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(6, 6, 6, 6); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.LINE_END; 

        int yPos = 0; 

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Nome:"), gbc); 
        nomeField = new JTextField(25); 
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START; add(nomeField, gbc); 

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Cognome:"), gbc); 
        cognomeField = new JTextField(25); 
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.anchor = GridBagConstraints.LINE_START; add(cognomeField, gbc); 

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Numero Documento:"), gbc); 
        numeroDocumentoField = new JTextField(25); 
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.anchor = GridBagConstraints.LINE_START; add(numeroDocumentoField, gbc); 

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Username (per Login):"), gbc); 
        usernameField = new JTextField(25); 
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.anchor = GridBagConstraints.LINE_START; add(usernameField, gbc); 

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Password:"), gbc); 
        passwordField = new JPasswordField(25); 
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.anchor = GridBagConstraints.LINE_START; add(passwordField, gbc); 

        gbc.gridx = 0; gbc.gridy = yPos; add(new JLabel("Conferma Password:"), gbc); 
        confirmPasswordField = new JPasswordField(25); 
        gbc.gridx = 1; gbc.gridy = yPos++; gbc.anchor = GridBagConstraints.LINE_START; add(confirmPasswordField, gbc); 

        errorLabel = new JLabel(" ", SwingConstants.CENTER); 
        errorLabel.setForeground(Color.RED); 
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 12)); 
        gbc.gridx = 0; gbc.gridy = yPos++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; add(errorLabel, gbc); 
        gbc.gridwidth = 1; 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); 
        buttonPanel.setOpaque(false); 

        registerButton = new JButton("Registrati"); 
        registerButton.setPreferredSize(new Dimension(120, 30)); 
        buttonPanel.add(registerButton); 

        backToLoginButton = new JButton("Torna al Login"); 
        backToLoginButton.setPreferredSize(new Dimension(120, 30)); 
        buttonPanel.add(backToLoginButton); 

        gbc.gridx = 0; gbc.gridy = yPos; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; add(buttonPanel, gbc); 

        registerButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                performRegistration(); 
            }
        });

        backToLoginButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                mainFrame.showLoginPanel(); 
            }
        });
    }

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

        boolean registrationSuccess = appController.register(nome, cognome, numDoc, username, password); 

        if (registrationSuccess) { 
            JOptionPane.showMessageDialog(this, 
                    "Registrazione completata con successo!\nOra puoi effettuare il login.", 
                    "Registrazione Riuscita", 
                    JOptionPane.INFORMATION_MESSAGE); 
            mainFrame.showLoginPanel(); 
        } else {
            errorLabel.setText("Username già esistente o errore di registrazione. Scegline un altro."); 
            usernameField.requestFocusInWindow(); 
            usernameField.selectAll(); 
        }
    }

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