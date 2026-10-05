package com.springboot.libraryPJ.REN.dto;

import lombok.Data;

/**
 * 貸出資料入力DTO
 */
@Data
public class TSRENRBTinDto {

	// 会員ID
	private String memberId;

	// 資料ID
	private String bookId;

	// 資料名
	private String title;

	// 貸出ID
	private String rentalId;
}
