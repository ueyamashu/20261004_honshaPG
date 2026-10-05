package com.springboot.libraryPJ.ZZ.dto;

import java.util.List;

import com.springboot.libraryPJ.REN.dto.TSRENRBRoutDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出資料一覧
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRBTDto {

	// 会員ID
	private String memberId;

	// 資料ID
	private String bookId;

	// 資料名
	private String title;

    // 資料IDリスト
    private List<String> bookIdList;

    // 資料情報リスト
    private List<TSRENRBRoutDto> bookList;
}
