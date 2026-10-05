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
 * TBL004_貸出テーブル
 */
@Entity
@Data
@Table(name = "RENTAL")
public class RentalEntity {
	@Id
	@Column(name = "RENTAL_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RENTAL_ID_generator")
	@SequenceGenerator(name = "RENTAL_ID_generator", sequenceName = "RENTAL_ID_seq", allocationSize = 1)
	private int rentalId;

	@Column(name = "BOOK_ID")
	private int bookId;

	@Column(name = "MEMBER_ID")
	private int memberId;

	@Column(name = "RENTAL_DATE")
	private Date rentalDate;

	@Column(name = "RENTAL_DUE_DATE")
	private Date rentalDueDate;

	@Column(name = "RETURN_DATE")
	private Date returnDate;

	@Column(name = "REMIND_FLAG")
	private String remindFlag;

	@Column(name = "DELETE_FLAG")
	private String deleteFlag;

	@Column(name = "REGISTER_ID")
	private long registerId;

	@Column(name = "REGISTER_DATE")
	private Date registerDate;

	@Column(name = "UPDATE_ID")
	private long updateId;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "EXCLUSIVE_KEY")
	private long exclusiveKey;

}
