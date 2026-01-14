package com.musicstore.ui;

import com.musicstore.db.ArtistRepository;
import com.musicstore.db.TrackRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Task1Panel extends JPanel {
  private Connection connection;
  private ArtistRepository artistRepository;
  private TrackRepository trackRepository;

  private JTextField artistNameField;
  private JTextField updateArtistIdField;
  private JTextField updateArtistNameField;
  private JTextField deleteArtistIdField;
  private JTextArea outputArea;
  private JTable artistsTable;
  private DefaultTableModel tableModel;

  public Task1Panel(Connection connection) {
    this.connection = connection;
    this.artistRepository = new ArtistRepository(connection);
    this.trackRepository = new TrackRepository(connection);
    initializeUI();
    loadArtists();
  }

  private void initializeUI() {
    setLayout(new BorderLayout(10, 10));

    // Левая панель: управление исполнителями
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
    controlPanel.setBorder(BorderFactory.createTitledBorder("Управление исполнителями"));

    // Добавление исполнителя
    JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    addPanel.add(new JLabel("Имя исполнителя:"));
    artistNameField = new JTextField(20);
    addPanel.add(artistNameField);
    JButton addButton = new JButton("Добавить");
    addButton.addActionListener(e -> addArtist());
    addPanel.add(addButton);
    controlPanel.add(addPanel);

    // Обновление исполнителя
    JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    updatePanel.add(new JLabel("ID:"));
    updateArtistIdField = new JTextField(5);
    updatePanel.add(updateArtistIdField);
    updatePanel.add(new JLabel("Новое имя:"));
    updateArtistNameField = new JTextField(15);
    updatePanel.add(updateArtistNameField);
    JButton updateButton = new JButton("Обновить");
    updateButton.addActionListener(e -> updateArtist());
    updatePanel.add(updateButton);
    controlPanel.add(updatePanel);

    // Удаление исполнителя
    JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    deletePanel.add(new JLabel("ID для удаления:"));
    deleteArtistIdField = new JTextField(5);
    deletePanel.add(deleteArtistIdField);
    JButton deleteButton = new JButton("Удалить");
    deleteButton.setForeground(Color.RED);
    deleteButton.addActionListener(e -> deleteArtist());
    deletePanel.add(deleteButton);
    controlPanel.add(deletePanel);

    // Кнопка обновления списка
    JButton refreshButton = new JButton("Обновить список исполнителей");
    refreshButton.addActionListener(e -> loadArtists());
    controlPanel.add(refreshButton);

    // Таблица исполнителей
    String[] columns = { "ID", "Имя исполнителя" };
    tableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    artistsTable = new JTable(tableModel);
    artistsTable.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting() && artistsTable.getSelectedRow() != -1) {
        int selectedRow = artistsTable.getSelectedRow();
        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String name = tableModel.getValueAt(selectedRow, 1).toString();
        updateArtistIdField.setText(id);
        deleteArtistIdField.setText(id);
        updateArtistNameField.setText(name);
      }
    });
    JScrollPane tableScrollPane = new JScrollPane(artistsTable);
    tableScrollPane.setPreferredSize(new Dimension(400, 200));
    controlPanel.add(tableScrollPane);

    // Кнопка выполнения запроса из ЛР1
    JButton queryButton = new JButton("Выполнить запрос 1 из ЛР1");
    queryButton.addActionListener(e -> executeQuery1());
    controlPanel.add(queryButton);

    // Правая панель: вывод результатов
    JPanel outputPanel = new JPanel(new BorderLayout());
    outputPanel.setBorder(BorderFactory.createTitledBorder("Результаты"));

    outputArea = new JTextArea();
    outputArea.setEditable(false);
    outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JScrollPane outputScrollPane = new JScrollPane(outputArea);
    outputPanel.add(outputScrollPane, BorderLayout.CENTER);

    // Кнопка очистки вывода
    JButton clearButton = new JButton("Очистить вывод");
    clearButton.addActionListener(e -> outputArea.setText(""));
    outputPanel.add(clearButton, BorderLayout.SOUTH);

    // Располагаем панели
    add(controlPanel, BorderLayout.WEST);
    add(outputPanel, BorderLayout.CENTER);
  }

  private void loadArtists() {
    try {
      List<String> artists = artistRepository.getAllArtists();
      tableModel.setRowCount(0); // Очищаем таблицу

      for (String artist : artists) {
        String[] parts = artist.split("\t");
        if (parts.length == 2) {
          tableModel.addRow(parts);
        }
      }
      outputArea.append("Список исполнителей загружен (" + artists.size() + " записей)\n");
    } catch (SQLException e) {
      showError("Ошибка при загрузке исполнителей: " + e.getMessage());
    }
  }

  private void addArtist() {
    String name = artistNameField.getText().trim();
    if (name.isEmpty()) {
      showError("Введите имя исполнителя");
      return;
    }

    try {
      int id = artistRepository.addArtist(name);
      if (id != -1) {
        outputArea.append("Добавлен исполнитель: " + name + " (ID: " + id + ")\n");
        artistNameField.setText("");
        loadArtists();
      }
    } catch (SQLException e) {
      showError("Ошибка при добавлении: " + e.getMessage());
    }
  }

  private void updateArtist() {
    try {
      int id = Integer.parseInt(updateArtistIdField.getText().trim());
      String newName = updateArtistNameField.getText().trim();

      if (newName.isEmpty()) {
        showError("Введите новое имя");
        return;
      }

      boolean updated = artistRepository.updateArtist(id, newName);
      if (updated) {
        outputArea.append("Исполнитель с ID " + id + " обновлен на '" + newName + "'\n");
        updateArtistIdField.setText("");
        updateArtistNameField.setText("");
        loadArtists();
      } else {
        showError("Исполнитель с ID " + id + " не найден");
      }
    } catch (NumberFormatException e) {
      showError("Введите корректный ID (число)");
    } catch (SQLException e) {
      showError("Ошибка при обновлении: " + e.getMessage());
    }
  }

  private void deleteArtist() {
    try {
      int id = Integer.parseInt(deleteArtistIdField.getText().trim());

      int confirm = JOptionPane.showConfirmDialog(this,
          "Удалить исполнителя с ID " + id + "? Все связанные альбомы и композиции также будут удалены.",
          "Подтверждение удаления",
          JOptionPane.YES_NO_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        boolean deleted = artistRepository.deleteArtist(id);
        if (deleted) {
          outputArea.append("Исполнитель с ID " + id + " удален\n");
          deleteArtistIdField.setText("");
          loadArtists();
        } else {
          showError("Исполнитель с ID " + id + " не найден");
        }
      }
    } catch (NumberFormatException e) {
      showError("Введите корректный ID (число)");
    } catch (SQLException e) {
      showError("Ошибка при удалении: " + e.getMessage());
    }
  }

  private void executeQuery1() {
    try {
      List<String> songs = trackRepository.getSongDetails();
      outputArea.append("\n=== Результаты запроса 1 из ЛР1 ===\n");
      for (String song : songs) {
        outputArea.append(song + "\n");
      }
      outputArea.append("Всего композиций: " + songs.size() + "\n");
    } catch (SQLException e) {
      showError("Ошибка при выполнении запроса: " + e.getMessage());
    }
  }

  private void showError(String message) {
    outputArea.append("ОШИБКА: " + message + "\n");
  }
}