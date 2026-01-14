package com.musicstore.ui;

import com.musicstore.db.MetadataService;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Task3Panel extends JPanel {
  private Connection connection;
  private MetadataService metadataService;

  private JComboBox<String> tablesComboBox;
  private JTable columnsTable;
  private DefaultTableModel columnsTableModel;
  private JTextArea outputArea;

  public Task3Panel(Connection connection) {
    this.connection = connection;
    this.metadataService = new MetadataService(connection);
    initializeUI();
    loadTables();
  }

  private void initializeUI() {
    setLayout(new BorderLayout(10, 10));

    // Верхняя панель: выбор таблицы
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.setBorder(BorderFactory.createTitledBorder("Метаданные базы данных"));

    // Выбор таблицы
    JPanel tableSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    tableSelectPanel.add(new JLabel("Выберите таблицу:"));

    tablesComboBox = new JComboBox<>();
    tablesComboBox.addActionListener(e -> loadTableColumns());
    tableSelectPanel.add(tablesComboBox);

    JButton refreshButton = new JButton("Обновить список");
    refreshButton.addActionListener(e -> loadTables());
    tableSelectPanel.add(refreshButton);
    topPanel.add(tableSelectPanel);

    // Таблица столбцов
    String[] columns = { "Имя столбца", "Тип данных", "Размер" };
    columnsTableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    columnsTable = new JTable(columnsTableModel);
    JScrollPane tableScrollPane = new JScrollPane(columnsTable);
    tableScrollPane.setPreferredSize(new Dimension(600, 200));
    topPanel.add(tableScrollPane);

    // Кнопка для полной информации
    JButton fullInfoButton = new JButton("Показать все метаданные");
    fullInfoButton.addActionListener(e -> showAllMetadata());
    topPanel.add(fullInfoButton);

    // Нижняя панель: дополнительная информация
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBorder(BorderFactory.createTitledBorder("Дополнительная информация"));

    outputArea = new JTextArea();
    outputArea.setEditable(false);
    outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JScrollPane scrollPane = new JScrollPane(outputArea);
    bottomPanel.add(scrollPane, BorderLayout.CENTER);

    // Кнопка очистки
    JButton clearButton = new JButton("Очистить вывод");
    clearButton.addActionListener(e -> outputArea.setText(""));
    bottomPanel.add(clearButton, BorderLayout.SOUTH);

    // Располагаем панели
    add(topPanel, BorderLayout.NORTH);
    add(bottomPanel, BorderLayout.CENTER);
  }

  private void loadTables() {
    try {
      List<String> tables = metadataService.getAllTables();
      tablesComboBox.removeAllItems();

      for (String table : tables) {
        tablesComboBox.addItem(table);
      }

      if (tablesComboBox.getItemCount() > 0) {
        tablesComboBox.setSelectedIndex(0);
        loadTableColumns();
      }
    } catch (SQLException e) {
      showError("Ошибка при загрузке таблиц: " + e.getMessage());
    }
  }

  private void loadTableColumns() {
    String tableName = (String) tablesComboBox.getSelectedItem();
    if (tableName == null)
      return;

    try {
      List<String> columns = metadataService.getTableColumns(tableName);
      columnsTableModel.setRowCount(0); // Очищаем таблицу

      for (String column : columns) {
        // Разбираем строку формата "имя тип (размер)"
        String[] parts = column.split("\\s+");
        if (parts.length >= 3) {
          // Убираем скобки из размера
          String size = parts[2];
          if (size.startsWith("(") && size.endsWith(")")) {
            size = size.substring(1, size.length() - 1);
          }
          columnsTableModel.addRow(new String[] { parts[0], parts[1], size });
        }
      }

      outputArea.setText("");
      outputArea.append("Таблица: " + tableName + "\n");
      outputArea.append("Количество столбцов: " + columns.size() + "\n");
      outputArea.append("Последнее обновление: " + java.time.LocalDateTime.now() + "\n");

    } catch (SQLException e) {
      showError("Ошибка при загрузке столбцов: " + e.getMessage());
    }
  }

  private void showAllMetadata() {
    outputArea.append("\n=== Полная информация о базе данных ===\n");

    try {
      List<String> tables = metadataService.getAllTables();
      outputArea.append("Всего таблиц в базе данных: " + tables.size() + "\n");
      outputArea.append("Список таблиц:\n");

      for (String table : tables) {
        outputArea.append("  • " + table + "\n");
        List<String> columns = metadataService.getTableColumns(table);
        outputArea.append("    Столбцы (" + columns.size() + "):\n");
        for (String column : columns) {
          outputArea.append("      - " + column + "\n");
        }
      }
      outputArea.append("✓ Метаданные успешно загружены\n");
    } catch (SQLException e) {
      showError("Ошибка при загрузке метаданных: " + e.getMessage());
    }
  }

  private void showError(String message) {
    outputArea.append("  " + message + "\n");
  }
}