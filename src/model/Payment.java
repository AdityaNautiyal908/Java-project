package model;

import java.time.LocalDate;

public class Payment {
    private int id;
    private int userId;
    private double amount;
    private LocalDate paymentDate;
    private LocalDate dueDate;
    private boolean isPaid;

    // Constructors
    public Payment() {}

    public Payment(int userId, double amount, LocalDate dueDate) {
        this.userId = userId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = false;
    }

    public Payment(int id, int userId, double amount, LocalDate paymentDate, LocalDate dueDate, boolean isPaid) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
} 