package com.springboot.libraryPJ.ZZ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出確認画面、貸出完了画面
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRBRDto {

	// 会員ID
	private String memberId;

	// 資料ID
	private String bookId;

	// 名前
	private String name;

	// 資料名
	private String title;

	// 返却期限
	private String rentalDueDate;

}
