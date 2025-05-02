import view.AdminLogin;
import view.SplashScreen;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Show splash screen first
        SplashScreen splash = new SplashScreen();
        splash.showSplashScreen();

        // After splash, show the login screen or admin panel
        SwingUtilities.invokeLater(() -> new AdminLogin()); // or new AdminPanel("admin");
    }
}

