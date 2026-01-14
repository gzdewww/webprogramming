package com.musicstore;

import com.musicstore.entity.*;
import com.musicstore.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №4: Hibernate ===");
        System.out.println("Цель: Демонстрация CRUD операций (Create, Read, Update, Delete)\n");

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            // === ЧАСТЬ 1: ИНИЦИАЛИЗАЦИЯ БАЗЫ ДАННЫХ ===
            System.out.println("1. СОЗДАНИЕ И ЗАПОЛНЕНИЕ ТАБЛИЦ (CREATE):");
            System.out.println("----------------------------------------");

            initializeDatabase(sessionFactory);

            // === ЧАСТЬ 2: ЧТЕНИЕ ДАННЫХ ===
            System.out.println("\n2. ВЫВОД ДАННЫХ (READ):");
            System.out.println("----------------------");

            displayAllData(sessionFactory);

            // === ЧАСТЬ 3: РЕДАКТИРОВАНИЕ ДАННЫХ ===
            System.out.println("\n3. РЕДАКТИРОВАНИЕ ДАННЫХ (UPDATE):");
            System.out.println("-----------------------------------");

            updateTrack(sessionFactory, "Thunder", "Thunder (Extended Mix)", "4:30");

            // === ЧАСТЬ 4: УДАЛЕНИЕ ДАННЫХ ===
            System.out.println("\n4. УДАЛЕНИЕ ДАННЫХ (DELETE):");
            System.out.println("-----------------------------");

            deleteArtist(sessionFactory, "Bond");

            // === ЧАСТЬ 5: ИТОГИ ===
            System.out.println("\n5. РЕЗУЛЬТАТЫ ПОСЛЕ ВСЕХ ОПЕРАЦИЙ:");
            System.out.println("----------------------------------");

            displayStatistics(sessionFactory);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
            System.out.println("\n=== Работа завершена ===");
        }
    }

    private static void initializeDatabase(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            // Создаём исполнителей (только тех, которые есть в изначальных данных)
            Artist benson = new Artist("Benson");
            Artist keller = new Artist("Keller");
            Artist bond = new Artist("Bond");
            Artist metallica = new Artist("Metallica");
            Artist imagineDragons = new Artist("Imagine Dragons");

            session.persist(benson);
            session.persist(keller);
            session.persist(bond);
            session.persist(metallica);
            session.persist(imagineDragons);

            // Создаём альбомы
            Album stringsAttached = new Album("Strings Attached", "Classical", benson);
            Album masterOfPuppets = new Album("Master of Puppets", "Metal", metallica);
            Album evolve = new Album("Evolve", "Alternative Rock", imagineDragons);

            session.persist(stringsAttached);
            session.persist(masterOfPuppets);
            session.persist(evolve);

            // Создаём треки
            Track t1 = new Track("Girl", Duration.ofMinutes(3).plusSeconds(40), stringsAttached);
            Track t2 = new Track("Symphony", Duration.ofMinutes(7).plusSeconds(30), stringsAttached);
            Track t3 = new Track("Battery", Duration.ofMinutes(5).plusSeconds(12), masterOfPuppets);
            Track t4 = new Track("Master of Puppets", Duration.ofMinutes(8).plusSeconds(36), masterOfPuppets);
            Track t5 = new Track("Believer", Duration.ofMinutes(3).plusSeconds(24), evolve);
            Track t6 = new Track("Thunder", Duration.ofMinutes(3).plusSeconds(7), evolve);
            Track t7 = new Track("Walking the Wire", Duration.ofMinutes(4).plusSeconds(30), evolve);

            session.persist(t1);
            session.persist(t2);
            session.persist(t3);
            session.persist(t4);
            session.persist(t5);
            session.persist(t6);
            session.persist(t7);

            tx.commit();
            System.out.println("✓ Создано: 5 исполнителей, 3 альбома, 7 треков");
        }
    }

    private static void displayAllData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            // Исполнители
            List<Artist> artists = session.createQuery(
                    "FROM Artist ORDER BY name", Artist.class).list();
            System.out.println("Исполнители:");
            for (Artist a : artists) {
                System.out.println("  • " + a.getName());
            }

            // Альбомы
            List<Album> albums = session.createQuery(
                    "FROM Album ORDER BY title", Album.class).list();
            System.out.println("\nАльбомы:");
            for (Album a : albums) {
                System.out.println("  • " + a.getTitle() + " (" + a.getGenre() +
                        ") - " + a.getArtist().getName());
            }

            // Треки
            List<Track> tracks = session.createQuery(
                    "FROM Track ORDER BY title", Track.class).list();
            System.out.println("\nТреки:");
            for (Track t : tracks) {
                System.out.println("  • " + t.getTitle() + " - " +
                        t.getAlbum().getTitle() + " (" + t.getDuration().toMinutes() +
                        ":" + String.format("%02d", t.getDuration().getSeconds() % 60) + ")");
            }
        }
    }

    private static void updateTrack(SessionFactory sessionFactory, String oldTitle, String newTitle,
            String newDuration) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Track track = session.createQuery(
                    "FROM Track WHERE title = :title", Track.class)
                    .setParameter("title", oldTitle)
                    .uniqueResult();

            if (track != null) {
                System.out.println("Найден трек: " + track.getTitle() +
                        " (ID: " + track.getId() + ")");

                track.setTitle(newTitle);
                // Парсим строку "MM:SS" в Duration
                String[] parts = newDuration.split(":");
                track.setDuration(java.time.Duration.ofMinutes(Long.parseLong(parts[0]))
                        .plusSeconds(Long.parseLong(parts[1])));

                session.merge(track);
                tx.commit();

                System.out.println("✓ Обновлено: '" + oldTitle + "' → '" + newTitle +
                        "' (" + newDuration + ")");
            } else {
                System.out.println("Трек '" + oldTitle + "' не найден");
            }
        }
    }

    private static void deleteArtist(SessionFactory sessionFactory, String artistName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Artist artist = session.createQuery(
                    "FROM Artist WHERE name = :name", Artist.class)
                    .setParameter("name", artistName)
                    .uniqueResult();

            if (artist != null) {
                System.out.println("Удаляем исполнителя: " + artist.getName() +
                        " (ID: " + artist.getId() + ")");
                System.out.println("Примечание: Каскадно удалятся все альбомы и треки");

                session.remove(artist);
                tx.commit();

                System.out.println("✓ Исполнитель '" + artistName + "' удален");
            } else {
                System.out.println("Исполнитель '" + artistName + "' не найден");
            }
        }
    }

    private static void displayStatistics(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            long artistCount = session.createQuery(
                    "SELECT COUNT(*) FROM Artist", Long.class).uniqueResult();

            long albumCount = session.createQuery(
                    "SELECT COUNT(*) FROM Album", Long.class).uniqueResult();

            long trackCount = session.createQuery(
                    "SELECT COUNT(*) FROM Track", Long.class).uniqueResult();

            System.out.println("Статистика:");
            System.out.println("  • Исполнителей: " + artistCount);
            System.out.println("  • Альбомов: " + albumCount);
            System.out.println("  • Треков: " + trackCount);

            // Покажем обновлённый трек
            Track updated = session.createQuery(
                    "FROM Track WHERE title LIKE '%Extended%'", Track.class)
                    .uniqueResult();

            if (updated != null) {
                System.out.println("\nПример обновлённого трека:");
                System.out.println("  • " + updated.getTitle());
                System.out.println("  • Альбом: " + updated.getAlbum().getTitle());
                System.out.println("  • Исполнитель: " + updated.getAlbum().getArtist().getName());
            }
        }
    }
}