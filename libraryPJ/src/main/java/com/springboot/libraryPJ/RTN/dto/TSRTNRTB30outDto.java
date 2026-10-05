package com.springboot.libraryPJ.RTN.dto;

import java.sql.Date;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料返却確認
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRTNRTB30outDto {
	// 貸出ID
	private int rentalId;

	// 会員ID
	private int memberId;

	// 資料ID
	private int bookId;

	// 名前
	private String name;

	// 資料名
	private String title;

	// 貸出期限
	private Date rentalDueDate;

	// 貸出期限
	private int overDueDays;
	
	public TSRTNRTB30outDto(RentalMemberEntity rental) {
		this.rentalId = rental.getRentalId();
		this.memberId = rental.getMemberId();
		this.bookId = rental.getBookId();
		this.name = rental.getName();
		this.title = rental.getTitle();
		this.rentalDueDate = rental.getRentalDueDate();
		this.overDueDays = rental.getOverDueDays();
	}
}
