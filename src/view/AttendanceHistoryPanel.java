// File: view/AttendanceHistoryPanel.java
package view;

import dao.AttendanceDAO;
import dao.UserDAO;
import model.Attendance;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AttendanceHistoryPanel extends JFrame {
    private JComboBox<String> userDropdown;
    private JTextArea resultArea;
    private UserDAO userDAO = new UserDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private List<User> userList;

    public AttendanceHistoryPanel() {
        setTitle("Attendance History");
        setSize(500, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        userList = userDAO.getAllUsers();

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select User:"));

        userDropdown = new JComboBox<>();
        for (User user : userList) {
            userDropdown.addItem(user.getName());
        }
        topPanel.add(userDropdown);

        JButton viewBtn = new JButton("View Attendance");
        viewBtn.addActionListener(e -> showAttendance());
        topPanel.add(viewBtn);

        add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void showAttendance() {
        int index = userDropdown.getSelectedIndex();
        if (index >= 0) {
            User selectedUser = userList.get(index);
            List<Attendance> attendanceList = attendanceDAO.getAttendanceForUser(selectedUser.getId());

            StringBuilder sb = new StringBuilder();
            sb.append("Attendance for ").append(selectedUser.getName()).append(":\n\n");
            for (Attendance a : attendanceList) {
                sb.append("• ").append(a.getDate()).append("\n");
            }
            resultArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        new AttendanceHistoryPanel();
    }
}
