package com.musicstore.tasks;

import com.musicstore.db.AlbumRepository;
import com.musicstore.db.ArtistRepository;
import com.musicstore.db.TrackRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Task2Executor {
  private Connection connection;
  private TrackRepository trackRepository;
  private ArtistRepository artistRepository;
  private AlbumRepository albumRepository;
  private Scanner scanner;

  public Task2Executor(Connection connection) {
    this.connection = connection;
    this.trackRepository = new TrackRepository(connection);
    this.artistRepository = new ArtistRepository(connection);
    this.albumRepository = new AlbumRepository(connection);
    this.scanner = new Scanner(System.in);
  }

  public void execute() {
    System.out.println("\n" + "═".repeat(60));
    System.out.println("ЗАДАНИЕ 2: PREPAREDSTATEMENT");
    System.out.println("═".repeat(60));

    try {
      System.out.println("\n1. Параметризованные запросы из ЛР1:");
      demonstratePreparedQueries();

      System.out.println("\n2. Добавление данных через PreparedStatement:");
      demonstratePreparedInserts();

      System.out.println("\n" + "═".repeat(60));
      System.out.println("ЗАДАНИЕ 2 ВЫПОЛНЕНО");
      System.out.println("═".repeat(60));

    } catch (SQLException e) {
      System.err.println("Ошибка при выполнении задания 2: " + e.getMessage());
    } catch (NumberFormatException e) {
      System.err.println("Ошибка формата числа: " + e.getMessage());
    }
  }

  private void demonstratePreparedQueries() throws SQLException {
    System.out.println("\nа) Композиции вне диапазона 5-10 минут:");
    List<String> tracks = trackRepository.getTracksOutsideRange("00:05:00", "00:10:00");
    displayList(tracks, "Нет композиций вне диапазона");

    System.out.println("\nб) Альбомы исполнителя 'Bond':");
    List<String> albums = trackRepository.getAlbumsByArtistName("Bond");
    displayList(albums, "Нет альбомов у исполнителя Bond");

    System.out.println("\nв) Альбомы в жанре 'Rock':");
    List<String> rockAlbums = albumRepository.getAlbumsByGenre("Rock");
    displayList(rockAlbums, "Нет альбомов в жанре Rock");
  }

  private void demonstratePreparedInserts() throws SQLException {
    System.out.println("\nа) Добавление нового исполнителя:");
    System.out.print("Введите имя исполнителя: ");
    String artistName = scanner.nextLine();

    int artistId = artistRepository.addArtist(artistName);
    if (artistId != -1) {
      System.out.println("✓ Добавлен исполнитель: " + artistName + " (ID: " + artistId + ")");
    }

    System.out.println("\nб) Добавление альбома:");
    System.out.print("Введите название альбома: ");
    String albumTitle = scanner.nextLine();

    System.out.print("Введите жанр: ");
    String genre = scanner.nextLine();

    int albumId = albumRepository.addAlbum(albumTitle, genre, artistId);
    if (albumId != -1) {
      System.out.println("✓ Добавлен альбом: " + albumTitle + " (ID: " + albumId + ")");
    }

    System.out.println("\nв) Добавление композиции:");
    System.out.print("Введите название композиции: ");
    String trackTitle = scanner.nextLine();

    System.out.print("Введите длительность (HH:MM:SS): ");
    String duration = scanner.nextLine();

    int trackId = trackRepository.addTrackWithCheck(trackTitle, duration, albumId);
    if (trackId != -1) {
      System.out.println("✓ Добавлена композиция: " + trackTitle + " (ID: " + trackId + ")");
    }
  }

  private void displayList(List<String> items, String emptyMessage) {
    if (items.isEmpty()) {
      System.out.println(emptyMessage);
    } else {
      for (String item : items) {
        System.out.println("  • " + item);
      }
    }
  }
}