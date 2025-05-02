// File: view/AttendanceHistoryPanel.java
package view;

import com.toedter.calendar.JDateChooser;
import dao.AttendanceDAO;
import dao.UserDAO;
import model.Attendance;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendanceHistoryPanel extends JFrame {
    private JComboBox<String> userDropdown;
    private JComboBox<String> filterTypeDropdown;
    private JTextArea resultArea;
    private JDateChooser specificDateChooser, startDateChooser, endDateChooser;

    private UserDAO userDAO = new UserDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();
    private List<User> userList;

    public AttendanceHistoryPanel() {
        setTitle("Attendance History");
        setSize(600, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        userList = userDAO.getAllUsers();

        // Top Panel - User selection + filter type
        JPanel topPanel = new JPanel(new FlowLayout());

        topPanel.add(new JLabel("Select User:"));
        userDropdown = new JComboBox<>();
        for (User user : userList) {
            userDropdown.addItem(user.getName());
        }
        topPanel.add(userDropdown);

        filterTypeDropdown = new JComboBox<>(new String[]{"All", "Today", "Specific Date", "Date Range"});
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterTypeDropdown);

        // Date pickers
        specificDateChooser = new JDateChooser();
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        topPanel.add(specificDateChooser);
        topPanel.add(startDateChooser);
        topPanel.add(new JLabel("to"));
        topPanel.add(endDateChooser);

        JButton viewBtn = new JButton("View Attendance");
        viewBtn.addActionListener(e -> showAttendance());
        topPanel.add(viewBtn);

        add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Initially hide date choosers
        updateDateChooserVisibility();
        filterTypeDropdown.addActionListener(e -> updateDateChooserVisibility());

        setVisible(true);
    }

    private void updateDateChooserVisibility() {
        String filter = (String) filterTypeDropdown.getSelectedItem();
        specificDateChooser.setVisible("Specific Date".equals(filter));
        startDateChooser.setVisible("Date Range".equals(filter));
        endDateChooser.setVisible("Date Range".equals(filter));
        revalidate();
        repaint();
    }

    private void showAttendance() {
        int index = userDropdown.getSelectedIndex();
        if (index >= 0) {
            User selectedUser = userList.get(index);
            List<Attendance> attendanceList = attendanceDAO.getAttendanceForUser(selectedUser.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String filter = (String) filterTypeDropdown.getSelectedItem();

            StringBuilder sb = new StringBuilder();
            sb.append("Attendance for ").append(selectedUser.getName()).append(":\n\n");

            Date today = new Date();
            for (Attendance a : attendanceList) {
                Date att = a.getDate(); // Assuming this returns java.util.Date or java.sql.Date
                String attDate = sdf.format(att);
                try {
                    sdf.parse(attDate);

                    boolean show = switch (filter) {
                        case "Today" -> attDate.equals(sdf.format(today));
                        case "Specific Date" -> specificDateChooser.getDate() != null &&
                                attDate.equals(sdf.format(specificDateChooser.getDate()));
                        case "Date Range" -> startDateChooser.getDate() != null && endDateChooser.getDate() != null &&
                                !att.before(startDateChooser.getDate()) && !att.after(endDateChooser.getDate());
                        default -> true; // "All"
                    };

                    if (show) {
                        sb.append("• ").append(attDate).append("\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            resultArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        new AttendanceHistoryPanel();
    }
}
