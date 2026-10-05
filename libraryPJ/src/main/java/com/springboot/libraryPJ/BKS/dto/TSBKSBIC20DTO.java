package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料情報更新用DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIC20DTO {
	// 資料ID
	private int bookId;
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
	// ISBN番号
	private String isbn;
	// 入荷年月日
	private String arrival;
	// 元ISBN番号を取得
	private String isbnOrigin;
	// 本情報の排他キー
	private int bookExclusiveKey;
	// 蔵書情報の排他キー
	private int libraryExclusiveKey;
}
