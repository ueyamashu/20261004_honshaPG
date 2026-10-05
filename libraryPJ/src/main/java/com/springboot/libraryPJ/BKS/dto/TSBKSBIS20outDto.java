package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料情報入庫画面用outDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIS20outDto {

	// ISBN番号
	private String isbn;
	// 資料名
	private String title;
	// 分類
	private String category;
	// 著者名
	private String author;
	// 出版社
	private String publisher;
	// 出版日
	private String releaseDate;
	// 資料の本数
	private int bookCount;
}
