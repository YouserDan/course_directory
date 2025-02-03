DROP DATABASE IF EXISTS course_directory_DB;

CREATE DATABASE course_directory_DB;

USE course_directory_DB;

CREATE TABLE administrator (
    id INT AUTO_INCREMENT PRIMARY KEY,        -- Уникальный идентификатор администратора
    firstname VARCHAR(50) NOT NULL,          -- Имя администратора, не может быть NULL
    lastname VARCHAR(50) NOT NULL,           -- Фамилия администратора, не может быть NULL
    email VARCHAR(100) NOT NULL UNIQUE,      -- Электронная почта, не может быть NULL и должна быть уникальной
 login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,        -- Уникальный идентификатор пользователя
    firstname VARCHAR(50) NOT NULL,          -- Имя не может быть NULL
    lastname VARCHAR(50) NOT NULL,           -- Фамилия не может быть NULL
    email VARCHAR(100) NOT NULL UNIQUE,      -- Электронная почта, не может быть NULL и должна быть уникальной
    password_hash VARCHAR(255) NOT NULL,     -- Хэш пароля (для хранения пароля безопасно)
    role_entity VARCHAR(255) NOT NULL        -- Роль и права
);

CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,           -- Уникальный идентификатор курса
    title VARCHAR(255) NOT NULL,                 -- Название курса
    description TEXT,                            -- Описание курса
    author VARCHAR(100),                         -- Имя автора курса
    category VARCHAR(100),                       -- Категория курса
    price DECIMAL(10, 2) DEFAULT 0.00,           -- Стоимость курса, по умолчанию 0 (бесплатный)
    duration_hours INT,                          -- Продолжительность курса в часах
    image_url VARCHAR(255),                      -- URL на изображение курса
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата и время создания записи
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Дата и время последнего обновления
    rating DECIMAL(3, 2) DEFAULT 0.00,           -- Рейтинг курса
    num_reviews INT DEFAULT 0,                   -- Количество отзывов
    is_active BOOLEAN DEFAULT TRUE               -- Статус активности курса
);