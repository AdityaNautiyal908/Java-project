package view;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {
    private JWindow splashWindow;

    public SplashScreen() {
        splashWindow = new JWindow();

        // Create a panel with vertical padding
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(-150, 0, 0, 0)); // Top, Left, Bottom, Right

        JLabel label = new JLabel(new ImageIcon("resources/splash-image.png"));
        panel.add(label, BorderLayout.NORTH);

        splashWindow.getContentPane().add(panel);
        splashWindow.setSize(600, 400); // Adjust as needed
        splashWindow.setLocationRelativeTo(null);
    }

    public void showSplashScreen() {
        splashWindow.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashWindow.setVisible(false);
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen();
        splash.showSplashScreen();

        SwingUtilities.invokeLater(() -> new AdminPanel("admin"));
    }
}
