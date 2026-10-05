package com.springboot.libraryPJ.RTN.dto;

import java.sql.Date;

import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料返却完了
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRTNRTB40outDto {
	// 名前
	private String name;

	// 資料名
	private String title;

	// 貸出期限
	private Date rentalDueDate;

	public TSRTNRTB40outDto(RentalMemberEntity rental) {
		this.name = rental.getName();
		this.title = rental.getTitle();
		this.rentalDueDate = rental.getRentalDueDate();
	}
	
}
