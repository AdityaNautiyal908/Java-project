// File: view/AttendancePanel.java
package view;

import dao.AttendanceDAO;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.util.List;

public class AttendancePanel extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    public AttendancePanel() {
        setTitle("Mark Attendance");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Click on your photo to mark attendance", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane, BorderLayout.CENTER);

        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BorderLayout());

            ImageIcon icon = new ImageIcon(user.getPhotoPath());
            Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JButton photoButton = new JButton(new ImageIcon(scaledImage));

            photoButton.setPreferredSize(new Dimension(120, 120));
            photoButton.setFocusPainted(false);
            photoButton.setBorder(BorderFactory.createEmptyBorder());

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

            JLabel nameLabel = new JLabel(user.getName(), SwingConstants.CENTER);

            userPanel.add(photoButton, BorderLayout.CENTER);
            userPanel.add(nameLabel, BorderLayout.SOUTH);
            gridPanel.add(userPanel);
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new AttendancePanel();
    }
}
