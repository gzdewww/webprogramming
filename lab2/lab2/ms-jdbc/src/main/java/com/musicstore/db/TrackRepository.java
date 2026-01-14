package com.musicstore.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackRepository {
  private Connection connection;

  public TrackRepository(Connection connection) {
    this.connection = connection;
  }

  // Запрос 1 из ЛР1: название композиции и длительность
  public List<String> getSongDetails() throws SQLException {
    List<String> results = new ArrayList<>();
    String query = "SELECT title || ', ' || duration AS \"Song Details\" FROM track";

    try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        results.add(rs.getString("Song Details"));
      }
    }
    return results;
  }

  // Запрос 2 из ЛР1: композиции вне диапазона 5-10 минут
  public List<String> getTracksOutsideRange(String minDuration, String maxDuration) throws SQLException {
    List<String> results = new ArrayList<>();
    String query = "SELECT title, duration FROM track " +
        "WHERE duration NOT BETWEEN ?::interval AND ?::interval " +
        "ORDER BY duration DESC";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, minDuration);
      stmt.setString(2, maxDuration);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getString("title") + ", " + rs.getString("duration"));
        }
      }
    }
    return results;
  }

  // Запрос 5 из ЛР1: альбомы исполнителя
  public List<String> getAlbumsByArtistName(String artistName) throws SQLException {
    List<String> results = new ArrayList<>();
    String query = "SELECT a.title FROM album a " +
        "JOIN artist ar ON a.artist_id = ar.artist_id " +
        "WHERE ar.name = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, artistName);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getString("title"));
        }
      }
    }
    return results;
  }

  // Добавление композиции с PreparedStatement (задание 2)
  public int addTrackWithCheck(String title, String duration, int albumId) throws SQLException {
    String query = "INSERT INTO track (title, duration, album_id) VALUES (?, ?::interval, ?) RETURNING track_id";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, title);
      stmt.setString(2, duration);
      stmt.setInt(3, albumId);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("track_id");
        }
      }
    }
    return -1;
  }
}