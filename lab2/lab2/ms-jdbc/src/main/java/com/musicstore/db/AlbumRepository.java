package com.musicstore.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumRepository {
  private Connection connection;

  public AlbumRepository(Connection connection) {
    this.connection = connection;
  }

  public List<String> getAllAlbums() throws SQLException {
    List<String> albums = new ArrayList<>();
    String query = "SELECT album_id, title, genre FROM album ORDER BY album_id";

    try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        albums.add(rs.getInt("album_id") + "\t" +
            rs.getString("title") + "\t" +
            rs.getString("genre"));
      }
    }
    return albums;
  }

  public int addAlbum(String title, String genre, int artistId) throws SQLException {
    String query = "INSERT INTO album (title, genre, artist_id) VALUES (?, ?, ?) RETURNING album_id";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, title);
      stmt.setString(2, genre);
      stmt.setInt(3, artistId);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("album_id");
        }
      }
    }
    return -1;
  }

  public List<String> getAlbumsByGenre(String genre) throws SQLException {
    List<String> albums = new ArrayList<>();
    String query = "SELECT title, genre FROM album WHERE genre = ? ORDER BY title";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, genre);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          albums.add(rs.getString("title") + " (" + rs.getString("genre") + ")");
        }
      }
    }
    return albums;
  }
}