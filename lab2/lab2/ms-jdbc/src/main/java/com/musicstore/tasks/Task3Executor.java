package com.musicstore.tasks;

import com.musicstore.db.MetadataService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Task3Executor {
  private MetadataService metadataService;

  public Task3Executor(Connection connection) {
    this.metadataService = new MetadataService(connection);
  }

  public void execute() {
    System.out.println("\n" + "═".repeat(60));
    System.out.println("ЗАДАНИЕ 3: МЕТАДАННЫЕ БАЗЫ ДАННЫХ");
    System.out.println("═".repeat(60));

    try {
      System.out.println("\n1. Все таблицы базы данных:");
      System.out.println("-".repeat(40));
      displayAllTables();

      System.out.println("\n2. Столбцы таблицы 'artist':");
      System.out.println("-".repeat(40));
      displayTableColumns("artist");

      System.out.println("\n3. Столбцы таблицы 'album':");
      System.out.println("-".repeat(40));
      displayTableColumns("album");

      System.out.println("\n" + "═".repeat(60));
      System.out.println("ЗАДАНИЕ 3 ВЫПОЛНЕНО");
      System.out.println("═".repeat(60));

    } catch (SQLException e) {
      System.err.println("Ошибка при выполнении задания 3: " + e.getMessage());
    }
  }

  private void displayAllTables() throws SQLException {
    List<String> tables = metadataService.getAllTables();

    if (tables.isEmpty()) {
      System.out.println("В базе данных нет таблиц");
      return;
    }

    System.out.println("Имя таблицы");
    System.out.println("-".repeat(20));

    for (String table : tables) {
      System.out.println(table);
    }

    System.out.println("-".repeat(20));
    System.out.println("Всего таблиц: " + tables.size());
  }

  private void displayTableColumns(String tableName) throws SQLException {
    List<String> columns = metadataService.getTableColumns(tableName);

    if (columns.isEmpty()) {
      System.out.println("Таблица '" + tableName + "' не найдена или не имеет столбцов");
      return;
    }

    System.out.println("Имя столбца          Тип данных       (Размер)");
    System.out.println("-".repeat(50));

    for (String column : columns) {
      System.out.println(column);
    }

    System.out.println("-".repeat(50));
    System.out.println("Всего столбцов: " + columns.size());
  }
}