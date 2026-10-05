package com.springboot.libraryPJ.ZZ.domain.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 */
@Entity
@Data
@Table(name = "RENTAL")
public class DelayEntity {
	@Id
	@Column(name = "RENTAL_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long rentalId;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "TELNUMBER")
	private String telNumber;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "RENTAL_DUE_DATE")
	private Date rentalDueDate;

	@Column(name = "REMIND_FLAG")
	private boolean remidFlag;

	@Column(name = "OVER_DUE_DAYS")
	private int overDueDays;

	public boolean getRemidFlag() {
		return remidFlag;
	}

}
