import view.AdminLogin;
import view.SplashScreen;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;

public class MainApp {
    public static void main(String[] args) {
        // Show splash screen first
        SplashScreen splash = new SplashScreen();
        splash.setLogo("resources/splash-image.png", 400, 300);
        splash.setTitle("FITNESS HUB", new Font("Arial", Font.BOLD, 36), Color.WHITE);
        
        // Show splash screen in a separate thread
        Thread splashThread = new Thread(() -> {
            splash.showSplashScreen();
            // After splash screen completes, show the login screen
            SwingUtilities.invokeLater(() -> {
                new AdminLogin();
            });
        });
        splashThread.start();
    }
}

