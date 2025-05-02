// File: view/ForgotPassword.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgotPassword extends JDialog {
    private JTextField emailField;
    private JButton resetButton;

    public ForgotPassword(JFrame parentFrame) {
        super(parentFrame, "Forgot Password", true);  // Modal dialog
        setBounds(600, 300, 400, 200);
        setLayout(new FlowLayout());

        JLabel promptLabel = new JLabel("Enter your registered email address:");
        emailField = new JTextField(20);
        resetButton = new JButton("Send Reset Link");

        // Add components to the dialog
        add(promptLabel);
        add(emailField);
        add(resetButton);

        // Action for reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                if (isValidEmail(email)) {
                    // Simulate sending a reset link (you can integrate an actual email system here)
                    JOptionPane.showMessageDialog(ForgotPassword.this, "Password reset link sent to: " + email);
                    dispose(); // Close the dialog after sending the reset link
                } else {
                    JOptionPane.showMessageDialog(ForgotPassword.this, "Please enter a valid email address.");
                }
            }
        });

        setVisible(true);
    }

    // Simple email validation (you can replace this with more robust validation)
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
