package view;

import javax.swing.*;
import java.awt.*;

public class Loading extends JFrame implements Runnable {

    private JProgressBar progressBar;
    private Thread th;
    private String username;
    private AdminLogin loginFrame;
    private Runnable onComplete;
    private boolean spinning = true;
    private Timer spinnerTimer;
    private int spinnerAngle = 0;

    public Loading(String username, AdminLogin loginFrame, Runnable onComplete) {
        this.username = username;
        this.loginFrame = loginFrame;
        this.onComplete = onComplete;

        setBounds(600, 300, 600, 400);
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(41, 128, 185),
                    0, getHeight(), new Color(44, 62, 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Spinning loader panel
        JPanel spinnerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 60;
                int x = (getWidth() - size) / 2;
                int y = 0;
                g2.translate(getWidth() / 2, size / 2);
                g2.rotate(Math.toRadians(spinnerAngle));
                g2.setColor(new Color(46, 204, 113));
                g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(-size/2, -size/2, size, size, 0, 270);
                g2.setColor(new Color(255,255,255,80));
                g2.drawArc(-size/2, -size/2, size, size, 270, 90);
            }
        };
        spinnerPanel.setOpaque(false);
        spinnerPanel.setBounds(250, 40, 100, 60);
        contentPane.add(spinnerPanel);

        // Start spinner animation
        spinnerTimer = new Timer(16, e -> {
            spinnerAngle = (spinnerAngle + 6) % 360;
            spinnerPanel.repaint();
        });
        spinnerTimer.start();

        JLabel label = new JLabel("Please Wait...");
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        label.setBounds(200, 120, 250, 40);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(label);

        progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                // Draw background
                g2.setColor(new Color(255,255,255,60));
                g2.fillRoundRect(0, 0, width, height, height, height);
                // Draw gradient progress
                int progress = (int) (((double)getValue() / getMaximum()) * width);
                GradientPaint gp = new GradientPaint(0, 0, new Color(46, 204, 113), width, 0, new Color(52, 152, 219));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, progress, height, height, height);
                // Draw border
                g2.setColor(new Color(255,255,255,120));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, width-1, height-1, height, height);
                // Draw string
                String text = getString();
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(text);
                int stringHeight = fm.getAscent();
                g2.setColor(Color.WHITE);
                g2.drawString(text, (width - stringWidth) / 2, (height + stringHeight) / 2 - 2);
            }
        };
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        progressBar.setStringPainted(true);
        progressBar.setBounds(150, 200, 300, 40);
        progressBar.setOpaque(false);
        contentPane.add(progressBar);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0,0,0,0));
        panel.setBounds(10, 10, 580, 380);
        contentPane.add(panel);
    }

    public void setUploading() {
        setVisible(true);
        th = new Thread(this);
        th.start();
        if (loginFrame != null) {
            loginFrame.dispose();
        }
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i <= 100; i++) {
                progressBar.setValue(i);
                Thread.sleep(20);
            }
            spinnerTimer.stop();
            setVisible(false);
            if (onComplete != null) {
                SwingUtilities.invokeLater(onComplete);
            }
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Loading loading = new Loading("admin", null, null);
            loading.setUploading();
        });
    }
}
