package org.example.course_directory.entyty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Course {

    private int id;  // Уникальный идентификатор курса
    private String title;  // Название курса
    private String author;  // Автор курса
    private String programmingLanguage;  // Язык программирования (если актуально)
    private String imageUrl;  // URL изображения
    private String level;  // Уровень сложности
    private String duration;  // Длительность курса
    private String durationType;  // Длительность чч мм гг
    private String access;  // Доступность (например, бесплатно или платно)
    private double price;  // Цена курса
    private String currency;
    private String keywords;  // Ключевые слова
    private String description;  // Описание курса
    private String languageOfCourse;  // Язык курса
    private String resourceUrl;  // Ссылка на ресурс
    private String createdBy;  // Кто создал курс
    private LocalDateTime createdAt;  // Дата и время создания
    private LocalDateTime updatedAt;  // Дата и время последнего обновления
    private String updatedBy;  // Кто обновил курс

    // Конструктор без ID, потому что ID обычно генерируется базой данных
    public Course(String title, String author, String programmingLanguage, String imageUrl, String level,
                  String duration, String durationType, String access, double price, String currency, String keywords, String description,
                  String languageOfCourse, String resourceUrl, String createdBy) {
        this.title = title;
        this.author = author;
        this.programmingLanguage = programmingLanguage;
        this.imageUrl = imageUrl;
        this.level = level;
        this.duration = duration;
        this.durationType = durationType;
        this.access = access;
        this.price = price;
        this.currency = currency;
        this.keywords = keywords;
        this.description = description;
        this.languageOfCourse = languageOfCourse;
        this.resourceUrl = resourceUrl;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();  // Устанавливаем текущее время как дату создания
        this.updatedAt = LocalDateTime.now();  // Устанавливаем текущее время как дату обновления
    }

    // Конструктор (создаёт объект курса из данных БД) для вывода
    public Course(int id, String title, String author, String programmingLanguage, String imageUrl, String level, int duration,
                  String durationType, String access, double price, String currency, String description, String languageOfCourse, String resourceUrl,
                  String createdBy, String createdAt, String updatedBy, String updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.programmingLanguage = programmingLanguage;
        this.imageUrl = imageUrl;
        this.level = level;
        this.duration = String.valueOf(duration);
        this.durationType = durationType;
        this.access = access;
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.languageOfCourse = languageOfCourse;
        this.resourceUrl = resourceUrl;
        this.createdBy = createdBy;

        // Указываем формат для парсинга
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Парсим строки в LocalDateTime с использованием форматтера
        this.createdAt = LocalDateTime.parse(createdAt, formatter);
        this.updatedBy = updatedBy;
        try {
            // Для createdAt
            this.createdAt = LocalDateTime.parse(createdAt.trim(), formatter);

            // Для updatedAt
            this.updatedAt = LocalDateTime.parse(updatedAt.trim(), formatter);
        } catch (DateTimeParseException e) {
            // Логирование ошибки
            System.err.println("Error parsing date: " + e.getMessage());
        }
    }

    // Геттер и сеттер для валюты
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Геттеры и сеттеры для всех полей
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDuration() {
        return duration;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguageOfCourse() {
        return languageOfCourse;
    }

    public void setLanguageOfCourse(String languageOfCourse) {
        this.languageOfCourse = languageOfCourse;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
