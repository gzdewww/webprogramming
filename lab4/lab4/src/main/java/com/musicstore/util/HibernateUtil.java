package com.musicstore.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.musicstore.entity.Album;
import com.musicstore.entity.Artist;
import com.musicstore.entity.Track;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            // Регистрируем entity классы
            configuration.addAnnotatedClass(Artist.class);
            configuration.addAnnotatedClass(Album.class);
            configuration.addAnnotatedClass(Track.class);
            
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        getSessionFactory().close();
    }
}