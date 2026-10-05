
package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料情報廃棄画面用DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIDinDto {

	// 資料ID
	private String bookId;

	// 廃棄年月日
	private String disposalDate;

	// 備考
	private String disposalNote;

	// 貸出フラグ
	private String rentalFlag;
}
