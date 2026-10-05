package com.springboot.libraryPJ.ZZ.domain.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 */
// TODO add comment
@Entity
@Data
public class BookLibraryEntity {
	@Column(name = "ISBN")
	private String isbn;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "AUTHOR")
	private String author;

	@Column(name = "PUBLISHER")
	private String publisher;

	@Column(name = "RELEASE_DATE")
	private Date releaseDate;

	@Id
	@Column(name = "BOOK_ID")
	private int bookId;

	@Column(name = "ARRIVAL_DATE")
	private Date arrivalDate;

	@Column(name = "DISPOSAL_DATE")
	private Date disposalDate;

	@Column(name = "BOOK_EXCLUSIVE_KEY")
	private int bookExclusiveKey;

	@Column(name = "LIBRARY_EXCLUSIVE_KEY")
	private int libraryExclusiveKey;

}
