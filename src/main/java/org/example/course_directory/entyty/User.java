package org.example.course_directory.entyty;

public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String passwordHash;
    private final boolean isAdmin = false;

    public User(String firstName, String lastName, String email, String passwordHash, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    public Object getRole() {
        return false;
    }
}
