package model;

public class AdminSecurity {
    private int id;
    private String username;
    private String securityQuestion;
    private String securityAnswer;
    private String petName;
    private String secretCode;

    public AdminSecurity() {}

    public AdminSecurity(String username, String securityQuestion, String securityAnswer, 
                        String petName, String secretCode) {
        this.username = username;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.petName = petName;
        this.secretCode = secretCode;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getSecretCode() { return secretCode; }
    public void setSecretCode(String secretCode) { this.secretCode = secretCode; }
} 