package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料情報登録完了outDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIR40outDto {

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
	// 結果コード
	private int resultCd;

}
