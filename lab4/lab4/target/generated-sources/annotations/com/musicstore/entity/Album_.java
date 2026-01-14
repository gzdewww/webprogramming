package com.musicstore.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Album.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Album_ {

	
	/**
	 * @see com.musicstore.entity.Album#artist
	 **/
	public static volatile SingularAttribute<Album, Artist> artist;
	
	/**
	 * @see com.musicstore.entity.Album#genre
	 **/
	public static volatile SingularAttribute<Album, String> genre;
	
	/**
	 * @see com.musicstore.entity.Album#id
	 **/
	public static volatile SingularAttribute<Album, Long> id;
	
	/**
	 * @see com.musicstore.entity.Album#title
	 **/
	public static volatile SingularAttribute<Album, String> title;
	
	/**
	 * @see com.musicstore.entity.Album
	 **/
	public static volatile EntityType<Album> class_;
	
	/**
	 * @see com.musicstore.entity.Album#tracks
	 **/
	public static volatile ListAttribute<Album, Track> tracks;

	public static final String ARTIST = "artist";
	public static final String GENRE = "genre";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String TRACKS = "tracks";

}

