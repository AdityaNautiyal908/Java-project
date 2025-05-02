package view;

import javax.swing.*;
import java.awt.*;

public class Loading extends JFrame implements Runnable {

    private JProgressBar progressBar;
    private Thread th;
    private String username;

    public Loading(String username) {
        this.username = username;

        setBounds(600, 300, 600, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(51,204, 255));
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
        // th.start(); ❌ Remove this line
    }

    public void setUploading() {
        setVisible(true);
        th = new Thread(this); // ✅ Create new thread instance
        th.start();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                progressBar.setValue(i + 1);
                Thread.sleep(50);
            }
            setVisible(false);
            new AdminPanel(username).setVisible(true); // Navigate to Admin Panel after loading
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Loading loading = new Loading("");
            loading.setUploading(); // ✅ Start thread from here
        });
    }
}
