package view;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {
    private JWindow splashWindow;

    public SplashScreen() {
        splashWindow = new JWindow();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(-130, 0, 0, 0)); // Move image higher

        JLabel label = new JLabel(new ImageIcon("resources/splash-image.png"));
        panel.add(label, BorderLayout.NORTH);

        splashWindow.getContentPane().add(panel);
        splashWindow.setSize(600, 400);
        splashWindow.setLocationRelativeTo(null);
    }

    public void showSplashScreen() {
        // Set initial opacity (only works on undecorated windows and Java 1.7+)
        splashWindow.setOpacity(0f); // Make fully transparent
        splashWindow.setVisible(true);

        // Gradually increase opacity to create fade-in effect
        for (float i = 0f; i <= 1f; i += 0.05f) {
            splashWindow.setOpacity(i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Hold for 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashWindow.setVisible(false);
    }

    public static void main(String[] args) {
        // Ensure window translucency is supported
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
            System.err.println("Translucency is not supported on your system.");
        }

        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.showSplashScreen();
            new AdminPanel("admin");
        });
    }
}
