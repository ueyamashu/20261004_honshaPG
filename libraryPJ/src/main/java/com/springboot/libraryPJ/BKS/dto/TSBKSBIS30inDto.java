package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料入庫画面用のinDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIS30inDto {

	// ISBN番号
	private String isbn;
	
	// 追加本数
	private int addBookCount;
	
	// 入荷年月日
	private String arrivalDate;
}
