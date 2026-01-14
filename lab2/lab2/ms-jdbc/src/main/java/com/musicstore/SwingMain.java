package com.musicstore;

import com.musicstore.db.DatabaseConnection;
import com.musicstore.ui.MainFrame;

import javax.swing.*;
import java.sql.Connection;

public class SwingMain {
  public static void main(String[] args) {
    // Запускаем в потоке обработки событий Swing (EDT)
    SwingUtilities.invokeLater(() -> {
      try {
        // Устанавливаем красивый вид (Nimbus)
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Nimbus".equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            break;
          }
        }
      } catch (Exception e) {
        // Если Nimbus не доступен, используем системный
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      // Проверяем подключение к БД
      if (!DatabaseConnection.testConnection()) {
        JOptionPane.showMessageDialog(null,
            "Не удалось подключиться к базе данных!\n" +
                "Проверьте параметры подключения в DatabaseConnection.java\n" +
                "и убедитесь, что сервер PostgreSQL запущен.",
            "Ошибка подключения",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      try {
        Connection connection = DatabaseConnection.getConnection();
        // Создаем и показываем главное окно
        MainFrame mainFrame = new MainFrame(connection);
        mainFrame.setVisible(true);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Ошибка инициализации приложения: " + e.getMessage(),
            "Ошибка",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
      }
    });
  }
}