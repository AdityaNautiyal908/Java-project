package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SplashScreen {
    private JWindow splashWindow;
    private JProgressBar progressBar;
    private JLabel logoLabel;
    private JLabel titleLabel;

    public SplashScreen() {
        splashWindow = new JWindow();
        splashWindow.setShape(new RoundRectangle2D.Double(0, 0, 800, 500, 20, 20));

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(41, 128, 185), // Dark blue
                    0, getHeight(), new Color(44, 62, 80) // Darker blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo and title
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        // Logo with custom positioning
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        centerPanel.add(logoLabel, BorderLayout.CENTER);

        // Title with custom styling
        titleLabel = new JLabel("FITNESS HUB", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel, BorderLayout.SOUTH);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113)); // Green color
        progressBar.setBackground(new Color(44, 62, 80));
        progressBar.setBorderPainted(false);
        progressBar.setString("Loading...");

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        splashWindow.getContentPane().add(mainPanel);
        splashWindow.setSize(800, 500);
        splashWindow.setLocationRelativeTo(null);
    }

    // Method to set custom logo
    public void setLogo(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledImage));
    }

    // Method to set custom title
    public void setTitle(String title, Font font, Color color) {
        titleLabel.setText(title);
        titleLabel.setFont(font);
        titleLabel.setForeground(color);
    }

    public void showSplashScreen() {
        splashWindow.setOpacity(0f);
        splashWindow.setVisible(true);

        // Fade in animation
        for (float i = 0f; i <= 1f; i += 0.05f) {
            splashWindow.setOpacity(i);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Progress bar animation
        for (int i = 0; i <= 100; i++) {
            progressBar.setValue(i);
            progressBar.setString("Loading " + i + "%");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Hold for 1 second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fade out animation
        for (float i = 1f; i >= 0f; i -= 0.05f) {
            splashWindow.setOpacity(i);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        splashWindow.setVisible(false);
    }

    public static void main(String[] args) {
        if (!GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
            System.err.println("Translucency is not supported on your system.");
        }

        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            // Customize the splash screen
            splash.setLogo("resources/splash-image.png", 400, 300); // Adjust size as needed
            splash.setTitle("FITNESS HUB", new Font("Arial", Font.BOLD, 36), Color.WHITE);
            splash.showSplashScreen();
            new AdminLogin(); // Changed to AdminLogin instead of AdminPanel
        });
    }
}
