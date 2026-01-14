package com.musicstore;

import com.musicstore.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestConnection {
  public static void main(String[] args) {
    System.out.println("=== Тестирование подключения к Hibernate ===");

    // Инициализируем SessionFactory
    var sessionFactory = HibernateUtil.getSessionFactory();

    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();

      // Тестовый запрос (исправлен для Hibernate 6)
      Integer result = session.createNativeQuery("SELECT 1", Integer.class).getSingleResult();
      System.out.println("Подключение успешно! Результат теста: " + result);

      transaction.commit();
      System.out.println("Транзакция успешно завершена");

    } catch (Exception e) {
      System.err.println("Ошибка выполнения запроса:");
      e.printStackTrace();
    } finally {
      HibernateUtil.shutdown();
    }
  }
}