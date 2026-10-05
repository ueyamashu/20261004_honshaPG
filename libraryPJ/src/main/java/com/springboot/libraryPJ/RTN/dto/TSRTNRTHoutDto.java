package com.springboot.libraryPJ.RTN.dto;

import java.sql.Date;

import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出返却履歴一覧画面
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRTNRTHoutDto {
	// 会員ID
	private int memberId;

	// 資料ID
	private int bookId;

	// 貸出日付
	private Date rentalDate;

	// 貸出期限
	private Date rentalDueDate;

	// 返却日
	private Date returnDate;

	public TSRTNRTHoutDto(RentalEntity rental) {
		this.memberId = rental.getMemberId();
		this.bookId = rental.getBookId();
		this.rentalDate = rental.getRentalDate();
		this.rentalDueDate = rental.getRentalDueDate();
		this.returnDate = rental.getReturnDate();
	}
}
