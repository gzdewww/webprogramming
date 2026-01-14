package com.musicstore.ui;

import com.musicstore.db.AlbumRepository;
import com.musicstore.db.ArtistRepository;
import com.musicstore.db.TrackRepository;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;

public class Task2Panel extends JPanel {
  private Connection connection;
  private TrackRepository trackRepository;
  private ArtistRepository artistRepository;
  private AlbumRepository albumRepository;

  private JComboBox<String> queryComboBox;
  private JPanel paramPanel;
  private JTextArea outputArea;

  public Task2Panel(Connection connection) {
    this.connection = connection;
    this.trackRepository = new TrackRepository(connection);
    this.artistRepository = new ArtistRepository(connection);
    this.albumRepository = new AlbumRepository(connection);
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout(10, 10));

    // Верхняя панель: выбор запроса
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.setBorder(BorderFactory.createTitledBorder("PreparedStatement запросы"));

    // Выбор запроса
    JPanel querySelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    querySelectPanel.add(new JLabel("Выберите запрос:"));

    String[] queries = {
        "Композиции вне диапазона 5-10 минут",
        "Альбомы исполнителя",
        "Альбомы по жанру"
    };

    queryComboBox = new JComboBox<>(queries);
    queryComboBox.addActionListener(e -> updateParameterPanel());
    querySelectPanel.add(queryComboBox);
    topPanel.add(querySelectPanel);

    // Панель для параметров (динамическая)
    paramPanel = new JPanel();
    paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
    topPanel.add(paramPanel);

    // Кнопка выполнения
    JButton executeButton = new JButton("Выполнить запрос");
    executeButton.addActionListener(e -> executeSelectedQuery());
    topPanel.add(executeButton);

    // Нижняя панель: вывод результатов
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBorder(BorderFactory.createTitledBorder("Результаты"));

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

    updateParameterPanel();
  }

  private void updateParameterPanel() {
    paramPanel.removeAll();
    int selectedIndex = queryComboBox.getSelectedIndex();

    switch (selectedIndex) {
      case 0: // Композиции вне диапазона
        paramPanel.add(new JLabel("Минимальная длительность (HH:MM:SS):"));
        JTextField minField = new JTextField("00:05:00", 10);
        paramPanel.add(minField);

        paramPanel.add(new JLabel("Максимальная длительность (HH:MM:SS):"));
        JTextField maxField = new JTextField("00:10:00", 10);
        paramPanel.add(maxField);
        break;

      case 1: // Альбомы исполнителя
        paramPanel.add(new JLabel("Имя исполнителя:"));
        JTextField artistField = new JTextField("Bond", 15);
        paramPanel.add(artistField);
        break;

      case 2: // Альбомы по жанру
        paramPanel.add(new JLabel("Жанр:"));
        JTextField genreField = new JTextField("Rock", 15);
        paramPanel.add(genreField);
        break;
    }

    paramPanel.revalidate();
    paramPanel.repaint();
  }

  private void executeSelectedQuery() {
    outputArea.append("\n" + java.time.LocalDateTime.now() + "\n");
    outputArea.append("Выполнение запроса: " + queryComboBox.getSelectedItem() + "\n");

    int selectedIndex = queryComboBox.getSelectedIndex();
    Component[] components = paramPanel.getComponents();

    try {
      switch (selectedIndex) {
        case 0: // Композиции вне диапазона
          String minDuration = ((JTextField) components[1]).getText();
          String maxDuration = ((JTextField) components[3]).getText();

          List<String> tracks = trackRepository.getTracksOutsideRange(minDuration, maxDuration);
          outputArea.append("Результаты (вне диапазона " + minDuration + " - " + maxDuration + "):\n");
          for (String track : tracks) {
            outputArea.append("  • " + track + "\n");
          }
          outputArea.append("Найдено: " + tracks.size() + " композиций\n");
          break;

        case 1: // Альбомы исполнителя
          String artistName = ((JTextField) components[1]).getText();
          List<String> albums = trackRepository.getAlbumsByArtistName(artistName);
          outputArea.append("Альбомы исполнителя '" + artistName + "':\n");
          for (String album : albums) {
            outputArea.append("  • " + album + "\n");
          }
          outputArea.append("Найдено: " + albums.size() + " альбомов\n");
          break;

        case 2: // Альбомы по жанру
          String genre = ((JTextField) components[1]).getText();
          List<String> genreAlbums = albumRepository.getAlbumsByGenre(genre);
          outputArea.append("Альбомы в жанре '" + genre + "':\n");
          for (String album : genreAlbums) {
            outputArea.append("  • " + album + "\n");
          }
          outputArea.append("Найдено: " + genreAlbums.size() + " альбомов\n");
          break;
      }
      outputArea.append("✓ Запрос выполнен успешно\n");
    } catch (SQLException e) {
      outputArea.append("  Ошибка: " + e.getMessage() + "\n");
    } catch (Exception e) {
      outputArea.append("  Ошибка выполнения: " + e.getMessage() + "\n");
    }
  }
}