package com.springboot.libraryPJ.ZZ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログイン画面DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSCOMLOGDto {

	// 会員ID
	private String memberId;

	// パスワード
	private String password;

}
