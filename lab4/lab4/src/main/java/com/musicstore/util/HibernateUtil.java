package com.musicstore.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import com.musicstore.entity.*;

public class HibernateUtil {
  private static StandardServiceRegistry registry;
  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      try {
        registry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml") // Ищет в classpath
            .build();

        MetadataSources sources = new MetadataSources(registry);
        sources.addAnnotatedClass(Artist.class);
        sources.addAnnotatedClass(Album.class);
        sources.addAnnotatedClass(Track.class);

        Metadata metadata = sources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();

        System.out.println("SessionFactory создана успешно");

      } catch (Exception e) {
        System.err.println("Ошибка создания SessionFactory:");
        e.printStackTrace();
        if (registry != null) {
          StandardServiceRegistryBuilder.destroy(registry);
        }
        throw new ExceptionInInitializerError("Не удалось инициализировать Hibernate");
      }
    }
    return sessionFactory;
  }

  public static void shutdown() {
    if (sessionFactory != null && !sessionFactory.isClosed()) {
      sessionFactory.close();
      System.out.println("SessionFactory закрыта");
    }
    if (registry != null) {
      StandardServiceRegistryBuilder.destroy(registry);
    }
  }
}