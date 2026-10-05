package com.springboot.libraryPJ.ZZ.domain.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 */
@Entity
@Data
public class RentalMemberEntity {
	@Id
	@Column(name = "RENTAL_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int rentalId;

	@Column(name = "MEMBER_ID")
	private int memberId;

	@Column(name = "BOOK_ID")
	private int bookId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "ISBN")
	private String isbn;

	@Column(name = "RENTAL_DUE_DATE")
	private Date rentalDueDate;

	@Column(name = "OVER_DUE_DAYS")
	private int overDueDays;
}
