package com.springboot.libraryPJ.ZZ.domain.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * TBL002_蔵書テーブル
 */
@Entity
@Data
@Table(name = "LIBRARY")
public class LibraryEntity {
	@Id
	@Column(name = "BOOK_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_ID_generator")
	@SequenceGenerator(name = "BOOK_ID_generator", sequenceName = "BOOK_ID_seq", allocationSize = 1)
	private int bookId;

	@Column(name = "ISBN", nullable = false)
	private String isbn;

	@Column(name = "ARRIVAL_DATE")
	private Date arrivalDate = new Date(System.currentTimeMillis());

	@Column(name = "DISPOSAL_DATE")
	private Date disposalDate;

	@Column(name = "DISPOSAL_NOTE")
	private String disposalNote;

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
