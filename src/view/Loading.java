package view;

import javax.swing.*;
import java.awt.*;

public class Loading extends JFrame implements Runnable {

    private JProgressBar progressBar;
    private Thread th;
    private String username;
    private AdminLogin loginFrame;

    public Loading(String username, AdminLogin loginFrame) {
        this.username = username;
        this.loginFrame = loginFrame;

        setBounds(600, 300, 600, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(51, 204, 255));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label = new JLabel("Please Wait...");
        label.setFont(new Font("Tahoma", Font.BOLD, 20));
        label.setForeground(new Color(72, 209, 204));
        label.setBounds(220, 150, 150, 25);
        contentPane.add(label);

        progressBar = new JProgressBar();
        progressBar.setFont(new Font("Tahoma", Font.BOLD, 12));
        progressBar.setStringPainted(true);
        progressBar.setBounds(130, 200, 300, 25);
        contentPane.add(progressBar);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(10, 10, 580, 380);
        contentPane.add(panel);

        setUndecorated(true);
    }

    public void setUploading() {
        setVisible(true);
        th = new Thread(this);
        th.start();
        // Don't open AdminPanel here, we'll handle it in the run method after progress is complete.
        if (loginFrame != null) {
            loginFrame.dispose();  // Close AdminLogin window after setting up loading
        }
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                progressBar.setValue(i + 1);
                Thread.sleep(50); // Simulate loading process
            }
            setVisible(false); // Hide loading screen after progress is complete
            // Open AdminPanel after loading completes
            SwingUtilities.invokeLater(() -> new AdminPanel(username).setVisible(true));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Loading loading = new Loading("admin", null);  // Dummy username, no login frame
            loading.setUploading();
        });
    }
}
