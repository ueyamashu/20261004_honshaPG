package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料一覧画面inDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBTBinDto {

	// 資料名
	private String bookTitle;

	// 著者名
	private String authorName;

	// 分類コード
	private String categoryCode;

	// ２次開発
	private String Isbn;

}
