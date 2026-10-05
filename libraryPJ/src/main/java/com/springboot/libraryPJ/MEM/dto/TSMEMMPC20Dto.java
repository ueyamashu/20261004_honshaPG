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
public class TSMEMMPC20Dto {

	// 会員ID
	private String memberId;

	// 名前
	private String name;

	// 現在のパスワード
	private String memberPassword;

	// 新しいパスワード
	private String memberPasswordNew;

	// 新しいパスワード(確認)
	private String memberPasswordNew2;

	// 排他キー
	private long exclusiveKey;

	// DBから取得したパスワード
	private String password;
}
