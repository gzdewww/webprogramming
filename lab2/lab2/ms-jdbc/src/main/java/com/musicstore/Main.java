package com.musicstore;

import com.musicstore.db.DatabaseConnection;
import com.musicstore.tasks.Task1Executor;
import com.musicstore.tasks.Task2Executor;
import com.musicstore.tasks.Task3Executor;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    Лабораторная работа №2: JDBC          ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // Проверяем подключение
        if (!DatabaseConnection.testConnection()) {
            System.err.println(" Не удалось подключиться к базе данных!");
            System.err.println("   Проверьте параметры в DatabaseConnection.java");
            System.err.println("   и убедитесь, что сервер PostgreSQL запущен.");
            return;
        }

        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Создаём одно подключение на всё приложение
            connection = DatabaseConnection.getConnection();
            System.out.println(" Подключение к базе данных установлено\n");

            boolean running = true;

            while (running) {
                clearConsole();

                System.out.println("╔══════════════════════════════════════════╗");
                System.out.println("║           ВЫБЕРИТЕ ЗАДАНИЕ               ║");
                System.out.println("╠══════════════════════════════════════════╣");
                System.out.println("║ 1. Задание 1: Базовые операции JDBC      ║");
                System.out.println("║ 2. Задание 2: PreparedStatement          ║");
                System.out.println("║ 3. Задание 3: Метаданные БД              ║");
                System.out.println("║ 4. Выполнить все задания                 ║");
                System.out.println("║ 0. Выход                                 ║");
                System.out.println("╚══════════════════════════════════════════╝");
                System.out.print("\nВаш выбор: ");

                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\n Введите число от 0 до 4");
                    waitForEnter(scanner);
                    continue;
                }

                switch (choice) {
                    case 1:
                        executeTask1(connection, scanner);
                        break;
                    case 2:
                        executeTask2(connection, scanner);
                        break;
                    case 3:
                        executeTask3(connection, scanner);
                        break;
                    case 4:
                        executeAllTasks(connection, scanner);
                        break;
                    case 0:
                        System.out.println("\nВыход из программы...");
                        running = false;
                        break;
                    default:
                        System.out.println("\n  Неверный выбор. Введите число от 0 до 4");
                        waitForEnter(scanner);
                }
            }

        } catch (Exception e) {
            System.err.println("\n  Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Закрываем подключение и сканер
            DatabaseConnection.closeConnection(connection);
            scanner.close();
            System.out.println("\nПрограмма завершена.");
        }
    }

    private static void executeTask1(Connection connection, Scanner scanner) {
        clearConsole();
        Task1Executor task1 = new Task1Executor(connection);
        task1.execute();
        waitForEnter(scanner);
    }

    private static void executeTask2(Connection connection, Scanner scanner) {
        clearConsole();
        Task2Executor task2 = new Task2Executor(connection);
        task2.execute();
        waitForEnter(scanner);
    }

    private static void executeTask3(Connection connection, Scanner scanner) {
        clearConsole();
        Task3Executor task3 = new Task3Executor(connection);
        task3.execute();
        waitForEnter(scanner);
    }

    private static void executeAllTasks(Connection connection, Scanner scanner) {
        clearConsole();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  ВЫПОЛНЕНИЕ ВСЕХ ЗАДАНИЙ ЛАБОРАТОРНОЙ    ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        executeTask1(connection, new Scanner(System.in)); // Временный сканер
        waitForEnter(scanner);

        clearConsole();
        executeTask2(connection, new Scanner(System.in));
        waitForEnter(scanner);

        clearConsole();
        executeTask3(connection, new Scanner(System.in));
        waitForEnter(scanner);

        System.out.println("\n Все задания лабораторной работы выполнены!");
    }

    private static void clearConsole() {
        // Выводим 50 пустых строк для "очистки" консоли
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.print("\n⏎ Нажмите Enter для возврата в меню...");
        scanner.nextLine();
    }
}