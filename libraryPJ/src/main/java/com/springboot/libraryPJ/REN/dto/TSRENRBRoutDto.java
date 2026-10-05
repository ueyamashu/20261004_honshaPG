package com.springboot.libraryPJ.REN.dto;

import java.sql.Date;

import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出確認出力DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRBRoutDto {
	// 取得結果
	private int resultCd;

	// エラーメッセージ
	private String errmsg;

	// 資料ID
	private int bookId;

	// 資料名
	private String title;

	// 出版日
	private Date releaseDate;

	// 返却期限
	private Date rentalDueDate;

	public TSRENRBRoutDto(BookLibraryEntity entity) {
		this.bookId = entity.getBookId();
		this.title = entity.getTitle();
		this.releaseDate = entity.getReleaseDate();
	}

	public TSRENRBRoutDto(RentalMemberEntity entity) {
		this.bookId = entity.getBookId();
		this.title = entity.getTitle();
		this.rentalDueDate = entity.getRentalDueDate();
	}
}
