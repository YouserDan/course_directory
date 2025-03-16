package org.example.course_directory.entyty;

public class User {
    private Integer id;  // ID пользователя теперь может быть null (пока не извлечен из базы)
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String passwordHash;
    private final boolean isAdmin;

    // Конструктор без параметра id (id будет автоматически сгенерирован)
    public User(String firstName, String lastName, String email, String passwordHash, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    // Конструктор с установкой id (для использования после аутентификации)
    public User(Integer id, String firstName, String lastName, String email, String passwordHash, boolean isAdmin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    // Метод getId, который возвращает актуальный id, если он был установлен
    public Integer getId() {
        return id != null ? id : 0;  // Возвращаем id, если оно установлено, или 0 по умолчанию
    }

    // Установить id после извлечения из базы данных
    public void setId(Integer id) {
        this.id = id;
    }
}

