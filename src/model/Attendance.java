// File: model/Attendance.java
package model;

import java.sql.Date;

public class Attendance {
    private int id;
    private int userId;
    private Date date;

    // Constructors
    public Attendance() {}

    public Attendance(int userId, Date date) {
        this.userId = userId;
        this.date = date;
    }

    public Attendance(int id, int userId, Date date) {
        this.id = id;
        this.userId = userId;
        this.date = date;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
