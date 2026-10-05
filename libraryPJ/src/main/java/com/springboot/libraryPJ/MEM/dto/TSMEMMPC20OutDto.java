package com.springboot.libraryPJ.MEM.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * パスワード変更DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSMEMMPC20OutDto {

	// 会員ID
	private String memberId;

	// 名前
	private String name;

	// パスワード
	private String password;

	// レコード件数
	private int recordCnt;

	// 排他キー
	private long exclusiveKey;

}
