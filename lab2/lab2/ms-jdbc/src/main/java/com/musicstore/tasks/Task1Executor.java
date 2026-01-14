package com.musicstore.tasks;

import com.musicstore.db.ArtistRepository;
import com.musicstore.db.TrackRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Task1Executor {
  private Connection connection;
  private ArtistRepository artistRepository;
  private TrackRepository trackRepository;
  private Scanner scanner;

  public Task1Executor(Connection connection) {
    this.connection = connection;
    this.artistRepository = new ArtistRepository(connection);
    this.trackRepository = new TrackRepository(connection);
    this.scanner = new Scanner(System.in);
  }

  public void execute() {
    System.out.println("\n" + "═".repeat(60));
    System.out.println("ЗАДАНИЕ 1: БАЗОВЫЕ ОПЕРАЦИИ JDBC");
    System.out.println("═".repeat(60));

    try {
      System.out.println("\n1. Демонстрация запроса из ЛР1:");
      System.out.println("-".repeat(40));
      demonstrateLab1Query();

      System.out.println("\n2. CRUD операции с таблицей artist:");
      System.out.println("-".repeat(40));
      performCRUDOperations();

      System.out.println("\n" + "═".repeat(60));
      System.out.println("ЗАДАНИЕ 1 ВЫПОЛНЕНО");
      System.out.println("═".repeat(60));

    } catch (SQLException e) {
      System.err.println("Ошибка при выполнении задания 1: " + e.getMessage());
    }
  }

  private void demonstrateLab1Query() throws SQLException {
    List<String> songs = trackRepository.getSongDetails();
    System.out.println("Результаты запроса (Композиция, длительность):");
    System.out.println("-".repeat(50));

    for (String song : songs) {
      System.out.println(song);
    }

    System.out.println("-".repeat(50));
    System.out.println("Всего композиций: " + songs.size());
  }

  private void performCRUDOperations() throws SQLException {
    // Показываем текущих исполнителей
    System.out.println("\nТекущие исполнители:");
    displayArtists();

    // Добавление
    System.out.print("\nВведите имя нового исполнителя: ");
    String newArtistName = scanner.nextLine();

    int newArtistId = artistRepository.addArtist(newArtistName);
    if (newArtistId != -1) {
      System.out.println("✓ Добавлен исполнитель: " + newArtistName + " (ID: " + newArtistId + ")");
    }

    // Редактирование
    System.out.print("\nВведите ID исполнителя для редактирования: ");
    int artistIdToUpdate = Integer.parseInt(scanner.nextLine());

    System.out.print("Введите новое имя: ");
    String newName = scanner.nextLine();

    boolean updated = artistRepository.updateArtist(artistIdToUpdate, newName);
    if (updated) {
      System.out.println("✓ Исполнитель обновлен");
    } else {
      System.out.println("✗ Исполнитель не найден");
    }

    // Показываем после изменений
    System.out.println("\nИсполнители после изменений:");
    displayArtists();

    // Удаление
    System.out.print("\nВведите ID исполнителя для удаления: ");
    int artistIdToDelete = Integer.parseInt(scanner.nextLine());

    boolean deleted = artistRepository.deleteArtist(artistIdToDelete);
    if (deleted) {
      System.out.println("✓ Исполнитель удален");
    } else {
      System.out.println("✗ Исполнитель не найден");
    }

    // Финальный список
    System.out.println("\nФинальный список исполнителей:");
    displayArtists();
  }

  private void displayArtists() throws SQLException {
    List<String> artists = artistRepository.getAllArtists();
    System.out.println("ID\tName");
    System.out.println("-".repeat(30));

    for (String artist : artists) {
      System.out.println(artist);
    }
    System.out.println("-".repeat(30));
  }
}