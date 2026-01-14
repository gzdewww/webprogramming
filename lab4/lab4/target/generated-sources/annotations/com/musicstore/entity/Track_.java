package com.musicstore.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Duration;

@StaticMetamodel(Track.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Track_ {

	
	/**
	 * @see com.musicstore.entity.Track#duration
	 **/
	public static volatile SingularAttribute<Track, Duration> duration;
	
	/**
	 * @see com.musicstore.entity.Track#album
	 **/
	public static volatile SingularAttribute<Track, Album> album;
	
	/**
	 * @see com.musicstore.entity.Track#id
	 **/
	public static volatile SingularAttribute<Track, Long> id;
	
	/**
	 * @see com.musicstore.entity.Track#title
	 **/
	public static volatile SingularAttribute<Track, String> title;
	
	/**
	 * @see com.musicstore.entity.Track
	 **/
	public static volatile EntityType<Track> class_;

	public static final String DURATION = "duration";
	public static final String ALBUM = "album";
	public static final String ID = "id";
	public static final String TITLE = "title";

}

