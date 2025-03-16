package org.example.course_directory.entyty;

import java.sql.Timestamp;

public class Click {
    private int id;
    private int userId;
    private int courseId;
    private Timestamp clickedAt;
    private int transitionCount;

    public Click() {
    }

    // Конструктор без ID (новый клик)
    public Click(int userId, int courseId, Timestamp clickedAt, int transitionCount) {
        this.userId = userId;
        this.courseId = courseId;
        this.clickedAt = clickedAt;
        this.transitionCount = transitionCount;
    }

    // Конструктор с ID (из БД)
    public Click(int id, int userId, int courseId, Timestamp clickedAt, int transitionCount) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.clickedAt = clickedAt;
        this.transitionCount = transitionCount;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Timestamp getClickedAt() {
        return clickedAt;
    }

    public void setClickedAt(Timestamp clickedAt) {
        this.clickedAt = clickedAt;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

    public void setTransitionCount(int transitionCount) {
        this.transitionCount = transitionCount;
    }

    @Override
    public String toString() {
        return "Click [id=" + id + ", userId=" + userId + ", courseId=" + courseId + ", clickedAt=" + clickedAt
                + ", transitionCount=" + transitionCount + "]";
    }
}
