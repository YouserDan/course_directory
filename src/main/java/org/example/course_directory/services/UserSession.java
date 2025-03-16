package org.example.course_directory.services;

public class UserSession {
    private static UserSession instance;
    private String username;

    private UserSession(String username) {
        this.username = username;
    }

    public static UserSession getInstance(String username) {
        if (instance == null) {
            instance = new UserSession(username);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void clearSession() {
        instance = null; // Очистка сессии при выходе
    }
}
