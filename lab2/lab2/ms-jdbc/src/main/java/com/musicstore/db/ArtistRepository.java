package com.musicstore.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepository {
  private Connection connection;

  public ArtistRepository(Connection connection) {
    this.connection = connection;
  }

  public List<String> getAllArtists() throws SQLException {
    List<String> artists = new ArrayList<>();
    String query = "SELECT artist_id, name FROM artist ORDER BY artist_id";

    try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        artists.add(rs.getInt("artist_id") + "\t" + rs.getString("name"));
      }
    }
    return artists;
  }

  public int addArtist(String name) throws SQLException {
    String query = "INSERT INTO artist (name) VALUES (?) RETURNING artist_id";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, name);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("artist_id");
        }
      }
    }
    return -1;
  }

  public boolean updateArtist(int artistId, String newName) throws SQLException {
    String query = "UPDATE artist SET name = ? WHERE artist_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, newName);
      stmt.setInt(2, artistId);

      return stmt.executeUpdate() > 0;
    }
  }

  public boolean deleteArtist(int artistId) throws SQLException {
    String query = "DELETE FROM artist WHERE artist_id = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setInt(1, artistId);
      return stmt.executeUpdate() > 0;
    }
  }
}