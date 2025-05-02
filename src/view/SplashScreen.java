package view;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {
    private JWindow splashWindow;

    public SplashScreen() {
        // Create the splash window
        splashWindow = new JWindow();
        JLabel label = new JLabel(new ImageIcon("resources/splash-image.png"));
        splashWindow.getContentPane().add(label, BorderLayout.CENTER);
        splashWindow.setSize(600, 400); // Adjust size to fit your image
        splashWindow.setLocationRelativeTo(null); // Center on screen
    }

    // Display the splash screen for 3 seconds
    public void showSplashScreen() {
        splashWindow.setVisible(true);
        try {
            Thread.sleep(3000); // Wait for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashWindow.setVisible(false); // Close the splash screen
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen();
        splash.showSplashScreen();

        // After the splash screen, launch the main application
        SwingUtilities.invokeLater(() -> new AdminPanel("admin")); // Example username
    }
}
