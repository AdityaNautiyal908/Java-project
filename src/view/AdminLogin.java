// File: view/AdminLogin.java
package view;

import dao.AdminDAO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, forgotPasswordButton;
    private AdminDAO adminDAO = new AdminDAO();
    private JLabel capsLockLabel;
    private boolean passwordVisible = false;
    private JButton togglePasswordButton;

    public AdminLogin() {
        setTitle("Fitness Hub - Admin Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 500, 20, 20));

        // Create main panel with gradient background
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

        // Create left panel for image
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        ImageIcon icon = new ImageIcon("resources/login.png");
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // Create right panel for login form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title label
        JLabel titleLabel = new JLabel("ADMIN LOGIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(titleLabel, gbc);

        // Username field
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(usernameLabel, gbc);

        gbc.gridy = 2;
        usernameField = createStyledTextField();
        rightPanel.add(usernameField, gbc);

        // Password field
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(passwordLabel, gbc);

        gbc.gridy = 4;
        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(255, 255, 255, 200));
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
            new EmptyBorder(5, 10, 5, 10) // Increased right padding for eye icon
        ));
        
        // Create toggle password button
        togglePasswordButton = new JButton();
        togglePasswordButton.setFocusable(false);
        togglePasswordButton.setContentAreaFilled(false);
        togglePasswordButton.setBorderPainted(false);
        togglePasswordButton.setPreferredSize(new Dimension(30, 30));
        togglePasswordButton.setIcon(new ImageIcon(new ImageIcon("resources/eye.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        togglePasswordButton.setVisible(false); // Initially hidden
        togglePasswordButton.addActionListener(e -> {
            passwordVisible = !passwordVisible;
            passwordField.setEchoChar(passwordVisible ? (char)0 : '\u2022');
            togglePasswordButton.setIcon(new ImageIcon(new ImageIcon(passwordVisible ? "resources/eye-off.png" : "resources/eye.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        });
        
        // Add document listener to show/hide toggle button based on content
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateToggleVisibility();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateToggleVisibility();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateToggleVisibility();
            }

            private void updateToggleVisibility() {
                togglePasswordButton.setVisible(passwordField.getPassword().length > 0);
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.selectAll();
                passwordField.setBackground(new Color(255, 255, 255, 255));
                togglePasswordButton.setVisible(passwordField.getPassword().length > 0);
            }
            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBackground(new Color(255, 255, 255, 200));
                togglePasswordButton.setVisible(false);
            }
        });

        // Create panel to hold both password field and toggle button
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setPreferredSize(usernameField.getPreferredSize());
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        // Add the toggle button to the EAST with some padding
        JPanel toggleButtonPanel = new JPanel(new BorderLayout());
        toggleButtonPanel.setOpaque(false);
        toggleButtonPanel.setBorder(new EmptyBorder(0, 5, 0, -5)); 
        toggleButtonPanel.add(togglePasswordButton, BorderLayout.CENTER);
        passwordPanel.add(toggleButtonPanel, BorderLayout.EAST);
        
        rightPanel.add(passwordPanel, gbc);

        // Caps Lock warning label
        gbc.gridy = 5;
        capsLockLabel = new JLabel("");
        capsLockLabel.setForeground(Color.YELLOW);
        capsLockLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rightPanel.add(capsLockLabel, gbc);

        // Login button
        gbc.gridy = 6;
        loginButton = createStyledButton("Login", new Color(46, 204, 113));
        loginButton.addActionListener(e -> loginWithAnimation());
        rightPanel.add(loginButton, gbc);

        // Forgot password button
        gbc.gridy = 7;
        forgotPasswordButton = createStyledButton("Forgot Password?", new Color(52, 152, 219));
        forgotPasswordButton.addActionListener(e -> {
            ForgotPassword forgotPassword = new ForgotPassword(this);
            forgotPassword.setVisible(true);
        });
        rightPanel.add(forgotPasswordButton, gbc);

        // Register button
        gbc.gridy = 8;
        JButton registerButton = createStyledButton("Register New Admin", new Color(155, 89, 182));
        registerButton.addActionListener(e -> {
            AdminRegistration registration = new AdminRegistration();
            registration.setVisible(true);
        });
        rightPanel.add(registerButton, gbc);

        // Add panels to main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Add close button
        JButton closeButton = createStyledButton("×", new Color(231, 76, 60));
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        closeButton.setPreferredSize(new Dimension(40, 40));
        closeButton.addActionListener(e -> dispose());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(closeButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        add(mainPanel);
        setVisible(true);

        // Add keyboard navigation
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginWithAnimation();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                boolean isCapsLock = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
                capsLockLabel.setText(isCapsLock ? "Caps Lock is ON" : "");
            }
        });

        // Request focus on username field
        usernameField.requestFocus();
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setBackground(new Color(255, 255, 255, 200));
        textField.setForeground(Color.BLACK);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.selectAll();
                textField.setBackground(new Color(255, 255, 255, 255));
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBackground(new Color(255, 255, 255, 200));
            }
        });
        return textField;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void showStyledMessageDialog(Component parent, String message, String title, int messageType) {
        UIManager.put("OptionPane.background", new Color(44, 62, 80));
        UIManager.put("Panel.background", new Color(44, 62, 80));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("Button.background", new Color(46, 204, 113));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (adminDAO.isValidAdmin(username, password)) {
            // Create loading screen with completion callback
            Loading loading = new Loading(username, this, () -> {
                new AdminPanel(username);
            });
            loading.setUploading();
        } else {
            showStyledMessageDialog(this, 
                "Invalid credentials ❌",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );
            usernameField.requestFocus();
        }
    }

    // Animated login feedback
    private void loginWithAnimation() {
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        Timer timer = new Timer(1200, e -> {
            loginButton.setEnabled(true);
            loginButton.setText("Login");
            login();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLogin::new);
    }
}