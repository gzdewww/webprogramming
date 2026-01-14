package com.musicstore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist")
public class Artist {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_seq")
  @SequenceGenerator(name = "artist_seq", sequenceName = "seq_artist", allocationSize = 1)
  @Column(name = "artist_id")
  private Long id;

  @Column(name = "name", nullable = false, unique = true, length = 100)
  private String name;

  @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Album> albums = new ArrayList<>();

  // Конструкторы
  public Artist() {
    // Пустой конструктор для Hibernate
  }

  public Artist(String name) {
    this.name = name;
  }

  // Геттеры и сеттеры
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Album> getAlbums() {
    return albums;
  }

  public void setAlbums(List<Album> albums) {
    this.albums = albums;
  }

  @Override
  public String toString() {
    return "Artist{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}