package com.springboot.libraryPJ.ZZ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出会員一覧
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRMTDto {

	// 会員ID
	private String memberId;

	// 氏名
	private String name;

}
