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
 * TBL003_会員テーブル
 */
@Entity
@Data
@Table(name = "MEMBER")
public class MemberEntity {
	@Id
	@Column(name = "MEMBER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_ID_generator")
	@SequenceGenerator(name = "MEMBER_ID_generator", sequenceName = "MEMBER_ID_seq", allocationSize = 1)
	private int memberId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "POSTALCODE")
	private String postalCode;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "TELNUMBER")
	private String telNumber;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "BIRTHDAY")
	private Date birthday;

	@Column(name = "JOIN_DATE")
	private Date joinDate;

	@Column(name = "WITHDRAWAL_DATE")
	private Date withdrawDate;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "MEMBER_CLASS")
	private String memberClass;

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
