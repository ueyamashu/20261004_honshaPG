package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TSBKSBIRinDto30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIRinDto {

	// 資料ID
	private int bookId;
	// ISBN番号
	private String isbn;
	// 分類
	private String category;
	// 資料名
	private String title;
	// 著者名
	private String author;
	// 出版社
	private String publisher;
	// 入荷年月日
	private String arrival;
	// 出版日
	private String releaseDate;

}
