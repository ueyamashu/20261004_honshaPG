package com.springboot.libraryPJ.ZZ.domain.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * TBL001_資料テーブル
 */
@Entity
@Data
@Table(name = "BOOK")
public class BookEntity {
	@Id
	@Column(name = "ISBN", nullable = false)
	private String isbn;

	@Column(name = "CATEGORY", nullable = false)
	private String catgory;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "AUTHOR", nullable = false)
	private String author;

	@Column(name = "PUBLISHER", nullable = false)
	private String publisher;

	@Column(name = "RELEASE_DATE", nullable = false)
	private Date releaseDate;

	@Column(name = "DELETE_FLAG")
	private String deleteFlag = "0";

	@Column(name = "REGISTER_ID")
	private long registerId;

	@Column(name = "REGISTER_DATE")
	private Date registerDate = new Date(System.currentTimeMillis());

	@Column(name = "UPDATE_ID")
	private long updateId;

	@Column(name = "UPDATE_DATE")
	private Date updateDate = new Date(System.currentTimeMillis());

	@Column(name = "EXCLUSIVE_KEY")
	private long exclusiveKey = 0;

}
