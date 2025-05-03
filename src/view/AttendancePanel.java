package view;

import dao.AttendanceDAO;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;

public class AttendancePanel extends JFrame {
    private JPanel mainPanel;
    private JPanel userListPanel;
    private UserDAO userDAO = new UserDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    public AttendancePanel() {
        setTitle("Fitness Hub - Mark Attendance");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with gradient background
        mainPanel = new JPanel(new BorderLayout()) {
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

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        loadUsers();
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Title label
        JLabel titleLabel = new JLabel("Click on user photo to mark attendance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Date panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setOpaque(false);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        datePanel.add(dateLabel);

        JTextField dateField = createStyledTextField();
        dateField.setPreferredSize(new Dimension(150, 30));
        datePanel.add(dateField);

        headerPanel.add(datePanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);

        // Create search panel
        JPanel searchPanel = createSearchPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        // Create user list panel
        userListPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        userListPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
            "Users",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100)),
            "Search Users",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.WHITE
        ));

        JTextField searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchPanel.add(searchField);

        JButton searchButton = createStyledButton("Search", new Color(52, 152, 219));
        searchButton.addActionListener(e -> searchUsers(searchField.getText()));
        searchPanel.add(searchButton);

        return searchPanel;
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

    private void loadUsers() {
        userListPanel.removeAll();
        List<User> users = userDAO.getAllUsers();
        displayUsers(users);
    }

    private void searchUsers(String keyword) {
        userListPanel.removeAll();
        List<User> users = userDAO.searchUsers(keyword);
        displayUsers(users);
    }

    private void displayUsers(List<User> users) {
        userListPanel.removeAll();

        for (User user : users) {
            JPanel userPanel = new JPanel(new BorderLayout());
            userPanel.setOpaque(false);
            userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Create photo button
            ImageIcon icon = new ImageIcon(user.getPhotoPath());
            Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JButton photoButton = new JButton(new ImageIcon(scaledImage));
            
            // Style the photo button
            photoButton.setPreferredSize(new Dimension(150, 150));
            photoButton.setFocusPainted(false);
            photoButton.setBorderPainted(false);
            photoButton.setContentAreaFilled(false);
            photoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Add hover effect to photo button
            photoButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    photoButton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    photoButton.setBorder(null);
                }
            });

            // Add click action
            photoButton.addActionListener(e -> {
                if (attendanceDAO.hasMarkedToday(user.getId())) {
                    JOptionPane.showMessageDialog(this,
                            "⚠️ " + user.getName() + " has already marked attendance today.");
                } else {
                    boolean success = attendanceDAO.markAttendance(user.getId(), new Date(System.currentTimeMillis()));
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                                "✅ Attendance marked for " + user.getName() + "!");
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "❌ Failed to mark attendance.");
                    }
                }
            });

            // Create name label
            JLabel nameLabel = new JLabel(user.getName(), SwingConstants.CENTER);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            // Add components to panel
            userPanel.add(photoButton, BorderLayout.CENTER);
            userPanel.add(nameLabel, BorderLayout.SOUTH);

            userListPanel.add(userPanel);
        }

        revalidate();
        repaint();
    }
}