package com.musicstore.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetadataService {
  private Connection connection;

  public MetadataService(Connection connection) {
    this.connection = connection;
  }

  public List<String> getAllTables() throws SQLException {
    List<String> tables = new ArrayList<>();
    DatabaseMetaData metaData = connection.getMetaData();

    try (ResultSet rs = metaData.getTables(null, null, "%", new String[] { "TABLE" })) {
      while (rs.next()) {
        tables.add(rs.getString("TABLE_NAME"));
      }
    }
    return tables;
  }

  public List<String> getTableColumns(String tableName) throws SQLException {
    List<String> columns = new ArrayList<>();
    DatabaseMetaData metaData = connection.getMetaData();

    try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
      while (rs.next()) {
        String columnName = rs.getString("COLUMN_NAME");
        String columnType = rs.getString("TYPE_NAME");
        int columnSize = rs.getInt("COLUMN_SIZE");
        columns.add(String.format("%-20s %-15s (%d)", columnName, columnType, columnSize));
      }
    }
    return columns;
  }
}