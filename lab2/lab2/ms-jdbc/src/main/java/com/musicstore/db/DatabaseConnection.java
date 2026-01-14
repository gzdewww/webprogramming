package com.musicstore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Простой класс для создания подключения к базе данных.
 * Не содержит бизнес-логики, только подключение.
 */
public class DatabaseConnection {
  private static final String URL = "jdbc:postgresql://localhost:5432/music_store";
  private static final String USER = "postgres"; // Замените на вашего пользователя
  private static final String PASSWORD = "postgres"; // Замените на ваш пароль

  /**
   * Создаёт и возвращает новое подключение к базе данных
   */
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  /**
   * Закрывает подключение (если оно не null и не закрыто)
   */
  public static void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        if (!connection.isClosed()) {
          connection.close();
          System.out.println("Подключение закрыто.");
        }
      } catch (SQLException e) {
        System.err.println("Ошибка при закрытии подключения: " + e.getMessage());
      }
    }
  }

  /**
   * Проверяет доступность базы данных
   */
  public static boolean testConnection() {
    try (Connection conn = getConnection()) {
      return conn != null;
    } catch (SQLException e) {
      System.err.println("Ошибка подключения: " + e.getMessage());
      return false;
    }
  }
}