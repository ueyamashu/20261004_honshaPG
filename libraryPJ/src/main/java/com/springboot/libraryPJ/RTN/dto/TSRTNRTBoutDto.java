package com.springboot.libraryPJ.RTN.dto;

import java.sql.Date;

import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料返却一覧画面
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRTNRTBoutDto {

	// 貸出ID
	private int rentalId;

	// 資料ID
	private int bookId;

	// 会員ID
	private int memberId;

	// 貸出日付
	private Date rentalDate;

	// 貸出期限
	private Date rentalDueDate;

	public TSRTNRTBoutDto(RentalEntity rental) {
		this.rentalId = rental.getRentalId();
		this.bookId = rental.getBookId();
		this.memberId = rental.getMemberId();
		this.rentalDate = rental.getRentalDate();
		this.rentalDueDate = rental.getRentalDueDate();
	}
}
