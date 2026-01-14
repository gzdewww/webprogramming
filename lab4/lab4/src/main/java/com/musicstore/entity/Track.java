package com.musicstore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Duration;

@Entity
@Table(name = "track")
public class Track {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "track_seq")
  @SequenceGenerator(name = "track_seq", sequenceName = "seq_track", allocationSize = 1)
  @Column(name = "track_id")
  private Long id;

  @Column(name = "title", nullable = false, length = 100)
  private String title;

  // Используем встроенный маппинг для PostgreSQL INTERVAL
  @Column(name = "duration", columnDefinition = "INTERVAL")
  @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
  private Duration duration;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "album_id", nullable = false)
  private Album album;

  // Конструкторы
  public Track() {
    // Пустой конструктор для Hibernate
  }

  public Track(String title, Duration duration, Album album) {
    this.title = title;
    this.duration = duration;
    this.album = album;
  }

  // Геттеры и сеттеры
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public Album getAlbum() {
    return album;
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  @Override
  public String toString() {
    return "Track{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", duration=" + duration +
        ", album=" + (album != null ? album.getTitle() : "null") +
        '}';
  }
}