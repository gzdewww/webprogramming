package com.musicstore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "album")
public class Album {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "album_seq")
  @SequenceGenerator(name = "album_seq", sequenceName = "seq_album", allocationSize = 1)
  @Column(name = "album_id")
  private Long id;

  @Column(name = "title", nullable = false, length = 100)
  private String title;

  @Column(name = "genre", nullable = false, length = 50)
  private String genre;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "artist_id", nullable = false)
  private Artist artist;

  @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Track> tracks = new ArrayList<>();

  // Конструктор
  public Album() {
    // Пустой конструктор для Hibernate
  }

  public Album(String title, String genre, Artist artist) {
    this.title = title;
    this.genre = genre;
    this.artist = artist;
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

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public List<Track> getTracks() {
    return tracks;
  }

  public void setTracks(List<Track> tracks) {
    this.tracks = tracks;
  }
}