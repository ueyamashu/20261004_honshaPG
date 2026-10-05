package com.springboot.libraryPJ.RTN.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出返却履歴一覧画面
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRTNRTHinDto {
	// 会員ID
	private String memberId;

	// 資料ID
	private String bookId;

	private String deleteFlag;
}
