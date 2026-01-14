package com.musicstore.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Artist.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Artist_ {

	
	/**
	 * @see com.musicstore.entity.Artist#albums
	 **/
	public static volatile ListAttribute<Artist, Album> albums;
	
	/**
	 * @see com.musicstore.entity.Artist#name
	 **/
	public static volatile SingularAttribute<Artist, String> name;
	
	/**
	 * @see com.musicstore.entity.Artist#id
	 **/
	public static volatile SingularAttribute<Artist, Long> id;
	
	/**
	 * @see com.musicstore.entity.Artist
	 **/
	public static volatile EntityType<Artist> class_;

	public static final String ALBUMS = "albums";
	public static final String NAME = "name";
	public static final String ID = "id";

}

