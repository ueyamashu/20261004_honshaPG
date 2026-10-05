package com.springboot.libraryPJ.RTN.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料返却一覧画面
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRTNRTBinDto {

	// 会員ID
	private String memberId;

	// 資料ID
	private String bookId;

	// 削除フラグ
	private String deleteFlag;

	// 貸出ID
	private List<String> rentalIdList;
}
