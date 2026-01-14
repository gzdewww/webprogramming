package com.musicstore.ui;

import com.musicstore.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainFrame extends JFrame {
  private Connection connection;
  private JTabbedPane tabbedPane;

  public MainFrame(Connection connection) {
    this.connection = connection;
    initializeUI();
  }

  private void initializeUI() {
    setTitle("Музыкальный магазин - Лабораторная работа №2");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 700);
    setLocationRelativeTo(null); // центрируем окно

    // Создаем меню
    createMenuBar();

    // Создаем панель с вкладками
    tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Задание 1: Базовые операции", new Task1Panel(connection));
    tabbedPane.addTab("Задание 2: PreparedStatement", new Task2Panel(connection));
    tabbedPane.addTab("Задание 3: Метаданные", new Task3Panel(connection));

    add(tabbedPane, BorderLayout.CENTER);

    // Панель статуса
    JLabel statusLabel = new JLabel(" Готово к работе");
    add(statusLabel, BorderLayout.SOUTH);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("Файл");
    JMenuItem exitItem = new JMenuItem("Выход");
    exitItem.addActionListener(e -> {
      DatabaseConnection.closeConnection(connection);
      System.exit(0);
    });
    fileMenu.add(exitItem);

    JMenu helpMenu = new JMenu("Помощь");
    JMenuItem aboutItem = new JMenuItem("О программе");
    aboutItem.addActionListener(e -> showAboutDialog());
    helpMenu.add(aboutItem);

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);
  }

  private void showAboutDialog() {
    JOptionPane.showMessageDialog(this,
        "Лабораторная работа №2: JDBC\n" +
            "Музыкальный магазин\n\n" +
            "Реализованы все 4 задания:\n" +
            "1. Базовые операции JDBC\n" +
            "2. Использование PreparedStatement\n" +
            "3. Работа с метаданными БД\n" +
            "4. Графический интерфейс на Swing\n\n" +
            "Выполнил: [Ваше имя]\n" +
            "Группа: [Ваша группа]",
        "О программе",
        JOptionPane.INFORMATION_MESSAGE);
  }
}