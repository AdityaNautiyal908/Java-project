// File: model/User.java
package model;

public class User {
    private int id;
    private String name;
    private String photoPath;

    // Constructors
    public User() {}

    public User(String name, String photoPath) {
        this.name = name;
        this.photoPath = photoPath;
    }

    public User(int id, String name, String photoPath) {
        this.id = id;
        this.name = name;
        this.photoPath = photoPath;
    }


    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
